package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.SettlementReminderProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementReminderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 受注決済督促
 *
 * @author Doan Thang (VJP)
 */
@Component
public class SettlementReminderConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SettlementReminderConsumer.class);

    public SettlementReminderConsumer() {
    }

    /**
     * メール送信
     *
     * @param settlementReminderRequest 受注決済督促リクエスト
     */
    @RabbitListener(queues = "#{settlementReminderQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(SettlementReminderRequest settlementReminderRequest) {

        SettlementReminderProcessor settlementReminderProcessor =
                        ApplicationContextUtility.getBean(SettlementReminderProcessor.class);

        try {
            settlementReminderProcessor.processor(settlementReminderRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}