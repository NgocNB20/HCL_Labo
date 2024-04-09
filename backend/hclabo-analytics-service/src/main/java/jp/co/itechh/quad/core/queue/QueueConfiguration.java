package jp.co.itechh.quad.core.queue;

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
public class QueueConfiguration {

    /**
     * 受注情報を登録・更新するルーティングキー
     */
    @Value("${queue.ordersearchregistupdate.routing}")
    private String orderSearchRegistUpdateRouting;

    /**
     * 受注情報を登録・更新するDirectExchange
     */
    @Value("${queue.ordersearchregistupdate.direct}")
    private String orderSearchRegistUpdateDirect;

    @Value("${queue.ordersalesdownloadcsv.routing}")
    private String orderSalesDownloadCsvRouting;

    /**
     * 受注情報を登録・更新するDirectExchange
     */
    @Value("${queue.ordersalesdownloadcsv.direct}")
    private String orderSalesDownloadCsvDirect;

    /**
     * 受注検索 受注CSVダウンロードルーティングキー
     */
    @Value("${queue.ordersearchdownloadordercsv.routing}")
    private String orderSearchDownloadOrderCsvRouting;

    /**
     * 受注検索 受注CSVダウンロードDirectExchange
     */
    @Value("${queue.ordersearchdownloadordercsv.direct}")
    private String orderSearchDownloadOrderCsvDirect;

    /**
     * 受注検索 出荷CSVダウンロードルーティングキー
     */
    @Value("${queue.ordersearchdownloadshipmentcsv.routing}")
    private String orderSearchDownloadShipmentCsvRouting;

    /**
     * 受注検索 出荷CSVダウンロードDirectExchange
     */
    @Value("${queue.ordersearchdownloadshipmentcsv.direct}")
    private String orderSearchDownloadShipmentCsvDirect;

    /**
     * 集計用販売デーダドルーティングキー
     */
    @Value("${queue.reportsregist.routing}")
    private String reportsRegistRouting;

    /**
     * 集計用販売デーダDirectExchange
     */
    @Value("${queue.reportsregist.direct}")
    private String reportsRegistDirect;

    /**
     * 受注検索 出荷CSVダウンロードルーティングキー
     */
    @Value("${queue.goodssaledownloadordercsv.routing}")
    private String goodsSalesDownloadCsvRouting;

    /**
     * 受注検索 出荷CSVダウンロードDirectExchange
     */
    @Value("${queue.goodssaledownloadordercsv.direct}")
    private String goodsSalesDownloadCsvDirect;

    /**
     * 受注情報を登録・更新するキュー定義
     */
    @Bean
    Queue orderSearchRegistUpdateQueue() {
        return QueueBuilder.durable(this.orderSearchRegistUpdateRouting).build();
    }

    /**
     * 受注情報を登録・更新するキュー定義
     */
    @Bean
    Queue orderSalesDownloadCsvQueue() {
        return QueueBuilder.durable(this.orderSalesDownloadCsvRouting).build();
    }

    /**
     * 受注検索 受注CSVダウンロードキュー定義
     */
    @Bean
    Queue orderSearchDownloadOrderCsvQueue() {
        return QueueBuilder.durable(this.orderSearchDownloadOrderCsvRouting).build();
    }

    /**
     * 受注検索 出荷CSVダウンロードキュー定義
     */
    @Bean
    Queue orderSearchDownloadShipmentCsvQueue() {
        return QueueBuilder.durable(this.orderSearchDownloadShipmentCsvRouting).build();
    }

    /**
     * 集計用販売デーダるキュー定義
     */
    @Bean
    Queue reportsRegistQueue() {
        return QueueBuilder.durable(this.reportsRegistRouting).build();
    }

    /**
     * 受注検索 出荷CSVダウンロードキュー定義
     */
    @Bean
    Queue goodsSalesDownloadCsvQueue() {
        return QueueBuilder.durable(this.goodsSalesDownloadCsvRouting).build();
    }

    /**
     * 受注情報を登録・更新するDirectExchange
     */
    @Bean
    DirectExchange orderSearchRegistUpdateDirectExchange() {
        return new DirectExchange(this.orderSearchRegistUpdateDirect);
    }

