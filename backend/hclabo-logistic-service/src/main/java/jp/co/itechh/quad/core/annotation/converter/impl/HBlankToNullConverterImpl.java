package jp.co.itechh.quad.core.annotation.converter.impl;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * グローバル的なコンバーター
 * 未入力の項目はNULLに変換するため
 * 作成日：2021/04/08
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class HBlankToNullConverterImpl implements Formatter<String> {

    /**
     * model → view
     *
     * @param fromController
     * @param locale
     * @return 変換後の値
     */
    @Override
    public String print(String fromController, Locale locale) {
        // 特に制御しない
        return fromController;
    }

    /**
     * view → model
     *
     * @param fromScreen
     * @param locale
     * @return 変換後の値
     * @throws ParseException
     */
    @Override
    public String parse(String fromScreen, Locale locale) throws ParseException {
        // 未入力の項目はNULLに変換する
        // ただし、Formatterの仕組みでデフォルトに提供されているようなため、特に実装する必要がない
        return fromScreen;
    }

}
