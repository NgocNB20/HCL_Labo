/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.util.seasar.TimestampConversionUtil;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 日付関連Utilityクラス
 *
 * @author negishi
 * @author Nishigaki Mio (itec) 2010/10/21 日付フォーマットメソッド（Date型用）を追加
 * @author tm27400 (itec)       2012/01/19 チケット #2722 対応 convertStringDate() を追加
 * @author Kaneko (itec) 2012/08/20 UtilからHelperへ変更
 *
 */
@Component
public class DateUtility {

    /** コンストラクタの説明・概要 */
    public DateUtility() {
    }

    /** 日付フォーマット yyyy */
    public static final String Y = "yyyy";

    /** 日付フォーマット yyyyMM */
    public static final String YM = "yyyyMM";

    /** 日付フォーマット yyyyMMdd */
    public static final String YMD = "yyyyMMdd";

    /** 日付フォーマット yyyy/ */
    public static final String Y_SLASH = "yyyy/";

    /** 日付フォーマット yyyy/MM */
    public static final String YM_SLASH = "yyyy/MM";

    /** 日付フォーマット yyyy/MM/dd */
    public static final String YMD_SLASH = "yyyy/MM/dd";

    /** 日付フォーマット yyyy年M月 */
    public static final String YM_JP = "yyyy年M月";

    /** 日付フォーマット yyyy年M月d日 */
    public static final String YMD_JP = "yyyy年M月d日";

    /** 日付フォーマット yyyy年M月d日H時m分 */
    public static final String YMD_HMS_JP = "yyyy年M月d日H時m分";

    /** 日付フォーマット M月d日 */
    public static final String MD_JP = "M月d日";

    /** 時刻フォーマット HH:mm:ss */
    public static final String HMS = "HH:mm:ss";

    /** 時刻フォーマット HHmmss */
    public static final String HMS_NON_COLON = "HHmmss";

    /** 時刻フォーマット HH:mm */
    public static final String HM = "HH:mm";

    /** 時刻フォーマット H:mm */
    public static final String HM_SINGLE = "H:mm";

    /** 日時フォーマット yyyy/MM/dd HH:mm */
    public static final String YMD_SLASH_HM = YMD_SLASH + " " + HM;

    /** 日時フォーマット yyyy/MM/dd HH:mm:ss */
    public static final String YMD_SLASH_HMS = YMD_SLASH + " " + HMS;

    /** 日時フォーマット(ISO8601) yyyy-MM-dd'T'HH:mm:ssZZ */
    public static final String ISO_8601_EXT = "yyyy-MM-dd'T'HH:mm:ssZZ";

    /** 日付フォーマット　yyyy/M/d */
    public static final String YMD_SLASH_SINGLE = "yyyy/M/d";

    /** 日時フォーマット yyyy/M/d H:m */
    public static final String YMD_SLASH_SINGLE_HM = YMD_SLASH_SINGLE + " " + HM_SINGLE;

    /** 日時フォーマット */
    public static final String YMD_HMS_MS = "yyyyMMddHHmmssSSS";

    /** マップキー：開始日時 */
    public static final String START_TIME = "startTime";

    /** マップキー：終了日時 */
    public static final String END_TIME = "endTime";

    /** 期間取得メソッド引数：今日 */
    public static final String TERM_TODAY = "1";

    /** 期間取得メソッド引数：昨日 */
    public static final String TERM_YESTERDAY = "2";

    /** 期間取得メソッド引数：今月 */
    public static final String TERM_THIS_MONTH = "3";

    /** 期間取得メソッド引数：先月 */
    public static final String TERM_LAST_MONTH = "4";

    /** 期間取得メソッド引数のリスト */
    public static final List<String> TERM_LIST =
                    Arrays.asList(TERM_TODAY, TERM_YESTERDAY, TERM_THIS_MONTH, TERM_LAST_MONTH);

