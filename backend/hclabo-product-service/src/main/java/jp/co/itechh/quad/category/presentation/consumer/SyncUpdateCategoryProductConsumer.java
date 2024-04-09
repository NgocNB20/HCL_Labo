/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.category.presentation.consumer;

import jp.co.itechh.quad.batch.queue.SyncUpdateCategoryProductQueueMessage;
import jp.co.itechh.quad.category.presentation.consumer.processor.SyncUpdateCategoryProductProcessor;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * カテゴリ商品更新 Consumer.
 */
@Component
public class SyncUpdateCategoryProductConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SyncUpdateCategoryProductConsumer.class);

    public SyncUpdateCategoryProductConsumer() {
    }

    /**
     * Consumer
     *
     * @param syncUpdateCategoryProductQueueMessage キューメッセージ
     */
    @RabbitListener(queues = "#{syncUpdateCategoryProductBatchQueue.getName()}", errorHandler = "messageErrorHandler")
    public void consumer(SyncUpdateCategoryProductQueueMessage syncUpdateCategoryProductQueueMessage) {
        SyncUpdateCategoryProductProcessor syncUpdateCategoryProductProcessor =
                        ApplicationContextUtility.getBean(SyncUpdateCategoryProductProcessor.class);

        try {
            syncUpdateCategoryProductProcessor.processor(syncUpdateCategoryProductQueueMessage);
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