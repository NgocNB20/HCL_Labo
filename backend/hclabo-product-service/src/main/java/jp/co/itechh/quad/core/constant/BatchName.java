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

    /** マルチペイメント入金処理 */
    public static final String BATCH_MULTIPAYMENT_PAYMENT = "BATCH_MULTIPAYMENT_PAYMENT";

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

    /** 受注CSV非同期バッチ */
    public static final String BATCH_ORDER_CSV_ASYNCHRONOUS = "BATCH_ORDER_CSV_ASYNCHRONOUS";
}