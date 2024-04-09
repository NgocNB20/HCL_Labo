package jp.co.itechh.quad.clear.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dao.SecuredShippingItemDao;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dao.SecuredShippingItemForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dao.ShippingSlipDao;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dao.ShippingSlipForRevisionDao;
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

    /** 配送伝票Daoクラス */
    private final ShippingSlipDao shippingSlipDao;

    /** 配送商品Daoクラス */
    private final SecuredShippingItemDao securedShippingItemDao;

    /** 改訂用配送伝票Daoクラス */
    private final ShippingSlipForRevisionDao shippingSlipForRevisionDao;

    /** 改訂用配送商品Daoクラス */
    private final SecuredShippingItemForRevisionDao securedShippingItemForRevisionDao;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param shippingSlipDao                   配送伝票Daoクラス
     * @param securedShippingItemDao            配送商品Daoクラス
     * @param shippingSlipForRevisionDao        改訂用配送伝票Daoクラス
     * @param securedShippingItemForRevisionDao 改訂用配送商品Daoクラス
     * @param dateUtility                       日付関連Utilityクラス
     */
    @Autowired
    public ClearProcessor(ShippingSlipDao shippingSlipDao,
                          SecuredShippingItemDao securedShippingItemDao,
                          ShippingSlipForRevisionDao shippingSlipForRevisionDao,
                          SecuredShippingItemForRevisionDao securedShippingItemForRevisionDao,
                          ConversionUtility conversionUtility,
                          DateUtility dateUtility) {
        this.shippingSlipDao = shippingSlipDao;
        this.securedShippingItemDao = securedShippingItemDao;
        this.shippingSlipForRevisionDao = shippingSlipForRevisionDao;
        this.securedShippingItemForRevisionDao = securedShippingItemForRevisionDao;
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

        batchLogging.setBatchId("LOGISTIC_BATCH_CLEAR");
        batchLogging.setBatchName("物流クリアバッチ");
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

            processCount = securedShippingItemDao.clearSecuredShippingItem(deleteTime);

            processCount += shippingSlipDao.clearShippingSlip(deleteTime);

            processCount += securedShippingItemForRevisionDao.clearSecuredShippingItemForRevision(deleteTime);

            processCount += shippingSlipForRevisionDao.clearShippingSlipRevision(deleteTime);

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