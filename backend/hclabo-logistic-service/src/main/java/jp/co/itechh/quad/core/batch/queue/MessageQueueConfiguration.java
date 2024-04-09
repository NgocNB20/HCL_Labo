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

    /** 在庫解放 ルーティングキー */
    @Value("${queue.inventoryrelease.routing}")
    private String inventoryReleaseRouting;

    /** 在庫解放 DirectExchange */
    @Value("${queue.inventoryrelease.direct}")
    private String inventoryReleaseDirect;

    /** 郵便番号自動更新ルーティングキー */
    @Value("${queue.zipcodeupdate.routing}")
    private String zipCodeUpdateRouting;

    /** 郵便番号自動更新 DirectExchange */
    @Value("${queue.zipcodeupdate.direct}")
    private String zipCodeUpdateDirect;

    /** 事業所郵便番号自動更新更新ルーティングキー */
    @Value("${queue.officezipcodeupdate.routing}")
    private String officezipcodeupdateRouting;

    /** 事業所郵便番号自動更新更新 DirectExchange */
    @Value("${queue.officezipcodeupdate.direct}")
    private String officezipcodeupdateDirect;

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

    /** 在庫解放 キュー定義 */
    @Bean
    Queue inventoryReleaseQueue() {
        return QueueBuilder.durable(inventoryReleaseRouting).build();
    }

    /** 在庫解放 DirectExchange */
    @Bean
    DirectExchange inventoryReleaseDirectExchange() {
        return new DirectExchange(this.inventoryReleaseDirect);
    }

    /** 在庫解放 Binding */
    @Bean
    Binding inventoryReleaseBinding(Queue inventoryReleaseQueue, DirectExchange inventoryReleaseDirectExchange) {
        return BindingBuilder.bind(inventoryReleaseQueue)
                             .to(inventoryReleaseDirectExchange)
                             .with(this.inventoryReleaseRouting);
    }

    /**
     * 郵便番号自動更新キュー定義
     */
    @Bean
    Queue zipCodeUpdateQueue() {
        return QueueBuilder.durable(zipCodeUpdateRouting).build();
    }

    /**
     * 事業所郵便番号自動更新更新キュー定義
     */
    @Bean
    Queue officezipcodeupdateQueue() {
        return QueueBuilder.durable(officezipcodeupdateRouting).build();
    }

    /**  DirectExchange */
    @Bean
    DirectExchange zipCodeUpdateDirectExchange() {
        return new DirectExchange(zipCodeUpdateDirect);
    }

    /**  DirectExchange */
    @Bean
    DirectExchange officezipcodeupdateDirectExchange() {
        return new DirectExchange(officezipcodeupdateDirect);
    }

    /**
     * 郵便番号自動更新 Binding
     */
    @Bean
    Binding zipCodeUpdateBinding(Queue zipCodeUpdateQueue, DirectExchange zipCodeUpdateDirectExchange) {
        return BindingBuilder.bind(zipCodeUpdateQueue).to(zipCodeUpdateDirectExchange).with(zipCodeUpdateRouting);
    }

    /**
     * 事業所郵便番号自動更新更新 Binding
     */
    @Bean
    Binding officezipcodeupdateBinding(Queue officezipcodeupdateQueue,
                                       DirectExchange officezipcodeupdateDirectExchange) {
        return BindingBuilder.bind(officezipcodeupdateQueue)
                             .to(officezipcodeupdateDirectExchange)
                             .with(officezipcodeupdateRouting);
    }

    /** コンバータ */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}