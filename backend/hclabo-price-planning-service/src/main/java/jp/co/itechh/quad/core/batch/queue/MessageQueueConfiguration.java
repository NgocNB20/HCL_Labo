package jp.co.itechh.quad.core.batch.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * キューコンフィグレーション
 *
 * @author kimura
 */
@Configuration
public class MessageQueueConfiguration {

    /** クリアバッチルーティングキー */
    @Value("${queue.clear.routing}")
    private String clearRouting;

    /** クリアバッチ DirectExchange */
    @Value("${queue.clear.direct}")
    private String clearDirect;

    /** クリアバッチキュー定義 */
    @Bean
    Queue clearQueue() {
        return QueueBuilder.durable(clearRouting).build();
    }

    /**  クリアバッチDirectExchange */
    @Bean
    DirectExchange clearDirectExchange() {
        return new DirectExchange(clearDirect);
    }

    /**
     * クリアバッチBinding
     */
    @Bean
    Binding clearBinding(Queue clearQueue, DirectExchange clearDirectExchange) {
        return BindingBuilder.bind(clearQueue).to(clearDirectExchange).with(clearRouting);
    }

    /** コンバータ */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}