/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.search;

import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryTreeResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.NumberUtility;
import jp.co.itechh.quad.front.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.front.thymeleaf.NumberConverterViewUtil;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.PageInfo;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品検索画面 Controller
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@SessionAttributes(value = "searchModel")
@RequestMapping("/search/")
@Controller
public class SearchController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    /** 価格帯From フィールド名 **/
    private static final String FILED_NAME_LL = "ll";

    /** 価格帯To フィールド名 **/
    private static final String FILED_NAME_UL = "ul";

    /** メッセージID:不正値  */
    private static final String MSGCD_INVALID = "GOODS-SEARCH-001-E";

    /** メッセージID:範囲外 **/
    private static final String MSGCD_OUT_OF_RANGE = "GOODS-SEARCH-002-E";

    /** 価格下限値 **/
    private static final int PRICE_MIN = 0;

    /** 価格上限値 **/
    private static final int PRICE_MAX = 99999999;

    /** 商品検索画面：デフォルトページ番号 */
    private static final String DEFAULT_PNUM = "1";

    /** 商品検索画面：１ページ当たりのリスト形式デフォルト最大表示件数 */
    private static final int DEFAULT_LIST_LIMIT = 10;

    /** 商品検索画面：１ページ当たりのサムネイル形式デフォルト最大表示件数 */
    private static final int DEFAULT_THUMBNAIL_LIMIT = 20;

    /** 商品検索画面：デフォルト表示形式キー */
    private static final String DEFAULT_VIEW_TYPE_KEY = PageInfo.VIEW_TYPE_LIST_KEY;

    /** 商品検索画面：デフォルト並び順キー */
    private static final String DEFAULT_SORT_TYPE_KEY = PageInfo.SORT_TYPE_REGISTDATE_KEY;

    /** 商品検索画面：デフォルトサムネイル表示改行位置*/
    private static final int DEFAULT_CHANGE_LINE_INDEX_THUMBNAIL = 5;

    /** 検索キーワードの最大文字数（超過部分は切り捨てます） */
    private static final int KEYWORD_MAX_LENGTH = 100;

    /** helperクラス */
    private final SearchHelper searchHelper;

    /** 商品API */
    private final ProductApi productApi;

    /** カテゴAPI */
    private final CategoryApi categoriesApi;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param searchHelper helperクラス
     * @param productApi 商品API
     * @param categoriesApi カテゴAPI
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public SearchController(SearchHelper searchHelper,
                            ProductApi productApi,
                            CategoryApi categoriesApi,
                            ConversionUtility conversionUtility) {
        this.searchHelper = searchHelper;
        this.productApi = productApi;
        this.conversionUtility = conversionUtility;
        this.categoriesApi = categoriesApi;
    }

    /**
     * 商品検索画面：初期処理
     *
     * @param fromView
     * @param searchModel
     * @param error
     * @param model
     * @return 商品検索画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "search/index")
    protected String doLoadIndex(@RequestParam(required = false) String fromView,
                                 SearchModel searchModel,
                                 BindingResult error,
                                 Model model) {

        // Model初期化
        searchHelper.toPageForLoad(searchModel, fromView);

        // （１） パラメータの情報を元に、商品情報の一覧を取得する。
        manualValidateAndConvert(searchModel, error);

        if (error.hasErrors()) {
            model.addAttribute(PageInfo.ATTRIBUTE_NAME_KEY, searchModel.getPageInfo());
            return "search/index";
        }

        // 表示条件にデフォルト値を設定
        setModelDefaultValue(searchModel);
        List<GoodsGroupDto> goodsGroupDtoList = getSearchResultList(searchModel, error, model);

        if (error.hasErrors()) {
            return "search/index";
        }

        // （３） 取得した商品一覧情報と検索条件を、ページ情報にセットする。
        searchHelper.toPageForLoad(goodsGroupDtoList, searchModel);

        prerender(searchModel, error);

        if (error.hasErrors()) {
            return "search/index";
        }

        return "search/index";
    }

    /**
     *
     * 検索<br/>
     *
     * @param fromView
     * @param searchModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return 商品検索画面
     */
    @PostMapping(value = "/", params = "doSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "search/index")
    public String doSearch(@RequestParam(required = false) String fromView,
                           @Validated SearchModel searchModel,
                           BindingResult error,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (error.hasErrors()) {
            model.addAttribute(PageInfo.ATTRIBUTE_NAME_KEY, searchModel.getPageInfo());
            return "search/index";
        }

        // Model初期化
        searchHelper.toPageForLoad(searchModel, fromView);

        // （１） パラメータの情報を元に、商品情報の一覧を取得する。
        // 検索条件
        // キーワード：画面で入力したキーワード
        // カテゴリ：画面で入力したカテゴリSEQ
        // 価格：画面で入力した価格From
        // 価格：画面で入力した価格To
        // 最大表示数：画面で選択されている最大表示数
        // ページ：1
        // ソート：画面で選択されている並び順
        searchModel.setPnum(DEFAULT_PNUM);
        setModelDefaultValue(searchModel);
        List<GoodsGroupDto> goodsGroupDtoList = getSearchResultList(searchModel, error, model);

        // （２） 取得した商品一覧情報を、ページ情報にセットする。
        searchHelper.toPageForLoad(goodsGroupDtoList, searchModel);

        // 自画面を表示
        return "search/index";
    }

    /**
     * 手動バリデータ＆コンバータ
     * LEB#731にてPOST⇒GETに方式変更したため、手動バリデータを実施
     * ※Modelクラスに定義されたバリデータのうち、最低限必要なもののみ実施する
     * @param searchModel
     * @param error
     */
    protected void manualValidateAndConvert(SearchModel searchModel, BindingResult error) {

        // 価格帯(FROM)チェック開始
        searchModel.setLl(checkValidateAndConvert(searchModel.getLl(), FILED_NAME_LL, error));
        // 価格帯(TO)チェック開始
        searchModel.setUl(checkValidateAndConvert(searchModel.getUl(), FILED_NAME_UL, error));
    }

    /**
     * 手動バリデータ＆コンバータのチェックメソッド
     * @param value 実行前の値
     * @param filedName
     * @param error
     * @return result 実行後の値
     */
    protected String checkValidateAndConvert(String value, String filedName, BindingResult error) {

        // 数値コンバータ
        NumberConverterViewUtil util = new NumberConverterViewUtil();

        // 数値関連Helper取得
        NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);

        // 数値コンバート @HCNumber
        String result = util.convert(value, "#");

        if (!StringUtils.isEmpty(result)) {
            // 数値かどうか @HVNumber
            if (!numberUtility.isNumber(result)) {
                error.rejectValue(filedName, MSGCD_INVALID);
            } else {
                // 範囲内かどうか @Range
                if (PRICE_MIN > Integer.parseInt(result) || Integer.parseInt(result) > PRICE_MAX) {
                    Object[] arg = new Object[] {Integer.toString(PRICE_MIN), Integer.toString(PRICE_MAX)};
                    error.rejectValue(filedName, MSGCD_OUT_OF_RANGE, arg, null);
                }
            }
        }
        return result;
    }

    /**
     * 表示条件にデフォルト値を設定<br/>
     *
     * @param searchModel
     */
    protected void setModelDefaultValue(SearchModel searchModel) {

        if (searchModel.getPnum() == null) {
            searchModel.setPnum(DEFAULT_PNUM);
        }
        if (searchModel.getVtype() == null) {
            searchModel.setVtype(DEFAULT_VIEW_TYPE_KEY);
        }
        if (searchModel.getStype() == null) {
            searchModel.setStype(DEFAULT_SORT_TYPE_KEY);
        }
        if (searchModel.getListLimit() == 0) {
            searchModel.setListLimit(DEFAULT_LIST_LIMIT);
        }
        if (searchModel.getThumbnailLimit() == 0) {
            searchModel.setThumbnailLimit(DEFAULT_THUMBNAIL_LIMIT);
        }
        if (searchModel.getCol() == 0) {
            searchModel.setCol(DEFAULT_CHANGE_LINE_INDEX_THUMBNAIL);
        }

    }

    /**
     * 検索結果商品リストの取得
     *
     * @param searchModel
     * @param model
     * @return 商品グループDTOリスト
     */
    protected List<GoodsGroupDto> getSearchResultList(SearchModel searchModel, BindingResult error, Model model) {
        // PageInfoモジュール取得
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        // 表示形式を取得
        String vType = searchModel.getVtype();

        ProductListGetRequest productListGetRequest = searchHelper.toSearchProductListGetRequest(searchModel);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // リクエスト用のページャー項目をセット
        pageInfoModule.setupPageRequest(pageInfoRequest, this.conversionUtility.toInteger(searchModel.getPnum()),
                                        getLimit(vType, searchModel), getStypeMap().get(searchModel.getStype()),
                                        searchModel.isAsc()
                                       );

        ProductListResponse productListResponse = null;
        try {
            // 公開商品情報検索サービス実行
            productListResponse = productApi.getList(productListGetRequest, pageInfoRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("minPrice", "ll");
            itemNameAdjust.put("maxPrice", "ul");
            itemNameAdjust.put("categoryId", "condCid");
            itemNameAdjust.put("stcockExistStatus", "stcockExistStatusList");
            itemNameAdjust.put("inStock", "st");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return null;
        }

        if (!ObjectUtils.isEmpty(productListResponse) && !ObjectUtils.isEmpty(productListResponse.getPageInfo())) {
            PageInfoResponse pageInfoResponse = productListResponse.getPageInfo();
            // 検索前ページャーセットアップ
            pageInfoModule.setupPageInfo(searchModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                         pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                         pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages(), getStypeMap(),
                                         searchModel.getStype(), null, searchModel.isAsc(), vType
                                        );
        }

        // 検索後ページングセットアップ
        pageInfoModule.setupViewPager(searchModel.getPageInfo(), model);

        List<GoodsGroupDto> goodsGroupDtoList = searchHelper.toGoodsGroupDtoList(productListResponse);

        return goodsGroupDtoList;
    }

    /**
     * 画面最大表示件数取得
     *
     * @param vtype 表示形式
     * @param searchModel
     * @return 画面最大表示件数
     */
    private int getLimit(String vtype, SearchModel searchModel) {

        int limit;
        // 表示形式がリストならば
        if (PageInfo.VIEW_TYPE_LIST_KEY.equals(vtype)) {
            // リストの画面最大表示件数取得
            limit = searchModel.getListLimit();
        } else {
            // サムネイルの画面最大表示件数取得
            limit = searchModel.getThumbnailLimit();
        }
        return limit;
    }

    /**
     * 表示形式の取得
     *
     * @param vType 表示形式
     * @return 表示形式
     */
    private String getVType(String vType) {
        // 表示形式がリストでないならば
        if (!PageInfo.VIEW_TYPE_LIST_KEY.equals(vType)) {
            // 表示形式はサムネイルにする
            vType = PageInfo.VIEW_TYPE_THUMBNAIL_KEY;
        }
        return vType;
    }

    /**
     * ソート項目の省略文字と正式名称のMapを取得
     *
     * @return ソート項目の省略文字と正式名称のMap
     */
    private Map<String, String> getStypeMap() {
        Map<String, String> stypeMap = new HashMap<String, String>();
        stypeMap.put(PageInfo.SORT_TYPE_REGISTDATE_KEY, "whatsnewdate");
        stypeMap.put(PageInfo.SORT_TYPE_GOODSPRICE_KEY, "goodsGroupMinPrice");
        stypeMap.put(PageInfo.SORT_TYPE_SALECOUNT_KEY, "popularityCount");
        return stypeMap;
    }

    /**
     * 表示前処理<br/>
     *
     * @param searchModel
     * @return 遷移先ページクラス
     */
    public Class<?> prerender(SearchModel searchModel, BindingResult error) {

        // ルートカテゴリ一覧情報をページクラスにセット
        // 検索条件プルダウンに、項目をセット
        CategoryTreeDto categoryTreeDto = getCategoryTreeDto(error);
        if (categoryTreeDto != null) {
            searchHelper.toPageForLoad(categoryTreeDto, searchModel);
        }

        return null;
    }

    /**
     * カテゴリー木構造取得
     *
     * @return カテゴリー木構造Dtoクラス
     */
    protected CategoryTreeDto getCategoryTreeDto(BindingResult error) {

        // リクエストパラメータ
        CategoryTreeGetRequest categoryTreeGetRequest = searchHelper.setupCategoryTree();

        CategoryTreeResponse categoryTreeResponse = null;
        try {
            categoryTreeResponse = categoriesApi.getTreeNodes(categoryTreeGetRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return null;
        }

        CategoryTreeDto categoryTreeDto = new CategoryTreeDto();
        searchHelper.toCategoryTreeDto(categoryTreeDto, categoryTreeResponse);

        return categoryTreeDto;
    }

}
