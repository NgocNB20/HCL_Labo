package jp.co.itechh.quad.ddd.usecase.transaction.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IMulPayAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderReceivedId;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionReflectDepositedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * 決済代行入金結果受取予備処理実行 Processor
 *
 * @author Pham Quang Dieu (VJP)
 */
@Component
@Scope("prototype")
public class MulpayNotificationRecoveryProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MulpayNotificationRecoveryProcessor.class);

    /** 通知アダプター */
    private final ITransactionRepository transactionRepository;

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** マルチペイメントアダプター */
    private final IMulPayAdapter mulpayAdapter;

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** 取引API */
    private final TransactionApi transactionApi;

    /** コンストラクタ */
    @Autowired
    public MulpayNotificationRecoveryProcessor(ITransactionRepository transactionRepository,
                                               IOrderReceivedRepository orderReceivedRepository,
                                               IMulPayAdapter mulpayAdapter,
                                               MessagePublisherService messagePublisherService,
                                               TransactionApi transactionApi) {
        this.transactionRepository = transactionRepository;
        this.orderReceivedRepository = orderReceivedRepository;
        this.mulpayAdapter = mulpayAdapter;
        this.messagePublisherService = messagePublisherService;
        this.transactionApi = transactionApi;
    }

    /**
     * Processorメソッド <br/>
     * 決済代行入金結果受取予備処理実行
     *
     * @param batchQueueMessage キューメッセージ
     * @throws JsonProcessingException
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws JsonProcessingException {

        BatchLogging batchLogging = new BatchLogging();
        batchLogging.setBatchId(HTypeBatchName.BATCH_MULPAY_NOTIFICATION_RECOVERY.getValue());
        batchLogging.setBatchName(HTypeBatchName.BATCH_MULPAY_NOTIFICATION_RECOVERY.getLabel());
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("決済代行入金結果受取予備処理実行バッチ開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();
        // 実行件数
        int resultCount = 0;
        // エラー件数
        int errorCount = 0;

        try {
            // 未入金取引(受注番号)一覧取得
            List<TransactionEntity> transactionEntityList = transactionRepository.getUnpaidTransactionList();

            // 受注単位のループを行う
            if (!CollectionUtils.isEmpty(transactionEntityList)) {

                for (TransactionEntity entity : transactionEntityList) {

                    try {
                        OrderReceivedId orderReceivedId = entity.getOrderReceivedId();

                        // 受注取得
                        OrderReceivedEntity orderReceivedEntity = orderReceivedRepository.get(orderReceivedId);
                        if (orderReceivedEntity == null) {
                            continue;
                        }

                        // 【決済サービス】決済代行へ入金を確認
                        Boolean requiredReflectionProcessingFlag = mulpayAdapter.confirmPaymentAgencyResult(
                                        orderReceivedEntity.getOrderCode().getValue());

                        //要入金反映処理=trueの場合
                        if (Boolean.TRUE.equals(requiredReflectionProcessingFlag)) {

                            // HM入金結果データを確認して受注を入金済みにする(非同期処理 MQは同サービスでもAPI呼出し)
                            try {
                                TransactionReflectDepositedRequest transactionReflectDepositedRequest =
                                                new TransactionReflectDepositedRequest();
                                transactionReflectDepositedRequest.setOrderCode(
                                                orderReceivedEntity.getOrderCode().getValue());
                                transactionApi.reflectDeposited(transactionReflectDepositedRequest);
                            } catch (HttpServerErrorException | HttpClientErrorException e) {
                                LOGGER.error("例外処理が発生しました", e);
                                throw e;
                            }
                            resultCount++;

                        } else {
                            continue;
                        }
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                        errorCount++;
                    }
                }
            } else {
                // データが存在しない場合
                reportString.append("未入金取引が対象件数が0件のため、入金結果受取予備処理バッチは実行されていません").append("\r\n");
            }

            // 結果レポート設定
            if (resultCount > 0) {
                reportString.append(resultCount + "件の入金処理が終了しました。").append("\r\n");
            } else {
                LOGGER.info("入金結果受取予備処理の入金対象がありません。");
            }
            if (errorCount > 0) {
                reportString.append(errorCount + "件のエラーが発生しました。").append("\r\n");
            }

            // バッチログ設定
            batchLogging.setProcessCount(resultCount + errorCount);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

        } catch (Exception e) {

            LOGGER.error(e.getMessage());

            batchLogging.setProcessCount(0);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            throw e;

        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.setReport(reportString);
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("決済代行入金結果受取予備処理実行バッチ終了");
        }

    }

}