package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.MultipaymentAlertProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MultiPaymentAlertRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * マルチペイメントアラート
 *
 * @author Doan Thang (VJP)
 */
@Component
public class MultipaymentAlertConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MultipaymentAlertConsumer.class);

    public MultipaymentAlertConsumer() {
    }

    /**
     * メール送信
     *
     * @param multiPaymentAlertRequest マルチペイメントアラートリクエスト
     */
    @RabbitListener(queues = "#{multiPaymentAlertQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(MultiPaymentAlertRequest multiPaymentAlertRequest) {
        MultipaymentAlertProcessor multipaymentAlertProcessor =
                        ApplicationContextUtility.getBean(MultipaymentAlertProcessor.class);

        try {
            multipaymentAlertProcessor.processor(multiPaymentAlertRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}