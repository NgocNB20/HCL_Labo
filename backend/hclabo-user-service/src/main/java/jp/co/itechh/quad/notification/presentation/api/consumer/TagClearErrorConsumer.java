package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.TagClearErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.TagClearErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * タグクリアバッチ異常
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class TagClearErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(TagClearErrorConsumer.class);

    public TagClearErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param tagClearErrorRequest タグクリアバッチ異常リクエスト
     */
    @RabbitListener(queues = "#{tagClearQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(TagClearErrorRequest tagClearErrorRequest) {
        TagClearErrorProcessor tagClearErrorProcessor = ApplicationContextUtility.getBean(TagClearErrorProcessor.class);

        try {
            tagClearErrorProcessor.processor(tagClearErrorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}