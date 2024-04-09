/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * 商品金額条件フラグ<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeGoodsPriceTotalFlag implements EnumType {

    /** 条件なし */
    OFF("条件なし", "0"),

    /** 対象商品のみの金額を条件とする */
    ON("対象商品のみの金額を条件とする", "1");

    /** doma用ファクトリメソッド */
    public static HTypeGoodsPriceTotalFlag of(String value) {

        HTypeGoodsPriceTotalFlag hType = EnumTypeUtil.getEnumFromValue(HTypeGoodsPriceTotalFlag.class, value);

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