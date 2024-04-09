package jp.co.itechh.quad.core.base.util.seasar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * {@link String}用の変換ユーティリティです。
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
public class StringConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected StringConversionUtil() {
    }

    /**
     * 文字列に変換します。
     *
     * @param value 値
     * @return 変換された結果
     */
    public static String toString(Object value) {
        return toString(value, null);
    }

    /**
     * 文字列に変換します。
     *
     * @param value   値
     * @param pattern パターン
     * @return 変換された結果
     */
    public static String toString(Object value, String pattern) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else if (value instanceof java.util.Date) {
            return toString((java.util.Date) value, pattern);
        } else if (value instanceof Number) {
            return toString((Number) value, pattern);
        } else if (value instanceof byte[]) {
            return Base64Util.encode((byte[]) value);
        } else {
            return value.toString();
        }
    }

    /**
     * 文字列に変換します。
     *
     * @param value   値
     * @param pattern パターン
     * @return 変換された結果
     */
    public static String toString(Number value, String pattern) {
        if (value != null) {
            if (pattern != null) {
                return new DecimalFormat(pattern).format(value);
            }
            return value.toString();
        }
        return null;
    }

    /**
     * 文字列に変換します。
     *
     * @param value   値
     * @param pattern パターン
     * @return 変換された結果
     */
    public static String toString(java.util.Date value, String pattern) {
        if (value != null) {
            if (pattern != null) {
                return new SimpleDateFormat(pattern).format(value);
            }
            return value.toString();
        }
        return null;
    }
}
