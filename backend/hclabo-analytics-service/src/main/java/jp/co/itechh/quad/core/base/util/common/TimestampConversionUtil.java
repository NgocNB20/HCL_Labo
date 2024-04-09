package jp.co.itechh.quad.core.base.util.common;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

/**
 * 機能概要：日付変換機能
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class TimestampConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected TimestampConversionUtil() {
    }

    /**
     * {@link Timestamp}に変換します。
     *
     * @param o
     * @return {@link Timestamp}
     */
    public static Timestamp toTimestamp(Object o) {
        return toTimestamp(o, null);
    }

    /**
     * {@link Timestamp}に変換します。
     *
     * @param o
     * @param pattern
     * @return {@link Timestamp}
     */
    public static Timestamp toTimestamp(Object o, String pattern) {
        if (o instanceof Timestamp) {
            return (Timestamp) o;
        }
        Date date = DateConversionUtil.toDate(o, pattern);
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    /**
     * 日付パターンを返します。
     *
     * @param locale
     * @return 日付パターン
     */
    public static String getPattern(Locale locale) {
        return DateConversionUtil.getY4Pattern(locale) + " " + TimeConversionUtil.getPattern(locale);
    }
}