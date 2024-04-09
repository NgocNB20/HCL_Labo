package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.InquiryProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.InquiryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 問い合わせメール送信
 *
 * @author Doan Thang (VJP)
 */
@Component
public class InquiryConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(InquiryConsumer.class);

    public InquiryConsumer() {
    }

    /**
     * メール送信
     *
     * @param inquiryRequest 問い合わせメール送信リクエスト
     */
    @RabbitListener(queues = "#{inquiryQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(InquiryRequest inquiryRequest) {
        InquiryProcessor inquiryProcessor = ApplicationContextUtility.getBean(InquiryProcessor.class);

        try {
            inquiryProcessor.processor(inquiryRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}
