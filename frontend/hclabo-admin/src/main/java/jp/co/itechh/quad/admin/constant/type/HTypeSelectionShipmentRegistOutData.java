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
 * 出荷登録データタイプ(選択出力用)
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSelectionShipmentRegistOutData implements EnumType {

    /** 出荷登録CSV */
    SHIPMENT_CSV("出荷登録CSV", "0");

    /** doma用ファクトリメソッド */
    public static HTypeSelectionShipmentRegistOutData of(String value) {

        HTypeSelectionShipmentRegistOutData hType =
                        EnumTypeUtil.getEnumFromValue(HTypeSelectionShipmentRegistOutData.class, value);

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