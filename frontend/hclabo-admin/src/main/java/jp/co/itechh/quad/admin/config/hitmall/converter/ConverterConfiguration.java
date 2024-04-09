/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.config.hitmall.converter;

import jp.co.itechh.quad.admin.annotation.converter.factory.HDateConverterFactory;
import jp.co.itechh.quad.admin.annotation.converter.factory.HHankakuConverterFactory;
import jp.co.itechh.quad.admin.annotation.converter.factory.HLowerConverterFactory;
import jp.co.itechh.quad.admin.annotation.converter.factory.HNumberConverterFactory;
import jp.co.itechh.quad.admin.annotation.converter.factory.HZenkakuConverterFactory;
import jp.co.itechh.quad.admin.annotation.converter.factory.HZenkakuKanaConverterFactory;
import jp.co.itechh.quad.admin.annotation.converter.impl.HBlankToNullConverterImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 独自コンバータConfigクラス
 *
 * @author kimura
 */
@Configuration
public class ConverterConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(String.class, new HBlankToNullConverterImpl());
        registry.addFormatterForFieldAnnotation(new HDateConverterFactory());
        registry.addFormatterForFieldAnnotation(new HHankakuConverterFactory());
        registry.addFormatterForFieldAnnotation(new HLowerConverterFactory());
        registry.addFormatterForFieldAnnotation(new HNumberConverterFactory());
        registry.addFormatterForFieldAnnotation(new HZenkakuConverterFactory());
        registry.addFormatterForFieldAnnotation(new HZenkakuKanaConverterFactory());
    }
}