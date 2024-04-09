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

/**
 * 日付関連Utilityクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
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
     * 日時フォーマット yyyy/MM/dd HH:mm:ss
     */
    public static final String YMD_SLASH_HMS = YMD_SLASH + " " + HMS;

    /**
     * 現在日付を返す
     *
     * @return String 現在日付（yyyyMMdd）
     */
    public String getCurrentYmd() {
        return format(getCurrentTime(), YMD);
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
     * 年月日フォーマット(yyyyMMdd)
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

}
