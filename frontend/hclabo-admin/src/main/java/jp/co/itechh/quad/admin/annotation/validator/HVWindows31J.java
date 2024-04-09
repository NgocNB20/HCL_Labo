/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.validator.HWindows31JValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="defaultValidator">★デフォルトバリデータ</span><br />
 * <span class="logicName">【Windows-31J文字列】</span>Windows-31Jのコード外の文字列チェックバリデータのアノテーション。<br/>
 *
 * @author hk57400
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = HWindows31JValidator.class)
public @interface HVWindows31J {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HWindows31JValidator.MESSAGE_ID;

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
     * デフォルトのWindows-31Jチェックではなく、JIS X 0208チェックとするフラグ
     *
     * @see HWindows31JValidator#setCheckJISX0208(boolean)
     * @return true:JIS X 0208チェック, false:Windows-31Jチェック
     */
    boolean checkJISX0208() default false;

}
