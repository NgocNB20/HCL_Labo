package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.StockReportProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockReportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 在庫状況を管理者にメール送信
 *
 * @author Doan Thang (VJP)
 */
@Component
public class StockReportConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(StockReportConsumer.class);

    public StockReportConsumer() {
    }

    /**
     * メール送信
     *
     * @param stockReportRequest 在庫状況を管理者にメール送信リクエスト
     * @throws Exception Exception
     */
    @RabbitListener(queues = "#{stockReportQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(StockReportRequest stockReportRequest) throws Exception {
        StockReportProcessor stockReportProcessor = ApplicationContextUtility.getBean(StockReportProcessor.class);

        try {
            stockReportProcessor.processor(stockReportRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
