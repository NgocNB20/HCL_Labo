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
 * 月
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeMonth implements EnumType {

    /** 1月 */
    JAN("1", "1"),

    /** 2月 */
    FEB("2", "2"),

    /** 3月 */
    MAR("3", "3"),

    /** 4月 */
    APR("4", "4"),

    /** 5月 */
    MAY("5", "5"),

    /** 6月 */
    JUNE("6", "6"),

    /** 7月 */
    JULY("7", "7"),

    /** 8月 */
    AUG("8", "8"),

    /** 9月 */
    SEPT("9", "9"),

    /** 10月 */
    OCT("10", "10"),

    /** 11月 */
    NOV("11", "11"),

    /** 12月 */
    DEC("12", "12");

    /** doma用ファクトリメソッド */
    public static HTypeMonth of(String value) {

        HTypeMonth hType = EnumTypeUtil.getEnumFromValue(HTypeMonth.class, value);

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