package jp.co.itechh.quad.registasynchronous.presentation.api;

import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.registasynchronous.presentation.api.processor.RegistAsynchronousBatchProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品登録非同期 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
public class RegistAsynchronousBatchConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(RegistAsynchronousBatchConsumer.class);

    /**
     * コンストラクタ
     */
    @Autowired
    public RegistAsynchronousBatchConsumer() {
    }

    /**
     * Consumer
     *
     * @param batchQueueMessage バッチキューメッセージ
     * @param message           メッセージ
     */
    @RabbitListener(queues = "#{registAsynchronousQueue.getName()}", errorHandler = "messageErrorHandler")
    public void consumer(BatchQueueMessage batchQueueMessage, Message message) {
        RegistAsynchronousBatchProcessor registAsynchronousBatchProcessor =
                        ApplicationContextUtility.getBean(RegistAsynchronousBatchProcessor.class);
        try {
            registAsynchronousBatchProcessor.processor(batchQueueMessage);
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