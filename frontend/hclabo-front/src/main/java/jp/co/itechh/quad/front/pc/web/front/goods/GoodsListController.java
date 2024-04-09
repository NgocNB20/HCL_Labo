/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.PreviewAccessCheckRequest;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathListResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeManualDisplayFlag;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.PageInfo;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品一覧画面 Controller
 *
 * @author Pham Quang Dieu (VJP)
 */
@RequestMapping("/goods/")
@Controller
public class GoodsListController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsListController.class);

    /**
     * メッセージコード：プレビューアクセスチェックエラー
     */
    public static final String MESSGE_CODE_PREVIEW_NO_ACCESS = "PREVIEW-NO-ACCESS-001-";

    /**
     * メッセージコード：プレビューアクセスチェックエラー（リクエストパラメータ不備）
     */
    public static final String MESSGE_CODE_PREVIEW_FAIL_OPEN = "PREVIEW-NO-ACCESS-002-";

    /**
     * 商品一覧画面：デフォルトページ番号
     */
    public static final String DEFAULT_PNUM = "1";

    /**
     * 商品一覧画面：１ページ当たりのリスト形式デフォルト最大表示件数
     */
    public static final String DEFAULT_LIST_LIMIT = "10";

    /**
     * 商品一覧画面：１ページ当たりのサムネイル形式デフォルト最大表示件数
     */
    public static final String DEFAULT_THUMBNAIL_LIMIT = "20";

    /**
     * 商品一覧画面：デフォルト表示形式キー
     */
    public static final String DEFAULT_VIEW_TYPE_KEY = PageInfo.VIEW_TYPE_LIST_KEY;

    /**
     * 商品一覧画面：デフォルト並び順キー
     */
    public static final String DEFAULT_SORT_TYPE_KEY = PageInfo.SORT_TYPE_REGISTDATE_KEY;

    /**
     * 商品一覧画面：デフォルト昇順キー
     */
    public static final String DEFAULT_ASC_TYPE_KEY = PageInfo.ASC_TYPE_FALSE_KEY;

    /**
     * サムネイル表示改行位置
     */
    public static final String DEFAULT_CHANGE_LINE_INDEX_THUMBNAIL = "5";

    /** 画面初期表示*/
    public static final String DEFAULT_INIT_DISPLAY = "false";

    /**
     * 商品一覧 Helper
     */
    private GoodsListHelper goodsListHelper;

    /**
     * 日付関連Utilityクラス
     */
    private final DateUtility dateUtility;

    /**
     * 商品API
     */
    private final ProductApi productApi;

    /**
     * カテゴリAPI
     */
    private final CategoryApi categoryApi;

    /**
     * 権限グループAPI
     */
    private final AuthorizationApi authorizationApi;

    /** コンストラクタ */
    @Autowired
    public GoodsListController(GoodsListHelper goodsListHelper,
                               DateUtility dateUtility,
                               ProductApi productApi,
                               CategoryApi categoryApi,
                               AuthorizationApi authorizationApi) {
        this.goodsListHelper = goodsListHelper;
        this.dateUtility = dateUtility;
        this.productApi = productApi;
        this.categoryApi = categoryApi;
        this.authorizationApi = authorizationApi;
    }

    /**
     * 商品一覧画面：初期処理
     *
     * @param goodsListModel
     * @param pnum           ページ番号
     * @param listLimit      リスト形式デフォルト最大表示件数
     * @param thumbnailLimit サムネイル形式デフォルト最大表示件数
     * @param vtype          表示形式
     * @param stype          並び順
     * @param asc            昇順
     * @param col            サムネイル表示改行位置
     * @param prekey         プレビューキー
     * @param pretime        プレビュー日時（yyyyMMddHHmmss）
     * @param model
     * @return 商品検索画面
     */
    @GetMapping(value = "/list")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/error")
    protected String doLoadIndex(GoodsListModel goodsListModel,
                                 BindingResult error,
                                 @RequestParam(required = false, defaultValue = DEFAULT_INIT_DISPLAY) boolean isSide,
                                 @RequestParam(required = false, defaultValue = DEFAULT_PNUM) String pnum,
                                 @RequestParam(required = false, defaultValue = DEFAULT_LIST_LIMIT) int listLimit,
                                 @RequestParam(required = false, defaultValue = DEFAULT_THUMBNAIL_LIMIT)
                                                 int thumbnailLimit,
                                 @RequestParam(required = false, defaultValue = DEFAULT_VIEW_TYPE_KEY) String vtype,
                                 @RequestParam(required = false, defaultValue = DEFAULT_SORT_TYPE_KEY) String stype,
                                 @RequestParam(required = false) boolean asc,
                                 @RequestParam(required = false, defaultValue = DEFAULT_CHANGE_LINE_INDEX_THUMBNAIL)
                                                 int col,
                                 @RequestParam(required = false) String prekey,
                                 @RequestParam(required = false) String pretime,
                                 Model model) {

        // 画面初期表示処理フラグ
        goodsListModel.setInitialDisplayFlag(isSide);

        // カテゴリID
        String cid = goodsListModel.getCid();

        // 階層付き通番
        String hierarchicalSerialNumber = goodsListModel.getHsn();

        if (StringUtils.isNotEmpty(cid)) {

            // プレビュー画面表示チェック
            if (StringUtils.isNotBlank(prekey) || StringUtils.isNotBlank(pretime)) {
                // パラメータにどちらかが設定されている場合、プレビュー画面の遷移として扱う
                checkPreviewOpen(goodsListModel, prekey, pretime);
            }

            getGoodsGroupListByCategoryId(cid, hierarchicalSerialNumber, goodsListModel, error, pnum, listLimit,
                                          thumbnailLimit, vtype, stype, asc, col, model
                                         );
        } else {
            return "redirect:/error";
        }

        if (error.hasErrors()) {
            return "redirect:/error";
        }

        return "goods/list";

    }

    /**
     * カテゴリ商品一覧表示<br/>
     *
     * @param cid            カテゴリID
     * @param hierarchicalSerialNumber 階層付き通番
     * @param goodsListModel
     * @param pnum           ページ番号
     * @param listLimit      リスト形式デフォルト最大表示件数
     * @param thumbnailLimit サムネイル形式デフォルト最大表示件数
     * @param vType          表示形式
     * @param sType          並び順
     * @param asc            昇順
     * @param col            サムネイル表示改行位置
     * @param model
     */
    protected void getGoodsGroupListByCategoryId(String cid,
                                                 String hierarchicalSerialNumber,
                                                 GoodsListModel goodsListModel,
                                                 BindingResult error,
                                                 String pnum,
                                                 int listLimit,
                                                 int thumbnailLimit,
                                                 String vType,
                                                 String sType,
                                                 boolean asc,
                                                 int col,
                                                 Model model) {

        // 自カテゴリ情報の取得
        CategoryDetailsDto categoryDetailsDto = getCategoryDto(goodsListModel, error);

        if (error.hasErrors()) {
            return;
        }

        goodsListHelper.toPageForLoad(categoryDetailsDto, goodsListModel);

        // カテゴリが取得できない場合 エラーページ
        if (categoryDetailsDto == null) {
            throwMessage(GoodsListModel.MSGCD_CATEGORY_NOT_FOUND_ERROR);
        }

        // パンくず情報の取得
        TopicPathListResponse topicPathListResponse = null;
        try {
            TopicPathGetRequest topicPathGetRequest = new TopicPathGetRequest();
            topicPathGetRequest.setHierarchicalSerialNumber(hierarchicalSerialNumber);

            if (StringUtils.isNotBlank(goodsListModel.getPreKey())) {
                topicPathGetRequest.setFrontDisplayReferenceDate(
                                this.dateUtility.toTimestampValue(goodsListModel.getPreTime(),
                                                                  this.dateUtility.YMD_HMS
                                                                 ));
            }

            topicPathListResponse = categoryApi.getCategoryTopicPath(cid, topicPathGetRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }
        goodsListHelper.toPageForLoadForTopicPath(topicPathListResponse, goodsListModel);

        // 指定したカテゴリに属する商品グループ情報を取得
        List<GoodsGroupDto> categoryGoodsList =
                        getCategoryGoodsList(cid, goodsListModel, error, pnum, listLimit, thumbnailLimit, vType, sType,
                                             asc, model
                                            );

        // 手動並び替え表示モード設定
        if (categoryDetailsDto != null && getStypeMap().get(PageInfo.SORT_TYPE_NORMAL_KEY)
                                                       .equals(categoryDetailsDto.getGoodsSortColumn())) {
            goodsListModel.setManualDisplayFlag(HTypeManualDisplayFlag.ON);
        }

        // カテゴリー管理で設定したソート順が初期設定する
        if (goodsListModel.getInitialDisplayFlag() && categoryDetailsDto != null) {
            goodsListModel.getPageInfo().setOrderField(categoryDetailsDto.getGoodsSortColumn());
            goodsListModel.getPageInfo().setOrderAsc(categoryDetailsDto.isGoodsSortOrder());
        }

        if (error.hasErrors()) {
            return;
        }

        goodsListHelper.toPageForLoadCurrentCategoryGoods(categoryGoodsList, col, goodsListModel);

    }

    /**
     * 自カテゴリリストの取得
     *
     * @param goodsListModel
     * @return 商品詳細情報DTO
     */
    protected CategoryDetailsDto getCategoryDto(GoodsListModel goodsListModel, BindingResult error) {

        // 公開中カテゴリ取得
        CategoryResponse categoryResponse = null;
        try {
            CategoryGetRequest categoryGetRequest = new CategoryGetRequest();
            if (StringUtils.isNotBlank(goodsListModel.getPreKey())) {
                categoryGetRequest.setFrontDisplayReferenceDate(
                                this.dateUtility.toTimestampValue(goodsListModel.getPreTime(),
                                                                  this.dateUtility.YMD_HMS
                                                                 ));
            }
            categoryResponse = categoryApi.getCategoryOpen(goodsListModel.getCid(), categoryGetRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return null;
        }

        return goodsListHelper.toCategoryDetailsDto(categoryResponse);
    }

    /**
     * 指定したカテゴリに属する商品グループ情報を取得する。<br/>
     *
     * @param cid            カテゴリID
     * @param goodsListModel
     * @param pnum           ページ番号
     * @param listLimit      リスト形式デフォルト最大表示件数
     * @param thumbnailLimit サムネイル形式デフォルト最大表示件数
     * @param vType          表示形式
     * @param sType          並び順
     * @param asc            昇順
     * @param model
     * @return 商品グループ情報
     */
    protected List<GoodsGroupDto> getCategoryGoodsList(String cid,
                                                       GoodsListModel goodsListModel,
                                                       BindingResult error,
                                                       String pnum,
                                                       int listLimit,
                                                       int thumbnailLimit,
                                                       String vType,
                                                       String sType,
                                                       boolean asc,
                                                       Model model) {

        // PageInfoモジュール取得
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();

        // 画面初期表示処理で、フロントエンド→商品サービスから商品一覧を取得する際に
        //「並び順項目」と「asc/desc」は　何も引き渡さない　ように修正
        if (goodsListModel.getInitialDisplayFlag()) {
            pageInfoModule.setupPageRequest(pageInfoRequest, Integer.valueOf(pnum),
                                            getLimit(vType, listLimit, thumbnailLimit), null, null
                                           );
        } else {
            pageInfoModule.setupPageRequest(pageInfoRequest, Integer.valueOf(pnum),
                                            getLimit(vType, listLimit, thumbnailLimit), getStypeMap().get(sType),
                                            getAsc(sType, asc, goodsListModel)
                                           );
        }
        // conditionDtoをセットする
        GoodsGroupSearchForDaoConditionDto conditionDto = setConditionDto(cid);

        ProductListGetRequest productListGetRequest =
                        goodsListHelper.toProductListGetRequest(conditionDto, goodsListModel);

        ProductListResponse productListResponse = null;
        try {
            productListResponse = productApi.getList(productListGetRequest, pageInfoRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return null;
        }

        if (ObjectUtils.isNotEmpty(productListResponse) && productListResponse.getPageInfo() != null) {
            // ページャーにレスポンス情報をセット
            PageInfoResponse pageInfoResponse = productListResponse.getPageInfo();

            // 検索前ページャーセットアップ
            pageInfoModule.setupPageInfo(goodsListModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                         pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                         pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages(), getStypeMap(),
                                         getSType(sType, goodsListModel), (String) null,
                                         getAsc(sType, asc, goodsListModel), getVType(vType)
                                        );
        }

        List<GoodsGroupDto> goodsGroupDtoList = goodsListHelper.toGoodsGroupDtos(productListResponse);

        // 検索後ページングセットアップ
        pageInfoModule.setupViewPager(goodsListModel.getPageInfo(), model);

        return goodsGroupDtoList;
    }

    /**
     * conditionDtoをセットする
     *
     * @param categoryId カテゴリID
     * @return goodsGroupSearchForDaoConditionDto
     */
    private GoodsGroupSearchForDaoConditionDto setConditionDto(String categoryId) {

        GoodsGroupSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(GoodsGroupSearchForDaoConditionDto.class);
        conditionDto.setCategoryId(categoryId);
        conditionDto.setMinPrice(null);
        conditionDto.setMaxPrice(null);
        conditionDto.setOpenStatus(HTypeOpenDeleteStatus.OPEN);

        return conditionDto;

    }

    /**
     * 画面最大表示件数取得
     *
     * @param vtype          表示形式
     * @param listLimit      リスト形式デフォルト最大表示件数
     * @param thumbnailLimit サムネイル形式デフォルト最大表示件数
     * @return 画面最大表示件数
     */
    private int getLimit(String vtype, int listLimit, int thumbnailLimit) {

        int limit;
        // 表示形式がリストならば
        if (PageInfo.VIEW_TYPE_LIST_KEY.equals(vtype)) {
            // リストの画面最大表示件数取得
            limit = listLimit;
        } else {
            // サムネイルの画面最大表示件数取得
            limit = thumbnailLimit;
        }
        return limit;
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
        stypeMap.put(PageInfo.SORT_TYPE_NORMAL_KEY, "normal");
        return stypeMap;
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
     * 並び順の取得
     *
     * @param sType          並び順
     * @param goodsListModel
     * @return 並び順
     */
    private String getSType(String sType, GoodsListModel goodsListModel) {

        // sTypeに値が設定されていれば
        if (sType != null) {
            // そのままsTypeを返す
            return sType;
        }

        // 手動表示フラグがONならば
        if (HTypeManualDisplayFlag.ON == goodsListModel.getManualDisplayFlag()) {
            // 並び順を標準順とする
            sType = PageInfo.SORT_TYPE_NORMAL_KEY;
        } else {
            // 並び順を新着順とする
            sType = PageInfo.SORT_TYPE_REGISTDATE_KEY;
        }
        return sType;
    }

    /**
     * 並び順の取得
     *
     * @param sType          並び順
     * @param asc            昇順
     * @param goodsListModel
     * @return 昇順かどうか
     */
    private boolean getAsc(String sType, boolean asc, GoodsListModel goodsListModel) {

        // sTypeに値が設定されていれば
        if (sType != null) {
            // そのままascを返す
            return asc;
        }

        // 手動表示フラグがONならば
        if (HTypeManualDisplayFlag.ON == goodsListModel.getManualDisplayFlag()) {
            asc = false;
        }
        return asc;
    }

    /**
     * プレビュー画面表示チェック
     *
     * @param goodsListModel
     * @param preKey         プレビューアクセスキー
     * @param preTime        プレビュー日時
     */
    protected void checkPreviewOpen(GoodsListModel goodsListModel, String preKey, String preTime) {

        boolean errFlag = false;
        // プレビューキーとプレビュー日時の両方が設定されていない場合
        if (StringUtils.isBlank(preKey) || StringUtils.isBlank(preTime)) {
            errFlag = true;
        }

        // プレビュー日時のチェック
        if (!this.dateUtility.isDate(preTime, this.dateUtility.YMD_HMS)) {
            errFlag = true;
        }

        if (errFlag) {
            throwMessage(MESSGE_CODE_PREVIEW_FAIL_OPEN);
        }

        // プレビューキーと有効期限をチェック
        try {
            PreviewAccessCheckRequest request = new PreviewAccessCheckRequest();
            request.setPreviewAccessKey(preKey);
            this.authorizationApi.chackPreviewAccess(request);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage(MESSGE_CODE_PREVIEW_NO_ACCESS);
        }

        // 正常なプレビュー画面遷移であるため、値を設定
        goodsListModel.setPreKey(preKey);
        goodsListModel.setPreTime(preTime);
        Timestamp tmp = this.dateUtility.toTimestampValue(preTime, this.dateUtility.YMD_HMS);
        goodsListModel.setPreviewDate(this.dateUtility.format(tmp, this.dateUtility.YMD_SLASH));
        goodsListModel.setPreviewTime(this.dateUtility.format(tmp, this.dateUtility.HMS));
    }

}
