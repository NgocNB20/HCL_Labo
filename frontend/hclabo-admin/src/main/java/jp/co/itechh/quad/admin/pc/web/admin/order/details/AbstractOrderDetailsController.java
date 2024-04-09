/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.addressbook.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.addressbook.presentation.api.param.ServerErrorResponse;
import jp.co.itechh.quad.admin.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.admin.base.application.HmMessages;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 受注詳細抽象コントローラー<br/>
 *
 * @author kimura
 */
public class AbstractOrderDetailsController extends AbstractController {

    /** 排他制御エラー */
    protected static final String EXCLUSIVE_CONTROL_ERR = "ORDER-ODER0001-E";

    /**
     * クライアントエラーを生成<br/>
     * ここでは生成するだけであり、呼び出し元でエラーハンドリングを実行する
     *
     * @param responseBody       レスポンスボディ
     * @param bindingResult      バインディング結果
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected static void createClientErrorMessage(String responseBody,
                                                   BindingResult bindingResult,
                                                   RedirectAttributes redirectAttributes,
                                                   Model model) {

        // アイテム名調整マップ
        Map<String, String> itemNameAdjust = new HashMap<>();

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        ClientErrorResponse clientError = conversionUtility.toObject(responseBody, ClientErrorResponse.class);
        Map<String, List<ErrorContent>> messages = clientError.getMessages();

        if (MapUtils.isNotEmpty(messages)) {
            for (Map.Entry<String, List<ErrorContent>> entry : messages.entrySet()) {

                String key = entry.getKey();
                List<ErrorContent> errorResponseList = entry.getValue();

                if (key.equals("common")) {
                    for (ErrorContent errorResponse : errorResponseList) {
                        addMessageApi(errorResponse.getCode(), errorResponse.getMessage(), redirectAttributes, model);
                    }
                    // 項目バリデーションエラーの場合
                } else {
                    for (ErrorContent errorResponse : errorResponseList) {
                        bindingResult.rejectValue(itemNameAdjust.getOrDefault(key, key),
                                                  Objects.requireNonNull(errorResponse.getCode())
                                                 );
                    }
                }
            }
        }
    }

    /**
     * サーバーエラーを生成<br/>
     * ここでは生成するだけであり、呼び出し元でエラーハンドリングを実行する
     *
     * @param responseBody       レスポンスボディ
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected static void createServerErrorMessage(String responseBody,
                                                   RedirectAttributes redirectAttributes,
                                                   Model model) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        ServerErrorResponse serverError = conversionUtility.toObject(responseBody, ServerErrorResponse.class);
        if ((serverError != null) && (MapUtils.isNotEmpty(serverError.getMessages()))) {
            for (List<ErrorContent> errorResponseList : serverError.getMessages().values()) {
                for (ErrorContent errorResponse : errorResponseList) {
                    addMessageApi(errorResponse.getCode(), errorResponse.getMessage(), redirectAttributes, model);
                }
            }
        }
    }

    /**
     * @param msgBody メッセージ内容
     * @return エラーメッセージ
     */
    public String getServerErrorMessage(String msgBody) {
        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        ServerErrorResponse serverError = conversionUtility.toObject(msgBody, ServerErrorResponse.class);
        if ((serverError != null) && (MapUtils.isNotEmpty(serverError.getMessages()))) {
            for (List<ErrorContent> errorResponseList : serverError.getMessages().values()) {
                if (CollectionUtils.isNotEmpty(errorResponseList)) {
                    return errorResponseList.get(0).getMessage();
                }
            }
        }
        return null;
    }

    /**
     * エラー情報の文字列リストを生成<br/>
     * ※請求不整合チェック用<br/>
     * ここでは生成するだけであり、呼び出し元でエラーハンドリングを実行する
     *
     * @param responseBody レスポンスボディ
     * @return errorInfo
     */
    protected static List<String> createBillCheckErrorInfo(String responseBody) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        ClientErrorResponse clientError = conversionUtility.toObject(responseBody, ClientErrorResponse.class);
        Map<String, List<ErrorContent>> messages = clientError.getMessages();

        List<String> errorInfo = new ArrayList<>();
        if (MapUtils.isNotEmpty(messages)) {
            for (Map.Entry<String, List<ErrorContent>> entry : messages.entrySet()) {
                List<ErrorContent> errorResponseList = entry.getValue();
                for (ErrorContent errorResponse : errorResponseList) {
                    errorInfo.add("[" + errorResponse.getCode() + "]" + errorResponse.getMessage());
                }
            }
        }
        return errorInfo;
    }

    /**
     * APIが返却したメッセージを追加<br/>
     * API側でのみメッセージコードが定義されており、
     * フロントエンド側がメッセージコードからメッセージを取得できない場合に、直接メッセージをセットするためのメソッド
     *
     * @param messageCode        メッセージコード
     * @param message            メッセージ
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected static void addMessageApi(String messageCode,
                                        String message,
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

        // アプリケーションログ出力Helper取得
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);

        // addMessage のログを出力する
        applicationLogUtility.writeLogicErrorLog(
                        ste.getClassName() + "#" + ste.getMethodName(), messageCode + ":" + message);

        // FlashAttributeへメッセージセット
        HmMessages allMessages = new HmMessages();

        Map<String, ?> flashattrsMap = redirectAttributes.getFlashAttributes();
        if (flashattrsMap != null && flashattrsMap.containsKey(FLASH_MESSAGES)) {
            allMessages = (HmMessages) flashattrsMap.get(FLASH_MESSAGES);
        }
        allMessages.add(new AppLevelFacesMessage(message));

        // その後の遷移先がリダイレクトであってもなくても、うまく画面表示できるよう、
        // RedirectAttributes、Model両方にメッセージを設定しておく
        redirectAttributes.addFlashAttribute(FLASH_MESSAGES, allMessages);
        model.addAttribute(FLASH_MESSAGES, allMessages);
    }

}