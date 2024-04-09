package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.PasswordNotificationProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PasswordNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * パスワード再設定
 *
 * @author Doan Thang (VJP)
 */
@Component
public class PasswordNotificationConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(PasswordNotificationConsumer.class);

    public PasswordNotificationConsumer() {
    }

    /**
     * メール送信
     *
     * @param passwordNotificationRequest パスワード再設定メール送信リクエスト
     */
    @RabbitListener(queues = "#{passwordNotificationQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(PasswordNotificationRequest passwordNotificationRequest) {
        PasswordNotificationProcessor passwordNotificationProcessor =
                        ApplicationContextUtility.getBean(PasswordNotificationProcessor.class);

        try {
            passwordNotificationProcessor.processor(passwordNotificationRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}