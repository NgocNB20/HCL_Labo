package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.SettlementAdministratorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementAdministratorMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 支払督促／支払期限切れ処理結果
 *
 * @author Doan Thang (VJP)
 */
@Component
public class SettlementAdministratorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(SettlementAdministratorConsumer.class);

    public SettlementAdministratorConsumer() {
    }

    /**
     * メール送信
     *
     * @param settlementAdministratorMailRequest 受注決済期限切れメール送信リクエスト
     */
    @RabbitListener(queues = "#{settlementAdministratorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(SettlementAdministratorMailRequest settlementAdministratorMailRequest) {
        SettlementAdministratorProcessor settlementAdministratorProcessor = ApplicationContextUtility.getBean(SettlementAdministratorProcessor.class);

        try {
            settlementAdministratorProcessor.processor(settlementAdministratorMailRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}
