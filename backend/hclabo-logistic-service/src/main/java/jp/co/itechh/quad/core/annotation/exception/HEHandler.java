/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.annotation.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 例外制御サポートアノテーション<br/>
 * @author yt23807
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(HEHandler.List.class)
public @interface HEHandler {

    /**
     * サポート対象の例外クラス
     * @return 例外クラス
     */
    Class<? extends Throwable> exception();

    /**
     * 戻り先ビュー名
     * @return 戻り先ビュー
     */
    String returnView();

    /**
     * 使用するメッセージコード
     * ※①）任意メッセージコードを指定したい場合（常にSQLExceptionをキャッチする場合）
     * ※②）旧HIT-MALLでは、「@Hook***」系の仕組み
     * @return 使用するメッセージコード
     */
    String messageCode() default "";

    /** 同一アノテーションの重複指定 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface List {
        HEHandler[] value();
    }

}