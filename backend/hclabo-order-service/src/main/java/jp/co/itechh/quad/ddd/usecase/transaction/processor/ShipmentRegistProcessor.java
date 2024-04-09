package jp.co.itechh.quad.ddd.usecase.transaction.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.addressbook.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.core.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.core.base.util.csvupload.CsvUploadResult;
import jp.co.itechh.quad.core.base.util.csvupload.CsvValidationResult;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.FileOperationUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.dto.order.ShipmentCsvDto;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.ddd.exception.BaseException;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.usecase.transaction.service.ShipmentRegistSingleExecuter;
import jp.co.itechh.quad.ddd.usecase.transaction.service.ShipmentRegisterMessageDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 出荷登録実行クラス Consumer
 *
 * @author kimura
 */
@Component
@Scope("prototype")
public class ShipmentRegistProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ShipmentRegistProcessor.class);

    /** オーソリ通信（売上計上）が失敗した場合 */
    public static final String MSGCD_SALES_AUTHORI_ERROR = "SOO002208E";

    /** 出荷登録実行前段階でエラーが発生した場合 */
    public static final String MSG_ERROR = "予期せぬエラーが発生しました。処理を中断し終了します。";

    /** 出荷登録で想定外エラーが発生した場合 */
    public static final String MSGCD_SHIPMENT_ERROR = "ORDER-SHIPPING07-E";

    /** 出荷登録実行クラス */
    private final ShipmentRegistSingleExecuter shipmentRegister;

    /** CSV読み込みのモジュール */
    private final CsvReaderModule csvReaderModule;

    /** 通知アダプター */
    private final INotificationSubAdapter notificationSubAdapter;

    /** コンストラクタ */
    @Autowired
    public ShipmentRegistProcessor(ShipmentRegistSingleExecuter shipmentRegister,
                                   CsvReaderModule csvReaderModule,
                                   INotificationSubAdapter notificationSubAdapter) {
        this.shipmentRegister = shipmentRegister;
        this.csvReaderModule = csvReaderModule;
        this.notificationSubAdapter = notificationSubAdapter;
    }

    /**
     * Consumerメソッド <br/>
     * 取引の出荷実績を登録する
     *
     * @param batchQueueMessage キューメッセージ
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws JsonProcessingException {

        BatchLogging batchLogging = new BatchLogging();
        batchLogging.setBatchId(HTypeBatchName.BATCH_SHIPMENT_REGIST.getValue());
        batchLogging.setBatchName(HTypeBatchName.BATCH_SHIPMENT_REGIST.getLabel());
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("出荷登録バッチ開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();
        // 管理者宛メールエラー報告メッセージ
        StringBuilder errorMailMessage = new StringBuilder();
        // 実行件数
        int resultCount = 0;
        // エラー件数
        int errorCount = 0;

        try {
            // バッチのジョブ情報取得
            Integer administratorSeq = batchQueueMessage.getAdministratorSeq();
            List<ShipmentCsvDto> shipmentCsvDtoList = createTargetDtoListForTmpFile(batchQueueMessage.getFilePath());

            // 取引単位のループを行う
            if (!CollectionUtils.isEmpty(shipmentCsvDtoList)) {
                for (ShipmentCsvDto dto : shipmentCsvDtoList) {
                    ShipmentRegisterMessageDto messageDto = null;
                    try {
                        // 実行
                        messageDto = this.shipmentRegister.execute(dto.getOrderCode(), dto.getDeliveryCode(),
                                                                   dto.getShipmentDate(), administratorSeq
                                                                  );
                        shipmentRegister.asyncAfterProcess(messageDto);

                        // エラーの場合
                        if (StringUtils.isNotBlank(messageDto.getErrCode())) {

                            // 請求決済エラーの場合
                            if (ShipmentRegistSingleExecuter.MSGCD_BILL_PAYMENT_ERROR.equals(messageDto.getErrCode())) {
                                // オーソリ通信エラーメッセージ（GMOから返却されたエラーコードに対応するメッセージ）をログへ出力する
                                writeLogForAuth(messageDto, dto.getOrderCode());

                                // オーソリ通信エラー発生時、請求決済エラーが発生した旨を管理者宛メールエラーメッセージに格納する。また、請求決済エラーとなった出荷データは処理件数には含めないものとする
                                AppLevelFacesMessage authErrMessage =
                                                AppLevelFacesMessageUtil.getAllMessage(MSGCD_SALES_AUTHORI_ERROR,
                                                                                       new Object[] {dto.getOrderCode()}
                                                                                      );
                                errorMailMessage.append(authErrMessage.getMessage()).append("\r\n");
                            }
                            // それ以外のメッセージ
                            else {
                                AppLevelFacesMessage messageInfo =
                                                AppLevelFacesMessageUtil.getAllMessage(messageDto.getErrCode(),
                                                                                       new Object[] {dto.getOrderCode()}
                                                                                      );
                                writeLog(dto.getOrderCode(), messageInfo.getMessage());
                                settingErrorMailMessage(dto.getOrderCode(), messageInfo.getMessage(), errorMailMessage);
                            }

                            // 実施件数に含めない
                            errorCount++;

                        } else {
                            resultCount++;
                        }

                    } catch (HttpClientErrorException | HttpServerErrorException e) {
                        // 出荷処理実行時に、外部APIでエラーが発生した場合はこちらでキャッチ
                        writeLog(dto.getOrderCode(), e);
                        ConversionUtility conversionUtility =
                                        ApplicationContextUtility.getBean(ConversionUtility.class);
                        ClientErrorResponse clientError = conversionUtility.toObject(e.getResponseBodyAsString(),
                                                                                     ClientErrorResponse.class
                                                                                    );
                        if (clientError == null || clientError.getMessages() == null) {
                            LOGGER.error("出荷登録に失敗しました。詳細：", e);
                        } else {
                            clientError.getMessages().forEach((fieldName, errorContentList) -> {
                                for (ErrorContent errorContent : errorContentList) {
                                    settingErrorMailMessage(
                                                    dto.getOrderCode(), errorContent.getMessage(), errorMailMessage);
                                }
                            });
                        }

                        // TODO CommunicateResultの対応 他のキャッチ部分にも必要なので共通化すること
                        // CommunicateResult communicateResult = ApplicationContextUtility.getBean(CommunicateResult.class);
                        // if (communicateResult.isCommunicate()) {
                        //     List<String[]> tranList = communicateResult.getTranList();
                        //     errorMailMessage.append("（決済代行会社にて売上実施済です　").append(communicateResult.getMailBodyOrderId(tranList.get(0)[1])).append("）\r\n");
                        // }

                        errorCount++;

                    } catch (BaseException e) {
                        // 出荷処理実行時に、BaseExceptionを継承したException（業務エラーなど）が発生した場合はこちらでキャッチ
                        writeLog(dto.getOrderCode(), e);
                        e.getMessageMap().forEach((fieldName, exceptionContentList) -> {
                            for (ExceptionContent exceptionContent : exceptionContentList) {
                                settingErrorMailMessage(
                                                dto.getOrderCode(), exceptionContent.getMessage(), errorMailMessage);
                            }
                        });
                        errorCount++;

                    } catch (Exception e) {
                        // 出荷処理実行時に、想定外エラーが発生した場合はこちらでキャッチ
                        writeLog(dto.getOrderCode(), e);
                        settingErrorMailMessage(dto.getOrderCode(),
                                                AppLevelFacesMessageUtil.getAllMessage(MSGCD_SHIPMENT_ERROR,
                                                                                       new Object[] {dto.getOrderCode()}
                                                                                      )
                                                                        .getMessage(), errorMailMessage
                                               );
                        errorCount++;
                    }
                }
            } else {
                // データが存在しない場合
                reportString.append("CSVファイルを読み込みましたが対象件数が0件のため、出荷登録バッチは実行されていません。").append("\r\n");
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
            // 出荷登録の実処理外で想定外エラーが発生した場合 (例：アップロードファイルの読込など

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
                    LOGGER.info("[" + ShipmentRegistProcessor.class + "] " + getCommonErrorMessage(em));
                }
            } else {
                reportString.append(getCommonErrorMessage(e));
                errorMailMessage.append(getCommonErrorMessage(e));
                LOGGER.info("[" + ShipmentRegistProcessor.class + "] " + getCommonErrorMessage(e));
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
            LOGGER.info("出荷登録バッチ終了");
        }

    }

    /**
     * アップロードファイルからDtoリストを作成
     *
     * @param filePath ファイルパス
     * @return List<ShipmentCsvDto> DtoList
     */
    private List<ShipmentCsvDto> createTargetDtoListForTmpFile(String filePath) {

        if (StringUtils.isBlank(filePath)) {
            // ファイルパスが存在しない場合
            throw new DomainException("ORDER-SHIPPINGBATCH01-E");
        }

        List<Integer> recordCount = new ArrayList<>();
        List<ShipmentCsvDto> shipmentCsvDtoList = new ArrayList<>();
        CsvValidationResult result = this.csvReaderModule.validateCSV(new File(filePath), recordCount,
                                                                    CsvUploadResult.CSV_UPLOAD_VALID_LIMIT,
                                                                    shipmentCsvDtoList, ShipmentCsvDto.class
                                                                   );

        if (!result.isValid()) {
            // バリデーションエラーの場合
            throw new DomainException("ORDER-SHIPPINGBATCH01-E");
        }
        return shipmentCsvDtoList;
    }

    /**
     * オーソリエラーのログを出力<br/>
     * バッチ管理のレポートには出力しない
     *
     * @param dto       出荷登録実行メッセージDto
     * @param orderCode 受注番号
     */
    private void writeLogForAuth(ShipmentRegisterMessageDto dto, String orderCode) {
        LOGGER.error("オーソリエラーが発生しました。詳細は決済サービスのログを確認してください。 -- 受注番号：" + orderCode + " 情報：" + dto.getErrMessage());
        LOGGER.error("受注番号：" + orderCode + "の出荷登録処理は処理件数に含みません。");
    }

    /**
     * エラーのログを出力
     *
     * @param orderCode 受注番号
     * @param e         Exception情報
     */
    private void writeLog(String orderCode, Throwable e) {
        LOGGER.error("出荷登録処理を実行中にエラーが発生しました。 -- 受注番号：" + orderCode, e);
    }

    /**
     * エラーのログを出力
     *
     * @param orderCode 受注番号
     * @param message   メッセージ内容
     */
    private void writeLog(String orderCode, String message) {
        LOGGER.error("出荷登録処理を実行中にエラーが発生しました。 -- 受注番号：" + orderCode);
        LOGGER.error("情報 -- " + message);
    }

    /**
     * 管理者用エラーメールのメッセージをセット
     *
     * @param orderCode        受注番号
     * @param message          エラーメッセージ内容
     * @param errorMailMessage 送信用エラーメッセージ内容
     */
    private void settingErrorMailMessage(String orderCode, String message, StringBuilder errorMailMessage) {
        if (StringUtils.isBlank(message)) {
            // メッセージが存在しない場合
            errorMailMessage.append(
                            AppLevelFacesMessageUtil.getAllMessage(MSGCD_SHIPMENT_ERROR, new Object[] {orderCode})
                                                    .getMessage()).append("\r\n");
        } else if (StringUtils.isNotBlank(message) && message.contains(orderCode)) {
            // 渡されたメッセージに受注番号が含まれている場合
            errorMailMessage.append(message).append("\r\n");
        } else {
            errorMailMessage.append("「受注番号：" + orderCode + "」" + message).append("\r\n");
        }
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
     * 管理者にエラーメールを非同期送信
     *
     * @param errorMailMessage エラーメッセージ内容
     */
    private void sendAsyncAdministratorErrorMail(StringBuilder errorMailMessage) {
        this.notificationSubAdapter.shipmenRegist(errorMailMessage.toString());
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

}