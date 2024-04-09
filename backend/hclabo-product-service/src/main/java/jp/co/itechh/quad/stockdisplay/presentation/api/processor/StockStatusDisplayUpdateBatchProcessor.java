package jp.co.itechh.quad.stockdisplay.presentation.api.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import jp.co.itechh.quad.batch.logging.BatchLogging;
import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.batch.offline.stockdisplay.dao.StockStatusDisplayBatchDao;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockStatusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * 商品グループ在庫状態更新 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class StockStatusDisplayUpdateBatchProcessor {
    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(StockStatusDisplayUpdateBatchProcessor.class);

    /** 商品グループ在庫表示Dao */
    private final StockStatusDisplayBatchDao stockStatusDisplayBatchDao;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    /** 通知サブドメイン */
    private final NotificationSubApi notificationSubApi;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /**
     * コンストラクタ
     * @param stockStatusDisplayBatchDao 商品グループ在庫表示Dao
     * @param dateUtility 日付関連Utility
     * @param notificationSubApi 日付関連Utility
     * @param headerParamsUtil ヘッダパラメーターユーティル
     * @param asyncService 非同期処理サービス
     */
    @Autowired
    public StockStatusDisplayUpdateBatchProcessor(StockStatusDisplayBatchDao stockStatusDisplayBatchDao,
                                                  DateUtility dateUtility,
                                                  NotificationSubApi notificationSubApi,
                                                  HeaderParamsUtility headerParamsUtil,
                                                  AsyncService asyncService) {
        this.stockStatusDisplayBatchDao = stockStatusDisplayBatchDao;
        this.dateUtility = dateUtility;
        this.notificationSubApi = notificationSubApi;
        this.headerParamsUtil = headerParamsUtil;
        this.asyncService = asyncService;
    }

    /**
     * Consumer
     *
     * @param batchQueueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws JsonProcessingException {

        // 管理者SEQにパラメーターを設定する
        headerParamsUtil.setAdministratorSeq(
                        notificationSubApi.getApiClient(), String.valueOf(batchQueueMessage.getAdministratorSeq()));

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("BATCH_STOCKSTATUSDISPLAY_UPDATE");
        batchLogging.setBatchName("商品グループ在庫状態更新");
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("商品グループ在庫状態更新開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            // 商品グループ在庫状態表示一括登録。
            // ・未登録の商品を登録します。
            int insertCnt = stockStatusDisplayBatchDao.insertNotExists(dateUtility.getCurrentTime());

            // 商品グループ在庫状態表示一括更新。
            // ・全ての商品グループの在庫状態PC,MBを更新します。
            int updateCnt = stockStatusDisplayBatchDao.updateAll(dateUtility.getCurrentTime());

            reportString.append("登録件数[").append(insertCnt).append("]更新件数[").append(updateCnt).append("]で処理が終了しました。");
            batchLogging.setProcessCount(insertCnt + updateCnt);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());
        } catch (Exception e) {
            reportString.append(new Timestamp(System.currentTimeMillis())).append(" 予期せぬエラーが発生しました。処理を中断し終了します。");
            batchLogging.setProcessCount(null);
            batchLogging.setReport(reportString);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());

            // メール送信
            // エラーがあった場合は管理者にメール送信
            StockStatusRequest stockStatusRequest = new StockStatusRequest();
            stockStatusRequest.setExecptionInfo(e.getClass().getName());

            Object[] args = new Object[] {stockStatusRequest};
            Class<?>[] argsClass = new Class<?>[] {StockStatusRequest.class};
            asyncService.asyncService(notificationSubApi, "stockStatus", args, argsClass);

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");

            LOGGER.info(reportString.toString());
            LOGGER.info("商品グループ在庫状態更新終了");
        }
    }
}