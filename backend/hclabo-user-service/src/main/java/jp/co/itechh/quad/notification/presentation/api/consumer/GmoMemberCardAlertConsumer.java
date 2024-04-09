package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.GmoMemberCardAlertProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GmoMemberCardAlertRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * GMO会員カードアラート
 *
 * @author Doan Thang (VJP)
 */
@Component
public class GmoMemberCardAlertConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GmoMemberCardAlertConsumer.class);

    public GmoMemberCardAlertConsumer() {
    }

    /**
     * メール送信
     *
     * @param gmoMemberCardAlertRequest GMO会員カードアラートリクエスト
     */
    @RabbitListener(queues = "#{gmoMemberCardAlertQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(GmoMemberCardAlertRequest gmoMemberCardAlertRequest) {
        GmoMemberCardAlertProcessor gmoMemberCardAlertProcessor =
                        ApplicationContextUtility.getBean(GmoMemberCardAlertProcessor.class);

        try {
            gmoMemberCardAlertProcessor.processor(gmoMemberCardAlertRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}