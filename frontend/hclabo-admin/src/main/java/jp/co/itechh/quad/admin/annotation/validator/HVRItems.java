/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRItems.List;
import jp.co.itechh.quad.admin.validator.HItemsRValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * HTypeに値が含まれるかの入力チェックバリデータのアノテーション<br/>
 * targetに紐づけた項目がチェック対象<br/>
 * ※targetには区分値のkeyが対象。String[]が格納されたフィールドを指定可能<br/>
 * ※comparisonに区分値のMaPが対象。Map<String, String>が格納されたフィールドを指定可能
 *
 * @author kimura
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Constraint(validatedBy = HItemsRValidator.class)
@Repeatable(List.class)
public @interface HVRItems {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HItemsRValidator.INVALID_MESSAGE_ID;

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

    /** 同一アノテーションの重複指定 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = ElementType.TYPE)
    public @interface List {
        HVRItems[] value();
    }
}