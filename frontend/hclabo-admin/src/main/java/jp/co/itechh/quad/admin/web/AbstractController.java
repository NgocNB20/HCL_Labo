/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.web;

import jp.co.itechh.quad.accesskeywords.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.ServerErrorResponse;
import jp.co.itechh.quad.admin.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.admin.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.admin.base.application.FacesMessage;
import jp.co.itechh.quad.admin.base.application.HmMessages;
import jp.co.itechh.quad.admin.base.exception.AppLevelException;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.admin.base.utility.BeanUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.exception.ControllerException;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * コントローラ基底クラス
 *
 * @author kn23834
 * @Transactionalでトランザクション制御
 */
@Transactional(rollbackFor = Exception.class)
public abstract class AbstractController {

    /** エラーリスト */
    private List<AppLevelException> errorList = new ArrayList<>();

    public static final String FLASH_MESSAGES = "allMessages";

    /**
     * モデルをクリアする
     */
    public void clearModel(Class<?> clazz, Object targetModel, Model model) {
        clearModel(clazz, targetModel, null, model);
    }

    /**
     * モデルをクリアする
     *
     * @param clazz          モデルクラス
     * @param targetModel    クリア対象のモデル
     * @param excludedFields クリア除外対象フィールド
     * @param model          Model
     */
    public void clearModel(Class<?> clazz, Object targetModel, String[] excludedFields, Model model) {
        // BeanUtility#clearBean実行
        BeanUtility beanUtility = ApplicationContextUtility.getBean(BeanUtility.class);
        beanUtility.clearBean(clazz, targetModel, excludedFields);
    }

    /**
     * エラーメッセージの有無判定
     *
     * @return true..有、false..無
     */
    protected boolean hasErrorMessage() {
        return errorList != null && !errorList.isEmpty();
    }

    /**
     * 各Messages.propertiesファイルからメッセージを取得する<br/>
     * ※messageCodeにメッセージタイプを指定しない場合は、
     * F → E → W → I で存在する順番に取得する
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @return メッセージ
     */
    protected String getMessage(String messageCode, Object[] args) {
        return AppLevelFacesMessageUtil.getAllMessage(messageCode, args).getMessage();
    }

    /**
     * エラーメッセージの追加<br/>
     *
     * @param controllerException
     */
    protected void addErrorMessage(ControllerException controllerException) {
        if (errorList == null) {
            errorList = new ArrayList<>();
        }
        errorList.add(controllerException);
    }

    /**
     * エラーメッセージの追加<br/>
     * <p>
     * ControllerExceptionを作成し、errorListに追加
     *
     * @param messageCode メッセージコード
     */
    protected void addErrorMessage(String messageCode) {
        addErrorMessage(messageCode, null);
    }

    /**
     * エラーメッセージの追加<br/>
     * <p>
     * ControllerExceptionを作成し、errorListに追加
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     */
    protected void addErrorMessage(String messageCode, Object[] args) {
        addErrorMessage(messageCode, args, null, null);
    }

    /**
     * エラーメッセージの追加<br/>
     * <p>
     * ControllerExceptionを作成し、errorListに追加
     * メソッドの説明・概要<br/>
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param cause       例外
     */
    protected void addErrorMessage(String messageCode, Object[] args, Throwable cause) {
        addErrorMessage(messageCode, args, null, cause);
    }

    /**
     * エラーメッセージの追加<br/>
     * <p>
     * ControllerExceptionを作成し、errorListに追加
     * メソッドの説明・概要<br/>
     *
     * @param messageCode メッセージコード
     * @param args        メッセージ引数
     * @param componentId コンポーネントID
     * @param cause       例外
     */
    protected void addErrorMessage(String messageCode, Object[] args, String componentId, Throwable cause) {
        addErrorMessage(createControllerException(FacesMessage.SEVERITY_ERROR, messageCode, args, componentId, cause));
    }

    /**
     * エラーを発生させる(以降の処理中断)<br/>
     * 現在溜まっているメッセージからAppLevelListExceptionを作成し、<br/>
     * 例外をスローする<br/>
     * メッセージがない場合は、空のAppLevelListExceptionをスローする<br/>
     *
     * @throws AppLevelListException メッセージがある場合にスロー
     * @throws RuntimeException      メッセージがない場合にスロー
     */
    protected void throwMessage() throws AppLevelListException, RuntimeException {
        throw new AppLevelListException(errorList);
    }

