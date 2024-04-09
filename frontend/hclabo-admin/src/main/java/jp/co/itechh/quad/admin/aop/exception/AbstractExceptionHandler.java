/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.aop.exception;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.admin.base.application.HmMessages;
import jp.co.itechh.quad.admin.base.exception.AppLevelException;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ApplicationLogUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 例外ハンドラクラス（フロント、管理共通）
 *
 * @author yt23807
 * @version $Revision: 1.0 $
 *
 */
public abstract class AbstractExceptionHandler {

    /**
     * デフォルトエラー遷移先ビュー名取得
     * @return デフォルトエラー遷移先ビュー名取得
     */
    protected abstract String getDefaultErrorView();

    /**
     * デフォルトメッセージコード取得
     * @return デフォルトメッセージコード取得
     */
    protected abstract String getDefaultMessageCode();

    /**
     * メッセージ設定処理<br/>
     * <pre>
     * AppLevelListExceptionのもつエラーメッセージを、
     * Model、RedirectAttributesにセットします。
     * </pre>
     * @param eList AppLevelListException
     * @param redirectAttributes RedirectAttributes
     * @param model Model
     * @param handlerMethod HandlerMethod
     */
    protected void setAllMessages(AppLevelListException eList,
                                  RedirectAttributes redirectAttributes,
                                  Model model,
                                  HandlerMethod handlerMethod) {

        // メッセージリストの件数分ループ
        HmMessages allMessages = new HmMessages();
        for (AppLevelException e : eList.getErrorList()) {
            AppLevelFacesMessage appLevelFacesMessage = e.getAppLevelFacesMessage();

            allMessages.add(appLevelFacesMessage);
        }
        // 遷移先画面がリダイレクトあり／なしにかかわらずメッセージ出力できるよう、
        // RedirectAttributes、Model両方に属性セットする
        redirectAttributes.addFlashAttribute("allMessages", allMessages);
        model.addAttribute("allMessages", allMessages);
    }

    /**
     * 画面のModelを設定します<br/>
     * <pre>
     *
     * </pre>
     * @param request HTTPサーブレットリクエスト
     * @param redirectAttributes RedirectAttributes
     * @param model Model
     * @param handlerMethod HandlerMethod
     */
    protected void setModel(HttpServletRequest request,
                            RedirectAttributes redirectAttributes,
                            Model model,
                            HandlerMethod handlerMethod) {
        // セッションキーリスト
        List<String> sessionKeyList = getSessionAttributesNameList(handlerMethod);

        // セッションキーリストの件数分ループ
        for (String sessionKey : sessionKeyList) {
            // セッションオブジェクトを取得
            HttpSession session = request.getSession(true);
            Object sessionVal = session.getAttribute(sessionKey);
            if (sessionVal != null) {
                // セッションオブジェクトが取得できれば、それをModelにセットする
                model.addAttribute(sessionKey, sessionVal);
            } else {
                // セッションオブジェクトが取得できなかった場合
                // ※画面初期表示時でのチェックエラー発生時が該当
                // 　⇒この場合、ハンドラメソッド引数を確認し、セッションキーと同じ名前の引数があれば、
                // 　　そのオブジェクトのインスタンスを生成して、Modelにセットする
                // 　　（この処理がなければ、画面初期表示時にth:object={$xxxModel}という参照時に、Exceptionが発生してしまう
                Object methodParamInitVal = createInstanceByMethodParameter(handlerMethod, sessionKey);
                if (methodParamInitVal != null) {
                    model.addAttribute(sessionKey, methodParamInitVal);
                }
            }
        }
    }

    /**
     * セッションアトリビュートのキー名リストを取得<br/>
     * @param handlerMethod ハンドラメソッド
     * @return セッションアトリビュートのキー名
     */
    protected List<String> getSessionAttributesNameList(HandlerMethod handlerMethod) {
        // セッションキーリスト
        List<String> sessionKeyList = new ArrayList<>();

        //　SessionAttributesからセッションキーを取得
        SessionAttributes sessionAttributes = getAnnotation(handlerMethod, SessionAttributes.class);
        if (sessionAttributes != null) {
            String[] sessionKeys = sessionAttributes.value();
            if (sessionKeys != null) {
                // 取得したセッションキーをリストにadd
                sessionKeyList.addAll(Arrays.asList(sessionKeys));
            }
        }
        //　SessionAttributeも念のため見ておく
        SessionAttribute sessionAttribute = getAnnotation(handlerMethod, SessionAttribute.class);
        if (sessionAttribute != null) {
            String sessionKey = sessionAttribute.value();
            if (sessionKey != null) {
                // 取得したセッションキーをリストにadd
                sessionKeyList.add(sessionKey);
            }
        }
        return sessionKeyList;
    }

