/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.annotation.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CSV出力DTOのフィールドへ付加するアノテーション
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvColumn {

    /**
     * 出力順序
     *
     * @return 出力順序
     */
    int order();

    /**
     * カラム名称
     *
     *  @return カラム名称
     */
    String columnLabel() default "";

    /**
     * 日付フォーマットコンバータ
     * （日付項目に対しては必ず指定する必要がある）
     *
     * @return 日付フォーマットコンバータ名
     */
    String dateFormat() default "";

    /**
     * 年月日時分秒フォーマットの補完
     * ・補完種別 開始日時: TYPE_START = "start"
     * ・補完種別 終了日時: TYPE_END   = "end"
     *
     * @return 年月日時分秒フォーマットの補完種別
     */
    String supplyDateType() default "";

    /**
     * 数値フォーマットコンバータ
     *
     * @return 数値フォーマットコンバータ名
     */
    String numberFormat() default "";

    /**
     * enum 型フィールドの出力形式の指定<br />
     * <pre>
     * label - Enum のラベルを出力する
     * value - DB の値を出力する
     * type  - Enum の物理名を出力する
     * </pre>
     *
     * @return enum 型フィールドの出力形式
     */
    String enumOutputType() default "";

    /**
     * ダウンロード時のみに使う項目
     * （アップロード時はバインドされない）
     *
     * @return ダウンロード時のみに使う項目であるかどうか
     */
    boolean isOnlyDownload() default false;

    /**
     * nullからblankへコンバータ
     *
     * @return コンバータかどうか
     */
    boolean isConvertToBlank() default false;

}