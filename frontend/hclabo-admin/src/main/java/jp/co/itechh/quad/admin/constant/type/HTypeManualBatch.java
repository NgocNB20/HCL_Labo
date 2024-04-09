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
 * 手動起動用バッチ処理名：列挙型
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeManualBatch implements EnumType {

    /** 人気商品ランキング集計 */
    PRODUCT_POPULARITY_TOTALING("人気商品ランキング集計", BatchName.BATCH_PRODUCT_POPULARITY_TOTALING),

    /** 郵便番号自動更新 */
    ZIP_CODE("郵便番号自動更新", BatchName.BATCH_ZIP_CODE),

    /** 事業所郵便番号自動更新 */
    OFFICE_ZIP_CODE("事業所郵便番号自動更新", BatchName.BATCH_OFFICE_ZIP_CODE),

    /** 商品グループ在庫状態更新 */
    STOCKSTATUSDISPLAY_UPDATE("商品グループ在庫状態更新", BatchName.BATCH_STOCKSTATUSDISPLAY_UPDATE),

    /** 決済代行入金結果受取予備処理バッチ */
    BATCH_MULPAY_NOTIFICATION_RECOVERY("入金結果受付予備処理", BatchName.BATCH_MULPAY_NOTIFICATION_RECOVERY),

    /** 支払督促バッチ */
    BATCH_REMINDER_PAYMENT("支払督促メール送信", BatchName.BATCH_REMINDER_PAYMENT),

    /** 支払期限切れバッチ */
    BATCH_EXPIRED_PAYMENT("支払期限切れ処理", BatchName.BATCH_EXPIRED_PAYMENT),

    /** GMO決済キャンセル漏れ検知バッチ */
    BATCH_LINK_PAY_CANCEL_REMINDER("GMO決済キャンセル漏れメール送信", BatchName.BATCH_LINK_PAY_CANCEL_REMINDER);

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
