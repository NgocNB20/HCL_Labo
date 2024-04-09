/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.regist;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorExistCheckResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorRegistRequest;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 新規運営者登録・確認コントローラー<
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/administrator/regist")
@Controller
@SessionAttributes(value = "administratorRegistModel")
@PreAuthorize("hasAnyAuthority('ADMIN:8')")
public class AdministratorRegistController extends AbstractController {

    /**
     * メッセージコード：不正操作
     */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AYO000603";

    private static final String MSGCD_ADMINISTRATOR_ID_MULTI = "LKA000201";

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "inputMd";

    /**
     * 表示モード:「new」の場合
     */
    public static final String MODE_NEW = "new";

    /**
     * 確認画面から
     */
    public static final String FLASH_FROM_CONFIRM = "fromConfirm";

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorRegistController.class);

    /**
     * 新規運営者登録ページhelper
     */
    private final AdministratorRegistHelper administratorRegistHelper;

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
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param administratorRegistHelper 新規運営者登録ページhelper
     * @param administratorApi          運営者のAPI
     * @param authorizationApi          運営者/権限のAPI
     */
    @Autowired
    public AdministratorRegistController(AdministratorRegistHelper administratorRegistHelper,
                                         AdministratorApi administratorApi,
                                         AuthorizationApi authorizationApi,
                                         ConversionUtility conversionUtility) {
        this.administratorRegistHelper = administratorRegistHelper;
        this.administratorApi = administratorApi;
        this.authorizationApi = authorizationApi;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期処理
     *
     * @return 新規運営者登録画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/regist/index")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> inputMd,
                                 AdministratorRegistModel administratorRegistModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (!model.containsAttribute(FLASH_FROM_CONFIRM)) {
            clearModel(AdministratorRegistModel.class, administratorRegistModel, model);
        }

        initComponentValue(administratorRegistModel, error);

        if (error.hasErrors()) {
            return "administrator/regist/index";
        }

        administratorRegistModel.setNormality(true);

        String md = null;
        if (model.containsAttribute(FLASH_MD)) {
            md = (String) model.getAttribute(FLASH_MD);
        } else if (inputMd.isPresent()) {
            md = inputMd.get();
        }

        if (MODE_NEW.equals(md)) {
            // ページ情報の初期処理
            administratorRegistHelper.clearPage(administratorRegistModel);
        }

        // 画面初期描画時のデフォルト値を設定
        administratorRegistHelper.setDefaultValueForLoad(administratorRegistModel);

        return "administrator/regist/index";
    }

    /**
     * キャンセルボタン押下処理
     *
     * @return 運営者検索画面
     */
    @PostMapping(value = "/", params = "doCancel")
    public String doCancel(AdministratorRegistModel administratorRegistModel,
                           RedirectAttributes redirectAttrs,
                           Model model) {
        // 検索条件復元用情報をセットし、運営者検索画面に遷移
        redirectAttrs.addFlashAttribute(FLASH_MD, MODE_LIST);
        return "redirect:/administrator/";
    }

    /**
     * 確認ページへ<
     *
     * @return 新規運営者登録確認
     */
    @PostMapping(value = "/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/regist/index")
    public String doConfirm(@Validated AdministratorRegistModel administratorRegistModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (error.hasErrors()) {
            return "administrator/regist/index";
        }

        // 不正操作チェック
        if (!administratorRegistModel.isNormality()) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/administrator/";
        }

        // 状態チェック
        if (EnumTypeUtil.getEnumFromValue(
                        HTypeAdministratorStatus.class, administratorRegistModel.getAdministratorStatus()) == null) {
            throwMessage(AdministratorRegistModel.MSGCD_ADMINISTRATOR_STATUS_ERROR);
        }

        // 運営者情報データチェックサービス実行
        administratorRegistHelper.buildAdministratorRegistModelForConfirm(administratorRegistModel);

        try {
            AdministratorExistCheckResponse administratorExistCheckResponse =
                            administratorApi.checkExist(administratorRegistModel.getAdministratorId());

            if (administratorExistCheckResponse.getAdministratorFlag()) {
                throwMessage(MSGCD_ADMINISTRATOR_ID_MULTI);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "administrator/regist/index";
        }

        return "redirect:/administrator/regist/confirm";

    }

    /**
     * プルダウンアイテム情報を取得
     *
     * @param administratorRegistModel フリーエリア登録・更新画面
     * @param error                    エラー
     */
    private void initComponentValue(AdministratorRegistModel administratorRegistModel, BindingResult error) {
        administratorRegistModel.setAdministratorStatusItems(EnumTypeUtil.getEnumMap(HTypeAdministratorStatus.class));

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

            administratorRegistModel.setAdministratorGroupSeqItems(adminAuthGroupMap);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 初期処理<
     *
     * @return 自画面
     */
    @GetMapping(value = "/confirm")
    protected String doLoadConfirm(AdministratorRegistModel administratorRegistModel,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        // ブラウザバックの場合、処理しない
        if (administratorRegistModel == null) {
            return "redirect:/error";
        }

        // 不正操作チェック
        if (checkInput(administratorRegistModel)) {
            return "redirect:/error";
        }

        // 利用開始/終了日情報を画面に反映
        administratorRegistHelper.toPageForLoad(null, administratorRegistModel);

        // 自画面
        return "administrator/regist/confirm";
    }

    /**
     * 新規運営者登録入力画面へ<
     *
     * @return 新規運営者登録入力画面
     */
    @PostMapping(value = "/confirm", params = "doIndex")
    public String doIndex(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(FLASH_FROM_CONFIRM, true);
        // 入力画面へ
        return "redirect:/administrator/regist/";
    }

    /**
     * 運営者情報登録処理<
     *
     * @return 運営者詳細画面
     */
    @PostMapping(value = "/confirm", params = "doOnceFinishRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/administrator/regist/")
    public String doOnceFinishRegist(AdministratorRegistModel administratorRegistModel,
                                     BindingResult error,
                                     RedirectAttributes redirectAttrs,
                                     SessionStatus sessionStatus,
                                     Model model) {

        // 運営者エンティティ情報の作成
        AdministratorRegistRequest administratorRegistRequest =
                        administratorRegistHelper.toAdministratorRegistRequestForRegist(administratorRegistModel);

        // ログ出力用
        String administratorId = getCommonInfo().getCommonInfoAdministrator().getAdministratorId();
        String adminId = administratorRegistRequest.getAdministratorId();

        try {
            // 運営者登録サービスの実行
            administratorApi.regist(administratorRegistRequest);

            // 検索条件復元用情報をセットし、運営者検索画面に遷移
            redirectAttrs.addFlashAttribute(FLASH_MD, MODE_LIST);
            // ログ出力
            LOGGER.warn("[運営者操作]操作運営者ID:" + administratorId + " 対象ID:" + adminId + " 操作:登録" + " 処理結果:成功");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // ログ出力
            LOGGER.warn("[運営者操作]操作運営者ID:" + administratorId + " 対象ID:" + adminId + " 操作:登録" + " 処理結果:失敗");

            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/administrator/regist/";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/administrator/";
    }

    /**
     * 必須項目を全てチェックし、不正遷移かどうかをチェック<
     *
     * @return true=不正、false=正常
     */
    protected boolean checkInput(AdministratorRegistModel administratorRegistModel) {

        // ID
        if (administratorRegistModel.getAdministratorId() == null) {
            return true;
        }

        // PASSWORD
        if (administratorRegistModel.getAdministratorPassword() == null) {
            return true;
        }

        // 氏名(姓)
        if (administratorRegistModel.getAdministratorLastName() == null) {
            return true;
        }

        // フリガナ(セイ)
        if (administratorRegistModel.getAdministratorLastKana() == null) {
            return true;
        }

        // 状態
        if (administratorRegistModel.getAdministratorStatus() == null) {
            return true;
        }

        // グループ
        if (administratorRegistModel.getAdministratorGroupSeq() == null) {
            return true;
        }

        return false;
    }

}