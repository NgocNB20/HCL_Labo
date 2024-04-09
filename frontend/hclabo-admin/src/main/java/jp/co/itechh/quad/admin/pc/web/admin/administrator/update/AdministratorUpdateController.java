/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.update;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.CopyUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.admin.pc.web.admin.administrator.AdministratorHelper;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorSameCheckResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorUpdateRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorsSameCheckRequest;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationListResponse;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationSubResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 運営者情報変更入力・確認画面コントローラー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/administrator/update")
@Controller
@SessionAttributes(value = "administratorUpdateModel")
@PreAuthorize("hasAnyAuthority('ADMIN:8')")
public class AdministratorUpdateController extends AbstractController {

    /**
     * 運営者更新ページ
     */
    public static final String FLASH_UPDATE_MODEL = "administratorUpdateModel";

    /**
     * 運営者確認ページ
     */
    public static final String FLASH_UPDATE_CONFIRM_MODEL = "updateConfirmModel";

    /**
     * 確認画面から
     */
    public static final String FLASH_FROM_CONFIRM = "fromConfirm";

    /**
     * メッセージコード：不正操作
     */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AYO000304";

    protected static final String MSGCD_ILLEGAL_OPERATION_ADMIN = "AYO000302";

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorUpdateController.class);

    private static final String MSGCD_ADMINISTRATOR_UPDATE_FAIL = "LKA000802";

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * 新規運営者登録ページHelper
     */
    private final AdministratorUpdateHelper helper;

    /**
     * 運営者検索ページHelper
     */
    private final AdministratorHelper administratorHelper;

    /**
     * 運営者のAPI
     */
    private final AdministratorApi administratorApi;

    /**
     * 運営者/権限のAPI
     */
    private final AuthorizationApi authorizationApi;

    /**
     * 変換ユーティリティクラス
     **/
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param helper              新規運営者登録ページHelper
     * @param administratorHelper 運営者検索ページHelper
     * @param administratorApi    運営者のAPI
     * @param authorizationApi    運営者/権限のAPI
     */
    @Autowired
    public AdministratorUpdateController(AdministratorUpdateHelper helper,
                                         AdministratorHelper administratorHelper,
                                         AdministratorApi administratorApi,
                                         AuthorizationApi authorizationApi,
                                         ConversionUtility conversionUtility) {
        this.helper = helper;
        this.administratorHelper = administratorHelper;
        this.administratorApi = administratorApi;
        this.authorizationApi = authorizationApi;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期処理
     *
     * @return 新規運営者登録画面
     */
    @GetMapping("")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/update/index")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> administratorSeq,
                                 AdministratorUpdateModel administratorUpdateModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (!model.containsAttribute(FLASH_FROM_CONFIRM)) {
            clearModel(AdministratorUpdateModel.class, administratorUpdateModel, model);
        }

        initComponentValue(administratorUpdateModel, error);

        if (error.hasErrors()) {
            return "administrator/update/index";
        }

        // 詳細画面から
        if (administratorSeq.isPresent()) {
            administratorUpdateModel.setAdministratorSeq(Integer.parseInt(administratorSeq.get()));
        }

        // 確認画面から
        if (model.containsAttribute(FLASH_UPDATE_CONFIRM_MODEL)) {
            administratorUpdateModel = (AdministratorUpdateModel) model.getAttribute(FLASH_UPDATE_CONFIRM_MODEL);
        }

        // 運営者SEQ必須の画面です。
        if (administratorUpdateModel == null || administratorUpdateModel.getAdministratorSeq() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION_ADMIN, redirectAttributes, model);
            return "redirect:/administrator/";
        }

        // 確認画面からの遷移の場合は、セッション情報を表示
        if (administratorUpdateModel.isEditFlag()) {
            // 必須項目がある場合 自画面表示 ない場合再取得
            if (!checkInput(administratorUpdateModel)) {
                // ページに反映
                helper.toPageForLoad(administratorUpdateModel.getModifiedEntity(), administratorUpdateModel);

                // 確認画面フラグを初期化
                administratorUpdateModel.setEditFlag(false);

                return "administrator/update/index";
            }

            // 確認画面フラグを初期化
            administratorUpdateModel.setEditFlag(false);
        }

        AdministratorEntity administratorEntity;
        try {
            // 運営者詳細取得サービス実行
            AdministratorResponse administratorResponse =
                            administratorApi.getByAdministratorSeq(administratorUpdateModel.getAdministratorSeq());
            administratorEntity =
                            administratorHelper.toAdministratorEntityFromAdministratorResponse(administratorResponse);
            // ページに反映
            helper.toPageForLoad(administratorEntity, administratorUpdateModel);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 運営者詳細情報が取得できなかった
            addMessage(AdministratorUpdateModel.MSGCD_ADMINISTRATOR_NO_DATA, redirectAttributes, model);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "redirect:/administrator/";
        }

        return "administrator/update/index";
    }

    /**
     * キャンセルボタン押下処理
     *
     * @return 運営者検索画面
     */
    @PostMapping(value = "/", params = "doCancel")
    public String doCancel(AdministratorUpdateModel administratorUpdateModel) {
        administratorUpdateModel.setAdministratorSeq(
                        administratorUpdateModel.getOriginalEntity().getAdministratorSeq());
        return "redirect:/administrator/details?administratorSeq=" + administratorUpdateModel.getAdministratorSeq();
    }

    /**
     * 運営者修正確認画面
     *
     * @return 運営者修正確認画面
     */
    @PostMapping(value = "/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/update/index")
    public String doConfirm(@Validated AdministratorUpdateModel administratorUpdateModel,
                            BindingResult error,
                            RedirectAttributes redirectAttrs,
                            Model model) {

        if (error.hasErrors()) {
            return "administrator/update/index";
        }

        // 不正操作が行われたかどうか
        if (checkInput(administratorUpdateModel)) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttrs, model);
            return "redirect:/administrator/";
        }

        // 状態チェック
        if (EnumTypeUtil.getEnumFromValue(
                        HTypeAdministratorStatus.class, administratorUpdateModel.getAdministratorStatus()) == null) {
            throwMessage(AdministratorUpdateModel.MSGCD_ADMINISTRATOR_STATUS_ERROR);
        }

        // 画面から運営者詳細DTOに変換
        AdministratorEntity modifiedEntity =
                        helper.toAdministratorEntityFromAdministratorUpdateModel(administratorUpdateModel);

        // 同一ユーザを更新しようとしているか確認する
        AdministratorsSameCheckRequest administratorsSameCheckRequest = new AdministratorsSameCheckRequest();
        administratorsSameCheckRequest.setAdministratorSeq(modifiedEntity.getAdministratorSeq());
        administratorsSameCheckRequest.setUserId(modifiedEntity.getAdministratorId());

        try {
            AdministratorSameCheckResponse administratorSameCheckResponse =
                            administratorApi.checkDuplicate(administratorsSameCheckRequest);

            if (Boolean.FALSE.equals(administratorSameCheckResponse.getAdministratorFlag())) {
                this.throwMessage(MSGCD_ADMINISTRATOR_UPDATE_FAIL);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("userId", "administratorId");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "administrator/update/index";
        }

        if (administratorUpdateModel.getAdministratorPassword() != null) {
            modifiedEntity.setPasswordNeedChangeFlag(HTypePasswordNeedChangeFlag.ON);
        }
        // 運営者詳細DTOをページに設定
        administratorUpdateModel.setModifiedEntity(modifiedEntity);

        redirectAttrs.addFlashAttribute(FLASH_UPDATE_MODEL, administratorUpdateModel);

        // 運営者修正確認画面へ遷移
        return "redirect:/administrator/update/confirm";
    }

    /**
     * 必須項目を全てチェック
     *
     * @return true=不正、false=正常
     */
    protected boolean checkInput(AdministratorUpdateModel administratorUpdateModel) {
        if (administratorUpdateModel.getModifiedEntity() == null) {
            return true;
        }

        return false;
    }

    /**
     * コンポーネント値初期化
     *
     * @param administratorUpdateModel 運営者変更ページ
     * @param error                    エラー
     */
    private void initComponentValue(AdministratorUpdateModel administratorUpdateModel, BindingResult error) {
        administratorUpdateModel.setAdministratorStatusItems(EnumTypeUtil.getEnumMap(HTypeAdministratorStatus.class));

        try {
            AuthorizationListResponse authorizationListResponse = authorizationApi.get(null);

            Map<String, String> adminAuthGroupMap = new HashMap<>();

            if (CollectionUtil.isNotEmpty(authorizationListResponse.getAuthorizationResponseList())) {
                adminAuthGroupMap = authorizationListResponse.getAuthorizationResponseList()
                                                             .stream()
                                                             .sorted(Comparator.comparingInt(
                                                                             AuthorizationSubResponse::getAdminAuthGroupSeq))
                                                             .collect(Collectors.toMap(
                                                                             item -> conversionUtility.toString(
                                                                                             item.getAdminAuthGroupSeq()),
                                                                             AuthorizationSubResponse::getAuthGroupDisplayName,
                                                                             (oldValue, newValue) -> oldValue,
                                                                             LinkedHashMap::new
                                                                                      ));
            }

            administratorUpdateModel.setAdministratorGroupSeqItems(adminAuthGroupMap);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 初期処理
     *
     * @return 自画面
     */
    @GetMapping("/confirm")
    public String doLoadConfirm(AdministratorUpdateModel administratorUpdateModel, Model model) {

        // 不正遷移チェック 必須項目の有無でエラーページへ遷移
        if (model.containsAttribute(FLASH_UPDATE_MODEL)) {
            administratorUpdateModel = (AdministratorUpdateModel) model.getAttribute(FLASH_UPDATE_MODEL);
        }

        // ブラウザバックの場合、処理しない
        if (administratorUpdateModel == null) {
            return "redirect:/error";
        }

        // 必須項目を全てチェックし、不正遷移かどうかをチェック
        if (checkInputConfirm(administratorUpdateModel)) {
            return "redirect:/error";
        }

        // 確認画面まできましたよ
        administratorUpdateModel.setEditFlag(true);

        // 入力情報を画面に反映
        helper.toPageForLoad(administratorUpdateModel.getModifiedEntity(), administratorUpdateModel);

        // 変更前データと変更後データの差異リスト作成
        List<String> modifiedList = DiffUtil.diff(administratorUpdateModel.getOriginalEntity(),
                                                  administratorUpdateModel.getModifiedEntity()
                                                 );
        administratorUpdateModel.setModifiedList(modifiedList);

        return "administrator/update/confirm";
    }

    /**
     * 新規運営者登録入力画面へ
     *
     * @return 新規運営者登録入力画面
     */
    @PostMapping(value = "/confirm", params = "doIndex")
    public String doIndex(AdministratorUpdateModel administratorUpdateModel, RedirectAttributes redirectAttrs) {
        // 入力画面へ
        redirectAttrs.addFlashAttribute(FLASH_UPDATE_CONFIRM_MODEL, administratorUpdateModel);
        redirectAttrs.addFlashAttribute(FLASH_FROM_CONFIRM, true);
        return "redirect:/administrator/update";
    }

    /**
     * 運営者情報登録処理
     *
     * @return 運営者詳細画面
     */
    @PostMapping(value = "/confirm", params = "doOnceFinishUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/administrator/update")
    public String doOnceFinishUpdate(AdministratorUpdateModel administratorUpdateModel,
                                     BindingResult error,
                                     RedirectAttributes redirectAttrs,
                                     SessionStatus sessionStatus,
                                     Model model) {

        // ロジックでエンティティ情報が修正されても影響されないようにエンティティのクローンを作成する
        // ※パスワードの暗号化等で必ずロジック内で修正される
        AdministratorEntity administratorEntity = CopyUtil.deepCopy(administratorUpdateModel.getModifiedEntity());

        // ログ出力用
        String administratorId = getCommonInfo().getCommonInfoAdministrator().getAdministratorId();
        String adminId = administratorEntity.getAdministratorId();

        try {
            // 運営者登録サービスの実行
            AdministratorUpdateRequest administratorUpdateRequest =
                            helper.toAdministratorUpdateRequestFromAdministratorEntity(administratorEntity);

            administratorApi.update(administratorEntity.getAdministratorSeq(), administratorUpdateRequest);

            // 検索条件復元用情報をセットし、運営者検索画面に遷移
            redirectAttrs.addFlashAttribute(FLASH_MD, MODE_LIST);

            // ログ出力
            LOGGER.warn("[運営者操作]操作運営者ID:" + administratorId + " 対象ID:" + adminId + " 操作:変更" + " 処理結果:成功");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // ログ出力
            LOGGER.warn("[運営者操作]操作運営者ID:" + administratorId + " 対象ID:" + adminId + " 操作:変更" + " 処理結果:失敗");
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/administrator/update";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/administrator/";
    }

    /**
     * 必須項目を全てチェックし、不正遷移かどうかをチェック
     *
     * @return true=不正、false=正常
     */
    protected boolean checkInputConfirm(AdministratorUpdateModel administratorUpdateModel) {

        // ID
        if (administratorUpdateModel.getAdministratorId() == null) {
            return true;
        }

        // 氏名(姓)
        if (administratorUpdateModel.getAdministratorLastName() == null) {
            return true;
        }

        // フリガナ(セイ)
        if (administratorUpdateModel.getAdministratorLastKana() == null) {
            return true;
        }

        // 状態
        if (administratorUpdateModel.getAdministratorStatus() == null) {
            return true;
        }

        // グループ
        if (administratorUpdateModel.getAdministratorGroupSeq() == null) {
            return true;
        }

        return false;
    }

}