package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.GoodsAsynchronousProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsAsynchronousRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 商品グループ/規格画像更新 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
public class GoodsAsynchronousConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GoodsAsynchronousConsumer.class);

    public GoodsAsynchronousConsumer() {
    }

    /**
     * メール送信
     *
     * @param goodsAsynchronousRequest パスワード再設定メール送信リクエスト
     */
    @RabbitListener(queues = "#{goodsAsynchronousQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(GoodsAsynchronousRequest goodsAsynchronousRequest) {
        GoodsAsynchronousProcessor goodsAsynchronousProcessor =
                        ApplicationContextUtility.getBean(GoodsAsynchronousProcessor.class);

        try {
            goodsAsynchronousProcessor.processor(goodsAsynchronousRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}