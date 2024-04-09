package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.FileOperationUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatusWithNoDeleted;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.admin.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CategoryAndTagForGoodsRegistUpdateDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.AbstractGoodsRegistUpdateController;
import jp.co.itechh.quad.admin.pc.web.admin.goods.AbstractGoodsRegistUpdateModel;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.AddGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.DeleteGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.DownGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.UpGoodsGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.validation.group.UploadImageGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.validation.GoodsTagValidator;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.utility.GoodsUtility;
import jp.co.itechh.quad.admin.utility.ImageUtility;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.icon.presentation.api.IconApi;
import jp.co.itechh.quad.icon.presentation.api.param.IconListResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupRegistUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.GoodsRelationRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductCorrelationCheckRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductRegistUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductResponse;
import jp.co.itechh.quad.relation.presentation.api.RelationApi;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListGetRequest;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListResponse;
import jp.co.itechh.quad.tag.presentation.api.TagApi;
import jp.co.itechh.quad.tag.presentation.api.param.ProductTagListGetRequest;
import jp.co.itechh.quad.tag.presentation.api.param.ProductTagListResponse;
import jp.co.itechh.quad.tax.presentation.api.TaxApi;
import jp.co.itechh.quad.tax.presentation.api.param.TaxesResponse;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.ListUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品管理：商品登録更新（商品基本設定）コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Controller
@RequestMapping(value = "/goods/registupdate")
@SessionAttributes(value = "goodsRegistUpdateModel")
@PreAuthorize("hasAnyAuthority('GOODS:8')")
public class GoodsRegistUpdateController extends AbstractGoodsRegistUpdateController {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsRegistUpdateController.class);

    /**
     * 商品規格最大件数
     */
    protected static final int UNIT_COUNT_MAX = 9999;

    /**
     * 規格表示設定値最大値
     */
    protected static final int ORDERDISPLAY_MAX_VALUE = 9999;

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 画面リロード後の自動スクロールのターゲットポジション : 基本情報
     */
    public static final String ID_SCROLL_GOODS_UNIT_ITEMS = "autoScrollGoodsUnitItems";

    /**
     * 画面リロード後の自動スクロールのターゲットポジション : 関連商品設定
     */
    public static final String ID_SCROLL_GOODS_RELATION_ITEMS = "autoScrollGoodsRelationItems";

    /**
     * 確認画面から
     */
    public static final String FLASH_FROM_CONFIRM = "fromConfirm";

    /** デフォルトページ番号 */
    private static final Integer DEFAULT_PNUM = 1;

    /** タグ一覧 : デフォルトページ番号 */
    private static final Integer DEFAULT_TAG_PNUM = 1;

    /** タグ一覧 : １ページ当たりのデフォルト最大表示件数 */
    private static final Integer DEFAULT_TAG_LIMIT = 20;

    /** タグ一覧 : ソート項目 */
    private static final String DEFAULT_TAG_ORDER_FIELD = "tag";

    /** カテゴリ一一覧 : デフォルトページ番号 */
    private static final Integer DEFAULT_CATEGORY_PNUM = 1;

    /** カテゴリ一一覧 : １ページ当たりのデフォルト最大表示件数 */
    private static final Integer DEFAULT_CATEGORY_LIMIT = 20;

    /** カテゴリ一一覧 : ソート項目 */
    private static final String DEFAULT_CATEGORY_ORDER_FIELD = "categoryName";

    /**
     * 商品Utility取得
     */
    private final GoodsUtility goodsUtility;

    /**
     * 商品タグの動的バリデータ
     */
    private final GoodsTagValidator goodsTagValidator;

    /**
     * 商品API
     */
    private final ProductApi productApi;

    /**
     * 関連商品API
     */
    private final RelationApi relationApi;

    /**
     * 税API
     */
    private final TaxApi taxApi;

    /**
     * カテゴリAPI
     */
    private final CategoryApi categoryApi;

    /**
     * アイコンAPI
     */
    private final IconApi iconApi;

    /**
     * 商品登録更新確認ページHelper
     */
    private final GoodsRegistUpdateConfirmHelper goodsRegistUpdateConfirmHelper;

    /**
     * 商品登録更新（商品基本設定）ページDxo
     */
    private final GoodsRegistUpdateHelper goodsRegUpHelper;

    /**
     * 商品登録更新（商品基本設定）ページDxo
     */
    private final GoodsRegistUpdateHelper goodsRegistUpdateHelper;

    /**
     * タグAPI
     */
    private final TagApi tagApi;

    /** コンストラクタ */
    @Autowired
    public GoodsRegistUpdateController(GoodsUtility goodsUtility,
                                       ProductApi productApi,
                                       RelationApi relationApi,
                                       TaxApi taxApi,
                                       CategoryApi categoryApi,
                                       IconApi iconApi,
                                       GoodsRegistUpdateConfirmHelper goodsRegistUpdateConfirmHelper,
                                       GoodsRegistUpdateHelper goodsRegUpHelper,
                                       GoodsRegistUpdateHelper goodsRegistUpdateHelper,
                                       TagApi tagApi,
                                       GoodsTagValidator goodsTagValidator) {
        super(productApi, relationApi);
        this.goodsUtility = goodsUtility;
        this.productApi = productApi;
        this.relationApi = relationApi;
        this.taxApi = taxApi;
        this.categoryApi = categoryApi;
        this.iconApi = iconApi;
        this.goodsRegistUpdateConfirmHelper = goodsRegistUpdateConfirmHelper;
        this.goodsRegUpHelper = goodsRegUpHelper;
        this.goodsRegistUpdateHelper = goodsRegistUpdateHelper;
        this.tagApi = tagApi;
        this.goodsTagValidator = goodsTagValidator;
    }

    @InitBinder(value = "goodsRegistUpdateModel")
    public void initBinder(WebDataBinder error) {
        // 商品タグの動的バリデータ
        error.addValidators(goodsTagValidator);
    }

    /**
     * 画像表示処理<br/>
     * 初期表示用メソッド
     *
     * @param from                   the from
     * @param scroll                 the scroll
     * @param goodGroupCode          the good group code
     * @param md                     the md
     * @param recycle                the recycle
     * @param goodsRegistUpdateModel the goods regist update model
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return ページクラス string
     */
    @GetMapping("")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/registupdate/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> from,
                              @RequestParam(required = false) Optional<String> scroll,
                              @RequestParam(required = false) Optional<String> goodGroupCode,
                              @RequestParam(required = false) Optional<String> md,
                              @RequestParam(required = false) Optional<String> recycle,
                              GoodsRegistUpdateModel goodsRegistUpdateModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        Integer shopSeq = 1001;

        List<CategoryEntity> categoryEntityList = new ArrayList<>();

        if (goodsRegistUpdateModel.isReloadFlag() || (from.isPresent() && from.get().equals("confirm")) || (
                        goodGroupCode.isPresent() && goodGroupCode.get()
                                                                  .equals(goodsRegistUpdateModel.getGoodsGroupCode())
                        && md.isEmpty())) {

            goodsRegistUpdateModel.setReloadFlag(false);
            return "goods/registupdate/index";
        }

        // 画面表示用商品グループ取得レスポンス
        ProductDisplayResponse productDisplayResponse = new ProductDisplayResponse();

        if (goodGroupCode.isPresent()) {

            // 画面表示用商品グループ取得リクエスト
            ProductDisplayGetRequest productDisplayGetRequest =
                            goodsRegistUpdateHelper.toProductDisplayGetRequest(goodGroupCode.get());
            try {
                // 画面表示用商品グループ取得
                productDisplayResponse = productApi.getForDisplay(productDisplayGetRequest);

            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "goods/registupdate/index";
            }

            // ひもづいているカテゴリー
            categoryEntityList = goodsRegUpHelper.toCategoryEntityFromResponse(
                            Objects.requireNonNull(productDisplayResponse.getCategoryGoodsResponseList()), shopSeq);

            if (categoryEntityList.size() > 0) {
                // ひもづいているカテゴリ一覧の作成
                goodsRegistUpdateModel.setLinkedCategoryList(categoryEntityList);
            }

        }

        // コンポーネント値初期化
        initComponentValue(goodsRegistUpdateModel, error);
        if (error.hasErrors()) {
            return "goods/registupdate/index";
        }

        // キープセッションを確認してください
        if (scroll.isPresent() && scroll.get().equalsIgnoreCase("relation")) {
            model.addAttribute(FLASH_FROM_CONFIRM, true);
        }

        if (!model.containsAttribute(FLASH_FROM_CONFIRM)) {
            clearModel(GoodsRegistUpdateModel.class, goodsRegistUpdateModel, model);
            initComponentValue(goodsRegistUpdateModel, error);
        }
        if (error.hasErrors()) {
            return "goods/registupdate/index";
        }

        if (from.isPresent() || (recycle.isPresent() && StringUtils.isNotEmpty(recycle.get()))) {
            goodsRegistUpdateModel.setRegistFlg(true);
        }
        boolean fromRelation = false;
        if (scroll.isPresent() && "relation".equalsIgnoreCase(scroll.get())) {
            fromRelation = true;
        }
        if (goodGroupCode.isPresent() && md.isPresent()) {
            goodsRegistUpdateModel.setRedirectGoodsGroupCode(goodGroupCode.get());
            goodsRegistUpdateModel.setMd(md.get());
            goodsRegistUpdateModel.setRedirectRecycle(recycle.isPresent() ? recycle.get() : null);
            // 再利用の場合、不正操作チェック用項目値を初期化
            if ("1".equals(goodsRegistUpdateModel.getRedirectRecycle())) {
                goodsRegistUpdateModel.setScGoodsGroupSeq(null);
                if (goodsRegistUpdateModel.getGoodsGroupDto() != null
                    && goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity() != null) {
                    goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().setGoodsGroupSeq(null);
                }
                categoryEntityList = categoryEntityList.stream()
                                                       .filter(category -> category.getCategoryType()
                                                                           == HTypeCategoryType.NORMAL)
                                                       .collect(Collectors.toList());
            }
            goodsRegistUpdateModel.setInputingFlg(false);
        }

        if (!fromRelation && !model.containsAttribute(FLASH_FROM_CONFIRM)) {

            // 共通初期表示処理
            String errorClass =
                            loadPage(goodsRegistUpdateModel, error, goodsRegistUpdateHelper, redirectAttributes, model);
            if (error.hasErrors()) {
                return "goods/registupdate/index";
            }

            // 共通初期表示エラー時
            if (errorClass != null) {
                return errorClass;
            }
        }

        /***************************************************************************************************************************
         ** カテゴリー・商品タグ設定
         ***************************************************************************************************************************/
        if (goodGroupCode.isPresent()) {

            // ひもづいている商品タグ
            if (productDisplayResponse.getGoodsGroupDisplay() != null
                && productDisplayResponse.getGoodsGroupDisplay().getGoodsTag() != null) {
                goodsRegistUpdateModel.setGoodsTagList(productDisplayResponse.getGoodsGroupDisplay().getGoodsTag());
                goodsRegistUpdateModel.setOldGoodsTagList(productDisplayResponse.getGoodsGroupDisplay().getGoodsTag());
            }
        }

        /***************************************************************************************************************************
         ** 商品詳細設定
         ***************************************************************************************************************************/
        // 商品アイコン情報リスト取得
        if (goodsRegistUpdateModel.getIconList() == null) {
            try {
                // 検索
                IconListResponse iconListResponse = iconApi.getList(null);

                List<GoodsInformationIconDto> goodsInformationIconDtoList =
                                goodsRegUpHelper.toGoodsInformationIconDtoList(
                                                Objects.requireNonNull(iconListResponse.getIconList()), shopSeq);

                goodsRegistUpdateModel.setIconList(goodsInformationIconDtoList);
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "goods/registupdate/index";
            }
        }

        // 商品関連画面からページリロード不要
        if (!fromRelation && !model.containsAttribute(FLASH_FROM_CONFIRM)) {
            goodsRegistUpdateHelper.toPageForLoad(goodsRegistUpdateModel);
        }
        if (!fromRelation && categoryEntityList.size() > 0) {
            // ひもづいているカテゴリ一覧の作成
            goodsRegistUpdateModel.setLinkedCategoryList(categoryEntityList);
        }

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 自動スクロールのターゲットポジションをリセット
        if (scroll.isPresent()) {
            goodsRegistUpdateModel.setTargetAutoScrollTagId(ID_SCROLL_GOODS_RELATION_ITEMS);
        } else {
            goodsRegistUpdateModel.setTargetAutoScrollTagId("");
        }

        // 画面初期描画時のデフォルト値を設定
        goodsRegistUpdateHelper.setDefaultValueForLoad(goodsRegistUpdateModel);

        return "goods/registupdate/index";
    }

    /**
     * 商品タグリスト取得 （Ajax）<br/>
     *
     * @param goodsTagInput
     * @param orderField
     * @param limit
     * @return 商品タグリスト
     */
    @GetMapping(value = "/tags/ajax")
    @ResponseBody
    public ResponseEntity<ProductTagListResponse> getTagList(
                    @RequestParam(name = "goodsTag", required = false) String goodsTagInput,
                    @RequestParam(name = "orderField", required = false) String orderField,
                    @RequestParam(name = "limit", required = false) Integer limit,
                    @RequestParam(name = "page", required = false) Integer page) {

        // タグ一覧取得リクエスト
        ProductTagListGetRequest productTagListGetRequest = new ProductTagListGetRequest();
        if (goodsTagInput == null) {
            productTagListGetRequest.tagSearchKeyword("");
        } else {
            productTagListGetRequest.tagSearchKeyword(goodsTagInput);
        }

        jp.co.itechh.quad.tag.presentation.api.param.PageInfoRequest pageInfoRequest =
                        new jp.co.itechh.quad.tag.presentation.api.param.PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャー項目をセット
        pageInfoHelper.setupPageRequest(pageInfoRequest, page != null ? page : DEFAULT_TAG_PNUM,
                                        limit == null ? Integer.MAX_VALUE : limit,
                                        orderField == null ? DEFAULT_TAG_ORDER_FIELD : orderField, true
                                       );

        // 商品タグ一覧レスポンス
        ProductTagListResponse productTagListResponse = new ProductTagListResponse();
        try {
            // タグ一覧取得
            productTagListResponse = tagApi.get(productTagListGetRequest, pageInfoRequest);

        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        } catch (HttpClientErrorException ce) {
            LOGGER.error("商品タグリスト取得", ce);
        }
        return ResponseEntity.ok(productTagListResponse);
    }

    /**
     * カテゴリ一覧取得 （Ajax）<br/>
     *
     * @return カテゴリーリスト
     */
    @PostMapping(value = "/category/ajax")
    @ResponseBody
    public ResponseEntity<List<CategoryEntity>> getCategoryList(GoodsRegistUpdateModel goodsRegistUpdateModel) {

        Integer shopSeq = 1001;

        // カテゴリ一覧取得リクエスト
        CategoryListGetRequest categoryListGetRequest = new CategoryListGetRequest();
        if (goodsRegistUpdateModel.getCategorySearch() == null) {
            categoryListGetRequest.categorySearchKeyword("");
        } else {
            categoryListGetRequest.categorySearchKeyword(goodsRegistUpdateModel.getCategorySearch());
        }

        // 検索条件：カテゴリー種別＝手動
        List<String> categoryTypeList = new ArrayList<>();
        categoryTypeList.add(EnumTypeUtil.getValue(HTypeCategoryType.NORMAL));
        categoryListGetRequest.setCategoryType(categoryTypeList);

        jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest pageInfoRequest =
                        new jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャー項目をセット(並び順項目＝カテゴリー名,並び順＝true（昇順）,limit＝20件
        pageInfoHelper.setupPageRequest(pageInfoRequest, DEFAULT_CATEGORY_PNUM, DEFAULT_CATEGORY_LIMIT,
                                        DEFAULT_CATEGORY_ORDER_FIELD, true
                                       );

        // カテゴリ一覧取得レスポンス
        CategoryListResponse categoryListResponse = new CategoryListResponse();
        try {
            // 画面表示用商品グループ取得
            categoryListResponse = categoryApi.get(categoryListGetRequest, pageInfoRequest);

        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        } catch (HttpClientErrorException ce) {
            LOGGER.error("カテゴリ一覧取得", ce);
        }

        List<CategoryEntity> categoryEntityList =
                        goodsRegUpHelper.toCategoryEntityFromResponse(categoryListResponse, goodsRegistUpdateModel,
                                                                      shopSeq
                                                                     );
        goodsRegistUpdateModel.setCategoryEntityList(categoryEntityList);
        return ResponseEntity.ok(categoryEntityList);
    }

    /**
     * 選択されたカテゴリをモデルに更新 （Ajax）<br/>
     *
     * @return カテゴリーリスト
     */
    @PostMapping(value = "/category/update/ajax")
    @ResponseBody
    public ResponseEntity<?> updateSelectedCategoryList(
                    @RequestBody CategoryAndTagForGoodsRegistUpdateDto categoryChosenDto,
                    GoodsRegistUpdateModel goodsRegistUpdateModel) {

        if (CollectionUtil.isNotEmpty(categoryChosenDto.getCategoryChosenList())) {
            // 自動カテゴリを無視
            categoryChosenDto.getCategoryChosenList()
                             .stream()
                             .filter(item -> item.getCategoryType() != HTypeCategoryType.AUTO);

            goodsRegistUpdateModel.setLinkedCategoryList(categoryChosenDto.getCategoryChosenList());
        } else if (CollectionUtil.isEmpty(categoryChosenDto.getCategoryChosenList())) {
            goodsRegistUpdateModel.setLinkedCategoryList(new ArrayList<>());
        }

        return ResponseEntity.ok("Success");
    }

    /**
     * 選択された商品タグをモデルに更新 （Ajax）<br/>
     *
     * @return 商品タグーリスト
     */
    @PostMapping(value = "/goodstag/update/ajax")
    @ResponseBody
    public ResponseEntity<?> updateSelectedTagList(@RequestBody CategoryAndTagForGoodsRegistUpdateDto tagListChosenDto,
                                                   GoodsRegistUpdateModel goodsRegistUpdateModel,
                                                   RedirectAttributes redirectAttributes,
                                                   Model model) {

        if (CollectionUtil.isNotEmpty(tagListChosenDto.getTagChosenList())) {
            goodsRegistUpdateModel.setGoodsTagList(tagListChosenDto.getTagChosenList());
        } else if (CollectionUtil.isEmpty(tagListChosenDto.getTagChosenList())) {
            goodsRegistUpdateModel.setGoodsTagList(new ArrayList<>());
        }

        return ResponseEntity.ok("Success");
    }

    /**
     * 初期表示用メソッド(Ajax用)
     *
     * @param from                                 the from
     * @param goodsRegistUpdateModel               the goods regist update model
     * @param goodsRegistUpdateRelationSearchModel the goods regist update relationsearch model
     * @param redirectAttributes                   the redirect attributes
     * @param model                                the model
     * @return ページクラス response entity
     */
    @PostMapping("loadAjax")
    @ResponseBody
    public ResponseEntity<?> doLoadIndexAjax(@RequestParam(required = false) Optional<String> from,
                                             GoodsRegistUpdateModel goodsRegistUpdateModel,
                                             @RequestBody
                                                             GoodsRegistUpdateRelationSearchModel goodsRegistUpdateRelationSearchModel,
                                             RedirectAttributes redirectAttributes,
                                             Model model) {
        if (!ObjectUtils.isEmpty(goodsRegistUpdateRelationSearchModel)) {
            goodsRegistUpdateModel.setRedirectGoodsRelationEntityList(
                            goodsRegistUpdateRelationSearchModel.getRedirectGoodsRelationEntityList());
            goodsRegistUpdateModel.setTmpGoodsRelationEntityList(
                            goodsRegistUpdateRelationSearchModel.getTmpGoodsRelationEntityList());
            goodsRegistUpdateModel.setGoodsGroupDto(goodsRegistUpdateRelationSearchModel.getGoodsGroupDto());
            goodsRegistUpdateModel.setGoodsRelationEntityList(
                            goodsRegistUpdateRelationSearchModel.getGoodsRelationEntityList());
        }
        // 商品関連画面からページリロード不要
        if (!ObjectUtils.isEmpty(goodsRegistUpdateRelationSearchModel)) {
            goodsRegistUpdateHelper.toPageForLoadRelation(goodsRegistUpdateModel);
        }
        return ResponseEntity.ok("Success");
    }

    /**
     * 確認するイベント<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param error                  the error
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return 自画面 string
     */
    @PostMapping(value = "/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/registupdate/index")
    public String doConfirm(@Validated(ConfirmGroup.class) GoodsRegistUpdateModel goodsRegistUpdateModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // 自動スクロールのターゲットポジションをリセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId("");

        // チェックボックスにデフォルト値を設定
        initValueFlag(goodsRegistUpdateModel);

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "goods/registupdate/index";
        }

        // 不整合処理チェック
        checkPageGoodsGroupEntity(goodsRegistUpdateModel, redirectAttributes, model);
        checkData(goodsRegistUpdateModel, redirectAttributes, model);
        checkDataDetails(goodsRegistUpdateModel, redirectAttributes, model);
        checkDataUnit(goodsRegistUpdateModel, redirectAttributes, model);
        checkDataStock(goodsRegistUpdateModel, redirectAttributes, model);

        goodsRegistUpdateHelper.toPageForNext(goodsRegistUpdateModel);
        // 共通商品情報から個別商品情報へのデータ反映
        goodsRegistUpdateHelper.toGoodsListFromCommonGoods(goodsRegistUpdateModel);
        return "redirect:/goods/registupdate/confirm";
    }

    /**
     * アクション実行前処理
     *
     * @param abstractGoodsRegistUpdateModel the abstract goods regist update model
     * @param redirectAttributes             the redirect attributes
     * @param model                          the model
     * @return the string
     */
    public String preDoAction(AbstractGoodsRegistUpdateModel abstractGoodsRegistUpdateModel,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // 不正操作チェック
        return checkIllegalOperation(abstractGoodsRegistUpdateModel, redirectAttributes, model);
    }

    /**
     * 入力情報チェック<br/>
     * Validatorで対応できないもの
     *
     * @param goodsRegistUpdateModel
     * @param redirectAttributes
     * @param model
     */
    private void checkData(GoodsRegistUpdateModel goodsRegistUpdateModel,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        // 不整合処理チェック
        checkPageGoodsGroupEntity(goodsRegistUpdateModel, redirectAttributes, model);
        // 更新時の新着日時
        if (!goodsRegistUpdateModel.isRegist() && goodsRegistUpdateModel.getWhatsnewDate() == null) {
            addErrorMessage("AGG000207");
        }
        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /***************************************************************************************************************************
     ** 商品詳細設定
     ***************************************************************************************************************************/
    /**
     * 入力情報チェック<br/>
     * Validatorで対応できないもの
     *
     * @param detailsModel
     * @param redirectAttributes
     * @param model
     */
    private void checkDataDetails(GoodsRegistUpdateModel detailsModel,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        // 不整合処理チェック
        checkPageGoodsGroupEntity(detailsModel, redirectAttributes, model);

        for (GoodsRegistUpdateIconItem item : detailsModel.getGoodsInformationIconItems()) {
            if (item.isCheckBoxPc() && StringUtils.isEmpty(item.getColorCode())) {
                addErrorMessage("AGG000402", new Object[] {item.getIconName()});
            }
        }
        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /***************************************************************************************************************************
     ** 商品規格設定
     ***************************************************************************************************************************/
    /**
     * 商品規格追加処理<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param error                  the error
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return string
     */
    @PostMapping(value = "/", params = "doAddGoods")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/registupdate/index")
    public String doAddGoods(@Validated(AddGoodsGroup.class) GoodsRegistUpdateModel goodsRegistUpdateModel,
                             BindingResult error,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 画面リロード後の自動スクロールのターゲットポジションをセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId(ID_SCROLL_GOODS_UNIT_ITEMS);

        // goodsGroupDtoのバインディングエラーは一旦無視する（HTMLで在庫DTOのhidden項目を使っているため）
        // それ以外の項目は、自画面に遷移し入力エラーを表示する
        if (error.hasErrors()) {
            for (FieldError fieldError : error.getFieldErrors()) {
                if (!fieldError.getField().contains("goodsGroupDto")) {
                    return "goods/registupdate/index";
                }
            }
        }

        // 商品規格登録数チェック
        List<GoodsRegistUpdateUnitItem> unitItems = goodsRegistUpdateModel.getUnitItems();
        if (unitItems != null && unitItems.size() >= UNIT_COUNT_MAX) {
            // 商品規格登録件数が最大値（9999）以上の場合、規格追加不可エラー
            throwMessage("AGG000723", new Object[] {UNIT_COUNT_MAX});
        }

        // 規格表示順チェック
        // 商品規格リスト取得
        List<GoodsDto> goodsDtoList = goodsRegistUpdateModel.getGoodsGroupDto().getGoodsDtoList();
        if (goodsDtoList != null && !goodsDtoList.isEmpty()) {
            // 規格表示順設定値チェック
            // 商品規格最後尾のDto取得
            GoodsDto lastGoodsDto = goodsDtoList.get(goodsDtoList.size() - 1);
            if (lastGoodsDto.getGoodsEntity().getOrderDisplay().intValue() >= ORDERDISPLAY_MAX_VALUE) {
                // 規格表示順設定値が最大値（9999）以上の場合、規格追加不可エラー
                throwMessage("AGG000724", new Object[] {ORDERDISPLAY_MAX_VALUE + 1});
            }
        }

        // 変更情報の保存(同一画面内遷移)
        goodsRegistUpdateHelper.toPageForSame(goodsRegistUpdateModel);

        // 共通商品情報から個別商品情報へのデータ反映
        goodsRegistUpdateHelper.toGoodsListFromCommonGoods(goodsRegistUpdateModel);

        // 商品規格追加
        goodsRegistUpdateHelper.toPageForAddGoods(goodsRegistUpdateModel);

        // 画面再表示
        goodsRegistUpdateHelper.toPageForLoadUnit(goodsRegistUpdateModel);

        return "goods/registupdate/index";
    }

    /**
     * 商品規格削除処理<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param error                  the error
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return string
     */
    @PostMapping(value = "/", params = "doDeleteGoods")
    public String doDeleteGoods(@Validated(DeleteGoodsGroup.class) GoodsRegistUpdateModel goodsRegistUpdateModel,
                                BindingResult error,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 画面リロード後の自動スクロールのターゲットポジションをセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId(ID_SCROLL_GOODS_UNIT_ITEMS);

        if (error.hasErrors()) {
            return "goods/registupdate/index";
        }

        // 変更情報の保存(同一画面内遷移)
        goodsRegistUpdateHelper.toPageForSame(goodsRegistUpdateModel);

        // 共通商品情報から個別商品情報へのデータ反映
        goodsRegistUpdateHelper.toGoodsListFromCommonGoods(goodsRegistUpdateModel);

        // 商品規格削除
        goodsRegistUpdateHelper.toPageForDeleteGoods(goodsRegistUpdateModel);

        // 画面再表示
        goodsRegistUpdateHelper.toPageForLoadUnit(goodsRegistUpdateModel);

        return "goods/registupdate/index";
    }

    /**
     * 商品表示順変更(上)<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param error                  the error
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return string
     */
    @PostMapping(value = "/", params = "doUpGoods")
    public String doUpGoods(@Validated(UpGoodsGroup.class) GoodsRegistUpdateModel goodsRegistUpdateModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 画面リロード後の自動スクロールのターゲットポジションをセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId(ID_SCROLL_GOODS_UNIT_ITEMS);

        if (error.hasErrors()) {
            return "goods/registupdate/index";
        }

        // 変更情報の保存(同一画面内遷移)
        goodsRegistUpdateHelper.toPageForSame(goodsRegistUpdateModel);

        // 共通商品情報から個別商品情報へのデータ反映
        goodsRegistUpdateHelper.toGoodsListFromCommonGoods(goodsRegistUpdateModel);

        // 商品表示順変更(上)
        goodsRegistUpdateHelper.toPageForUpGoods(goodsRegistUpdateModel);

        // 画面再表示
        goodsRegistUpdateHelper.toPageForLoadUnit(goodsRegistUpdateModel);

        return "goods/registupdate/index";
    }

    /**
     * 商品表示順変更(下)<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param error                  the error
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return string
     */
    @PostMapping(value = "/", params = "doDownGoods")
    public String doDownGoods(@Validated(DownGoodsGroup.class) GoodsRegistUpdateModel goodsRegistUpdateModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 画面リロード後の自動スクロールのターゲットポジションをセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId(ID_SCROLL_GOODS_UNIT_ITEMS);

        if (error.hasErrors()) {
            return "goods/registupdate/index";
        }

        // 変更情報の保存(同一画面内遷移)
        goodsRegistUpdateHelper.toPageForSame(goodsRegistUpdateModel);

        // 共通商品情報から個別商品情報へのデータ反映
        goodsRegistUpdateHelper.toGoodsListFromCommonGoods(goodsRegistUpdateModel);

        // 商品表示順変更(下)
        goodsRegistUpdateHelper.toPageForDownGoods(goodsRegistUpdateModel);

        // 画面再表示
        goodsRegistUpdateHelper.toPageForLoadUnit(goodsRegistUpdateModel);

        return "goods/registupdate/index";
    }

    /**
     * 入力情報チェック<br/>
     * Validatorで対応できないもの
     *
     * @param goodsRegistUpdateModel
     * @param redirectAttributes
     * @param model
     */
    private void checkDataUnit(GoodsRegistUpdateModel goodsRegistUpdateModel,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        // 不整合処理チェック
        checkPageGoodsGroupEntity(goodsRegistUpdateModel, redirectAttributes, model);

        // 規格設定チェック
        checkGoodsUnit(goodsRegistUpdateModel);

        // 商品コード、カタログコードの重複チェック
        checkCodeDuplication(goodsRegistUpdateModel);

        // 規格管理ありの場合、規格名と規格画像のチェックを実施する
        if (HTypeUnitManagementFlag.ON.equals(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                            goodsRegistUpdateModel.getUnitManagementFlag()
                                                                           ))) {
            // 規格名重複チェック
            checkDuplicateUnitvalue(goodsRegistUpdateModel);
        }

        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /**
     * 規格名重複チェック
     *
     * @param goodsRegistUpdateModel
     */
    private void checkDuplicateUnitvalue(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        Set<String> unitvalues = new HashSet<>();
        for (GoodsRegistUpdateUnitItem item : goodsRegistUpdateModel.getUnitItems()) {
            // 規格1未入力の場合はチェック不要
            // ※規格１未入力のエラーメッセージが出力されるため
            if (item.getUnitValue1() != null) {
                // 規格2は入力無しのケース有のため、nullの場合は空文字として扱う
                String unitvalue =
                                item.getUnitValue1() + (item.getUnitValue2() != null ? " " + item.getUnitValue2() : "");
                if (unitvalues.contains(unitvalue)) {
                    addErrorMessage("AGG000728", new Object[] {item.getUnitDspNo()});
                } else {
                    unitvalues.add(unitvalue);
                }
            }
        }
    }

    /**
     * 規格設定チェック<br/>
     *
     * @param goodsRegistUpdateModel
     */
    private void checkGoodsUnit(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        if (HTypeUnitManagementFlag.ON.getValue().equals(goodsRegistUpdateModel.getUnitManagementFlag())) {
            // 規格１表示名
            if (goodsRegistUpdateModel.getUnitTitle1() == null) {
                addErrorMessage("AGG000709");
            }
            // 規格情報リスト
            if (goodsRegistUpdateModel.getUnitItems() == null) {
                return;
            }
            for (GoodsRegistUpdateUnitItem unitPageItem : goodsRegistUpdateModel.getUnitItems()) {
                if (unitPageItem.getUnitValue1() == null) {
                    addErrorMessage("AGG000710", new Object[] {unitPageItem.getUnitDspNo()});
                }
                if (goodsRegistUpdateModel.getUnitTitle2() != null && unitPageItem.getUnitValue2() == null) {
                    addErrorMessage("AGG000711", new Object[] {unitPageItem.getUnitDspNo()});
                }
                if (goodsRegistUpdateModel.getUnitTitle2() == null && unitPageItem.getUnitValue2() != null) {
                    addErrorMessage("AGG000712", new Object[] {unitPageItem.getUnitDspNo()});
                }
            }
        } else {
            // 規格１表示名
            if (goodsRegistUpdateModel.getUnitTitle1() != null) {
                addErrorMessage("AGG000713");
            }
            // 規格２表示名
            if (goodsRegistUpdateModel.getUnitTitle2() != null) {
                addErrorMessage("AGG000714");
            }
            // 規格情報リスト
            if (goodsRegistUpdateModel.getUnitItems() == null) {
                return;
            }
            boolean existUnit = false;
            boolean existUnitFail = false;
            for (GoodsRegistUpdateUnitItem unitPageItem : goodsRegistUpdateModel.getUnitItems()) {
                if (!existUnitFail && !HTypeGoodsSaleStatus.DELETED.getValue()
                                                                   .equals(unitPageItem.getGoodsSaleStatusPC())) {
                    if (existUnit) {
                        addErrorMessage("AGG000721");
                        existUnitFail = true;
                    } else {
                        existUnit = true;
                    }
                }
                if (unitPageItem.getUnitValue1() != null) {
                    addErrorMessage("AGG000715", new Object[] {unitPageItem.getUnitDspNo()});
                }
                if (unitPageItem.getUnitValue2() != null) {
                    addErrorMessage("AGG000716", new Object[] {unitPageItem.getUnitDspNo()});
                }
            }
        }
    }

    /**
     * 商品コード、カタログコードの重複チェック<br/>
     *
     * @param goodsRegistUpdateModel
     */
    private void checkCodeDuplication(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        // 商品コード、カタログコードの重複チェック
        Set<String> goodsCodeSet = new HashSet<>();
        for (GoodsRegistUpdateUnitItem unitPageItem : goodsRegistUpdateModel.getUnitItems()) {
            if (goodsCodeSet.contains(unitPageItem.getGoodsCode())) {
                addErrorMessage("AGG000717", new Object[] {unitPageItem.getUnitDspNo()});
            } else {
                goodsCodeSet.add(unitPageItem.getGoodsCode());
            }
        }
    }

    /***************************************************************************************************************************
     ** 商品画像設定
     ***************************************************************************************************************************/
    // *****************************************************************************
    // 新HIT-MALL：複数画像アップロード
    // *****************************************************************************

    /**
     * 複数商品グループ詳細画像アップロードイベント<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param error                  the error
     * @param model                  the model
     * @return 自画面 string
     */
    @PostMapping(value = "/", params = "doUploadMultipleDetailsImage")
    public String doUploadMultipleDetailsImage(
                    @Validated(UploadImageGroup.class) GoodsRegistUpdateModel goodsRegistUpdateModel,
                    BindingResult error,
                    Model model) {

        if (error.hasErrors()) {
            return "goods/registupdate/index";
        }

        MultipartFile[] uploadedGoodsImages = goodsRegistUpdateModel.getUploadedGoodsImages();

        // 画像操作Utility取得
        ImageUtility imageUtility = ApplicationContextUtility.getBean(ImageUtility.class);

        // 一時画像ファイルディレクトリ 物理パス
        String realPath = imageUtility.getRealTempPath() + "/";

        // 一時画像ファイルディレクトリ 一時urlパス
        String urlPath = imageUtility.getTempPath() + "/";

        // アップロードされた全ての画像を処理
        for (MultipartFile uploadedImageFile : uploadedGoodsImages) {
            Integer selectGn = goodsRegistUpdateHelper.getLastImageIndex(goodsRegistUpdateModel.getGoodsImageItems());

            // 対象の商品グループ詳細画像アイテムを取得
            GoodsRegistUpdateImageItem item = getSelectDetailsImageItem(selectGn, goodsRegistUpdateModel);
            if (item == null) {
                // 異常なので処理なし
                return "goods/registupdate/index";
            }

            // 対象の商品グループ詳細画像ファイル取得
            if (ObjectUtils.isEmpty(uploadedImageFile)) {
                // 画像取得失敗エラー
                throwMessage("AGG000601");
            } else {
                item.setImageFile(uploadedImageFile);
            }

            // 保存するファイル名を作成
            String saveFileName = goodsUtility.createImageFileName(goodsRegistUpdateModel.getGoodsGroupCode(), selectGn,
                                                                   null
                                                                  );

            // 一時ファイル名と公開ファイル名を取得
            String tmpFileName = imageUtility.createTempImageFileNameExtension(uploadedImageFile.getOriginalFilename(),
                                                                               saveFileName
                                                                              );
            String realFileName = imageUtility.createImageFileNameExtension(uploadedImageFile.getOriginalFilename(),
                                                                            saveFileName
                                                                           );

            // ファイル取得
            MultipartFile selectedRealFile = getSelectedDetailsImageFile(item, realPath, tmpFileName);
            if (selectedRealFile == null) {
                // 画像取得失敗エラー
                throwMessage("AGG000601");
            }

            // ページにセット
            goodsRegistUpdateHelper.makeGoodsGroupImageRegistUpdateInfo(
                            goodsRegistUpdateModel, selectGn, realPath + tmpFileName, tmpFileName, realFileName, false);

            // 一時ファイルの情報を反映
            item.setImagePath(urlPath + tmpFileName);
        }

        // 画面リロード後の自動スクロールのターゲットポジションをセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId("autoScrollGoodsImages");
        goodsRegistUpdateModel.setReloadFlag(true);
        return "redirect:/goods/registupdate/?goodGroupCode=" + goodsRegistUpdateModel.getGoodsGroupCode();
    }

    /**
     * 商品画像削除イベント<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return 自画面 string
     */
    @PostMapping(value = "/", params = "doDeleteDetailsImage")
    public String doDeleteDetailsImage(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        Integer selectImageNo = goodsRegistUpdateModel.getSelectImageNo();

        // 対象の商品グループ詳細画像アイテムを取得
        GoodsRegistUpdateImageItem item = getSelectDetailsImageItem(selectImageNo, goodsRegistUpdateModel);

        // ページにセット
        goodsRegistUpdateHelper.makeGoodsGroupImageRegistUpdateInfo(
                        goodsRegistUpdateModel, selectImageNo, null, null, null, true);

        // 一時ファイルの情報を反映
        if (item != null) {
            item.setImagePath(null);
        }

        // 画面リロード後の自動スクロールのターゲットポジションをセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId("autoScrollGoodsImages");

        return "goods/registupdate/index";
    }

    /**
     * 処理対象の商品グループ詳細画像アイテム取得<br/>
     *
     * @param selectImageNo          対象の画像連番
     * @param goodsRegistUpdateModel
     * @return 商品グループ詳細画像アイテム
     */
    private GoodsRegistUpdateImageItem getSelectDetailsImageItem(Integer selectImageNo,
                                                                 GoodsRegistUpdateModel goodsRegistUpdateModel) {

        for (GoodsRegistUpdateImageItem item : goodsRegistUpdateModel.getGoodsImageItems()) {
            if (selectImageNo.equals(item.getImageNo())) {
                return item;
            }
        }
        return null;
    }

    /***************************************************************************************************************************
     ** 在庫設定
     ***************************************************************************************************************************/
    /**
     * 入力情報チェック<br/>
     * Validatorで対応できないもの
     *
     * @param goodsRegistUpdateModel
     * @param redirectAttributes
     * @param model
     */
    private void checkDataStock(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        // 不整合処理チェック
        checkPageGoodsGroupEntity(goodsRegistUpdateModel, redirectAttributes, model);

        HTypeStockManagementFlag stockManagementFlag = EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                                     goodsRegistUpdateModel.getStockManagementFlag()
                                                                                    );

        // 在庫設定
        // ※管理フラグがONの場合、整合性チェックを実施
        // ※管理フラグがOFFの場合、在庫設定値を全て0に置き換えて登録する
        for (int i = 0;
             goodsRegistUpdateModel.getStockItems() != null && i < goodsRegistUpdateModel.getStockItems().size(); i++) {
            GoodsRegistUpdateStockItem stockPageItem = goodsRegistUpdateModel.getStockItems().get(i);
            if (HTypeStockManagementFlag.ON == stockManagementFlag) {
                // 残少表示在庫数
                if (stockPageItem.getRemainderFewStock() == null) {
                    addErrorMessage("AGG000801", new Object[] {stockPageItem.getStockDspNo()});
                }
                // 発注点在庫数
                if (stockPageItem.getOrderPointStock() == null) {
                    addErrorMessage("AGG000802", new Object[] {stockPageItem.getStockDspNo()});
                }
                // 安全在庫数
                if (stockPageItem.getSafetyStock() == null) {
                    addErrorMessage("AGG000803", new Object[] {stockPageItem.getStockDspNo()});
                }
                // 入庫数
                if (stockPageItem.getSupplementCount() == null) {
                    addErrorMessage("GOODS-STOCK-001-", new Object[] {stockPageItem.getStockDspNo()});
                }
            } else {

                // OFFのため値を0に置き換える

                // 残少表示在庫数
                stockPageItem.setRemainderFewStock("0");
                // 発注点在庫数
                stockPageItem.setOrderPointStock("0");
                // 安全在庫数
                stockPageItem.setSafetyStock("0");
                // 入庫数
                stockPageItem.setSupplementCount("0");
            }
        }

        // エラーがある場合は投げる
        if (hasErrorMessage()) {
            throwMessage();
        }
    }

    /***************************************************************************************************************************
     ** 関連商品設定
     ***************************************************************************************************************************/
    /**
     * 関連商品削除処理<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return ページクラス string
     */
    @PostMapping(value = "/", params = "doDeleteRelationGoods")
    public String doDeleteRelationGoods(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 関連商品削除
        goodsRegistUpdateHelper.doDeleteRelationGoods(goodsRegistUpdateModel);
        // 画面再表示
        goodsRegistUpdateHelper.toPageForLoadRelation(goodsRegistUpdateModel);
        // 画面リロード後の自動スクロールのターゲットポジションをセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId(ID_SCROLL_GOODS_RELATION_ITEMS);

        return "goods/registupdate/index";
    }

    /**
     * 関連商品表示順変更(上)<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return ページクラス string
     */
    @PostMapping(value = "/", params = "doUpRelationGoods")
    public String doUpRelationGoods(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }
        // 関連商品表示順変更(上)
        goodsRegistUpdateHelper.toPageForUpRelationGoods(goodsRegistUpdateModel);
        // 画面再表示
        goodsRegistUpdateHelper.toPageForLoadRelation(goodsRegistUpdateModel);
        // 画面リロード後の自動スクロールのターゲットポジションをセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId(ID_SCROLL_GOODS_RELATION_ITEMS);
        return "goods/registupdate/index";
    }

    /**
     * 関連商品表示順変更(下)<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return ページクラス string
     */
    @PostMapping(value = "/", params = "doDownRelationGoods")
    public String doDownRelationGoods(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }
        // 商品表示順変更(下)
        goodsRegistUpdateHelper.toPageForDownRelationGoods(goodsRegistUpdateModel);
        // 画面再表示
        goodsRegistUpdateHelper.toPageForLoadRelation(goodsRegistUpdateModel);
        // 画面リロード後の自動スクロールのターゲットポジションをセット
        goodsRegistUpdateModel.setTargetAutoScrollTagId(ID_SCROLL_GOODS_RELATION_ITEMS);

        return "goods/registupdate/index";
    }

    /**
     * 関連商品追加イベント（Ajax用）<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return 関連商品検索ダイアログ response entity
     */
    @PostMapping(value = "/doAddGoodRelationAjax")
    @ResponseBody
    public ResponseEntity<?> doAddGoodsRelationAjax(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                                    RedirectAttributes redirectAttributes,
                                                    Model model) {
        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return ResponseEntity.badRequest().body(check);
        }

        goodsRegistUpdateModel.setRedirectGoodsRelationEntityList(
                        goodsRegistUpdateModel.getTmpGoodsRelationEntityList());

        return ResponseEntity.ok(goodsRegistUpdateModel);
    }

    /**
     * コンポーネント値初期化
     *
     * @param goodsRegistUpdateModel
     */
    private void initComponentValue(GoodsRegistUpdateModel goodsRegistUpdateModel, BindingResult error) {
        try {
            // 検索
            TaxesResponse taxesResponse = taxApi.get();
            Map<BigDecimal, String> taxRateItems = goodsRegUpHelper.toBigDecimalKeyOfMap(taxesResponse);

            goodsRegistUpdateModel.setTaxRateItems(taxRateItems);
            goodsRegistUpdateModel.setAlcoholFlagItems(EnumTypeUtil.getEnumMap(HTypeAlcoholFlag.class));
            goodsRegistUpdateModel.setSnsLinkFlagItems(EnumTypeUtil.getEnumMap(HTypeSnsLinkFlag.class));
            goodsRegistUpdateModel.setIndividualDeliveryTypeItems(
                            EnumTypeUtil.getEnumMap(HTypeIndividualDeliveryType.class));
            goodsRegistUpdateModel.setGoodsOpenStatusPCItems(EnumTypeUtil.getEnumMap(HTypeOpenDeleteStatus.class));
            goodsRegistUpdateModel.setFreeDeliveryFlagItems(EnumTypeUtil.getEnumMap(HTypeFreeDeliveryFlag.class));
            goodsRegistUpdateModel.setUnitManagementFlagItems(EnumTypeUtil.getEnumMap(HTypeUnitManagementFlag.class));
            goodsRegistUpdateModel.setGoodsSaleStatusPCItems(
                            EnumTypeUtil.getEnumMap(HTypeGoodsSaleStatusWithNoDeleted.class));
            goodsRegistUpdateModel.setStockManagementFlagItems(EnumTypeUtil.getEnumMap(HTypeStockManagementFlag.class));
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * チェックボックスにデフォルト値を設定
     *
     * @param goodsRegistUpdateModel
     */
    private void initValueFlag(GoodsRegistUpdateModel goodsRegistUpdateModel) {
        if (StringUtils.isEmpty(goodsRegistUpdateModel.getAlcoholFlag())) {
            goodsRegistUpdateModel.setAlcoholFlag(HTypeAlcoholFlag.NOT_ALCOHOL.getValue());
        }
        if (StringUtils.isEmpty(goodsRegistUpdateModel.getNoveltyGoodsType())) {
            goodsRegistUpdateModel.setNoveltyGoodsType(HTypeNoveltyGoodsType.NORMAL_GOODS.getValue());
        }
        if (StringUtils.isEmpty(goodsRegistUpdateModel.getSnsLinkFlag())) {
            goodsRegistUpdateModel.setSnsLinkFlag(HTypeSnsLinkFlag.OFF.getValue());
        }
        if (StringUtils.isEmpty(goodsRegistUpdateModel.getIndividualDeliveryType())) {
            goodsRegistUpdateModel.setIndividualDeliveryType(HTypeIndividualDeliveryType.OFF.getValue());
        }
        if (StringUtils.isEmpty(goodsRegistUpdateModel.getFreeDeliveryFlag())) {
            goodsRegistUpdateModel.setFreeDeliveryFlag(HTypeFreeDeliveryFlag.OFF.getValue());
        }
        if (StringUtils.isEmpty(goodsRegistUpdateModel.getUnitManagementFlag())) {
            goodsRegistUpdateModel.setUnitManagementFlag(HTypeUnitManagementFlag.OFF.getValue());
        }
        if (StringUtils.isEmpty(goodsRegistUpdateModel.getStockManagementFlag())) {
            goodsRegistUpdateModel.setStockManagementFlag(HTypeStockManagementFlag.OFF.getValue());
        }
    }

    /***************************************************************************************************************************
     ** 商品登録更新確認
     ***************************************************************************************************************************/
    /**
     * 画像表示処理<br/>
     * 初期表示用メソッド<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return ページクラス string
     */
    @GetMapping("/confirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/goods/registupdate?from=confirm")
    public String doLoadConfirm(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                BindingResult error,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        Integer shopSeq = 1001;

        // ブラウザバックの場合、処理しない
        if (goodsRegistUpdateModel.getGoodsGroupDto() == null) {
            return "redirect:/goods/";
        }

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        goodsRegistUpdateModel.setInputingFlg(true);

        // 共通初期表示処理
        String errorClass = super.loadPage(goodsRegistUpdateModel, error, goodsRegistUpdateConfirmHelper,
                                           redirectAttributes, model
                                          );

        if (error.hasErrors()) {
            return "redirect:/goods/registupdate?from=confirm";
        }

        // 共通初期表示エラー時
        if (errorClass != null) {
            return errorClass;
        }

        // 商品グループが存在しない場合エラー
        if (!goodsRegistUpdateModel.isInputingFlg()
            && goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq() == null) {
            // 商品グループコード未指定の場合
            addMessage("AGG000002", redirectAttributes, model);
            return "redirect:/error";
        }

        // 商品アイコン情報リスト取得
        if (goodsRegistUpdateModel.getIconList() == null) {
            // 検索
            IconListResponse iconListResponse = new IconListResponse();
            try {
                iconListResponse = iconApi.getList(null);
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました ", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "redirect:/goods/registupdate?from=confirm";
            }
            List<GoodsInformationIconDto> goodsInformationIconDtoList = goodsRegUpHelper.toGoodsInformationIconDtoList(
                            Objects.requireNonNull(iconListResponse.getIconList()), shopSeq);

            goodsRegistUpdateModel.setIconList(goodsInformationIconDtoList);
        }

        Map<Integer, GoodsGroupImageEntity> masterGgImageMap = new HashMap<>();

        // 更新処理の場合は変更情報を表示する
        if (!goodsRegistUpdateModel.isRegist()
            && goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupCode() != null
            && goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq() != null) {

            String goodsGroupCodeRequest =
                            goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupCode();

            ProductDisplayGetRequest productDisplayGetRequest = new ProductDisplayGetRequest();
            productDisplayGetRequest.setGoodsGroupCode(goodsGroupCodeRequest);
            productDisplayGetRequest.setGoodCode(null);
            productDisplayGetRequest.setOpenStatus(EnumTypeUtil.getValue(HTypeOpenDeleteStatus.OPEN));
            productDisplayGetRequest.setSiteType(EnumTypeUtil.getValue(HTypeSiteType.BACK));

            ProductDisplayResponse productDisplayResponse = new ProductDisplayResponse();
            try {
                // 検索
                productDisplayResponse = productApi.getForDisplay(productDisplayGetRequest);
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "redirect:/goods/registupdate?from=confirm";
            }

            // 商品グループDtoを取得
            GoodsGroupDto masterGoodsGroupDto =
                            goodsRegUpHelper.toGoodsDtoFromProductDisplayResponse(productDisplayResponse);
            if (masterGoodsGroupDto == null) {
                // 商品グループコードなしエラー
                addMessage("AGG000001", new Object[] {goodsRegistUpdateModel.getGoodsGroupDto()
                                                                            .getGoodsGroupEntity().getGoodsGroupCode()},
                           redirectAttributes, model
                          );
                return "redirect:/error";
            }
            // インフォメーションアイコンPC(変更前)をセット
            if (masterGoodsGroupDto.getGoodsGroupDisplayEntity().getInformationIconPC() != null) {
                goodsRegistUpdateModel.setMasterInformationIconPcList(
                                Arrays.asList(masterGoodsGroupDto.getGoodsGroupDisplayEntity()
                                                                 .getInformationIconPC()
                                                                 .split("/")));
            }

            // 登録カテゴリIDリスト(変更前)をセット
            goodsRegistUpdateModel.setMasterCategoryGoodsEntityList(masterGoodsGroupDto.getCategoryGoodsEntityList());

            // 商品規格リスト(変更前)をセット
            goodsRegistUpdateModel.setMasterGoodsDtoList(masterGoodsGroupDto.getGoodsDtoList());

            // 登録カテゴリ情報の取得
            List<CategoryEntity> masterCategoryGoodsList =
                            goodsRegUpHelper.toCategoryEntity(masterGoodsGroupDto.getCategoryGoodsEntityList(),
                                                              shopSeq
                                                             );
            // ひもづいているカテゴリー
            List<CategoryEntity> categoryOfGoodsList =
                            goodsRegUpHelper.toCategoryRegistList(goodsRegistUpdateModel.getLinkedCategoryList(),
                                                                  masterCategoryGoodsList
                                                                 );
            if (!ListUtils.isEmpty(categoryOfGoodsList)) {
                goodsRegistUpdateModel.setRegistCategoryEntityList(categoryOfGoodsList);

            } else {
                goodsRegistUpdateModel.setRegistCategoryEntityList(null);
            }

            // 関連商品一覧取得
            RelationGoodsListGetRequest relationGoodsListGetRequest = new RelationGoodsListGetRequest();
            relationGoodsListGetRequest.setOpenStatus(EnumTypeUtil.getValue(HTypeOpenDeleteStatus.OPEN));

            jp.co.itechh.quad.relation.presentation.api.param.PageInfoRequest pageInfoRequest =
                            new jp.co.itechh.quad.relation.presentation.api.param.PageInfoRequest();
            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
            // リクエスト用のページャー項目をセット
            pageInfoHelper.setupPageRequest(pageInfoRequest, DEFAULT_PNUM, Integer.MAX_VALUE, null, true);

            RelationGoodsListResponse relationGoodsListResponse = new RelationGoodsListResponse();
            try {
                relationGoodsListResponse = relationApi.get(goodsRegistUpdateModel.getGoodsGroupDto()
                                                                                  .getGoodsGroupEntity()
                                                                                  .getGoodsGroupSeq(),
                                                            relationGoodsListGetRequest, pageInfoRequest
                                                           );
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました ", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "redirect:/goods/registupdate?from=confirm";
            }
            // 関連商品エンティティリスト(変更前)を取得する
            List<GoodsRelationEntity> masterGoodsRelationEntityList =
                            goodsRegUpHelper.toGoodsRelationEntityList(relationGoodsListResponse);
            goodsRegistUpdateModel.setMasterGoodsRelationEntityList(masterGoodsRelationEntityList);

            // 商品グループDtoの比較(修正箇所マップを取得)
            List<String> modifiedGoodsGroupList =
                            DiffUtil.diff(masterGoodsGroupDto, goodsRegistUpdateModel.getGoodsGroupDto());
            goodsRegistUpdateModel.setModifiedGoodeGroupList(modifiedGoodsGroupList);
            // 商品グループDtoの各項目における差分を作成
            goodsRegistUpdateConfirmHelper.setDiff(
                            goodsRegistUpdateModel, masterGoodsGroupDto, goodsRegistUpdateModel.getGoodsGroupDto());

            // 共通商品項目の比較(修正箇所マップを取得)
            List<String> modifiedCommonGoodsList = new ArrayList<>();
            GoodsEntity masterCommonGoodsEntity = null;
            for (int i = 0; masterGoodsGroupDto.getGoodsDtoList() != null && i < masterGoodsGroupDto.getGoodsDtoList()
                                                                                                    .size(); i++) {
                if (HTypeGoodsSaleStatus.DELETED != masterGoodsGroupDto.getGoodsDtoList()
                                                                       .get(i)
                                                                       .getGoodsEntity()
                                                                       .getSaleStatusPC()) {
                    masterCommonGoodsEntity = masterGoodsGroupDto.getGoodsDtoList().get(i).getGoodsEntity();
                    break;
                }
            }
            if (goodsRegistUpdateModel.getNotDeletedGoodsFirstIndex(goodsRegistUpdateModel.getGoodsGroupDto()) != -1) {
                modifiedCommonGoodsList = DiffUtil.diff(masterCommonGoodsEntity,
                                                        goodsRegistUpdateModel.getGoodsGroupDto()
                                                                              .getGoodsDtoList()
                                                                              .get(goodsRegistUpdateModel.getNotDeletedGoodsFirstIndex(
                                                                                              goodsRegistUpdateModel.getGoodsGroupDto()))
                                                                              .getGoodsEntity()
                                                       );
            }
            goodsRegistUpdateModel.setModifiedCommonGoodsList(modifiedCommonGoodsList);

            // 各画像情報のマスタを保持(比較用)
            // グループ画像
            List<GoodsGroupImageEntity> ggImageDtoList = masterGoodsGroupDto.getGoodsGroupImageEntityList();
            if (ggImageDtoList != null) {
                for (GoodsGroupImageEntity ggImage : ggImageDtoList) {
                    GoodsGroupImageEntity entity = masterGgImageMap.get(ggImage.getImageTypeVersionNo());
                    masterGgImageMap.put(ggImage.getImageTypeVersionNo(), entity);
                }
            }
            goodsRegistUpdateModel.setMasterGoodsGroupImageEntityMap(masterGgImageMap);
        } else {
            // 新製品登録用
            if (CollectionUtils.isNotEmpty(goodsRegistUpdateModel.getGoodsGroupDto().getCategoryGoodsEntityList())) {
                goodsRegistUpdateModel.setRegistCategoryEntityList(goodsRegUpHelper.toCategoryEntity(
                                goodsRegistUpdateModel.getGoodsGroupDto().getCategoryGoodsEntityList(), shopSeq));
            }
        }

        // ページ表示情報の編集
        goodsRegistUpdateConfirmHelper.toPageForLoad(goodsRegistUpdateModel);

        if (goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq() != null) {
            // 親商品グループがセール対象になっている警告
            List<String> goodsCodeList = new ArrayList<>();
            for (GoodsDto goodsDto : goodsRegistUpdateModel.getGoodsGroupDto().getGoodsDtoList()) {
                GoodsEntity goodsEntity = goodsDto.getGoodsEntity();
                if (HTypeGoodsSaleStatus.DELETED == goodsDto.getGoodsEntity().getSaleStatusPC()) {
                    continue;
                }
                goodsCodeList.add(goodsEntity.getGoodsCode());
            }
        }

        // 商品グループDto相関チェック
        if (goodsRegistUpdateModel.getGoodsGroupDto().getGoodsDtoList() != null) {

            ProductCorrelationCheckRequest productCorrelationCheckRequest = new ProductCorrelationCheckRequest();
            GoodsGroupRegistUpdateRequest goodsGroupRegistUpdateRequest =
                            goodsRegUpHelper.toGoodsGroupRegistUpdateRequest(goodsRegistUpdateModel.getGoodsGroupDto());
            List<GoodsRelationRequest> goodsRelationRequestList = goodsRegUpHelper.toGoodsRelationRequest(
                            goodsRegistUpdateModel.getGoodsRelationEntityList());

            productCorrelationCheckRequest.setGoodsGroup(goodsGroupRegistUpdateRequest);
            productCorrelationCheckRequest.setGoodsRelationList(goodsRelationRequestList);

            try {
                productApi.checkCorrelation(productCorrelationCheckRequest);
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                itemNameAdjust.put("goodsOpenStatus", "goodsOpenStatusPC");
                itemNameAdjust.put("openStartTime", "openStartTimePC");
                itemNameAdjust.put("openEndTime", "openEndTimePC");
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "redirect:/goods/registupdate?from=confirm";
            }
        }

        return "goods/registupdate/confirm";
    }

    /**
     * 登録更新イベント<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @param sessionStatus          the session status
     * @return 検索画面 string
     */
    @PostMapping(value = "/confirm/", params = "doOnceRegistUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/registupdate/confirm")
    public String doOnceRegistUpdate(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                     BindingResult error,
                                     RedirectAttributes redirectAttributes,
                                     Model model,
                                     SessionStatus sessionStatus) {

        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 商品グループDto相関チェック
        ProductCorrelationCheckRequest productCorrelationCheckRequest = new ProductCorrelationCheckRequest();
        GoodsGroupRegistUpdateRequest goodsGroupRegistUpdateRequest =
                        goodsRegUpHelper.toGoodsGroupRegistUpdateRequest(goodsRegistUpdateModel.getGoodsGroupDto());
        List<GoodsRelationRequest> goodsRelationRequestList =
                        goodsRegUpHelper.toGoodsRelationRequest(goodsRegistUpdateModel.getGoodsRelationEntityList());

        productCorrelationCheckRequest.setGoodsGroup(goodsGroupRegistUpdateRequest);
        productCorrelationCheckRequest.setGoodsRelationList(goodsRelationRequestList);

        try {
            productApi.checkCorrelation(productCorrelationCheckRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "goods/registupdate/confirm";
        }
        // 画像に関する登録更新情報をセット
        goodsRegistUpdateConfirmHelper.toSetImageInfo(goodsRegistUpdateModel);

        // 商品グループ登録更新
        ProductRegistUpdateRequest productRegistUpdateRequest = new ProductRegistUpdateRequest();

        productRegistUpdateRequest.setGoodsGroup(
                        goodsRegUpHelper.toGoodsGroupRegistUpdateRequest(goodsRegistUpdateModel.getGoodsGroupDto()));

        productRegistUpdateRequest.setGoodsRelationRequestList(
                        goodsRegUpHelper.toGoodsRelationRequest(goodsRegistUpdateModel.getGoodsRelationEntityList()));

        productRegistUpdateRequest.setGoodsGroupImageUpdateRequest(
                        goodsRegUpHelper.toGoodsGroupImageRegistUpdateRequest(
                                        goodsRegistUpdateModel.getGoodsGroupImageRegistUpdateDtoList()));

        productRegistUpdateRequest.setGoodsImageItems(
                        goodsRegUpHelper.toGoodsImageItemRequestList(goodsRegistUpdateModel.getGoodsImageItems()));

        try {
            ProductResponse productResponse = productApi.registUpdate(productRegistUpdateRequest);

            if (goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq() != null) {
                // 再検索フラグをセット
                redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);
            }

            // ワーニングメッセージの設定
            if (StringUtils.isNotEmpty(productResponse.getWarningMessage())) {
                String[] warningMessages = productResponse.getWarningMessage().split(",");
                for (String messageId : warningMessages) {
                    addMessage(messageId, redirectAttributes, model);
                }
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("goodsOpenStatus", "goodsOpenStatusPC");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "goods/registupdate/confirm";
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/goods/";
    }

    /**
     * 商品登録更新（関連商品設定）遷移<br/>
     *
     * @param goodsRegistUpdateModel the goods regist update model
     * @param redirectAttributes     the redirect attributes
     * @param model                  the model
     * @return 商品登録更新 （関連商品設定）画面
     */
    @PostMapping(value = "/confirm/", params = "doCancelConfirm")
    public String doCancelConfirm(GoodsRegistUpdateModel goodsRegistUpdateModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        Integer shopSeq = 1001;
        // 実行前処理
        String check = preDoAction(goodsRegistUpdateModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 更新処理の場合は変更情報を表示する
        if (!goodsRegistUpdateModel.isRegist()
            && goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupCode() != null
            && goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq() != null) {
            String goodsGroupCodeRequest =
                            goodsRegistUpdateModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupCode();

            ProductDisplayGetRequest productDisplayGetRequest = new ProductDisplayGetRequest();
            productDisplayGetRequest.setGoodsGroupCode(goodsGroupCodeRequest);
            productDisplayGetRequest.setGoodCode(null);
            productDisplayGetRequest.setOpenStatus(EnumTypeUtil.getValue(HTypeOpenDeleteStatus.OPEN));
            productDisplayGetRequest.setSiteType(EnumTypeUtil.getValue(HTypeSiteType.BACK));

            ProductDisplayResponse productDisplayResponse = new ProductDisplayResponse();
            try {
                // 検索
                productDisplayResponse = productApi.getForDisplay(productDisplayGetRequest);
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました ", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "redirect:/goods/registupdate?from=confirm";
            }

            // 商品グループDtoを取得
            GoodsGroupDto masterGoodsGroupDto =
                            goodsRegUpHelper.toGoodsDtoFromProductDisplayResponse(productDisplayResponse);
            // 登録カテゴリ情報の取得
            List<CategoryEntity> masterCategoryGoodsList =
                            goodsRegUpHelper.toCategoryEntity(masterGoodsGroupDto.getCategoryGoodsEntityList(),
                                                              shopSeq
                                                             );
            // ひもづいているカテゴリー
            List<CategoryEntity> categoryOfGoodsList =
                            goodsRegUpHelper.toCategoryRegistList(goodsRegistUpdateModel.getLinkedCategoryList(),
                                                                  masterCategoryGoodsList
                                                                 );
            goodsRegistUpdateModel.setLinkedCategoryList(categoryOfGoodsList);
        } else {
            // 新製品登録用
            if (CollectionUtils.isNotEmpty(goodsRegistUpdateModel.getGoodsGroupDto().getCategoryGoodsEntityList())) {
                goodsRegistUpdateModel.setRegistCategoryEntityList(goodsRegUpHelper.toCategoryEntity(
                                goodsRegistUpdateModel.getGoodsGroupDto().getCategoryGoodsEntityList(), shopSeq));
            }
        }
        goodsRegistUpdateModel.setInputingFlg(false);
        redirectAttributes.addFlashAttribute(FLASH_FROM_CONFIRM, true);
        return "redirect:/goods/registupdate/?goodGroupCode=" + goodsRegistUpdateModel.getGoodsGroupCode();
    }

    /**
     * 商品グループ詳細画像ファイル取得<br/>
     *
     * @param item        商品グループ詳細画像アイテム
     * @param realPath    商品画像ディレクトリパス
     * @param tmpFileName TMPファイル名
     * @return 詳細画像ファイル
     */
    private MultipartFile getSelectedDetailsImageFile(GoodsRegistUpdateImageItem item,
                                                      String realPath,
                                                      String tmpFileName) {
        putFile(item.getImageFile(), realPath + tmpFileName);
        return item.getImageFile();
    }

    /**
     * ファイル移動処理<br/>
     *
     * @param src 移動元ファイル
     * @param dst 移動先ファイル名
     */
    private void putFile(final MultipartFile src, final String dst) {
        try {
            FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
            fileOperationUtility.put(src, dst);
        } catch (IOException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("AGG001403");
        }
    }

}