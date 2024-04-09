/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.annotation.validator;

import jp.co.itechh.quad.front.annotation.validator.HVRZipCode.List;
import jp.co.itechh.quad.front.validator.HZipCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="logicName">【郵便番号】</span>郵便番号チェック用バリデータのアノテーション。<br/>
 *
 * @author kimura
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Constraint(validatedBy = HZipCodeValidator.class)
@Repeatable(List.class)
public @interface HVRZipCode {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HZipCodeValidator.INVALID_MESSAGE_ID;

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
     * 相関チェック対象のフィールド名
     *
     * @return 相関チェック対象のフィールド名
     */
    String targetLeft() default "";

    /**
     * 相関チェック対象のフィールド名
     *
     * @return 相関チェック対象のフィールド名
     */
    String targetRight() default "";

    /** 同一アノテーションの重複指定 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = ElementType.TYPE)
    public @interface List {
        HVRZipCode[] value();
    }
}