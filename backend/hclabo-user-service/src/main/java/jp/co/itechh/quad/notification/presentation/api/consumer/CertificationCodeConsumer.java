package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.CertificationCodeProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CertificationCodeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 認証コード
 *
 */
@Component
public class CertificationCodeConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CertificationCodeConsumer.class);

    public CertificationCodeConsumer() {
    }

    /**
     * メール送信
     *
     * @param certificationCodeRequest 認証コードメール送信
     */
    @RabbitListener(queues = "#{certificationCodeQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(CertificationCodeRequest certificationCodeRequest) {
        CertificationCodeProcessor certificationCodeProcessor =
                        ApplicationContextUtility.getBean(CertificationCodeProcessor.class);

        try {
            certificationCodeProcessor.processor(certificationCodeRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}