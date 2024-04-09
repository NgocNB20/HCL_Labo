package jp.co.itechh.quad.core.base.util.seasar;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 機能概要：＜修正要＞
 * 作成日：2021/02/25
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
}
