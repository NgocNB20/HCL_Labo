package jp.co.itechh.quad.batch.queue;

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
 * @author Doan Thang (VJP)
 */
@Configuration
public class MessageQueueConfiguration {

    /** 商品画像更新ルーティングキー */
    @Value("${queue.imageupdate.routing}")
    private String imageUpdateRouting;

    /** 商品画像更新 DirectExchange */
    @Value("${queue.imageupdate.direct}")
    private String imageUpdateDirect;

    /** 商品在庫表示テーブルアップサート非同期ルーティングキー */
    @Value("${queue.syncUpsertGoodsStockDisplay.routing}")
    private String syncUpsertGoodsStockDisplayRouting;

    /** 商品在庫表示テーブルアップサート非同期 DirectExchange */
    @Value("${queue.syncUpsertGoodsStockDisplay.direct}")
    private String syncUpsertGoodsStockDisplayDirect;

    /** 商品グループ在庫状態更新ルーティングキー */
    @Value("${queue.stockstatusdisplayupdate.routing}")
    private String stockStatusDisplayUpdateRouting;

    /** 商品グループ在庫状態更新 DirectExchange */
    @Value("${queue.stockstatusdisplayupdate.direct}")
    private String stockStatusDisplayUpdateDirect;

    /** 商品登録非同期ルーティングキー */
    @Value("${queue.registasynchronous.routing}")
    private String registAsynchronousRouting;

    /** 商品登録非同期 DirectExchange */
    @Value("${queue.registasynchronous.direct}")
    private String registAsynchronousDirect;

    /** 在庫状況確認メール送信ルーティングキー */
    @Value("${queue.stockreport.routing}")
    private String stockReportRouting;

    /** 在庫状況確認メール送信 DirectExchange */
    @Value("${queue.stockreport.direct}")
    private String stockReportDirect;

    /** 人気商品ランキング集計バッチ ルーティングキー */
    @Value("${queue.productPopularityTotalingBatch.routing}")
    private String productPopularityTotalingBatchRouting;

    /** 人気商品ランキング集計バッチ DirectExchange */
    @Value("${queue.productPopularityTotalingBatch.direct}")
    private String productPopularityTotalingBatchDirect;

    /** タグクリアバッチルーティングキー */
    @Value("${queue.tagClear.routing}")
    private String tagClearRouting;

    /** タグクリアバッチ DirectExchange */
    @Value("${queue.tagClear.direct}")
    private String tagClearDirect;

    /** カテゴリ商品更新バッチ ルーティングキー */
    @Value("${queue.syncUpdateCategoryProductBatch.routing}")
    private String syncUpdateCategoryProductBatchRouting;

    /** カテゴリ商品更新バッチ DirectExchange */
    @Value("${queue.syncUpdateCategoryProductBatch.direct}")
    private String syncUpdateCategoryProductBatchDirect;

    /**
     * 商品画像更新キュー定義
     */
    @Bean
    Queue imageUpdateQueue() {
        return QueueBuilder.durable(imageUpdateRouting).build();
    }

    /**
     * 商品グループ在庫状態更新キュー定義
     */
    @Bean
    Queue stockStatusDisplayUpdateQueue() {
        return QueueBuilder.durable(stockStatusDisplayUpdateRouting).build();
    }

    /**
     * 商品登録非同期キュー定義
     */
    @Bean
    Queue registAsynchronousQueue() {
        return QueueBuilder.durable(registAsynchronousRouting).build();
    }

    /**
     * 在庫状況確認メール送信キュー定義
     */
    @Bean
    Queue stockReportQueue() {
        return QueueBuilder.durable(stockReportRouting).build();
    }

    /**
     * 商品在庫表示テーブルアップサート非同期ルーティングキー定義
     */
    @Bean
    Queue syncUpsertGoodsStockDisplayQueue() {
        return QueueBuilder.durable(syncUpsertGoodsStockDisplayRouting).build();
    }

    /**
     * 人気商品ランキング集計バッチ キュー定義
     */
    @Bean
    Queue productPopularityTotalingBatchQueue() {
        return QueueBuilder.durable(productPopularityTotalingBatchRouting).build();
    }

    /**
     * タグクリアバッチ キュー定義
     */
    @Bean
    Queue tagClearQueue() {
        return QueueBuilder.durable(tagClearRouting).build();
    }

    /** カテゴリ商品更新バッチ キュー定義 */
    @Bean
    Queue syncUpdateCategoryProductBatchQueue() {
        return QueueBuilder.durable(syncUpdateCategoryProductBatchRouting).build();
    }

