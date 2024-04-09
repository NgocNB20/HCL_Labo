/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.constant.BatchName;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * バッチ処理名：列挙型
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeBatchName implements EnumType {

    /** 人気商品ランキング集計 */
    BATCH_PRODUCT_POPULARITY_TOTALING("人気商品ランキング集計", BatchName.BATCH_PRODUCT_POPULARITY_TOTALING),

    /** 出荷アップロード */
    BATCH_SHIPMENT_REGIST("出荷アップロード", BatchName.BATCH_SHIPMENT_REGIST),

    /** 郵便番号自動更新 */
    BATCH_ZIP_CODE("郵便番号自動更新", BatchName.BATCH_ZIP_CODE),

    /** 事業所郵便番号自動更新 */
    BATCH_OFFICE_ZIP_CODE("事業所郵便番号自動更新", BatchName.BATCH_OFFICE_ZIP_CODE),

    /** 商品画像更新 */
    BATCH_GOODSIMAGE_UPDATE("商品画像更新", BatchName.BATCH_GOODSIMAGE_UPDATE),

    /** 商品登録非同期バッチ */
    BATCH_GOODS_ASYNCHRONOUS("商品登録非同期バッチ", BatchName.BATCH_GOODS_ASYNCHRONOUS),

    /** 商品グループ在庫状態更新 */
    BATCH_STOCKSTATUSDISPLAY_UPDATE("商品グループ在庫状態更新", BatchName.BATCH_STOCKSTATUSDISPLAY_UPDATE),

    /** オーソリ期限切れ間近注文警告 */
    BATCH_AUTHTIMELIMITORDER_NOTIFICATION("オーソリ期限切れ間近注文警告", BatchName.BATCH_AUTHTIMELIMITORDER_NOTIFICATION),

    /** 在庫開放バッチ */
    BATCH_ORDER_RESERVE_STOCK_RELEASE("在庫開放バッチ", BatchName.BATCH_ORDER_RESERVE_STOCK_RELEASE),

    /** 与信枠解放バッチ */
    BATCH_ORDER_CREDITLINE_REPORT("与信枠解放バッチ", BatchName.BATCH_ORDER_CREDITLINE_REPORT),

    /** 在庫状況メール送信バッチ */
    BATCH_STOCK_REPORT("在庫状況メール送信バッチ", BatchName.BATCH_STOCK_REPORT),

    /** 物流サービスクリアバッチ */
    LOGISTIC_BATCH_CLEAR("物流サービスクリアバッチ", BatchName.LOGISTIC_BATCH_CLEAR),

    /** 受注サービスクリアバッチ */
    ORDER_BATCH_CLEAR("受注サービスクリアバッチ", BatchName.ORDER_BATCH_CLEAR),

    /** 決済サービスクリアバッチ */
    PAYMENT_BATCH_CLEAR("決済サービスクリアバッチ", BatchName.PAYMENT_BATCH_CLEAR),

    /** 販売企画サービスクリアバッチ */
    PRICE_PLANNING_BATCH_CLEAR("販売企画サービスクリアバッチ", BatchName.PRICE_PLANNING_BATCH_CLEAR),

    /** プロモーションサービスクリアバッチ */
    PROMOTION_BATCH_CLEAR("プロモーションサービスクリアバッチ", BatchName.PROMOTION_BATCH_CLEAR),

    /** 商品タグクリアバッチ */
    TAG_BATCH_CLEAR("商品タグクリアバッチ", BatchName.TAG_BATCH_CLEAR),

    /** 受注CSVダウンロードバッチ */
    BATCH_ORDER_CSV_ASYNCHRONOUS("受注CSVダウンロード", BatchName.BATCH_ORDER_CSV_ASYNCHRONOUS),

    /** 決済代行入金結果受取予備処理バッチ */
    BATCH_MULPAY_NOTIFICATION_RECOVERY("入金結果受付予備処理", BatchName.BATCH_MULPAY_NOTIFICATION_RECOVERY),

    /** 支払督促バッチ */
    BATCH_REMINDER_PAYMENT("支払督促メール送信", BatchName.BATCH_REMINDER_PAYMENT),

    /** 支払期限切れバッチ */
    BATCH_EXPIRED_PAYMENT("支払期限切れ処理", BatchName.BATCH_EXPIRED_PAYMENT),

    /** GMO決済キャンセル漏れ検知バッチ */
    BATCH_LINK_PAY_CANCEL_REMINDER("GMO決済キャンセル漏れメール送信", BatchName.BATCH_LINK_PAY_CANCEL_REMINDER),

    /** 商品販売個数集計CSVダウンロードバッチ */
    BATCH_GOODS_SALES_CSV_ASYNCHRONOUS("商品販売個数集計CSVダウンロード", BatchName.BATCH_GOODS_SALES_CSV_ASYNCHRONOUS),

    /** 受注・売上集計CSVダウンロードバッチ */
    BATCH_ORDER_SALES_CSV_ASYNCHRONOUS("受注・売上集計CSVダウンロード", BatchName.BATCH_ORDER_SALES_CSV_ASYNCHRONOUS),

    /** カテゴリ商品更新バッチ */
    CATEGORY_GOODS_REGISTER_UPDATE_BATCH("カテゴリ商品更新", BatchName.CATEGORY_GOODS_REGISTER_UPDATE_BATCH),

    /** 出荷CSVダウンロードバッチ */
    BATCH_SHIPMENT_CSV_ASYNCHRONOUS("出荷CSVダウンロード", BatchName.BATCH_SHIPMENT_CSV_ASYNCHRONOUS),

    /** 検査キット受領登録バッチ */
    BATCH_EXAMKIT_RECEIVED_ENTRY("検査キット受領登録バッチ", BatchName.BATCH_EXAMKIT_RECEIVED_ENTRY),

    /** 検査結果登録バッチ */
    BATCH_EXAM_RESULTS_ENTRY("検査結果登録バッチ", BatchName.BATCH_EXAM_RESULTS_ENTRY);

    /** doma用ファクトリメソッド */
    public static HTypeBatchName of(String value) {

        HTypeBatchName hType = EnumTypeUtil.getEnumFromValue(HTypeBatchName.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}