    /**
     * 受注情報を登録・更新するDirectExchange
     */
    @Bean
    DirectExchange orderSalesDownloadCsvDirectExchange() {
        return new DirectExchange(this.orderSalesDownloadCsvDirect);
    }

    /**
     * 受注検索 受注CSVダウンロードDirectExchange
     */
    @Bean
    DirectExchange orderSearchDownloadOrderCsvDirectExchange() {
        return new DirectExchange(this.orderSearchDownloadOrderCsvDirect);
    }

    /**
     * 受注検索 出荷CSVダウンロードDirectExchange
     */
    @Bean
    DirectExchange orderSearchDownloadShipmentCsvDirectExchange() {
        return new DirectExchange(this.orderSearchDownloadShipmentCsvDirect);
    }

    /**
     * 集計用販売デーダDirectExchange
     */
    @Bean
    DirectExchange reportsRegistDirectExchange() {
        return new DirectExchange(this.reportsRegistDirect);
    }

    /**
     * 受注検索 出荷CSVダウンロードDirectExchange
     */
    @Bean
    DirectExchange goodsSalesDownloadCsvDirectExchange() {
        return new DirectExchange(this.goodsSalesDownloadCsvDirect);
    }

    /**
     * 受注情報を登録・更新するBinding
     */
    @Bean
    Binding orderSearchRegistUpdateBinding(Queue orderSearchRegistUpdateQueue,
                                           DirectExchange orderSearchRegistUpdateDirectExchange) {
        return BindingBuilder.bind(orderSearchRegistUpdateQueue)
                             .to(orderSearchRegistUpdateDirectExchange)
                             .with(this.orderSearchRegistUpdateRouting);
    }

    /**
     * 受注情報を登録・更新するBinding
     */
    @Bean
    Binding orderSalesDownloadCsvBinding(Queue orderSalesDownloadCsvQueue,
                                         DirectExchange orderSalesDownloadCsvDirectExchange) {
        return BindingBuilder.bind(orderSalesDownloadCsvQueue)
                             .to(orderSalesDownloadCsvDirectExchange)
                             .with(this.orderSalesDownloadCsvRouting);
    }

    /**
     * 受注検索 受注CSVダウンロードBinding
     */
    @Bean
    Binding orderSearchDownloadOrderCsvBinding(Queue orderSearchDownloadOrderCsvQueue,
                                               DirectExchange orderSearchDownloadOrderCsvDirectExchange) {
        return BindingBuilder.bind(orderSearchDownloadOrderCsvQueue)
                             .to(orderSearchDownloadOrderCsvDirectExchange)
                             .with(this.orderSearchDownloadOrderCsvRouting);
    }

    /**
     * 受注検索 出荷CSVダウンロードBinding
     */
    @Bean
    Binding orderSearchDownloadShipmentCsvBinding(Queue orderSearchDownloadShipmentCsvQueue,
                                                  DirectExchange orderSearchDownloadShipmentCsvDirectExchange) {
        return BindingBuilder.bind(orderSearchDownloadShipmentCsvQueue)
                             .to(orderSearchDownloadShipmentCsvDirectExchange)
                             .with(this.orderSearchDownloadShipmentCsvRouting);
    }

    /**
     * 集計用販売デーダDirectExchange
     */
    @Bean
    Binding reportsRegistBinding(Queue reportsRegistQueue, DirectExchange reportsRegistDirectExchange) {
        return BindingBuilder.bind(reportsRegistQueue).to(reportsRegistDirectExchange).with(this.reportsRegistRouting);
    }

    /**
     * 受注検索 出荷CSVダウンロードBinding
     */
    @Bean
    Binding goodsSalesDownloadCsvBinding(Queue goodsSalesDownloadCsvQueue,
                                         DirectExchange goodsSalesDownloadCsvDirectExchange) {
        return BindingBuilder.bind(goodsSalesDownloadCsvQueue)
                             .to(goodsSalesDownloadCsvDirectExchange)
                             .with(this.goodsSalesDownloadCsvRouting);
    }

    /**
     * コンバータ
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}