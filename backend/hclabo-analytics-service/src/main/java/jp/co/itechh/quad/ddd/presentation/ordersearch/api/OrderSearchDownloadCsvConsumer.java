package jp.co.itechh.quad.ddd.presentation.ordersearch.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.presentation.ordersearch.api.processor.OrderSearchCsvDownloadProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 受注検索 CSVダウンロード Consumer
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSearchDownloadCsvConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderSearchDownloadCsvConsumer.class);

    /**
     * 受注CSVダウンロードを行うConsumer
     *
     * @param queueMessage メッセージ
     */
    @RabbitListener(queues = "#{orderSearchDownloadOrderCsvQueue.getName()}",
                    errorHandler = "messageQueueExceptionHandler")
    public void consumerOrderCsv(QueueMessage queueMessage) {

        OrderSearchCsvDownloadProcessor orderSearchCsvDownloadProcessor =
                        ApplicationContextUtility.getBean(OrderSearchCsvDownloadProcessor.class);

        try {
            orderSearchCsvDownloadProcessor.processorOrderCsv(queueMessage);
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

    /**
     * 出荷CSVダウンロードを行うConsumer
     *
     * @param queueMessage メッセージ
     */
    @RabbitListener(queues = "#{orderSearchDownloadShipmentCsvQueue.getName()}",
                    errorHandler = "messageQueueExceptionHandler")
    public void consumerShipmentCsv(QueueMessage queueMessage) {

        OrderSearchCsvDownloadProcessor orderSearchCsvDownloadProcessor =
                        ApplicationContextUtility.getBean(OrderSearchCsvDownloadProcessor.class);

        try {
            orderSearchCsvDownloadProcessor.processorShipmentCsv(queueMessage);
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