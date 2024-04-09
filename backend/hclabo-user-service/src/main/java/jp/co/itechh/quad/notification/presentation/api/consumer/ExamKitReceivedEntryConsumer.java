package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.ExamkitReceivedEntryProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamkitReceivedEntryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 検査キット受領登録
 *
 */
@Component
public class ExamKitReceivedEntryConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExamKitReceivedEntryConsumer.class);

    public ExamKitReceivedEntryConsumer() {
    }

    /**
     * メール送信
     *
     * @param examkitReceivedEntryRequest 検査キット受領登録リクエスト（エラー通知）
     */
    @RabbitListener(queues = "#{examkitReceivedEntryQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(ExamkitReceivedEntryRequest examkitReceivedEntryRequest) {
        ExamkitReceivedEntryProcessor examkitReceivedEntryProcessor =
                        ApplicationContextUtility.getBean(ExamkitReceivedEntryProcessor.class);

        try {
            examkitReceivedEntryProcessor.processor(examkitReceivedEntryRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}