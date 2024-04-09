package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.LinkpayCancelReminderProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.LinkpayCancelReminderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * GMO決済キャンセル漏れConsumer
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class LinkpayCancelReminderConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(LinkpayCancelReminderConsumer.class);

    public LinkpayCancelReminderConsumer() {
    }

    /**
     * メール送信
     *
     * @param linkpayCancelReminderRequest GMO決済キャンセル漏れメール送信要求リクエスト
     */
    @RabbitListener(queues = "#{linkpayCancelReminderQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(LinkpayCancelReminderRequest linkpayCancelReminderRequest) {
        LinkpayCancelReminderProcessor linkpayCancelReminderProcessor =
                        ApplicationContextUtility.getBean(LinkpayCancelReminderProcessor.class);

        try {
            linkpayCancelReminderProcessor.processor(linkpayCancelReminderRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}