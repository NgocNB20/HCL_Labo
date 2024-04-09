/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.constant.BatchName;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * バッチ処理名：列挙型
 *
 * @author kaneda
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeBatchName implements EnumType {

    /** TOP画面集計 */
    BATCH_TOP_TOTALING("TOP画面集計", BatchName.BATCH_TOP_TOTALING),

    /** 出荷アップロード */
    BATCH_SHIPMENT_REGIST("出荷アップロード", BatchName.BATCH_SHIPMENT_REGIST),

    /** 受注督促メール送信 */
    BATCH_SETTLEMENT_REMINDER("受注督促メール送信", BatchName.BATCH_SETTLEMENT_REMINDER),

    /** 受注決済期限切れメール送信 */
    BATCH_SETTLEMENT_EXPIRED_NOTIFICATION("受注決済期限切れメール送信", BatchName.BATCH_SETTLEMENT_EXPIRED_NOTIFICATION),

    /** マルチペイメント入金状況問合せ */
    BATCH_MULTIPAYMENT_CONFIRMATION("マルチペイメント入金状況問合せ", BatchName.BATCH_MULTIPAYMENT_CONFIRMATION),

    /** 入金結果受付予備処理メール */
    BATCH_MULPAY_NOTIFICATION_RECOVERY_MAIL("入金結果受付予備処理メール", BatchName.BATCH_MULPAY_NOTIFICATION_RECOVERY_MAIL),

    /** メール送信バッチ全般 */
    BATCH_MAIL("メール送信バッチ全般", BatchName.BATCH_MAIL),

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

    /** サイトマップXML出力バッチ */
    BATCH_SITEMAPXML_OUTPUT("サイトマップXML出力バッチ", BatchName.BATCH_SITEMAPXML_OUTPUT),

    /** オーソリ期限切れ間近注文警告 */
    BATCH_AUTHTIMELIMITORDER_NOTIFICATION("オーソリ期限切れ間近注文警告", BatchName.BATCH_AUTHTIMELIMITORDER_NOTIFICATION),

    /** 在庫開放バッチ */
    BATCH_ORDER_RESERVE_STOCK_RELEASE("在庫開放バッチ", BatchName.BATCH_ORDER_RESERVE_STOCK_RELEASE),

    /** 与信枠解放バッチ */
    BATCH_ORDER_CREDITLINE_REPORT("与信枠解放バッチ", BatchName.BATCH_ORDER_CREDITLINE_REPORT),

    /** 在庫状況メール送信バッチ */
    BATCH_STOCK_REPORT("在庫状況メール送信バッチ", BatchName.BATCH_STOCK_REPORT),

    /** クリアバッチ */
    BATCH_CLEAR("クリアバッチ", BatchName.BATCH_CLEAR),

    /** タスククリーンバッチ */
    BATCH_TASK_CLEAN("タスククリーンバッチ", BatchName.BATCH_TASK_CLEAN),

    /** 受注CSVバッチ */
    BATCH_ORDER_CSV("受注CSV", BatchName.BATCH_ORDER_CSV),

    /** 出荷CSVバッチ */
    BATCH_SHIPMENT_CSV("出荷CSV", BatchName.BATCH_SHIPMENT_CSV),

    /** GMO決済キャンセル漏れ */
    GMO_PAYMENT_CANCEL_FORGOT("GMO決済キャンセル漏れ", BatchName.GMO_PAYMENT_CANCEL_FORGOT),

    /** タグクリアバッチ */
    BATCH_TAG_CLEAR("タグクリアバッチ", BatchName.BATCH_TAG_CLEAR),

    /** カテゴリ商品更新バッチ */
    BATCH_CATEGORY_GOODS_REGISTER_UPDATE("カテゴリ商品更新バッチ", BatchName.BATCH_CATEGORY_GOODS_REGISTER_UPDATE),

    /** 入金過不足 */
    PAYMENT_EXCESS_ALERT("入金過不足", BatchName.PAYMENT_EXCESS_ALERT),

    /** 商品販売個数集計CSVバッチ */
    BATCH_SOLD_GOODS_CSV("商品販売個数集計CSV", BatchName.BATCH_SOLD_GOOD_CSV),

    /** 受注・売上集計CSVバッチ */
    BATCH_ORDER_SALES_CSV("受注・売上集計CSV", BatchName.BATCH_ORDER_SALES_CSV),

    /** 非同期処理(MQ)エラー通知 */
    BATCH_MQ_ERROR_NOTIFICATION("非同期処理(MQ)エラー通知", BatchName.BATCH_MQ_ERROR_NOTIFICATION),

    /** 検査キット受領登録 */
    EXAMKIT_RECEIVED_ENTRY("検査キット受領登録", BatchName.EXAMKIT_RECEIVED_ENTRY),

    /** 検査結果登録 */
    EXAM_RESULTS_ENTRY("検査結果登録", BatchName.EXAM_RESULTS_ENTRY);

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
