/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.CategoryGoodsRegistUpdateErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CategoryGoodsRegistUpdateErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * カテゴリ商品更新バッチ異常
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class CategoryGoodsRegistUpdateErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CategoryGoodsRegistUpdateErrorConsumer.class);

    public CategoryGoodsRegistUpdateErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param categoryGoodsRegistUpdateErrorRequest カテゴリ商品更新バッチ異常リクエスト
     */
    @RabbitListener(queues = "#{categoryGoodsRegistUpdateQueue.getName()}",
                    errorHandler = "messageQueueExceptionHandler")
    public void sendMail(CategoryGoodsRegistUpdateErrorRequest categoryGoodsRegistUpdateErrorRequest) {
        CategoryGoodsRegistUpdateErrorProcessor categoryGoodsRegistUpdateErrorProcessor =
                        ApplicationContextUtility.getBean(CategoryGoodsRegistUpdateErrorProcessor.class);

        try {
            categoryGoodsRegistUpdateErrorProcessor.processor(categoryGoodsRegistUpdateErrorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}