    /**
     * 引数 errorCode からActionExceptionを作成し、AppLevelListExceptionにセットし、スロー<br/>
     *
     * @param errorCode エラーコード
     * @throws AppLevelListException 作成した例外をスロー
     */
    protected void throwMessage(String errorCode) throws AppLevelListException {
        throwMessage(errorCode, null);
    }

    /**
     * 引数 errorCode args からActionExceptionを作成し、AppLevelListExceptionにセットし、スロー<br/>
     *
     * @param errorCode エラーコード
     * @param args      エラーメッセージ引数
     * @throws AppLevelListException 作成した例外をスロー
     */
    protected void throwMessage(String errorCode, Object[] args) throws AppLevelListException {
        throwMessage(errorCode, args, null);
    }

    /**
     * 引数 errorCode args cause からActionExceptionを作成し、AppLevelListExceptionにセットし、スロー<br/>
     * エラーレベルは、FacesMessage.SEVERITY_ERROR<br/>
     *
     * @param errorCode エラーコード
     * @param args      エラーメッセージ引数
     * @param cause     例外
     * @throws AppLevelListException 作成した例外をスロー
     */
    protected void throwMessage(String errorCode, Object[] args, Throwable cause) throws AppLevelListException {
        throwMessage(errorCode, args, null, cause);
    }

    /**
     * 引数 errorCode args cause からActionExceptionを作成し、AppLevelListExceptionにセットし、スロー<br/>
     * エラーレベルは、FacesMessage.SEVERITY_ERROR<br/>
     *
     * @param errorCode   エラーコード
     * @param args        エラーメッセージ引数
     * @param componentId コンポーネントID
     * @param cause       例外
     * @throws AppLevelListException 作成した例外をスロー
     */
    protected void throwMessage(String errorCode, Object[] args, String componentId, Throwable cause)
                    throws AppLevelListException {
        ControllerException controllerException =
                        createControllerException(FacesMessage.SEVERITY_ERROR, errorCode, args, componentId, cause);
        addErrorMessage(controllerException);
        throwMessage();
    }

