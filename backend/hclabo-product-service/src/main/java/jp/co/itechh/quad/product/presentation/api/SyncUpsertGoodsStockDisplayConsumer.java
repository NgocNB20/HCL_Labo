/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.product.presentation.api;

import jp.co.itechh.quad.batch.queue.SyncUpsertGoodsStockDisplayQueueMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.product.presentation.api.processor.SyncUpsertGoodsStockDisplayProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 商品在庫表示アップサート Consumer
 *
 * @author kimura
 */
@Component
public class SyncUpsertGoodsStockDisplayConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SyncUpsertGoodsStockDisplayConsumer.class);

    /** コンストラクタ */
    public SyncUpsertGoodsStockDisplayConsumer() {
    }

    /**
     * Consumer
     *
     * @param message メッセージ
     */
    @RabbitListener(queues = "#{syncUpsertGoodsStockDisplayQueue.getName()}", errorHandler = "messageErrorHandler")
    public void consumer(SyncUpsertGoodsStockDisplayQueueMessage message) throws Exception {
        SyncUpsertGoodsStockDisplayProcessor syncUpsertGoodsStockDisplayProcessor =
                        ApplicationContextUtility.getBean(SyncUpsertGoodsStockDisplayProcessor.class);

        try {
            syncUpsertGoodsStockDisplayProcessor.processor(message);
        } catch (Exception e) {
            // AppLevelListExceptionの場合は、ErrorListを出力する
            if (e instanceof AppLevelListException) {
                for (AppLevelException ae : ((AppLevelListException) e).getErrorList()) {
                    LOGGER.error(ae.getMessage());
                }
            }
            // AppLevelListException以外の場合は、エラーメッセージを出力する
            else {
                LOGGER.error(e.getMessage());
            }
        }
    }
}