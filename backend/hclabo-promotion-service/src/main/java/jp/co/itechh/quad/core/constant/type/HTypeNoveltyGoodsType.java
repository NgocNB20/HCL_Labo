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
 * ノベルティ商品フラグ
 *
 * @author Pham Quang Dieu
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeNoveltyGoodsType implements EnumType {

    /** 0:通常商品 */
    NORMAL_GOODS("通常商品", "0"),

    /** 1:ノベルティ商品 */
    NOVELTY_GOODS("ノベルティ商品", "1");

    /** doma用ファクトリメソッド */
    public static HTypeNoveltyGoodsType of(String value) {

        HTypeNoveltyGoodsType hType = EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class, value);

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
