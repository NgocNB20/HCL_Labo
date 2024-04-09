/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.annotation.validator;

import jp.co.itechh.quad.front.validator.HMultipartFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="logicName">【ファイル】</span>MultipartFileファイル用バリデータのアノテーション。<br/>
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = HMultipartFileValidator.class)
public @interface HVMultipartFile {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HMultipartFileValidator.FILE_EMPTY_MESSAGE_ID;

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
     * ファイル拡張子
     *
     * @return ファイル拡張子
     */
    String[] fileExtension() default {};

}