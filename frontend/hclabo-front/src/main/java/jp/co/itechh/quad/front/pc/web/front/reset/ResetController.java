/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.reset;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.mailcertification.presentation.api.MailCertificationApi;
import jp.co.itechh.quad.mailcertification.presentation.api.param.PasswordResetMailSendRequest;
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

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * パスワードリセット Controller<br/>
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@SessionAttributes(value = "resetModel")
@RequestMapping("/reset/")
@Controller
public class ResetController extends AbstractController {
    /**
     * 不正遷移<br/>
     */
    protected static final String MSGCD_DB_UNIQUE_CONFIRMMAIL_PASSWORD_FAIL = "APX000101";

    /** パスワードを再設定する API */
    private final MailCertificationApi mailCertificationApi;

    /**
     * コンストラクタ
     *
     * @param mailCertificationApi
     */
    @Autowired
    public ResetController(MailCertificationApi mailCertificationApi) {
        this.mailCertificationApi = mailCertificationApi;
    }

    /**
     * パスワードリセット入力画面：初期処理
     *
     * @param resetModel
     * @param model
     * @return パスワードリセット入力画面
     */
    @GetMapping(value = "/")
    public String doLoadIndex(ResetModel resetModel, Model model) {
        // パスワードリセット入力画面のモデルをクリア
        clearModel(ResetModel.class, resetModel, model);

        return "reset/index";

    }

    /**
     * パスワードリセットメール送信処理<br/>
     *
     * @param resetModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return パスワードリセットメール送信完了画面
     */
    @PostMapping(value = "/", params = "doOncePassWordResetMailSend")
    @HEHandler(exception = AppLevelListException.class, returnView = "reset/index")
    public String doOncePassWordResetMailSend(@Validated ResetModel resetModel,
                                              BindingResult error,
                                              RedirectAttributes redirectAttributes,
                                              Model model) {

        if (error.hasErrors()) {
            return "reset/index";
        }

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        Timestamp birthDay = conversionUtility.toTimeStamp(resetModel.getMemberInfoBirthdayYear(),
                                                           resetModel.getMemberInfoBirthdayMonth(),
                                                           resetModel.getMemberInfoBirthdayDay()
                                                          );

        try {
            PasswordResetMailSendRequest passwordResetMailSendRequest = new PasswordResetMailSendRequest();
            passwordResetMailSendRequest.setMemberInfoMail(resetModel.getMemberInfoMail());
            passwordResetMailSendRequest.setDateOfBirth(birthDay);

            // パスワードリセットメール送信サービス実行
            mailCertificationApi.sendMailPasswordReset(passwordResetMailSendRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Exceptionログを出力しておく
            ApplicationLogUtility appLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
            appLogUtility.writeExceptionLog(e);

            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "reset/index";
        }

        // 完了画面へ遷移
        return "redirect:/reset/complete";
    }

    /**
     * パスワードリセットメール送信完了画面：画面表示処理
     *
     * @param resetModel
     * @param sessionStatus
     * @param model
     * @return パスワードリセットメール送信完了画面
     */
    @GetMapping(value = "/complete")
    protected String doLoadComplete(ResetModel resetModel, SessionStatus sessionStatus, Model model) {

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "reset/complete";

    }

}