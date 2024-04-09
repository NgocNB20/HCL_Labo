/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 条件演算子
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeConditionOperatorType implements EnumType {

    /** が右と一致 */
    MATCH("と一致する", "0"),

    /** が右と一致しない */
    NOT_MATCH("と一致しない", "1"),

    /** が右より大きい */
    GREATER("より大きい", "2"),

    /** が右より少ない */
    LESS("より少ない", "3"),

    /** が右で始まる */
    START_ON("で始まる", "4"),

    /** が右で終わる */
    END_ON("で終わる", "5"),

    /** が右を含む */
    CONTAINS("を含む", "6"),

    /** が右を含まない */
    NOT_CONTAINS("を含まない", "7"),

    /** の設定あり */
    SETTING("設定されている", "8"),

    /** の設定なし */
    NO_SETTING("設定されていない", "9");

    /** doma用ファクトリメソッド */
    public static HTypeConditionOperatorType of(String value) {

        HTypeConditionOperatorType hType = EnumTypeUtil.getEnumFromValue(HTypeConditionOperatorType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /** ラベル */
    private final String label;

    /** 区分値 */
    private final String value;
}
