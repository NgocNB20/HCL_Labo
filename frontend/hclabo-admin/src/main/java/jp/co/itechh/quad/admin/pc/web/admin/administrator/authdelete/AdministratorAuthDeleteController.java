/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.authdelete;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthType;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.pc.web.admin.administrator.auth.AdministratorAuthDetailHelper;
import jp.co.itechh.quad.admin.pc.web.admin.util.IdenticalDataCheckUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 権限グループ削除確認画面 Action クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/administrator/auth/delete")
@Controller
@SessionAttributes(value = "administratorAuthDeleteModel")
@PreAuthorize("hasAnyAuthority('ADMIN:8')")
public class AdministratorAuthDeleteController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorAuthDeleteController.class);

    /**
     * メッセージコード：不正遷移
     */
    protected static final String BAD_TRANSITION = "AYP001003";

    /**
     * メッセージコード：権限グループ使用中
     */
    protected static final String GROUP_IN_USE = "AYP001002";

    /**
     * メッセージコード：該当権限グループなし
     */
    protected static final String GROUP_NOT_FOUND = "AYP001004";

    /**
     * メッセージコード：削除完了
     */
    protected static final String DELETE_COMPLETED = "AYP001007";

    /**
     * The constant FLASH_SEQ.
     */
    public static final String FLASH_SEQ = "seq";

    /**
     * Helper
     */
    public AdministratorAuthDeleteHelper administratorAuthDeleteHelper;

    /**
     * メタ権限情報。DICON で設定された値が自動バインドされる。必ず設定されている必要あり
     */
    public List<MetaAuthType> metaAuthList;

    /**
     * 権限グループAPI
     **/
    private final AuthorizationApi authorizationApi;

    /**
     * 権限グループ詳細画面 Helper クラス
     */
    private final AdministratorAuthDetailHelper administratorAuthDetailHelper;

    /**
     * コンストラクタ
     *
     * @param administratorAuthDeleteHelper 権限グループ削除確認画面 Dxo クラス
     * @param authorizationApi              権限グループAPI
     * @param administratorAuthDetailHelper 権限グループ詳細画面 Helper クラス
     */
    @Autowired
    public AdministratorAuthDeleteController(AdministratorAuthDeleteHelper administratorAuthDeleteHelper,
                                             AuthorizationApi authorizationApi,
                                             AdministratorAuthDetailHelper administratorAuthDetailHelper) {
        this.administratorAuthDeleteHelper = administratorAuthDeleteHelper;
        this.authorizationApi = authorizationApi;
        this.administratorAuthDetailHelper = administratorAuthDetailHelper;
        this.metaAuthList = (List<MetaAuthType>) ApplicationContextUtility.getApplicationContext()
                                                                          .getBean("metaAuthTypeList");
    }

    /**
     * 初期表示処理
     *
     * @param administratorAuthDeleteModel 権限グループ削除確認画面
     * @param error                        the error
     * @param redirectAttrs                the redirect attrs
     * @param model                        the model
     * @return 遷移先ページ
     */
    @GetMapping(value = "")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/authdelete/delete")
    protected String doLoadIndex(AdministratorAuthDeleteModel administratorAuthDeleteModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttrs,
                                 Model model) {

        // リダイレクトスコープの authSeq が消える前にコピー
        if (model.containsAttribute("seq")) {
            administratorAuthDeleteModel.setAdminAuthGroupSeq((String) model.getAttribute(FLASH_SEQ));
        }
        // if (administratorAuthDeleteModel.getSeq() != null) {
        // administratorAuthDeleteModel.setAdminAuthGroupSeq(administratorAuthDeleteModel.getSeq().toString());
        // }

        // 不正遷移チェック
        checkBadTransition(administratorAuthDeleteModel);

        // 対象レコード存在確認
        AdminAuthGroupEntity group = checkExistence(administratorAuthDeleteModel, error);

        if (error.hasErrors()) {
            return "redirect:/administrator/auth";
        }

        // ページ初期化
        administratorAuthDeleteHelper.toPageForLoad(group, metaAuthList, administratorAuthDeleteModel);

        return "administrator/authdelete/delete";
    }

    /**
     * 権限詳細ページへ遷移
     *
     * @param administratorAuthDeleteModel 権限グループ削除確認画面
     * @return 遷移先ページクラス
     */
    @PostMapping(value = "/", params = "doGoBack")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/authdelete/delete")
    public String doGoBack(AdministratorAuthDeleteModel administratorAuthDeleteModel) {

        // 不正遷移チェック
        checkBadTransition(administratorAuthDeleteModel);

        administratorAuthDeleteModel.seq = Integer.parseInt(administratorAuthDeleteModel.getAdminAuthGroupSeq());

        return "redirect:/administrator/auth/detail";
    }

    /**
     * 権限グループ削除実行
     *
     * @param administratorAuthDeleteModel 権限グループ削除確認画面
     * @param error                        エラー
     * @param redirectAttributes           the redirect attributes
     * @param model                        the model
     * @return 遷移先ページクラス
     */
    @PostMapping(value = "/", params = "doOnceDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/authdelete/delete")
    @HEHandler(exception = DataIntegrityViolationException.class, returnView = "redirect:/administrator/auth",
               messageCode = GROUP_IN_USE)
    public String doOnceDelete(AdministratorAuthDeleteModel administratorAuthDeleteModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        // 実行前処理
        String check = preDoAction(administratorAuthDeleteModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 不正遷移チェック
        checkBadTransition(administratorAuthDeleteModel);

        // 対象レコード存在確認
        AdminAuthGroupEntity group = checkExistence(administratorAuthDeleteModel, error);

        if (error.hasErrors()) {
            return "administrator/authdelete/delete";
        }

        try {
            authorizationApi.delete(group.getAdminAuthGroupSeq());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "administrator/authdelete/delete";
        }

        addInfoMessage(DELETE_COMPLETED, null, redirectAttributes, model);

        return "redirect:/administrator/auth";
    }

    /**
     * 処理対象レコードの存在確認と取得
     *
     * @param administratorAuthDeleteModel 権限グループ削除確認画面
     * @param error                        エラー
     * @return 運営者権限グループエンティティ
     */
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/administrator/auth")
    protected AdminAuthGroupEntity checkExistence(AdministratorAuthDeleteModel administratorAuthDeleteModel,
                                                  BindingResult error) {

        // 権限グループ情報とメタ権限情報をページへ設定する
        Integer shopSeq = getCommonInfo().getCommonInfoBase().getShopSeq();

        AdminAuthGroupEntity group = null;

        try {
            // 削除対象の権限グループ情報
            AuthorizationResponse authorizationResponse = authorizationApi.getByAdminAuthGroupSeq(
                            Integer.valueOf(administratorAuthDeleteModel.getAdminAuthGroupSeq()));

            group = administratorAuthDetailHelper.toAdminAuthGroupEntity(authorizationResponse, shopSeq);

            // 対象の権限グループが存在しない場合
            if (group == null) {
                throwMessage(GROUP_NOT_FOUND);
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        return group;
    }

    /**
     * 不正遷移チェック
     *
     * @param administratorAuthDeleteModel 権限グループ削除確認画面
     */
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/administrator/auth")
    protected void checkBadTransition(AdministratorAuthDeleteModel administratorAuthDeleteModel) {

        // 正常な画面遷移でなかった場合
        if (administratorAuthDeleteModel.getAdminAuthGroupSeq() == null) {
            throwMessage(BAD_TRANSITION);
        }
    }

    /**
     * アクション実行前処理
     *
     * @param administratorAuthDeleteModel 権限グループ削除確認画面
     * @param redirectAttributes           the redirect attributes
     * @param model                        the model
     * @return string
     */
    public String preDoAction(AdministratorAuthDeleteModel administratorAuthDeleteModel,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // 不正操作チェック
        if (!IdenticalDataCheckUtil.checkIdentical(
                        administratorAuthDeleteModel.getScSeq(), administratorAuthDeleteModel.getDbSeq())) {
            addMessage(IdenticalDataCheckUtil.MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/administrator/auth";
        }
        return null;
    }
}