/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * キャンセル・返品系
 * 出荷済み商品の在庫戻し可否フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeShipedGoodsStockReturn implements EnumType {

    /** 在庫戻しなし ※ラベル未使用 */
    OFF("", "0"),

    /** 在庫戻しあり ※ラベル未使用 */
    ON("", "1");

    /** doma用ファクトリメソッド */
    public static HTypeShipedGoodsStockReturn of(String value) {

        HTypeShipedGoodsStockReturn hType = EnumTypeUtil.getEnumFromValue(HTypeShipedGoodsStockReturn.class, value);

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