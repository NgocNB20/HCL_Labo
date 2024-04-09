package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.MemberPreregistrationProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MemberPreregistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 仮会員登録
 *
 * @author Doan Thang (VJP)
 */
@Component
public class MemberPreregistrationConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MemberPreregistrationConsumer.class);

    public MemberPreregistrationConsumer() {
    }

    /**
     * メール送信
     *
     * @param memberPreregistrationRequest 仮会員登録リクエスト
     */
    @RabbitListener(queues = "#{memberPreregistrationQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(MemberPreregistrationRequest memberPreregistrationRequest) {
        MemberPreregistrationProcessor memberPreregistrationProcessor =
                        ApplicationContextUtility.getBean(MemberPreregistrationProcessor.class);

        try {
            memberPreregistrationProcessor.processor(memberPreregistrationRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}