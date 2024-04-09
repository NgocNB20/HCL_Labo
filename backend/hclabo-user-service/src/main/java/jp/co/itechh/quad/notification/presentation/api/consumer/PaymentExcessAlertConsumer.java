package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.PaymentExcessAlertProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PaymentExcessAlertRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 入金過不足アラートConsumer
 */
@Component
public class PaymentExcessAlertConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(PaymentExcessAlertConsumer.class);

    public PaymentExcessAlertConsumer() {
    }

    /**
     * メール送信
     *
     * @param paymentExcessAlertRequest 入金過不足アラートメール送信要求リクエスト
     */
    @RabbitListener(queues = "#{paymentExcessAlertQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(PaymentExcessAlertRequest paymentExcessAlertRequest) {

        PaymentExcessAlertProcessor paymentExcessAlertProcessor =
                        ApplicationContextUtility.getBean(PaymentExcessAlertProcessor.class);

        try {
            paymentExcessAlertProcessor.processor(paymentExcessAlertRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }
}