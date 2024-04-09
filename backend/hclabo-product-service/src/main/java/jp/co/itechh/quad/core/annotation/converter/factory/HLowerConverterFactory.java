/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.annotation.converter.factory;

import jp.co.itechh.quad.core.annotation.converter.HCLower;
import jp.co.itechh.quad.core.annotation.converter.impl.HLowerConverterImpl;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <span class="logicName">【小文字変換】</span>小文字変換コンバータのアノテーションファクトリクラス
 *
 * @author kimura
 */
public class HLowerConverterFactory implements AnnotationFormatterFactory<HCLower> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(String.class));
    }

    @Override
    public Printer<String> getPrinter(HCLower annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    @Override
    public Parser<String> getParser(HCLower annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    private Formatter<String> configureFormatterFrom(HCLower annotation) {
        return new HLowerConverterImpl();
    }
}