/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 受注状態（受注検索）：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSelectMapOrderStatus implements EnumType {

    /** 入金確認中 */
    PAYMENT_WAITING("入金確認中", "0"),

    /** 商品準備中 */
    GOODS_PREPARING("商品準備中", "1"),

    /** 出荷完了*/
    SHIPMENT_COMPLETION("出荷完了", "3"),

    /** キャンセル */
    CANCEL("キャンセル", "5"),

    /** 請求決済エラー */
    BILL_SETTLEMENT_ERROR("請求決済エラー", "7"),

    /** キャンセル以外 */
    OTHERCANCEL("キャンセル以外", "8");

    /** doma用ファクトリメソッド */
    public static HTypeSelectMapOrderStatus of(String value) {

        HTypeSelectMapOrderStatus hType = EnumTypeUtil.getEnumFromValue(HTypeSelectMapOrderStatus.class, value);

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