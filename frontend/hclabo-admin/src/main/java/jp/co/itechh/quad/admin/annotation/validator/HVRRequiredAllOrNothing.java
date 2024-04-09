/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRRequiredAllOrNothing.List;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.validator.HRequiredAllOrNothingValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 複数項目の必須チェックバリデータのアノテーション
 *
 * <pre>
 * 全て未入力、または全て入力の場合はOK
 * そうでない場合はNG
 * </pre>
 *
 * errorFieldに紐づけた項目がチェック対象<br/>
 * ※未指定の場合、fieldsの１つ目がチェック対象
 *
 * @author kimura
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Constraint(validatedBy = HRequiredAllOrNothingValidator.class)
@Repeatable(List.class)
public @interface HVRRequiredAllOrNothing {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HRequiredAllOrNothingValidator.REQUIRED_ALL_OR_NOTHING_MESSAGE_ID;

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
    String[] fields() default "";

    /**
     * エラーメッセージ表示フィールド名
     *
     * @return エラーメッセージ表示フィールド名
     */
    String errorField() default ValidatorConstants.NOTARGET;

    /** 同一アノテーションの重複指定 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = ElementType.TYPE)
    public @interface List {
        HVRRequiredAllOrNothing[] value();
    }
}