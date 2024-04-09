/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.mail;

import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerMailAddressUpdateSendMailRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.web.AbstractController;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * メールアドレス変更 Controller
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@SessionAttributes(value = "memberMailModel")
@RequestMapping("/member/mail")
@Controller
public class MemberMailController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberMailController.class);

    /**
     * 確認メールDBユニーク<br/>
     */
    protected static final String MSGCD_DB_UNIQUE_CONFIRMMAIL_PASSWORD_FAIL = "AMX000801";

    /**
     * メールアドレス不正<br/>
     */
    protected static final String MSGCD_MAIL_ADDRESS_FAIL = "AMX000802";

    /**
     * メールアドレス変更 Helper<br/>
     */
    private final MemberMailHelper memberMailHelper;

    /**
     * 会員API
     */
    private final CustomerApi customerApi;

    /**
     * コンストラクタ
     *
     * @param memberMailHelper メールアドレス変更HELPER
     * @param customerApi 会員API
     */
    @Autowired
    public MemberMailController(MemberMailHelper memberMailHelper, CustomerApi customerApi) {
        this.memberMailHelper = memberMailHelper;
        this.customerApi = customerApi;
    }

    /**
     * 変更画面：初期処理
     *
     * @param memberMailModel メールアドレス変更 Model
     * @param model           Model
     * @return 変更画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/mail/index")
    protected String doLoadIndex(MemberMailModel memberMailModel, BindingResult error, Model model) {

        // 初期化処理
        clearModel(MemberMailModel.class, memberMailModel, model);

        CustomerResponse customerResponse = null;
        try {
            // 会員情報取得
            Integer memberInfoSeq = getCommonInfo().getCommonInfoUser().getMemberInfoSeq();
            customerResponse = customerApi.getByMemberinfoSeq(memberInfoSeq);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "member/mail/index";
        }

        MemberInfoEntity memberInfoEntity = memberMailHelper.toMemberInfoEntity(customerResponse);

        // メールアドレスをセット
        memberMailHelper.toPageForLoad(memberInfoEntity, memberMailModel);

        return "member/mail/index";
    }

    /**
     * メールアドレス送信確認処理<br/>
     *
     * @param memberMailModel メールアドレス変更 Model
     * @param error           エラー
     * @param model           Model
     * @return メールアドレス送信完了画面
     */
    @PostMapping(value = "/", params = "doOnceMailAddressSendConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/mail/index")
    public String doOnceMailAddressSendConfirm(@Validated MemberMailModel memberMailModel,
                                               BindingResult error,
                                               RedirectAttributes redirectAttributes,
                                               Model model) {

        if (error.hasErrors()) {
            return "member/mail/index";
        }

        // メールアドレスがない場合は、画面再表示メッセージ
        if (StringUtils.isEmpty(memberMailModel.getMemberInfoMail())) {
            throwMessage(MSGCD_MAIL_ADDRESS_FAIL);
        }

        Integer memberInfoSeq = getCommonInfo().getCommonInfoUser().getMemberInfoSeq();
        CustomerMailAddressUpdateSendMailRequest customerMailAddressUpdateSendMailRequest =
                        memberMailHelper.toCustomerMailAddressUpdateSendMailRequest(memberMailModel);

        try {
            // 変更メールアドレス登録
            customerApi.send(memberInfoSeq, customerMailAddressUpdateSendMailRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Exceptionログを出力しておく
            ApplicationLogUtility appLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
            appLogUtility.writeExceptionLog(e);

            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "member/mail/index";
        }

        // メールアドレス送信完了画面へ遷移
        return "redirect:/member/mail/complete";
    }

    /**
     * 入力画面：初期処理
     *
     * @param memberMailModel
     * @param model
     * @return 入力画面
     */
    @GetMapping(value = "/complete")
    protected String doLoadComplete(MemberMailModel memberMailModel, Model model) {

        return "member/mail/complete";
    }

}