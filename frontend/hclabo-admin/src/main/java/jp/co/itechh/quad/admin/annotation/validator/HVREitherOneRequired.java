/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVREitherOneRequired.List;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.validator.HEitherOneRequiredValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="logicName">【必須】</span>いずれかの項目が入力されているかチェックするバリデータのアノテーション。<br/>
 * errorFieldに紐づけた項目がチェック対象<br/>
 * ※未指定の場合、fieldsの１つ目がチェック対象
 *
 * @author kimura
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Constraint(validatedBy = HEitherOneRequiredValidator.class)
@Repeatable(List.class)
public @interface HVREitherOneRequired {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HEitherOneRequiredValidator.REQUIRED_MESSAGE_ID;

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
        HVREitherOneRequired[] value();
    }
}