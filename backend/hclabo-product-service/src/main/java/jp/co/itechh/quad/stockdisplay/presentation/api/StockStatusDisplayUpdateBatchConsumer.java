package jp.co.itechh.quad.stockdisplay.presentation.api;

import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.stockdisplay.presentation.api.processor.StockStatusDisplayUpdateBatchProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 商品グループ在庫状態更新 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
public class StockStatusDisplayUpdateBatchConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(StockStatusDisplayUpdateBatchConsumer.class);

    /**
     * コンストラクタ
     */
    public StockStatusDisplayUpdateBatchConsumer() {
    }

    /**
     * Consumer
     *
     * @param batchQueueMessagemessage メッセージ
     */
    @RabbitListener(queues = "#{stockStatusDisplayUpdateQueue.getName()}", errorHandler = "messageErrorHandler")
    public void consumer(BatchQueueMessage batchQueueMessagemessage) {
        StockStatusDisplayUpdateBatchProcessor stockStatusDisplayUpdateBatchProcessor =
                        ApplicationContextUtility.getBean(StockStatusDisplayUpdateBatchProcessor.class);

        try {
            stockStatusDisplayUpdateBatchProcessor.processor(batchQueueMessagemessage);
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