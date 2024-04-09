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

    /** ノベルティバッチルーティングキー */
    @Value("${queue.novelty.routing}")
    private String noveltyRouting;

    /** ノベルティバッチ DirectExchange */
    @Value("${queue.novelty.direct}")
    private String noveltyDirect;

    /** 出荷登録バッチ ルーティングキー */
    @Value("${queue.shipmentregist.routing}")
    private String shipmentRegistRouting;

    /** 出荷登録バッチ DirectExchange */
    @Value("${queue.shipmentregist.direct}")
    private String shipmentRegistDirect;

    /** 受注を入金済みにするバッチ ルーティングキー */
    @Value("${queue.reflectdeposited.routing}")
    private String reflectDepositedRouting;

    /** 受注を入金済みにするバッチ DirectExchange */
    @Value("${queue.reflectdeposited.direct}")
    private String reflectDepositedDirect;

    /** 決済代行入金結果受取予備処理バッチ ルーティングキー*/
    @Value("${queue.mulpaynotificationrecovery.routing}")
    private String mulpayNotificationRecoveryRouting;

    /** 決済代行入金結果受取予備処理バッチ  DirectExchange */
    @Value("${queue.mulpaynotificationrecovery.direct}")
    private String mulpayNotificationRecoveryDirect;

    /** 支払督促バッチ ルーティングキー */
    @Value("${queue.reminderpayment.routing}")
    private String reminderPaymentRouting;

    /** 支払督促バッチ DirectExchange */
    @Value("${queue.reminderpayment.direct}")
    private String reminderPaymentDirect;

    /** 支払期限切れバッチ ルーティングキー */
    @Value("${queue.expiredpayment.routing}")
    private String expiredPaymentRouting;

    /** 支払期限切れバッチ DirectExchange */
    @Value("${queue.expiredpayment.direct}")
    private String expiredPaymentDirect;

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

    /** ノベルティバッチキュー定義 */
    @Bean
    Queue noveltyQueue() {
        return QueueBuilder.durable(noveltyRouting).build();
    }

    /**  ノベルティバッチDirectExchange */
    @Bean
    DirectExchange noveltyDirectExchange() {
        return new DirectExchange(noveltyDirect);
    }

    /** 出荷登録バッチキュー定義 */
    @Bean
    Queue shipmentRegistQueue() {
        return QueueBuilder.durable(shipmentRegistRouting).build();
    }

    /** 出荷登録バッチDirectExchange */
    @Bean
    DirectExchange shipmentRegistDirectExchange() {
        return new DirectExchange(shipmentRegistDirect);
    }

    /** 受注を入金済みにするバッチキュー定義 */
    @Bean
    Queue reflectDepositedQueue() {
        return QueueBuilder.durable(reflectDepositedRouting).build();
    }

    /** 受注を入金済みにするバッチDirectExchange */
    @Bean
    DirectExchange reflectDepositedDirectExchange() {
        return new DirectExchange(reflectDepositedDirect);
    }

    /** 決済代行入金結果受取予備処理バッチキュー定義 */
    @Bean
    Queue mulpayNotificationRecoveryQueue() {
        return QueueBuilder.durable(mulpayNotificationRecoveryRouting).build();
    }

    /** 決済代行入金結果受取予備処理バッチDirectExchange */
    @Bean
    DirectExchange mulpayNotificationRecoveryDirectExchange() {
        return new DirectExchange(mulpayNotificationRecoveryDirect);
    }

    /** 支払督促バッチキュー定義 */
    @Bean
    Queue reminderPaymentQueue() {
        return QueueBuilder.durable(reminderPaymentRouting).build();
    }

    /** 支払督促バッチDirectExchange */
    @Bean
    DirectExchange reminderPaymentDirectExchange() {
        return new DirectExchange(reminderPaymentDirect);
    }

    /** 支払期限切れバッチキュー定義 */
    @Bean
    Queue expiredPaymentQueue() {
        return QueueBuilder.durable(expiredPaymentRouting).build();
    }

    /** 支払期限切れバッチDirectExchange */
    @Bean
    DirectExchange expiredPaymentDirectExchange() {
        return new DirectExchange(expiredPaymentDirect);
    }

    /**
     * クリアバッチBinding
     */
    @Bean
    Binding clearBinding(Queue clearQueue, DirectExchange clearDirectExchange) {
        return BindingBuilder.bind(clearQueue).to(clearDirectExchange).with(clearRouting);
    }

    /** 支払督促バッチBinding */
    @Bean
    Binding reminderPaymentBinding(Queue reminderPaymentQueue, DirectExchange reminderPaymentDirectExchange) {
        return BindingBuilder.bind(reminderPaymentQueue).to(reminderPaymentDirectExchange).with(reminderPaymentRouting);
    }

    /** 支払期限切れバッチBinding */
    @Bean
    Binding expiredPaymentBinding(Queue expiredPaymentQueue, DirectExchange expiredPaymentDirectExchange) {
        return BindingBuilder.bind(expiredPaymentQueue).to(expiredPaymentDirectExchange).with(expiredPaymentRouting);
    }

    /** 受注を入金済みにするバッチBinding */
    @Bean
    Binding reflectDepositedBinding(Queue reflectDepositedQueue, DirectExchange reflectDepositedDirectExchange) {
        return BindingBuilder.bind(reflectDepositedQueue)
                             .to(reflectDepositedDirectExchange)
                             .with(reflectDepositedRouting);
    }

    /**
     * ノベルティバッチBinding
     */
    @Bean
    Binding noveltyBinding(Queue noveltyQueue, DirectExchange noveltyDirectExchange) {
        return BindingBuilder.bind(noveltyQueue).to(noveltyDirectExchange).with(noveltyRouting);
    }

    /**
     * 出荷登録バッチBinding
     */
    @Bean
    Binding shipmentRegistBinding(Queue shipmentRegistQueue, DirectExchange shipmentRegistDirectExchange) {
        return BindingBuilder.bind(shipmentRegistQueue).to(shipmentRegistDirectExchange).with(shipmentRegistRouting);
    }

    /** コンバータ */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}