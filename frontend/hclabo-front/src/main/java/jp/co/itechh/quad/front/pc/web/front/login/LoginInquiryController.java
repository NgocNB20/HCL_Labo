/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.login;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.front.utility.InquiryUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.inquiry.presentation.api.InquiryApi;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryGetRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * 問合せ認証Controller
 *
 * @author kaneda
 */
@SessionAttributes(value = "loginInquiryModel")
@RequestMapping("/login/inquiry")
@Controller
public class LoginInquiryController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInquiryController.class);

    /**
     * メッセージコード：該当の問い合わせ情報がない場合
     */
    private static final String MSGCD_GET_FEILED_INQUIRY = "PKG-3720-001-A-";

    /**
     * メッセージコード：会員のお問い合わせの場合
     */
    private static final String MSGCD_MEMBER_INQUIRY = "PKG-3720-002-A-";

    /**
     * 問合せ系ユーティリティ
     */
    private InquiryUtility inquiryUtility;

    /**
     * 問い合わせAPI
     */
    private final InquiryApi inquiryApi;

    /**
     * 問合せ認証Helper
     */
    private final LoginInquiryHelper loginInquiryHelper;

    /**
     * コンストラクタ
     *
     * @param inquiryApi         the inquiry api
     * @param loginInquiryHelper 問合せ認証 Helper
     * @param inquiryUtility     問合せ系ユーティリティ
     */
    @Autowired
    public LoginInquiryController(InquiryApi inquiryApi,
                                  LoginInquiryHelper loginInquiryHelper,
                                  InquiryUtility inquiryUtility) {
        this.inquiryApi = inquiryApi;
        this.loginInquiryHelper = loginInquiryHelper;
        this.inquiryUtility = inquiryUtility;
    }

    /**
     * 問合せ認証：初期処理
     *
     * @param icd
     * @param loginInquiryModel
     * @param model
     * @return 問合せ認証画面
     */
    @GetMapping("/")
    protected String doLoadIndex(@RequestParam(required = false) String icd,
                                 LoginInquiryModel loginInquiryModel,
                                 Model model) {

        // 初期化処理
        clearModel(LoginInquiryModel.class, loginInquiryModel, model);

        // URLパラメータを設定
        loginInquiryModel.setInquiryCode(icd);

        return "login/inquiry";
    }

    /**
     * 問合せ認証処理
     *
     * @param loginInquiryModel
     * @param error
     * @param redirectAttributes
     * @return 問合せ詳細画面
     */
    @PostMapping(value = "/", params = "doLogin")
    @HEHandler(exception = AppLevelListException.class, returnView = "login/inquiry")
    public String doLogin(@Validated LoginInquiryModel loginInquiryModel,
                          BindingResult error,
                          RedirectAttributes redirectAttributes,
                          SessionStatus sessionStatus,
                          Model model) {

        if (error.hasErrors()) {
            return "login/inquiry";
        }

        InquiryGetRequest inquiryGetRequest = loginInquiryHelper.toInquiryGetRequest(loginInquiryModel.getInquiryTel());

        InquiryResponse inquiryResponse = null;
        try {
            // 問い合わせ情報を取得
            inquiryResponse = inquiryApi.getByInquiryCode(loginInquiryModel.getInquiryCode(), inquiryGetRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "login/inquiry";
        }
        InquiryEntity inquiry = loginInquiryHelper.toInquiryEntity(inquiryResponse);

        // 認証対象のデータかをチェック
        checkData(inquiry);

        // Sessionに認証済みのお問い合わせ番号として登録
        inquiryUtility.saveCode(inquiry.getInquiryCode());

        // 問合せ詳細画面へ
        redirectAttributes.addFlashAttribute("icd", loginInquiryModel.getInquiryCode());

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/guest/inquiry/";
    }

    /**
     * 認証対象のデータかをチェック
     *
     * @param inquiry 問い合わせEntity
     */
    private void checkData(InquiryEntity inquiry) {
        if (ObjectUtils.isEmpty(inquiry)) {
            throwMessage(MSGCD_GET_FEILED_INQUIRY);
        }
        Integer memberInfoSeq = inquiry.getMemberInfoSeq();
        if (!(memberInfoSeq == null || memberInfoSeq == 0)) {
            // 問い合わせ情報が「会員」の場合はエラーメッセージを表示
            throwMessage(MSGCD_MEMBER_INQUIRY);
        }
    }
}