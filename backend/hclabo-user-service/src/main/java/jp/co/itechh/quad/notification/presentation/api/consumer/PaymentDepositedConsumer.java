package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.PaymentDepositedProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PaymentDepositedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 入金完了メール送信要求Consumer
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class PaymentDepositedConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(PaymentDepositedConsumer.class);

    /** コンストラクタ */
    public PaymentDepositedConsumer() {
    }

    /**
     * 入金完了メール送信要求
     *
     * @param paymentDepositedRequest 入金完了メール送信要求リクエスト
     */
    @RabbitListener(queues = "#{paymentDepositedQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(PaymentDepositedRequest paymentDepositedRequest) {
        PaymentDepositedProcessor paymentDepositedProcessor =
                        ApplicationContextUtility.getBean(PaymentDepositedProcessor.class);

        try {
            paymentDepositedProcessor.processor(paymentDepositedRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}