/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.base.utility.FileOperationUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryGoodsManualSort;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryGoodsSort;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionColumnType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionOperatorType;
import jp.co.itechh.quad.admin.constant.type.HTypeManualDisplayFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.admin.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.MessageUtils;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.ValidatorMessage;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CategoryGoodsManualAddDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ImageUploadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.PreviewGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.category.validation.CategoryInputValidator;
import jp.co.itechh.quad.admin.pc.web.admin.goods.category.validation.group.NextGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.GoodsSearchModel;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.goodssearch.group.GoodsSearchGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.utility.CategoryUtility;
import jp.co.itechh.quad.admin.utility.ImageUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.HeaderParamsHelper;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.PreviewAccessKeyResponse;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryItemListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryRegistRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryUpdateRequest;
import jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static jp.co.itechh.quad.admin.web.PageInfo.DEFAULT_PNUM;

/**
 * カテゴリ管理 : カテゴリ入力
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/goods/category")
@Controller
@SessionAttributes(value = "categoryInputModel")
@PreAuthorize("hasAnyAuthority('GOODS:8')")
public class CategoryInputController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryInputController.class);

    /**
     * カテゴリーヘルパークラス
     */
    private final CategoryUtility categoryUtility;

    /**
     * カテゴリ登録Helper
     */
    private final CategoryInputHelper inputHelper;

    /**
     * カテゴリ木構造取得サービス
     */
    private final CategoryApi categoryApi;

    /** 権限グループAPI **/
    private final AuthorizationApi authorizationApi;

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /** 日付関連ユーティリティ */
    private final DateUtility dateUtility;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /**
     * カテゴリ入力バリデータ
     */
    private final CategoryInputValidator categoryInputValidator;

    /** 商品Api */
    private final ProductApi productApi;

    /** 商品検索：デフォルトページ番号 */
    private static final Integer DEFAULT_GOODSSEARCH_PNUM = 1;

    /** 商品検索：デフォルト1ページ件数 */
    private static final Integer DEFAULT_GOODSSEARCH_LIMIT = 100;

    /** 商品検索：デフォルト：ソート項目 */
    private static final String DEFAULT_GOODSSEARCH_ORDER_FIELD = "goodsGroupCode";

    /** 商品検索：デフォルト：ソート条件(昇順/降順) */
    private static final boolean DEFAULT_GOODSSEARCH_ORDER_ASC = true;

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * コンストラクター
     * @param categoryUtility カテゴリーヘルパークラス
     * @param inputHelper     カテゴリ登録Helper
     * @param categoryApi     カテゴリ木構造取得サービス
     * @param authorizationApi
     * @param conversionUtility
     * @param dateUtility
     * @param headerParamsHelper
     * @param categoryInputValidator カテゴリ入力バリデータ
     * @param productApi             商品Api
     */
    @Autowired
    public CategoryInputController(CategoryUtility categoryUtility,
                                   CategoryInputHelper inputHelper,
                                   CategoryApi categoryApi,
                                   AuthorizationApi authorizationApi,
                                   ConversionUtility conversionUtility,
                                   DateUtility dateUtility,
                                   HeaderParamsHelper headerParamsHelper,
                                   CategoryInputValidator categoryInputValidator,
                                   ProductApi productApi) {
        this.categoryUtility = categoryUtility;
        this.inputHelper = inputHelper;
        this.categoryApi = categoryApi;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.headerParamsHelper = headerParamsHelper;
        this.authorizationApi = authorizationApi;
        this.categoryInputValidator = categoryInputValidator;
        this.productApi = productApi;
    }

    /**
     * 動的バリデータ初期化
     *
     * @param error WebDataBinder
     */
    @InitBinder(value = "categoryInputModel")
    public void initBinder(WebDataBinder error) {
        // 動的バリデータをセット
        error.addValidators(categoryInputValidator);
    }

    /**
     * 初期表示<br/>
     *
     * @param listScreen
     * @param categoryId
     * @param categoryInputModel
     * @param model
     * @return
     */
    @GetMapping("/input/")
    @HEHandler(exception = AppLevelListException.class, returnView = "goods/category/input")
    protected String doLoad(@RequestParam(required = false) Optional<String> listScreen,
                            @RequestParam(required = false) Optional<String> from,
                            @RequestParam(required = false) Optional<String> categoryId,
                            CategoryInputModel categoryInputModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (from.isPresent() && from.get().equals("confirm")) {
            return "goods/category/input";
        }

        // 自動スクロールのターゲットポジションをリセット
        categoryInputModel.setTargetAutoScrollTagId("");

        // コンポーネント値初期化
        initComponentValue(categoryInputModel);

        if (listScreen.isPresent() && categoryId.isPresent()) {
            categoryInputModel.setListScreen(Boolean.parseBoolean(listScreen.get()));
            categoryInputModel.setCategoryId(categoryId.get());
        } else if (!categoryInputModel.isConfirmScreen()) {
            clearModel(CategoryInputModel.class, categoryInputModel, model);
            initComponentValue(categoryInputModel);
        }

        // 【★０－１】画面：初期表示
        // get category
        if (categoryId.isPresent()) {
            try {
                CategoryResponse categoryResponseDb =
                                categoryApi.getByCategoryId(categoryUtility.getCategoryIdTop(), null);

                CategoryDto categoryDtoDb = new CategoryDto();
                inputHelper.setCategoryInformation(categoryDtoDb, categoryResponseDb);
                categoryInputModel.setCategoryDtoDB(categoryDtoDb);

                CategoryResponse categoryResponse =
                                categoryApi.getByCategoryId(categoryInputModel.getCategoryId(), null);
                CategoryDto categoryDto = new CategoryDto();
                inputHelper.setCategoryInformation(categoryDto, categoryResponse);
                categoryInputModel.setCategoryDto(categoryDto);
                categoryInputModel.setScCategorySeq(categoryDto.getCategoryEntity().getCategorySeq());

                inputHelper.setCategoryGoodSort(categoryInputModel, categoryResponse.getGoodsSortColumn(),
                                                categoryResponse.getGoodsSortOrder()
                                               );

                if (categoryResponse.getCategoryCondition() != null) {
                    List<CategoryConditionItem> categoryConditionItems =
                                    inputHelper.toCategoryConditionItems(categoryResponse.getCategoryCondition());
                    categoryInputModel.setCategoryConditionItems(categoryConditionItems);
                    categoryInputModel.setCategoryConditionType(
                                    categoryResponse.getCategoryCondition().getConditionType());
                }

                PageInfoRequest pageInfoRequest = new PageInfoRequest();
                PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
                pageInfoHelper.setupPageRequest(pageInfoRequest, DEFAULT_PNUM, Integer.MAX_VALUE, null, null);

                CategoryItemListResponse categoryItemListResponse =
                                categoryApi.getCategoryItems(categoryInputModel.getCategoryId(), null, pageInfoRequest);
                List<GoodsModelItem> goodsModelItemList = inputHelper.toGoodsModelItemList(categoryItemListResponse);

                categoryInputModel.setGoodsModelItems(goodsModelItemList);

            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
        }

        if (error.hasErrors()) {
            return "goods/category/input";
        }

        // 新規登録の場合
        if (!categoryInputModel.isListScreen() && !categoryInputModel.isConfirmScreen()) {
            inputHelper.registInit(categoryInputModel);
            // 実行前処理
            String check = preDoAction(categoryInputModel, redirectAttributes, model);
            if (StringUtils.isNotEmpty(check)) {
                return check;
            }
            // 画面初期描画時のデフォルト値を設定
            inputHelper.setDefaultValueForLoad(categoryInputModel);
            return "goods/category/input";
        }

        // 修正で一覧画面からの場合
        if (categoryInputModel.isListScreen() && !categoryInputModel.isConfirmScreen()) {

            // TOPカテゴリの場合エラーとする（URL直リンクの場合に発生）
            if (categoryUtility.getCategoryIdTop().equals(categoryInputModel.getCategoryId())) {
                addMessage("AGC000027", redirectAttributes, model);
                return "redirect:/goods/category/";
            }

            try {
                inputHelper.sessionInit(categoryInputModel);
                inputHelper.toModigyFromList(categoryInputModel);

                // 初期表示(カテゴリー一覧ページ)
                categoryInputModel.setInitialDisplayList(true);

                return "goods/category/input";
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }
            if (error.hasErrors()) {
                return "redirect:/goods/category/";
            }
        }
        // 画面初期描画時のデフォルト値を設定
        inputHelper.setDefaultValueForLoad(categoryInputModel);
        return "goods/category/input";
    }

    /**
     * カテゴリ条件を追加
     *
     * @param categoryInputModel カテゴリ入力
     */
    @PostMapping(value = "/addCategoryCondition")
    @ResponseBody
    public ResponseEntity<Void> addCategoryCondition(CategoryInputModel categoryInputModel) {

        inputHelper.addCategoryCondition(categoryInputModel);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * カテゴリ条件を削除
     *
     * @param index              インデックス
     * @param categoryInputModel 　カテゴリ入力
     */
    @PostMapping(value = "/removeCategoryCondition/{index}")
    @ResponseBody
    public ResponseEntity<Void> removeCategoryCondition(@PathVariable Integer index,
                                                        CategoryInputModel categoryInputModel) {

        inputHelper.removeCategoryCondition(categoryInputModel, index);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 商品の並べ替え ajax を取得する
     * @param categoryGoodsSort
     * @param categoryInputModel カテゴリ入力
     * @return 商品モデルアイテムリスト
     */
    @GetMapping(value = "/getGoodsSortAjax")
    @ResponseBody
    public ResponseEntity<List<?>> getGoodsSortAjax(@Validated CategoryGoodsSort categoryGoodsSort,
                                                    BindingResult errors,
                                                    CategoryInputModel categoryInputModel) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }

        inputHelper.sortGoods(categoryGoodsSort.getOptionSort(), categoryGoodsSort.getGoodsGroupChecked(),
                              categoryGoodsSort.getPosition(), categoryGoodsSort.getOptionSortManual(),
                              categoryInputModel
                             );

        return ResponseEntity.ok(categoryInputModel.getGoodsModelItems());
    }

    /**
     * 検索結果商品リストの取得
     *
     * @return 受注修正_追加商品検索結果レスポンスクラス
     */
    @PostMapping(value = "/doProductsSearchAjax")
    public ResponseEntity<?> doProductsSearchAjax(@Validated(GoodsSearchGroup.class) GoodsSearchModel goodsSearchModel,
                                                  BindingResult error,
                                                  RedirectAttributes redirectAttributes,
                                                  Model model) {

        // エラーチェック
        if (error.hasErrors()) {
            List<ValidatorMessage> mapError = MessageUtils.getMessageErrorFromBindingResult(error);
            return ResponseEntity.badRequest().body(mapError);
        }

        // 商品グループ検索条件リクエストに変換
        ProductListGetRequest productListGetRequest =
                        inputHelper.toGoodsSearchForBackDaoConditionDtoForProductsSearchAjax(goodsSearchModel);

        // ページング条件作成
        jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest pageInfoRequest =
                        new jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャー項目をセット
        pageInfoHelper.setupPageRequest(pageInfoRequest, DEFAULT_GOODSSEARCH_PNUM, DEFAULT_GOODSSEARCH_LIMIT,
                                        DEFAULT_GOODSSEARCH_ORDER_FIELD, DEFAULT_GOODSSEARCH_ORDER_ASC
                                       );

        // 公開商品情報検索サービス実行
        ProductListResponse productDetailListResponse = productApi.getList(productListGetRequest, pageInfoRequest);
        if (productDetailListResponse == null) {
            return null;
        }

        return ResponseEntity.ok(productDetailListResponse.getGoodsGroupList());
    }

    /**
     * 商品の ajax 応答エンティティを削除する
     *
     * @param goodsGroupSeq      商品グループSEQ
     * @param categoryInputModel カテゴリ入力
     */
    @PostMapping(value = "/deleteGoodsAjax/{goodsGroupSeq}")
    @ResponseBody
    public ResponseEntity<Void> deleteGoodsAjax(@PathVariable Integer goodsGroupSeq,
                                                CategoryInputModel categoryInputModel) {

        inputHelper.removeGoods(goodsGroupSeq, categoryInputModel.getGoodsModelItems());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PC画像アップロード<br/>
     *
     * @return class
     */
    @PostMapping(value = "/input/", params = "doUploadImagePC")
    public String doUploadImagePC(@Validated(ImageUploadGroup.class) CategoryInputModel categoryInputModel,
                                  BindingResult error,
                                  RedirectAttributes attributes,
                                  Model model) {

        if (error.hasErrors()) {
            return "goods/category/input";
        }

        // アップロードされている場合のみ
        if (categoryInputModel.getUploadCategoryImagePC() != null) {
            // 画像操作Utility取得
            ImageUtility imageUtility = ApplicationContextUtility.getBean(ImageUtility.class);
            FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

            // 仮のファイル名
            String tempFileName = imageUtility.createTempImageFileNameExtension(
                            categoryInputModel.getUploadCategoryImagePC().getOriginalFilename(), "");

            // ファイルコピー
            String realTmpFilePath = imageUtility.getRealTempPath() + "/" + tempFileName;

            try {
                if (!fileOperationUtility.put(categoryInputModel.getUploadCategoryImagePC(), realTmpFilePath)) {
                    addMessage("AGC000003", attributes, model);
                    return "goods/category/input";
                }
            } catch (IOException e) {
                LOGGER.error("例外処理が発生しました", e);
                addMessage("AGC000504", attributes, model);
                return "goods/category/input";
            }

            // 一時アップロードされたファイルへのパスへ変更
            String tmpPath = imageUtility.getTempPath() + "/" + tempFileName;
            categoryInputModel.setCategoryImagePathPC(tmpPath);
            categoryInputModel.getCategoryDto().getCategoryDisplayEntity().setCategoryImagePC(tempFileName);
            categoryInputModel.setFileNamePC(categoryInputModel.getUploadCategoryImagePC().getOriginalFilename());
            categoryInputModel.setTmpImagePC(true);
        }

        // 画面リロード後の自動スクロールのターゲットポジションをセット
        categoryInputModel.setTargetAutoScrollTagId("autoScrollCategoryImages");

        return "goods/category/input";
    }

    /**
     * PC画像削除<br/>
     *
     * @return class
     */
    @PostMapping(value = "/input/", params = "doDeleteImagePC")
    public String doDeleteImagePC(CategoryInputModel categoryInputModel,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        // 実行前処理
        String check = preDoAction(categoryInputModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        categoryInputModel.setCategoryImagePathPC(null);
        categoryInputModel.getCategoryDto().getCategoryDisplayEntity().setCategoryImagePC(null);
        categoryInputModel.setFileNamePC(null);
        categoryInputModel.setTmpImagePC(false);

        // 画面リロード後の自動スクロールのターゲットポジションをセット
        categoryInputModel.setTargetAutoScrollTagId("autoScrollCategoryImages");

        return "goods/category/input";
    }

    /**
     * キャンセル<br/>
     *
     * @param categoryInputModel
     * @param sessionStatus
     * @return
     */
    @PostMapping(value = "/input/", params = "doCancel")
    public String doCancel(CategoryInputModel categoryInputModel,
                           SessionStatus sessionStatus,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        // 再検索フラグをセット
        redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);

        // 実行前処理
        String check = preDoAction(categoryInputModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        categoryInputModel.setTmpImagePC(false);
        categoryInputModel.setFileNamePC(null);
        // カテゴリ一覧画面に戻る
        sessionStatus.setComplete();
        return "redirect:/goods/category/";
    }

    /**
     * アクション実行前処理
     *
     * @param categoryInputModel
     */
    public String preDoAction(CategoryInputModel categoryInputModel,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // 不正操作チェック
        return checkIllegalOperation(categoryInputModel, redirectAttributes, model);
    }

    /**
     * 不正操作チェック
     *
     * @param categoryInputModel
     * @return
     */
    protected String checkIllegalOperation(CategoryInputModel categoryInputModel,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        Integer scCategorySeq = categoryInputModel.getScCategorySeq();
        Integer dbCategorySeq = null;
        if (categoryInputModel.getCategoryDto() != null) {
            dbCategorySeq = categoryInputModel.getCategoryDto().getCategoryEntity().getCategorySeq();
        }

        boolean isError = false;

        if (scCategorySeq == null && dbCategorySeq != null) {
            // 登録画面にも関わらず、カテゴリSEQのDB情報を保持している場合エラー
            isError = true;

        } else if (scCategorySeq != null && dbCategorySeq == null) {
            // 修正画面にも関わらず、カテゴリSEQのDB情報を保持していない場合エラー
            isError = true;

        } else if (scCategorySeq != null && !scCategorySeq.equals(dbCategorySeq)) {
            // 画面用カテゴリSEQとDB用カテゴリSEQが異なる場合エラー
            isError = true;
        }

        if (isError) {
            addMessage(CategoryInputModel.MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/goods/category/";
        }
        return "";
    }

    /**
     * 登録処理<br/>
     *
     * @param categoryInputModel
     * @param redirectAttributes
     * @param sessionStatus
     * @param model
     * @return
     */
    @PostMapping(value = "/input/", params = "doOnceRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/goods/category/input/")
    public String doOnceRegist(@Validated(NextGroup.class) CategoryInputModel categoryInputModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               SessionStatus sessionStatus,
                               Model model) throws IOException {

        // 実行前処理
        if (error.hasErrors()) {
            return "goods/category/input";
        }

        categoryInputModel.setTargetParentCategoryId(null);
        inputHelper.toDto(categoryInputModel);

        // 画像ファイルの処理
        inputHelper.fileMovement(categoryInputModel);

        try {
            if (categoryInputModel.getCategoryDto().getCategoryEntity().getCategorySeq() == null) {
                CategoryRegistRequest categoryRegistRequest = inputHelper.toCategoryRegistRequest(categoryInputModel);
                //カテゴリ登録
                categoryApi.regist(categoryRegistRequest);
                addMessage("CATEGORY-REGIST-I", redirectAttributes, model);
            } else {
                CategoryUpdateRequest categoryUpdateRequest = inputHelper.toCategoryUpdateRequest(categoryInputModel);
                //カテゴリ更新
                categoryApi.updateCategories(categoryInputModel.getCategoryId(), categoryUpdateRequest);
                addMessage("CATEGORY-UPDATE-I", redirectAttributes, model);
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("fileName", "fileNamePC");
            itemNameAdjust.put("categoryImage", "categoryImagePC");
            itemNameAdjust.put("tmpImage", "tmpImagePC");
            itemNameAdjust.put("rootCategorySeq", "rootCategorySeqPC");
            itemNameAdjust.put("categoryOpenStatus", "categoryOpenStatusPC");
            itemNameAdjust.put("categoryOpenStartTime", "categoryOpenStartTimePC");
            itemNameAdjust.put("categoryOpenEndTime", "categoryOpenEndTimePC");
            itemNameAdjust.put("openGoodsCount", "openGoodsCountPC");
            itemNameAdjust.put("categoryName", "categoryNamePC");
            itemNameAdjust.put("categoryNote", "categoryNotePC");
            itemNameAdjust.put("freeText", "freeTextPC");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "redirect:/goods/category/input/";
        }

        // 再検索フラグをセット
        redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);

        sessionStatus.setComplete();
        return "redirect:/goods/category/input/?listScreen=true&categoryId=" + categoryInputModel.getCategoryId();
    }

    /**
     * 戻る
     *
     * @param categoryInputModel
     * @param redirectAttributes
     * @return
     */
    @PostMapping(value = "/confirm/", params = "doReturn")
    public String doReturn(CategoryInputModel categoryInputModel, RedirectAttributes redirectAttributes, Model model) {

        // 実行前処理
        String check = preDoAction(categoryInputModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // セッション値チェック
        if (categoryInputModel.getCategoryDto().getCategoryEntity() == null) {
            return "redirect:/goods/category/input/";
        }

        categoryInputModel.setConfirmScreen(true);
        categoryInputModel.setListScreen(categoryInputModel.getTargetParentCategory().isListScreen());

        return "redirect:/goods/category/input/";
    }

    /**
     * 「追加」ボタン押下時の処理<br/>
     *
     * @param categoryInputModel カテゴリ管理 : カテゴリ入力
     * @param categoryGoodsManualAddDto カテゴリ登録商品手動追加Dto
     * @param redirectAttributes
     * @param model
     * @return 商品選択画面
     */
    @PostMapping(value = "doProductsAddAjax")
    public ResponseEntity<?> doProductsAddAjax(CategoryInputModel categoryInputModel,
                                               @RequestBody CategoryGoodsManualAddDto categoryGoodsManualAddDto,
                                               RedirectAttributes redirectAttributes,
                                               Model model) {
        List<GoodsModelItem> goodsModelItems = new ArrayList<>();

        if (ObjectUtils.isNotEmpty(categoryGoodsManualAddDto)) {
            goodsModelItems = inputHelper.sortGoodsForNewlyAdded(categoryGoodsManualAddDto.getOptionSort(),
                                                                 categoryGoodsManualAddDto.getGoodsItems()
                                                                );
            categoryInputModel.setGoodsModelItems(goodsModelItems);
        }

        return ResponseEntity.ok(goodsModelItems);
    }

    /**
     * コンポーネント値初期化
     *
     * @param categoryInputModel
     */
    private void initComponentValue(CategoryInputModel categoryInputModel) {

        // プルダウンアイテム情報を取得
        categoryInputModel.setCategoryOpenStatusPCItems(EnumTypeUtil.getEnumMap(HTypeOpenStatus.class));

        categoryInputModel.setCategoryGoodsSortItems(EnumTypeUtil.getEnumMap(HTypeCategoryGoodsSort.class));
        categoryInputModel.setCategoryGoodsSortManualItems(EnumTypeUtil.getEnumMap(HTypeCategoryGoodsManualSort.class));

        Map<String, String> categoryConditionColumnItems = EnumTypeUtil.getEnumMap(HTypeConditionColumnType.class);
        categoryConditionColumnItems.remove(HTypeConditionColumnType.ALL.getValue());

        categoryInputModel.setCategoryConditionColumnItems(categoryConditionColumnItems);
        categoryInputModel.setCategoryConditionOperatorItems(EnumTypeUtil.getEnumMap(HTypeConditionOperatorType.class));

        if (CollectionUtils.isEmpty(categoryInputModel.getCategoryConditionItems())) {

            CategoryConditionItem categoryConditionItemEmpty = new CategoryConditionItem();

            List<CategoryConditionItem> categoryConditionItems = new ArrayList<>();
            categoryConditionItems.add(categoryConditionItemEmpty);

            categoryInputModel.setCategoryConditionItems(categoryConditionItems);
        }
    }

    /**
     * チェックボックスにデフォルト値を設定
     *
     * @param categoryInputModel
     */
    private void initValueFlag(CategoryInputModel categoryInputModel) {
        if (StringUtils.isEmpty(categoryInputModel.getCategoryType())) {
            categoryInputModel.setCategoryType(HTypeCategoryType.AUTO.getValue());
        }
        if (StringUtils.isEmpty(categoryInputModel.getManualDisplayFlag())) {
            categoryInputModel.setManualDisplayFlag(HTypeManualDisplayFlag.OFF.getValue());
        }
        if (StringUtils.isEmpty(categoryInputModel.getSiteMapFlag())) {
            categoryInputModel.setSiteMapFlag(HTypeSiteMapFlag.OFF.getValue());
        }
    }

    /**
     * プレビュー表示イベント<br/>
     *
     * @param categoryInputModel
     * @param model
     * @return プレビュー遷移用の値設定後の画面Model
     */
    @PostMapping(value = "/input/doPreviewAjax")
    @ResponseBody
    public ResponseEntity<?> doPreviewAjax(@Validated(PreviewGroup.class) CategoryInputModel categoryInputModel,
                                           BindingResult error,
                                           Model model) {

        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors());
        }
        if (StringUtils.isBlank(categoryInputModel.getPreviewTime())) {
            //　日付のみ入力の場合、時間を設定
            categoryInputModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
        }

        try {
            // 管理者SEQをヘッダーに設定
            Integer seq = getCommonInfo().getCommonInfoAdministrator().getAdministratorSeq();
            this.headerParamsHelper.setAdminSeq(ObjectUtils.isEmpty(seq) ? null : seq.toString());

            PreviewAccessKeyResponse response = this.authorizationApi.issuePreviewAccessKey();
            if (ObjectUtils.isEmpty(response) || StringUtils.isEmpty(response.getPreviewAccessKey())) {
                return ResponseEntity.internalServerError().body("/error");
            }
            categoryInputModel.setPreKey(response.getPreviewAccessKey());
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            return ResponseEntity.internalServerError().body("/error");
        }
        // Timestampに変換後、指定の書式（yyyyMMddHHmmss）に変換
        Timestamp tmp = this.dateUtility.toTimestampValue(
                        categoryInputModel.getPreviewDate() + " " + categoryInputModel.getPreviewTime(),
                        this.dateUtility.YMD_SLASH_HMS
                                                         );
        categoryInputModel.setPreTime(this.dateUtility.format(tmp, this.dateUtility.YMD_HMS));
        return ResponseEntity.ok(categoryInputModel);
    }

}