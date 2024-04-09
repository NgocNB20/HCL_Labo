package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.StockStatusProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.StockStatusRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 商品グループ在庫状態更新異常
 *
 * @author Doan Thang (VJP)
 */
@Component
public class StockStatusConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(StockStatusConsumer.class);

    public StockStatusConsumer() {
    }

    /**
     * メール送信
     *
     * @param stockStatusRequest 商品グループ在庫状態更新異常リクエスト
     */
    @RabbitListener(queues = "#{stockStatusQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(StockStatusRequest stockStatusRequest) {
        StockStatusProcessor stockStatusProcessor = ApplicationContextUtility.getBean(StockStatusProcessor.class);

        try {
            stockStatusProcessor.processor(stockStatusRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