    /**
     * 商品画像更新 DirectExchange
     */
    @Bean
    DirectExchange imageUpdateDirectExchange() {
        return new DirectExchange(imageUpdateDirect);
    }

    /**
     * 商品在庫表示テーブルアップサート非同期DirectExchange
     */
    @Bean
    DirectExchange syncUpsertGoodsStockDisplayDirectExchange() {
        return new DirectExchange(syncUpsertGoodsStockDisplayDirect);
    }

    /**
     * 商品グループ在庫状態更新DirectExchange
     */
    @Bean
    DirectExchange stockStatusDisplayUpdateDirectExchange() {
        return new DirectExchange(stockStatusDisplayUpdateDirect);
    }

    /**
     * 商品登録非同期DirectExchange
     */
    @Bean
    DirectExchange registAsynchronousDirectExchange() {
        return new DirectExchange(registAsynchronousDirect);
    }

    /**
     * 在庫状況確認メール送信DirectExchange
     */
    @Bean
    DirectExchange stockReportDirectExchange() {
        return new DirectExchange(stockReportDirect);
    }

    /** 人気商品ランキング集計バッチ DirectExchange */
    @Bean
    DirectExchange productPopularityTotalingBatchDirectExchange() {
        return new DirectExchange(productPopularityTotalingBatchDirect);
    }

    /** タグクリアバッチ DirectExchange */
    @Bean
    DirectExchange tagClearDirectExchange() {
        return new DirectExchange(tagClearDirect);
    }

    /** カテゴリ商品更新バッチ DirectExchange */
    @Bean
    DirectExchange syncUpdateCategoryProductBatchDirectExchange() {
        return new DirectExchange(syncUpdateCategoryProductBatchDirect);
    }

    /**
     * 商品画像更新 Binding
     */
    @Bean
    Binding imageUpdateBinding(Queue stockStatusDisplayUpdateQueue, DirectExchange imageUpdateDirectExchange) {
        return BindingBuilder.bind(stockStatusDisplayUpdateQueue)
                             .to(imageUpdateDirectExchange)
                             .with(imageUpdateRouting);
    }

    /**
     * 商品グループ在庫状態更新 Binding
     */
    @Bean
    Binding stockDisplayBinding(Queue stockStatusDisplayUpdateQueue,
                                DirectExchange stockStatusDisplayUpdateDirectExchange) {
        return BindingBuilder.bind(stockStatusDisplayUpdateQueue)
                             .to(stockStatusDisplayUpdateDirectExchange)
                             .with(stockStatusDisplayUpdateRouting);
    }

    /**
     * 商品登録非同期 Binding
     */
    @Bean
    Binding registAsynchronousBinding(Queue registAsynchronousQueue, DirectExchange registAsynchronousDirectExchange) {
        return BindingBuilder.bind(registAsynchronousQueue)
                             .to(registAsynchronousDirectExchange)
                             .with(registAsynchronousRouting);
    }

    /**
     * 在庫状況確認メール送信 Binding
     */
    @Bean
    Binding stockReportBinding(Queue stockReportQueue, DirectExchange stockReportDirectExchange) {
        return BindingBuilder.bind(stockReportQueue).to(stockReportDirectExchange).with(stockReportRouting);
    }

    /**
     * 人気商品ランキング集計バッチ Binding
     */
    @Bean
    Binding productPopularityTotalingBatchBinding(Queue productPopularityTotalingBatchQueue,
                                                  DirectExchange productPopularityTotalingBatchDirectExchange) {
        return BindingBuilder.bind(productPopularityTotalingBatchQueue)
                             .to(productPopularityTotalingBatchDirectExchange)
                             .with(productPopularityTotalingBatchRouting);
    }

    /**
     * タグクリアバッチ Binding
     */
    @Bean
    Binding tagClearBinding(Queue tagClearQueue, DirectExchange tagClearDirectExchange) {
        return BindingBuilder.bind(tagClearQueue).to(tagClearDirectExchange).with(tagClearRouting);
    }

    /**
     * カテゴリ商品更新バッチ Binding
     */
    @Bean
    Binding syncUpdateCategoryProductBatchBinding(Queue syncUpdateCategoryProductBatchQueue,
                                                  DirectExchange syncUpdateCategoryProductBatchDirectExchange) {
        return BindingBuilder.bind(syncUpdateCategoryProductBatchQueue)
                             .to(syncUpdateCategoryProductBatchDirectExchange)
                             .with(syncUpdateCategoryProductBatchRouting);
    }

    /**
     * コンバータ
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}