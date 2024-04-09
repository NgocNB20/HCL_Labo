/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.base.utility;

import jp.co.itechh.quad.hclabo.core.base.util.seasar.TimestampConversionUtil;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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

    /**
     * コンストラクタの説明・概要
     */
    public DateUtility() {
    }

    /**
     * 日付フォーマット yyyyMMdd
     */
    public static final String YMD = "yyyyMMdd";

    /**
     * 日付フォーマット yyyy/MM/dd
     */
    public static final String YMD_SLASH = "yyyy/MM/dd";

    /**
     * 時刻フォーマット HH:mm:ss
     */
    public static final String HMS = "HH:mm:ss";

    /**
     * 時刻フォーマット HHmmss
     */
    public static final String HMS_NON_COLON = "HHmmss";

    /**
     * 時刻フォーマット HH:mm
     */
    public static final String HM = "HH:mm";

    /**
     * 日時フォーマット yyyy/MM/dd HH:mm:ss
     */
    public static final String YMD_SLASH_HMS = YMD_SLASH + " " + HMS;

    /** 日時フォーマット */
    public static final String YMD_HMS = "yyyyMMddHHmmss";

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
     * 現在日付を返す
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
     * 現在日付を返す
     *
     * @return String 現在日付（yyyyMMdd）
     */
    public String getCurrentYmd() {
        return format(getCurrentTime(), YMD);
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
     * 指定された時間を加算または減算したTimestamp型を返します。
     *
     * @param amountMinute  加算(減算)する量
     * @param plus          加算の場合はtrue、減算の場合はfalse
     * @param date          日時
     * @return 指定された時間を加算(減算)したTimestamp
     */
    public Timestamp getAmountMinuteTimestamp(int amountMinute, boolean plus, Timestamp date) {
        return getAmountTimestamp(amountMinute, plus, date, Calendar.MINUTE);
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
     * 開始・終了時間内かどうかを判定
     * 開示・終了時間を指定しない場合は、比較対象からはずす為、
     * 「期間内」と判定する
     * ※比較対象は、現在日時
     *
     * @param openStartTime 期間開始時間(Null許可)
     * @param openEndTime   期間終了時間(Null許可)
     * @return true=期間内、false=期間外
     */
    public boolean isOpen(Timestamp openStartTime, Timestamp openEndTime) {

        // 現在日時
        Timestamp currentTime = this.getCurrentTime();
        return isOpen(openStartTime, openEndTime, currentTime);
    }

    /**
     * 開始・終了時間内かどうかを判定
     * 開示・終了時間を指定しない場合は、比較対象からはずす為、
     * 「期間内」と判定する
     *
     * @param startTime  期間開始時間(Null許可)
     * @param endTime    期間終了時間(Null許可)
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
}