package jp.co.itechh.quad.ddd.presentation.reports.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.presentation.reports.api.processor.OrderSalesDownloadCsvProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 受注・売上集計CSV Consumer
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSalesDownloadCsvConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderSalesDownloadCsvConsumer.class);

    /**
     * Consumer.
     *
     * @param queueMessage メッセージ
     */
    @RabbitListener(queues = "#{orderSalesDownloadCsvQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void consumer(QueueMessage queueMessage) {
        OrderSalesDownloadCsvProcessor orderSalesDownloadCsvProcessor =
                        ApplicationContextUtility.getBean(OrderSalesDownloadCsvProcessor.class);
        if (queueMessage.getOrderSalesSearchQueryCondition().getAggregateTimeFrom() == null
            || queueMessage.getOrderSalesSearchQueryCondition().getAggregateTimeTo() == null) {
            LOGGER.error(String.valueOf(new ExceptionContent("ANALYTICS-ORSS0003-E", null).getMessage()));
            return;
        }

        try {
            orderSalesDownloadCsvProcessor.processor(queueMessage);
        } catch (Exception e) {
            if (e instanceof DomainException) {
                Set<Map.Entry<String, List<ExceptionContent>>> entries =
                                ((DomainException) e).getMessageMap().entrySet();

                for (Map.Entry<String, List<ExceptionContent>> entry : entries) {
                    for (ExceptionContent ec : entry.getValue()) {
                        LOGGER.error(ec.getMessage());
                    }
                }
            } else {
                LOGGER.error(e.getMessage());
            }
        }

    }
}