/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.annotation.converter.impl;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ZenHanConversionUtility;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * <span class="logicName">【半角変換】</span>半角コンバータのアノテーションlogic実装クラス<br/>
 *
 * @author kimura
 */
public class HHankakuConverterImpl implements Formatter<String> {

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
        return zenHanConversionUtility.toHankaku(fromScreen);
    }
}