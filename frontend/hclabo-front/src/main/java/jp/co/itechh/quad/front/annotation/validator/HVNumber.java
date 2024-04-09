/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.annotation.validator;

import jp.co.itechh.quad.front.validator.HNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="logicName">【数値】</span>数値チェック用バリデータのアノテーション。<br />
 * <br />
 *
 * <pre>
 * アノテーションが指定されたフィールドの値が数値として正しくない場合、messageIdに指定されたメッセージを出力する。
 * ただし、minusにtrueがセットされた場合にはminusNumberMessageIdに指定されたメッセージを出力する。
 * </pre>
 *
 * @author kimura
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = HNumberValidator.class)
public @interface HVNumber {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HNumberValidator.NOT_NUMBER_MESSAGE_ID;

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
     * マイナス入力許可フラグ
     *
     * @return マイナス入力許可フラグ
     */
    boolean minus() default HNumberValidator.DEFAULT_MINUS_SIGN;

}