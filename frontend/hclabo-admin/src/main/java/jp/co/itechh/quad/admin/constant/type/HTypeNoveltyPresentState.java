/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ノベルティプレゼント条件状態<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeNoveltyPresentState implements EnumType {

    /** 0:有効 */
    VALID("有効", "0"),

    /** 1:無効 */
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