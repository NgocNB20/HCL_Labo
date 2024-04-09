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
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日付関連Utilityクラス
 *
 * @author negishi
 * @author Nishigaki Mio (itec) 2010/10/21 日付フォーマットメソッド（Date型用）を追加
 * @author tm27400 (itec)       2012/01/19 チケット #2722 対応 convertStringDate() を追加
 * @author Kaneko (itec) 2012/08/20 UtilからHelperへ変更
 */
@Component
public class DateUtility {

    /** コンストラクタの説明・概要 */
    public DateUtility() {
    }

    /** 日付フォーマット yyyyMM */
    public static final String YM = "yyyyMM";

    /** 日付フォーマット yyyyMMdd */
    public static final String YMD = "yyyyMMdd";

    /** 日付フォーマット yyyy/MM/dd */
    public static final String YMD_SLASH = "yyyy/MM/dd";

    /** 時刻フォーマット HH:mm:ss */
    public static final String HMS = "HH:mm:ss";

    /** 日時フォーマット yyyy/MM/dd HH:mm:ss */
    public static final String YMD_SLASH_HMS = YMD_SLASH + " " + HMS;

    /** 日付フォーマット yyyyMMddHHmmss */
    public static final String YMDHMS = "yyyyMMddHHmmss";

    /**
     * 指定された年月をDateに変換する<br/>
     * 日付に変換できない場合、nullを返却する。
     *
     * @param year  年
     * @param month 月
     * @return Date
     */
    public Date toDate(String year, String month) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
            return formatter.parse(year + "/" + month);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 指定された年月をTimestampに変換する<br/>
     * 日付に変換できない場合、nullを返却する。
     *
     * @param year  年
     * @param month 月
     * @return Timestamp
     */
    public Timestamp toTimestamp(String year, String month) {
        Date date = toDate(year, month);
        if (date == null) {
            return null;
        } else {
            return new Timestamp(date.getTime());
        }
    }

    /**
     * 現在日付のx日後を返す（x日前も可）<br/>
     *
     * @param addDate 追加する日数
     * @return Timestamp 現在日付に追加する日数を加えた日付（yyyy-MM-dd 00:00:00.0）
     */
    public Timestamp getAdditionalDate(int addDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.DAY_OF_MONTH, addDate);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 現在日付を返す<br/>
     *
     * @return Timestamp 現在日付（yyyy-MM-dd 00:00:00.0）
     */
    public Timestamp getCurrentDate() {
        return TimestampConversionUtil.toTimestamp(getCurrentYmd(), YMD);
    }

    /**
     * 現在日時を返す
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
     * 現在日付を返す<br/>
     *
     * @return String 現在日付（yyyyMMddHHmmss）
     */
    public String getCurrentYmdHms() {
        return format(getCurrentTime(), YMDHMS);
    }

    /**
     * 指定日付のN月前の月末を取得<br/>
     * 指定日付の月末の場合は0を指定
     *
     * @param amountMonth N月前
     * @param timestamp   指定日付
     * @return 月末
     */
    public Timestamp getEndOfMonth(int amountMonth, Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.add(Calendar.MONTH, -amountMonth);
        int endOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, endOfMonth);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 書式変換
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
     * 書式変換
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
     * 年月日フォーマット(yyyy/MM/dd)<br/>
     *
     * @param value 変換元タイムスタンプ
     * @return フォーマット後の値
     */
    public String formatYmdWithSlash(Timestamp value) {
        if (value == null) {
            return null;
        }
        return format(value, YMD_SLASH);
    }

    /**
     * 年月フォーマット(yyyyMM)<br/>
     *
     * @param value 変換元タイムスタンプ
     * @return フォーマット後の値
     */
    public String formatYm(Timestamp value) {
        if (value == null) {
            return null;
        }
        return format(value, YM);
    }

    /**
     * 二つのDateが同一日かどうかを確認する
     *
     * @param date1 日付１ 時分秒は判定に使用されない
     * @param date2 日付２ 時分秒は判定に使用されない
     * @return 比較結果
     */
    public boolean compareDate(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        }

        if (date1 == null || date2 == null) {
            return false;
        }

        String ymd1 = formatYmd(new Timestamp(date1.getTime()));
        String ymd2 = formatYmd(new Timestamp(date2.getTime()));

        return ymd1.equals(ymd2);
    }

    /**
     * 二つのDateが同一月かどうかを確認する
     *
     * @param date1 日付１ 日時分秒は判定に使用されない
     * @param date2 日付２ 日時分秒は判定に使用されない
     * @return 比較結果
     */
    public boolean compareMonth(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        }

        if (date1 == null || date2 == null) {
            return false;
        }

        String ym1 = formatYm(new Timestamp(date1.getTime()));
        String ym2 = formatYm(new Timestamp(date2.getTime()));

        return ym1.equals(ym2);
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

    /**
     * 減算分で時間を取得
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
     * 指定日付の最終時刻を取得(23時59分59秒)
     *
     * @param timestamp 指定日付
     * @return 指定日付の最終時刻
     */
    public Timestamp getEndOfDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }
}