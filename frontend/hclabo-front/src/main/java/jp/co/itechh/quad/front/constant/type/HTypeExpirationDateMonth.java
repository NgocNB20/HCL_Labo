/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * クレジットカード有効期限（月）
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeExpirationDateMonth implements EnumType {

    /** 1月 */
    JAN("01", "01"),

    /** 2月 */
    FEB("02", "02"),

    /** 3月 */
    MAR("03", "03"),

    /** 4月 */
    APR("04", "04"),

    /** 5月 */
    MAY("05", "05"),

    /** 6月 */
    JUNE("06", "06"),

    /** 7月 */
    JULY("07", "07"),

    /** 8月 */
    AUG("08", "08"),

    /** 9月 */
    SEPT("09", "09"),

    /** 10月 */
    OCT("10", "10"),

    /** 11月 */
    NOV("11", "11"),

    /** 12月 */
    DEC("12", "12");

    /** doma用ファクトリメソッド */
    public static HTypeExpirationDateMonth of(String value) {

        HTypeExpirationDateMonth hType = EnumTypeUtil.getEnumFromValue(HTypeExpirationDateMonth.class, value);

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