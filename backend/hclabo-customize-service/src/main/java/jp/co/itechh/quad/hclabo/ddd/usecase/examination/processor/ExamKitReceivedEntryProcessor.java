package jp.co.itechh.quad.hclabo.ddd.usecase.examination.processor;

import jp.co.itechh.quad.hclabo.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.hclabo.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.hclabo.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.hclabo.core.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.hclabo.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.hclabo.core.base.utility.FileOperationUtility;
import jp.co.itechh.quad.hclabo.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.hclabo.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.hclabo.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.hclabo.core.dto.ExamKitCsvDto;
import jp.co.itechh.quad.hclabo.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.hclabo.ddd.exception.BaseException;
import jp.co.itechh.quad.hclabo.ddd.exception.DomainException;
import jp.co.itechh.quad.hclabo.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.hclabo.ddd.usecase.examination.service.ExamKitReceivedEntrySingleExecuter;
import jp.co.itechh.quad.ordersearch.presentation.api.OrderSearchApi;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchRegistUpdateRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 検査キット受領登録 CSVダウンロード Processor
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExamKitReceivedEntryProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExamKitReceivedEntryProcessor.class);

    /** MSGCD_EXAMKIT_RECEIVED_ERROR */
    private static final String MSGCD_EXAMKIT_RECEIVED_UNEXPECTED_ERROR = "ORDER-EXAMKIT05-E";

    /** 検査キット受領登録処理以外で例外が発生した場合 */
    private static final String MSG_ERROR = "予期せぬエラーが発生しました。処理を中断し終了します。";

    /** CSV読み込みのモジュール */
    private final CsvReaderModule csvReaderModule;

    /** 受注検索API */
    private final OrderSearchApi orderSearchApi;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** ExamKitReceivedEntrySingleExecuter */
    private final ExamKitReceivedEntrySingleExecuter examKitReceivedEntrySingleExecuter;

    /** コンストラクタ */
    @Autowired
    public ExamKitReceivedEntryProcessor(CsvReaderModule csvReaderModule,
                                         OrderSearchApi orderSearchApi,
                                         INotificationSubAdapter notificationSubAdapter,
                                         ExamKitReceivedEntrySingleExecuter examKitReceivedEntrySingleExecuter) {
        this.csvReaderModule = csvReaderModule;
        this.orderSearchApi = orderSearchApi;
        this.notificationSubAdapter = notificationSubAdapter;
        this.examKitReceivedEntrySingleExecuter = examKitReceivedEntrySingleExecuter;
    }

    /**
     * Consumerメソッド <br/>
     *
     * @param batchQueueMessage キューメッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws Exception {
        BatchLogging batchLogging = new BatchLogging();
        batchLogging.setBatchId(HTypeBatchName.BATCH_EXAMKIT_RECEIVED_ENTRY.getValue());
        batchLogging.setBatchName(HTypeBatchName.BATCH_EXAMKIT_RECEIVED_ENTRY.getLabel());
        batchLogging.setStartType(Objects.requireNonNull(
                EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
            .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("検査キット受領登録バッチ開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();
        // 管理者宛メールエラー報告メッセージ
        StringBuilder errorMailMessage = new StringBuilder();
        // 実行件数
        int resultCount = 0;
        // エラー件数
        int errorCount = 0;

        try {
            List<ExamKitCsvDto> examKitCsvDtoList = createTargetDtoListForTmpFile(batchQueueMessage.getFilePath());

            Set<String> orderReceivedIdSet = new HashSet<>();

            if (!CollectionUtils.isEmpty(examKitCsvDtoList)) {
                // 1. 検査キット受領登録処理
                for (ExamKitCsvDto examKitCsvDto : examKitCsvDtoList) {
                    String examKitCode = examKitCsvDto.getExamKitCode();
                    try {
                        // 1.1 ~ 1.4 (検査キット情報取得 -> 受注情報取得 -> 注文状況チェック -> 検査状態の更新)
                        String orderReceivedId = this.examKitReceivedEntrySingleExecuter.execute(examKitCode);
                        // 1.5. 受注IDリスト追加
                        orderReceivedIdSet.add(orderReceivedId);
                        resultCount++;
                    } catch (BaseException e) {
                        // 検査キット受領処理実行時に、BaseExceptionを継承したException（業務エラーなど）が発生した場合はこちらでキャッチ
                        writeLog(examKitCode, e);
                        e.getMessageMap().forEach((fieldName, exceptionContentList) -> {
                            for (ExceptionContent exceptionContent : exceptionContentList) {
                                settingErrorMailMessage(
                                    examKitCode, exceptionContent.getMessage(), errorMailMessage);
                            }
                        });
                        errorCount++;
                    } catch (Exception e) {
                        settingErrorMailMessage("",
                            AppLevelFacesMessageUtil.getAllMessage(MSGCD_EXAMKIT_RECEIVED_UNEXPECTED_ERROR,
                                    new Object[] {e.getMessage()}
                                )
                                .getMessage(), errorMailMessage
                        );
                        errorCount++;
                    }
                }

                // 2. 受注情報更新処理
                for (String orderReceivedId : orderReceivedIdSet) {
                    try {
                        OrderSearchRegistUpdateRequest orderSearchRegistUpdateRequest = new OrderSearchRegistUpdateRequest();
                        orderSearchRegistUpdateRequest.setOrderReceivedId(orderReceivedId);
                        orderSearchApi.registUpdate(orderSearchRegistUpdateRequest);
                    } catch (HttpClientErrorException | HttpServerErrorException e) {
                        settingErrorMailMessage("",
                            AppLevelFacesMessageUtil.getAllMessage("ORDER-EXAMKIT07-E",
                                    new Object[] {orderReceivedId, e.getMessage()}
                                )
                                .getMessage(), errorMailMessage
                        );
                        errorCount++;
                    }
                }
            } else {
                // 対象データ0件の場合
                reportString.append("CSVファイルを読み込みましたが対象件数が0件のため、検査キット受領登録バッチは実行されていません。").append("\r\n");
            }

            // 結果レポート設定
            if (resultCount > 0) {
                reportString.append(resultCount + "件の処理が終了しました。").append("\r\n");
            } else {
                LOGGER.info("メール送信対象の受注がありません。");
            }
            if (errorCount > 0) {
                reportString.append(errorCount + "件のエラーが発生しました。").append("\r\n");
            }

            // エラーがある場合、管理者宛エラーメール送付
            if (errorMailMessage.length() > 0) {
                sendAsyncAdministratorErrorMail(errorMailMessage);
            }

            // バッチログ設定
            batchLogging.setProcessCount(resultCount + errorCount);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            // 一時ファイルを削除
            deleteTmpFile(batchQueueMessage.getFilePath());

        } catch (Exception e) {
            // 検査キット受領登録処理以外で例外が発生した場合

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
            if (CollectionUtils.isNotEmpty(errorMessageList)) {
                for (String em : errorMessageList) {
                    reportString.append(getCommonErrorMessage(em));
                    errorMailMessage.append(getCommonErrorMessage(em));
                    LOGGER.info("[" + ExamKitReceivedEntryProcessor.class + "] " + getCommonErrorMessage(em));
                }
            } else {
                reportString.append(getCommonErrorMessage(e));
                errorMailMessage.append(getCommonErrorMessage(e));
                LOGGER.info("[" + ExamKitReceivedEntryProcessor.class + "] " + getCommonErrorMessage(e));
            }

            // バッチログ設定
            batchLogging.setProcessCount(0);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            // リトライ失敗の場合は管理者にメール送信
            sendAsyncAdministratorErrorMail(errorMailMessage);
            // 一時ファイルを削除
            deleteTmpFile(batchQueueMessage.getFilePath());

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.setReport(reportString);
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("検査キット受領登録バッチ終了");
        }
    }

    /**
     * アップロードファイルからDtoリストを作成
     *
     * @param filePath ファイルパス
     * @return List<ExamKitCsvDto> DtoList
     */
    private List<ExamKitCsvDto> createTargetDtoListForTmpFile(String filePath) {

        if (StringUtils.isBlank(filePath)) {
            // ファイルパスが存在しない場合
            throw new DomainException("ORDER-EXAMKIT06-E");
        }

        List<Integer> recordCount = new ArrayList<>();
        List<ExamKitCsvDto> examKitCsvDtoList = new ArrayList<>();
        CsvValidationResult result = this.csvReaderModule.validateCSV(new File(filePath), recordCount,
            CsvUploadResult.CSV_UPLOAD_VALID_LIMIT,
            examKitCsvDtoList, ExamKitCsvDto.class
        );

        if (!result.isValid()) {
            // バリデーションエラーの場合
            throw new DomainException("ORDER-EXAMKIT06-E");
        }
        return examKitCsvDtoList;
    }

    /**
     * 管理者用エラーメールのメッセージをセット
     *
     * @param examKitCode      検査キット番号
     * @param message          エラーメッセージ内容
     * @param errorMailMessage 送信用エラーメッセージ内容
     */
    private void settingErrorMailMessage(String examKitCode, String message, StringBuilder errorMailMessage) {
        if (StringUtils.isBlank(examKitCode)) {
            // メッセージが存在しない場合
            errorMailMessage.append(
                AppLevelFacesMessageUtil.getAllMessage(MSGCD_EXAMKIT_RECEIVED_UNEXPECTED_ERROR, new Object[] {message})
                    .getMessage()).append("\r\n");
        } else if (StringUtils.isNotBlank(message) && message.contains(examKitCode)) {
            // 渡されたメッセージに受注番号が含まれている場合
            errorMailMessage.append(message).append("\r\n");
        }
    }

    /**
     * 管理者にエラーメールを非同期送信
     *
     * @param errorMailMessage エラーメッセージ内容
     */
    private void sendAsyncAdministratorErrorMail(StringBuilder errorMailMessage) {
        this.notificationSubAdapter.examkitReceivedEntry(errorMailMessage.toString());
        LOGGER.info("管理者へ通知メールを送信しました。");
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

    /**
     * エラーのログを出力
     *
     * @param examKitCode 検査キット番号
     * @param e           Exception情報
     */
    private void writeLog(String examKitCode, Throwable e) {
        LOGGER.error("検査キット受領登録処理を実行中にエラーが発生しました。 -- 検査キット番号：" + examKitCode, e);
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
}
