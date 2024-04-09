package jp.co.itechh.quad.notification.presentation.api.consumer;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.notification.presentation.api.processor.GoodsImageUpdateErrorProcessor;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateErrorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 商品画像更新エラー
 *
 * @author Doan Thang (VJP)
 */
@Component
public class GoodsImageUpdateErrorConsumer {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GoodsImageUpdateErrorConsumer.class);

    public GoodsImageUpdateErrorConsumer() {
    }

    /**
     * メール送信
     *
     * @param goodsImageUpdateErrorRequest 商品画像更新エラーリクエスト
     */
    @RabbitListener(queues = "#{goodsImageUpdateErrorQueue.getName()}", errorHandler = "messageQueueExceptionHandler")
    public void sendAdministratorErrorMail(GoodsImageUpdateErrorRequest goodsImageUpdateErrorRequest) {
        GoodsImageUpdateErrorProcessor goodsImageUpdateErrorProcessor =
                        ApplicationContextUtility.getBean(GoodsImageUpdateErrorProcessor.class);

        try {
            goodsImageUpdateErrorProcessor.processor(goodsImageUpdateErrorRequest);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}