package jp.co.itechh.quad.admin.constant;

/**
 * バッチ名
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
public class BatchName {

    /** 出荷アップロード */
    public static final String BATCH_SHIPMENT_REGIST = "BATCH_SHIPMENT_REGIST";

    /** 人気商品ランキング集計バッチ */
    public static final String BATCH_PRODUCT_POPULARITY_TOTALING = "BATCH_PRODUCT_POPULARITY_TOTALING";

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

    /** オーソリ期限切れ間近注文警告 */
    public static final String BATCH_AUTHTIMELIMITORDER_NOTIFICATION = "BATCH_AUTHTIMELIMITORDER_NOTIFICATION";

    /** 在庫開放バッチ */
    public static final String BATCH_ORDER_RESERVE_STOCK_RELEASE = "BATCH_ORDER_RESERVE_STOCK_RELEASE";

    /** 与信枠解放バッチ */
    public static final String BATCH_ORDER_CREDITLINE_REPORT = "BATCH_ORDER_CREDITLINE_REPORT";

    /** 在庫状況メール送信バッチ */
    public static final String BATCH_STOCK_REPORT = "BATCH_STOCK_REPORT";

    /** 物流サービスクリアバッチ */
    public static final String LOGISTIC_BATCH_CLEAR = "LOGISTIC_BATCH_CLEAR";

    /** 受注サービスクリアバッチ */
    public static final String ORDER_BATCH_CLEAR = "ORDER_BATCH_CLEAR";

    /** 決済サービスクリアバッチ */
    public static final String PAYMENT_BATCH_CLEAR = "PAYMENT_BATCH_CLEAR";

    /** 販売企画サービスクリアバッチ */
    public static final String PRICE_PLANNING_BATCH_CLEAR = "PRICE_PLANNING_BATCH_CLEAR";

    /** プロモーションサービスクリアバッチ */
    public static final String PROMOTION_BATCH_CLEAR = "PROMOTION_BATCH_CLEAR";

    /** 商品タグクリアバッチ */
    public static final String TAG_BATCH_CLEAR = "TAG_BATCH_CLEAR";

    /** 受注CSVダウンロードバッチ */
    public static final String BATCH_ORDER_CSV_ASYNCHRONOUS = "BATCH_ORDER_CSV_ASYNCHRONOUS";

    /** 決済代行入金結果受取予備処理バッチ */
    public static final String BATCH_MULPAY_NOTIFICATION_RECOVERY = "BATCH_MULPAY_NOTIFICATION_RECOVERY";

    /** 支払督促バッチ */
    public static final String BATCH_REMINDER_PAYMENT = "BATCH_REMINDER_PAYMENT";

    /** 支払期限切れバッチ */
    public static final String BATCH_EXPIRED_PAYMENT = "BATCH_EXPIRED_PAYMENT";

    /** GMO決済キャンセル漏れ検知バッチ */
    public static final String BATCH_LINK_PAY_CANCEL_REMINDER = "BATCH_LINK_PAY_CANCEL_REMINDER";

    /** 商品販売個数集計CSVダウンロードバッチ */
    public static final String BATCH_GOODS_SALES_CSV_ASYNCHRONOUS = "BATCH_GOODS_SALES_CSV_ASYNCHRONOUS";

    /** 受注・売上集計CSVダウンロードバッチ */
    public static final String BATCH_ORDER_SALES_CSV_ASYNCHRONOUS = "BATCH_ORDER_SALES_CSV_ASYNCHRONOUS";

    /** カテゴリ商品更新バッチ */
    public static final String CATEGORY_GOODS_REGISTER_UPDATE_BATCH = "CATEGORY_GOODS_REGISTER_UPDATE_BATCH";

    /** 出荷CSVダウンロードバッチ */
    public static final String BATCH_SHIPMENT_CSV_ASYNCHRONOUS = "BATCH_SHIPMENT_CSV_ASYNCHRONOUS";

    /** 検査キット受領登録バッチ */
    public static final String BATCH_EXAMKIT_RECEIVED_ENTRY = "BATCH_EXAMKIT_RECEIVED_ENTRY";

    /** 検査結果登録バッチ */
    public static final String BATCH_EXAM_RESULTS_ENTRY = "BATCH_EXAM_RESULTS_ENTRY";
}
