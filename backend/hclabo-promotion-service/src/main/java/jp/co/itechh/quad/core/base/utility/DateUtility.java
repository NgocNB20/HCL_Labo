/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
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
 *
 */
@Component
public class DateUtility {

    /** コンストラクタの説明・概要 */
    public DateUtility() {
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
     * 書式変換
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
     * 書式変換
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
