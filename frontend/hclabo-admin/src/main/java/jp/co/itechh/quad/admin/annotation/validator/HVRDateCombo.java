/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVRDateCombo.List;
import jp.co.itechh.quad.admin.validator.HDateComboValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="logicName">【日時比較（対象項目 ≦ 比較項目）】</span>日付チェック用バリデータのアノテーション。<br/>
 * time ＞ date , right > leftの順でチェック対象
 *
 * @author kimura
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Constraint(validatedBy = HDateComboValidator.class)
@Repeatable(List.class)
public @interface HVRDateCombo {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HDateComboValidator.UNPARSABLE_MESSAGE_ID;

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
     * 比較用左辺年月日フォーマット
     *
     * @return 比較用左辺年月日フォーマット
     */
    String dateLeftFormat() default "yyyy/MM/dd";

    /**
     * 比較用左辺時分秒フォーマット
     *
     * @return 比較用左辺時分秒フォーマット
     */
    String timeLeftFormat() default "HH:mm:ss";

    /**
     * 比較用右辺年月日フォーマット
     *
     * @return 比較用右辺年月日フォーマット名
     */
    String dateRightFormat() default "yyyy/MM/dd";

    /**
     * 比較用右辺時分秒フォーマット
     *
     * @return 比較用右辺時分秒フォーマット
     */
    String timeRightFormat() default "HH:mm:ss";

    /**
     * 演算子<br />
     *
     * From ～ To 形式以外の検証を行う場合は値を指定する。
     *
     * @return 演算子
     */
    String operator() default "<=";

    /**
     * 日付入力がない時刻入力を受付けるかどうか<br />
     * true を指定すると、日付が空の場合本日日付が暗黙的に使用される。
     *
     * @return 日付入力がない時刻入力を受付ける:true それ以外:false
     */
    boolean acceptOrphanedTime() default false;

    /**
     * デフォルト値（省略可能）：比較用左辺年月日値<br />
     *
     * @return 比較用左辺年月日値
     */
    String dateLeftTarget() default "";

    /**
     * デフォルト値（省略可能）：比較用左辺時分秒値<br />
     *
     * @return 比較用左辺時分秒値
     */
    String timeLeftTarget() default "";

    /**
     * デフォルト値（省略可能）：比較用右辺年月日値<br />
     *
     * @return 比較用右辺年月日値
     */
    String dateRightTarget() default "";

    /**
     * デフォルト値（省略可能）：比較用右辺時分秒値<br />
     *
     * @return 比較用右辺時分秒値
     */
    String timeRightTarget() default "";

    /** 同一アノテーションの重複指定 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = ElementType.TYPE)
    public @interface List {
        HVRDateCombo[] value();
    }
}