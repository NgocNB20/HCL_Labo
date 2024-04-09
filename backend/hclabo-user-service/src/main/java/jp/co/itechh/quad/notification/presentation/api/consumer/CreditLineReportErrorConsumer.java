package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.CreditLineReportErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CreditLineReportErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * クレジットラインレポートエラー
 *
 * @author Doan Thang (VJP)
 */
@Component
public class CreditLineReportErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CreditLineReportErrorConsumer.class);

    public CreditLineReportErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param creditLineReportErrorRequest クレジットラインレポートエラーリクエスト
     */
    @RabbitListener(queues = "#{creditLineReportErrorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendAdministratorMail(CreditLineReportErrorRequest creditLineReportErrorRequest) {
        CreditLineReportErrorProcessor creditLineReportErrorProcessor =
                        ApplicationContextUtility.getBean(CreditLineReportErrorProcessor.class);

        try {
            creditLineReportErrorProcessor.processor(creditLineReportErrorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}