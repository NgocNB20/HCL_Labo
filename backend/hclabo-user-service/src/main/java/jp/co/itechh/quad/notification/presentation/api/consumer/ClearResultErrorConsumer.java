package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.ClearResultErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ClearResultErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * クリア異常
 *
 * @author Doan Thang (VJP)
 */
@Component
public class ClearResultErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ClearResultErrorConsumer.class);

    public ClearResultErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param clearResultErrorRequest クリア異常リクエスト
     */
    @RabbitListener(queues = "#{clearResultErrorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(ClearResultErrorRequest clearResultErrorRequest) {
        ClearResultErrorProcessor clearResultErrorProcessor =
                        ApplicationContextUtility.getBean(ClearResultErrorProcessor.class);

        try {
            clearResultErrorProcessor.processor(clearResultErrorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}