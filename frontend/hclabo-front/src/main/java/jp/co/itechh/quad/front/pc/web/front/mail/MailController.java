/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.mail;

import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerMailAddressUpdateRequest;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.constant.type.HTypeSendStatus;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.pc.web.front.member.mail.MemberMailModel;
import jp.co.itechh.quad.front.service.common.impl.HmFrontUserDetailsServiceImpl;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.mailcertification.presentation.api.MailCertificationApi;
import jp.co.itechh.quad.mailcertification.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 会員詳細情報取得 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@SessionAttributes(value = "mailModel")
@RequestMapping("/mail")
@Controller
public class MailController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailController.class);

    /** メールパスワード不正 */
    protected static final String MSGCD_MAILPASSWORD_FAIL = "AMX001001";

    /** メールアドレス変更 Helper */
    private final MailHelper mailHelper;

    /**
     * メール認証API
     */
    private final MailCertificationApi mailCertificationApi;

    /**
     * 会員API
     */
    private final CustomerApi customerApi;

    /**
     * メルマガAPI
     */
    private final MailMagazineApi mailMagazineApi;

    /** 認証サービス */
    private final HmFrontUserDetailsServiceImpl hmFrontUserDetailsService;

    /** Persistent Token方式を利用する場合のトークンリポジトリ */
    private final PersistentTokenBasedRememberMeServices rememberMeTokenService;

    /**
     * コンストラクタ
     *
     * @param mailHelper                       メールアドレス変更HELPER
     * @param mailCertificationApi             メール認証API
     * @param mailMagazineApi                  メールAPI
     * @param customerApi                      会員API
     * @param hmFrontUserDetailsService        Front認証サービスクラス
     * @param rememberMeTokenService           PersistentTokenBasedRememberMeServices
     */
    @Autowired
    public MailController(MailHelper mailHelper,
                          MailCertificationApi mailCertificationApi,
                          MailMagazineApi mailMagazineApi,
                          CustomerApi customerApi,
                          HmFrontUserDetailsServiceImpl hmFrontUserDetailsService,
                          PersistentTokenBasedRememberMeServices rememberMeTokenService) {
        this.mailHelper = mailHelper;
        this.mailCertificationApi = mailCertificationApi;
        this.customerApi = customerApi;
        this.mailMagazineApi = mailMagazineApi;
        this.hmFrontUserDetailsService = hmFrontUserDetailsService;
        this.rememberMeTokenService = rememberMeTokenService;
    }

    /**
     * 変更画面：初期処理
     *
     * @param mailModel          メールアドレス変更 Model
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     * @return 変更画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "mail/regist")
    protected String doLoadIndex(MailModel mailModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        mailModel.setErrorUrl(true);
        // パラメータチェック
        if (StringUtils.isEmpty(mailModel.getMid())) {
            addMessage(MSGCD_MAILPASSWORD_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        CustomerResponse customerInfoUpdateMail = null;
        jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse customerResponse = null;
        try {
            // 取得情報
            customerInfoUpdateMail = mailCertificationApi.getCustomerInfoUpdateMail(mailModel.getMid());
            // SEQから変更前の会員情報を取得
            customerResponse = customerApi.getByMemberinfoSeq(customerInfoUpdateMail.getMemberInfoSeq());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "mail/regist";
        }

        if (!ObjectUtils.isEmpty(customerResponse)) {
            // 変更前のメアドをセット
            mailModel.setPreMemberInfoMail(customerResponse.getMemberInfoMail());
        }

        MemberInfoEntity memberInfoEntity = mailHelper.toMemberInfoEntityForLoad(customerInfoUpdateMail);
        // ページに反映
        mailHelper.toPageForLoad(memberInfoEntity, mailModel);

        MemberInfoEntity preMemberInfoEntity = mailHelper.toMemberInfoEntity(customerResponse);
        // ページにメルマガ情報をセット
        setMailMagazineMember(mailModel, error, preMemberInfoEntity);

        if (error.hasErrors()) {
            return "mail/regist";
        }

        // 有効なURLから遷移してきた。
        mailModel.setErrorUrl(false);

        return "mail/regist";

    }

    /**
     * メールアドレスの変更処理<br/>
     *
     * @param mailModel          メールアドレス変更 Model
     * @param error              BindingResult
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     * @param request            HttpServletRequest リクエスト
     * @param response           HttpServletResponse レスポンス
     * @return 完了画面
     */
    @PostMapping(value = "/", params = "doOnceMailUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "mail/regist")
    public String doOnceMailUpdate(@Validated MailModel mailModel,
                                   BindingResult error,
                                   RedirectAttributes redirectAttributes,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        // 変更会員情報
        MemberInfoEntity memberInfoEntity;
        try {
            memberInfoEntity = mailHelper.toMemberInfoEntityForMailUpdate(mailModel);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSGCD_MAILPASSWORD_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        try {
            // 会員メールアドレス変更サービス実行
            CustomerMailAddressUpdateRequest customerMailAddressUpdateRequest =
                            mailHelper.toCustomerMailAddressUpdateRequest(mailModel);
            customerApi.updateMail(memberInfoEntity.getMemberInfoSeq(), customerMailAddressUpdateRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "mail/regist";
        }

        // セッションのユーザー情報を更新
        request.setAttribute("isCheckInfo", true);

        try {
            hmFrontUserDetailsService.updateUserInfo(mailModel.getMemberInfoEntity().getMemberInfoId());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "mail/regist";
        }

        try {
            // Remember-Meトークンを更新
            if (StringUtils.isNotEmpty(hmFrontUserDetailsService.extractRememberMeCookie(request))) {
                Authentication newAuthentications = SecurityContextHolder.getContext().getAuthentication();
                rememberMeTokenService.loginSuccess(request, response, newAuthentications);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "mail/regist";
        }

        // メール送信完了画面に遷移
        return "redirect:/mail/complete";
    }

    /**
     * 変更画面：初期処理
     *
     * @param memberMailModel メールアドレス変更 Model
     * @param sessionStatus   SessionStatus
     * @param model           Model
     * @return 変更画面
     */
    @GetMapping(value = "/complete")
    protected String doLoadComplete(MemberMailModel memberMailModel, SessionStatus sessionStatus, Model model) {

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "mail/complete";

    }

    /**
     * メルマガ会員情報を設定<br/>
     * 将来的には、会員とメルマガ会員のテーブルは統合されるので、このあたりのメソッドは不要となる
     *
     * @param mailModel         メールアドレス変更 Model
     * @param memberInfoEntity  会員クラス
     */
    protected void setMailMagazineMember(MailModel mailModel, BindingResult error, MemberInfoEntity memberInfoEntity) {
        MailmagazineMemberResponse mailmagazineMemberResponse = null;

        if (!ObjectUtils.isEmpty(memberInfoEntity)) {
            try {
                // メルマガ会員情報を取得
                mailmagazineMemberResponse = mailMagazineApi.getByMemberinfoSeq(memberInfoEntity.getMemberInfoSeq());
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                return;
            }
        }

        if (mailmagazineMemberResponse != null
            && EnumTypeUtil.getEnumFromValue(HTypeSendStatus.class, mailmagazineMemberResponse.getSendStatus())
               == HTypeSendStatus.SENDING) {
            mailModel.setMailMagazine(true);
            mailModel.setPreMailMagazine(true);
        } else {
            mailModel.setMailMagazine(false);
            mailModel.setPreMailMagazine(false);
        }
    }
}