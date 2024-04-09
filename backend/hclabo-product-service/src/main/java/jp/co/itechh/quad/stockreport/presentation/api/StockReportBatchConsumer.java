package jp.co.itechh.quad.stockreport.presentation.api;

import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.stockreport.presentation.api.processor.StockReportBatchProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 在庫状況確認メール送信 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
public class StockReportBatchConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(StockReportBatchConsumer.class);

    /**
     * コンストラクタ
     */
    public StockReportBatchConsumer() {
    }

    /**
     * Consumer
     *
     * @param message メッセージ
     */
    @RabbitListener(queues = "#{stockReportQueue.getName()}", errorHandler = "messageErrorHandler")
    public void consumer(BatchQueueMessage message) {
        StockReportBatchProcessor stockReportBatchProcessor =
                        ApplicationContextUtility.getBean(StockReportBatchProcessor.class);

        try {
            stockReportBatchProcessor.processor(message);
        } catch (Exception e) {
            // AppLevelListExceptionの場合は、ErrorListを出力する
            if (e instanceof AppLevelListException) {
                for (AppLevelException ae : ((AppLevelListException) e).getErrorList()) {
                    LOGGER.error(ae.getMessage());
                }
            }
            // AppLevelListException以外の場合は、エラーメッセージを出力する
            else {
                LOGGER.error(e.getMessage());
            }
        }
    }
}