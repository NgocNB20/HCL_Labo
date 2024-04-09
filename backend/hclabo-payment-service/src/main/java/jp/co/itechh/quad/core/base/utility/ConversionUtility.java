/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.util.seasar.BigDecimalConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 変換ユーティリティクラス
 * <p>
 * Dxoでデータの型変換や分割・結合時に使用する。
 *
 * @author ueshima
 * @author Kaneko (itec) 2012/08/17 UtilからHelperへ変更 SanitizeUtilを統合
 */
@Component
public class ConversionUtility {

    /** 分割文字（改行コード：CR） */
    public static final String DIV_CHAR_CR = "\r";

    /** 分割文字（改行コード：LF） */
    public static final String DIV_CHAR_LF = "\n";

    /** 分割文字（改行コード：CRLF） */
    public static final String DIV_CHAR_CRLF = DIV_CHAR_CR + DIV_CHAR_LF;

    /**
     * 隠蔽コンストラクタ
     */
    public ConversionUtility() {
    }

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
     * BigDecimal変換
     *
     * @param value 変換元文字列
     * @return 変換後の値
     */
    public BigDecimal toBigDecimal(Object value) {
        return BigDecimalConversionUtil.toBigDecimal(value);
    }

    /**
     * Date⇒TimeStamp変換
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
     * @param mask  マスク文字
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
     * @param value      値
     * @param mask       マスク文字
     * @param beginIndex 開始インデックス
     * @param endIndex   終了インデックス
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

    /**
     * サイニタイジング対象文字を置き換える為のMAP
     */
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
     * 分割された文字列（リスト）を分割文字列で結合する。<br/>
     * divCharがnullの場合、デフォルトの分割文字列を使用する。
     *
     * @param targetStrList 文字列リスト
     * @param divChar       分割文字
     * @return 分割した文字配列
     */
    public String toUnitStr(List<String> targetStrList, String divChar) {
        if (CollectionUtil.getSize(targetStrList) <= 0) {
            return null;
        }
        if (StringUtil.isEmpty(divChar)) {
            divChar = DIV_CHAR_CRLF;
        }
        StringBuilder strBuilder = new StringBuilder();
        for (String targetStr : targetStrList) {
            // 既に文字列が入っていれば、分割文字を付加する
            if (strBuilder.length() > 0) {
                strBuilder.append(divChar);
            }
            strBuilder.append(targetStr);
        }
        return strBuilder.toString();
    }

}
