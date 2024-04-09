package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.OrderRegistAlertProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.OrderRegistAlertRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 注文データ作成アラート
 *
 * @author Doan Thang (VJP)
 */
@Component
public class OrderRegistAlertConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderRegistAlertConsumer.class);

    public OrderRegistAlertConsumer() {
    }

    /**
     * メール送信
     *
     * @param orderRegistAlertRequest 注文データ作成アラートリクエスト
     */
    @RabbitListener(queues = "#{orderRegistAlertQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(OrderRegistAlertRequest orderRegistAlertRequest) {
        OrderRegistAlertProcessor orderRegistAlertProcessor =
                        ApplicationContextUtility.getBean(OrderRegistAlertProcessor.class);

        try {
            orderRegistAlertProcessor.processor(orderRegistAlertRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}