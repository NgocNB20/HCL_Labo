/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.util.seasar.BigDecimalConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.DateConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.DoubleConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.IntegerConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringConversionUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.util.seasar.TimestampConversionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    /** 終了時刻（デフォルト値） */
    public static final String DEFAULT_END_TIME = "23:59:59";

    /** 分割文字（改行コード：CR） */
    public static final String DIV_CHAR_CR = "\r";

    /** 分割文字（改行コード：LF） */
    public static final String DIV_CHAR_LF = "\n";

    /** 分割文字（改行コード：CRLF） */
    public static final String DIV_CHAR_CRLF = DIV_CHAR_CR + DIV_CHAR_LF;

    /** 分割文字（スラッシュ：/） */
    public static final String DIV_CHAR_SLASH = "/";

    /** 分割文字（HTML改行：<br />） */
    public static final String DIV_CHAR_BR = "<br />";

    /** 分割文字（カンマ：,） */
    public static final String DIV_CHAR_COMMA = ",";

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
     * Double変換<br/>
     *
     * @param value 変換元文字列
     * @return 変換後の値
     */
    public Double toDouble(Object value) {
        return DoubleConversionUtil.toDouble(value);
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
     * 年月日・時分秒⇒TimeStamp変換　デフォルト時分秒指定<br/>
     * 年月日がnullまたは空文字でなく、時分秒がnullまたは空文字の場合<br/>
     * 時分秒にデフォルト時分秒を設定してTimeStamp変換します。
     *
     * @param ymd 年月日（yyyy/MM/dd）
     * @param hms 時分秒（HH:mm:ss）
     * @param defaultHms デフォルト時分秒（HH:mm:ss）
     * @return 変換後の値
     */
    public Timestamp toTimeStampWithDefaultHms(String ymd, String hms, String defaultHms) {
        hms = toDefaultHms(ymd, hms, defaultHms);
        if (hms == null) {
            return null;
        }
        return toTimeStamp(ymd, hms);
    }

    /**
     * 年月日・時分秒⇒時分秒変換　デフォルト時分秒指定<br/>
     * <ul>
     *  <li>時分秒がnullまたは空文字以外の場合…渡された時分秒</li>
     *  <li>年月日がnullまたは空文字の場合…null</li>
     *  <li>上記以外の場合…デフォルト時分秒</li>
     * </ul>
     *
     * @param ymd 年月日（yyyy/MM/dd）
     * @param hms 時分秒(HH:mm:dd)
     * @param defaultHms デフォルト時分秒（HH:mm:ss）
     * @return 変換後の値
     */
    public String toDefaultHms(String ymd, String hms, String defaultHms) {
        if (ymd == null || "".equals(ymd)) {
            // 年月日がnullまたは空文字の場合
            return null;
        } else if (hms == null || "".equals(hms)) {
            // 年月日がnullまたは空文字でなく、時分秒がnullまたは空文字の場合
            hms = defaultHms;
        }
        return hms;
    }

    /**
     * TimeStamp⇒年月日時分秒変換<br/>
     * <ul>nullの場合、空文字列を返す</ul>
     *
     * @param value 変換前の値
     * @return 変換後の値（yyyy/MM/dd HH:mm:ss）
     */
    public String toYmdHms(Timestamp value) {
        if (value == null) {
            return "";
        }
        return toYmd(value) + " " + toHms(value);
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
     * TimeStamp⇒時分秒変換<br/>
     *
     * @param value 変換前の値
     * @return 変換後の値（HH:mm:ss）
     */
    public String toHms(Timestamp value) {

        if (value == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setLenient(false);

        return formatter.format(value);
    }

    /**
     * TimeStamp⇒年月日（分割）変換<br/>
     *
     * @param value 年月日
     * @return 変換後の値（[0]:yyyy、[1]:MM、[2]:dd）
     */
    public String[] toYmdArray(Timestamp value) {

        String[] ymdArray = new String[3];

        if (value == null) {
            return ymdArray;
        }

        String ymd = toYmd(value);
        if (ymd != null && ymd.length() == 10) {
            ymdArray[0] = ymd.substring(0, 4);
            ymdArray[1] = ymd.substring(5, 7);
            ymdArray[2] = ymd.substring(8, 10);
        }

        return ymdArray;
    }

    /**
     *
     * 時分（HH:mm）へ開始秒（:00）を追加する<br/>
     *
     * @param hm 時分（HH:mm）
     * @return 補完後の値
     */
    public String addStartSecond(String hm) {
        if (StringUtil.isNotEmpty(hm)) {
            return hm + ":00";
        }
        return hm;
    }

    /**
     *
     * 時分（HH:mm）へ終了秒（:59）を追加する<br/>
     *
     * @param hm 時分（HH:mm）
     * @return 補完後の値
     */
    public String addEndSecond(String hm) {
        if (StringUtil.isNotEmpty(hm)) {
            return hm + ":59";
        }
        return hm;
    }

    /**
     * 郵便番号変換<br />
     *
     * @param zipcode1 郵便番号（上３桁）
     * @param zipcode2 郵便番号（下４桁）
     * @return 変換後の値（000-0000）
     */
    public String toZipCode(String zipcode1, String zipcode2) {

        if (StringUtils.isEmpty(zipcode1) && StringUtils.isEmpty(zipcode2)) {
            return null;
        }
        return zipcode1 + "-" + zipcode2;
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
     * インデクッス以前をマスク
     *
     * @param value 値
     * @param mask マスク文字
     * @param endIndex 終了インデックス
     * @return マスキングされた値
     */
    public String toMaskString(Object value, char mask, int endIndex) {
        return this.toMaskString(value, mask, endIndex, false);
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

    /**
     * 「-」じゃない場合，「％」をいれる(レポートの比率表示で利用)
     *
     * @param value 値
     * @return 変換後の値値
     */
    public String toPercent(String value) {
        if (value == null) {
            return null;
        }
        if (!"-".equals(value)) {
            return value + "％";
        }
        return value;
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
     * サニタイジングを行います<br/>
     *
     * @param string 文字列
     * @return サニタイジング後の文字列
     */
    public String sanitizing(String string) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }
        StringBuilder sbBuf = new StringBuilder();
        String partsString = null;
        String convertedString = null;

        for (int i = 0; i < string.length(); i++) {
            partsString = string.substring(i, i + 1);
            convertedString = (String) SANITIZE_MAP.get(partsString);
            if (convertedString == null) {
                convertedString = partsString;
            }
            sbBuf.append(convertedString);
        }

        return sbBuf.toString();
    }

    /**
     * ジェネリックスリストの型変換<br/>
     * 親⇒子クラスリスト<br/>
     *
     * <pre>
     * List<MemberInfoDepartEntity> convertList = convertSubList(list, new ArrayList<MemberInfoDepartEntity>());
     * </pre>
     *
     * @param baseList 元リスト
     * @param convertList 変換リスト
     * @param <T> リストの型（オブジェクト）
     * @param <T1> T を継承している型（オブジェクト）
     * @return 変換後のリスト
     */
    @SuppressWarnings("unchecked")
    public <T, T1 extends T> List<T1> convertSubList(List<T> baseList, List<T1> convertList) {

        if (baseList == null || convertList == null) {
            return null;
        }

        for (T t : baseList) {
            convertList.add((T1) t);
        }
        return convertList;
    }

    /**
     * ジェネリックスリストの型変換<br/>
     * 子クラス⇒親リスト<br/>
     *
     * <pre>
     * List<MemberInfoEntity> reverseList = convertParentList(convertList, new ArrayList<MemberInfoEntity>());
     * </pre>
     *
     * @param baseList 元リスト
     * @param convertList 変換リスト
     * @param <T> リストの型（オブジェクト）
     * @return 変換後のリスト
     */
    public <T> List<T> convertParentList(List<? extends T> baseList, List<T> convertList) {

        if (baseList == null || convertList == null) {
            return null;
        }

        for (T t : baseList) {
            convertList.add(t);
        }
        return convertList;
    }

    /**
     *
     * 入力文字列の改行コードでの分割<br/>
     * <ul>
     *  <li>空白を削除する</li>
     *  <li>2つ以上連続した改行コードを1つにまとめる</li>
     *  <li>先頭または最後尾の改行コードを削除する</li>
     * </ul>
     *
     * @param targetStr 文字列
     * @return 分割した文字配列
     */
    public String[] toDivArray(String targetStr) {
        return this.toDivArray(targetStr, LINE_SEPARATOR);
    }

    /**
     *
     * 入力文字列の分割<br/>
     * <ul>
     *  <li>空白を削除する</li>
     *  <li>2つ以上連続した分割文字を1つにまとめる</li>
     *  <li>先頭または最後尾の分割文字を削除する</li>
     * </ul>
     *
     * @param targetStr 文字列
     * @param divChar 分割文字（正規表現）
     * @return 分割した文字配列
     */
    public String[] toDivArray(String targetStr, String divChar) {
        if (targetStr == null || divChar == null) {
            return null;
        }
        // 分割文字列の統合し、配列化する
        return toSumLineSeparator(targetStr, divChar).split(divChar);

    }

    /**
     *
     * 分割文字列の統合<br/>
     * <ul>
     *  <li>空白を削除する</li>
     *  <li>2つ以上連続した分割文字を1つにまとめる</li>
     *  <li>先頭または最後尾の分割文字を削除する</li>
     * </ul>
     *
     * @param targetStr 文字列
     * @return 分割した文字配列
     */
    public String toSumLineSeparator(String targetStr) {
        return toSumLineSeparator(targetStr, LINE_SEPARATOR);
    }

    /**
     *
     * 分割文字列の統合<br/>
     * <ul>
     *  <li>空白を削除する</li>
     *  <li>2つ以上連続した分割文字を1つにまとめる</li>
     *  <li>先頭または最後尾の分割文字を削除する</li>
     * </ul>
     *
     * @param targetStr 文字列
     * @param divChar 分割文字（正規表現）
     * @return 分割した文字配列
     */
    public String toSumLineSeparator(String targetStr, String divChar) {
        if (targetStr == null || divChar == null) {
            return null;
        }
        // 空白を削除する
        targetStr = targetStr.replaceAll("[ 　\t\\x0B\f]", "");
        // 2つ以上連続した改行コードを1つにまとめる
        targetStr = targetStr.replaceAll("(" + divChar + "){2,}", "\n");
        // 先頭または最後尾の改行コードを削除する
        targetStr = targetStr.replaceAll("^[" + divChar + "]+|[" + divChar + "]$", "");

        return targetStr;
    }

    /**
     *
     * 分割された文字列（リスト）を分割文字列で結合する。<br/>
     * divCharがnullの場合、デフォルトの分割文字列を使用する。
     *
     * @param targetStrList 文字列リスト
     * @param divChar 分割文字
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

    /**
     *
     * 分割された文字列（配列）を分割文字列で結合する。<br/>
     * divCharがnullの場合、デフォルトの分割文字列を使用する。
     *
     * @param targetStrArray 文字配列
     * @param divChar 分割文字（正規表現）
     * @return 分割した文字配列
     */
    public String toUnitStr(String[] targetStrArray, String divChar) {
        if (targetStrArray == null || targetStrArray.length == 0) {
            return null;
        }
        return toUnitStr(Arrays.asList(targetStrArray), divChar);
    }

    /**
     *
     * 分割された文字列（リスト）を改行コードで結合する。<br/>
     *
     * @param targetStrList 文字リスト
     * @return 分割した文字配列
     */
    public String toUnitStr(List<String> targetStrList) {
        return toUnitStr(targetStrList, DIV_CHAR_CRLF);
    }

    /**
     *
     * 分割された文字列（配列）を改行コードで結合する。<br/>
     *
     * @param targetStrArray 文字配列
     * @return 分割した文字配列
     */
    public String toUnitStr(String[] targetStrArray) {
        return toUnitStr(targetStrArray, DIV_CHAR_CRLF);
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
     * Objectに変換<br/>
     *
     * @param typeReference
     * @param object
     * @return Objectに変換
     */
    public <T> T toObject(TypeReference<T> typeReference, Object object) {
        return new ObjectMapper().convertValue(object, typeReference);
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
            return null;
        }
    }
}