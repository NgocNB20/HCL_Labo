package jp.co.itechh.quad.clear.presentation.api.processor;

import jp.co.itechh.quad.clear.presentation.api.param.UnnecessaryBillingSlipCancellationRequest;
import jp.co.itechh.quad.clear.presentation.api.param.UnnecessaryOrderSlipCancellationRequest;
import jp.co.itechh.quad.clear.presentation.api.param.UnnecessarySalesSlipCancellationRequest;
import jp.co.itechh.quad.clear.presentation.api.param.UnnecessaryShippingSlipCancellationRequest;
import jp.co.itechh.quad.clearlogistic.presentation.api.LogisticClearApi;
import jp.co.itechh.quad.clearpayment.presentation.api.PaymentClearApi;
import jp.co.itechh.quad.clearpriceplanning.presentation.api.PricePlanningClearApi;
import jp.co.itechh.quad.clearpromotion.presentation.api.PromotionClearApi;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dao.OrderReceivedDao;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dao.TransactionDao;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dao.TransactionForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.GetByDraftStatusResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 未確定受注データ取消バッチ Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class ClearProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClearProcessor.class);

    /** 受注Daoクラス */
    private final OrderReceivedDao orderReceivedDao;

    /** 取引Daoクラス */
    private final TransactionDao transactionDao;

    /** 改訂用取引Daoクラス */
    private final TransactionForRevisionDao transactionForRevisionDao;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /** 未確定受注データ取消バッチAPI */
    private final LogisticClearApi logisticClearApi;

    /** 未確定受注データ取消バッチAPI */
    private final PaymentClearApi paymentClearApi;

    /** 未確定受注データ取消バッチAPI */
    private final PricePlanningClearApi pricePlanningClearApi;

    /** 未確定受注データ取消バッチAPI */
    private final PromotionClearApi promotionClearApi;

    /**
     * コンストラクタ
     *
     * @param orderReceivedDao          受注Daoクラス
     * @param transactionDao            取引Daoクラス
     * @param transactionForRevisionDao 改訂用取引Daoクラス
     * @param conversionUtility         変換Helper
     * @param dateUtility               日付関連Utilityクラス
     * @param logisticClearApi          未確定受注データ取消バッチAPI
     * @param paymentClearApi           未確定受注データ取消バッチAPI
     * @param pricePlanningClearApi     未確定受注データ取消バッチAPI
     * @param promotionClearApi         未確定受注データ取消バッチAPI
     */
    @Autowired
    public ClearProcessor(OrderReceivedDao orderReceivedDao,
                          TransactionDao transactionDao,
                          TransactionForRevisionDao transactionForRevisionDao,
                          ConversionUtility conversionUtility,
                          DateUtility dateUtility,
                          LogisticClearApi logisticClearApi,
                          PaymentClearApi paymentClearApi,
                          PricePlanningClearApi pricePlanningClearApi,
                          PromotionClearApi promotionClearApi) {
        this.orderReceivedDao = orderReceivedDao;
        this.transactionDao = transactionDao;
        this.transactionForRevisionDao = transactionForRevisionDao;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.logisticClearApi = logisticClearApi;
        this.paymentClearApi = paymentClearApi;
        this.pricePlanningClearApi = pricePlanningClearApi;
        this.promotionClearApi = promotionClearApi;
    }

    /**
     * Processorメソッド
     *
     * @param batchQueueMessage メッセージ
     */
    public void processor(BatchQueueMessage batchQueueMessage) throws Exception {

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("ORDER_BATCH_CLEAR");
        batchLogging.setBatchName("受注クリアバッチ（未確定受注データ取消バッチ）");
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("受注クリアバッチ（未確定受注データ取消バッチ）を開始します");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();
        // 実行件数
        AtomicInteger resultCount = new AtomicInteger();
        // エラー件数
        AtomicInteger errorCount = new AtomicInteger();

        try {
            //　対象期間日時取得
            Integer periodTimeCancelUnopen = conversionUtility.toInteger(
                            PropertiesUtil.getSystemPropertiesValue("period-time-cancel-unopen-batch"));
            Timestamp periodTime = dateUtility.getDateBySubtractionMinutes(periodTimeCancelUnopen);

            // 対象期間の下書き状態の取引データ取得して、取消を行う
            try (Stream<GetByDraftStatusResultDto> getByDraftStatusResultDtoStream = transactionDao.getByDraftStatusList(
                            periodTime)) {
                // 自身及び各サービスの不要データの取消を行う
                getByDraftStatusResultDtoStream.forEach(draftStatusResultDto -> {
                    try {
                        deleteUnnecessaryTransactionData(
                                        draftStatusResultDto.getTransactionId(), draftStatusResultDto.getOrderCode());
                    } catch (Exception e) {
                        errorCount.getAndIncrement();
                        LOGGER.warn("未確定受注データ取消でエラー発生 取引ID：" + draftStatusResultDto.getTransactionId(), e);
                    }
                    resultCount.getAndIncrement();
                });
            }

            // 改訂用取引データ（未確定）取得処理を取得して、取消を行う
            try (Stream<String> transactionRevisionIdStream = transactionForRevisionDao.getTransactionForRevisionUnconfirmedList(
                            periodTime)) {
                // 自身及び各サービスの不要データの取消を行う
                transactionRevisionIdStream.forEach(transactionRevisionId -> {
                    try {
                        deleteUnnecessaryDataForRevision(transactionRevisionId);
                    } catch (Exception e) {
                        errorCount.getAndIncrement();
                        LOGGER.warn("未確定受注データ取消でエラー発生 改訂用取引ID：" + transactionRevisionId, e);
                    }
                    resultCount.getAndIncrement();
                });
            }

            // 不要改訂用取引データ（確定済）の対象期間全件削除
            transactionForRevisionDao.deleteTransactionForRevisionConfirmed(periodTime);

            // レポート
            reportString.append(resultCount + "件の未確定受注データ処理が終了しました。").append("\r\n");
            if (errorCount.get() > 0) {
                reportString.append("内、" + errorCount + "件のエラーが発生しました。").append("\r\n");
                batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());
            } else {
                batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());
            }

        } catch (Exception e) {
            // レポート
            reportString.append(resultCount + "件の未確定受注データ処理が終了しました").append("\r\n");
            if (errorCount.get() > 0) {
                reportString.append("内、" + errorCount + "件のエラーが発生しました。").append("\r\n");
            }
            reportString.append("処理中に予期せぬエラーが発生したため異常終了しています").append("\r\n");

            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.error("処理中に予期せぬエラーが発生したため異常終了しています。", e);

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.setProcessCount(resultCount.get());
            batchLogging.setReport(reportString);
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("未確定受注データ取消バッチが終了しました");
        }
    }

    /**
     * 不要取引/受注データ取消
     *
     * @param transactionId 取引ID
     * @param orderCode     受注番号
     */
    private void deleteUnnecessaryTransactionData(String transactionId, String orderCode) {

        // 不要配送伝票削除（在庫戻しあり
        UnnecessaryShippingSlipCancellationRequest unnecessaryShippingSlipCancellationRequest =
                        new UnnecessaryShippingSlipCancellationRequest();
        unnecessaryShippingSlipCancellationRequest.setTransactionId(transactionId);
        unnecessaryShippingSlipCancellationRequest.setStockReleaseFlag(true);
        logisticClearApi.delete(unnecessaryShippingSlipCancellationRequest);

        // 不要請求伝票取消（クレジット与信枠開放/リンク決済メール通知あり
        UnnecessaryBillingSlipCancellationRequest unnecessaryBillingSlipCancellationRequest =
                        new UnnecessaryBillingSlipCancellationRequest();
        unnecessaryBillingSlipCancellationRequest.setTransactionId(transactionId);
        unnecessaryBillingSlipCancellationRequest.setOrderCode(orderCode);
        paymentClearApi.delete(unnecessaryBillingSlipCancellationRequest);

        // 不要販売伝票取消
        UnnecessarySalesSlipCancellationRequest unnecessarySalesSlipCancellationRequest =
                        new UnnecessarySalesSlipCancellationRequest();
        unnecessarySalesSlipCancellationRequest.setTransactionId(transactionId);
        pricePlanningClearApi.delete(unnecessarySalesSlipCancellationRequest);

        // 不要注文票取消
        UnnecessaryOrderSlipCancellationRequest unnecessaryOrderSlipCancellationRequest =
                        new UnnecessaryOrderSlipCancellationRequest();
        unnecessaryOrderSlipCancellationRequest.setTransactionId(transactionId);
        promotionClearApi.delete(unnecessaryOrderSlipCancellationRequest);

        // 不要受注データ削除
        orderReceivedDao.deleteOrderReceived(transactionId);
        transactionDao.deleteTransaction(transactionId);

    }

    /**
     * 不要改訂用取引データ取消
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    private void deleteUnnecessaryDataForRevision(String transactionRevisionId) {

        // 不要配送伝票取消
        UnnecessaryShippingSlipCancellationRequest unnecessaryShippingSlipCancellationRequest =
                        new UnnecessaryShippingSlipCancellationRequest();
        unnecessaryShippingSlipCancellationRequest.setTransactionId(transactionRevisionId);
        unnecessaryShippingSlipCancellationRequest.setStockReleaseFlag(false);
        logisticClearApi.delete(unnecessaryShippingSlipCancellationRequest);

        // 不要請求伝票取消
        UnnecessaryBillingSlipCancellationRequest unnecessaryBillingSlipCancellationRequest =
                        new UnnecessaryBillingSlipCancellationRequest();
        unnecessaryBillingSlipCancellationRequest.setTransactionId(transactionRevisionId);
        paymentClearApi.delete(unnecessaryBillingSlipCancellationRequest);

        // 不要販売伝票取消
        UnnecessarySalesSlipCancellationRequest unnecessarySalesSlipCancellationRequest =
                        new UnnecessarySalesSlipCancellationRequest();
        unnecessarySalesSlipCancellationRequest.setTransactionId(transactionRevisionId);
        pricePlanningClearApi.delete(unnecessarySalesSlipCancellationRequest);

        // 不要注文票取消
        UnnecessaryOrderSlipCancellationRequest unnecessaryOrderSlipCancellationRequest =
                        new UnnecessaryOrderSlipCancellationRequest();
        unnecessaryOrderSlipCancellationRequest.setTransactionId(transactionRevisionId);
        promotionClearApi.delete(unnecessaryOrderSlipCancellationRequest);

        // 不要改訂用取引データ（未確定）削除
        transactionForRevisionDao.deleteTransactionForRevision(transactionRevisionId);

    }
}