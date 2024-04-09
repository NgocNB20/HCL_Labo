package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.GoodsAsynchronousErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsAsynchronousErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 商品グループ/規格画像更新エラー Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
public class GoodsAsynchronousErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GoodsAsynchronousErrorConsumer.class);

    public GoodsAsynchronousErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param goodsAsynchronousErrorRequest 商品グループ規格画像更新（商品一括アップロード）異常リクエスト
     */
    @RabbitListener(queues = "#{goodsAsynchronousErrorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(GoodsAsynchronousErrorRequest goodsAsynchronousErrorRequest) {
        GoodsAsynchronousErrorProcessor goodsAsynchronousErrorProcessor =
                        ApplicationContextUtility.getBean(GoodsAsynchronousErrorProcessor.class);

        try {
            goodsAsynchronousErrorProcessor.processor(goodsAsynchronousErrorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}