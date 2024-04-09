package jp.co.itechh.quad.product.presentation.consumer;

import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.product.presentation.api.processor.TagClearProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * タグクリアバッチ Consumer
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class TagClearConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(TagClearConsumer.class);

    /**
     * タグクリアバッチ
     */
    public TagClearConsumer() {
    }

    /**
     * Consumerメソッド
     *
     * @param batchQueueMessage メッセージ
     */
    @RabbitListener(queues = "#{tagClearQueue.getName()}", errorHandler = "messageErrorHandler")
    public void consumer(BatchQueueMessage batchQueueMessage) {
        TagClearProcessor clearBatchProcessor = ApplicationContextUtility.getBean(TagClearProcessor.class);

        try {
            clearBatchProcessor.processor(batchQueueMessage);
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