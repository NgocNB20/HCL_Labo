/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorListGetRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorListResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import java.util.Optional;

/**
 * 運営者検索・詳細・削除確認コントローラー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/administrator")
@Controller
@SessionAttributes(value = "administratorModel")
@PreAuthorize("hasAnyAuthority('ADMIN:4')")
public class AdministratorController extends AbstractController {

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "inputMd";

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * 表示モード(md):new
     */
    public static final String MODE_NEW = "new";

    /**
     * デフォルト：ソート項目
     */
    private static final String DEFAULT_ORDER_FIELD = "administratorId";

    /**
     * デフォルト：ソート項目
     */
    private static final boolean DEFAULT_ORDER_ASC = true;

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorController.class);

    /**
     * 運営者検索ページHelper<br/>
     */
    private final AdministratorHelper administratorHelper;

    /**
     * 運営者API
     */
    private final AdministratorApi administratorApi;

    /**
     * コンストラクター
     *
     * @param administratorHelper 運営者検索ページHelper
     * @param administratorApi    運営者API
     */
    @Autowired
    public AdministratorController(AdministratorHelper administratorHelper, AdministratorApi administratorApi) {
        this.administratorHelper = administratorHelper;
        this.administratorApi = administratorApi;
    }

    /**
     * 運営者検索：初期表示
     *
     * @param administratorModel 運営者検索ページ
     * @param error              エラー
     * @param mode               実行モード
     * @param model              model
     * @return
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/index")
    protected String doLoadIndex(AdministratorModel administratorModel,
                                 BindingResult error,
                                 @RequestParam(required = false) String mode,
                                 Model model) {

        // 再検索以外か判定
        if (!StringUtils.equals(MODE_LIST, mode)) {
            // モデル初期化
            clearModel(AdministratorModel.class, administratorModel, model);
            // 初期ソートセット
            administratorModel.setOrderField(DEFAULT_ORDER_FIELD);
            administratorModel.setOrderAsc(DEFAULT_ORDER_ASC);
        }
        search(administratorModel, error);

        return "administrator/index";
    }

    /**
     * 検索処理<br/>
     *
     * @param administratorModel 運営者検索ページ
     * @param error              エラー
     */
    protected void search(AdministratorModel administratorModel, BindingResult error) {

        try {
            // 取得
            AdministratorListGetRequest administratorListGetRequest = new AdministratorListGetRequest();

            administratorListGetRequest.setOrderField(administratorModel.getOrderField());
            administratorListGetRequest.setOrderAsc(administratorModel.isOrderAsc());

            AdministratorListResponse administratorListResponse =
                            administratorApi.get(administratorListGetRequest, null);

            // ページにセット
            administratorHelper.toPageForSearch(administratorListResponse, administratorModel);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 検索<br/>
     *
     * @param administratorModel 運営者検索ページ
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doSearch")
    public String doSearch(AdministratorModel administratorModel) {
        return "administrator/index";
    }

    /**
     * 表示切替<br/>
     *
     * @param administratorModel 運営者検索ページ
     * @param error              エラー
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/index")
    public String doDisplayChange(AdministratorModel administratorModel, BindingResult error) {
        search(administratorModel, error);
        return "administrator/index";
    }

    /**
     * 運営者登録
     *
     * @param administratorModel 運営者検索ページ
     * @param redirectAttributes
     * @return 運営者登録ページ
     */
    @PreAuthorize("hasAnyAuthority('ADMIN:8')")
    @PostMapping(value = "/", params = "doAdministratorRegist")
    public String doAdministratorRegist(AdministratorModel administratorModel, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(FLASH_MD, MODE_NEW);
        return "redirect:/administrator/regist/";
    }

    /**
     * 初期処理
     *
     * @param administratorSeq   運営者SEQ
     * @param administratorModel 運営者検索ページ
     * @param error              エラー
     * @param model              model
     * @return 運営者詳細
     */
    @GetMapping(value = "/details")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/administrator/")
    protected String doLoadConfirm(@RequestParam(required = false) Optional<Integer> administratorSeq,
                                   AdministratorModel administratorModel,
                                   BindingResult error,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        // 運営者SEQ必須の画面です。
        if (administratorSeq.isEmpty()) {
            addMessage(AdministratorModel.MSGCD_ADMINISTRATOR_NO_DATA, redirectAttributes, model);
            return "redirect:/administrator/";
        }

        administratorModel.setAdministratorSeq(administratorSeq.get());

        AdministratorEntity administratorEntity;
        try {
            // 運営者詳細取得サービス実行
            AdministratorResponse administratorResponse =
                            administratorApi.getByAdministratorSeq(administratorModel.getAdministratorSeq());
            administratorEntity =
                            administratorHelper.toAdministratorEntityFromAdministratorResponse(administratorResponse);
            // ページに反映
            administratorHelper.toPageForLoadDetails(administratorEntity, administratorModel);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 運営者詳細情報が取得できなかった
            throwMessage(AdministratorModel.MSGCD_ADMINISTRATOR_NO_DATA);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/administrator/";
        }

        return "administrator/details";
    }

    /**
     * 一覧に戻る
     *
     * @param administratorModel 運営者検索ページ
     * @param redirectAttrs
     * @return 管理者情報一覧画面
     */
    @PostMapping(value = "/details", params = "doAdministratorBack")
    public String doAdministratorBack(AdministratorModel administratorModel, RedirectAttributes redirectAttrs) {
        // 検索条件復元用情報をセットし、運営者検索画面に遷移
        redirectAttrs.addFlashAttribute(FLASH_MD, MODE_LIST);
        return "redirect:/administrator/";
    }

    /**
     * 初期処理
     *
     * @param administratorModel 運営者検索ページ
     * @param error              エラー
     * @param model              model
     * @return 運営者削除確認
     */
    @PreAuthorize("hasAnyAuthority('ADMIN:8')")
    @GetMapping(value = "/deleteConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/deleteConfirm")
    protected String doLoadDeleteConfirm(@RequestParam(required = false) Optional<String> administratorSeq,
                                         AdministratorModel administratorModel,
                                         BindingResult error,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {

        // 運営者SEQを取得
        if (administratorSeq.isPresent()) {
            administratorModel.setAdministratorSeq(Integer.parseInt(administratorSeq.get()));
        }

        // 運営者SEQ必須の画面です。
        if (administratorModel.getAdministratorSeq() == null) {
            return "redirect:/error";
        }

        AdministratorEntity administratorDetailsEntity;
        try {
            // 運営者詳細取得サービス実行
            AdministratorResponse administratorResponse =
                            administratorApi.getByAdministratorSeq(administratorModel.getAdministratorSeq());
            administratorDetailsEntity =
                            administratorHelper.toAdministratorEntityFromAdministratorResponse(administratorResponse);
            // ページに反映
            administratorHelper.toPageForLoadDeleteConfirm(administratorDetailsEntity, administratorModel);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 運営者詳細情報が取得できなかった
            addMessage(AdministratorModel.MSGCD_ADMINISTRATOR_NO_DATA, redirectAttributes, model);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        // 自画面
        return "administrator/deleteConfirm";
    }

    /**
     * 運営者情報削除処理
     *
     * @param administratorModel 運営者検索ページ
     * @param error              エラー
     * @param redirectAttrs
     * @param sessionStatus
     * @param model              model
     * @return 運営者検索画面
     */
    @PreAuthorize("hasAnyAuthority('ADMIN:8')")
    @PostMapping(value = "/deleteConfirm", params = "doOnceFinishDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "administrator/deleteConfirm")
    public String doOnceFinishDelete(AdministratorModel administratorModel,
                                     BindingResult error,
                                     RedirectAttributes redirectAttrs,
                                     SessionStatus sessionStatus,
                                     Model model) {

        AdministratorEntity administratorEntity = administratorModel.getAdministratorEntity();

        // ログ出力用
        String administratorId = getCommonInfo().getCommonInfoAdministrator().getAdministratorId();
        String adminId = administratorEntity.getAdministratorId();

        Integer administratorSeq = getCommonInfo().getCommonInfoAdministrator().getAdministratorSeq();
        Integer adminSeq = administratorEntity.getAdministratorSeq();

        try {
            // 運営者登録サービスの実行
            administratorApi.delete(adminSeq);

            // 検索条件復元用情報をセットし、運営者検索画面に遷移
            redirectAttrs.addFlashAttribute(FLASH_MD, MODE_LIST);

            // ログ出力
            LOGGER.warn("[運営者操作]操作運営者ID:" + administratorId + " 対象ID:" + adminId + " 操作:削除" + " 処理結果:成功");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // ログ出力
            LOGGER.warn("[運営者操作]操作運営者ID:" + administratorId + " 対象ID:" + adminId + " 操作:削除" + " 処理結果:失敗");
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "administrator/deleteConfirm";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // セッションクリアし、ログアウト画面へ遷移する
        if (administratorSeq.equals(adminSeq)) {
            SecurityContextHolder.clearContext();
        }

        return "redirect:/administrator/";
    }

}