/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.annotation.validator;

import jp.co.itechh.quad.front.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.front.validator.HBothSideSpaceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="defaultValidator">★デフォルトバリデータ</span><br />
 * <span class=
 * "logicName">【データ両端の空白】</span>データの先頭／末尾のスペースをチェックするバリデータのアノテーション。<br/>
 *
 * @author kimura
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = HBothSideSpaceValidator.class)
public @interface HVBothSideSpace {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HBothSideSpaceValidator.SPACE_DENY_MESSAGE_ID;

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
     * データの先頭にスペースを認めるかどうかを指定する
     *
     * @return データの先頭にスペースを認める:true それ以外:false
     */
    SpaceValidateMode startWith() default SpaceValidateMode.DENY_SPACE;

    /**
     * データの末尾にスペースを認めるかどうかを指定する
     *
     * @return データの末尾にスペースを認める:true それ以外:false
     */
    SpaceValidateMode endWith() default SpaceValidateMode.DENY_SPACE;
}