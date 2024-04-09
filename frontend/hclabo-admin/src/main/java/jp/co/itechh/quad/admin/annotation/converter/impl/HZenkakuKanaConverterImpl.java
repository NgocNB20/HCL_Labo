/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.converter.impl;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.HiraganaKatakanaConversionUtility;
import jp.co.itechh.quad.admin.base.utility.ZenHanConversionUtility;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * <span class="logicName">【全角カナ】</span>カタカナコンバータのアノテーションlogic実装クラス<br/>
 *
 * @author kimura
 */
public class HZenkakuKanaConverterImpl implements Formatter<String> {

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
     * view → model
     *
     * @param fromScreen
     * @param locale
     * @return 変換後の値 or 未変換の値
     */
    @Override
    public String parse(String fromScreen, Locale locale) throws ParseException {
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);
        // 先に全角
        fromScreen = zenHanConversionUtility.toZenkaku(fromScreen);

        HiraganaKatakanaConversionUtility hiraganaKatakanaConversionUtility =
                        ApplicationContextUtility.getBean(HiraganaKatakanaConversionUtility.class);
        // ひらがなをカタカナに変換
        return hiraganaKatakanaConversionUtility.toKatakana(fromScreen);
    }
}