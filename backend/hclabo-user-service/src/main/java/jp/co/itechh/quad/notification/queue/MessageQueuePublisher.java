package jp.co.itechh.quad.notification.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * キューパブリッシャーサービス
 *
 * @author Doan Thang (VJP)
 */
@Component
public class MessageQueuePublisher {
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageQueuePublisher.class);

    /** Rabbitテンプレート */
    private final RabbitTemplate rabbitTemplate;

    /**
     * コンストラクタ
     *
     * @param rabbitTemplate Rabbitテンプレート
     */
    @Autowired
    public MessageQueuePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * パブリッシュー
     *
     * @param routingKey ルーティングキー
     * @param message    メッセージ
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
