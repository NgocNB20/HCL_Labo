/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.validator.HVRequiredDiscountTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = HVRequiredDiscountTypeValidator.class)
public @interface HVRequiredDiscountType {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default "";

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
     * 割引率
     */
    String discountRate() default "";

    /**
     * 割引金額
     */
    String discountPrice() default "";

    /**
     * 割引種別
     */
    String discountType() default "";

}
