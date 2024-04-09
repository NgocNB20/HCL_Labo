package jp.co.itechh.quad.admin.pc.web.admin.goods;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.exception.FileDownloadException;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsOutData;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.download.DownloadApiClient;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CategoryAndTagForGoodsRegistUpdateDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CsvDownloadOptionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.AllDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DownloadBottomGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DownloadTopGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.OptionDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.PreviewGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate.GoodsRegistUpdateModel;
import jp.co.itechh.quad.admin.pc.web.admin.goods.validation.GoodsValidator;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.HeaderParamsHelper;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.PreviewAccessKeyResponse;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvDownloadGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvGetOptionRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductCsvOptionUpdateRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListResponse;
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

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 商品検索コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/goods")
@Controller
@SessionAttributes(value = "goodsModel")
@PreAuthorize("hasAnyAuthority('GOODS:4')")
public class GoodsController extends AbstractController {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsController.class);

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 商品検索：デフォルトページ番号
     */
    private static final String DEFAULT_GOODSSEARCH_PNUM = "1";

    /**
     * 商品検索：デフォルト：ソート項目
     */
    private static final String DEFAULT_GOODSSEARCH_ORDER_FIELD = "goodsGroupCode";

    /**
     * カテゴリー：デフォルト：ソート項目
     */
    private static final String DEFAULT_CATEGORYSEARCH_ORDER_FIELD = "categoryId";

    /**
     * 商品検索：デフォルト：ソート条件(昇順/降順)
     */
    private static final boolean DEFAULT_GOODSSEARCH_ORDER_ASC = true;

    private final static String GOODS_SEARCH_DOWNLOAD = "/products/csv";

    /**
     * 商品検索ヘルパー<br/>
     */
    private final GoodsHelper goodsHelper;

    /**
     * 商品検索一覧の動的バリデータ
     */
    private final GoodsValidator goodsValidator;

    /**
     * 商品Api
     */
    private final ProductApi productApi;

    /**
     * カテゴリーApi
     */
    private final CategoryApi categoryApi;

    /** 権限グループAPI **/
    private final AuthorizationApi authorizationApi;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /** 日付関連ユーティリティ */
    private final DateUtility dateUtility;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /**
     * ダウンロードAPIクライアント
     */
    private final DownloadApiClient downloadApiClient;

    private String fileName = "goods";

    /** カテゴリ一一覧 : デフォルトページ番号 */
    private static final Integer DEFAULT_CATEGORY_PNUM = 1;

    /** カテゴリ一一覧 : １ページ当たりのデフォルト最大表示件数 */
    private static final Integer DEFAULT_CATEGORY_LIMIT = 20;

    /** カテゴリ一一覧 : ソート項目 */
    private static final String DEFAULT_CATEGORY_ORDER_FIELD = "categoryName";

    /**
     * コンストラクター
     *
     * @param goodsHelper       商品検索ヘルパー
     * @param goodsValidator    商品検索一覧の動的バリデータ
     * @param productApi        商品Api
     * @param categoryApi       カテゴリーApi
     * @param conversionUtility 変換ユーティリティクラス
     * @param downloadApiClient ダウンロードAPIクライアント
     */
    /** コンストラクタ */
    @Autowired
    public GoodsController(GoodsHelper goodsHelper,
                           GoodsValidator goodsValidator,
                           ProductApi productApi,
                           CategoryApi categoryApi,
                           AuthorizationApi authorizationApi,
                           ConversionUtility conversionUtility,
                           DateUtility dateUtility,
                           HeaderParamsHelper headerParamsHelper,
                           DownloadApiClient downloadApiClient) {
        this.goodsHelper = goodsHelper;
        this.goodsValidator = goodsValidator;
        this.productApi = productApi;
        this.categoryApi = categoryApi;
        this.authorizationApi = authorizationApi;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.headerParamsHelper = headerParamsHelper;
        this.downloadApiClient = downloadApiClient;
    }

    @InitBinder(value = "goodsModel")
    public void initBinder(WebDataBinder error) {
        // 商品検索一覧の動的バリデータをセット
        error.addValidators(goodsValidator);
    }

    /**
     * 画像表示処理<br/>
     * 初期表示用メソッド<br/>
     *
     * @param md
     * @param goodsModel
     * @param model
     * @return
     */
    @GetMapping("/")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                              GoodsModel goodsModel,
                              BindingResult error,
                              Model model) {

        // サブアプリケーション内の情報を初期化
        goodsModel.setInputingFlg(false);

        if (error.hasErrors()) {
            return "goods/index";
        }

        // 再検索の場合
        if (md.isPresent() || model.containsAttribute(FLASH_MD)) {
            // 再検索を実行
            search(goodsModel, error, model);
            if (error.hasErrors()) {
                return "goods/index";
            }
        } else {
            clearModel(GoodsModel.class, goodsModel, model);
        }

        // プルダウンアイテム情報を取得
        initDropDownValue(goodsModel, error);

        return "goods/index";
    }

    /**
     * 選択されたカテゴリをモデルに更新 （Ajax）<br/>
     *
     * @return カテゴリーリスト
     */
    @PostMapping(value = "/category/update/ajax")
    @ResponseBody
    public ResponseEntity<?> updateSelectedCategoryList(@RequestBody CategoryAndTagForGoodsRegistUpdateDto categoryChosenDto,
                                                        GoodsModel goodsModel) {

        if (CollectionUtil.isNotEmpty(categoryChosenDto.getCategoryChosenList())) {
            goodsModel.setCategoryList(categoryChosenDto.getCategoryChosenList());
        } else {
            goodsModel.setCategoryList(new ArrayList<>());
        }

        return ResponseEntity.ok("Success");
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
        CategoryListGetRequest categoryListGetRequest = goodsHelper.toCategoryListGetRequest(goodsRegistUpdateModel);

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
                        goodsHelper.toCategoryEntityFromResponse(categoryListResponse, goodsRegistUpdateModel, shopSeq);
        goodsRegistUpdateModel.setCategoryEntityList(categoryEntityList);
        return ResponseEntity.ok(categoryEntityList);
    }

    /**
     * プルダウンアイテム情報を取得
     *
     * @param goodsModel 商品検索モデル
     */
    protected void initDropDownValue(GoodsModel goodsModel, BindingResult error) {

        // プルダウンアイテム情報を取得
        goodsModel.setGoodsOpenStatusItems(EnumTypeUtil.getEnumMap(HTypeOpenDeleteStatus.class));
        goodsModel.setGoodsSaleStatusItems(EnumTypeUtil.getEnumMap(HTypeGoodsSaleStatus.class));
        goodsModel.setGoodsOutDataAllItems(EnumTypeUtil.getEnumMap(HTypeGoodsOutData.class));
        goodsModel.setFrontDisplayItems(EnumTypeUtil.getEnumMap(HTypeFrontDisplayStatus.class));

        goodsModel.setCsvDownloadOptionDtoList(goodsHelper.toCsvDownloadOptionDtoList(productApi.getOptionCsv()));

        // リクエスト用のページャーを生成
        jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest pageInfoRequest =
                        new jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

        // リクエスト用のページャー項目をセット
        // ソート順（カテゴパス）固定
        // ※カテゴリの検索ではページング制御不要のため、PageInfoは使わない（AbstractConditionDtoの継承はしない）
        pageInfoHelper.setupPageRequest(pageInfoRequest, pageInfoRequest.getPage(), Integer.MAX_VALUE,
                                        DEFAULT_CATEGORYSEARCH_ORDER_FIELD, true
                                       );

        try {
            CategoryListGetRequest categoryListGetRequest = new CategoryListGetRequest();
            CategoryListResponse categoryListResponse = categoryApi.get(categoryListGetRequest, pageInfoRequest);
            Map<String, String> categoryMap = goodsHelper.toCategoryMap(categoryListResponse);

            goodsModel.setSearchCategoryIdItems(categoryMap);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 検索処理<br/>
     */
    protected void search(GoodsModel goodsModel, BindingResult error, Model model) {

        // 検索条件作成
        ProductItemListGetRequest productItemListGetRequest = goodsHelper.toProductItemListGetRequest(goodsModel);

        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // リクエスト用のページャー項目をセット
        pageInfoHelper.setupPageRequest(pageInfoRequest, conversionUtility.toInteger(goodsModel.getPageNumber()),
                                        goodsModel.getLimit(), goodsModel.getOrderField(), goodsModel.isOrderAsc()
                                       );

        productItemListGetRequest.setRelationGoodsSearchFlag(false);

        // 取得
        ProductItemListResponse productItemListResponse = new ProductItemListResponse();
        try {
            productItemListResponse = productApi.getItems(productItemListGetRequest, pageInfoRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        goodsHelper.toPageForSearch(productItemListResponse, goodsModel);
        // ページャーにレスポンス情報をセット
        PageInfoResponse tmp = productItemListResponse.getPageInfo();
        pageInfoHelper.setupPageInfo(goodsModel, tmp.getPage(), tmp.getLimit(), tmp.getNextPage(), tmp.getPrevPage(),
                                     tmp.getTotal(), tmp.getTotalPages()
                                    );
        // ページャーセットアップ
        pageInfoHelper.setupViewPager(goodsModel.getPageInfo(), goodsModel);
    }

    /**
     * 新規登録画面遷移イベント<br/>
     *
     * @param sessionStatus
     * @param model
     * @return 商品登録更新(基本情報設定)画面
     */
    @PreAuthorize("hasAnyAuthority('GOODS:8')")
    @PostMapping(value = "/", params = "doRegist")
    public String doRegist(SessionStatus sessionStatus, Model model) {
        // Modelをセッションより破棄
        sessionStatus.setComplete();
        return "redirect:/goods/registupdate?from=menu";
    }

    /**
     * 商品管理 商品一括アップロード遷移イベント(Csv)
     *
     * @param sessionStatus
     * @param model
     * @return 商品管理 商品一括アップロード画面
     */
    @PreAuthorize("hasAnyAuthority('GOODS:8')")
    @PostMapping(value = "/", params = "doBundledUpload")
    public String doBundledUpload(SessionStatus sessionStatus, Model model) {
        // Modelをセッションより破棄
        sessionStatus.setComplete();
        return "redirect:/goods/bundledupload/csv/";
    }

    /**
     * 商品管理 商品一括アップロード遷移イベント(Image)
     *
     * @param sessionStatus
     * @param model
     * @return 商品管理 商品一括アップロード画面
     */
    @PreAuthorize("hasAnyAuthority('GOODS:8')")
    @PostMapping(value = "/", params = "doBundledImageUpload")
    public String doBundledImageUpload(SessionStatus sessionStatus, Model model) {
        // Modelをセッションより破棄
        sessionStatus.setComplete();
        return "redirect:/goods/bundledupload/image/";
    }

    /**
     * 検索イベント<br/>
     *
     * @param goodsModel
     * @param error
     * @param model
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doGoodsSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/index")
    public String doGoodsSearch(@Validated(SearchGroup.class) GoodsModel goodsModel, BindingResult error, Model model) {
        if (error.hasErrors()) {
            return "goods/index";
        }
        // 初期ソートと1ページをセット
        goodsModel.setOrderField(DEFAULT_GOODSSEARCH_ORDER_FIELD);
        goodsModel.setOrderAsc(DEFAULT_GOODSSEARCH_ORDER_ASC);
        goodsModel.setPageNumber(DEFAULT_GOODSSEARCH_PNUM);

        // 検索
        search(goodsModel, error, model);

        return "goods/index";
    }

    /**
     * 検索結果の表示切替<br/>
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/index")
    public String doDisplayChange(@Validated(DisplayChangeGroup.class) GoodsModel goodsModel,
                                  BindingResult error,
                                  Model model) {

        if (error.hasErrors()) {
            return "goods/index";
        }

        // 検索結果チェック
        resultListCheck(goodsModel);

        // 検索条件作成
        search(goodsModel, error, model);

        return "goods/index";
    }

    /**
     * ダイアログからのプレビューイベント AJAX<br/>
     *
     * @param goodsModel
     * @param error
     * @param model
     * @return プレビュー遷移用の値設定後の画面Model
     */
    @PostMapping(value = "doConfirmPreviewAjax")
    @ResponseBody
    public ResponseEntity<?> doConfirmPreviewAjax(@Validated(PreviewGroup.class) GoodsModel goodsModel,
                                                  BindingResult error,
                                                  Model model) {
        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors());
        }
        // ダイアログフォームから詰め替え
        goodsModel.setPreviewDate(goodsModel.getDialogPreviewDate());
        goodsModel.setPreviewTime(goodsModel.getDialogPreviewTime());
        return settingPreviewInfo(goodsModel);
    }

    /**
     * 検索結果から直接呼び出されるプレビューイベント AJAX<br/>
     *
     * @param goodsModel
     * @param model
     * @return プレビュー遷移用の値設定後の画面Model
     */
    @PostMapping(value = "doPreviewAjax")
    @ResponseBody
    public ResponseEntity<?> doPreviewAjax(GoodsModel goodsModel, Model model) {
        return settingPreviewInfo(goodsModel);
    }

    /**
     * プレビュー用Ajaxから呼び出されるプレビュー設定用の共通メソッド
     *
     * @param goodsModel
     * @return プレビュー遷移用の値設定後の画面Model
     */
    private ResponseEntity<?> settingPreviewInfo(GoodsModel goodsModel) {

        if (StringUtils.isBlank(goodsModel.getPreviewTime())) {
            //　日付のみ入力の場合、時間を設定
            goodsModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
        }

        try {
            // 管理者SEQをヘッダーに設定
            Integer seq = getCommonInfo().getCommonInfoAdministrator().getAdministratorSeq();
            this.headerParamsHelper.setAdminSeq(ObjectUtils.isEmpty(seq) ? null : seq.toString());

            PreviewAccessKeyResponse response = this.authorizationApi.issuePreviewAccessKey();
            if (ObjectUtils.isEmpty(response) || StringUtils.isEmpty(response.getPreviewAccessKey())) {
                return ResponseEntity.internalServerError().body("/error");
            }
            goodsModel.setPreKey(response.getPreviewAccessKey());
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            return ResponseEntity.internalServerError().body("/error");
        }
        // Timestampに変換後、指定の書式（yyyyMMddHHmmss）に変換
        Timestamp tmp = this.dateUtility.toTimestampValue(
                        goodsModel.getPreviewDate() + " " + goodsModel.getPreviewTime(),
                        this.dateUtility.YMD_SLASH_HMS
                                                         );
        goodsModel.setPreTime(this.dateUtility.format(tmp, this.dateUtility.YMD_HMS));
        return ResponseEntity.ok(goodsModel);
    }

    /**
     * CSVダウンロードイベント<br/>
     *
     * @param goodsModel
     * @param error
     * @return プレビュー画面 or ダイアログ画面（エラーメッセージ表示）
     */
    @PreAuthorize("hasAnyAuthority('GOODS:8')")
    @PostMapping(value = "/", params = "doCsvDownloadAll")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "goods/index")
    public void doCsvDownloadAll(HttpServletResponse response,
                                 @Validated(AllDownloadGroup.class) GoodsModel goodsModel,
                                 BindingResult error,
                                 Model model) {

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }

        // 検索条件作成
        ProductCsvDownloadGetRequest productItemListGetRequest = goodsHelper.toProductCsvDownloadGetRequest(goodsModel);

        // CSVDL オプション テンプレートの設定
        ProductCsvGetOptionRequest productCsvGetOptionRequest = new ProductCsvGetOptionRequest();
        if (goodsModel.getCsvDownloadOptionDto() != null) {
            productCsvGetOptionRequest.setOptionId(goodsModel.getCsvDownloadOptionDto().getOptionId());
        }
        try {
            downloadApiClient.invokeAPI(response, productApi.getApiClient().getBasePath(), GOODS_SEARCH_DOWNLOAD,
                                        getFileName(),
                                        conversionUtility.convertObjectListToMap(productCsvGetOptionRequest,
                                                                                 productItemListGetRequest
                                                                                )
                                       );
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
    }

    /**
     * CSVダウンロードイベント(検索結果上のボタン)
     *
     * @param goodsModel
     * @param error
     * @return 自画面
     */
    @PreAuthorize("hasAnyAuthority('GOODS:8')")
    @PostMapping(value = "/", params = "doCsvDownloadSelectTop")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "goods/index")
    public void doCsvDownloadSelectTop(HttpServletResponse response,
                                       @Validated(DownloadTopGroup.class) GoodsModel goodsModel,
                                       BindingResult error,
                                       Model model) {

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }

        csvDownloadSelect(response, goodsModel, error);

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
    }

    /**
     * CSVダウンロードイベント(検索結果下のボタン)
     *
     * @param goodsModel
     * @param error
     * @return
     */
    @PreAuthorize("hasAnyAuthority('GOODS:8')")
    @PostMapping(value = "/", params = "doCsvDownloadSelectBottom")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "goods/index")
    public void doCsvDownloadSelectBottom(HttpServletResponse response,
                                          @Validated(DownloadBottomGroup.class) GoodsModel goodsModel,
                                          BindingResult error,
                                          Model model) {

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }

        csvDownloadSelect(response, goodsModel, error);

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
    }

    /**
     * デフォルトの CSV ダウンロード オプションを取得 AJAX<br/>
     *
     * @return productCsvOptionResponse 商品検索CSVDLオプションリストレスポンス
     */
    @GetMapping(value = "/getDefaultOptionDownloadAjax")
    @ResponseBody
    public ResponseEntity<ProductCsvOptionResponse> getDefaultOptionDownloadAjax() {
        return ResponseEntity.ok(productApi.getDefault());
    }

    /**
     * CSVテンプレートの一覧を取得 AJAX<br/>
     *
     * @return productCsvOptionListResponse 商品検索CSVDLオプションリストレスポンス
     */
    @GetMapping(value = "/getCsvTemplateListAjax")
    @ResponseBody
    public ResponseEntity<ProductCsvOptionListResponse> getCsvTemplateListAjax() {
        return ResponseEntity.ok(productApi.getOptionCsv());
    }

    /**
     * CSVオプションの更新テンプレート AJAX <br/>
     *
     * @param csvDownloadOptionDto CSVDLオプショDto
     * @param error エラーリスト
     * @param goodsModel 商品検索ページ
     * @return
     */
    @PostMapping(value = "/doUpdateTemplateAjax")
    @ResponseBody
    public ResponseEntity<?> doUpdateTemplateAjax(
                    @RequestBody @Validated(OptionDownloadGroup.class) CsvDownloadOptionDto csvDownloadOptionDto,
                    BindingResult error,
                    GoodsModel goodsModel) {

        // ユーザーが注文画面でテンプレートを選択しなかった場合
        if (csvDownloadOptionDto.getResetFlg()) {
            goodsModel.setCsvDownloadOptionDto(null);
        }

        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors());
        }

        // ユーザーが注文画面でテンプレートのみを選択した場合
        if (csvDownloadOptionDto.getUpdateFlg()) {
            ProductCsvOptionUpdateRequest orderSearchCsvOptionUpdateRequest =
                            goodsHelper.toProductCsvOptionUpdateRequest(csvDownloadOptionDto);

            productApi.updateOption(orderSearchCsvOptionUpdateRequest);
        }

        goodsModel.setCsvDownloadOptionDto(csvDownloadOptionDto);

        return ResponseEntity.ok("ok");
    }

    /**
     * CSV選択ダウンロード<br/>
     *
     * @param goodsModel
     */
    protected void csvDownloadSelect(HttpServletResponse response, GoodsModel goodsModel, BindingResult error) {

        // 検索結果チェック
        resultListCheck(goodsModel);

        List<Integer> goodsSeqList = goodsHelper.toGoodsSeqList(goodsModel);

        // チェック選択なし
        if (goodsSeqList.isEmpty()) {
            throwMessage("AGG000102");
        }
        // 検索条件作成
        ProductCsvDownloadGetRequest productItemListGetRequest = goodsHelper.toProductCsvDownloadGetRequest(goodsModel);
        productItemListGetRequest.setGoodsSeqList(goodsSeqList);

        // CSVDL オプション テンプレートの設定
        ProductCsvGetOptionRequest productCsvGetOptionRequest = new ProductCsvGetOptionRequest();

        if (goodsModel.getOptionTemplateIndexResult() != null) {
            boolean errorFlag = true;
            for (CsvDownloadOptionDto csvDownloadOptionDto : goodsModel.getCsvDownloadOptionDtoList()) {
                if (StringUtils.isNotEmpty(csvDownloadOptionDto.getOptionId()) && csvDownloadOptionDto.getOptionId()
                                                                                                      .equals(goodsModel.getOptionTemplateIndexResult())) {
                    errorFlag = false;
                    break;
                }
            }
            if (errorFlag) {
                goodsModel.setOptionTemplateIndexResult(null);
            }
        }
        productCsvGetOptionRequest.setOptionId(goodsModel.getOptionTemplateIndexResult());

        try {
            downloadApiClient.invokeAPI(response, productApi.getApiClient().getBasePath(), GOODS_SEARCH_DOWNLOAD,
                                        getFileName(),
                                        conversionUtility.convertObjectListToMap(productCsvGetOptionRequest,
                                                                                 productItemListGetRequest
                                                                                )
                                       );
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 商品検索結果リストが空でないことをチェックする<br/>
     * (ブラウザバック後の選択出力などでの不具合防止のため)<br/>
     */
    protected void resultListCheck(GoodsModel goodsModel) {
        if (!goodsModel.isResult() || goodsModel.getResultItems().size() == 0) {
            return;
        }
        if (StringUtil.isEmpty(goodsModel.getResultItems().get(0).getGoodsGroupCode())) {
            goodsModel.setResultItems(null);
            this.throwMessage("AGG000103");
        }
    }

    public String getFileName() {
        if (StringUtils.isEmpty(this.fileName)) {
            this.fileName = "download_file";
        }

        CsvUtility csvUtility = (CsvUtility) ApplicationContextUtility.getBean(CsvUtility.class);
        return csvUtility.getDownLoadCsvFileName(this.fileName);
    }

}