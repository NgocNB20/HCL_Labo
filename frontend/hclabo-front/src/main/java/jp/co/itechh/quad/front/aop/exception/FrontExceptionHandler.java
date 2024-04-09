/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.aop.exception;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.application.HmMessages;
import jp.co.itechh.quad.front.base.exception.AppLevelException;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.exception.CertificationCodeException;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.web.doublesubmit.DoubleSubmitException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 例外ハンドラクラス
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@ControllerAdvice
public class FrontExceptionHandler extends AbstractExceptionHandler {

    @Override
    protected String getDefaultErrorView() {
        return "error";
    }

    @Override
    protected String getDefaultMessageCode() {
        return "SQL-EXCEPTION-E";
    }

    /**
     * AppLevelListExceptionハンドラメソッド
     *
     * @param request HttpServletRequest
     * @param eList AppLevelListException
     * @param redirectAttributes RedirectAttributes
     * @param model Model
     * @param handlerMethod HandlerMethod
     * @return 遷移先のビュー名
     *
     */
    @ExceptionHandler
    public String handleAppLevelListException(HttpServletRequest request,
                                              AppLevelListException eList,
                                              RedirectAttributes redirectAttributes,
                                              Model model,
                                              HandlerMethod handlerMethod) {

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeLogicErrorLog(eList);

        // -------------------
        // メッセージをセット
        // -------------------
        setAllMessages(eList, redirectAttributes, model, handlerMethod);

        // -------------------
        // モデルをセット
        // -------------------
        setModel(request, redirectAttributes, model, handlerMethod);

        // -------------------
        // メッセージ表示画面へ遷移
        // -------------------
        return getReturnView(AppLevelListException.class, handlerMethod);
    }

    /**
     * AppLevelExceptionハンドラメソッド
     *
     * @param request HttpServletRequest
     * @param e AppLevelException
     * @param redirectAttributes RedirectAttributes
     * @param model Model
     * @param handlerMethod HandlerMethod
     * @return 遷移先のビュー名
     *
     */
    @ExceptionHandler
    public String handleAppLevelException(HttpServletRequest request,
                                          AppLevelException e,
                                          RedirectAttributes redirectAttributes,
                                          Model model,
                                          HandlerMethod handlerMethod) {

        List<AppLevelException> exceptionList = new ArrayList<>();
        exceptionList.add(e);
        AppLevelListException eList = new AppLevelListException(exceptionList);

        // AppLevelListExceptionハンドリングメソッドに委譲
        return handleAppLevelListException(request, eList, redirectAttributes, model, handlerMethod);
    }

    @ExceptionHandler
    public String handleCertificationCodeException(CertificationCodeException e,
                                                   Model model,
                                                   HandlerMethod handlerMethod) {
        // -------------------
        // モデルをセット
        // -------------------
        model.addAllAttributes(e.getModel().asMap());

        // -------------------
        // メッセージ表示画面へ遷移
        // -------------------
        return getReturnView(CertificationCodeException.class, handlerMethod);
    }

    /**
     * DoubleSubmitExceptionハンドラメソッド
     *
     * @param e DoubleSubmitException
     * @param handlerMethod HandlerMethod
     * @param request HttpServletRequest
     * @param model Model
     * @param redirectAttributes RedirectAttributes
     * @return 遷移先のビュー名
     */
    @ExceptionHandler
    public String handleDoubleSubmitException(DoubleSubmitException e,
                                              HandlerMethod handlerMethod,
                                              HttpServletRequest request,
                                              Model model,
                                              RedirectAttributes redirectAttributes) {

        HEHandler support = getExceptionHandlerSupport(DoubleSubmitException.class, handlerMethod);

        // InterceptorのpreHandleでエラーが発生するため、Controllerの処理は未実行
        // Modelに表示値を詰める処理はまだ動いていない状態
        // よって、1度元画面へ自画面リダイレクトし、doLoadの処理を走らせる＆新トークンに更新する

        // -------------------
        // メッセージをセット
        //   ※リダイレクトさせるため、Modelへのメッセージ設定は不要
        // -------------------
        String messageCode;
        if (support == null || StringUtils.isBlank(support.messageCode())) {
            messageCode = "DOUBLE-SUBMIT-EXCEPTION-E";
        } else {
            messageCode = support.messageCode();
        }

        HmMessages messages = new HmMessages();
        messages.add(AppLevelFacesMessageUtil.getAllMessage(messageCode, null));
        redirectAttributes.addFlashAttribute(AbstractController.FLASH_MESSAGES, messages);

        // -------------------
        // モデルをセット
        //   ※リダイレクトさせるため、画面保持情報の引継ぎは不要
        // -------------------
        // setModel(request, redirectAttributes, model, handlerMethod);

        // -------------------
        // メッセージ表示画面へ遷移
        //   ※ダブルサブミットは @HEHandler が設定されていない場合、エラー画面ではなく自画面リダイレクト
        //   ※リライトにしない理由は、上のL.150付近のコメントを参照
        // -------------------
        return "redirect:" + getRedirectViewOrUri(support, request);
    }

    /**
     * 戻り先のViewマッピングパス、もしくは、URIを生成
     *
     * @param support 例外制御サポートアノテーション
     * @param request HttpServletRequest
     * @return Viewマッピングパス、もしくは、URI
     */
    private String getRedirectViewOrUri(HEHandler support, HttpServletRequest request) {

        // アノテーションが付与されている場合は、指定したView
        // ※redirectなので、Viewの形ではなくURLパスの形で指定する ex) ○:/cart/ ×:cart/index
        if (support != null) {
            return support.returnView();
        }

        // リファラがあればそこへ戻す
        // 基本的には、リファラが入っているはず・・・
        String referer = request.getHeader("referer");
        if (StringUtils.isNotBlank(referer)) {
            // リファラは、ブラウザに表示されていたURIなのでホストやポート名の置換は不要
            // クエリストリングも付いているので、無加工で利用（CsRfチェックのAccessDeniedControllerと同じ）
            return referer;
        }

        // TODO 本来は下記のように、元の画面へ戻すべきである
        //   が、サーバ設定の運用方法や片肺運転時の特殊対応などのこともあり、ややこしくなるため回避している
        //   リファラーがない = scriptなどによる特殊アクセスのはずのため、現状エラー画面固定としている
        return "/" + getDefaultErrorView();

        /*
        // リファラがない場合はPOSTされたフォームのactionのURIへ戻す
        // 結合・本番環境では、バーチャルホスト・ポート変換が必要となる
        // 利用するには、X-Forwarded-Proto, X-Forwarded-Host, X-Forwarded-Port の設定＆試験が必要
        String uri = ServletUriComponentsBuilder.fromRequestUri(request).toUriString();
        String queryString = request.getQueryString();
        if (StringUtils.isBlank(queryString)) {
            return uri;
        } else {
            return uri + '?' + queryString;
        }
        */
    }

    /**
     * 例外ハンドラメソッド
     * @param th その他例外
     * @param model Model
     * @return 遷移先のビュー名
     *
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleThrowable(Throwable th, Model model) {

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeExceptionLog(th);

        // -------------------
        // エラー画面へ遷移
        // -------------------
        return getDefaultErrorView();
    }

}
