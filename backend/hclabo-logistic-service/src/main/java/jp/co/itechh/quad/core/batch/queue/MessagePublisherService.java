package jp.co.itechh.quad.core.batch.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * キューパブリッシャーサービス
 *
 * @author Doan Thang (VJP)
 */
@Service
public class MessagePublisherService {
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePublisherService.class);

    /** Rabbitテンプレート */
    private final RabbitTemplate rabbitTemplate;

    /**
     * コンストラクタ
     *
     * @param rabbitTemplate Rabbitテンプレート
     */
    @Autowired
    public MessagePublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * パブリッシュー
     *
     * @param routingKey ルーティングキー
     * @param message メッセージ
     */
    public void publish(String routingKey, Object message) {
        try {
            LOGGER.info("【Publish】ルーティングキー： " + routingKey);
            rabbitTemplate.convertAndSend(routingKey, message);
        } catch (AmqpException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new AmqpException(e.getMessage());
        }
    }
}
