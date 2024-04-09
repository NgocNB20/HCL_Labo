package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.ShipmentNotificationProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ShipmentNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 出荷完了メール送信
 *
 * @author Doan Thang (VJP)
 */
@Component
public class ShipmentNotificationConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ShipmentNotificationConsumer.class);

    public ShipmentNotificationConsumer() {
    }

    /**
     * メール送信
     *
     * @param shipmentNotificationRequest 出荷完了メール送信リクエスト
     */
    @RabbitListener(queues = "#{shipmentNotificationQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(ShipmentNotificationRequest shipmentNotificationRequest) {
        ShipmentNotificationProcessor shipmentNotificationProcessor =
                        ApplicationContextUtility.getBean(ShipmentNotificationProcessor.class);

        try {
            shipmentNotificationProcessor.processor(shipmentNotificationRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}