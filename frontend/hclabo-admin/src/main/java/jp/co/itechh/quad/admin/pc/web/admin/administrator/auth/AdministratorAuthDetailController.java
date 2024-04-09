/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.auth;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthType;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.pc.web.admin.util.IdenticalDataCheckUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 権限グループ詳細画面 Action クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/administrator/auth/detail")
@Controller
@SessionAttributes(value = "administratorAuthDetailModel")
@PreAuthorize("hasAnyAuthority('ADMIN:4')")
public class AdministratorAuthDetailController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorAuthDetailController.class);
    /**
     * メッセージコード：対象の権限グループが見つからない
     */
    protected static final String GROUP_NOT_FOUND = "AYP001004";

    /**
     * メッセージコード：不正遷移
     */
    protected static final String BAD_TRANSITION = "AYP001003";

    /**
     * The constant FLASH_SEQ.
     */
    public static final String FLASH_SEQ = "seq";

    /**
     * 権限グループ詳細画面 Helper クラス
     */
    public AdministratorAuthDetailHelper administratorAuthDetailHelper;

    /**
     * メタ権限情報。DICON で設定された値が自動バインドされる。必ず設定されている必要あり
     */
    private List<MetaAuthType> metaAuthList;

    /**
     * 権限グループAPI
     **/
    private final AuthorizationApi authorizationApi;

    /**
     * コンストラクタ
     *
     * @param administratorAuthDetailHelper 権限グループ詳細画面 Helper クラス
     * @param authorizationApi              権限グループAPI
     */
    @Autowired
    public AdministratorAuthDetailController(AdministratorAuthDetailHelper administratorAuthDetailHelper,
                                             AuthorizationApi authorizationApi) {
        this.administratorAuthDetailHelper = administratorAuthDetailHelper;
        this.authorizationApi = authorizationApi;
        this.metaAuthList = (List<MetaAuthType>) ApplicationContextUtility.getApplicationContext()
                                                                          .getBean("metaAuthTypeList");
    }

    /**
     * 初期表示
     *
     * @param seq                          SEQ
     * @param administratorAuthDetailModel 権限グループ詳細画面
     * @param error                        the error
     * @param redirectAttrs                the redirect attrs
     * @param model                        the model
     * @return 遷移先ページ
     */
    @GetMapping(value = "")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/auth/detail")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> seq,
                                 AdministratorAuthDetailModel administratorAuthDetailModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttrs,
                                 Model model) {

        // super.doLoad();

        // authSeq を受け取っている場合は adminAuthGroupSeq として保存
        // if (administratorAuthDetailModel.getSeq() != null) {
        // administratorAuthDetailModel.setAdminAuthGroupSeq(administratorAuthDetailModel.getSeq().toString());
        // }

        if (seq.isPresent()) {
            administratorAuthDetailModel.setAdminAuthGroupSeq(seq.get().toString());
        } else if (model.containsAttribute("seq")) {
            administratorAuthDetailModel.setAdminAuthGroupSeq((String) model.getAttribute(FLASH_SEQ));
        }

        checkBadTransition(administratorAuthDetailModel);

        // 権限グループ情報とメタ権限情報をページへ設定する
        Integer shopSeq = getCommonInfo().getCommonInfoBase().getShopSeq();

        try {
            // 編集対象の権限グループ情報
            AuthorizationResponse authorizationResponse = authorizationApi.getByAdminAuthGroupSeq(
                            Integer.valueOf(administratorAuthDetailModel.getAdminAuthGroupSeq()));

            AdminAuthGroupEntity entity =
                            administratorAuthDetailHelper.toAdminAuthGroupEntity(authorizationResponse, shopSeq);

            // 対象の権限グループが存在しない場合
            if (entity == null) {
                addMessage(GROUP_NOT_FOUND, redirectAttrs, model);
                return "redirect:/administrator/auth";
            }

            administratorAuthDetailHelper.toPageForLoad(entity, metaAuthList, administratorAuthDetailModel);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/administrator/auth";
        }

        return "administrator/auth/detail";
    }

    /**
     * 一覧ページへ遷移アクション
     *
     * @return 遷移先ページ
     */
    @PostMapping(value = "/", params = "doGoBack")
    public String doGoBack() {
        return "redirect:/administrator/auth";
    }

    /**
     * 削除確認ページへ遷移アクション
     *
     * @param administratorAuthDetailModel 権限グループ詳細画面
     * @param redirectAttrs                the redirect attrs
     * @param model                        the model
     * @return 遷移先ページ
     */
    @PostMapping(value = "/", params = "doDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/auth/detail")
    @PreAuthorize("hasAnyAuthority('ADMIN:8')")
    public String doDelete(AdministratorAuthDetailModel administratorAuthDetailModel,
                           RedirectAttributes redirectAttrs,
                           Model model) {

        // 実行前処理
        String check = preDoAction(administratorAuthDetailModel, redirectAttrs, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        checkBadTransition(administratorAuthDetailModel);

        redirectAttrs.addFlashAttribute("seq", administratorAuthDetailModel.getAdminAuthGroupSeq());

        return "redirect:/administrator/auth/delete";
    }

    /**
     * 修正ページへ遷移アクション
     *
     * @param administratorAuthDetailModel 権限グループ詳細画面
     * @param redirectAttributes           the redirect attributes
     * @param model                        the model
     * @return 修正ページ
     */
    @PostMapping(value = "/", params = "doModify")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/auth/detail")
    @PreAuthorize("hasAnyAuthority('ADMIN:8')")
    public String doModify(AdministratorAuthDetailModel administratorAuthDetailModel,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        // 実行前処理
        String check = preDoAction(administratorAuthDetailModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        checkBadTransition(administratorAuthDetailModel);
        redirectAttributes.addFlashAttribute("seq", administratorAuthDetailModel.getAdminAuthGroupSeq());

        return "redirect:/administrator/authupdate";
    }

    /**
     * 不正遷移チェック
     *
     * @param administratorAuthDetailModel 権限グループ詳細画面
     */
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/administrator/auth")
    protected void checkBadTransition(AdministratorAuthDetailModel administratorAuthDetailModel) {

        // 正常な画面遷移でなかった場合
        if (administratorAuthDetailModel.getAdminAuthGroupSeq() == null) {
            throwMessage(BAD_TRANSITION);
        }
    }

    /**
     * アクション実行前処理
     *
     * @param administratorAuthDetailModel 権限グループ詳細画面
     * @param redirectAttributes           the redirect attributes
     * @param model                        the model
     * @return string
     */
    public String preDoAction(AdministratorAuthDetailModel administratorAuthDetailModel,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // 不正操作チェック
        if (!IdenticalDataCheckUtil.checkIdentical(
                        administratorAuthDetailModel.getScSeq(), administratorAuthDetailModel.getDbSeq())) {
            addMessage(IdenticalDataCheckUtil.MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/administrator/auth";
        }
        return null;
    }
}