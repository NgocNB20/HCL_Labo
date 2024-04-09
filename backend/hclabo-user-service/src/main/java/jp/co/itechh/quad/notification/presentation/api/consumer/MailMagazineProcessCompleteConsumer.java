package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.MailMagazineProcessCompleteProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailMagazineProcessCompleteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * メルマガ登録完了
 *
 * @author Doan Thang (VJP)
 */
@Component
public class MailMagazineProcessCompleteConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MailMagazineProcessCompleteConsumer.class);

    public MailMagazineProcessCompleteConsumer() {
    }

    /**
     * メール送信
     *
     * @param mailMagazineProcessCompleteRequest メルマガ登録完了リクエスト
     */
    @RabbitListener(queues = "#{mailMagazineProcessCompleteQueue.getName()}",
                    errorHandler = "messageQueueExceptionHandler")
    public void sendMail(MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest) {
        MailMagazineProcessCompleteProcessor mailMagazineProcessCompleteProcessor =
                        ApplicationContextUtility.getBean(MailMagazineProcessCompleteProcessor.class);

        try {
            mailMagazineProcessCompleteProcessor.processor(mailMagazineProcessCompleteRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}