    /**
     * メッセージを追加<br/>
     *
     * @param messageCode        メッセージコード
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected void addMessage(String messageCode, RedirectAttributes redirectAttributes, Model model) {
        addMessage(messageCode, null, redirectAttributes, model);
    }

    /**
     * メッセージを追加<br/>
     * SEVERITY_ERRORのレベルでメッセージを設定<br/>
     *
     * @param messageCode        メッセージコード
     * @param args               メッセージ引数
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected void addMessage(String messageCode, Object[] args, RedirectAttributes redirectAttributes, Model model) {
        addMessage(FacesMessage.SEVERITY_ERROR, messageCode, args, redirectAttributes, model);
    }

    /**
     * 警告メッセージを追加<br/>
     * SEVERITY_WARNのレベルでメッセージを設定<br/>
     *
     * @param messageCode        メッセージコード
     * @param args               メッセージ引数
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected void addWarnMessage(String messageCode,
                                  Object[] args,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        addMessage(FacesMessage.SEVERITY_WARN, messageCode, args, redirectAttributes, model);
    }

    /**
     * 情報メッセージを追加<br/>
     * SEVERITY_INFOのレベルでメッセージを設定<br/>
     *
     * @param messageCode        メッセージコード
     * @param args               メッセージ引数
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected void addInfoMessage(String messageCode,
                                  Object[] args,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        addMessage(FacesMessage.SEVERITY_INFO, messageCode, args, redirectAttributes, model);
    }

    /**
     * メッセージを追加<br/>
     *
     * @param severity           メッセージレベル
     * @param messageCode        メッセージコード
     * @param args               メッセージ引数
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected static void addMessage(FacesMessage.Severity severity,
                                     String messageCode,
                                     Object[] args,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        //
        // コールスタックを漁って addMessage を行ったクラスを取得する
        //

        Throwable th = new Throwable();
        StackTraceElement ste = th.getStackTrace()[0];

        // AbstractController は発生元クラスとしない
        for (StackTraceElement tmp : th.getStackTrace()) {
            if (AbstractController.class.getName().equals(tmp.getClassName())) {
                continue;
            }
            ste = tmp;
            break;
        }

        // メッセージの取得
        AppLevelFacesMessage message = AppLevelFacesMessageUtil.getAllMessage(messageCode, args);

        // アプリケーションログ出力Helper取得
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);

        // addMessage のログを出力する
        applicationLogUtility.writeLogicErrorLog(
                        ste.getClassName() + "#" + ste.getMethodName(), messageCode + ":" + message.getMessage());

        // FlashAttributeへメッセージセット
        HmMessages allMessages = new HmMessages();

        Map<String, ?> flashattrsMap = redirectAttributes.getFlashAttributes();
        if (flashattrsMap != null && flashattrsMap.containsKey(FLASH_MESSAGES)) {
            allMessages = (HmMessages) flashattrsMap.get(FLASH_MESSAGES);
        } else if (model.containsAttribute(FLASH_MESSAGES)) {
            allMessages = (HmMessages) model.getAttribute(FLASH_MESSAGES);
        }

        if (allMessages == null) {
            allMessages = new HmMessages();
        }
        allMessages.add(message);

        // その後の遷移先がリダイレクトであってもなくても、うまく画面表示できるよう、
        // RedirectAttributes、Model両方にメッセージを設定しておく
        redirectAttributes.addFlashAttribute(FLASH_MESSAGES, allMessages);
        model.addAttribute(FLASH_MESSAGES, allMessages);
    }

    /**
     * サービス層からスローされたAppLevelListExceptionを
     * return "redirect:/***" で任意画面へ遷移させたい時のメッセージ設定処理<br/>
     * <pre>
     * AppLevelListExceptionのもつエラーメッセージを、
     * Model、RedirectAttributesにセットします。
     * </pre>
     *
     * @param eList              AppLevelListException
     * @param redirectAttributes RedirectAttributes
     * @param model              Model
     */
    protected void setAllMessages(AppLevelListException eList, RedirectAttributes redirectAttributes, Model model) {
        // メッセージリストの件数分ループ
        HmMessages allMessages = new HmMessages();
        List<AppLevelException> errorList = eList.getErrorList();
        for (AppLevelException e : errorList) {
            allMessages.add(e.getAppLevelFacesMessage());
        }
        // 遷移先画面がリダイレクトあり／なしにかかわらずメッセージ出力できるよう、
        // RedirectAttributes、Model両方に属性セットする
        redirectAttributes.addFlashAttribute("allMessages", allMessages);
        model.addAttribute("allMessages", allMessages);
    }

    /**
     * 引数 severity, errorCode, args, causeからControllerExceptionを作成<br/>
     * メソッドの説明・概要<br/>
     *
     * @param severity    エラーレベル
     * @param errorCode   メッセージコード
     * @param args        メッセージ引数
     * @param componentId コンポーネントID
     * @param cause       例外
     * @return ControllerExceptionオブジェクト
     */
    protected static ControllerException createControllerException(FacesMessage.Severity severity,
                                                                   String errorCode,
                                                                   Object[] args,
                                                                   String componentId,
                                                                   Throwable cause) {
        return new ControllerException(severity, errorCode, args, componentId, cause);
    }

    /**
     * エラーリストをクリアする<br/>
     */
    public void clearErrorList() {
        if (errorList != null) {
            errorList = null;
        }
    }

    /**
     * エラーリストを取得する<br/>
     *
     * @return エラーリスト
     */
    public List<AppLevelException> getErrorList() {
        return errorList;
    }

    /**
     * 共通情報の取得<br/>
     *
     * @return 共通情報
     */
    protected CommonInfo getCommonInfo() {
        return ApplicationContextUtility.getBean(CommonInfo.class);
    }

    /**
     * APIのエラーメッセージの追加
     *
     * @param message メッセージ
     */
    protected void addErrorMessageApi(String message) {
        ControllerException controllerException = createControllerException(FacesMessage.SEVERITY_ERROR, message);
        addErrorMessage(controllerException);
    }

    /**
     * APIのエラーを発生させる
     *
     * @param message メッセージ
     * @throws AppLevelListException 作成した例外をスロー
     */
    protected void throwMessageApi(String message) throws AppLevelListException {
        ControllerException controllerException = createControllerException(FacesMessage.SEVERITY_ERROR, message);
        addErrorMessage(controllerException);
        throwMessage();
    }

