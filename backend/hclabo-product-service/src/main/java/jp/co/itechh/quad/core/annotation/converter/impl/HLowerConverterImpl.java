/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.annotation.converter.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ZenHanConversionUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * <span class="logicName">【小文字変換】</span>小文字変換コンバータのアノテーションlogic実装クラス
 *
 * @author kimura
 */
public class HLowerConverterImpl implements Formatter<String> {

    /**
     * model → view
     * ※こちらは未使用
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

        if (StringUtils.isEmpty(fromScreen)) {
            return fromScreen;
        }

        // 全角、半角の変換Helper取得
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);
        return zenHanConversionUtility.toHankaku(fromScreen).toLowerCase();
    }
}