    /**
     * 指定された年月をDateに変換する<br/>
     * 日付に変換できない場合、nullを返却する。
     *
     * @param year 年
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
     * @param year 年
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
     * 日付妥当性チェック<br/>
     *
     * @param value 対象値
     * @param datePatterns 日付書式複数パターン
     * @return true..OK / false..NG
     */
    public boolean isDate(Object value, String[] datePatterns) {

        if (Objects.equals(value, null) || Objects.equals(value, "") || datePatterns == null) {
            return false;
        }

        boolean errorFlag = true;

        if (value instanceof String) {
            for (String pattern : datePatterns) {
                if (isDate((String) value, pattern)) {
                    errorFlag = false;
                    break;
                }
            }
        } else if (value instanceof Date) {
            for (String pattern : datePatterns) {
                if (isDate((Date) value, pattern)) {
                    errorFlag = false;
                    break;
                }
            }
        } else {
            // ありえないはずだが型が不正
            return false;
        }

        if (errorFlag) {
            // 日付として存在しないか、指定された書式と違う。
            return false;
        }

        return true;
    }

    /**
     * 厳密な日付妥当性チェック<br/>
     *
     * @param value 対象値
     * @param datePattern 日付書式パターン
     * @return true..OK / false..NG
     */
    public boolean isDate(Date value, String datePattern) {
        if (value == null || datePattern == null) {
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        return isDate(formatter.format(value), datePattern);
    }

    /**
     * 厳密な日付妥当性チェック<br/>
     *
     * @param value 対象値
     * @param datePattern 日付書式パターン
     * @return true..OK / false..NG
     */
    public boolean isDate(String value, String datePattern) {
        return isDate(value, datePattern, false);
    }

    /**
     * 日付妥当性チェック<br/>
     *
     * @param value 対象値
     * @param datePattern 日付書式パターン
     * @param lenient 厳密にチェックするか否か。true..寛大 / false..厳密
     * @return true..OK / false..NG
     */
    public boolean isDate(String value, String datePattern, boolean lenient) {

        if (value == null || datePattern == null || datePattern.length() <= 0) {
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        // 厳密にチェック
        formatter.setLenient(false);

        try {
            Date dateValue = formatter.parse(value);

            // さらに厳密にチェックする場合
            if (!lenient) {
                // 入力値が書式どおりに入力されていればformat結果と一致する
                boolean match = value.equals(formatter.format(dateValue));

                // 年の範囲が 0001年から9999年であることを確認する。
                // PostgresqlのTimestamp型には1465001年まで、Date型には32767年までしか格納できない為。
                // システムの特性上、年の範囲を0001年から9999年に限定しても問題がないので、年が4桁を超える場合はエラーとする。
                formatter.applyPattern("yyyy");
                return match && formatter.format(dateValue).length() < 5;
            } else {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
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
     * 現在日付を返す<br/>
     *
     * @return String 現在日付（yyyy/MM/dd）
     */
    public String getCurrentYmdWithSlash() {
        return format(getCurrentTime(), YMD_SLASH);
    }

    /**
     * 現在日付を返す<br/>
     *
     * @return String 現在日付（yyyyMM）
     */
    public String getCurrentYm() {
        return format(getCurrentTime(), YM);
    }

    /**
     * 現在日付を返す<br/>
     *
     * @return String 現在日付（yyyy/MM）
     */
    public String getCurrentYmWithSlash() {
        return format(getCurrentTime(), YM_SLASH);
    }

    /**
     * 現在年を返す<br/>
     *
     * @return String 現在日付（yyyy）
     */
    public String getCurrentY() {
        return format(getCurrentTime(), Y);
    }

    /**
     * 現在年を返す<br/>
     *
     * @return String 現在日付（yyyy/）
     */
    public String getCurrentYWithSlash() {
        return format(getCurrentTime(), Y_SLASH);
    }

    /**
     * 書式変換<br/>
     *
     * @param value 変換元の値(Timestamp)
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
     * @param value 変換元の値(Date)
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
     * 年月日フォーマット(yyyy/MM/dd)<br/>
     *
     * @param value 変換元Date
     * @return フォーマット後の値
     */
    public String formatYmdWithSlash(Date value) {
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
     * 年月日フォーマット(yyyy/MM/dd)<br/>
     *
     * @param value 変換元タイムスタンプ
     * @return フォーマット後の値
     */
    public String formatYmWithSlash(Timestamp value) {
        if (value == null) {
            return null;
        }
        return format(value, YM_SLASH);
    }

    /**
     * 年月日フォーマット(yyyy/MM/dd)<br/>
     *
     * @param value 変換元Date
     * @return フォーマット後の値
     */
    public String formatYmWithSlash(Date value) {
        if (value == null) {
            return null;
        }
        return format(value, YM_SLASH);
    }

    /**
     * 時刻フォーマット(HH:mm:ss)<br/>
     *
     * @param value 変換元タイムスタンプ
     * @return フォーマット後の値
     */
    public String formatHMS(Timestamp value) {
        if (value == null) {
            return null;
        }
        return format(value, HMS);
    }

    /**
     * 時刻フォーマット(HH:mm)<br/>
     *
     * @param value 変換元タイムスタンプ
     * @return フォーマット後の値
     */
    public String formatHM(Timestamp value) {
        if (value == null) {
            return null;
        }
        return format(value, HM);
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

    /**
     * 指定日付のN月前の月初め(1日)を取得<br/>
     * 指定日付の月初めの場合は0を指定
     *
     * @param amountMonth N月前
     * @param timestamp 指定日付
     * @return 月始め
     */
    public Timestamp getStartOfMonth(int amountMonth, Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.add(Calendar.MONTH, -amountMonth);
        int startOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, startOfMonth);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 指定日付のN月前の月末を取得<br/>
     * 指定日付の月末の場合は0を指定
     *
     * @param amountMonth N月前
     * @param timestamp 指定日付
     * @return 月末
     */
    public Timestamp getEndOfMonth(int amountMonth, Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.add(Calendar.MONTH, -amountMonth);
        int endOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, endOfMonth);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 指定された年数を加算または減算したTimestamp型を返します。
     *
     * @param amountYears    加算(減算)する量
     * @param plus           加算の場合はtrue、減算の場合はfalse
     * @param date           日時
     * @return 指定された年数を加算(減算)したTimestamp
     */
    public Timestamp getAmountYearTimestamp(int amountYears, boolean plus, Timestamp date) {
        return getAmountTimestamp(amountYears, plus, date, Calendar.YEAR);
    }

    /**
     * 指定された月数を加算または減算したTimestamp型を返します。
     *
     * @param amountMonths    加算(減算)する量
     * @param plus            加算の場合はtrue、減算の場合はfalse
     * @param date            日時
     * @return 指定された月数を加算(減算)したTimestamp
     */
    public Timestamp getAmountMonthTimestamp(int amountMonths, boolean plus, Timestamp date) {
        return getAmountTimestamp(amountMonths, plus, date, Calendar.MONTH);
    }

    /**
     * 指定された日数を加算または減算したTimestamp型を返します。
     *
     * @param amountDays    加算(減算)する量
     * @param plus          加算の場合はtrue、減算の場合はfalse
     * @param date          日時
     * @return 指定された日数を加算(減算)したTimestamp
     */
    public Timestamp getAmountDayTimestamp(int amountDays, boolean plus, Timestamp date) {
        return getAmountTimestamp(amountDays, plus, date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 指定された時間を加算または減算したTimestamp型を返します。
     *
     * @param amountHours   加算(減算)する量
     * @param plus          加算の場合はtrue、減算の場合はfalse
     * @param date          日時
     * @return 指定された時間を加算(減算)したTimestamp
     */
    public Timestamp getAmountHourTimestamp(int amountHours, boolean plus, Timestamp date) {
        return getAmountTimestamp(amountHours, plus, date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 指定された分を加算または減算したTimestamp型を返します。
     *
     * @param amountMinutes  加算(減算)する量
     * @param plus           加算の場合はtrue、減算の場合はfalse
     * @param date           日時
     * @return 指定された分を加算(減算)したTimestamp
     */
    public Timestamp getAmountMinuteTimestamp(int amountMinutes, boolean plus, Timestamp date) {
        return getAmountTimestamp(amountMinutes, plus, date, Calendar.MINUTE);
    }

    /**
     * 指定された秒を加算または減算したTimestamp型を返します。
     *
     * @param amountSeconds   加算(減算)する量
     * @param plus            加算の場合はtrue、減算の場合はfalse
     * @param date            日時
     * @return 指定された秒を加算(減算)したTimestamp
     */
    public Timestamp getAmountSecondTimestamp(int amountSeconds, boolean plus, Timestamp date) {
        return getAmountTimestamp(amountSeconds, plus, date, Calendar.SECOND);
    }

    /**
     * 指定された分を加算または減算したTimestamp型を返します。
     *
     * @param amount        加算(減算)する量
     * @param plus          加算の場合はtrue、減算の場合はfalse
     * @param date          日時
     * @param targetField   加算(減算)する対象(java.util.Calendarの定数フィールドを指定する)
     * @return 指定された分を加算(減算)したTimestamp
     */
    public Timestamp getAmountTimestamp(int amount, boolean plus, Timestamp date, int targetField) {
        if (date == null) {
            return null;
        }
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
     * 開始・終了時間内かどうかを判定<br/>
     * 開示・終了時間を指定しない場合は、比較対象からはずす為、<br/>
     * 「期間内」と判定する
     * ※比較対象は、現在日時
     *
     * @param openStartTime 期間開始時間(Null許可)
     * @param openEndTime 期間終了時間(Null許可)
     * @return true=期間内、false=期間外
     */
    public boolean isOpen(Timestamp openStartTime, Timestamp openEndTime) {

        // 現在日時
        Timestamp currentTime = this.getCurrentTime();
        return isOpen(openStartTime, openEndTime, currentTime);
    }

    /**
     * 開始・終了時間内かどうかを判定<br/>
     * 開示・終了時間を指定しない場合は、比較対象からはずす為、<br/>
     * 「期間内」と判定する
     *
     * @param startTime 期間開始時間(Null許可)
     * @param endTime 期間終了時間(Null許可)
     * @param targetTime 比較する時間
     * @return true=期間内、false=期間外
     */
    public boolean isOpen(Timestamp startTime, Timestamp endTime, Timestamp targetTime) {

        // 開始時間指定がある場合
        boolean startFlg = false;
        if (startTime == null) {
            startFlg = true;
        } else {
            if (startTime.equals(targetTime) || startTime.before(targetTime)) {
                startFlg = true;
            }
        }

        // 終了時間指定がある場合
        boolean endFlg = false;
        if (endTime == null) {
            endFlg = true;
        } else {
            if (endTime.equals(targetTime) || endTime.after(targetTime)) {
                endFlg = true;
            }
        }

        // 両方OKの場合
        return startFlg && endFlg;
    }

    /**
     * yyyyMMddフォーマット文字列を指定日付フォーマットに変換<br/>
     * yyyyMMddにマッチしない場合はnullを返す<br/>
     *
     * @param ymd yyyyMMdd文字列
     * @param format 日付フォーマット
     * @return フォーマット変換後の文字列
     */
    public String getYmdFormatValue(String ymd, String format) {

        if (ymd == null) {
            return null;
        }

        if (!ymd.matches("[0-9]{8}")) {
            return null;
        }

        int year = Integer.parseInt(ymd.substring(0, 4));
        int month = Integer.parseInt(ymd.substring(4, 6));
        int day = Integer.parseInt(ymd.substring(6));

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    /**
     * 文字列で表記された日付（時刻も可）の書式を変換する。<br/>
     * <pre>
     * yyyy/MM/dd 形式文字列→ yyyyMMdd 形式文字列
     * </pre>
     * @param stringDate 日付文字列
     * @param oldFormat  変換前の日付フォーマット
     * @param newFormat  変換後の日付フォーマット
     * @return 変換後の日付。<br/>無効な引数を受け取った場合 null
     */
    public String convertStringDate(String stringDate, String oldFormat, String newFormat) {

        if (StringUtil.isEmpty(stringDate)) {
            return null;
        }

        Date obj = null;

        try {
            obj = new SimpleDateFormat(oldFormat).parse(stringDate);
        } catch (ParseException pe) {
            return null;
        }

        return new SimpleDateFormat(newFormat).format(obj);

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

    /**
     * 日付が本日よりも過去であるかどうかを判定します。<br/>
     *
     * @param date 日付
     * @return 過去であればtrue
     */
    public boolean isDatePast(Date date) {
        if (date == null) {
            return false;
        }
        // 本日の日付
        Date today = this.getCurrentDate();

        return (today.after(date));
    }

    /**
     * 指定された日時が現在日時よりも未来であるかどうかを判定します。<br/>
     *
     * @param time 指定日時
     * @return true:未来 false:過去または同時刻
     */
    public boolean isAfterCurrentTime(Timestamp time) {
        if (time == null) {
            return false;
        }
        // 現在日時
        Timestamp currenttime = this.getCurrentTime();

        return time.after(currenttime);
    }

    /**
     * 指定された日が現在日時よりも過去日時であるかどうかを判定します。<br/>
     *
     * @param time 指定日時
     * @return true:過去日時 false:未来日時または同時刻
     */
    public boolean isBeforeCurrentTime(Timestamp time) {
        if (time == null) {
            return false;
        }
        // 現在日時
        Timestamp currenttime = this.getCurrentTime();

        return time.before(currenttime);
    }

    /**
     * 指定された日が現在<b>日</b>よりも過去<b>日</b>であるかどうかを判定します。<br/>
     *
     * @param date 指定日
     * @return true:過去日または同日 false:過去日
     */
    public boolean isBeforeCurrentDate(Timestamp date) {
        if (date == null) {
            return false;
        }
        // 現在日
        Timestamp currentdate = this.getCurrentDate();

        return !date.after(currentdate);
    }

    /**
     * 指定された期間の最初日時、最終日時のマップを取得する<br/>
     * <ul>
     * 引数：DateUtility#TERM_TODAY、DateUtility#TERM_YESTERDAY、DateUtility#TERM_THIS_MONTH、DateUtility#TERM_LAST_MONTH<br />
     * マップキー：DateUtility#START_TIME、DateUtility#END_TIME
     * </ul>
     *
     * @param termType 指定期間：今日、昨日、今月、先月
     * @return 最初日時、終了日時のマップ
     */
    public Map<String, Timestamp> getTermMap(String termType) {
        if (StringUtil.isEmpty(termType) || !TERM_LIST.contains(termType)) {
            return null;
        }
        Timestamp currentDate = this.getCurrentDate();
        Timestamp startTime = null;
        Timestamp endTime = null;

        Map<String, Timestamp> termMap = new HashMap<>();
        if (TERM_TODAY.equals(termType)) {
            startTime = currentDate;
            endTime = currentDate;

        } else if (TERM_YESTERDAY.equals(termType)) {
            startTime = this.getAmountDayTimestamp(1, false, currentDate);
            endTime = this.getAmountDayTimestamp(1, false, currentDate);

        } else if (TERM_THIS_MONTH.equals(termType)) {
            startTime = this.getStartOfMonth(0, currentDate);
            endTime = this.getEndOfMonth(0, currentDate);

        } else {
            startTime = this.getStartOfMonth(1, currentDate);
            endTime = this.getEndOfMonth(1, currentDate);

        }

        termMap.put(START_TIME, startTime);
        termMap.put(END_TIME, this.getEndOfDate(endTime));

        return termMap;
    }

    /**
     * 指定した日時の年度の範囲を取得
     *
     * @param target 日時
     * @return [0] = 4月初, [1] = 3月末
     */
    public Timestamp[] getRengeYear(Timestamp target) {
        if (target == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(target);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        if (month < 3) {
            year--;
        }

        Timestamp dateTimeFrom = toTimestamp(String.valueOf(year), "4");
        Timestamp dateTimeTo = getEndOfMonth(0, toTimestamp(String.valueOf(year + 1), "3"));
        dateTimeTo = getEndOfDate(dateTimeTo);
        return new Timestamp[] {dateTimeFrom, dateTimeTo};
    }

    /**
     * 指定した日時の月の範囲を取得
     *
     * @param target 日時
     * @return [0] = 月初, [1] = 月末
     */
    public Timestamp[] getRengeMonth(Timestamp target) {
        if (target == null) {
            return null;
        }
        Timestamp dateTimeFrom = getStartOfMonth(0, target);
        Timestamp dateTimeTo = getEndOfMonth(0, target);
        dateTimeTo = getEndOfDate(dateTimeTo);
        return new Timestamp[] {dateTimeFrom, dateTimeTo};
    }

    /**
     * Date型からTimestampに変換
     *
     * @param date 日時
     * @return Timestamp
     */
    public Timestamp convertDateToTimestamp(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
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
}