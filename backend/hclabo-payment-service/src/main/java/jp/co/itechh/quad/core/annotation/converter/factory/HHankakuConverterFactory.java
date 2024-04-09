/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.annotation.converter.factory;

import jp.co.itechh.quad.core.annotation.converter.HCHankaku;
import jp.co.itechh.quad.core.annotation.converter.impl.HHankakuConverterImpl;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <span class="logicName">【半角変換】</span>半角コンバータのアノテーションファクトリクラス
 *
 * @author kimura
 */
public class HHankakuConverterFactory implements AnnotationFormatterFactory<HCHankaku> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(String.class));
    }

    @Override
    public Printer<String> getPrinter(HCHankaku annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    @Override
    public Parser<String> getParser(HCHankaku annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    private Formatter<String> configureFormatterFrom(HCHankaku annotation) {
        return new HHankakuConverterImpl();
    }
}
