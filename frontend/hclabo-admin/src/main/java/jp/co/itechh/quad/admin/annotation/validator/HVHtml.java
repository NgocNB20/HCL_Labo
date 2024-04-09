/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.validator.HHtmlValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="logicName">【HTMLバリデータ】</span>HTMLドキュメントバリデータのアノテーション。<br/>
 *
 * @author kimura
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = HHtmlValidator.class)
public @interface HVHtml {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default "";

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
     * XHTMLとしての検証をするか。
     *
     * <pre>
     * XHTML として検証した場合の動作
     * ・開始タグだけの記述は許可しない。
     * ・HTMLのタグは大文字を許可しない。
     * </pre>
     *
     * デフォルト値は「XHTMLでない」。
     *
     * @return XHTMLとしての検証をする:true しない:false
     */
    boolean xhtml() default false;

    /**
     * HTML組込みドキュメントの検証かどうか。
     *
     * <pre>
     * 組み込みドキュメントとして検証した場合の動作
     *  ・&lt;html&gt; の記述を許可しない
     *  ・&lt;head&gt; &lt;meta&gt; &lt;title&gt; &lt;link&gt; タグの記述を許可しない
     *  ・&lt;body&gt; の記述を許可しない
     * </pre>
     *
     * デフォルト値は「組込みでない」。
     *
     * @return HTML組込みドキュメントの検証する:true しない:false
     */
    boolean innerHtml() default false;

    /**
     * ユニークなタグの検証を行うかどうか
     *
     * @return ユニークなタグの検証を行う:true 行わない:false
     */
    boolean uniqueTagCheck() default true;

    /**
     * 排他タグの検証を行うかどうか
     *
     * @return 排他タグの検証を行う:true 行わない:false
     */
    boolean exclusiveTagCheck() default true;

    /**
     * タグの依存関係の検証を行うかどうか
     *
     * @return タグの依存関係の検証を行う:true 行わない:false
     */
    boolean dependencyCheck() default true;

    /**
     * HTMLのタグ以外のタグを禁止するか
     *
     * <pre>
     * true   : 禁止する
     * false  : 禁止しない
     * </pre>
     *
     * デフォルト値は「禁止する」。
     *
     * @return 禁止する:true 禁止しない:false
     */
    boolean rejectUndefinedTag() default true;

    /**
     * &lt;form&gt; の使用を許可するかどうか。<br />
     * デフォルトは「許可する」。
     *
     * @return &lt;form&gt; の使用を許可する:true 許可しない:false
     */
    boolean permitFormTag() default true;

    /**
     * &lt;script&gt; の使用を許可するかどうか。<br />
     * デフォルトは「許可する」。
     *
     * @return &lt;script&gt; の使用を許可する:true 許可しない:false
     */
    boolean permitScriptTag() default true;
}