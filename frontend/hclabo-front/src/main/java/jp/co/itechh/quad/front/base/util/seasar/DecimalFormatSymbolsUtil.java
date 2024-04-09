package jp.co.itechh.quad.front.base.util.seasar;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

/**
 * {@link DecimalFormatSymbols}用のユーティリティクラスです。
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 *
 */
public class DecimalFormatSymbolsUtil {
    private static Map cache = MapUtil.createHashMap();

    /**
     * インスタンスを構築します。
     */
    protected DecimalFormatSymbolsUtil() {
    }

    /**
     * {@link DecimalFormatSymbols}を返します。
     *
     * @return {@link DecimalFormatSymbols}
     */
    public static DecimalFormatSymbols getDecimalFormatSymbols() {
        return getDecimalFormatSymbols(Locale.getDefault());
    }

    /**
     * {@link DecimalFormatSymbols}を返します。
     *
     * @param locale
     * @return {@link DecimalFormatSymbols}
     */
    public static DecimalFormatSymbols getDecimalFormatSymbols(Locale locale) {
        DecimalFormatSymbols symbols = (DecimalFormatSymbols) cache.get(locale);
        if (symbols == null) {
            symbols = new DecimalFormatSymbols(locale);
            cache.put(locale, symbols);
        }
        return symbols;
    }
}
