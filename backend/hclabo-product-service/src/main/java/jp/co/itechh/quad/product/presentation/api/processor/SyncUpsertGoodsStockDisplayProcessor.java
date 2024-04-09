/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.product.presentation.api.processor;

import jp.co.itechh.quad.batch.queue.SyncUpsertGoodsStockDisplayQueueMessage;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.service.goods.goods.GoodsStockDisplaySyncService;
import jp.co.itechh.quad.product.presentation.api.param.GoodsStockDisplayUpsertRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * 商品在庫表示アップサート Processor
 *
 * @author kimura
 */
@Component
@Scope("prototype")
public class SyncUpsertGoodsStockDisplayProcessor {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SyncUpsertGoodsStockDisplayProcessor.class);

    /** 商品在庫表示サービス */
    private final GoodsStockDisplaySyncService goodsStockDisplaySyncService;

    /** コンストラクタ */
    @Autowired
    public SyncUpsertGoodsStockDisplayProcessor(GoodsStockDisplaySyncService goodsStockDisplaySyncService) {
        this.goodsStockDisplaySyncService = goodsStockDisplaySyncService;
    }

    /**
     * Processor
     *
     * @param message メッセージ
     */
    @Transactional(rollbackFor = Exception.class)
    public void processor(SyncUpsertGoodsStockDisplayQueueMessage message) throws Exception {

        LOGGER.info("商品在庫表示アップサート開始");

        // ユーザ向けレポート文字列
        StringBuilder reportString = new StringBuilder();

        try {
            GoodsStockDisplayUpsertRequest stockUpdateRequest = message.getGoodsStockDisplayUpsertRequest();
            int updateCnt = this.goodsStockDisplaySyncService.syncUpsertStock(stockUpdateRequest.getGoodsSeqList());
            reportString.append("更新件数[").append(updateCnt).append("]で処理が終了しました。");
            LOGGER.info("処理結果：" + HTypeBatchResult.COMPLETED.getLabel());
        } catch (Exception e) {
            reportString.append(new Timestamp(System.currentTimeMillis())).append(" 予期せぬエラーが発生しました。処理を中断し終了します。");
            LOGGER.info("処理結果：" + HTypeBatchResult.FAILED.getLabel());

            throw e;
        } finally {
            LOGGER.info(reportString.toString());
            LOGGER.info("商品在庫表示アップサート終了");
        }
    }
}
