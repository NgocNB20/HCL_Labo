/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.ExamResultsEntryProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamResultsEntryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 検査結果登録異常 Consumer
 *
 */
@Component
public class ExamResultsEntryConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExamResultsEntryConsumer.class);

    public ExamResultsEntryConsumer() {
    }

    /**
     * メール送信
     *
     * @param examResultsEntryRequest 検査結果登録リクエスト（エラー通知）
     */
    @RabbitListener(queues = "#{examResultsEntryQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(ExamResultsEntryRequest examResultsEntryRequest) {
        ExamResultsEntryProcessor examResultsEntryProcessor =
                        ApplicationContextUtility.getBean(ExamResultsEntryProcessor.class);

        try {
            examResultsEntryProcessor.processor(examResultsEntryRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}