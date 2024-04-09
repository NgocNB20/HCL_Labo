package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.AuthTimeLimitErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.AuthTimeLimitErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * オーソリ期限切れ間近注文異常
 *
 * @author Doan Thang (VJP)
 */
@Component
public class AuthTimeLimitErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AuthTimeLimitErrorConsumer.class);

    public AuthTimeLimitErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param authTimeLimitErrorRequest オーソリ期限切れ間近注文異常リクエスト
     */
    @RabbitListener(queues = "#{authTimeLimitErrorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(AuthTimeLimitErrorRequest authTimeLimitErrorRequest) {
        AuthTimeLimitErrorProcessor authTimeLimitErrorProcessor =
                        ApplicationContextUtility.getBean(AuthTimeLimitErrorProcessor.class);

        try {
            authTimeLimitErrorProcessor.processor(authTimeLimitErrorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}