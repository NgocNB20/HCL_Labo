package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.MemberInfoProcessCompleteProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MemberinfoProcessCompleteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 会員処理完了メール送信
 *
 * @author Doan Thang (VJP)
 */
@Component
public class MemberInfoProcessCompleteConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MemberInfoProcessCompleteConsumer.class);

    public MemberInfoProcessCompleteConsumer() {
    }

    /**
     * メール送信
     *
     * @param memberinfoProcessCompleteRequest 会員処理完了メール送信リクエスト
     */
    @RabbitListener(queues = "#{memberinfoProcessCompleteQueue.getName()}",
                    errorHandler = "messageQueueExceptionHandler")
    public void sendMail(MemberinfoProcessCompleteRequest memberinfoProcessCompleteRequest) {
        MemberInfoProcessCompleteProcessor memberInfoProcessCompleteProcessor =
                        ApplicationContextUtility.getBean(MemberInfoProcessCompleteProcessor.class);

        try {
            memberInfoProcessCompleteProcessor.processor(memberinfoProcessCompleteRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}