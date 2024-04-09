package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.SettlementMismatchProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 請求不整合報告
 *
 * @author Doan Thang (VJP)
 */
@Component
public class SettlementMismatchConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SettlementMismatchConsumer.class);

    public SettlementMismatchConsumer() {
    }

    /**
     * メール送信
     *
     * @param settlementMismatchRequest 通知サブドメインリストリクエスト
     */
    @RabbitListener(queues = "#{settlementMismatchQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(SettlementMismatchRequest settlementMismatchRequest) {
        SettlementMismatchProcessor settlementMismatchProcessor =
                        ApplicationContextUtility.getBean(SettlementMismatchProcessor.class);

        try {
            settlementMismatchProcessor.processor(settlementMismatchRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}