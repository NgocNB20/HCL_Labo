package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.OrderConfirmationProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.OrderConfirmationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 注文確認
 *
 * @author Doan Thang (VJP)
 */
@Component
public class OrderConfirmationConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderConfirmationConsumer.class);

    public OrderConfirmationConsumer() {
    }

    /**
     * メール送信
     *
     * @param orderConfirmationRequest 注文確認リクエスト
     */
    @RabbitListener(queues = "#{orderConfirmationQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(OrderConfirmationRequest orderConfirmationRequest) {
        OrderConfirmationProcessor orderConfirmationProcessor =
                        ApplicationContextUtility.getBean(OrderConfirmationProcessor.class);

        try {
            orderConfirmationProcessor.processor(orderConfirmationRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}