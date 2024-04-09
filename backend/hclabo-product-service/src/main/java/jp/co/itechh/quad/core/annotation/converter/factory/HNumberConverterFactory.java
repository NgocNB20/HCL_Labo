/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.annotation.converter.factory;

import jp.co.itechh.quad.core.annotation.converter.HCNumber;
import jp.co.itechh.quad.core.annotation.converter.impl.HNumberConverterImpl;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <span class="logicName">【数値フォーマット】</span>数値コンバータのアノテーションファクトリクラス
 *
 * @author kimura
 */
public class HNumberConverterFactory implements AnnotationFormatterFactory<HCNumber> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(String.class));
    }

    @Override
    public Printer<String> getPrinter(HCNumber annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    @Override
    public Parser<String> getParser(HCNumber annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    private Formatter<String> configureFormatterFrom(HCNumber annotation) {
        return new HNumberConverterImpl();
    }
}