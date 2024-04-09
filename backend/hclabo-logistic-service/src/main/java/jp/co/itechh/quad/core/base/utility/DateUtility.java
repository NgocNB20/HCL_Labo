/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.base.util.seasar.TimestampConversionUtil;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日付関連Utilityクラス
 */
@Component
public class DateUtility {

    /** コンストラクタの説明・概要 */
    public DateUtility() {
    }

    /** 日付フォーマット yyyyMMdd */
    public static final String YMD = "yyyyMMdd";

    /** 日付フォーマット yyyy/MM/dd */
    public static final String YMD_SLASH = "yyyy/MM/dd";

    /** 時刻フォーマット HH:mm:ss */
    public static final String HMS = "HH:mm:ss";

    /** 日時フォーマット yyyy/MM/dd HH:mm:ss */
    public static final String YMD_SLASH_HMS = YMD_SLASH + " " + HMS;

    /**
     * 現在日付を返す<br/>
     *
     * @return Timestamp 現在日付（yyyy-MM-dd 00:00:00.0）
     */
    public Timestamp getCurrentDate() {
        return TimestampConversionUtil.toTimestamp(getCurrentYmd(), YMD);
    }

    /**
     * 現在日時を返す<br/>
     *
     * @return Timestamp 現在日時（yyyy-MM-dd HH:mm:ss.SSS）
     */
    public Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 現在日付を返す<br/>
     *
     * @return String 現在日付（yyyyMMdd）
     */
    public String getCurrentYmd() {
        return format(getCurrentTime(), YMD);
    }

    /**
     * 書式変換<br/>
     *
     * @param value   変換元の値(Timestamp)
     * @param pattern 日付書式パターン
     * @return 変換後の値
     */
    public String format(Timestamp value, String pattern) {

        if (value == null || pattern == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(value);
    }

    /**
     * 書式変換<br/>
     *
     * @param value   変換元の値(Date)
     * @param pattern 日付書式パターン
     * @return 変換後の値
     */
    public String format(Date value, String pattern) {

        if (value == null || pattern == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(value);
    }

    /**
     * 指定された日数を加算または減算したTimestamp型を返します。
     *
     * @param amountDays 加算(減算)する量
     * @param plus       加算の場合はtrue、減算の場合はfalse
     * @param date       日時
     * @return 指定された日数を加算(減算)したTimestamp
     */
    public Timestamp getAmountDayTimestamp(int amountDays, boolean plus, Timestamp date) {
        return getAmountTimestamp(amountDays, plus, date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 指定された分を加算または減算したTimestamp型を返します。
     *
     * @param amount      加算(減算)する量
     * @param plus        加算の場合はtrue、減算の場合はfalse
     * @param date        日時
     * @param targetField 加算(減算)する対象(java.util.Calendarの定数フィールドを指定する)
     * @return 指定された分を加算(減算)したTimestamp
     */
    public Timestamp getAmountTimestamp(int amount, boolean plus, Timestamp date, int targetField) {

        // 減算の場合
        if (!plus) {
            amount = -1 * amount;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(targetField, amount);

        // 基準日数より算出した日付
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 年月日フォーマット(yyyyMMdd)<br/>
     *
     * @param value 変換元タイムスタンプ
     * @return フォーマット後の値
     */
    public String formatYmd(Timestamp value) {
        if (value == null) {
            return null;
        }
        return format(value, YMD);
    }

    /**
     * 減算分で日付を取得
     *
     * @param minutes 分
     * @return Timestamp（yyyy-MM-dd 00:00:00.0）
     */
    public Timestamp getDateBySubtractionMinutes(int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.MINUTE, -minutes);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     *
     * 指定フォーマット形式の文字列をTimestamp型に変換して返します。<br/>
     *
     * @param value  formatに指定した形式の日付文字列
     * @param format 日付フォーマット
     * @return 変換後のTimestamp
     */
    public Timestamp toTimestampValue(String value, String format) {
        if (value == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(format);
        ParsePosition p = new ParsePosition(0);
        Timestamp timestamp = new Timestamp(f.parse(value, p).getTime());
        return timestamp;
    }

}
