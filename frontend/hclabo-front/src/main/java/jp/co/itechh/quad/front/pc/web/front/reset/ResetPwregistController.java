/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.reset;

import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerPasswordResetUpdateRequest;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.mailcertification.presentation.api.MailCertificationApi;
import jp.co.itechh.quad.mailcertification.presentation.api.param.ConfirmMailResponse;
import jp.co.itechh.quad.mailcertification.presentation.api.param.CustomerResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * パスワードリセット Controller<br/>
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@SessionAttributes(value = "resetPwregistModel")
@RequestMapping("/reset/")
@Controller
public class ResetPwregistController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPwregistController.class);

    /**
     * 有効期限切れ・パラメータ不正で確認メール情報が取得できなかった場合のエラー<br/>
     */
    private static final String MSGCD_CONFIRMMAIL_GET_FAIL = "APX000102";

    /**
     * 不正遷移<br/>
     */
    protected static final String MSGCD_REFERER_FAIL = "APX000301";

    /** パスワードリセット Helper */
    private final ResetPwregistHelper resetPwregistHelper;

    /** パスワードを再設定する API */
    private final MailCertificationApi mailCertificationApi;

    /**
     * ユーザAPI
     */
    private final CustomerApi customerApi;

    /**
     * コンストラクタ
     *
     * @param resetPwregistHelper
     * @param mailCertificationApi
     */
    @Autowired
    public ResetPwregistController(ResetPwregistHelper resetPwregistHelper,
                                   MailCertificationApi mailCertificationApi,
                                   CustomerApi customerApi) {
        this.resetPwregistHelper = resetPwregistHelper;
        this.mailCertificationApi = mailCertificationApi;
        this.customerApi = customerApi;
    }

    /**
     * パスワード登録画面：初期処理
     *
     * @param resetPwregistModel
     * @param redirectAttributes
     * @param model
     * @return パスワード登録画面
     */
    @GetMapping(value = "/pwregist")
    @HEHandler(exception = AppLevelListException.class, returnView = "reset/pwregist")
    public String doLoadPwregist(ResetPwregistModel resetPwregistModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        resetPwregistModel.setErrorUrl(true);

        // パラメータチェック
        if (StringUtils.isEmpty(resetPwregistModel.getMid())) {
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        try {

            // 確認メール情報取得
            ConfirmMailResponse confirmMailResponse = mailCertificationApi.getConfirmMail(resetPwregistModel.getMid());

            // 確認メール情報の取得チェック
            if (confirmMailResponse == null) {
                // パラメータ不正、または有効期限切れなどで確認メール情報の取得ができなかった場合、メッセージを表示
                resetPwregistModel.setErrorUrl(true);
                throwMessage(MSGCD_CONFIRMMAIL_GET_FAIL);
            }

            // パスワード再設定会員情報取得
            CustomerResponse customerResponse =
                            mailCertificationApi.getCustomerInfoResetPassword(resetPwregistModel.getMid());
            // ページにセット
            resetPwregistHelper.toPageForLoad(customerResponse, resetPwregistModel);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "reset/pwregist";
        }

        // 有効なURLでしたとさ
        resetPwregistModel.setErrorUrl(false);

        return "reset/pwregist";

    }

    /**
     * パスワードリセット処理<br/>
     *
     * @param resetPwregistModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return 完了画面へ
     */
    @PostMapping(value = "/pwregist", params = "doOncePassWordReset")
    @HEHandler(exception = AppLevelListException.class, returnView = "reset/pwregist")
    public String doOncePassWordReset(@Validated ResetPwregistModel resetPwregistModel,
                                      BindingResult error,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        // パラメータチェック
        if (checkInput(resetPwregistModel)) {
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        if (error.hasErrors()) {
            return "reset/pwregist";
        }

        // 会員パスワード変更サービス実行
        CustomerPasswordResetUpdateRequest passwordResetUpdateRequest = new CustomerPasswordResetUpdateRequest();
        passwordResetUpdateRequest.setConfirmMailPassword(resetPwregistModel.getMid());
        passwordResetUpdateRequest.setMemberInfoNewPassword(resetPwregistModel.getMemberInfoNewPassWord());

        try {
            customerApi.resetPassword(
                            resetPwregistModel.getMemberInfoEntity().getMemberInfoSeq(), passwordResetUpdateRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("confirmMailPassword", "mid");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "reset/pwregist";
        }

        // 完了画面へ
        return "redirect:/reset/pwcomplete";
    }

    /**
     * 画面不正チェック<br/>
     * 必要なデータの有無をチェック
     *
     * @return true=エラー、false=エラーなし
     */
    protected boolean checkInput(ResetPwregistModel resetPwregistModel) {
        if (resetPwregistModel.getMemberInfoEntity() == null) {
            return true;
        }
        if (resetPwregistModel.getMemberInfoEntity().getMemberInfoSeq() == null) {
            return true;
        }
        if (StringUtils.isEmpty(resetPwregistModel.getMemberInfoEntity().getMemberInfoPassword())) {
            return true;
        }
        return false;
    }

    /**
     * パスワード登録完了画面：画面表示処理
     *
     * @param resetPwregistModel
     * @param sessionStatus
     * @param model
     * @return パスワード登録完了画面
     */
    @GetMapping(value = "/pwcomplete")
    protected String doLoadPwcomplete(ResetPwregistModel resetPwregistModel, SessionStatus sessionStatus, Model model) {

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "reset/pwcomplete";
    }

}