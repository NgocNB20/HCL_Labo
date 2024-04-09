package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.MulpayNotificationRecoveryAdministratorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MulpayNotificationRecoveryAdministratorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 入金結果受付予備処理結果
 *
 * @author Doan Thang (VJP)
 */
@Component
public class MulpayNotificationRecoveryAdministratorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MulpayNotificationRecoveryAdministratorConsumer.class);

    public MulpayNotificationRecoveryAdministratorConsumer() {
    }

    /**
     * メール送信
     *
     * @param mulpayNotificationRecoveryAdministratorRequest 入金結果受付予備処理結果メールリクエスト
     */
    @RabbitListener(queues = "#{mulpayNotificationRecoveryAdministratorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendAdministratorMail(MulpayNotificationRecoveryAdministratorRequest mulpayNotificationRecoveryAdministratorRequest) {
        MulpayNotificationRecoveryAdministratorProcessor mulpayNotificationRecoveryAdministratorProcessor = ApplicationContextUtility.getBean(MulpayNotificationRecoveryAdministratorProcessor.class);

        try {
            mulpayNotificationRecoveryAdministratorProcessor.processor(mulpayNotificationRecoveryAdministratorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
