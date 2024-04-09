package jp.co.itechh.quad.ddd.presentation.ordersearch.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.presentation.ordersearch.api.processor.OrderSearchRegistUpdateProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 受注検索 Consumer
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSearchRegistUpdateConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderSearchRegistUpdateConsumer.class);

    /**
     * 郵便番号データの追加、更新を行うConsumer
     *
     * @param message メッセージ
     */
    @RabbitListener(queues = "#{orderSearchRegistUpdateQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void consumer(QueueMessage message) {

        OrderSearchRegistUpdateProcessor orderSearchProcessor =
                        ApplicationContextUtility.getBean(OrderSearchRegistUpdateProcessor.class);

        try {
            orderSearchProcessor.processor(message);
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