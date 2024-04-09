package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.ZipcodeUpdateProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ZipcodeUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 事業所郵便番号更新
 *
 * @author Doan Thang (VJP)
 */
@Component
public class ZipcodeUpdateConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ZipcodeUpdateConsumer.class);

    public ZipcodeUpdateConsumer() {
    }

    /**
     * メール送信
     *
     * @param zipcodeUpdateRequest 事業所郵便番号更新リクエスト
     */
    @RabbitListener(queues = "#{zipcodeUpdateQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendAdministratorMail(ZipcodeUpdateRequest zipcodeUpdateRequest) {
        ZipcodeUpdateProcessor zipcodeUpdateProcessor = ApplicationContextUtility.getBean(ZipcodeUpdateProcessor.class);

        try {
            zipcodeUpdateProcessor.processor(zipcodeUpdateRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
