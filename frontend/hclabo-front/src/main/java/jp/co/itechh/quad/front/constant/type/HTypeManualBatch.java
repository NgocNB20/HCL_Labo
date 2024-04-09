/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.constant.BatchName;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 手動起動用バッチ処理名：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeManualBatch implements EnumType {

    /** TOP画面集計 */
    TOP_TOTALING("TOP画面集計", BatchName.BATCH_TOP_TOTALING),

    /** 受注督促メール送信 */
    SETTLEMENT_REMINDER("受注督促メール送信", BatchName.BATCH_SETTLEMENT_REMINDER),

    /** 受注決済期限切れメール送信 */
    SETTLEMENT_EXPIRED_NOTIFICATION("受注決済期限切れメール送信", BatchName.BATCH_SETTLEMENT_EXPIRED_NOTIFICATION),

    /** マルチペイメント入金状況問合せ */
    MULTIPAYMENT_CONFIRMATION("マルチペイメント入金状況問合せ", BatchName.BATCH_MULTIPAYMENT_CONFIRMATION),

    /** マルチペイメント入金処理 */
    MULTIPAYMENT_PAYMENT("マルチペイメント入金処理", BatchName.BATCH_MULTIPAYMENT_PAYMENT),

    /** 郵便番号自動更新 */
    ZIP_CODE("郵便番号自動更新", BatchName.BATCH_ZIP_CODE),

    /** 事業所郵便番号自動更新 */
    OFFICE_ZIP_CODE("事業所郵便番号自動更新", BatchName.BATCH_OFFICE_ZIP_CODE),

    /** 商品グループ在庫状態更新 */
    STOCKSTATUSDISPLAY_UPDATE("商品グループ在庫状態更新", BatchName.BATCH_STOCKSTATUSDISPLAY_UPDATE),

    /** サイトマップXML出力バッチ */
    SITEMAPXML_OUTPUT("サイトマップXML出力バッチ", BatchName.BATCH_SITEMAPXML_OUTPUT);

    /** doma用ファクトリメソッド */
    public static HTypeManualBatch of(String value) {

        HTypeManualBatch hType = EnumTypeUtil.getEnumFromValue(HTypeManualBatch.class, value);

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