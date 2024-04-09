/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.annotation.converter.impl;

import jp.co.itechh.quad.core.annotation.converter.HCDate;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <span class="logicName">【日付変換】</span>日付コンバータのアノテーションlogic実装クラス<br/>
 *
 * @author kimura
 */
public class HDateConverterImpl implements Formatter<String> {

    /** デフォルトのフォーマットパターン */
    public static final String DEFAULT_PATTERN = "yyyy/MM/dd";

    /** フォーマットパターン */
    private String pattern = DEFAULT_PATTERN;

    /**
     * コンストラクタ
     *
     * @param annotation
     */
    public HDateConverterImpl(HCDate annotation) {
        this.pattern = annotation.pattern();
    }

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
     * ※「1999/9/9」が入力された場合 → 「1999/09/09」に変換する<bt/>
     * この処理がない場合、後続のvalidatorで、フォーマットエラー発生
     *
     *
     * @param fromScreen
     * @param locale
     * @return 変換後の値 or 未変換の値
     */
    @Override
    public String parse(String fromScreen, Locale locale) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat(this.pattern);

        // 厳密に日付チェックを行う
        formatter.setLenient(false);
        ParsePosition position = new ParsePosition(0);
        Date dateValue = formatter.parse(fromScreen, position);

        if (fromScreen.length() == position.getIndex()) {
            return formatter.format(dateValue);
        } else {
            // 解析エラーあり
            return fromScreen;
        }
    }
}