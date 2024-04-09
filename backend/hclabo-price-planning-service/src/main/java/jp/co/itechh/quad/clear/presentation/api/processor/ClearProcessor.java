package jp.co.itechh.quad.clear.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.AdjustmentAmountDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.AdjustmentAmountForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.ItemPurchasePriceSubTotalDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.ItemPurchasePriceSubTotalForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.SalesSlipDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.SalesSlipForRevisionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * クリアバッチ Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class ClearProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClearProcessor.class);

    /** 販売伝票Daoクラス */
    private final SalesSlipDao salesSlipDao;

    /** 商品購入価格小計 Daoクラス */
    private final ItemPurchasePriceSubTotalDao itemPurchasePriceSubTotalDao;

    /** 販売伝票エンドポイントDaoクラス */
    private final SalesSlipForRevisionDao salesSlipForRevisionDao;

    /** 改訂用商品購入価格小計 Daoクラス */
    private final ItemPurchasePriceSubTotalForRevisionDao itemPurchasePriceSubTotalForRevisionDao;

    /** クーポンDaoクラス */
    private final AdjustmentAmountDao adjustmentAmountDao;

    /** 改訂用クーポンDaoクラス */
    private final AdjustmentAmountForRevisionDao adjustmentAmountForRevisionDao;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param salesSlipDao                            販売伝票Daoクラス
     * @param itemPurchasePriceSubTotalDao            商品購入価格小計 Daoクラス
     * @param salesSlipForRevisionDao                 販売伝票エンドポイントDaoクラス
     * @param itemPurchasePriceSubTotalForRevisionDao 改訂用商品購入価格小計 Daoクラス
     * @param adjustmentAmountDao                     クーポンDaoクラス
     * @param adjustmentAmountForRevisionDao          改訂用クーポンDaoクラス
     * @param conversionUtility                       変換Helper
     * @param dateUtility                             日付関連Utilityクラス
     */
    @Autowired
    public ClearProcessor(SalesSlipDao salesSlipDao,
                          ItemPurchasePriceSubTotalDao itemPurchasePriceSubTotalDao,
                          SalesSlipForRevisionDao salesSlipForRevisionDao,
                          ItemPurchasePriceSubTotalForRevisionDao itemPurchasePriceSubTotalForRevisionDao,
                          AdjustmentAmountDao adjustmentAmountDao,
                          AdjustmentAmountForRevisionDao adjustmentAmountForRevisionDao,
                          ConversionUtility conversionUtility,
                          DateUtility dateUtility) {
        this.salesSlipDao = salesSlipDao;
        this.itemPurchasePriceSubTotalDao = itemPurchasePriceSubTotalDao;
        this.salesSlipForRevisionDao = salesSlipForRevisionDao;
        this.itemPurchasePriceSubTotalForRevisionDao = itemPurchasePriceSubTotalForRevisionDao;
        this.adjustmentAmountDao = adjustmentAmountDao;
        this.adjustmentAmountForRevisionDao = adjustmentAmountForRevisionDao;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * Processorメソッド
     *
     * @param batchQueueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws Exception {

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("PRICE_PLANNING_BATCH_CLEAR");
        batchLogging.setBatchName("販売企画クリアバッチ");
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("クリアバッチを開始します");

        try {

            Integer periodTime = conversionUtility.toInteger(
                            PropertiesUtil.getSystemPropertiesValue("period-time-clear-batch"));

            Timestamp deleteTime = dateUtility.getDateBySubtractionMinutes(periodTime);

            // 処理件数
            int processCount = 0;

            processCount = itemPurchasePriceSubTotalDao.clearItemPurchasePriceSubTotal(deleteTime);

            processCount += adjustmentAmountDao.clearAdjustmentAmount(deleteTime);

            processCount += salesSlipDao.clearSaleSlip(deleteTime);

            processCount += itemPurchasePriceSubTotalForRevisionDao.clearItemPurchasePriceSubTotalForRevision(
                            deleteTime);

            processCount += adjustmentAmountForRevisionDao.clearAdjustmentAmountForRevision(deleteTime);

            processCount += salesSlipForRevisionDao.clearSalesSlipForRevision(deleteTime);

            batchLogging.setProcessCount(processCount);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());

        } catch (Exception e) {
            batchLogging.setProcessCount(null);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.error("処理中に予期せぬエラーが発生したため異常終了しています。", e);
            // バッチ異常終了
            LOGGER.error("ロールバックします。");

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");
            LOGGER.info("クリアバッチが終了しました");
        }
    }
}