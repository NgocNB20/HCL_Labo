/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.authupdate;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthType;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.pc.web.admin.administrator.auth.AdministratorAuthDetailHelper;
import jp.co.itechh.quad.admin.pc.web.admin.util.IdenticalDataCheckUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationCheckDataGetRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationResponse;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationUpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 権限グループ登録画面用 Action クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/administrator/authupdate")
@Controller
@SessionAttributes(value = "authUpdateModel")
@PreAuthorize("hasAnyAuthority('ADMIN:8')")
public class AuthUpdateController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUpdateController.class);

    /**
     * メッセージコード定数：更新しようとした権限グループは存在しない
     */
    protected static final String GROUP_NOT_FOUND = "AYP001004";

    /**
     * メッセージコード定数：登録しようとした権限グループは既に存在している
     */
    protected static final String GROUP_ALREADY_EXISTS = "AYP001001";

    /**
     * メッセージコード：更新完了
     */
    protected static final String UPDATE_COMPLETED = "AYP001006";

    /**
     * メッセージコード：権限SEQ必須
     */
    protected static final String MSGCD_ILLEGAL_ADMIN_AUTH = "CHECK-IDENTICALDATA-INVALID-E";

    /**
     * 確認画面から
     */
    public static final String FLASH_FROM_CONFIRM = "fromConfirm";

    /**
     * メタ権限情報。DICON で設定された値が自動バインドされる。必ず設定されている必要あり
     */
    // @Binding(bindingType = BindingType.MUST)
    private List<MetaAuthType> metaAuthList;

    /**
     * Helper
     */
    public AuthUpdateHelper authUpdateHelper;

    /**
     * 権限グループAPI
     **/
    private final AuthorizationApi authorizationApi;

    /**
     * 権限グループ詳細画面 Helper クラス
     **/
    private final AdministratorAuthDetailHelper administratorAuthDetailHelper;

    /**
     * コンストラクタ
     *
     * @param updateHelper                  権限グループ登録画面用 Dxo クラス
     * @param authorizationApi              権限グループAPI
     * @param administratorAuthDetailHelper 権限グループ詳細画面 Helper クラス
     */
    @Autowired
    public AuthUpdateController(AuthUpdateHelper updateHelper,
                                AuthorizationApi authorizationApi,
                                AdministratorAuthDetailHelper administratorAuthDetailHelper) {
        this.authUpdateHelper = updateHelper;
        this.authorizationApi = authorizationApi;
        this.administratorAuthDetailHelper = administratorAuthDetailHelper;
        this.metaAuthList = (List<MetaAuthType>) ApplicationContextUtility.getApplicationContext()
                                                                          .getBean("metaAuthTypeList");
    }

    /**
     * 初期表示アクション
     *
     * @param authUpdateModel    権限グループ登録画面用
     * @param error              エラー
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @return 遷移先クラス
     */
    @GetMapping("")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/authupdate/update")
    protected String doLoadIndex(AuthUpdateModel authUpdateModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (!model.containsAttribute(FLASH_FROM_CONFIRM)) {
            clearModel(AuthUpdateModel.class, authUpdateModel, model);
        }

        AdminAuthGroupEntity group;

        // authSeq を受け取っている場合は それを使用してエンティティを取得する
        if (model.containsAttribute("seq")) {
            String sq = (String) model.getAttribute("seq");
            clearModel(AuthUpdateModel.class, authUpdateModel, model);
            authUpdateModel.setSeq(StringUtil.isNotEmpty(sq) ? Integer.parseInt(sq) : null);
        }
        if (authUpdateModel.getSeq() != null) {
            Integer authSeq = authUpdateModel.getSeq();
            group = checkExistence(authSeq, error);

            if (error.hasErrors()) {
                return "redirect:/administrator/auth/";
            }

            authUpdateHelper.toPageForLoad(group, metaAuthList, authUpdateModel);

            // 不正操作対策の情報をセットする
            authUpdateModel.setScSeq(authSeq);
            authUpdateModel.setDbSeq(group.getAdminAuthGroupSeq());
        }
        // 権限SEQ必須の画面です。
        if (authUpdateModel.getScSeq() == null) {
            addMessage(MSGCD_ILLEGAL_ADMIN_AUTH, redirectAttributes, model);
            return "redirect:/administrator/auth/";
        }
        return "administrator/authupdate/update";
    }

    /**
     * 編集内容を破棄して権限詳細ページへ遷移
     *
     * @param authUpdateModel    権限グループ登録画面用
     * @param redirectAttributes the redirect attributes
     * @param sessionStatus      the session status
     * @return 遷移先ページクラス
     */
    @PostMapping(value = "/", params = "doCancel")
    public String doCancel(AuthUpdateModel authUpdateModel,
                           RedirectAttributes redirectAttributes,
                           SessionStatus sessionStatus) {
        redirectAttributes.addFlashAttribute("seq", authUpdateModel.getOriginalEntity().getAdminAuthGroupSeq());

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/administrator/auth/detail?seq=" + authUpdateModel.getOriginalEntity().getAdminAuthGroupSeq();
    }

    /**
     * 権限グループ登録確認画面へ遷移するアクション
     *
     * @param authUpdateModel    権限グループ登録画面用
     * @param error              the error
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @return 遷移先クラス
     */
    @PostMapping(value = "/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/authupdate/update")
    public String doConfirm(@Validated AuthUpdateModel authUpdateModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (error.hasErrors()) {
            return "administrator/authupdate/update";
        }
        // 実行前処理
        String check = preDoAction(authUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 対象レコード存在確認
        checkExistence(authUpdateModel.getOriginalEntity().getAdminAuthGroupSeq(), error);

        if (error.hasErrors()) {
            return "administrator/authupdate/update";
        }

        // 同一名グループの非存在を確認
        checkNotExistence(authUpdateModel, error);

        if (error.hasErrors()) {
            return "administrator/authupdate/update";
        }

        authUpdateHelper.setLevelName(authUpdateModel);

        return "redirect:/administrator/authupdate/confirm";
    }

    /**
     * 初期表示アクション
     *
     * @param authUpdateModel 権限グループ登録画面用
     * @return 遷移先ページ
     */
    @GetMapping("/confirm")
    protected String doLoadConfirm(AuthUpdateModel authUpdateModel) {

        // ブラウザバックの場合、処理しない
        if (StringUtils.isEmpty(authUpdateModel.getAuthGroupDisplayName())) {
            return "redirect:/administrator/auth";
        }

        //差分チェック用に、変更後データを作成
        authUpdateModel.setModifiedEntity(authUpdateHelper.toAdminAuthGroupEntityForUpdate(authUpdateModel));

        // 変更前データと変更後データの差異リスト作成
        List<String> modifiedList =
                        DiffUtil.diff(authUpdateModel.getOriginalEntity(), authUpdateModel.getModifiedEntity());
        authUpdateModel.setModifiedList(modifiedList);

        // 不正操作対策の情報をセットする
        authUpdateModel.setDbSeq(authUpdateModel.getOriginalEntity().getAdminAuthGroupSeq());
        return "administrator/authupdate/confirm";
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
        return "redirect:/administrator/authupdate";
    }

    /**
     * AdminAuthGroupEntity
     * 権限グループ登録アクション<br />
     *
     * @param authUpdateModel    権限グループ登録画面用
     * @param error              エラー
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @param sessionStatus      the session status
     * @return 遷移先ページ
     */
    @PostMapping(value = "/confirm", params = "doOnceUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/authupdate/confirm")
    public String doOnceUpdate(AuthUpdateModel authUpdateModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model,
                               SessionStatus sessionStatus) {

        // 更新対象エンティティが存在することを確認する
        checkExistenceConfirm(authUpdateModel, error);

        if (error.hasErrors()) {
            return "administrator/authupdate/confirm";
        }

        // 同名別権限グループが存在しないことを確認する
        checkNotExistenceConfirm(authUpdateModel, error);

        if (error.hasErrors()) {
            return "administrator/authupdate/confirm";
        }

        // ページ上にある入力情報から登録用 DTO を作成する
        AdminAuthGroupEntity group = authUpdateHelper.toAdminAuthGroupEntityForUpdate(authUpdateModel);

        // 更新する
        AuthorizationUpdateRequest authorizationUpdateRequest = authUpdateHelper.toAuthorizationUpdateRequest(group);

        try {
            authorizationApi.update(group.getAdminAuthGroupSeq(), authorizationUpdateRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "administrator/authupdate/confirm";
        }

        addInfoMessage(UPDATE_COMPLETED, null, redirectAttributes, model);

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/administrator/auth";
    }

    /**
     * 処理対象レコードの存在確認と取得
     *
     * @param authSeq 権限グループSEQ
     * @param error   エラー
     * @return 権限グループ
     */
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/administrator/auth")
    protected AdminAuthGroupEntity checkExistence(Integer authSeq, BindingResult error) {

        // 権限グループ情報とメタ権限情報をページへ設定する
        Integer shopSeq = getCommonInfo().getCommonInfoBase().getShopSeq();

        AdminAuthGroupEntity group = null;

        try {
            // 編集対象の権限グループ情報
            AuthorizationResponse authorizationResponse = authorizationApi.getByAdminAuthGroupSeq(authSeq);

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
     * 同名の別権限グループが存在しないことを確認する
     *
     * @param authUpdateModel 権限グループ登録画面用
     * @param error           エラー
     */
    protected void checkNotExistence(AuthUpdateModel authUpdateModel, BindingResult error) {

        String groupName = authUpdateModel.getAuthGroupDisplayName();

        AuthorizationCheckDataGetRequest authorizationCheckDataGetRequest = new AuthorizationCheckDataGetRequest();
        authorizationCheckDataGetRequest.setAuthGroupDisplayName(groupName);

        try {
            AuthorizationResponse authorizationResponse = authorizationApi.checkData(authorizationCheckDataGetRequest);

            // 同名の別権限グループが存在する場合
            if (authorizationResponse != null && !authorizationResponse.getAdminAuthGroupSeq()
                                                                       .equals(authUpdateModel.getOriginalEntity()
                                                                                              .getAdminAuthGroupSeq())) {
                throwMessage(GROUP_ALREADY_EXISTS);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

    }

    /**
     * 処理対象レコードの存在確認と取得
     *
     * @param authUpdateModel 権限グループ登録画面用
     * @return 運営者権限グループエンティティ
     */
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/administrator/auth")
    protected AdminAuthGroupEntity checkExistenceConfirm(AuthUpdateModel authUpdateModel, BindingResult error) {
        // 権限グループ情報とメタ権限情報をページへ設定する
        Integer shopSeq = getCommonInfo().getCommonInfoBase().getShopSeq();

        AdminAuthGroupEntity group = null;

        try {
            // 権限グループ情報取得
            AuthorizationResponse authorizationResponse = authorizationApi.getByAdminAuthGroupSeq(
                            authUpdateModel.getOriginalEntity().getAdminAuthGroupSeq());

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
     * 同名の別権限グループが存在しないことを確認する
     *
     * @param authUpdateModel 権限グループ登録画面用
     * @param error           エラー
     */
    protected void checkNotExistenceConfirm(AuthUpdateModel authUpdateModel, BindingResult error) {

        String groupName = authUpdateModel.getAuthGroupDisplayName();

        AuthorizationCheckDataGetRequest authorizationCheckDataGetRequest = new AuthorizationCheckDataGetRequest();
        authorizationCheckDataGetRequest.setAuthGroupDisplayName(groupName);

        try {
            AuthorizationResponse authorizationResponse = authorizationApi.checkData(authorizationCheckDataGetRequest);

            // 同名の別権限グループが存在する場合
            if (authorizationResponse != null && !authorizationResponse.getAdminAuthGroupSeq()
                                                                       .equals(authUpdateModel.getOriginalEntity()
                                                                                              .getAdminAuthGroupSeq())) {
                throwMessage(GROUP_ALREADY_EXISTS);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

    }

    /**
     * アクション実行前処理
     *
     * @param authUpdateModel    権限グループ登録画面用
     * @param redirectAttributes the redirect attributes
     * @param model              the model
     * @return string
     */
    public String preDoAction(AuthUpdateModel authUpdateModel, RedirectAttributes redirectAttributes, Model model) {
        // 不正操作チェック
        if (!IdenticalDataCheckUtil.checkIdentical(authUpdateModel.getScSeq(), authUpdateModel.getDbSeq())) {
            addMessage(IdenticalDataCheckUtil.MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/administrator/auth/";
        }
        return null;
    }
}