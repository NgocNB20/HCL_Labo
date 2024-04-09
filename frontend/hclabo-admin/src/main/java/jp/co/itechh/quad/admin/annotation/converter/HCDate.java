/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.converter;

import jp.co.itechh.quad.admin.annotation.converter.impl.HDateConverterImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="logicName">【日付変換】</span>日付コンバータのアノテーション<br/>
 *
 * @author kimura
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HCDate {

    /**
     * 日付書式パターン<br/>
     *
     * @return 日付書式パターン
     */
    String pattern() default HDateConverterImpl.DEFAULT_PATTERN;
}