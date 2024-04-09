package jp.co.itechh.quad.ddd.presentation.reports.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.queue.QueueMessage;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.presentation.reports.api.processor.ReportsRegistProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集計用販売データ登録用Consumer
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ReportsRegistConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ReportsRegistConsumer.class);

    /**
     * 集計用販売データの追加、を行う
     *
     * @param message メッセージ
     */
    @RabbitListener(queues = "#{reportsRegistQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void consumer(QueueMessage message) {

        ReportsRegistProcessor reportsRegistProcessor = ApplicationContextUtility.getBean(ReportsRegistProcessor.class);

        try {
            reportsRegistProcessor.processor(message);
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