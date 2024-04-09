/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRSeparateDate.List;
import jp.co.itechh.quad.admin.validator.HSeparateDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class=
 * "logicName">【日付（年月日分割）】</span>年、月、日の入力フィールドが別々の日付用バリデータのアノテーション。<br />
 * targetDate ＞ targetMonth ＞ targetYear の順でチェック対象
 *
 * @author kimura
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Constraint(validatedBy = HSeparateDateValidator.class)
@Repeatable(List.class)
public @interface HVRSeparateDate {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HSeparateDateValidator.NOT_DATE_MESSAGE_ID;

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
     * フィールド名（年）
     *
     * @return フィールド名（年）
     */
    String targetYear() default "";

    /**
     * フィールド名（月）
     *
     * @return フィールド名（月）
     */
    String targetMonth() default "";

    /**
     * フィールド名（日）
     *
     * @return フィールド名（日）
     */
    String targetDate() default "";

    /** 同一アノテーションの重複指定 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = ElementType.TYPE)
    public @interface List {
        HVRSeparateDate[] value();
    }
}