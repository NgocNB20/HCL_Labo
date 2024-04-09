package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.SettlementAdministratorErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementAdministratorErrorMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 支払督促／支払期限切れ処理結果エラー
 *
 * @author Doan Thang (VJP)
 */
@Component
public class SettlementAdministratorErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SettlementAdministratorErrorConsumer.class);

    public SettlementAdministratorErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param settlementAdministratorErrorMailRequest 支払督促メール送信／支払期限切れ処理結果エラーリクエスト
     */
    @RabbitListener(queues = "#{settlementAdministratorErrorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(SettlementAdministratorErrorMailRequest settlementAdministratorErrorMailRequest) {
        SettlementAdministratorErrorProcessor settlementAdministratorErrorProcessor =
                        ApplicationContextUtility.getBean(SettlementAdministratorErrorProcessor.class);

        try {
            settlementAdministratorErrorProcessor.processor(settlementAdministratorErrorMailRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}