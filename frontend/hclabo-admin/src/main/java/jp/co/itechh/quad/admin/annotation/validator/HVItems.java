/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.constant.type.EnumType;
import jp.co.itechh.quad.admin.validator.HItemsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HTypeに値が含まれるかの入力チェックバリデータのアノテーション<br/>
 * 引数targetにHTypeを指定する
 *
 * @author kaneda
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = HItemsValidator.class)
public @interface HVItems {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HItemsValidator.INVALID_MESSAGE_ID;

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
     * 対象区分値Map変数名
     *
     * @return 対象区分値Map変数名
     */
    Class<? extends EnumType> target() default EnumType.class;

    /**
     * 対象区分値Array変数名
     *
     * @return 対象区分値Array変数名
     */
    String[] targetArray() default {};
}