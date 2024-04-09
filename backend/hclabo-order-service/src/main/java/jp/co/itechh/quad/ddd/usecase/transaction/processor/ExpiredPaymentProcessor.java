package jp.co.itechh.quad.ddd.usecase.transaction.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.GetByDraftStatusResultDto;
import jp.co.itechh.quad.ddd.usecase.transaction.service.ExpiredPaymentSingleExecuter;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementAdministratorErrorMailRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementAdministratorMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * 支払期限切れProcessor
 *
 * @author Pham Quang Dieu (VJP)
 */
@Component
@Scope("prototype")
public class ExpiredPaymentProcessor {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpiredPaymentProcessor.class);

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 支払期限切れ1件処理 */
    private final ExpiredPaymentSingleExecuter expiredPaymentSingleProcessor;

    /** 通知サブドメイン */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 正常処理メッセージ(処理内容格納用） */
    private final StringBuilder mailMessage;

    /** コンストラクタ */
    @Autowired
    public ExpiredPaymentProcessor(ITransactionRepository transactionRepository,
                                   ExpiredPaymentSingleExecuter expiredPaymentSingleProcessor, NotificationSubApi notificationSubApi, AsyncService asyncService) {
        this.transactionRepository = transactionRepository;
        this.expiredPaymentSingleProcessor = expiredPaymentSingleProcessor;
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;
        this.mailMessage = new StringBuilder();
    }

    /**
     * 支払期限切れ
     *
     * @param batchQueueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws JsonProcessingException {

        BatchLogging batchLogging = new BatchLogging();
        batchLogging.setBatchId(HTypeBatchName.BATCH_EXPIRED_PAYMENT.getValue());
        batchLogging.setBatchName(HTypeBatchName.BATCH_EXPIRED_PAYMENT.getLabel());
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("支払期限切れバッチ開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();
        // 実行件数
        int resultCount = 0;
        // エラー件数
        int errorCount = 0;

        try {
            // 期限切れ対象受注一覧
            List<GetByDraftStatusResultDto> expiredPaymentDtoList = transactionRepository.getExpiredPayment();

            if (!CollectionUtils.isEmpty(expiredPaymentDtoList)) {

                // 受注単位のループを行う
                for (GetByDraftStatusResultDto expiredPaymentDto : expiredPaymentDtoList) {
                    final String msg = "%d件目 受注(transactionId:%s, orderCode:%s) %s\r\n";
                    String tranID = expiredPaymentDto.getTransactionId();
                    String code = expiredPaymentDto.getOrderCode();
                    try {

                        // 支払期限切れ1件処理（コミット）
                        OrderReceivedEntity orderReceivedEntity =
                                        expiredPaymentSingleProcessor.execute(expiredPaymentDto);
                        expiredPaymentSingleProcessor.asyncAfterProcess(orderReceivedEntity);

                        if (orderReceivedEntity != null) {
                            resultCount++;
                            mailMessage.append(String.format(msg, resultCount, tranID, code, "メールを送信"));
                        }
                    } catch (Exception e) {
                        LOGGER.warn("支払期限切れバッチでエラーが発生しました。", e);
                        errorCount++;
                        mailMessage.append(String.format("処理中に %s が発生しました。：受注(transactionId:%s, orderCode:%s)\r\n", e.getClass().getName(), tranID, code));

                    }
                }
            } else {
                // データが存在しない場合
                reportString.append("未入金取引対象件数が0件のため、支払期限切れバッチは実行されていません。").append("\r\n");
                // 督促メール送信対象０件メール送信（処理無しメール送信）
                sendAdministratorMail("期限切れメール送信対象の未入金受注はありません。", this.mailMessage.toString());
            }

            // 結果レポート設定
            if (resultCount > 0) {
                reportString.append(resultCount + "件の処理が終了しました。").append("\r\n");
            }

            if (errorCount > 0) {
                reportString.append(errorCount + "件のエラーが発生しました。").append("\r\n");
            }

            if (resultCount == 0 && errorCount == 0) {
                LOGGER.info("支払期限切れ対象の受注がありません。");
            }

            // バッチログ設定
            batchLogging.setProcessCount(resultCount + errorCount);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());
            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());

            if (resultCount + errorCount > 0) {
                // メールを送信する。
                sendAdministratorMail(reportString.toString(), this.mailMessage.toString());
            }

        } catch (Exception e) {
            reportString.append(new Timestamp(System.currentTimeMillis()))
                        .append(" 予期せぬエラーが発生しました。処理を中断し終了します。")
                        .append("\n");
            reportString.append(e.getMessage());
            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());

            sendAdministratorErrorMail(reportString.toString());
            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.setReport(reportString);
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("支払期限切れバッチ終了");
        }
    }

    /**
     * 管理者向け処理完了メールを送信する。
     *
     * @param result      結果レポート文字列
     * @param mailMessage 正常処理メッセージ(処理内容格納用）
     * @return 成否
     */
    private void sendAdministratorMail(String result, String mailMessage) {

        SettlementAdministratorMailRequest successMailRequest = new SettlementAdministratorMailRequest();
        successMailRequest.setResult(result);
        successMailRequest.setMailMessage(mailMessage);

        Object[] successMailRequestObj = new Object[] {successMailRequest};
        Class<?>[] successMailRequestObjClass = new Class<?>[] {SettlementAdministratorMailRequest.class};

        asyncService.asyncService(notificationSubApi, "settlementAdministratorMail", successMailRequestObj, successMailRequestObjClass);
    }
    /**
     * 処理が失敗した旨の管理者向けメールを送信する。
     *
     * @param errorResultMsg エラー結果メッセージ
     * @return true:成功、false:失敗
     */
    private void sendAdministratorErrorMail(String errorResultMsg) {

        // メールを送信する。
        SettlementAdministratorErrorMailRequest errMailRequest = new SettlementAdministratorErrorMailRequest();
        errMailRequest.setErrorResultMsg(errorResultMsg);

        Object[] errMailRequestObj = new Object[] {errMailRequest};
        Class<?>[] errMailRequestObjClass = new Class<?>[] {SettlementAdministratorMailRequest.class};

        asyncService.asyncService(notificationSubApi, "settlementAdministratorErrorMail", errMailRequestObj, errMailRequestObjClass);
    }
}