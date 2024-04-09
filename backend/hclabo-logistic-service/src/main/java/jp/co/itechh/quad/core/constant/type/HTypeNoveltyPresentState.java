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
 * ノベルティプレゼント条件状態<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeNoveltyPresentState implements EnumType {

    /** 有効 */
    VALID("有効", "0"),

    /** 無効 */
    INVALID("無効", "1");

    /** doma用ファクトリメソッド */
    public static HTypeNoveltyPresentState of(String value) {

        HTypeNoveltyPresentState hType = EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentState.class, value);

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