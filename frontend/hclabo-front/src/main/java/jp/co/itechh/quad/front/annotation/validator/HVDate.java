/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.annotation.validator;

import jp.co.itechh.quad.front.validator.HDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="logicName">【日付】</span>日付チェック用バリデータのアノテーション。<br/>
 *
 * @author kimura
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = HDateValidator.class)
public @interface HVDate {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HDateValidator.NOT_DATE_MESSAGE_ID;

    /**
     * バリデーショングループ
     *
     * @return バリデーショングループ
     */

    Class<?>[] groups() default {};

    /**
     * ペイロード
     *
     * @return ペイロード
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 日付書式パターン
     *
     * @return 日付書式パターン
     */
    String pattern() default HDateValidator.DEFAULT_DATE_PATTERN;

}