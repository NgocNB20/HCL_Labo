/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.pass;

import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerPasswordUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.web.AbstractController;
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

import java.util.HashMap;
import java.util.Map;

/**
 * パスワード変更 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 * @version $Revision: 1.0 $
 *
 */
@SessionAttributes(value = "memberPassModel")
@RequestMapping("/member/pass/")
@Controller
public class MemberPassController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberPassController.class);

    /** パスワード変更 Helper */
    private final MemberPassHelper memberPassHelper;

    /** 会員API */
    private final CustomerApi customerApi;

    /**
     * コンストラクタ
     *
     * @param memberPassHelper パスワード変更 Helper
     * @param customerApi 会員API
     */
    @Autowired
    public MemberPassController(MemberPassHelper memberPassHelper, CustomerApi customerApi) {
        this.memberPassHelper = memberPassHelper;
        this.customerApi = customerApi;
    }

    /**
     * パスワード変更入力画面：初期処理
     *
     * @param memberPassModel パスワード変更 Model
     * @param model Model
     * @return パスワードリセット入力画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/error")
    public String doLoadIndex(MemberPassModel memberPassModel, BindingResult error, Model model) {

        MemberInfoEntity memberInfoEntity = null;
        // 会員情報取得サービス実行
        Integer memberInfoSeq = this.getCommonInfo().getCommonInfoUser().getMemberInfoSeq();

        CustomerResponse customerResponse = null;
        try {
            customerResponse = customerApi.getByMemberinfoSeq(memberInfoSeq);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/error";
        }

        memberInfoEntity = memberPassHelper.toMemberInfoEntity(customerResponse);

        // ページに取得した会員情報を設定
        memberPassModel.setMemberInfoEntity(memberInfoEntity);

        return "member/pass/index";

    }

    /**
     * パスワード変更処理<br/>
     *
     * @param memberPassModel パスワード変更 Model
     * @param error error
     * @param model Model
     * @return パスワード変更完了画面
     */
    @PostMapping(value = "/", params = "doOncePassWordUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/pass/index")
    public String doOncePassWordUpdate(@Validated MemberPassModel memberPassModel, BindingResult error, Model model) {

        if (error.hasErrors()) {
            return "member/pass/index";
        }

        Integer memberinfoSeq = null;
        CustomerPasswordUpdateRequest customerPasswordUpdateRequest = new CustomerPasswordUpdateRequest();
        if (memberPassModel.getMemberInfoEntity() != null) {
            memberinfoSeq = memberPassModel.getMemberInfoEntity().getMemberInfoSeq();

            customerPasswordUpdateRequest = memberPassHelper.toCustomerPasswordUpdateRequest(memberPassModel);
        }

        try {
            // パスワード変更サービス実行
            customerApi.updatePassword(memberinfoSeq, customerPasswordUpdateRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "member/pass/index";
        }

        // パスワード変更完了画面へ遷移
        return "redirect:/member/pass/complete";
    }

    /**
     * パスワード変更完了画面：画面表示処理
     *
     * @param memberPassModel パスワード変更 Model
     * @param model Model
     * @return パスワード変更完了画面
     */
    @GetMapping(value = "/complete")
    public String doLoadConfirm(MemberPassModel memberPassModel, SessionStatus sessionStatus, Model model) {
        sessionStatus.setComplete();
        return "member/pass/complete";

    }
}