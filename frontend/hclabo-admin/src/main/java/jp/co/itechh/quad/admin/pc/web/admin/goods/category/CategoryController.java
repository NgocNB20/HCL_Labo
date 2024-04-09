/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionColumnType;
import jp.co.itechh.quad.admin.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.PreviewGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.HeaderParamsHelper;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.PreviewAccessKeyResponse;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.category.presentation.api.param.PageInfoResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * カテゴリー検索
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */

@RequestMapping("/goods/category")
@Controller
@SessionAttributes(value = "categoryModel")
@PreAuthorize("hasAnyAuthority('GOODS:4')")
public class CategoryController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    /**
     * デフォルトページ番号
     */
    private static final String DEFAULT_PNUM = "1";

    /**
     * デフォルト：ソート項目
     */
    private static final String DEFAULT_ORDER_FIELD = "categoryId";

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * デフォルト：ソート条件(昇順/降順)
     */
    private static final boolean DEFAULT_ORDER_ASC = true;

    /**
     * カテゴリー検索Helper
     */
    private final CategoryHelper categoryHelper;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 日付関連ユーティリティ */
    private final DateUtility dateUtility;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /** カテゴリーApi */
    private final CategoryApi categoryApi;

    /** 権限グループAPI **/
    private final AuthorizationApi authorizationApi;

    /** コンストラクタ */
    @Autowired
    public CategoryController(CategoryHelper categoryHelper,
                              ConversionUtility conversionUtility,
                              DateUtility dateUtility,
                              HeaderParamsHelper headerParamsHelper,
                              CategoryApi categoryApi,
                              AuthorizationApi authorizationApi) {
        this.categoryHelper = categoryHelper;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.headerParamsHelper = headerParamsHelper;
        this.categoryApi = categoryApi;
        this.authorizationApi = authorizationApi;
    }

    /**
     * 初期表示
     *
     * @param categoryModel          カテゴリー検索
     * @param error                  エラー
     * @param model                  Model
     * @return 遷移先ページ
     */
    @GetMapping("/")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/category/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                              CategoryModel categoryModel,
                              BindingResult error,
                              Model model) {

        if (error.hasErrors()) {
            return "goods/category/index";
        }

        // 再検索の場合
        if (md.isPresent() || model.containsAttribute(FLASH_MD)) {
            // ページング関連項目初期化（limitは画面プルダウンで指定されてくる）
            categoryModel.setPageNumber(DEFAULT_PNUM);
            categoryModel.setOrderField(DEFAULT_ORDER_FIELD);
            categoryModel.setOrderAsc(DEFAULT_ORDER_ASC);
            // 再検索を実行
            search(categoryModel, error, model);
            if (error.hasErrors()) {
                return "goods/category/index";
            }
        } else {
            clearModel(CategoryModel.class, categoryModel, model);
        }

        // プルダウンアイテム情報を取得`
        initDropDownValue(categoryModel);

        return "goods/category/index";
    }

    /**
     * フリーエリア検索
     *
     * @param categoryModel          カテゴリー検索
     * @param error                  エラー
     * @param model                  Model
     * @return 遷移先ページ
     */
    @PostMapping(value = "/", params = "doSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/category/index")
    public String doSearch(@Validated(SearchGroup.class) CategoryModel categoryModel,
                           BindingResult error,
                           Model model) {

        if (error.hasErrors()) {
            return "goods/category/index";
        }

        // ページング関連項目初期化（limitは画面プルダウンで指定されてくる）
        categoryModel.setPageNumber(DEFAULT_PNUM);
        categoryModel.setOrderField(DEFAULT_ORDER_FIELD);
        categoryModel.setOrderAsc(DEFAULT_ORDER_ASC);

        // 検索
        search(categoryModel, error, model);

        return "goods/category/index";
    }

    /**
     * 表示順変更
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/category/index")
    public String doDisplayChange(@Validated(DisplayChangeGroup.class) CategoryModel categoryModel,
                                  BindingResult error,
                                  Model model) {
        if (error.hasErrors()) {
            return "goods/category/index";
        }

        // 検索
        search(categoryModel, error, model);
        return "goods/category/index";
    }

    /**
     * 削除
     *
     * @param categoryModel
     * @param redirectAttributes
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('GOODS:8')")
    @PostMapping(value = "/", params = "doRemove")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/category/index")
    public String doRemove(CategoryModel categoryModel,
                           BindingResult error,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (error.hasErrors()) {
            return "goods/category/index";
        }

        // カテゴリSEQパス(画面受渡し用)に追加
        CategoryItem deleteTarget = categoryModel.getResultItems().get(Integer.valueOf(categoryModel.getResultIndex()));

        // カテゴリID
        String categoryId = deleteTarget.getCategoryId();
        // 削除処理
        try {
            categoryApi.deleteCategories(categoryId);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage("AGC000007", redirectAttributes, model);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "goods/category/index";
        }

        addInfoMessage("AGC000010", null, redirectAttributes, model);

        search(categoryModel, error, model);

        return "goods/category/index";
    }

    /**
     * カテゴリー登録画面へ遷移
     *
     * @return カテゴリー登録画面
     */
    @PostMapping(value = "/", params = "doRegist")
    public String doRegist() {
        return "redirect:/goods/category/input/";
    }

    /**
     * サイドメニュー設定コントローラ画面へ遷移画面へ遷移
     *
     * @return サイドメニュー設定コントローラ画面へ遷移画面
     */
    @PostMapping(value = "/", params = "sideMenuSetting")
    public String sideMenuSetting() {
        return "redirect:/goods/category/sidemenusettings/";
    }

    /**
     * ダイアログからのプレビューイベント AJAX<br/>
     *
     * @param categoryModel カテゴリー検索
     * @param error
     * @param model
     * @return プレビュー遷移用の値設定後の画面Model
     */
    @PostMapping(value = "doConfirmPreviewAjax")
    @ResponseBody
    public ResponseEntity<?> doConfirmPreviewAjax(@Validated(PreviewGroup.class) CategoryModel categoryModel,
                                                  BindingResult error,
                                                  Model model) {
        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors());
        }
        // ダイアログフォームから詰め替え
        categoryModel.setPreviewDate(categoryModel.getDialogPreviewDate());
        categoryModel.setPreviewTime(categoryModel.getDialogPreviewTime());
        return settingPreviewInfo(categoryModel);
    }

    /**
     * 検索結果から直接呼び出されるプレビューイベント AJAX<br/>
     *
     * @param categoryModel カテゴリー検索
     * @param model
     * @return プレビュー遷移用の値設定後の画面Model
     */
    @PostMapping(value = "doPreviewAjax")
    @ResponseBody
    public ResponseEntity<?> doPreviewAjax(CategoryModel categoryModel, Model model) {
        return settingPreviewInfo(categoryModel);
    }

    /**
     * プレビュー用Ajaxから呼び出されるプレビュー設定用の共通メソッド
     *
     * @param categoryModel カテゴリー検索
     * @return プレビュー遷移用の値設定後の画面Model
     */
    private ResponseEntity<?> settingPreviewInfo(CategoryModel categoryModel) {

        if (StringUtils.isBlank(categoryModel.getPreviewTime())) {
            //　日付のみ入力の場合、時間を設定
            categoryModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
        }

        try {
            // 管理者SEQをヘッダーに設定
            Integer seq = getCommonInfo().getCommonInfoAdministrator().getAdministratorSeq();
            this.headerParamsHelper.setAdminSeq(ObjectUtils.isEmpty(seq) ? null : seq.toString());

            PreviewAccessKeyResponse response = this.authorizationApi.issuePreviewAccessKey();
            if (ObjectUtils.isEmpty(response) || StringUtils.isEmpty(response.getPreviewAccessKey())) {
                return ResponseEntity.internalServerError().body("/error");
            }
            categoryModel.setPreKey(response.getPreviewAccessKey());
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            return ResponseEntity.internalServerError().body("/error");
        }
        // Timestampに変換後、指定の書式（yyyyMMddHHmmss）に変換
        Timestamp tmp = this.dateUtility.toTimestampValue(
                        categoryModel.getPreviewDate() + " " + categoryModel.getPreviewTime(),
                        this.dateUtility.YMD_SLASH_HMS
                                                         );
        categoryModel.setPreTime(this.dateUtility.format(tmp, this.dateUtility.YMD_HMS));
        return ResponseEntity.ok(categoryModel);
    }

    /**
     * プルダウン情報を列挙クラス（HTypeクラス）ベースで生成
     *
     * @param categoryModel カテゴリー検索
     */
    protected void initDropDownValue(CategoryModel categoryModel) {

        categoryModel.setOpenStatusItems(EnumTypeUtil.getEnumMap(HTypeOpenStatus.class));
        categoryModel.setSearchCategoryTypeItems(EnumTypeUtil.getEnumMap(HTypeCategoryType.class));
        categoryModel.setConditionColumnItems(EnumTypeUtil.getEnumMap(HTypeConditionColumnType.class));
        categoryModel.setFrontDisplayItems(EnumTypeUtil.getEnumMap(HTypeFrontDisplayStatus.class));
    }

    /**
     * 検索処理
     *
     * @param categoryModel カテゴリー検索
     * @param error         エラー
     */
    private void search(CategoryModel categoryModel, BindingResult error, Model model) {

        try {
            // 条件取得
            CategoryListGetRequest categoryListGetRequest =
                            categoryHelper.toCategoryListGetRequestForSearch(categoryModel);
            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            pageInfoHelper.setupPageRequest(pageInfoRequest, Integer.parseInt(categoryModel.getPageNumber()),
                                            categoryModel.getLimit(), categoryModel.getOrderField(),
                                            categoryModel.isOrderAsc()
                                           );
            // 検索
            CategoryListResponse categoryListResponse = categoryApi.get(categoryListGetRequest, pageInfoRequest);

            // ページへ反映
            categoryHelper.toPageForSearch(categoryListResponse, categoryModel);
            // ページャーにレスポンス情報をセット
            PageInfoResponse pageInfoResponse = categoryListResponse.getPageInfo();
            pageInfoHelper.setupPageInfo(categoryModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                         pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                         pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages()
                                        );
            // ページャーセットアップ
            pageInfoHelper.setupViewPager(categoryModel.getPageInfo(), categoryModel);

            // 件数セット
            categoryModel.setTotalCount(Objects.requireNonNull(categoryListResponse.getCategoryList()).size());

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }
}