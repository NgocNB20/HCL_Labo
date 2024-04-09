package jp.co.itechh.quad.ddd.presentation.reports.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.presentation.reports.api.processor.GoodsSaleDownloadCsvProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品販売個数集計CSVダウンロードConsumer
 */
@Component
public class GoodsSalesDownloadCsvConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GoodsSalesDownloadCsvConsumer.class);

    /**
     * Consumer
     *
     * @param queueMessage
     */
    @RabbitListener(queues = "#{goodsSalesDownloadCsvQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void consumer(QueueMessage queueMessage) {
        GoodsSaleDownloadCsvProcessor goodsSaleDownloadCsvProcessor =
                        ApplicationContextUtility.getBean(GoodsSaleDownloadCsvProcessor.class);
        if (queueMessage.getGoodsSaleQueryCondition().getAggregateTimeFrom() == null
            || queueMessage.getGoodsSaleQueryCondition().getAggregateTimeTo() == null) {
            LOGGER.error(String.valueOf(new ExceptionContent("ANALYTICS-GOODSALESE0002-E", null).getMessage()));
            return;
        }

        try {
            goodsSaleDownloadCsvProcessor.processor(queueMessage);
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