    /**
     * メソッドパラメータの初期インスタンスを生成<br/>
     * @param handlerMethod HandlerMethod
     * @param sessionKey セッションキー
     * @return メソッドパラメータの初期インスタンス
     */
    protected Object createInstanceByMethodParameter(HandlerMethod handlerMethod, String sessionKey) {
        // メソッドパラメータの数分ループ
        MethodParameter[] methodParams = handlerMethod.getMethodParameters();
        try {
            if (methodParams != null) {
                for (MethodParameter methodParam : methodParams) {
                    // セッションキーと同名のハンドラメソッドパラメータが存在しているか判定
                    if (StringUtils.equals(methodParam.getParameterName(), sessionKey)) {
                        Class<?> methodParameterClass = methodParam.getParameterType();
                        return methodParameterClass.getDeclaredConstructor().newInstance();
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            // アプリケーションログを出力し、処理続行（nullを返却）
            ApplicationLogUtility appLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
            appLogUtility.writeExceptionLog(e);
        }
        return null;
    }

    /**
     * 戻り先ビュー名を取得<br/>
     * ExceptionSupportアノテーションに設定した戻り先ビュー名を取得します。<br/>
     * @param thClass ExceptionSupport引き当て用例外クラス
     * @param handlerMethod HandlerMethod
     * @return 戻り先ビュー名
     */
    protected String getReturnView(Class<? extends Throwable> thClass, HandlerMethod handlerMethod) {
        HEHandler support = getExceptionHandlerSupport(thClass, handlerMethod);
        if (support != null) {
            return support.returnView();
        }
        // デフォルトエラー画面に遷移
        return getDefaultErrorView();
    }

    /**
     * メッセージコードを取得<br/>
     * ExceptionSupportアノテーションに設定したメッセージコードを取得します。<br/>
     * @param thClass ExceptionSupport引き当て用例外クラス
     * @param handlerMethod HandlerMethod
     * @return メッセージコード
     */
    protected String getMessageCode(Class<? extends Throwable> thClass, HandlerMethod handlerMethod) {
        HEHandler support = getExceptionHandlerSupport(thClass, handlerMethod);
        if (support != null) {
            return support.messageCode();
        }
        // デフォルトメッセージコード
        return getDefaultMessageCode();
    }

    /**
     * ExceptionHandlerSupportアノテーション取得<br/>
     * @param <T> 取得したいアノテーションクラス
     * @param annotationClass 取得したいアノテーションクラス
     * @param handlerMethod ハンドラメソッド
     * @return アノテーション（設定がなければnull）
     */
    protected HEHandler getExceptionHandlerSupport(Class<? extends Throwable> thClass, HandlerMethod handlerMethod) {
        HEHandler[] annotations = getAnnotationArray(handlerMethod, HEHandler.class);
        if (annotations != null) {
            for (HEHandler annotation : annotations) {
                if (thClass.equals(annotation.exception())) {
                    return annotation;
                }
            }
        }
        return null;
    }

    /**
     * アノテーション取得<br/>
     * ※優先順：メソッドアノテーション＞クラスアノテーション
     * @param <T> 取得したいアノテーションクラス
     * @param annotationClass 取得したいアノテーションクラス
     * @param handlerMethod ハンドラメソッド
     * @return アノテーション（設定がなければnull）
     */
    protected <T extends Annotation> T getAnnotation(HandlerMethod handlerMethod, Class<T> annotationClass) {
        // メソッドアノテーションがあればそれを返却
        T annotation = getHandlerMethhodAnnotation(handlerMethod, annotationClass);
        if (annotation != null) {
            return annotation;
        }
        // クラスアノテーション返却
        return getHandlerAnnotation(handlerMethod, annotationClass);
    }

    /**
     * アノテーション配列取得<br/>
     * ※優先順：メソッドアノテーション＞クラスアノテーション
     * @param <T> 取得したいアノテーションクラス
     * @param handlerMethod ハンドラメソッド
     * @param annotationClass 取得したいアノテーションクラス
     * @return アノテーション配列（設定がなければnull）
     */
    protected <T extends Annotation> T[] getAnnotationArray(HandlerMethod handlerMethod, Class<T> annotationClass) {
        // メソッドアノテーションを取得し、あれば返却
        T[] annotations = getHandlerMethhodAnnotationArray(handlerMethod, annotationClass);
        if (annotations != null) {
            return annotations;
        }
        // クラスアノテーション返却
        return getHandlerAnnotationArray(handlerMethod, annotationClass);
    }

    /**
     * クラスアノテーション配列取得
     * @param <T> 取得したいアノテーションクラス
     * @param handlerMethod ハンドラメソッド
     * @param annotationClass 取得したいアノテーションクラス
     * @return アノテーション（設定がなければnull）
     */
    protected <T extends Annotation> T getHandlerAnnotation(HandlerMethod handlerMethod, Class<T> annotationClass) {
        Object bean = handlerMethod.getBean();
        return bean.getClass().getAnnotation(annotationClass);
    }

    /**
     * クラスアノテーション配列取得
     * @param <T> 取得したいアノテーションクラス
     * @param handlerMethod ハンドラメソッド
     * @param annotationClass 取得したいアノテーションクラス
     * @return アノテーション配列（設定がなければnull）
     */
    protected <T extends Annotation> T[] getHandlerAnnotationArray(HandlerMethod handlerMethod,
                                                                   Class<T> annotationClass) {
        Object bean = handlerMethod.getBean();
        return bean.getClass().getAnnotationsByType(annotationClass);
    }

    /**
     * メソッドアノテーション取得
     * @param <T> 取得したいアノテーションクラス
     * @param handlerMethod ハンドラメソッド
     * @param annotationClass 取得したいアノテーションクラス
     * @return アノテーション（設定がなければnull）
     */
    protected <T extends Annotation> T getHandlerMethhodAnnotation(HandlerMethod handlerMethod,
                                                                   Class<T> annotationClass) {
        return handlerMethod.getMethodAnnotation(annotationClass);
    }

    /**
     * メソッドアノテーション配列取得
     * @param <T> 取得したいアノテーションクラス
     * @param handlerMethod ハンドラメソッド
     * @param annotationClass 取得したいアノテーションクラス
     * @return アノテーション配列（設定がなければnull）
     */
    protected <T extends Annotation> T[] getHandlerMethhodAnnotationArray(HandlerMethod handlerMethod,
                                                                          Class<T> annotationClass) {
        Method method = handlerMethod.getMethod();
        return method.getAnnotationsByType(annotationClass);
    }
}