package jp.co.itechh.quad.hclabo.core.batch.queue;

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
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Configuration
public class MessageQueueConfiguration {

    /** 検査キット受領登録バッチルーティングキー */
    @Value("${queue.examkitreceivedentry.routing}")
    private String examKitReceivedEntryRouting;

    /** 検査キット受領登録バッチDirectExchange */
    @Value("${queue.examkitreceivedentry.direct}")
    private String examKitReceivedEntryDirect;

    /** 検査結果登録バッチルーティングキー */
    @Value("${queue.examresultsentry.routing}")
    private String examResultsEntryRouting;

    /** 検査結果登録バッチDirectExchange */
    @Value("${queue.examresultsentry.direct}")
    private String examResultsEntryDirect;

    /** 検査キット受領登録バッチキュー定義 */
    @Bean
    Queue examKitReceivedEntryQueue() {
        return QueueBuilder.durable(this.examKitReceivedEntryRouting).build();
    }

    /** 検査結果登録バッチキュー定義 */
    @Bean
    Queue examResultsEntryQueue() {
        return QueueBuilder.durable(this.examResultsEntryRouting).build();
    }

    /** 検査キット受領登録バッチDirectExchange */
    @Bean
    DirectExchange examKitReceivedEntryDirectExchange() {
        return new DirectExchange(this.examKitReceivedEntryDirect);
    }

    /** 検査結果登録バッチDirectExchange */
    @Bean
    DirectExchange examResultsEntryDirectExchange() {
        return new DirectExchange(this.examResultsEntryDirect);
    }

    /** 検査キット受領登録バッチBinding */
    @Bean
    Binding examKitReceivedEntryBinding(Queue examKitReceivedEntryQueue,
                                        DirectExchange examKitReceivedEntryDirectExchange) {
        return BindingBuilder.bind(examKitReceivedEntryQueue)
                             .to(examKitReceivedEntryDirectExchange)
                             .with(this.examKitReceivedEntryRouting);
    }

    /** 検査キット受領登録バッチBinding */
    @Bean
    Binding examResultsEntryBinding(Queue examResultsEntryQueue,
                                        DirectExchange examResultsEntryDirectExchange) {
        return BindingBuilder.bind(examResultsEntryQueue)
                             .to(examResultsEntryDirectExchange)
                             .with(this.examResultsEntryRouting);
    }

    /** コンバータ */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}