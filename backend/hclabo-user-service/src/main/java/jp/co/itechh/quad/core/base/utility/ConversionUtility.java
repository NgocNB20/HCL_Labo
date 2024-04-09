/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.core.base.util.seasar.BigDecimalConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.TimestampConversionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionUtility.class);

    /** 隠蔽コンストラクタ  */
    public ConversionUtility() {
    }

    /** 分割文字（改行コード：CR） */
    public static final String DIV_CHAR_CR = "\r";

    /** 分割文字（改行コード：LF） */
    public static final String DIV_CHAR_LF = "\n";

    /** 分割文字（改行コード：CRLF） */
    public static final String DIV_CHAR_CRLF = DIV_CHAR_CR + DIV_CHAR_LF;

    /**
     * 複数入力項目分割文字（改行コード）<br/>
     * <code>DIV_CHAR</code>
     */
    public static final String LINE_SEPARATOR = DIV_CHAR_CRLF + "|" + DIV_CHAR_CR + "|" + DIV_CHAR_LF;

    /**
     * String変換<br/>
     *
     * @param value 変換元の値
     * @return 変換後の値
     */
    public String toString(Object value) {
        return StringConversionUtil.toString(value);
    }

    /**
     * Integer変換<br/>
     *
     * @param value 変換元文字列
     * @return 変換後の値
     */
    public Integer toInteger(Object value) {
        return IntegerConversionUtil.toInteger(value);
    }

    /**
     * BigDecimal変換<br/>
     *
     * @param value 変換元文字列
     * @return 変換後の値
     */
    public BigDecimal toBigDecimal(Object value) {
        return BigDecimalConversionUtil.toBigDecimal(value);
    }

    /**
     * 年月日⇒TimeStamp変換<br/>
     *
     * @param ymd 年月日（yyyy/MM/dd）
     * @return 変換後の値
     */
    public Timestamp toTimeStamp(String ymd) {
        return TimestampConversionUtil.toTimestamp(ymd, "yyyy/MM/dd");
    }

    /**
     * 年月日（分割）⇒TimeStamp変換<br/>
     *
     * @param year 年（yyyy）
     * @param month 月（MM）
     * @param day 日（dd）
     * @return 変換後の値
     */
    public Timestamp toTimeStamp(String year, String month, String day) {
        return toTimeStamp(year + "/" + month + "/" + day);
    }

    /**
     * 年月日・時分秒⇒TimeStamp変換<br/>
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
     * TimeStamp⇒年月日変換<br/>
     *
     * @param value 変換前の値
     * @return 変換後の値（yyyy/MM/dd）
     */
    public String toYmd(Timestamp value) {

        if (value == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        formatter.setLenient(false);

        return formatter.format(value);
    }

    /**
     * 郵便番号（分割）変換<br />
     *
     * @param zipcode 郵便番号（000-0000）
     * @return 変換後の値（[0]:000、[1]:0000）
     */
    public String[] toZipCodeArray(String zipcode) {

        String[] zipCodeArray = new String[2];
        if (zipcode != null && zipcode.length() == 7) {
            zipCodeArray[0] = zipcode.substring(0, 3);
            zipCodeArray[1] = zipcode.substring(3, 7);
        }

        return zipCodeArray;
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
     * インデックス以前か、以降をマスク
     *
     * @param value 値.
     * @param mask マスク文字
     * @param index インデックス
     * @param later true..以降 / false..以前
     * @return マスキングされた値
     */
    public String toMaskString(Object value, char mask, int index, boolean later) {
        if (value == null) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        String valueStr = this.toString(value);
        String toValue = valueStr.substring(0, index);
        String fromValue = valueStr.substring(index);
        if (later) {
            buffer.append(toValue);
            for (int i = 0; i < fromValue.length(); i++) {
                buffer.append(mask);
            }

        } else {
            for (int i = 0; i < toValue.length(); i++) {
                buffer.append(mask);
            }
            buffer.append(fromValue);
        }

        return buffer.toString();
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

    /** 以下、SanitizeUtilより統合 */

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

    /**
     * Stringレスポンスボディからオブジェクトクラスに変換
     *
     * @param body  レスポンスボディ
     * @param clazz クラス
     * @return <T>
     */
    public <T> T toObject(String body, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(body, clazz);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /**
     * To string json string.
     *
     * @param object the object
     * @return the string
     */
    public String toStringJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
