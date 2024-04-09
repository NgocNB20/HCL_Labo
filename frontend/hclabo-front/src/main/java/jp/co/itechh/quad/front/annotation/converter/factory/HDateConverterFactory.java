/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.annotation.converter.factory;

import jp.co.itechh.quad.front.annotation.converter.HCDate;
import jp.co.itechh.quad.front.annotation.converter.impl.HDateConverterImpl;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <span class="logicName">【日付変換】</span>日付コンバータのアノテーションファクトリクラス<br/>
 *
 * @author kimura
 */
public class HDateConverterFactory implements AnnotationFormatterFactory<HCDate> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(String.class));
    }

    @Override
    public Printer<String> getPrinter(HCDate annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    @Override
    public Parser<String> getParser(HCDate annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    private Formatter<String> configureFormatterFrom(HCDate annotation) {
        return new HDateConverterImpl(annotation);
    }
}