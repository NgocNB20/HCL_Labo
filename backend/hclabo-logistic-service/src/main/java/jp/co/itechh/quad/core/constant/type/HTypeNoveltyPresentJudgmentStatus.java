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
 * ノベルティプレゼント判定状態<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeNoveltyPresentJudgmentStatus implements EnumType {

    /** 0:未判定 */
    UNJUDGMENT("未判定", "0"),

    /** 1:判定済 */
    JUDGMENT("判定済", "1");

    /** doma用ファクトリメソッド */
    public static HTypeNoveltyPresentJudgmentStatus of(String value) {

        HTypeNoveltyPresentJudgmentStatus hType =
                        EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentJudgmentStatus.class, value);

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