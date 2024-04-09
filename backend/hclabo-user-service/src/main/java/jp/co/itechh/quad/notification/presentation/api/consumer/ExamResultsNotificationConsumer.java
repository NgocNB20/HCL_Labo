/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.ExamResultsNotificationProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamResultsNoticeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 検査結果通知メール Consumer
 *
 */
@Component
public class ExamResultsNotificationConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExamResultsNotificationConsumer.class);

    public ExamResultsNotificationConsumer() {
    }

    /**
     * メール送信
     *
     * @param examResultsNoticeRequest 検査結果通知メール送信リクエスト
     */
    @RabbitListener(queues = "#{examResultsNotificationQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(ExamResultsNoticeRequest examResultsNoticeRequest) {
        ExamResultsNotificationProcessor examResultsNotificationProcessor =
                        ApplicationContextUtility.getBean(ExamResultsNotificationProcessor.class);

        try {
            examResultsNotificationProcessor.processor(examResultsNoticeRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}