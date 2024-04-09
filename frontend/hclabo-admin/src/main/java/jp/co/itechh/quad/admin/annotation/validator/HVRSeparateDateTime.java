/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRSeparateDateTime.List;
import jp.co.itechh.quad.admin.validator.HSeparateDateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class=
 * "logicName">【日時（日付、時刻分割）】</span>日付と時間を別々に入力する項目用のバリデータのアノテーション。<br/>
 * targetTime ＞ targetDate の順でチェック対象
 *
 * @author kimura
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Constraint(validatedBy = HSeparateDateTimeValidator.class)
@Repeatable(List.class)
public @interface HVRSeparateDateTime {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HSeparateDateTimeValidator.NOT_DATE_TIME_MESSAGE_ID;

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
     * フィールド名（日付）
     *
     * @return フィールド名（日付）
     */
    String targetDate() default "";

    /**
     * フィールド名（時間）
     *
     * @return フィールド名（時間）
     */
    String targetTime() default "";

    /**
     * 日付のフォーマットパターン
     *
     * @return 日付のフォーマットパターン
     */
    String datePattern() default HSeparateDateTimeValidator.DATE_PATTERN;

    /**
     * 時刻のフォーマットパターン
     *
     * @return 時刻のフォーマットパターン
     */
    String timePattern() default HSeparateDateTimeValidator.DATE_TIME_PATTERN;

    /** 同一アノテーションの重複指定 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = ElementType.TYPE)
    public @interface List {
        HVRSeparateDateTime[] value();
    }
}