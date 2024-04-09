/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.ddd.usecase.examination.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.hclabo.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.hclabo.core.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.hclabo.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.hclabo.core.base.utility.FileOperationUtility;
import jp.co.itechh.quad.hclabo.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.hclabo.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeExamResultsUploadType;
import jp.co.itechh.quad.hclabo.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.hclabo.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.analytics.adapter.IOrderSearchAdapter;
import jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.hclabo.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.hclabo.ddd.exception.BaseException;
import jp.co.itechh.quad.hclabo.ddd.exception.DomainException;
import jp.co.itechh.quad.hclabo.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dto.ExamResultsCsvDto;
import jp.co.itechh.quad.hclabo.ddd.usecase.examination.service.ExamResultsCsvSingleExecuter;
import jp.co.itechh.quad.hclabo.ddd.usecase.examination.service.ExamResultsEntryMessageDto;
import jp.co.itechh.quad.hclabo.ddd.usecase.examination.service.ExamResultsPdfSingleExecuter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 検査結果登録実行クラス Consumer
 *
 */
@Component
@Scope("prototype")
public class ExamResultsEntryProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExamResultsEntryProcessor.class);


    /** 検査結果登録実行前段階でエラーが発生した場合 */
    public static final String MSG_ERROR = "予期せぬエラーが発生しました。処理を中断し終了します。";

    /** 検査結果CSV登録処理 */
    private final ExamResultsCsvSingleExecuter examResultsCsvSingleExecuter;

    /** 検査結果PDFアップロード処理  */
    private final ExamResultsPdfSingleExecuter examResultsPdfSingleExecuter;

    /** CSV読み込みのユーティリティ */
    private final CsvReaderModule csvReaderModule;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** 受注検索アダプター */
    private final IOrderSearchAdapter orderSearchAdapter;

    /** コンストラクタ */
    @Autowired
    public ExamResultsEntryProcessor(ExamResultsCsvSingleExecuter examResultsCsvSingleExecuter,
                                     ExamResultsPdfSingleExecuter examResultsPdfSingleExecuter,
                                     CsvReaderModule csvReaderModule,
                                     INotificationSubAdapter notificationSubAdapter,
                                     IOrderSearchAdapter orderSearchAdapter) {
        this.examResultsCsvSingleExecuter = examResultsCsvSingleExecuter;
        this.examResultsPdfSingleExecuter = examResultsPdfSingleExecuter;
        this.csvReaderModule = csvReaderModule;
        this.notificationSubAdapter = notificationSubAdapter;
        this.orderSearchAdapter = orderSearchAdapter;
    }

    /**
     * Consumerメソッド <br/>
     * ※検査結果を登録する
     *
     * @param batchQueueMessage キューメッセージ
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws JsonProcessingException {

        BatchLogging batchLogging = new BatchLogging();
        batchLogging.setBatchId(HTypeBatchName.BATCH_EXAM_RESULTS_ENTRY.getValue());
        batchLogging.setBatchName(HTypeBatchName.BATCH_EXAM_RESULTS_ENTRY.getLabel());
        batchLogging.setStartType(Objects.requireNonNull(EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("検査結果登録バッチ開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();
        // 管理者宛メールエラー報告メッセージ
        StringBuilder errorMailMessage = new StringBuilder();

        // バッチのジョッブ情報取得
        String filePath = batchQueueMessage.getFilePath();
        String uploadType = batchQueueMessage.getUploadType();

        // 実行件数
        Integer resultCount = 0;
        // エラー件数
        Integer errorCount = 0;

        try {
            ExamResultsEntryMessageDto examResultsEntryMessageDto = new ExamResultsEntryMessageDto();

            if (HTypeExamResultsUploadType.CSV.getValue().equals(uploadType)) {

                // バッチのジョブ情報取得
                List<ExamResultsCsvDto> examResultCsvDtoList = createTargetDtoListForTmpFile(filePath);

                // 検査結果単位のループを行う
                if (!CollectionUtils.isEmpty(examResultCsvDtoList)) {
                    // 検査キット番号でbreak処理
                    Map<String, List<ExamResultsCsvDto>> examResultsCsvDtoGroupMap = groupExamResultsByExamKitCode(examResultCsvDtoList);

                    for (List<ExamResultsCsvDto> examResultsGroup : examResultsCsvDtoGroupMap.values()) {
                        try {
                            examResultsCsvSingleExecuter.execute(examResultsGroup, examResultsEntryMessageDto);
                            resultCount += examResultsGroup.size();
                        } catch (Exception e) {
                            if (e instanceof DomainException) {
                                ((BaseException) e).getMessageMap().forEach((fieldName, exceptionContentList) -> {
                                    for (ExceptionContent exceptionContent : exceptionContentList) {
                                        toExamResultsRegisterMessageDto(exceptionContent.getMessage(), examResultsEntryMessageDto);
                                    }
                                });
                                errorCount += examResultsGroup.size();
                            }
                        }
                    }

                } else {
                    // データが存在しない場合
                    reportString.append("CSVファイルを読み込みましたが対象件数が0件のため、検査結果登録バッチは実行されていません。").append("\r\n");
                }
                if (!CollectionUtils.isEmpty(examResultsEntryMessageDto.getOrderReceivedList())) {
                    sendExamResultsNotificationMail(examResultsEntryMessageDto.getOrderReceivedList());
                }

                // 一時ファイルを削除
                deleteTmpFile(filePath);
            } else if (HTypeExamResultsUploadType.PDF.getValue().equals(uploadType)) {

                examResultsPdfSingleExecuter.execute(filePath, examResultsEntryMessageDto);

                resultCount = examResultsEntryMessageDto.getResultCount();
                errorCount = examResultsEntryMessageDto.getErrCount();

            } else {
                LOGGER.info("検査結果登録対象のアップロード種別がありません。");
            }

            // エラーがある場合、管理者宛エラーメール送付
            if (ObjectUtils.isNotEmpty(examResultsEntryMessageDto.getErrMessage())) {
                sendAsyncAdministratorErrorMail(examResultsEntryMessageDto.getErrMessage());
            }

            asyncAfterProcess(examResultsEntryMessageDto);

            // 結果レポート設定
            if (resultCount > 0) {
                reportString.append(resultCount + "件の処理が終了しました。").append("\r\n");
            } else {
                LOGGER.info("メール送信対象の検査結果がありません。");
            }
            if (errorCount > 0) {
                reportString.append(errorCount + "件のエラーが発生しました。").append("\r\n");
            }

            if (resultCount + errorCount == 0) {
                reportString.append("検査結果がありません。").append("\r\n");
            }

            // バッチログ設定
            batchLogging.setProcessCount(resultCount + errorCount);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

        } catch (Exception e) {
            // 検査結果登録の実処理外で想定外エラーが発生した場合 (例：アップロードファイルの読込など

            // 業務エラーの場合はメッセージリストを作成
            List<String> errorMessageList = new ArrayList<>();
            if (e instanceof BaseException) {
                ((BaseException) e).getMessageMap().forEach((fieldName, exceptionContentList) -> {
                    for (ExceptionContent exceptionContent : exceptionContentList) {
                        errorMessageList.add(exceptionContent.getMessage());
                    }
                });
            }

            // 出力情報の設定
            if (!CollectionUtils.isEmpty(errorMessageList)) {
                for (String em : errorMessageList) {
                    reportString.append(getCommonErrorMessage(em));
                    errorMailMessage.append(getCommonErrorMessage(em));
                    LOGGER.info("[" + ExamResultsEntryProcessor.class + "] " + getCommonErrorMessage(em));
                }
            } else {
                reportString.append(getCommonErrorMessage(e));
                errorMailMessage.append(getCommonErrorMessage(e));
                LOGGER.info("[" + ExamResultsEntryProcessor.class + "] " + getCommonErrorMessage(e));
            }

            // バッチログ設定
            batchLogging.setProcessCount(0);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            // リトライ失敗の場合は管理者にメール送信
            sendAsyncAdministratorErrorMail(errorMailMessage);
            // 一時ファイルを削除
            deleteTmpFile(filePath);

            throw e;

        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.setReport(reportString);
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("検査結果登録バッチ終了");
        }

    }

    /**
     * アップロードファイルからDtoリストを作成
     *
     * @param filePath ファイルパス
     * @return List<ExamResultCsvDto> DtoList
     */
    private List<ExamResultsCsvDto> createTargetDtoListForTmpFile(String filePath) {

        if (StringUtils.isBlank(filePath)) {
            // ファイルパスが存在しない場合
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0006-E");
        }

        List<Integer> recordCount = new ArrayList<>();
        List<ExamResultsCsvDto> examResultCsvDtoList = new ArrayList<>();
        CsvValidationResult result = this.csvReaderModule.validateCSV(new File(filePath), recordCount,
                                                                    CsvUploadResult.CSV_UPLOAD_VALID_LIMIT,
                                                                    examResultCsvDtoList, ExamResultsCsvDto.class
                                                                   );

        if (!result.isValid()) {
            // バリデーションエラーの場合
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0006-E");
        }
        return examResultCsvDtoList;
    }
    /**
     * 検査キット番号でbreak処理
     * ※ 検査キット番号順が前提
     *
     * @param examResultCsvDtoList
     * @return 検査結果のグループ
     */
    private Map<String, List<ExamResultsCsvDto>> groupExamResultsByExamKitCode(List<ExamResultsCsvDto> examResultCsvDtoList) {
        Set<String> examKitCodeGroup = new HashSet<>();
        Map<String, List<ExamResultsCsvDto>> examResultsCsvDtoGroupMap = new HashMap<>();

        examResultCsvDtoList.forEach(examResultsCsvDto -> {
            if (examKitCodeGroup.contains(examResultsCsvDto.getExamKitCode())) {
                List<ExamResultsCsvDto> additionValue = new ArrayList<>(examResultsCsvDtoGroupMap.get(examResultsCsvDto.getExamKitCode()));
                additionValue.add(examResultsCsvDto);

                examResultsCsvDtoGroupMap.replace(examResultsCsvDto.getExamKitCode(), additionValue);
            } else {
                examResultsCsvDtoGroupMap.put(examResultsCsvDto.getExamKitCode(), Collections.singletonList(examResultsCsvDto));
                examKitCodeGroup.add(examResultsCsvDto.getExamKitCode());
            }
        });

        return examResultsCsvDtoGroupMap;
    }

    /**
     * 検査結果登録実行メッセージDtoに変換
     *
     * @param message
     * @param messageDto
     */
    private void toExamResultsRegisterMessageDto(String message, ExamResultsEntryMessageDto messageDto) {
        if (messageDto.getErrMessage() == null) {
            messageDto.setErrMessage(new StringBuilder());
        }

        if (messageDto.getErrMessage().length() > 0) {
            messageDto.getErrMessage().append("\r\n");
        }

        messageDto.getErrMessage().append(message);
    }

    /**
     * エラー発生時の共通メッセージgetter
     *
     * @param e Exception情報
     * @return 共通エラーメッセージ
     */
    private String getCommonErrorMessage(Throwable e) {
        return MSG_ERROR + (StringUtils.isBlank(e.getMessage()) ? "" : " 情報：" + e.getMessage());
    }

    /**
     * エラー発生時の共通メッセージgetter
     *
     * @param message エラーメッセージ内容
     * @return 共通エラーメッセージ
     */
    private String getCommonErrorMessage(String message) {
        return MSG_ERROR + " 情報：" + message;
    }

    /**
     * 検査結果メール送信
     *
     * @param orderReceivedList
     */
    private void sendExamResultsNotificationMail(List<OrderReceived> orderReceivedList) {

        List<String> mailOrderCodeList = orderReceivedList.stream().map(OrderReceived::getOrderCode)
                                        .collect(Collectors.toList());

        notificationSubAdapter.examResultsNotice(mailOrderCodeList);
    }

    /**
     * 管理者にエラーメールを非同期送信
     *
     * @param errorMailMessage エラーメッセージ内容
     */
    private void sendAsyncAdministratorErrorMail( StringBuilder errorMailMessage) {
        this.notificationSubAdapter.examResultsEntry(errorMailMessage.toString());
        LOGGER.info("管理者へ通知メールを送信しました。");
    }

    /**
     * 処理実行後非同期処理
     *
     * @param examResultsEntryMessageDto
     */
    private void asyncAfterProcess(ExamResultsEntryMessageDto examResultsEntryMessageDto) {
        if (!CollectionUtils.isEmpty(examResultsEntryMessageDto.getOrderReceivedList())) {

            examResultsEntryMessageDto.getOrderReceivedList().forEach(orderReceived -> {
                // 受注情報更新処理
                orderSearchAdapter.registUpdateOrderSearch(orderReceived.getOrderReceivedId());
            });
        }

    }

    /**
     * 一時ファイルを削除
     *
     * @param filePath ファイルパス
     */
    private void deleteTmpFile(String filePath) {
        // ファイルパスが存在する場合は、一時ファイルを削除する
        if (StringUtils.isNotBlank(filePath)) {
            // ファイル操作Helper取得
            FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
            try {
                // アップロードされた一時ファイルを削除する
                fileOperationUtility.remove(filePath);
            } catch (IOException e) {
                // ファイルの削除に失敗した場合
                LOGGER.error("アップロードされた一時ファイルの削除に失敗しました。", e);
            }
        }
    }

}