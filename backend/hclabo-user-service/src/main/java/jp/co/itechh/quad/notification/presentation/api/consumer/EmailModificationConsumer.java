package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.EmailModificationProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.EmailModificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * メールアドレス変更確認
 *
 * @author Doan Thang (VJP)
 */
@Component
public class EmailModificationConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(EmailModificationConsumer.class);

    public EmailModificationConsumer() {
    }

    /**
     * メール送信
     *
     * @param emailModificationRequest メールアドレス変更確認リクエスト
     */
    @RabbitListener(queues = "#{emailModificationQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(EmailModificationRequest emailModificationRequest) {
        EmailModificationProcessor emailModificationProcessor =
                        ApplicationContextUtility.getBean(EmailModificationProcessor.class);

        try {
            emailModificationProcessor.processor(emailModificationRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}