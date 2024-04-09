/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.authregister;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthType;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationCheckDataGetRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationRegistRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 権限グループ登録画面用 Action クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/administrator/authregister")
@Controller
@SessionAttributes(value = "authRegisterModel")
@PreAuthorize("hasAnyAuthority('ADMIN:8')")
public class AuthRegisterController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRegisterController.class);

    /**
     * メッセージコード定数：登録しようとした権限グループは既に存在している
     */
    protected static final String GROUP_ALREADY_EXISTS = "AYP001001";

    /**
     * メッセージコード：登録完了
     */
    protected static final String REGISTER_COMPLETED = "AYP001005";

    public static final String MSGCD_PAGE_RELOAD =
                    "jp.co.hankyuhanshin.itec.hmbase.application.filter.FireGateFilter.INVALID_TRANSITION";

    /**
     * 新規登録モードであることを識別する為の値
     */
    protected static final String MD_NEW = "new";

    /**
     * 確認画面から
     */
    public static final String FLASH_FROM_CONFIRM = "fromConfirm";

    /**
     * メタ権限情報。DICON で設定された値が自動バインドされる。必ず設定されている必要あり
     */
    //    @Binding(bindingType = BindingType.MUST)
    private List<MetaAuthType> metaAuthList;

    /**
     * 権限グループAPI
     **/
    private final AuthorizationApi authorizationApi;

    /**
     * 権限グループ登録画面用 Dxo クラス
     **/
    private final AuthRegisterHelper authRegisterHelper;

    /**
     * コンストラクタ
     *
     * @param authorizationApi              権限グループAPI
     * @param authRegisterHelper            権限グループ登録画面用 Dxo クラス
     */
    @Autowired
    public AuthRegisterController(AuthorizationApi authorizationApi, AuthRegisterHelper authRegisterHelper) {
        this.authorizationApi = authorizationApi;
        this.authRegisterHelper = authRegisterHelper;
        this.metaAuthList = (List<MetaAuthType>) ApplicationContextUtility.getApplicationContext()
                                                                          .getBean("metaAuthTypeList");
    }

    /**
     * 初期表示アクション
     *
     * @param md                the md
     * @param authRegisterModel 権限グループ登録画面用
     * @param model             the model
     * @return 遷移先クラス
     */
    @GetMapping("")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                                 AuthRegisterModel authRegisterModel,
                                 Model model) {

        if (!model.containsAttribute(FLASH_FROM_CONFIRM)) {
            clearModel(AuthRegisterModel.class, authRegisterModel, model);
        }

        // 新規登録モードの場合、初期化を行う。
        if ((md.isPresent() && MD_NEW.equals(md.get()) || (authRegisterModel.getAuthItems() == null))) {
            authRegisterHelper.toPageForLoad(this.metaAuthList, authRegisterModel);
        }

        return "administrator/authregister/register";
    }

    /**
     * 画面描画直前にTeedaより呼び出されるメソッド
     * 注意事項
     * １．prerenderはバリデーションエラー発生時にも呼び出される
     * doアクションが成功していることを前提にしたコードを記述してはならない。
     * ２．doアクションの後処理として利用しない
     * 　doアクションの後処理用には postDoActionメソッドが提供されているのでそちらを利用すること
     *
     * @param authRegisterModel 権限グループ登録画面用
     * @param model             the model
     * @return 遷移先クラス
     */
    public String prerender(AuthRegisterModel authRegisterModel, Model model) {
        //        prerender(authenRegistModel, model);

        // サブアプリケーションスコープ消失時の対応
        // doLoadと同様の処理を行い、バリデーションエラー発生時でも権限種別が画面に表示されるようにする。
        if (authRegisterModel.getAuthItems() == null) {
            authRegisterHelper.toPageForLoad(metaAuthList, authRegisterModel);
        }

        return null;
    }

    /**
     * 権限グループ登録確認画面へ遷移するアクション
     *
     * @param authRegisterModel  権限グループ登録画面用
     * @param error              the error
     * @param redirectAttributes the redirect attributes
     * @return 遷移先クラス
     */
    @PostMapping(value = "/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/authregister/register")
    public String doConfirm(@Validated AuthRegisterModel authRegisterModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes) {

        if (error.hasErrors()) {
            return "administrator/authregister/register";
        }

        // 登録可能チェック
        checkNotExistence(authRegisterModel, error);

        if (error.hasErrors()) {
            return "administrator/authregister/register";
        }

        redirectAttributes.addFlashAttribute("authRegisterModel", authRegisterModel);
        return "redirect:/administrator/authregister/confirm";
    }

    /**
     * 権限一覧画面へ遷移するアクション
     *
     * @return 遷移先クラス
     */
    @PostMapping(value = "/", params = "doCancel")
    public String doCancel() {
        return "redirect:/administrator/auth";
    }

    /**
     * 初期表示アクション
     *
     * @param authRegisterModel 権限グループ登録画面用
     * @param model             the model
     * @return 遷移先ページ
     */
    @GetMapping("/confirm")
    protected String doLoadIndex(AuthRegisterModel authRegisterModel,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        //        super.doLoad();
        AuthRegisterModel registerModel;
        if (model.containsAttribute("authRegisterModel")) {
            registerModel = (AuthRegisterModel) model.getAttribute("authRegisterModel");
            if (registerModel == null || (registerModel.getAuthItems() == null && StringUtils.isEmpty(
                            registerModel.getAuthGroupDisplayName()))) {
                addInfoMessage(MSGCD_PAGE_RELOAD, null, redirectAttributes, model);
                return "redirect:/administrator/authregister/";
            }
            if (registerModel != null) {
                authRegisterHelper.setLevelName(registerModel);
            }
        }

        return "administrator/authregister/confirm";
    }

    /**
     * 入力画面へ戻るアクション
     *
     * @param redirectAttributes the redirect attributes
     * @return 遷移先ページ
     */
    @PostMapping(value = "/confirm", params = "doGoBack")
    public String doGoBack(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(FLASH_FROM_CONFIRM, true);
        return "redirect:/administrator/authregister";
    }

    /**
     * 権限グループ登録アクション
     *
     * @param authRegisterModel  権限グループ登録画面用
     * @param error              エラー
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @param sessionStatus      the session status
     * @return 遷移先ページ
     */
    @PostMapping(value = "/confirm", params = "doOnceRegister")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/authregister/register")
    @HEHandler(exception = DataIntegrityViolationException.class, returnView = "administrator/authregister/confirm",
               messageCode = GROUP_ALREADY_EXISTS)
    public String doOnceRegister(AuthRegisterModel authRegisterModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 SessionStatus sessionStatus) {

        // 登録可能チェック
        checkNotExistence(authRegisterModel, error);

        if (error.hasErrors()) {
            return "administrator/authregister/register";
        }

        // ページ上にある入力情報から登録用 DTO を作成する
        AdminAuthGroupEntity group = authRegisterHelper.toAdminAuthGroupEntityForRegister(authRegisterModel);

        // ユーザのショップSEQをDTOへセットする
        group.setShopSeq(getCommonInfo().getCommonInfoBase().getShopSeq());

        // 登録する
        AuthorizationRegistRequest authorizationRegistRequest = authRegisterHelper.toAuthorizationRegistRequest(group);

        try {
            authorizationApi.regist(authorizationRegistRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "administrator/authregister/register";
        }

        addInfoMessage(REGISTER_COMPLETED, null, redirectAttributes, model);

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/administrator/auth";
    }

    /**
     * 登録しようとしている権限グループが存在しないことを確認する
     *
     * @param authRegisterModel 権限グループ登録画面用
     * @param error             エラー
     */
    protected void checkNotExistence(AuthRegisterModel authRegisterModel, BindingResult error) {

        String groupName = authRegisterModel.getAuthGroupDisplayName();

        AuthorizationCheckDataGetRequest authorizationCheckDataGetRequest = new AuthorizationCheckDataGetRequest();
        authorizationCheckDataGetRequest.setAuthGroupDisplayName(groupName);

        try {

            AuthorizationResponse authorizationResponse = authorizationApi.checkData(authorizationCheckDataGetRequest);

            if (authorizationResponse != null) {
                throwMessage(GROUP_ALREADY_EXISTS);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }
}