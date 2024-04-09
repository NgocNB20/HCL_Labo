/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.constant;

/**
 * バッチ名
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
public class BatchName {

    /** TOP画面集計 */
    public static final String BATCH_TOP_TOTALING = "BATCH_TOP_TOTALING";

    /** 出荷アップロード */
    public static final String BATCH_SHIPMENT_REGIST = "BATCH_SHIPMENT_REGIST";

    /** 受注督促メール送信 */
    public static final String BATCH_SETTLEMENT_REMINDER = "BATCH_SETTLEMENT_REMINDER";

    /** 受注決済期限切れメール送信 */
    public static final String BATCH_SETTLEMENT_EXPIRED_NOTIFICATION = "BATCH_SETTLEMENT_EXPIRED_NOTIFICATION";

    /** マルチペイメント入金状況問合せ */
    public static final String BATCH_MULTIPAYMENT_CONFIRMATION = "BATCH_MULTIPAYMENT_CONFIRMATION";

    /** 入金結果受付予備処理メール */
    public static final String BATCH_MULPAY_NOTIFICATION_RECOVERY_MAIL = "BATCH_MULPAY_NOTIFICATION_RECOVERY_MAIL";

    /** メール送信バッチ全般 */
    public static final String BATCH_MAIL = "BATCH_MAIL";

    /** 郵便番号自動更新 */
    public static final String BATCH_ZIP_CODE = "BATCH_ZIP_CODE";

    /** 事業所郵便番号自動更新 */
    public static final String BATCH_OFFICE_ZIP_CODE = "BATCH_OFFICE_ZIP_CODE";

    /** 商品画像更新 */
    public static final String BATCH_GOODSIMAGE_UPDATE = "BATCH_GOODSIMAGE_UPDATE";

    /** 商品登録非同期バッチ */
    public static final String BATCH_GOODS_ASYNCHRONOUS = "BATCH_GOODS_ASYNCHRONOUS";

    /** 商品グループ在庫状態更新 */
    public static final String BATCH_STOCKSTATUSDISPLAY_UPDATE = "BATCH_STOCKSTATUSDISPLAY_UPDATE";

    /** サイトマップXML出力バッチ */
    public static final String BATCH_SITEMAPXML_OUTPUT = "BATCH_SITEMAPXML_OUTPUT";

    /** オーソリ期限切れ間近注文警告 */
    public static final String BATCH_AUTHTIMELIMITORDER_NOTIFICATION = "BATCH_AUTHTIMELIMITORDER_NOTIFICATION";

    /** 在庫開放バッチ */
    public static final String BATCH_ORDER_RESERVE_STOCK_RELEASE = "BATCH_ORDER_RESERVE_STOCK_RELEASE";

    /** 与信枠解放バッチ */
    public static final String BATCH_ORDER_CREDITLINE_REPORT = "BATCH_ORDER_CREDITLINE_REPORT";

    /** 在庫状況メール送信バッチ */
    public static final String BATCH_STOCK_REPORT = "BATCH_STOCK_REPORT";

    /** クリアバッチ */
    public static final String BATCH_CLEAR = "BATCH_CLEAR";

    /** タスククリーンバッチ */
    public static final String BATCH_TASK_CLEAN = "BATCH_TASK_CLEAN";

    /** 受注CSVバッチ */
    public static final String BATCH_ORDER_CSV = "BATCH_ORDER_CSV";

    /** 出荷CSVバッチ */
    public static final String BATCH_SHIPMENT_CSV = "BATCH_SHIPMENT_CSV";

    /** GMO決済キャンセル漏れ */
    public static final String GMO_PAYMENT_CANCEL_FORGOT = "GMO_PAYMENT_CANCEL_FORGOT";

    /** タグクリアバッチ */
    public static final String BATCH_TAG_CLEAR = "BATCH_TAG_CLEAR";

    /** カテゴリ商品更新バッチ */
    public static final String BATCH_CATEGORY_GOODS_REGISTER_UPDATE = "BATCH_CATEGORY_GOODS_REGISTER_UPDATE";

    /** 入金過不足バッチ */
    public static final String PAYMENT_EXCESS_ALERT = "PAYMENT_EXCESS_ALERT";

    /** 商品販売個数集計CSVバッチ */
    public static final String BATCH_SOLD_GOOD_CSV = "BATCH_SOLD_GOOD_CSV";

    /** 受注・売上集計CSVバッチ */
    public static final String BATCH_ORDER_SALES_CSV = "BATCH_ORDER_SALES_CSV";

    /** 非同期処理(MQ)エラー通知 */
    public static final String BATCH_MQ_ERROR_NOTIFICATION = "BATCH_MQ_ERROR_NOTIFICATION";

    /** 検査キット受領登録 */
    public static final String EXAMKIT_RECEIVED_ENTRY = "EXAMKIT_RECEIVED_ENTRY";

    /** 検査結果登録 */
    public static final String EXAM_RESULTS_ENTRY = "EXAM_RESULTS_ENTRY";
}
