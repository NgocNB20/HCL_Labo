package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.GoodsImageUpdateProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 商品グループ規格画像更新 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
public class GoodsImageUpdateConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GoodsImageUpdateConsumer.class);

    public GoodsImageUpdateConsumer() {
    }

    /**
     * メール送信
     *
     * @param goodsImageUpdateRequest 商品グループ規格画像更新通知リクエスト
     */
    @RabbitListener(queues = "#{goodsImageUpdateQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendMail(GoodsImageUpdateRequest goodsImageUpdateRequest) {
        GoodsImageUpdateProcessor goodsImageUpdateProcessor =
                        ApplicationContextUtility.getBean(GoodsImageUpdateProcessor.class);

        try {
            goodsImageUpdateProcessor.processor(goodsImageUpdateRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}