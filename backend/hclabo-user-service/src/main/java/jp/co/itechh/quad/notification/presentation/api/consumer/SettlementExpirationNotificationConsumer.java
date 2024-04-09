package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.SettlementExpirationNotificationProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementExpirationNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 受注決済期限切れメール送信
 *
 * @author Doan Thang (VJP)
 */
@Component
public class SettlementExpirationNotificationConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SettlementExpirationNotificationConsumer.class);

    public SettlementExpirationNotificationConsumer() {
    }

    /**
     * メール送信
     *
     * @param settlementExpirationNotificationRequest 受注決済期限切れメール送信リクエスト
     */
    @RabbitListener(queues = "#{settlementExpirationNotificationQueue.getName()}",
                    errorHandler = "messageQueueExceptionHandler")
    public void sendMail(SettlementExpirationNotificationRequest settlementExpirationNotificationRequest) {

        SettlementExpirationNotificationProcessor settlementExpirationNotificationProcessor =
                        ApplicationContextUtility.getBean(SettlementExpirationNotificationProcessor.class);

        try {
            settlementExpirationNotificationProcessor.processor(settlementExpirationNotificationRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}