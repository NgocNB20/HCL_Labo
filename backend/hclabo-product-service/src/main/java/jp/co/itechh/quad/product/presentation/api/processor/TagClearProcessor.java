package jp.co.itechh.quad.product.presentation.api.processor;

import jp.co.itechh.quad.batch.logging.BatchLogging;
import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeBatchStartType;
import jp.co.itechh.quad.core.service.goods.tag.GoodsTagClearService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.TagClearErrorRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * タグクリアバッチ Consumer
 *
 *@author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
@Scope("prototype")
public class TagClearProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(TagClearProcessor.class);

    /** 通知サブドメイン */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 商品タグクリア */
    private final GoodsTagClearService goodsTagClearService;

    /**
     * コンストラクタ
     * @param notificationSubApi 通知サブドメイン
     * @param asyncService 非同期処理サービス
     * @param goodsTagClearService 商品タグクリア
     */
    @Autowired
    public TagClearProcessor(NotificationSubApi notificationSubApi,
                             AsyncService asyncService,
                             GoodsTagClearService goodsTagClearService) {
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;
        this.goodsTagClearService = goodsTagClearService;
    }

    /**
     * Processorメソッド
     *
     * @param batchQueueMessage メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(BatchQueueMessage batchQueueMessage) throws Exception {

        BatchLogging batchLogging = new BatchLogging();

        batchLogging.setBatchId("TAG_BATCH_CLEAR");
        batchLogging.setBatchName("タグクリアバッチ");
        batchLogging.setStartType(Objects.requireNonNull(
                                                         EnumTypeUtil.getEnumFromValue(HTypeBatchStartType.class, batchQueueMessage.getStartType()))
                                         .getLabel());
        batchLogging.setInputData(batchQueueMessage);
        batchLogging.setStartTime(new Timestamp(System.currentTimeMillis()));

        LOGGER.info("タグクリアバッチを開始します");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {

            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

            String deleteTime = PropertiesUtil.getSystemPropertiesValue("product.tags.clear");

            Timestamp deleteTimestamp = dateUtility.getAmountMinuteTimestamp(Integer.parseInt(deleteTime), false,
                                                                             dateUtility.getCurrentTime()
                                                                            );

            int processCount = goodsTagClearService.execute(deleteTimestamp);

            reportString.append("タグクリアバッチ起動成功");
            batchLogging.setReport(reportString);
            batchLogging.setProcessCount(processCount);
            batchLogging.setResult(HTypeBatchResult.COMPLETED.getLabel());

            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());

        } catch (Exception e) {

            // エラーメッセージを成形する
            String errorResultMsg = null;
            TagClearErrorRequest tagClearErrorRequest = new TagClearErrorRequest();
            if (StringUtils.isNotBlank(e.getMessage())) {
                errorResultMsg = e.getMessage();
            } else {
                errorResultMsg = e.getClass().getName() + "が発生";
            }
            tagClearErrorRequest.setErrorResultMsg(errorResultMsg);

            // メール送信
            Object[] args = new Object[] {tagClearErrorRequest};
            Class<?>[] argsClass = new Class<?>[] {TagClearErrorRequest.class};
            asyncService.asyncService(notificationSubApi, "tagClearError", args, argsClass);

            reportString.append("タグクリアバッチ起動失敗");
            batchLogging.setReport(reportString);
            batchLogging.setProcessCount(null);
            batchLogging.setResult(HTypeBatchResult.FAILED.getLabel());

            LOGGER.error("処理中に予期せぬエラーが発生したため異常終了しています。", e);
            // バッチ異常終了
            LOGGER.error("ロールバックします。");

            throw e;
        } finally {
            batchLogging.setEndTime(new Timestamp(System.currentTimeMillis()));
            batchLogging.log("batch-log");
            LOGGER.info("タグクリアバッチが終了しました");
        }
    }
}