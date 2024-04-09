package jp.co.itechh.quad.imageupdate.presentation.api;

import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.imageupdate.presentation.api.processor.ImageUpdateBatchProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 商品画像更新 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
public class ImageUpdateBatchConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ImageUpdateBatchConsumer.class);

    /**
     * コンストラクタ
     *
     */
    public ImageUpdateBatchConsumer() {
    }

    /**
     * Consumer
     *
     * @param batchQueueMessage メッセージ
     */
    @RabbitListener(queues = "#{imageUpdateQueue.getName()}", errorHandler = "messageErrorHandler")
    public void consumer(BatchQueueMessage batchQueueMessage) {
        ImageUpdateBatchProcessor imageUpdateBatchProcessor =
                        ApplicationContextUtility.getBean(ImageUpdateBatchProcessor.class);

        try {
            imageUpdateBatchProcessor.processor(batchQueueMessage);
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