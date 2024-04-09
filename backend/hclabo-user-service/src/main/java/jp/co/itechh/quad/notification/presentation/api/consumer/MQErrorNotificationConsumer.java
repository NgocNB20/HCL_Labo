package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.MQErrorNotificationProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MQErrorNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 非同期処理(MQ)エラー通知　TODO　MQ異常系暫定対応
 *
 * @author Doan Thang (VJP)
 */
@Component
public class MQErrorNotificationConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MQErrorNotificationConsumer.class);

    public MQErrorNotificationConsumer() {
    }

    /**
     * メール送信
     *
     * @param mqErrorNotificationRequest 非同期処理(MQ)エラー通知リクエスト
     */
    @RabbitListener(queues = "#{mqErrorNotificationQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(MQErrorNotificationRequest mqErrorNotificationRequest) {
        MQErrorNotificationProcessor mqErrorNotificationProcessor =
                        ApplicationContextUtility.getBean(MQErrorNotificationProcessor.class);

        try {
            mqErrorNotificationProcessor.processor(mqErrorNotificationRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}