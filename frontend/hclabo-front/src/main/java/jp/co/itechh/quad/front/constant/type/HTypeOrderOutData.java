/*
HTypeSelectionReceiptOfMoneyRegistOutData.java * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 受注データタイプ
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeOrderOutData implements EnumType {

    /** 納品書 */
    INVOICE_ATTACHMENT("納品書", "0"),

    /** 受注明細 */
    ORDER_DETAIL("受注明細", "1"),

    /** 受注商品別明細 */
    ORDER_GOODS_DETAIL("受注商品別明細", "2"),

    /** 出荷登録CSV */
    SHIPMENT_CSV("出荷登録CSV", "4"),

    /** 入金登録CSV */
    PAYMENT_CSV("入金登録CSV", "5"),

    /** 受注CSV */
    ORDER_CSV("受注CSV", "6"),

    /** 受注商品CSV */
    ORDER_GOODS_CSV("受注商品CSV", "7");

    /** doma用ファクトリメソッド */
    public static HTypeOrderOutData of(String value) {

        HTypeOrderOutData hType = EnumTypeUtil.getEnumFromValue(HTypeOrderOutData.class, value);

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