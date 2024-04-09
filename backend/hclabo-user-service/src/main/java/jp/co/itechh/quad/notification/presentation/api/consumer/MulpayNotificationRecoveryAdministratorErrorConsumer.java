package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.MulpayNotificationRecoveryAdministratorErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MulpayNotificationRecoveryAdministratorErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 入金結果受付予備処理結果異常
 *
 * @author Doan Thang (VJP)
 */
@Component
public class MulpayNotificationRecoveryAdministratorErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MulpayNotificationRecoveryAdministratorErrorConsumer.class);

    public MulpayNotificationRecoveryAdministratorErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param mulpayNotificationRecoveryAdministratorRequest 入金結果受付予備処理結果メールリクエスト
     */
    @RabbitListener(queues = "#{mulpayNotificationRecoveryAdministratorErrorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendAdministratorMail(MulpayNotificationRecoveryAdministratorErrorRequest mulpayNotificationRecoveryAdministratorRequest) {
        MulpayNotificationRecoveryAdministratorErrorProcessor mulpayNotificationRecoveryAdministratorErrorProcessor =
                        ApplicationContextUtility.getBean(MulpayNotificationRecoveryAdministratorErrorProcessor.class);

        try {
            mulpayNotificationRecoveryAdministratorErrorProcessor.processor(mulpayNotificationRecoveryAdministratorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}