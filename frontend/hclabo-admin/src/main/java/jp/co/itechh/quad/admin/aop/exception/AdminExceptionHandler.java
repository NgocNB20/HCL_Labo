/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.aop.exception;

import jp.co.itechh.quad.admin.base.exception.AppLevelException;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.exception.FileDownloadException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ApplicationLogUtility;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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
public class AdminExceptionHandler extends AbstractExceptionHandler {

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

    /**
     * DataIntegrityViolationExceptionハンドラメソッド
     *
     * @param request HttpServletRequest
     * @param e DataIntegrityViolationException
     * @param redirectAttributes RedirectAttributes
     * @param model Model
     * @param handlerMethod HandlerMethod
     * @return 遷移先のビュー名及びメッセージ内容
     *
     */
    @ExceptionHandler
    public String handleSqlException(HttpServletRequest request,
                                     DataIntegrityViolationException e,
                                     RedirectAttributes redirectAttributes,
                                     Model model,
                                     HandlerMethod handlerMethod) {

        // AppLevelException及びAppLevelListExceptionを生成
        String messageCode = getMessageCode(DataIntegrityViolationException.class, handlerMethod);
        AppLevelException appLevelException = new AppLevelException(messageCode);

        List<AppLevelException> exceptionList = new ArrayList<>();
        exceptionList.add(appLevelException);
        AppLevelListException eList = new AppLevelListException(exceptionList);

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
        return getReturnView(DataIntegrityViolationException.class, handlerMethod);
    }

    /**
     * FileDownloadExceptionハンドラメソッド
     *
     * @param e FileDownloadException
     * @param model Model
     * @param handlerMethod HandlerMethod
     * @return 遷移先のビュー名
     *
     */
    @ExceptionHandler
    public String handleFileDownloadException(FileDownloadException e, Model model, HandlerMethod handlerMethod) {
        // -------------------
        // モデルをセット
        // -------------------
        model.addAllAttributes(e.getModel().asMap());

        // -------------------
        // メッセージ表示画面へ遷移
        // -------------------
        return getReturnView(FileDownloadException.class, handlerMethod);
    }

    /**
     * MaxUploadSizeExceededExceptionハンドラメソッド
     *
     * @param request HttpServletRequest
     * @param e MaxUploadSizeExceededException
     * @param redirectAttributes RedirectAttributes
     * @param model Model
     * @param handlerMethod HandlerMethod
     * @return 遷移先のビュー名
     */
    @ExceptionHandler
    public String handleFileUploadException(HttpServletRequest request,
                                            MaxUploadSizeExceededException e,
                                            RedirectAttributes redirectAttributes,
                                            Model model,
                                            HandlerMethod handlerMethod) {

        List<AppLevelException> exceptionList = new ArrayList<>();
        exceptionList.add(new AppLevelException(e));
        AppLevelListException eList = new AppLevelListException(exceptionList);

        // AppLevelListExceptionハンドリングメソッドに委譲
        return handleAppLevelListException(request, eList, redirectAttributes, model, handlerMethod);
    }

    /**
     * AccessDeniedExceptionハンドラメソッド（認可失敗の場合）
     *
     * @param e AccessDeniedException
     * @param model Model
     * @return
     */
    @ExceptionHandler
    public String handleAccessDeniedException(AccessDeniedException e, Model model) {

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeExceptionLog(e);

        // -------------------
        // 無権限エラー画面へ遷移
        // -------------------
        return getPermissionDeniedErrorView();
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

    @Override
    protected String getDefaultErrorView() {
        return "error";
    }

    @Override
    protected String getDefaultMessageCode() {
        return "SQL-EXCEPTION-E";
    }

    /**
     * 無権限エラー画面の取得
     *
     * @return 無権限エラー画面
     */
    protected String getPermissionDeniedErrorView() {
        return "permissiondenied";
    }

}