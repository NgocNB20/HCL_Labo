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

    /** 与信枠解放 ルーティングキー */
    @Value("${queue.creditlinerelease.routing}")
    private String creditLineReleaseRouting;

    /** 与信枠解放 DirectExchange */
    @Value("${queue.creditlinerelease.direct}")
    private String creditLineReleaseDirect;

    /** オーソリ期限間近注文警告通知 ルーティングキー */
    @Value("${queue.authnotification.routing}")
    private String authNotificationRouting;

    /** オーソリ期限間近注文警告通知 DirectExchange */
    @Value("${queue.authnotification.direct}")
    private String authNotificationDirect;

    /** クリアバッチルーティングキー */
    @Value("${queue.clear.routing}")
    private String clearRouting;

    /** クリアバッチ DirectExchange */
    @Value("${queue.clear.direct}")
    private String clearDirect;

    /** GMO決済キャンセル漏れルーティングキー */
    @Value("${queue.linkpaycancelforget.routing}")
    private String linkPayCancelForgetRouting;

    /** GMO決済キャンセル漏れDirectExchange */
    @Value("${queue.linkpaycancelforget.direct}")
    private String linkPayCancelForgetDirect;

    /** クリアバッチキュー定義 */
    @Bean
    Queue clearQueue() {
        return QueueBuilder.durable(clearRouting).build();
    }

    /** クリアバッチDirectExchange */
    @Bean
    DirectExchange clearDirectExchange() {
        return new DirectExchange(clearDirect);
    }

    /** 与信枠解放 キュー定義 */
    @Bean
    Queue creditLineReleaseQueue() {
        return QueueBuilder.durable(this.creditLineReleaseRouting).build();
    }

    /** 与信枠解放 DirectExchange */
    @Bean
    DirectExchange creditLineReleaseDirectExchange() {
        return new DirectExchange(this.creditLineReleaseDirect);
    }

    /** 与信枠解放 Binding */
    @Bean
    Binding creditLineReleaseBinding(Queue creditLineReleaseQueue, DirectExchange creditLineReleaseDirectExchange) {
        return BindingBuilder.bind(creditLineReleaseQueue)
                             .to(creditLineReleaseDirectExchange)
                             .with(this.creditLineReleaseRouting);
    }

    /** オーソリ期限間近注文警告通知 キュー定義 */
    @Bean
    Queue authNotificationQueue() {
        return QueueBuilder.durable(this.authNotificationRouting).build();
    }

    /** オーソリ期限間近注文警告通知 DirectExchange */
    @Bean
    DirectExchange authNotificationDirectExchange() {
        return new DirectExchange(this.authNotificationDirect);
    }

    /** オーソリ期限間近注文警告通知 Binding */
    @Bean
    Binding authNotificationBinding(Queue authNotificationQueue, DirectExchange authNotificationDirectExchange) {
        return BindingBuilder.bind(authNotificationQueue)
                             .to(authNotificationDirectExchange)
                             .with(this.authNotificationRouting);
    }

    /**
     * クリアバッチBinding
     */
    @Bean
    Binding clearBinding(Queue clearQueue, DirectExchange clearDirectExchange) {
        return BindingBuilder.bind(clearQueue).to(clearDirectExchange).with(clearRouting);
    }

    /**
     * GMO決済キャンセル漏れキュー定義
     */
    @Bean
    Queue linkPayCancelForgetQueue() {
        return QueueBuilder.durable(this.linkPayCancelForgetRouting).build();
    }

    /**
     * GMO決済キャンセル漏れDirectExchange
     */
    @Bean
    DirectExchange linkPayCancelForgetDirectExchange() {
        return new DirectExchange(linkPayCancelForgetDirect);
    }

    /**
     * GMO決済キャンセル漏れBinding
     */
    @Bean
    Binding linkPayCancelForgetBinding(Queue linkPayCancelForgetQueue,
                                       DirectExchange linkPayCancelForgetDirectExchange) {
        return BindingBuilder.bind(linkPayCancelForgetQueue)
                             .to(linkPayCancelForgetDirectExchange)
                             .with(linkPayCancelForgetRouting);
    }

    /** コンバータ */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}