package jp.co.itechh.quad.clear.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderItemDao;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderItemOriginRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderItemRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderSlipDao;
import jp.co.itechh.quad.ddd.infrastructure.order.dao.OrderSlipForRevisionDao;
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

    /** 注文票Daoクラス */
    private final OrderSlipDao orderSlipDao;

    /** 注文商品Daoクラス */
    private final OrderItemDao orderItemDao;

    /** 改訂用注文票Daoクラス */
    private final OrderSlipForRevisionDao orderSlipForRevisionDao;

    /** 注文商品Daoクラス */
    private final OrderItemRevisionDao orderItemRevisionDao;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 改訂元注文商品Daoクラス */
    private final OrderItemOriginRevisionDao orderItemOriginRevisionDao;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param orderSlipDao               注文票Daoクラス
     * @param orderItemDao               注文商品Daoクラス
     * @param orderSlipForRevisionDao    改訂用注文票Daoクラス
     * @param orderItemRevisionDao       注文商品Daoクラス
     * @param orderItemOriginRevisionDao 改訂元注文商品Daoクラス
     * @param dateUtility                日付関連Utilityクラス
     */
    @Autowired
    public ClearProcessor(OrderSlipDao orderSlipDao,
                          OrderItemDao orderItemDao,
                          OrderSlipForRevisionDao orderSlipForRevisionDao,
                          OrderItemRevisionDao orderItemRevisionDao,
                          ConversionUtility conversionUtility,
                          OrderItemOriginRevisionDao orderItemOriginRevisionDao,
                          DateUtility dateUtility) {
        this.orderSlipDao = orderSlipDao;
        this.orderItemDao = orderItemDao;
        this.orderSlipForRevisionDao = orderSlipForRevisionDao;
        this.orderItemRevisionDao = orderItemRevisionDao;
        this.conversionUtility = conversionUtility;
        this.orderItemOriginRevisionDao = orderItemOriginRevisionDao;
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

        batchLogging.setBatchId("PROMOTION_BATCH_CLEAR");
        batchLogging.setBatchName("プロモーションクリアバッチ");
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

            processCount = orderItemDao.clearOrderItem(deleteTime);

            processCount += orderSlipDao.clearOrderSlip(deleteTime);

            processCount += orderItemRevisionDao.clearOrderItemRevision(deleteTime);

            processCount += orderItemOriginRevisionDao.clearOrderItemOriginRevision(deleteTime);

            processCount += orderSlipForRevisionDao.clearOrderSlipRevision(deleteTime);

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