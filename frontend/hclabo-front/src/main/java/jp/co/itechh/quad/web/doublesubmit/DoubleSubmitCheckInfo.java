/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.web.doublesubmit;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.config.doublesubmit.DoubleSubmitCheckProperties;
import jp.co.itechh.quad.web.doublesubmit.annotation.DoubleSubmitCheck;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ダブルサブミットチェック制御情報クラス
 *
 * @author hk57400
 */
@Value
public class DoubleSubmitCheckInfo {

    /** デフォルトネームスペース */
    private static final String NAME_SPACE_DEFAULT = ".";

    /** true:チェックしない, false:チェックする */
    boolean disable;

    /** ネームスペース */
    String nameSpace;

    /**
     * コンストラクタ
     *
     * @param handlerMethod 実行メソッド
     */
    public DoubleSubmitCheckInfo(HandlerMethod handlerMethod) {

        Class<?> controllerClazz = handlerMethod.getBeanType();

        // RestControllerはチェックしない
        if (AnnotationUtils.findAnnotation(controllerClazz, RestController.class) != null) {
            this.disable = true;
            this.nameSpace = NAME_SPACE_DEFAULT;
            return;
        }

        DoubleSubmitCheck methodAnnotation = handlerMethod.getMethodAnnotation(DoubleSubmitCheck.class);
        DoubleSubmitCheck classAnnotation = AnnotationUtils.findAnnotation(controllerClazz, DoubleSubmitCheck.class);

        this.disable = isDisable(methodAnnotation, classAnnotation);
        this.nameSpace = getNameSpace(methodAnnotation, classAnnotation, controllerClazz);
    }

    /**
     * 無効フラグの判定
     *
     * @param methodAnnotation メソッドに付与されたアノテーション
     * @param classAnnotation Controllerクラスに付与されたアノテーション
     * @return true:チェックしない, false:チェックする
     */
    private boolean isDisable(DoubleSubmitCheck methodAnnotation, DoubleSubmitCheck classAnnotation) {

        // いずれかのアノテーションで無効が指定されている場合は、チェックから外す
        if (methodAnnotation != null && methodAnnotation.disable()) {
            return true;
        }
        return classAnnotation != null && classAnnotation.disable();
    }

    /**
     * ネームスペースの判定
     *
     * @param methodAnnotation メソッドに付与されたアノテーション
     * @param classAnnotation Controllerクラスに付与されたアノテーション
     * @param controllerClazz Controllerクラス
     * @return ネームスペース
     */
    private String getNameSpace(DoubleSubmitCheck methodAnnotation,
                                DoubleSubmitCheck classAnnotation,
                                Class<?> controllerClazz) {

        // メソッドで上書き指定がある場合は、そちらを利用
        if (methodAnnotation != null) {
            String nameSpace = methodAnnotation.nameSpace();
            if (StringUtils.isNotBlank(nameSpace)) {
                return nameSpace;
            }
        }

        // メソッドになく、クラスで上書き指定がある場合は、そちらを利用
        if (classAnnotation != null) {
            String nameSpace = classAnnotation.nameSpace();
            if (StringUtils.isNotBlank(nameSpace)) {
                return nameSpace;
            }
        }

        // デフォルトは、".member.change"のようなHTML階層構造
        // jp.co.itechh.quad.front.pc.web.front.IndexController ⇒ ""
        // jp.co.itechh.quad.front.pc.web.front.member.change.MemberChangeController ⇒ ".member.change"
        DoubleSubmitCheckProperties prop = ApplicationContextUtility.getBean(DoubleSubmitCheckProperties.class);
        String controllerName = controllerClazz.getName();
        Pattern p = Pattern.compile(prop.getNameSpacePattern());
        Matcher m = p.matcher(controllerName);
        if (m.find()) {
            String nameSpace = m.group(1);
            return nameSpace.length() == 0 ? NAME_SPACE_DEFAULT : nameSpace;
        }

        // ここまで来るのは、AjaxやFW標準のエラー画面Controller
        // ネームスペース管理する必要がない
        return NAME_SPACE_DEFAULT;
    }

}
