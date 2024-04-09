package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.StockReleaserProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockReleaseAdministratorErrorMailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 在庫開放
 *
 * @author Doan Thang (VJP)
 */
@Component
public class StockReleaserConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(StockReleaserConsumer.class);

    public StockReleaserConsumer() {
    }

    /**
     * メール送信
     *
     * @param stockReleaseAdministratorErrorMailRequest 在庫開放リクエスト
     */
    @RabbitListener(queues = "#{stockReleaseQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(StockReleaseAdministratorErrorMailRequest stockReleaseAdministratorErrorMailRequest) {
        StockReleaserProcessor stockReleaserProcessor = ApplicationContextUtility.getBean(StockReleaserProcessor.class);

        try {
            stockReleaserProcessor.processor(stockReleaseAdministratorErrorMailRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}
