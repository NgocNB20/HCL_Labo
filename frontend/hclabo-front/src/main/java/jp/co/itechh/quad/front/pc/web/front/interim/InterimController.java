/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.interim;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.temp.presentation.api.TempApi;
import jp.co.itechh.quad.temp.presentation.api.param.TempCustomerRegistRequest;
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
 * 利用規約画面 Controller
 *
 * @author kimura
 */
@SessionAttributes(value = "interimModel")
@RequestMapping("/interim")
@Controller
public class InterimController extends AbstractController {

    /** DB一意制約例外 */
    protected static final String MSGCD_DB_UNIQUE_CONFIRMMAIL_PASSWORD_FAIL = "AMR000101";

    /** 仮会員登録サービス */
    private final TempApi tempApi;

    /**
     * コンストラクタ
     *
     * @param tempApi 仮会員登録サービス
     */
    @Autowired
    public InterimController(TempApi tempApi) {
        this.tempApi = tempApi;
    }

    /**
     * 入力画面：初期処理
     *
     * @param interimModel 利用規約 Model
     * @param model        model
     * @return 入力画面
     */
    @GetMapping(value = "/")
    protected String doLoadIndex(InterimModel interimModel, Model model) {

        if (!model.containsAttribute(FLASH_MESSAGES)) {
            // リダイレクト以外の場合、初期化処理
            clearModel(InterimModel.class, interimModel, model);
        }

        return "interim/index";
    }

    /**
     * 仮会員登録処理
     *
     * @param interimModel       利用規約 Model
     * @param error              error
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return 完了画面
     */
    @PostMapping(value = "/", params = "doOnceTempMemberInfoRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "interim/index")
    public String doOnceTempMemberInfoRegist(@Validated InterimModel interimModel,
                                             BindingResult error,
                                             RedirectAttributes redirectAttributes,
                                             Model model) {

        if (error.hasErrors()) {
            return "interim/index";
        }

        try {
            // 仮会員登録サービス実行
            TempCustomerRegistRequest tempCustomerRegistRequest = new TempCustomerRegistRequest();

            tempCustomerRegistRequest.setMemberInfoMail(interimModel.getMemberInfoMail());

            tempApi.registTemps(tempCustomerRegistRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Exceptionログを出力しておく
            ApplicationLogUtility appLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
            appLogUtility.writeExceptionLog(e);

            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "interim/index";
        }

        // 完了画面へ遷移
        return "redirect:/interim/complete";
    }

    /**
     * 完了画面：画面表示処理
     *
     * @return 完了画面
     */
    @GetMapping(value = "/complete")
        protected String doLoadComplete(InterimModel interimModel) {

        return "interim/complete";
    }
}