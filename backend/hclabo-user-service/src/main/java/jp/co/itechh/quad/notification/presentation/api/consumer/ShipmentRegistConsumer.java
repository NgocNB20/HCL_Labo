package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.ShipmentRegistProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ShipmentRegistRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 出荷登録異常
 *
 * @author Doan Thang (VJP)
 */
@Component
public class ShipmentRegistConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ShipmentRegistConsumer.class);

    public ShipmentRegistConsumer() {
    }

    /**
     * メール送信
     *
     * @param shipmentRegistRequest 出荷登録異常リクエスト
     */
    @RabbitListener(queues = "#{shipmentRegistQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(ShipmentRegistRequest shipmentRegistRequest) {
        ShipmentRegistProcessor shipmentRegistProcessor =
                        ApplicationContextUtility.getBean(ShipmentRegistProcessor.class);

        try {
            shipmentRegistProcessor.processor(shipmentRegistRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}