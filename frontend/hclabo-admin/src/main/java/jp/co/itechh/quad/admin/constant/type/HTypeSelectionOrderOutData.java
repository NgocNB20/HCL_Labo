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
 * 受注データタイプ(選択出力用)
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSelectionOrderOutData implements EnumType {

    /** 受注CSV */
    ORDER_CSV("受注CSV", "0"),

    /** 出荷CSV */
    SHIPMENT_CSV("出荷CSV", "1");

    /** doma用ファクトリメソッド */
    public static HTypeSelectionOrderOutData of(String value) {

        HTypeSelectionOrderOutData hType = EnumTypeUtil.getEnumFromValue(HTypeSelectionOrderOutData.class, value);

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