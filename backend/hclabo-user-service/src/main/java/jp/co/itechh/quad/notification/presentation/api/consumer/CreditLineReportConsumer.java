package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.CreditLineReportProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.CreditLineReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * クレジットラインレポート
 *
 * @author Doan Thang (VJP)
 */
@Component
public class CreditLineReportConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AuthTimeLimitErrorConsumer.class);

    public CreditLineReportConsumer() {
    }

    /**
     * メール送信
     *
     * @param creditLineReportRequest クレジットラインレポートリクエスト
     */
    @RabbitListener(queues = "#{creditLineReportQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendAdministratorMail(CreditLineReportRequest creditLineReportRequest) {
        CreditLineReportProcessor creditLineReportProcessor =
                        ApplicationContextUtility.getBean(CreditLineReportProcessor.class);

        try {
            creditLineReportProcessor.processor(creditLineReportRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}