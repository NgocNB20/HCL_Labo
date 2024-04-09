/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

/**
 * ご注文主年代：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeOrderAgeType implements EnumType {

    /** 0～9歳 */
    AGE_0TO9("0～9歳", "00"),

    /** 10～19歳 */
    AGE_10TO19("10～19歳", "01"),

    /** 20～29歳 */
    AGE_20TO29("20～29歳", "02"),

    /** 30～39歳 */
    AGE_30TO39("30～39歳", "03"),

    /** 40～49歳 */
    AGE_40TO49("40～49歳", "04"),

    /** 50～59歳 */
    AGE_50TO59("50～59歳", "05"),

    /** 60～69歳 */
    AGE_60TO69("60～69歳", "06"),

    /** 70～79歳 */
    AGE_70TO79("70～79歳", "07"),

    /** 80～89歳 */
    AGE_80TO89("80～89歳", "08"),

    /** 90～99歳 */
    AGE_90TO99("90～99歳", "09"),

    /** 未回答・その他 */
    UNKNOWN("未回答・その他", "10");

    /**
     * 生年月日から年代を割り出します。
     *
     * @param birthday 生年月日
     * @return 注文主年代
     */
    public static HTypeOrderAgeType getType(Timestamp birthday) {
        if (birthday == null) {
            return UNKNOWN;
        }
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 現在日付を取得
        int currentDate = Integer.parseInt(dateUtility.getCurrentYmd());
        // 生年月日をフォーマット（yyyyMMdd）
        int birthdayDate = Integer.parseInt(dateUtility.formatYmd(birthday));
        // 年齢を算出
        int age = (currentDate - birthdayDate) / 10000;

        if (age < 10) {
            return AGE_0TO9;
        } else if (age >= 10 && age < 20) {
            return AGE_10TO19;
        } else if (age >= 20 && age < 30) {
            return AGE_20TO29;
        } else if (age >= 30 && age < 40) {
            return AGE_30TO39;
        } else if (age >= 40 && age < 50) {
            return AGE_40TO49;
        } else if (age >= 50 && age < 60) {
            return AGE_50TO59;
        } else if (age >= 60 && age < 70) {
            return AGE_60TO69;
        } else if (age >= 70 && age < 80) {
            return AGE_70TO79;
        } else if (age >= 80 && age < 90) {
            return AGE_80TO89;
        } else if (age >= 90 && age < 100) {
            return AGE_90TO99;
        } else {
            return UNKNOWN;
        }
    }

    /** doma用ファクトリメソッド */
    public static HTypeOrderAgeType of(String value) {

        HTypeOrderAgeType hType = EnumTypeUtil.getEnumFromValue(HTypeOrderAgeType.class, value);

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