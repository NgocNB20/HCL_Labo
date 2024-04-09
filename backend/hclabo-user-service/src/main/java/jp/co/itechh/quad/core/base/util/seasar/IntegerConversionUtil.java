package jp.co.itechh.quad.core.base.util.seasar;

import java.text.SimpleDateFormat;

/**
 * {@link Integer}用の変換ユーティリティです。
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 *
 */
public class IntegerConversionUtil {
    /**
     * インスタンスを構築します。
     */
    protected IntegerConversionUtil() {
    }

    /**
     * {@link Integer}に変換します。
     *
     * @param o
     * @return {@link Integer}
     */
    public static Integer toInteger(Object o) {
        return toInteger(o, null);
    }

    /**
     * {@link Integer}に変換します。
     *
     * @param o
     * @param pattern
     * @return {@link Integer}
     */
    public static Integer toInteger(Object o, String pattern) {
        if (o == null) {
            return null;
        } else if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Number) {
            return new Integer(((Number) o).intValue());
        } else if (o instanceof String) {
            return toInteger((String) o);
        } else if (o instanceof java.util.Date) {
            if (pattern != null) {
                return new Integer(new SimpleDateFormat(pattern).format(o));
            }
            return new Integer((int) ((java.util.Date) o).getTime());
        } else if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue() ? new Integer(1) : new Integer(0);
        } else {
            return toInteger(o.toString());
        }
    }

    private static Integer toInteger(String s) {
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        return new Integer(DecimalFormatUtil.normalize(s));
    }
}