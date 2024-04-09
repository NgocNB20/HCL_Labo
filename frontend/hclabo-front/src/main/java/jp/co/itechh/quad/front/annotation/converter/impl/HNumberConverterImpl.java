/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.annotation.converter.impl;

import lombok.Data;
import org.springframework.format.Formatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * <span class="logicName">【数値フォーマット】</span>数値コンバータのアノテーションlogic実装クラス<br/>
 *
 * @author kimura
 */
@Data
public class HNumberConverterImpl implements Formatter<String> {

    /**
     * model → view<br/>
     * ※こちらはthymeleaf側で対応しているため未使用
     *
     * @param fromController
     * @param locale
     * @return fromController
     */
    @Override
    public String print(String fromController, Locale locale) {
        return fromController;
    }

    /**
     * view → model<br/>
     * Number型に変換し、Stringで返却<br/>
     * 「01」 → 「1」への変換用フォーマッターのためフォーマットは未指定
     *
     * @param fromScreen
     * @param locale
     * @return 変換後の値 or 未変換の値
     */
    @Override
    public String parse(String fromScreen, Locale locale) throws ParseException {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        DecimalFormat formatter = new DecimalFormat("", symbols);

        ParsePosition position = new ParsePosition(0);
        Number numberValue = formatter.parse(fromScreen, position);

        if (fromScreen.length() == position.getIndex()) {
            return numberValue.toString();
        } else {
            // 解析エラーあり
            return fromScreen;
        }
    }
}