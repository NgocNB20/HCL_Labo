/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRDateRange.List;
import jp.co.itechh.quad.admin.validator.HDateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HVRDateRange
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Constraint(validatedBy = HDateRangeValidator.class)
@Repeatable(List.class)
public @interface HVRDateRange {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HDateRangeValidator.RANGE_INVALID_MESSAGE_ID;

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
     * 相関チェック対象のフィールド名<br/>
     * エラーメッセージを表示する項目
     *
     * @return 相関チェック対象のフィールド名
     */
    String target() default "";

    /**
     * 相関チェック比較対象のフィールド名
     *
     * @return 相関チェック比較対象のフィールド名
     */
    String comparison() default "";

    /**
     * 日付書式パターン
     *
     * @return 日付書式パターン
     */
    String pattern() default "yyyy/MM/dd";

    /**
     * range
     * */
    int range() default 31;

    /**
     *  0:月別, 1:日別
     * */
    String unit() default "";

    /** 同一アノテーションの重複指定 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = ElementType.TYPE)
    public @interface List {
        HVRDateRange[] value();
    }
}