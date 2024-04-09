package jp.co.itechh.quad.core.base.util.seasar;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * {@link DecimalFormat}用のユーティリティクラスです。
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
public class DecimalFormatUtil {
    /**
     * インスタンスを構築します。
     */
    protected DecimalFormatUtil() {
    }

    /**
     * 数値の文字列での表記を正規化します。
     *
     * @param s
     * @return 正規化された文字列
     * @see #normalize(String, Locale)
     */
    public static String normalize(String s) {
        return normalize(s, Locale.getDefault());
    }

    /**
     * 数値の文字列での表記をグルーピングセパレータを削除し、小数点を.であらわした標準形に正規化します。
     *
     * @param s
     * @param locale
     * @return 正規化された文字列
     */
    public static String normalize(String s, Locale locale) {
        if (s == null) {
            return null;
        }
        DecimalFormatSymbols symbols = DecimalFormatSymbolsUtil.getDecimalFormatSymbols(locale);
        char decimalSep = symbols.getDecimalSeparator();
        char groupingSep = symbols.getGroupingSeparator();
        StringBuffer buf = new StringBuffer(20);
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == groupingSep) {
                continue;
            } else if (c == decimalSep) {
                c = '.';
            }
            buf.append(c);
        }
        return buf.toString();
    }
}