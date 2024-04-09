package jp.co.itechh.quad.product.presentation.consumer;

import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.product.usecase.batch.ProductPopularityTotalingBatchProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 人気商品ランキング集計バッチ Consumer
 */
@Component
public class GoodsGroupPopularityTotalingBatchConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GoodsGroupPopularityTotalingBatchConsumer.class);

    /**
     * コンストラクタ
     */
    public GoodsGroupPopularityTotalingBatchConsumer() {
    }

    /**
     * 人気商品ランキング集計バッチ 実行<br/>
     *
     */
    @RabbitListener(queues = "#{productPopularityTotalingBatchQueue.getName()}", errorHandler = "messageErrorHandler")
    public void consumer(BatchQueueMessage message) {

        ProductPopularityTotalingBatchProcessor productPopularityTotalingBatchProcessor =
                        ApplicationContextUtility.getBean(ProductPopularityTotalingBatchProcessor.class);

        try {
            productPopularityTotalingBatchProcessor.processor(message);
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