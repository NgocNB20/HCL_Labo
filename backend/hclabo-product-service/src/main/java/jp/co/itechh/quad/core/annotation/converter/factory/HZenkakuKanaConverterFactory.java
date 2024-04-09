/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.annotation.converter.factory;

import jp.co.itechh.quad.core.annotation.converter.HCZenkakuKana;
import jp.co.itechh.quad.core.annotation.converter.impl.HZenkakuKanaConverterImpl;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <span class="logicName">【全角カナ】</span>カタカナコンバータのアノテーションファクトリクラス
 *
 * @author kimura
 */
public class HZenkakuKanaConverterFactory implements AnnotationFormatterFactory<HCZenkakuKana> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(String.class));
    }

    @Override
    public Printer<String> getPrinter(HCZenkakuKana annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    @Override
    public Parser<String> getParser(HCZenkakuKana annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation);
    }

    private Formatter<String> configureFormatterFrom(HCZenkakuKana annotation) {
        return new HZenkakuKanaConverterImpl();
    }
}