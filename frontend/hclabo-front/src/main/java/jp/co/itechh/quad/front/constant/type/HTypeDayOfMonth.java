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
 * 日付
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeDayOfMonth implements EnumType {

    /** 1日 */
    FIRST("1日", "1"),

    /** 2日 */
    SECOND("2日", "2"),

    /** 3日 */
    THIRD("3日", "3"),

    /** 4日 */
    FOURTH("4日", "4"),

    /** 5日 */
    FIFTH("5日", "5"),

    /** 6日 */
    SIXTH("6日", "6"),

    /** 7日 */
    SEVENTH("7日", "7"),

    /** 8日 */
    EIGHTH("8日", "8"),

    /** 9日 */
    NINTH("9日", "9"),

    /** 10日 */
    TENTH("10日", "10"),

    /** 11日 */
    ELEVENTH("11日", "11"),

    /** 12日 */
    TWELFTH("12日", "12"),

    /** 13日 */
    THRTEENTH("13日", "13"),

    /** 14日 */
    FOURTEENTH("14日", "14"),

    /** 15日 */
    FIFTEENTH("15日", "15"),

    /** 16日 */
    SIXTEENTH("16日", "16"),

    /** 17日 */
    SEVENTEENTH("17日", "17"),

    /** 18日 */
    EIGHTEENTH("18日", "18"),

    /** 19日 */
    NINETEENTH("19日", "19"),

    /** 20日 */
    TWENTIETH("20日", "20"),

    /** 21日 */
    TWENTYFIRST("21日", "21"),

    /** 22日 */
    TWENTYSECOND("22日", "22"),

    /** 23日 */
    TWENTYTHIRD("23日", "23"),

    /** 24日 */
    TWENTYFOURTH("24日", "24"),

    /** 25日 */
    TWENTYFIFTH("25日", "25"),

    /** 26日 */
    TWENTYSIXTH("26日", "26"),

    /** 27日 */
    TWENTYSEVENTH("27日", "27"),

    /** 28日 */
    TWENTYEIGHTH("28日", "28"),

    /** 29日 */
    TWENTYNINTH("29日", "29"),

    /** 30日 */
    THIRTIETH("30日", "30"),

    /** 末日 */
    LASTDAY("末日", "99");

    /** doma用ファクトリメソッド */
    public static HTypeDayOfMonth of(String value) {

        HTypeDayOfMonth hType = EnumTypeUtil.getEnumFromValue(HTypeDayOfMonth.class, value);

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