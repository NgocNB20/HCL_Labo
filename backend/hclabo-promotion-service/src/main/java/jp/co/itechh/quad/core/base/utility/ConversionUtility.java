/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.base.util.seasar.DateConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.TimestampConversionUtil;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 変換ユーティリティクラス
 * <P>
 * Dxoでデータの型変換や分割・結合時に使用する。
 *
 * @author ueshima
 * @author Kaneko (itec) 2012/08/17 UtilからHelperへ変更 SanitizeUtilを統合
 *
 */
@Component
public class ConversionUtility {

    /** 隠蔽コンストラクタ  */
    public ConversionUtility() {
    }

    /** 開始時刻（デフォルト値） */
    public static final String DEFAULT_START_TIME = "00:00:00";

    /**
     * String変換
     *
     * @param value 変換元の値
     * @return 変換後の値
     */
    public String toString(Object value) {
        return StringConversionUtil.toString(value);
    }

    /**
     * Integer変換
     *
     * @param value 変換元文字列
     * @return 変換後の値
     */
    public Integer toInteger(Object value) {
        return IntegerConversionUtil.toInteger(value);
    }

    /**
     * 年月日・時分秒⇒TimeStamp変換
     *
     * @param ymd 年月日（yyyy/MM/dd）
     * @param hms 時分秒（HH:mm:ss）
     * @return 変換後の値
     */
    public Timestamp toTimeStamp(String ymd, String hms) {
        return TimestampConversionUtil.toTimestamp(ymd + " " + hms, "yyyy/MM/dd HH:mm:ss");
    }

    /**
     * Date⇒TimeStamp変換<br/>
     *
     * @param date 日時
     * @return Timestamp
     */
    public Timestamp toTimestamp(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    /**
     * 年月日⇒Date変換<br/>
     *
     * @param ymd 年月日（yyyy/MM/dd）
     * @return 変換後の値
     */
    public Date toDate(String ymd) {
        return DateConversionUtil.toDate(ymd, "yyyy/MM/dd");
    }

    /**
     * TimeStamp⇒Date変換<br/>
     *
     * @param timestamp 日時
     * @return Date
     */
    public Date toDate(Timestamp timestamp) {
        if (timestamp != null) {
            return new Date(timestamp.getTime());
        }
        return null;
    }

    /**
     * 全マスク
     *
     * @param value 値
     * @param mask マスク文字
     * @return マスキングされた値
     */
    public String toMaskString(Object value, char mask) {
        if (value == null) {
            return null;
        }
        int length = toString(value).length();
        return toMaskString(value, mask, 0, length);
    }

    /**
     * 部分マスク
     * <pre>
     * 開始インデックスの文字はマスクする
     * 終了インデックスの文字はマスクしない
     * </pre>
     *
     * @param value 値
     * @param mask マスク文字
     * @param beginIndex 開始インデックス
     * @param endIndex 終了インデックス
     * @return マスキングされた値
     */
    public String toMaskString(Object value, char mask, int beginIndex, int endIndex) {
        if (value == null) {
            return null;
        }
        String pageValueStr = toString(value);
        StringBuilder result = new StringBuilder(pageValueStr.length());
        for (int i = 0; i < pageValueStr.length(); i++) {
            if (i >= beginIndex && i < endIndex) {
                result.append(mask);
            } else {
                result.append(pageValueStr.charAt(i));
            }
        }
        return result.toString();
    }

    /** サイニタイジング対象文字を置き換える為のMAP */
    protected static final Map<Object, Object> SANITIZE_MAP = new HashMap<>();

    static {
        synchronized (SANITIZE_MAP) {
            if (SANITIZE_MAP.isEmpty()) {
                SANITIZE_MAP.put("&", "&amp;");
                SANITIZE_MAP.put("<", "&lt;");
                SANITIZE_MAP.put(">", "&gt;");
                SANITIZE_MAP.put("\"", "&quot;");
                SANITIZE_MAP.put("'", "&#39;");
            }
        }
    }

}