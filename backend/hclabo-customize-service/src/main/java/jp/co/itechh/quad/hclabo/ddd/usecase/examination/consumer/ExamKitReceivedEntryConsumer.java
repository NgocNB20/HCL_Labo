package jp.co.itechh.quad.hclabo.ddd.usecase.examination.consumer;

import jp.co.itechh.quad.hclabo.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.hclabo.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.hclabo.ddd.exception.DomainException;
import jp.co.itechh.quad.hclabo.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.hclabo.ddd.usecase.examination.processor.ExamKitReceivedEntryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 検査キット受領登録 CSVダウンロード Consumer
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ExamKitReceivedEntryConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExamKitReceivedEntryConsumer.class);

    /**
     * 検査キット受領登録CSVダウンロードを行うConsumer
     *
     * @param queueMessage メッセージ
     */
    @RabbitListener(queues = "#{examKitReceivedEntryQueue.getName()}",
                    errorHandler = "messageQueueExceptionHandler")
    public void consumerOrderCsv(BatchQueueMessage queueMessage) {

        ExamKitReceivedEntryProcessor examKitReceivedEntryProcessor =
                        ApplicationContextUtility.getBean(ExamKitReceivedEntryProcessor.class);

        try {
            examKitReceivedEntryProcessor.processor(queueMessage);
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