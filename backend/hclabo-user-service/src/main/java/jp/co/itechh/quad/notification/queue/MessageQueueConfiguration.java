package jp.co.itechh.quad.notification.queue;

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

    /** 通知サブドメインルーティングキー */
    @Value("${queue.password-notification.routing}")
    private String passwordNotificationRouting;

    /** オーソリ期限切れ間近注文通知 */
    @Value("${queue.auth-time-limit.routing}")
    private String authTimeLimitRouting;

    /** 支払督促／支払期限切れ処理結果ルーティングキー */
    @Value("${queue.settlement-administrator.routing}")
    private String settlementAdministratorRouting;

    /** 支払督促／支払期限切れ処理結果エラールーティングキー */
    @Value("${queue.settlement-administrator-error.routing}")
    private String settlementAdministratorErrorRouting;

    /** 受注決済期限切れメールーティングキー */
    @Value("${queue.settlement-expiration-notification.routing}")
    private String settlementExpirationNotificationRouting;

    /** 在庫開放ルーティングキー */
    @Value("${queue.stock-release.routing}")
    private String stockReleaseRouting;

    /** オーソリ期限切れ間近注文異常ルーティングキー */
    @Value("${queue.auth-time-limit-error.routing}")
    private String authTimeLimitErrorRouting;

    /** クリア通知ルーティングキー */
    @Value("${queue.clear-result.routing}")
    private String clearResultRouting;

    /** クリア異常ルーティングキー */
    @Value("${queue.clear-result-error.routing}")
    private String clearResultErrorRouting;

    /** 商品グループ規格画像更新（商品一括アップロード）通知ルーティングキー */
    @Value("${queue.goods-asynchronous.routing}")
    private String goodsAsynchronousRouting;

    /** 商品グループ規格画像更新（商品一括アップロード）異常ルーティングキー */
    @Value("${queue.goods-asynchronous-error.routing}")
    private String goodsAsynchronousErrorRouting;

    /** 商品グループ規格画像更新通知ルーティングキー */
    @Value("${queue.goods-image-update.routing}")
    private String goodsImageUpdateRouting;

    /** クレジットラインレポートルーティングキー */
    @Value("${queue.goods-image-update-error.routing}")
    private String goodsImageUpdateErrorRouting;

    /** 入金結果受付予備処理ルーティングキー */
    @Value("${queue.mulpay-notification-recovery-administrator.routing}")
    private String mulpayNotificationRecoveryAdministratorRouting;

    /** 入金結果受付予備処理結果異常ルーティングキー */
    @Value("${queue.mulpay-notification-recovery-administrator-error.routing}")
    private String mulpayNotificationRecoveryAdministratorErrorRouting;

    /** 検査キット受領登録ルーティングキー */
    @Value("${queue.examkit-received-entry.routing}")
    private String examKitReceivedEntryRouting;

    /** 検査結果登録ルーティングキー */
    @Value("${queue.exam-results-entry.routing}")
    private String examResultsEntryRouting;

    /** 検査結果通知ルーティングキー */
    @Value("${queue.exam-results-notice.routing}")
    private String examResultsNotificationRouting;

    /** マルチペイメントルーティングキー */
    @Value("${queue.zipcode-update.routing}")
    private String zipcodeUpdateRouting;

    /** クレジットラインレポートルーティングキー */
    @Value("${queue.credit-line-report.routing}")
    private String creditLineReportRouting;

    /** クレジットラインレポートエラールーティングキー */
    @Value("${queue.credit-line-report-error.routing}")
    private String creditLineReportErrorRouting;

    /** 受注CSVルーティングキー */
    @Value("${queue.download-csv.routing}")
    private String downloadCsvRouting;

    /** 受注CSVエラールーティングキー */
    @Value("${queue.download-csv-error.routing}")
    private String downloadCsvErrorRouting;

    /** 受注決済督促ルーティングキー */
    @Value("${queue.settlement-reminder.routing}")
    private String settlementReminderRouting;

    /** 出荷登録異常ルーティングキー */
    @Value("${queue.shipment-regist.routing}")
    private String shipmentRegistRouting;

    /** クリア通知ルーティングキー */
    @Value("${queue.stock-report.routing}")
    private String stockReportRouting;

    /** 在庫状況を管理者にメール送信ルーティングキー */
    @Value("${queue.stock-status.routing}")
    private String stockStatusRouting;

    /** メールアドレス変更確認ルーティングキー */
    @Value("${queue.email-modification.routing}")
    private String emailModificationRouting;

    /** 非同期処理(MQ)エラー通知 ルーティングキー */
    @Value("${queue.mq-error-notification.routing}")
    private String mqErrorNotificationRouting;

    /** 会員処理完了メール送信要求ルーティングキー */
    @Value("${queue.memberinfo-process-complete.routing}")
    private String memberinfoProcessCompleteRouting;

    /** 請求不整合報告要求ルーティングキー */
    @Value("${queue.settlement-mismatch.routing}")
    private String settlementMismatchRouting;

    /** MO会員カードアラート要求ルーティングキー */
    @Value("${queue.gmo-member-card-alert.routing}")
    private String gmoMemberCardAlertRouting;

    /** 問い合わせメール送信要求ルーティングキー */
    @Value("${queue.inquiry.routing}")
    private String inquiryRouting;

    /** マルチペイメントアラートルーティングキー */
    @Value("${queue.multi-payment-alert.routing}")
    private String multiPaymentAlertRouting;

    /** 仮会員登録ルーティングキー */
    @Value("${queue.member-preregistration.routing}")
    private String memberPreregistrationRouting;

    /** 注文確認ルーティングキー */
    @Value("${queue.order-confirmation.routing}")
    private String orderConfirmationRouting;

    /** メルマガ登録完了ルーティングキー */
    @Value("${queue.mail-magazine-process-complete.routing}")
    private String mailMagazineProcessCompleteRouting;

    /** 注文データ作成アラートルーティングキー */
    @Value("${queue.order-regist-alert.routing}")
    private String orderRegistAlertRouting;

    /** 出荷完了メール送信ルーティングキー */
    @Value("${queue.shipment-notification.routing}")
    private String shipmentNotificationRouting;

    /** 通知サブドメイン DirectExchange */
    @Value("${queue.notification.direct}")
    private String notificationDirect;

    /** GMO決済キャンセル漏れルーティングキー */
    @Value("${queue.linkpay-cancel-reminder.routing}")
    private String linkpayCancelReminderRouting;

    /** 入金過不足アラートルーティングキー */
    @Value("${queue.payment-excess-alert.routing}")
    private String paymentExcessAlertRouting;

    /** 入金完了メール送信要求ルーティングキー */
    @Value("${queue.payment-deposited.routing}")
    private String paymentDepositedRouting;

    /** 認証コードメール送信要求ルーティングキー */
    @Value("${queue.certification-code.routing}")
    private String certificationCodeRouting;

    /** 入金完了メール送信要求 DirectExchange */
    @Value("${queue.payment-deposited.direct}")
    private String paymentDepositedDirect;

    /** タグクリアバッチルーティングキー */
    @Value("${queue.tag-clear.routing}")
    private String tagClearRouting;

    /** タグクリアバッチ DirectExchange */
    @Value("${queue.tag-clear.direct}")
    private String tagClearDirect;

    /** カテゴリ商品更新バッチルーティングキー */
    @Value("${queue.category-goods-regist-update-error.routing}")
    private String categoryGoodsRegistUpdateRouting;

    /** カテゴリ商品更新バッチ DirectExchange */
    @Value("${queue.category-goods-regist-update-error.direct}")
    private String categoryGoodsRegistUpdateDirect;

    /**
     * 通知サブドメインキュー定義
     */
    @Bean
    Queue passwordNotificationQueue() {
        return QueueBuilder.durable(passwordNotificationRouting).build();
    }

    /**
     * 通知サブドメインキュー定義
     */
    @Bean
    Queue authTimeLimitQueue() {
        return QueueBuilder.durable(authTimeLimitRouting).build();
    }

    /**
     * 支払督促／支払期限切れ処理結果キュー定義
     */
    @Bean
    Queue settlementAdministratorQueue() {
        return QueueBuilder.durable(settlementAdministratorRouting).build();
    }

    /**
     * 支払督促／支払期限切れ処理結果エラーキュー定義
     */
    @Bean
    Queue settlementAdministratorErrorQueue() {
        return QueueBuilder.durable(settlementAdministratorErrorRouting).build();
    }

    /**
     * 受注決済期限切れメーキュー定義
     */
    @Bean
    Queue settlementExpirationNotificationQueue() {
        return QueueBuilder.durable(settlementExpirationNotificationRouting).build();
    }

    /**
     * 在庫開放キュー定義
     */
    @Bean
    Queue stockReleaseQueue() {
        return QueueBuilder.durable(stockReleaseRouting).build();
    }

    /**
     * オーソリ期限切れ間近注文異常キュー定義
     */
    @Bean
    Queue authTimeLimitErrorQueue() {
        return QueueBuilder.durable(authTimeLimitErrorRouting).build();
    }

    /**
     * クリア通知キュー定義
     */
    @Bean
    Queue clearResultQueue() {
        return QueueBuilder.durable(clearResultRouting).build();
    }

    /**
     * クリア異常キュー定義
     */
    @Bean
    Queue clearResultErrorQueue() {
        return QueueBuilder.durable(clearResultErrorRouting).build();
    }

    /**
     * 商品グループ規格画像更新（商品一括アップロード）通知キュー定義
     */
    @Bean
    Queue goodsAsynchronousQueue() {
        return QueueBuilder.durable(goodsAsynchronousRouting).build();
    }

    /**
     * 商品グループ規格画像更新（商品一括アップロード）異常キュー定義
     */
    @Bean
    Queue goodsAsynchronousErrorQueue() {
        return QueueBuilder.durable(goodsAsynchronousErrorRouting).build();
    }

    /**
     * 商品グループ規格画像更新通知キュー定義
     */
    @Bean
    Queue goodsImageUpdateQueue() {
        return QueueBuilder.durable(goodsImageUpdateRouting).build();
    }

    /**
     * クレジットラインレポートキュー定義
     */
    @Bean
    Queue goodsImageUpdateErrorQueue() {
        return QueueBuilder.durable(goodsImageUpdateErrorRouting).build();
    }

    /**
     * 入金結果受付予備処理キュー定義
     */
    @Bean
    Queue mulpayNotificationRecoveryAdministratorQueue() {
        return QueueBuilder.durable(mulpayNotificationRecoveryAdministratorRouting).build();
    }

    /**
     * 入金結果受付予備処理結果異常キュー定義
     */
    @Bean
    Queue mulpayNotificationRecoveryAdministratorErrorQueue() {
        return QueueBuilder.durable(mulpayNotificationRecoveryAdministratorErrorRouting).build();
    }

    /**
     * 検査キット受領登録キュー定義
     */
    @Bean
    Queue examkitReceivedEntryQueue() {
        return QueueBuilder.durable(examKitReceivedEntryRouting).build();
    }

    /**
     * 検査結果登録キュー定義
     */
    @Bean
    Queue examResultsEntryQueue() {
        return QueueBuilder.durable(examResultsEntryRouting).build();
    }

    /**
     * 検査結果通知キュー定義
     */
    @Bean
    Queue examResultsNotificationQueue() {
        return QueueBuilder.durable(examResultsNotificationRouting).build();
    }

    /**
     * マルチペイメントキュー定義
     */
    @Bean
    Queue zipcodeUpdateQueue() {
        return QueueBuilder.durable(zipcodeUpdateRouting).build();
    }

    /**
     * クレジットラインレポートキュー定義
     */
    @Bean
    Queue creditLineReportQueue() {
        return QueueBuilder.durable(creditLineReportRouting).build();
    }

    /**
     * クレジットラインレポートエラーキュー定義
     */
    @Bean
    Queue creditLineReportErrorQueue() {
        return QueueBuilder.durable(creditLineReportErrorRouting).build();
    }

    /**
     * 受注CSVキュー定義
     */
    @Bean
    Queue downloadCsvQueue() {
        return QueueBuilder.durable(downloadCsvRouting).build();
    }

    /**
     * 受注CSVエラーキュー定義
     */
    @Bean
    Queue downloadCsvErrorQueue() {
        return QueueBuilder.durable(downloadCsvErrorRouting).build();
    }

    /**
     * 受注決済督促キュー定義
     */
    @Bean
    Queue settlementReminderQueue() {
        return QueueBuilder.durable(settlementReminderRouting).build();
    }

    /**
     * 出荷登録異常キュー定義
     */
    @Bean
    Queue shipmentRegistQueue() {
        return QueueBuilder.durable(shipmentRegistRouting).build();
    }

    /**
     * 在庫状況を管理者にメール送信キュー定義
     */
    @Bean
    Queue stockReportQueue() {
        return QueueBuilder.durable(stockReportRouting).build();
    }

    /**
     * 商品グループ在庫状態更新異常キュー定義
     */
    @Bean
    Queue stockStatusQueue() {
        return QueueBuilder.durable(stockStatusRouting).build();
    }

    /**
     * 非同期処理(MQ)エラー通知 キュー定義
     */
    @Bean
    Queue mqErrorNotificationQueue() {
        return QueueBuilder.durable(mqErrorNotificationRouting).build();
    }

    /**
     * メールアドレス変更確認キュー定義
     */
    @Bean
    Queue emailModificationQueue() {
        return QueueBuilder.durable(emailModificationRouting).build();
    }

    /**
     * 会員処理完了メール送信要求キュー定義
     */
    @Bean
    Queue memberinfoProcessCompleteQueue() {
        return QueueBuilder.durable(memberinfoProcessCompleteRouting).build();
    }

    /**
     * 請求不整合報告要求キュー定義
     */
    @Bean
    Queue settlementMismatchQueue() {
        return QueueBuilder.durable(settlementMismatchRouting).build();
    }

    /**
     * GMO会員カードアラート要求キュー定義
     */
    @Bean
    Queue gmoMemberCardAlertQueue() {
        return QueueBuilder.durable(gmoMemberCardAlertRouting).build();
    }

    /**
     * 問い合わせメール送信要求キュー定義
     */
    @Bean
    Queue inquiryQueue() {
        return QueueBuilder.durable(inquiryRouting).build();
    }

    /**
     * マルチペイメントアラートキュー定義
     */
    @Bean
    Queue multiPaymentAlertQueue() {
        return QueueBuilder.durable(multiPaymentAlertRouting).build();
    }

    /**
     * 仮会員登録キュー定義
     */
    @Bean
    Queue memberPreregistrationQueue() {
        return QueueBuilder.durable(memberPreregistrationRouting).build();
    }

    /**
     * 注文確認キュー定義
     */
    @Bean
    Queue orderConfirmationQueue() {
        return QueueBuilder.durable(orderConfirmationRouting).build();
    }

    /**
     * メルマガ登録完了キュー定義
     */
    @Bean
    Queue mailMagazineProcessCompleteQueue() {
        return QueueBuilder.durable(mailMagazineProcessCompleteRouting).build();
    }

    /**
     * 注文データ作成アラートキュー定義
     */
    @Bean
    Queue orderRegistAlertQueue() {
        return QueueBuilder.durable(orderRegistAlertRouting).build();
    }

    /**
     * 出荷完了メール送信キュー定義
     */
    @Bean
    Queue shipmentNotificationQueue() {
        return QueueBuilder.durable(shipmentNotificationRouting).build();
    }

    @Bean
    Queue linkpayCancelReminderQueue() {
        return QueueBuilder.durable(linkpayCancelReminderRouting).build();
    }

    @Bean
    Queue paymentExcessAlertQueue() {
        return QueueBuilder.durable(paymentExcessAlertRouting).build();
    }

    @Bean
    Queue paymentDepositedQueue() {
        return QueueBuilder.durable(paymentDepositedRouting).build();
    }

    /**
     * 認証コードメールキュー定義
     */
    @Bean
    Queue certificationCodeQueue() {
        return QueueBuilder.durable(certificationCodeRouting).build();
    }

    /** DirectExchange */
    @Bean
    Queue tagClearQueue() {
        return QueueBuilder.durable(tagClearRouting).build();
    }

    /** カテゴリ商品更新キュー定義 */
    @Bean
    Queue categoryGoodsRegistUpdateQueue() {
        return QueueBuilder.durable(categoryGoodsRegistUpdateRouting).build();
    }

    /** DirectExchange */
    @Bean
    DirectExchange notificationDirectExchange() {
        return new DirectExchange(notificationDirect);
    }

    /** DirectExchange */
    @Bean
    DirectExchange tagClearDirectExchange() {
        return new DirectExchange(tagClearDirect);
    }

    /** カテゴリ商品更新 DirectExchange */
    @Bean
    DirectExchange categoryGoodsRegistUpdateDirectExchange() {
        return new DirectExchange(categoryGoodsRegistUpdateDirect);
    }

    /**
     * 通知サブドメイン Binding
     */
    @Bean
    Binding passwordNotificationBinding(Queue passwordNotificationQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(passwordNotificationQueue)
                             .to(notificationDirectExchange)
                             .with(passwordNotificationRouting);
    }

    /**
     * 通知サブドメイン Binding
     */
    @Bean
    Binding authTimeLimitBinding(Queue authTimeLimitQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(authTimeLimitQueue).to(notificationDirectExchange).with(authTimeLimitRouting);
    }

    /**
     * 支払督促／支払期限切れ処理結果 Binding
     */
    @Bean
    Binding settlementAdministratorBinding(Queue settlementAdministratorQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(settlementAdministratorQueue).to(notificationDirectExchange).with(settlementAdministratorRouting);
    }

    /**
     * 支払督促／支払期限切れ処理結果エラー Binding
     */
    @Bean
    Binding settlementAdministratorErrorBinding(Queue settlementAdministratorErrorQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(settlementAdministratorErrorQueue).to(notificationDirectExchange).with(settlementAdministratorErrorRouting);
    }

    /**
     * 受注決済期限切れメール送信 Binding
     */
    @Bean
    Binding settlementExpirationNotificationBinding(Queue settlementExpirationNotificationQueue,
                                                    DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(settlementExpirationNotificationQueue)
                             .to(notificationDirectExchange)
                             .with(settlementExpirationNotificationRouting);
    }

    /**
     * 在庫開放 Binding
     */
    @Bean
    Binding stockReleaseBinding(Queue stockReleaseQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(stockReleaseQueue).to(notificationDirectExchange).with(stockReleaseRouting);
    }

    /**
     * オーソリ期限切れ間近注文異常 Binding
     */
    @Bean
    Binding authTimeLimitErrorBinding(Queue authTimeLimitErrorQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(authTimeLimitErrorQueue)
                             .to(notificationDirectExchange)
                             .with(authTimeLimitErrorRouting);
    }

    /**
     * クリア通知 Binding
     */
    @Bean
    Binding clearResultBinding(Queue clearResultQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(clearResultQueue).to(notificationDirectExchange).with(clearResultRouting);
    }

    /**
     * クリア異常 Binding
     */
    @Bean
    Binding clearResultErrorBinding(Queue clearResultErrorQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(clearResultErrorQueue).to(notificationDirectExchange).with(clearResultErrorRouting);
    }

    /**
     * 商品グループ規格画像更新（商品一括アップロード）通知 Binding
     */
    @Bean
    Binding goodsAsynchronousBinding(Queue goodsAsynchronousQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(goodsAsynchronousQueue)
                             .to(notificationDirectExchange)
                             .with(goodsAsynchronousRouting);
    }

    /**
     * 商品グループ規格画像更新（商品一括アップロード）異常 Binding
     */
    @Bean
    Binding goodsAsynchronousErrorBinding(Queue goodsAsynchronousErrorQueue,
                                          DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(goodsAsynchronousErrorQueue)
                             .to(notificationDirectExchange)
                             .with(goodsAsynchronousErrorRouting);
    }

    /**
     * 商品グループ規格画像更新通知 Binding
     */
    @Bean
    Binding goodsImageUpdateBinding(Queue goodsImageUpdateQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(goodsImageUpdateQueue).to(notificationDirectExchange).with(goodsImageUpdateRouting);
    }

    /**
     * クレジットラインレポート Binding
     */
    @Bean
    Binding goodsImageUpdateErrorBinding(Queue goodsImageUpdateErrorQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(goodsImageUpdateErrorQueue)
                             .to(notificationDirectExchange)
                             .with(goodsImageUpdateErrorRouting);
    }

    /**
     * 入金結果受付予備処理 Binding
     */
    @Bean
    Binding mulpayNotificationRecoveryAdministratorBinding(Queue mulpayNotificationRecoveryAdministratorQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(mulpayNotificationRecoveryAdministratorQueue).to(notificationDirectExchange).with(mulpayNotificationRecoveryAdministratorRouting);
    }

    /**
     * 入金結果受付予備処理結果異常 Binding
     */
    @Bean
    Binding mulpayNotificationRecoveryAdministratorErrorBinding(Queue mulpayNotificationRecoveryAdministratorErrorQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(mulpayNotificationRecoveryAdministratorErrorQueue)
                             .to(notificationDirectExchange)
                             .with(mulpayNotificationRecoveryAdministratorErrorRouting);
    }

    /**
     * 検査キット受領登録 Binding
     */
    @Bean
    Binding examkitReceivedEntryBinding(Queue examkitReceivedEntryQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(examkitReceivedEntryQueue)
                             .to(notificationDirectExchange)
                             .with(examKitReceivedEntryRouting);
    }

    /**
     * 検査結果登録 Binding
     */
    @Bean
    Binding examResultsEntryBinding(Queue examResultsEntryQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(examResultsEntryQueue)
                             .to(notificationDirectExchange)
                             .with(examResultsEntryRouting);
    }

    /**
     * 検査結果通知 Binding
     */
    @Bean
    Binding examResultsNotificationBinding(Queue examResultsNotificationQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(examResultsNotificationQueue)
                             .to(notificationDirectExchange)
                             .with(examResultsNotificationRouting);
    }

    /**
     * マルチペイメント Binding
     */
    @Bean
    Binding zipcodeUpdateBinding(Queue zipcodeUpdateQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(zipcodeUpdateQueue).to(notificationDirectExchange).with(zipcodeUpdateRouting);
    }

    /**
     * クレジットラインレポート Binding
     */
    @Bean
    Binding creditLineReportBinding(Queue creditLineReportQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(creditLineReportQueue).to(notificationDirectExchange).with(creditLineReportRouting);
    }

    /**
     * クレジットラインレポートエラー Binding
     */
    @Bean
    Binding creditLineReportErrorBinding(Queue creditLineReportErrorQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(creditLineReportErrorQueue)
                             .to(notificationDirectExchange)
                             .with(creditLineReportErrorRouting);
    }

    /**
     * 受注CSV Binding
     */
    @Bean
    Binding downloadCsvBinding(Queue downloadCsvQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(downloadCsvQueue).to(notificationDirectExchange).with(downloadCsvRouting);
    }

    /**
     * 受注CSVエラー Binding
     */
    @Bean
    Binding downloadCsvErrorBinding(Queue downloadCsvErrorQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(downloadCsvErrorQueue).to(notificationDirectExchange).with(downloadCsvErrorRouting);
    }

    /**
     * 受注決済督促 Binding
     */
    @Bean
    Binding settlementReminderBinding(Queue settlementReminderQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(settlementReminderQueue)
                             .to(notificationDirectExchange)
                             .with(settlementReminderRouting);
    }

    /**
     * 出荷登録異常 Binding
     */
    @Bean
    Binding shipmentRegistBinding(Queue shipmentRegistQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(shipmentRegistQueue).to(notificationDirectExchange).with(shipmentRegistRouting);
    }

    /**
     * 在庫状況を管理者にメール送信 Binding
     */
    @Bean
    Binding stockReportBinding(Queue stockReportQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(stockReportQueue).to(notificationDirectExchange).with(stockReportRouting);
    }

    /**
     * メールアドレス変更確認 Binding
     */
    @Bean
    Binding stockStatusBinding(Queue stockStatusQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(stockStatusQueue).to(notificationDirectExchange).with(stockStatusRouting);
    }

    /**
     * 非同期処理(MQ)エラー通知 Binding
     */
    @Bean
    Binding mqErrorNotificationBinding(Queue mqErrorNotificationQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(mqErrorNotificationQueue)
                             .to(notificationDirectExchange)
                             .with(mqErrorNotificationRouting);
    }

    /**
     * 商品グループ在庫状態更新異常 Binding
     */
    @Bean
    Binding emailModificationBinding(Queue emailModificationQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(emailModificationQueue)
                             .to(notificationDirectExchange)
                             .with(emailModificationRouting);
    }

    /**
     * 会員処理完了メール送信要求 Binding
     */
    @Bean
    Binding memberinfoProcessCompleteBinding(Queue memberinfoProcessCompleteQueue,
                                             DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(memberinfoProcessCompleteQueue)
                             .to(notificationDirectExchange)
                             .with(memberinfoProcessCompleteRouting);
    }

    /**
     * 請求不整合報告要求 Binding
     */
    @Bean
    Binding settlementMismatchBinding(Queue settlementMismatchQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(settlementMismatchQueue)
                             .to(notificationDirectExchange)
                             .with(settlementMismatchRouting);
    }

    /**
     * GMO会員カードアラート要求 Binding
     */
    @Bean
    Binding gmoMemberCardAlertBinding(Queue gmoMemberCardAlertQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(gmoMemberCardAlertQueue)
                             .to(notificationDirectExchange)
                             .with(gmoMemberCardAlertRouting);
    }

    /**
     * 問い合わせメール送信要求 Binding
     */
    @Bean
    Binding inquiryBinding(Queue inquiryQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(inquiryQueue).to(notificationDirectExchange).with(inquiryRouting);
    }

    /**
     * マルチペイメントアラート Binding
     */
    @Bean
    Binding multiPaymentAlertBinding(Queue multiPaymentAlertQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(multiPaymentAlertQueue)
                             .to(notificationDirectExchange)
                             .with(multiPaymentAlertRouting);
    }

    /**
     * 仮会員登録 Binding
     */
    @Bean
    Binding memberPreregistrationBinding(Queue memberPreregistrationQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(memberPreregistrationQueue)
                             .to(notificationDirectExchange)
                             .with(memberPreregistrationRouting);
    }

    /**
     * 注文確認 Binding
     */
    @Bean
    Binding orderConfirmationBinding(Queue orderConfirmationQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(orderConfirmationQueue)
                             .to(notificationDirectExchange)
                             .with(orderConfirmationRouting);
    }

    /**
     * メルマガ登録完了 Binding
     */
    @Bean
    Binding mailMagazineProcessCompleteBinding(Queue mailMagazineProcessCompleteQueue,
                                               DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(mailMagazineProcessCompleteQueue)
                             .to(notificationDirectExchange)
                             .with(mailMagazineProcessCompleteRouting);
    }

    /**
     * 注文データ作成アラート Binding
     */
    @Bean
    Binding orderRegistAlertBinding(Queue orderRegistAlertQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(orderRegistAlertQueue).to(notificationDirectExchange).with(orderRegistAlertRouting);
    }

    /**
     * 出荷完了メール送信 Binding
     */
    @Bean
    Binding shipmentNotificationBinding(Queue shipmentNotificationQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(shipmentNotificationQueue)
                             .to(notificationDirectExchange)
                             .with(shipmentNotificationRouting);
    }

    /**
     * GMO決済キャンセル漏れBinding
     */
    @Bean
    Binding linkpayCancelReminderBinding(Queue linkpayCancelReminderQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(linkpayCancelReminderQueue)
                             .to(notificationDirectExchange)
                             .with(linkpayCancelReminderRouting);
    }

    /**
     * 入金過不足アラートBinding
     */
    @Bean
    Binding paymentExcessAlertBinding(Queue paymentExcessAlertQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(paymentExcessAlertQueue)
                             .to(notificationDirectExchange)
                             .with(paymentExcessAlertRouting);
    }

    /**
     * 認証コードメールBinding
     */
    @Bean
    Binding certificationCodeBinding(Queue certificationCodeQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(certificationCodeQueue)
                             .to(notificationDirectExchange)
                             .with(certificationCodeRouting);
    }

    /**
     * タグクリアバッチBinding
     */
    @Bean
    Binding tagCLearBinding(Queue tagClearQueue, DirectExchange tagClearDirectExchange) {
        return BindingBuilder.bind(tagClearQueue).to(tagClearDirectExchange).with(tagClearRouting);
    }

    /**
     * カテゴリ商品更新バッチBinding
     */
    @Bean
    Binding categoryGoodsRegistUpdateBinding(Queue categoryGoodsRegistUpdateQueue,
                                             DirectExchange categoryGoodsRegistUpdateDirectExchange) {
        return BindingBuilder.bind(categoryGoodsRegistUpdateQueue)
                             .to(categoryGoodsRegistUpdateDirectExchange)
                             .with(categoryGoodsRegistUpdateRouting);
    }

    @Bean
    Binding paymentDepositedBinding(Queue paymentDepositedQueue, DirectExchange notificationDirectExchange) {
        return BindingBuilder.bind(paymentDepositedQueue).to(notificationDirectExchange).with(paymentDepositedRouting);
    }

    /**
     * コンバータ
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}