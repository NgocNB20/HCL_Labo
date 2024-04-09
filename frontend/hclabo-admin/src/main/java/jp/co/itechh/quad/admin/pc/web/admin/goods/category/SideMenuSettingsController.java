package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.MessageUtils;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.ValidatorMessage;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.PreviewGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.category.validation.SideMenuSettingsValidator;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.HeaderParamsHelper;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.PreviewAccessKeyResponse;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.menu.presentation.api.MenuApi;
import jp.co.itechh.quad.menu.presentation.api.param.MenuResponse;
import jp.co.itechh.quad.menu.presentation.api.param.MenuUpdateRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * サイドメニュー設定コントローラ
 *
 * @author Doan Thang (VJP)
 */
@RequestMapping("/goods/category")
@Controller
@SessionAttributes(value = "sideMenuSettingsModel")
@PreAuthorize("hasAnyAuthority('GOODS:8')")
public class SideMenuSettingsController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SideMenuSettingsController.class);

    /** サイドメニュー設定バリデータ */
    private final SideMenuSettingsValidator sideMenuSettingsValidator;

    /** メニューApi */
    private final MenuApi menuApi;

    /** カテゴリーApi */
    private final CategoryApi categoryApi;

    /** 権限グループAPI **/
    private final AuthorizationApi authorizationApi;

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /** 日付関連ユーティリティ */
    private final DateUtility dateUtility;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /** サイドメニュー設定ヘルパー */
    private final SideMenuSettingsHelper sideMenuSettingsHelper;

    /** コンストラクタ */
    @Autowired
    public SideMenuSettingsController(SideMenuSettingsValidator sideMenuSettingsValidator,
                                      MenuApi menuApi,
                                      CategoryApi categoryApi,
                                      AuthorizationApi authorizationApi,
                                      ConversionUtility conversionUtility,
                                      DateUtility dateUtility,
                                      HeaderParamsHelper headerParamsHelper,
                                      SideMenuSettingsHelper sideMenuSettingsHelper) {
        this.sideMenuSettingsValidator = sideMenuSettingsValidator;
        this.menuApi = menuApi;
        this.categoryApi = categoryApi;
        this.authorizationApi = authorizationApi;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.headerParamsHelper = headerParamsHelper;
        this.sideMenuSettingsHelper = sideMenuSettingsHelper;
    }

    /**
     * 動的バリデータ初期化
     *
     * @param error WebDataBinder
     */
    @InitBinder(value = "sideMenuSettingsModel")
    public void initBinder(WebDataBinder error) {
        // 動的バリデータをセット
        error.addValidators(sideMenuSettingsValidator);
    }

    /**
     * 初期表示
     *
     * @param sideMenuSettingsModel サイドメニュー設定モデル
     * @param model モデル
     * @return サイドメニュー設定画面
     */
    @GetMapping("/sidemenusettings/")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/category/")
    public String doLoad(SideMenuSettingsModel sideMenuSettingsModel, BindingResult error, Model model) {

        // モデルクリア
        clearModel(SideMenuSettingsModel.class, sideMenuSettingsModel, model);

        // 登録種別取得
        String registType = PropertiesUtil.getSystemPropertiesValue("side-menu.regist.type");
        sideMenuSettingsModel.setRegistType(
                        Objects.requireNonNullElse(registType, SideMenuSettingsHelper.SIDE_MENU_REGIST_TYPE_DEFAULT));

        // カテゴリーリスト取得
        CategoryListGetRequest categoryListGetRequest = new CategoryListGetRequest();
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        pageInfoRequest.setPage(1);
        pageInfoRequest.setLimit(Integer.MAX_VALUE);
        pageInfoRequest.setOrderBy("categoryId");
        pageInfoRequest.setSort(true);
        CategoryListResponse categoryListResponse = null;
        try {
            categoryListResponse = categoryApi.get(categoryListGetRequest, pageInfoRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors() || ObjectUtils.isEmpty(categoryListResponse)) {
            return "redirect:/goods/category/";
        }

        // メニューレスポンス取得
        MenuResponse menuResponse = null;
        try {
            menuResponse = menuApi.getMenu();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors() || ObjectUtils.isEmpty(menuResponse)) {
            return "redirect:/goods/category/";
        }

        try {
            sideMenuSettingsHelper.setCategoryTreeDtoList(sideMenuSettingsModel, menuResponse);
        } catch (JsonProcessingException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("SIDE-MENU-003-E");
        }

        List<SideMenuSettingsCategoryItem> categoryItemList =
                        sideMenuSettingsHelper.getCategoryItemList(registType, categoryListResponse,
                                                                   sideMenuSettingsModel.getCategoryTreeDtoList()
                                                                  );
        sideMenuSettingsModel.setSideMenuSettingsCategoryItemList(categoryItemList);

        // doLoad呼び出し時点のサイドメニュー構成1件目のカテゴリーIDを保持
        if (CollectionUtil.isNotEmpty(sideMenuSettingsModel.getCategoryTreeDtoList())) {
            sideMenuSettingsModel.setCid(sideMenuSettingsModel.getCategoryTreeDtoList().get(0).getCategoryId());
            // プレビュー日時設定
            setPreviewForm(sideMenuSettingsModel);
        }

        return "goods/category/sidemenusettings";
    }

    /**
     * サイドメニュー設定更新
     *
     * @param sideMenuSettingsModel サイドメニュー設定モデル
     * @param error BindingResult
     * @param redirectAttributes リダイレクト
     * @param model モデル
     * @return サイドメニュー設定画面
     */
    @PostMapping(value = "/sidemenusettings/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/category/sidemenusettings")
    public String doConfirm(@Validated(ConfirmGroup.class) SideMenuSettingsModel sideMenuSettingsModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (error.hasErrors()) {
            return "goods/category/sidemenusettings";
        }

        MenuUpdateRequest menuUpdateRequest = new MenuUpdateRequest();
        menuUpdateRequest.setCategoryTreeJson(sideMenuSettingsModel.getSideMenuSettingsList());

        try {
            MenuResponse menuResponse = menuApi.update(menuUpdateRequest);
            if (menuResponse != null) {
                addMessage("SIDE-MENU-001-I", redirectAttributes, model);
            } else {
                addMessage("SIDE-MENU-002-E", redirectAttributes, model);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage("SIDE-MENU-002-E", redirectAttributes, model);
        }

        return "redirect:/goods/category/sidemenusettings/";
    }

    /**
     * プレビュー表示イベント<br/>
     *
     * @param sideMenuSettingsModel
     * @param model
     * @return プレビュー遷移用の値設定後の画面Model
     */
    @PostMapping(value = "/sidemenu/doPreviewAjax")
    @ResponseBody
    public ResponseEntity<?> doPreviewAjax(@Validated(PreviewGroup.class) SideMenuSettingsModel sideMenuSettingsModel,
                                           BindingResult error,
                                           Model model) {

        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors());
        }
        if (StringUtils.isBlank(sideMenuSettingsModel.getPreviewTime())) {
            //　日付のみ入力の場合、時間を設定
            sideMenuSettingsModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
        }

        // doLoad呼び出し時に設定したcidと、現在のサイドメニュー1件目のカテゴリIDが一致するかチェック
        // メニューレスポンス取得
        MenuResponse menuResponse = null;
        try {
            menuResponse = menuApi.getMenu();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors() || ObjectUtils.isEmpty(menuResponse)) {
            return ResponseEntity.internalServerError().body("/error");
        }
        // jsonを変換
        ObjectMapper objectMapper =
                        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CategoryTreeDto categoryTreeDto = null;
        try {
            categoryTreeDto = objectMapper.readValue(menuResponse.getCategoryTreeJson(), CategoryTreeDto.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("例外処理が発生しました", e);
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(sideMenuSettingsModel.getCid()) || ObjectUtils.isEmpty(categoryTreeDto)
            || !sideMenuSettingsModel.getCid()
                                     .equals(categoryTreeDto.getCategoryTreeDtoList().get(0).getCategoryId())) {
            // doLoad呼び出し時に設定したcidと、現在のサイドメニュー1件目のカテゴリIDが一致しないためエラー
            // 想定内のエラー（400番台）の場合
            List<ValidatorMessage> messageList = new ArrayList<>();
            MessageUtils.getAllMessage(messageList, "SIDE-MENU-004-", null);
            return ResponseEntity.badRequest().body(messageList);
        }

        // ここからプレビュー用処理開始
        try {
            // 管理者SEQをヘッダーに設定
            Integer seq = getCommonInfo().getCommonInfoAdministrator().getAdministratorSeq();
            this.headerParamsHelper.setAdminSeq(ObjectUtils.isEmpty(seq) ? null : seq.toString());

            PreviewAccessKeyResponse response = this.authorizationApi.issuePreviewAccessKey();
            if (ObjectUtils.isEmpty(response) || StringUtils.isEmpty(response.getPreviewAccessKey())) {
                return ResponseEntity.internalServerError().body("/error");
            }
            sideMenuSettingsModel.setPreKey(response.getPreviewAccessKey());
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            return ResponseEntity.internalServerError().body("/error");
        }
        // Timestampに変換後、指定の書式（yyyyMMddHHmmss）に変換
        Timestamp tmp = this.dateUtility.toTimestampValue(
                        sideMenuSettingsModel.getPreviewDate() + " " + sideMenuSettingsModel.getPreviewTime(),
                        this.dateUtility.YMD_SLASH_HMS
                                                         );
        sideMenuSettingsModel.setPreTime(this.dateUtility.format(tmp, this.dateUtility.YMD_HMS));
        return ResponseEntity.ok(sideMenuSettingsModel);
    }

    /**
     * プレビュー日時が未設定の場合、現在日時を画面にセット
     *
     * @param sideMenuSettingsModel
     */
    protected void setPreviewForm(SideMenuSettingsModel sideMenuSettingsModel) {

        if (StringUtils.isBlank(sideMenuSettingsModel.getPreviewDate())) {
            sideMenuSettingsModel.setPreviewDate(this.dateUtility.getCurrentYmdWithSlash());
            sideMenuSettingsModel.setPreviewTime(this.dateUtility.getCurrentHMS());
        }
        // 日付は設定されているが、時刻未設定の場合
        else if (StringUtils.isNotBlank(sideMenuSettingsModel.getPreviewDate()) && StringUtils.isBlank(
                        sideMenuSettingsModel.getPreviewTime())) {
            sideMenuSettingsModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
        }
    }

}