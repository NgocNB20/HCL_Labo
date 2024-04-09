/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.annotation.validator;

import jp.co.itechh.quad.front.validator.HSpecialCharacterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="defaultValidator">★デフォルトバリデータ</span><br />
 * <span class="logicName">【特殊文字】</span>特殊文字チェックバリデータのアノテーション。<br/>
 *
 * @author kimura
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = HSpecialCharacterValidator.class)
public @interface HVSpecialCharacter {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HSpecialCharacterValidator.MESSAGE_ID;

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
     * 判定優先度①：制御文字以外すべてを許可するフラグ
     * <pre>
     *   このフラグを設定した場合、制御文字（NULLバイトなど）以外すべてが許可される。
     *   タブ・改行・半角記号、すべてOK。
     * </pre>
     *
     * @return true:制御文字以外すべてを許可する, false:他の設定値に委ねる
     */
    boolean allowPunctuation() default false;

    /**
     * 判定優先度②：半角記号を許可するフラグ
     * <pre>
     *   このフラグを設定した場合、半角記号が許可される。
     *   半角記号、OK。
     *   制御文字（NULLバイトなど）・タブ・改行、NG。
     *   ★デフォルトバリデータでは、こちらのフラグが適用されている。
     * </pre>
     *
     * @return true:半角記号を許可する, false:他の設定値に委ねる
     */
    boolean allowSymbol() default false;

    /**
     * 判定優先度③：許可する文字を個別指定
     * <pre>
     *   この配列を設定した場合、設定した文字が許可される。
     *   設定した文字（タブ・改行・半角記号）、OK。
     *   制御文字（NULLバイトなど）・設定しなかった文字（タブ・改行・半角記号）、NG。
     * </pre>
     *
     * @return 許可する文字
     */
    char[] allowCharacters() default {};

}