    /**
     * コントローラーの例外作成
     *
     * @param severity リハーサル
     * @param message  メッセージ
     * @return ControllerException
     */
    protected static ControllerException createControllerException(FacesMessage.Severity severity, String message) {
        return new ControllerException(severity, message);
    }

    /**
     * エラーを処理する
     *
     * @param httpStatus     HTTPステータ
     * @param responseBody   レスポンスボディ
     * @param bindingResult  バインディング結果
     * @param itemNameAdjust アイテム名調整
     */
    protected void handleError(HttpStatus httpStatus,
                               String responseBody,
                               BindingResult bindingResult,
                               Map<String, String> itemNameAdjust) {
        // クライアントのエラーを処理する
        if (httpStatus.is4xxClientError()) {
            handleClientError(responseBody, bindingResult, itemNameAdjust);
        }
        // サーバーのエラーを処理する
        else if (httpStatus.is5xxServerError()) {
            handleServerError(responseBody);
        }
    }

    /**
     * クライアントのエラーを処理する
     *
     * @param responseBody   レスポンスボディ
     * @param bindingResult  バインディング結果
     * @param itemNameAdjust アイテム名調整
     */
    protected void handleClientError(String responseBody,
                                     BindingResult bindingResult,
                                     Map<String, String> itemNameAdjust) {
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        boolean globalMessage = false;

        ClientErrorResponse clientError = conversionUtility.toObject(responseBody, ClientErrorResponse.class);
        Map<String, List<ErrorContent>> messages = clientError.getMessages();

        if (MapUtils.isNotEmpty(messages)) {
            for (Map.Entry<String, List<ErrorContent>> entry : messages.entrySet()) {

                String key = entry.getKey();
                List<ErrorContent> errorResponseList = entry.getValue();

                // commonの場合は、グローバルメッセージとしてスローする
                if (key.equals("common")) {
                    for (ErrorContent errorResponse : errorResponseList) {
                        addErrorMessageApi(errorResponse.getMessage());
                    }
                    globalMessage = true;
                    // 項目バリデーションエラーの場合
                } else {
                    for (ErrorContent errorResponse : errorResponseList) {
                        bindingResult.rejectValue(itemNameAdjust.getOrDefault(key, key),
                                                  Objects.requireNonNull(errorResponse.getCode())
                                                 );
                    }
                }
            }
            // グローバルメッセージがあった場合
            if (globalMessage) {
                throwMessage();
            }
        }
    }

    /**
     * サーバーのエラーを処理する
     *
     * @param responseBody レスポンスボディ
     */
    protected void handleServerError(String responseBody) {
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // サーバーエラー
        ServerErrorResponse serverError = conversionUtility.toObject(responseBody, ServerErrorResponse.class);
        if ((serverError != null) && (MapUtils.isNotEmpty(serverError.getMessages()))) {
            // 予期せぬエラー以外（アサートエラー）の場合
            // エラー内容を整理してからスローし、、FrontExceptionHandlerにてキャッチする
            for (List<ErrorContent> errorResponseList : serverError.getMessages().values()) {
                for (ErrorContent errorResponse : errorResponseList) {
                    throwMessageApi(errorResponse.getMessage());
                }
            }
        } else {
            // 予期せぬエラーの場合
            // そのままスローし、FrontExceptionHandlerにてキャッチする
            throw new RuntimeException();
        }
    }

    /**
     * commonに指定のエラーコードが含まれているか判定
     *
     * @param responseBody レスポンスボディ
     * @param errCode
     * @return
     */
    protected boolean isExistsErrCode(String responseBody, String errCode) {

        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        ClientErrorResponse clientError = conversionUtility.toObject(responseBody, ClientErrorResponse.class);
        Map<String, List<ErrorContent>> messages = clientError.getMessages();

        if (MapUtils.isNotEmpty(messages)) {
            for (Map.Entry<String, List<ErrorContent>> entry : messages.entrySet()) {

                String key = entry.getKey();
                List<ErrorContent> errorResponseList = entry.getValue();

                // commonの場合は、グローバルメッセージとしてスローする
                if (key.equals("common")) {
                    for (ErrorContent errorResponse : errorResponseList) {
                        if (errCode.equals(errorResponse.getCode())) {
                            return true;
                        }
                    }
                }
            }

        }

        return false;
    }
}