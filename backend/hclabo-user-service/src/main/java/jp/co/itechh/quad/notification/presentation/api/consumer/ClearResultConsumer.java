package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.ClearResultProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ClearResultRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * クリア通知
 *
 * @author Doan Thang (VJP)
 */
@Component
public class ClearResultConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClearResultConsumer.class);

    public ClearResultConsumer() {
    }

    /**
     * メール送信
     *
     * @param clearResultRequest クリア通知リクエスト
     */
    @RabbitListener(queues = "#{clearResultQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(ClearResultRequest clearResultRequest) {
        ClearResultProcessor clearResultProcessor = ApplicationContextUtility.getBean(ClearResultProcessor.class);

        try {
            clearResultProcessor.processor(clearResultRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}
