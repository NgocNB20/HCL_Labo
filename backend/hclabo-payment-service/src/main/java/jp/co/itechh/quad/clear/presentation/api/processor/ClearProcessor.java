package jp.co.itechh.quad.clear.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.logging.BatchLogging;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.BillingSlipDao;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.BillingSlipForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.OrderPaymentDao;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.OrderPaymentForRevisionDao;
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

    /** 請求伝票Daoクラス */
    private final BillingSlipDao billingSlipDao;

    /** 注文決済Daoクラス */
    private final OrderPaymentDao orderPaymentDao;

    /** 改訂用請求伝票Dao */
    private final BillingSlipForRevisionDao billingSlipForRevisionDao;

    /** 改訂用注文決済エンティティDaoクラス */
    private final OrderPaymentForRevisionDao orderPaymentForRevisionDao;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param billingSlipDao             請求伝票Daoクラス
     * @param orderPaymentDao            注文決済Daoクラス
     * @param billingSlipForRevisionDao  改訂用請求伝票Dao
     * @param orderPaymentForRevisionDao 改訂用注文決済エンティティDaoクラス
     * @param dateUtility                日付関連Utilityクラス
     */
    @Autowired
    public ClearProcessor(BillingSlipDao billingSlipDao,
                          OrderPaymentDao orderPaymentDao,
                          BillingSlipForRevisionDao billingSlipForRevisionDao,
                          OrderPaymentForRevisionDao orderPaymentForRevisionDao,
                          ConversionUtility conversionUtility,
                          DateUtility dateUtility) {
        this.billingSlipDao = billingSlipDao;
        this.orderPaymentDao = orderPaymentDao;
        this.billingSlipForRevisionDao = billingSlipForRevisionDao;
        this.orderPaymentForRevisionDao = orderPaymentForRevisionDao;
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

        batchLogging.setBatchId("PAYMENT_BATCH_CLEAR");
        batchLogging.setBatchName("決済クリアバッチ");
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

            processCount = orderPaymentDao.clearOrderPayment(deleteTime);

            processCount += billingSlipDao.clearBillingSlip(deleteTime);

            processCount += orderPaymentForRevisionDao.clearOrderPaymentForRevision(deleteTime);

            processCount += billingSlipForRevisionDao.clearBillingSlipForRevision(deleteTime);

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