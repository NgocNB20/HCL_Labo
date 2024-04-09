/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.mailmagazine;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineDeleteRequest;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineRegistRequest;
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
 * メルマガ Controller
 *
 * @author kimura
 */
@RequestMapping("/mailmagazine")
@Controller
@SessionAttributes(value = "mailmagazineModel")
public class MailmagazineController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailmagazineController.class);

    /** 解除処理を行わなかった場合 */
    private static final String MSGCD_REMOVE_PROCESS_NONE = "AZX000403";

    /** メールマガジン API */
    private final MailMagazineApi mailMagazineApi;

    /**
     * コンストラクタ
     *
     */
    @Autowired
    public MailmagazineController(MailMagazineApi mailMagazineApi) {
        this.mailMagazineApi = mailMagazineApi;
    }

    /** ***************** Regist start ***************** **/

    /**
     * 選択画面：初期処理
     *
     * @param mailmagazineModel
     * @param model
     * @return メールマガジン登録/解除の選択画面
     */
    @GetMapping(value = "/")
    protected String doLoadIndex(MailmagazineModel mailmagazineModel, Model model) {

        // モデル初期化
        clearModel(MailmagazineModel.class, mailmagazineModel, model);

        return "mailmagazine/index";
    }

    /**
     * 入力画面：初期処理
     *
     * @param mailmagazineModel
     * @param model
     * @return 登録画面
     */
    @GetMapping(value = "/regist")
    protected String doLoadRegist(MailmagazineModel mailmagazineModel, Model model) {

        // モデル初期化
        clearModel(MailmagazineModel.class, mailmagazineModel, model);

        return "mailmagazine/regist";
    }

    /**
     * メルマガ登録処理<br/>
     * 形式はHTMLのみ。アドレスタイプ判定は実施しない
     *
     * @param mailmagazineModel
     * @param error
     * @param sessionStatus
     * @param model
     * @return メルマガ登録完了画面
     */
    @PostMapping(value = "/regist", params = "doOnceRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "mailmagazine/regist")
    public String doOnceRegist(@Validated MailmagazineModel mailmagazineModel,
                               BindingResult error,
                               SessionStatus sessionStatus,
                               Model model) {

        if (error.hasErrors()) {
            return "mailmagazine/regist";
        }

        // 登録用メールアドレス
        String mailAddress = mailmagazineModel.getMailAddress();

        MailmagazineRegistRequest mailmagazineRegistRequest = new MailmagazineRegistRequest();
        mailmagazineRegistRequest.setMailAddress(mailAddress);
        try {
            mailMagazineApi.regist(mailmagazineRegistRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "mailmagazine/regist";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // メルマガ登録完了画面へ遷移
        return "redirect:/mailmagazine/complete";
    }

    /**
     * 完了画面：初期処理
     *
     * @param mailmagazineModel
     * @param model
     * @return 登録完了画面
     */
    @GetMapping(value = "/complete")
    protected String doLoadComplete(MailmagazineModel mailmagazineModel, Model model) {
        return "mailmagazine/complete";
    }

    /** ***************** Regist end ***************** **/

    /** ***************** Cancel start ***************** **/

    /**
     * 入力画面：初期処理
     *
     * @param mailmagazineModel
     * @param model
     * @return 解除画面
     */
    @GetMapping(value = "/cancel")
    protected String doLoadCancel(MailmagazineModel mailmagazineModel, Model model) {

        // モデル初期化
        clearModel(MailmagazineModel.class, mailmagazineModel, model);

        return "mailmagazine/cancel";
    }

    /**
     * メルマガ解除処理
     *
     * @param mailmagazineModel
     * @param error
     * @param sessionStatus
     * @param model
     * @return 解除完了画面
     */
    @PostMapping(value = "/cancel", params = "doOnceCancel")
    @HEHandler(exception = AppLevelListException.class, returnView = "mailmagazine/cancel")
    public String doOnceCancel(@Validated MailmagazineModel mailmagazineModel,
                               BindingResult error,
                               SessionStatus sessionStatus,
                               Model model) {

        if (error.hasErrors()) {
            return "mailmagazine/cancel";
        }
        MailmagazineDeleteRequest mailmagazineDeleteRequest = new MailmagazineDeleteRequest();
        mailmagazineDeleteRequest.setMailAddress(mailmagazineModel.getMailAddress());

        try {
            mailMagazineApi.delete(mailmagazineDeleteRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "mailmagazine/cancel";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // メルマガ解除完了画面に遷移
        return "redirect:/mailmagazine/ccomplete";
    }

    /**
     * 完了画面：初期処理
     *
     * @param mailmagazineModel
     * @param model
     * @return 解除完了画面
     */
    @GetMapping(value = "/ccomplete")
    protected String doLoadCComplete(MailmagazineModel mailmagazineModel, Model model) {
        return "mailmagazine/ccomplete";
    }

    /** ***************** Cancel end ***************** **/
}