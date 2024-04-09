package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.AuthTimeLimitProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.AuthTimeLimitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * オーソリ期限切れ間近注文通知
 *
 * @author Doan Thang (VJP)
 */
@Component
public class AuthTimeLimitConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AuthTimeLimitConsumer.class);

    public AuthTimeLimitConsumer() {
    }

    /**
     * メール送信
     *
     * @param authTimeLimitRequest オーソリ期限切れ間近注文通知リクエスト
     */
    @RabbitListener(queues = "#{authTimeLimitQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(AuthTimeLimitRequest authTimeLimitRequest) {
        AuthTimeLimitProcessor authTimeLimitProcessor = ApplicationContextUtility.getBean(AuthTimeLimitProcessor.class);

        try {
            authTimeLimitProcessor.processor(authTimeLimitRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
