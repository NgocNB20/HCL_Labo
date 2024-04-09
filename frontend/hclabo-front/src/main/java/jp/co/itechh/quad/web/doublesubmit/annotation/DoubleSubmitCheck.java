/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.web.doublesubmit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ダブルサブミットチェック制御用アノテーション
 * <pre>
 *   Controllerクラスと、そのメソッドに付与可能。
 *
 *   何も付けない場合、POST処理については基本的にチェックされる。
 *   ※システムプロパティで、exclude-path-patterns に設定したパスは除く。
 *
 *   チェックをさせたくない場合や、
 *   HTMLのフォルダパスを超える機能間で同じトークンを利用したい場合（ネームスペースの変更）は、
 *   このアノテーションで制御が可能。
 * </pre>
 *
 * @author hk57400
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleSubmitCheck {

    /**
     * @return true:チェックしない, false:チェックする
     */
    boolean disable() default false;

    /**
     * @return ネームスペース ※デフォルトでは、.member.change のようなHTML階層構造となる 変更したい場合に指定
     */
    String nameSpace() default "";

}
