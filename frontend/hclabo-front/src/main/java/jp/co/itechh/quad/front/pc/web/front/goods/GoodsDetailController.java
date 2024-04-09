/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.authorization.presentation.api.AuthorizationApi;
import jp.co.itechh.quad.authorization.presentation.api.param.PreviewAccessCheckRequest;
import jp.co.itechh.quad.browsinghistory.presentation.api.BrowsingHistoryApi;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListGetRequest;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryRegistRequest;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.TopicPathListResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.front.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.front.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsRelationSearchForDaoConditionDto;
import jp.co.itechh.quad.front.dto.memberinfo.wishlist.WishlistDto;
import jp.co.itechh.quad.front.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;
import jp.co.itechh.quad.front.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.front.utility.GoodsUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.inventory.presentation.api.InventoryApi;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryStatusDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import jp.co.itechh.quad.relation.presentation.api.RelationApi;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListGetRequest;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListResponse;
import jp.co.itechh.quad.wishlist.presentation.api.WishlistApi;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistListResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 商品詳細画面 Controller
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 */
@SessionAttributes(value = "goodsDetailModel")
@RequestMapping("/goods/")
@Controller
public class GoodsDetailController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsDetailController.class);

    /**
     * メッセージコード：プレビューアクセスチェックエラー
     */
    public static final String MESSGE_CODE_PREVIEW_NO_ACCESS = "PREVIEW-NO-ACCESS-001-";

    /**
     * メッセージコード：プレビューアクセスチェックエラー（リクエストパラメータ不備）
     */
    public static final String MESSGE_CODE_PREVIEW_FAIL_OPEN = "PREVIEW-NO-ACCESS-002-";

    /**
     * 商品情報取得スキップフラグ
     */
    public static final String FLASH_SKIP_GETGOODSINFO = "skipGetGoodsInfo";

    /**
     * 商品詳細画面：あしあと商品デフォルト件数
     */
    public static final String DEFAULT_FOOT_PRINT_GOODS_LIMIT = "5";

    /**
     * 商品詳細画面：お気に入り商品デフォルト件数
     */
    public static final String DEFAULT_WISHLISH_GOODS_LIMIT = "1000";

    /**
     * 商品詳細画面：関連商品デフォルト件数
     */
    public static final String DEFAULT_RELATED_GOODS_LIMIT = "5";

    /**
     * 商品詳細画面Helper
     */
    private final GoodsDetailHelper goodsDetailHelper;

    /**
     * 共通情報ヘルパークラス
     */
    private final CommonInfoUtility commonInfoUtility;

    /**
     * 日付関連Utilityクラス
     */
    private final DateUtility dateUtility;

    /**
     * 変換ユーティリティ
     */
    private final ConversionUtility conversionUtility;

    /**
     * 商品API
     */
    private final ProductApi productApi;

    /**
     * あしあとスエンドポイント API
     */
    private final BrowsingHistoryApi browsinghistoryApi;

    /**
     * お気に入りエンドポイント API
     **/
    private final WishlistApi wishlistApi;

    /**
     * カテゴリAPI
     */
    private final CategoryApi categoryApi;

    /**
     * 関連商品API
     */
    private final RelationApi relationApi;

    /**
     * 在庫API
     **/
    private final InventoryApi inventoryApi;

    /**
     * 権限グループAPI
     **/
    private final AuthorizationApi authorizationApi;

    /**
     * フラッシュスコープパラメータ：商品詳細画面から商品番号受取用
     */
    public static final String FLASH_GCD = "gcd";

    /**
     * フラッシュスコープパラメータ：商品詳細画面から商品グループ番号受取用
     */
    public static final String FLASH_GGCD = "ggcd";

    /**
     * フラッシュスコープパラメータ：商品詳細画面から商品規格1受取用
     */
    public static final String FLASH_UNIT = "redirectU1Lbl";

    /** コンストラクタ */
    @Autowired
    public GoodsDetailController(GoodsDetailHelper goodsDetailHelper,
                                 CommonInfoUtility commonInfoUtility,
                                 DateUtility dateUtility,
                                 ConversionUtility conversionUtility,
                                 ProductApi productApi,
                                 BrowsingHistoryApi browsinghistoryApi,
                                 WishlistApi wishlistApi,
                                 CategoryApi categoryApi,
                                 RelationApi relationApi,
                                 InventoryApi inventoryApi,
                                 AuthorizationApi authorizationApi) {
        this.goodsDetailHelper = goodsDetailHelper;
        this.commonInfoUtility = commonInfoUtility;
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
        this.productApi = productApi;
        this.browsinghistoryApi = browsinghistoryApi;
        this.wishlistApi = wishlistApi;
        this.categoryApi = categoryApi;
        this.relationApi = relationApi;
        this.inventoryApi = inventoryApi;
        this.authorizationApi = authorizationApi;
    }

    /**
     * 商品詳細画面：初期処理
     *
     * @param goodsDetailModel          商品詳細画面 Model
     * @param gcd                       商品コード
     * @param ggcd                      商品グループコード
     * @param cid                       カテゴリID
     * @param prekey                    プレビューキー
     * @param pretime                   プレビュー日時（yyyyMMddHHmmss）
     * @param browsingHistoryGoodsLimit あしあと商品件数
     * @param wishlistGoodsLimit        お気に入り商品件数
     * @param relatedGoodsLimit         関連商品件数
     * @param prekey                    プレビューキー
     * @param pretime                   プレビュー日時（yyyyMMddHHmmss）
     * @param error                     error
     * @param model                     model
     * @return 商品詳細画面
     */
    @GetMapping(value = "/detail")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/error")
    protected String doLoadIndex(GoodsDetailModel goodsDetailModel,
                                 @RequestParam(required = false) String gcd,
                                 @RequestParam(required = false) String ggcd,
                                 @RequestParam(required = false) String cid,
                                 @RequestParam(required = false) String hsn,
                                 @RequestParam(required = false, defaultValue = DEFAULT_FOOT_PRINT_GOODS_LIMIT)
                                                 int browsingHistoryGoodsLimit,
                                 @RequestParam(required = false, defaultValue = DEFAULT_WISHLISH_GOODS_LIMIT)
                                                 int wishlistGoodsLimit,
                                 @RequestParam(required = false, defaultValue = DEFAULT_RELATED_GOODS_LIMIT)
                                                 int relatedGoodsLimit,
                                 @RequestParam(required = false) String prekey,
                                 @RequestParam(required = false) String pretime,
                                 BindingResult error,
                                 Model model) {

        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);
        String accessUid = commonInfo.getCommonInfoBase().getAccessUid();

        // 商品情報の再取得が必要であれば
        if (isNeedGoodsInfo(goodsDetailModel, model)) {

            // 商品詳細画面モデルをクリア
            clearModel(GoodsDetailModel.class, goodsDetailModel, model);

            // 商品数量に1をセットする
            goodsDetailModel.setGcnt("1");
            // リクエストパラメータのgcd・ggcd・cidをセット
            goodsDetailModel.setGcd(gcd);
            goodsDetailModel.setGgcd(ggcd);
            goodsDetailModel.setCid(cid);
            goodsDetailModel.setHsn(hsn);

            // プレビュー画面表示チェック
            if (StringUtils.isNotBlank(prekey) || StringUtils.isNotBlank(pretime)) {
                // パラメータにどちらかが設定されている場合、プレビュー画面の遷移として扱う
                checkPreviewOpen(goodsDetailModel, prekey, pretime);
            }

            // 商品グループコード、商品コードの両方が指定されていない場合はエラーとする
            // ブラウザバック対応
            if (StringUtils.isEmpty(goodsDetailModel.getGgcd()) && StringUtils.isEmpty(goodsDetailModel.getGcd())) {
                throwMessage(GoodsDetailModel.MSGCD_GOODS_NOT_FOUND_ERROR);
            }

            // 画面表示処理
            setUpGoodsInfo(goodsDetailModel, error, browsingHistoryGoodsLimit, wishlistGoodsLimit, relatedGoodsLimit,
                           accessUid
                          );

            if (error.hasErrors()) {
                return "redirect:/error";
            }
        }

        // あしあと商品登録
        BrowsingHistoryRegistRequest browsinghistoryRegistRequest =
                        goodsDetailHelper.toBrowsingHistoryRegistRequest(goodsDetailModel.getGoodsGroupSeq());

        try {
            browsinghistoryApi.regist(accessUid, browsinghistoryRegistRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/error";
        }

        return "goods/detail";
    }

    /**
     * あしあと情報クリア処理(Ajax)<br/>
     * あしあとを削除するボタン押下時に呼び出される。<br/>
     * 自分のあしあと情報を全てクリアする。
     *
     * @param goodsDetailModel 商品詳細画面 Model
     */
    @GetMapping("/detail/doClearAccessGoods")
    @ResponseBody
    public void doClearAccessGoods(GoodsDetailModel goodsDetailModel) {

        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);
        String accessUid = commonInfo.getCommonInfoBase().getAccessUid();

        // あしあと商品クリアサービス実行
        try {
            browsinghistoryApi.delete(accessUid);
        }
        // サーバーエラーの場合、エラー画面へ遷移
        catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました ", se);
            handleServerError(se.getResponseBodyAsString());
        }
        // クライアントエラーの場合、何もしない
        catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました ", ce);
            return;
        }
        // セッションで保持しているモデルの足あと履歴をクリア
        goodsDetailModel.setBrowsingHistoryGoodsItems(null);
    }

    /**
     * お気に入り商品追加処理<br/>
     * ・ゲストがお気に入り追加した場合<br/>
     * ・Ajax通信でエラー発生した場合<br/>
     * にcall
     *
     * @param goodsDetailModel   商品詳細画面 Model
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return ページクラス
     */
    @PostMapping(value = "/detail", params = "doWishlistAdd")
    public String doWishlistAdd(GoodsDetailModel goodsDetailModel, RedirectAttributes redirectAttributes, Model model) {

        if (!StringUtils.isEmpty(goodsDetailModel.getGcd())) {
            redirectAttributes.addFlashAttribute("gcd", goodsDetailModel.getGcd());
        } else {
            // gcdが不足している場合、エラーメッセージ＋エラー画面を表示
            // goods/detailにredirectしても、最終的にエラーメッセージ＋エラー画面のため、この時点でエラー画面に遷移させる
            // 現行ではエラーメッセージ＋商品詳細画面を返すので、
            // それに合わせて、return goods/detail とすると、パラメータが残ってしまうので、ひとまずredirect:/errorで対応　
            addMessage(GoodsDetailModel.MSGCD_GOODS_NOT_FOUND_ERROR, redirectAttributes, model);
            return "redirect:/error";
        }

        return "redirect:/member/wishlist/";
    }

    /**
     * redirect to inquiry page
     *
     * @param goodsDetailModel   商品詳細画面 Model
     * @param redirectAttributes redirectAttributes
     * @param model              model
     * @return お問い合わせ画面
     */
    @PostMapping(value = "/detail", params = "doInquiry")
    public String doInquiry(GoodsDetailModel goodsDetailModel, RedirectAttributes redirectAttributes, Model model) {

        // 商品コードをお問い合わせ画面に引き継ぐ
        redirectAttributes.addFlashAttribute(FLASH_GCD, goodsDetailModel.getGcd());
        // 商品グループコードをお問い合わせ画面に引き継ぐ
        redirectAttributes.addFlashAttribute(FLASH_GGCD, goodsDetailModel.getGgcd());

        if (StringUtils.isNotEmpty(goodsDetailModel.getUnitSelect1())) {
            if (goodsDetailModel.getUnitSelect1Items() != null) {

                for (Map.Entry<String, String> unitSelect1ItemEntry : goodsDetailModel.getUnitSelect1Items()
                                                                                      .entrySet()) {
                    if (unitSelect1ItemEntry.getKey().equals(goodsDetailModel.getUnitSelect1())) {
                        // 選択された商品規格1の値をお問い合わせ画面に引き継ぐ
                        redirectAttributes.addFlashAttribute(FLASH_UNIT, unitSelect1ItemEntry.getValue());
                        break;
                    }
                }
            }
        }
        return "redirect:/inquiry/";
    }

    /**
     * 商品情報の再取得が必要か判断する<br/>
     *
     * @param goodsDetailModel 商品詳細画面 Model
     * @param model            Model
     * @return true:必要、false:不要
     */
    protected boolean isNeedGoodsInfo(GoodsDetailModel goodsDetailModel, Model model) {

        // 商品情報取得スキップフラグが設定されていれば
        if (model.containsAttribute(FLASH_SKIP_GETGOODSINFO)) {
            // 商品情報の再取得は不要
            return false;
        }

        // 商品情報の再取得は必要
        return true;

    }

    /**
     * 商品情報の取得と設定<br/>
     * 商品情報が取得できない場合はエラー画面に遷移させる（URL直接遷移・非公開商品等）<br/>
     *
     * @param goodsDetailModel          商品詳細画面 Model
     * @param browsingHistoryGoodsLimit あしあと商品件数
     * @param wishlistGoodsLimit        お気に入り商品件数
     * @param relatedGoodsLimit         関連商品件数
     */
    protected void setUpGoodsInfo(GoodsDetailModel goodsDetailModel,
                                  BindingResult error,
                                  int browsingHistoryGoodsLimit,
                                  int wishlistGoodsLimit,
                                  int relatedGoodsLimit,
                                  String accessUid) {

        // 遷移元のカテゴリ情報をページにセット
        setCategoryInfo(goodsDetailModel, error);

        if (error.hasErrors()) {
            return;
        }

        ProductDisplayResponse productDisplayResponse = null;
        try {
            // 商品情報取得
            ProductDisplayGetRequest productDisplayGetRequest = goodsDetailHelper.toProductGetRequest(goodsDetailModel);
            productDisplayResponse = productApi.getForDisplay(productDisplayGetRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("goodCode", "gcd");
            itemNameAdjust.put("goodsGroupCode", "ggcd");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }

        GoodsGroupDto goodsGroupDto = goodsDetailHelper.toGoodsGroupDto(productDisplayResponse);

        if (goodsGroupDto == null || goodsGroupDto.getGoodsGroupEntity() == null) {
            throwMessage(GoodsDetailModel.MSGCD_GOODS_NOT_FOUND_ERROR);
        }

        goodsDetailHelper.toPageForLoad(goodsGroupDto, goodsDetailModel);

        // 在庫一覧より非販売商品を除外
        excludeNoSaleGoodsFromStockList(goodsGroupDto, goodsDetailModel);

        // 公開あしあと商品詳細情報リスト取得サービス実行
        Integer goodsGroupSeq = null;

        if (!ObjectUtils.isEmpty(goodsGroupDto) && !ObjectUtils.isEmpty(goodsGroupDto.getGoodsGroupEntity())) {
            goodsGroupSeq = goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq();
        }

        BrowsingHistoryListGetRequest browsinghistoryListGetRequest =
                        goodsDetailHelper.toBrowsingHistoryListGetRequest(browsingHistoryGoodsLimit, goodsGroupSeq);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoBrowsingHistoryRequest = new PageInfoRequest();

        BrowsingHistoryListResponse browsinghistoryListResponse = null;
        try {
            browsinghistoryListResponse = browsinghistoryApi.get(accessUid, browsinghistoryListGetRequest,
                                                                 pageInfoBrowsingHistoryRequest
                                                                );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }

        List<GoodsGroupDto> browsinghistoryGoodsGroupDtoList =
                        goodsDetailHelper.toGoodsGroupDtoList(browsinghistoryListResponse);

        if (CollectionUtil.isEmpty(browsinghistoryGoodsGroupDtoList)) {
            goodsDetailModel.setBrowsingHistoryGoodsItems(null);

        } else {
            goodsDetailHelper.toPageForLoadBrowsingHistory(browsinghistoryGoodsGroupDtoList, goodsDetailModel);
        }

        List<WishlistDto> wishlistDtoList;
        // ※ゲストの場合は、この処理を行わない。
        if (commonInfoUtility.isLogin(getCommonInfo())) {
            WishlistSearchForDaoConditionDto wishlistConditionDto =
                            goodsDetailHelper.toWishlistConditionDtoForSearchWishlistList(wishlistGoodsLimit,
                                                                                          goodsDetailModel
                                                                                         );
            jp.co.itechh.quad.wishlist.presentation.api.param.PageInfoRequest pageInfoWishlistRequest =
                            goodsDetailHelper.toPageInfoWishlistRequest(wishlistConditionDto);

            WishlistListResponse wishlistListResponse = null;
            try {
                // お気に入り情報リスト取得サービス実行
                wishlistListResponse = wishlistApi.get(pageInfoWishlistRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                return;
            }

            wishlistDtoList = goodsDetailHelper.toWishlistDtoList(wishlistListResponse);

            if (wishlistDtoList != null) {
                // お気に入り情報に在庫状況表示を付与
                List<WishlistDto> stockWishlistDtoList = new ArrayList<>();

                for (WishlistDto wishlistDto : wishlistDtoList) {

                    InventoryStatusDisplayGetRequest inventoryStatusDisplayGetRequest =
                                    goodsDetailHelper.toInventoryStatusDisplayGetRequest(wishlistDto);

                    try {
                        wishlistDto.setStockStatus(inventoryApi.getGoodsStatus(inventoryStatusDisplayGetRequest)
                                                               .getCurrentStatus());
                    } catch (HttpClientErrorException | HttpServerErrorException e) {
                        LOGGER.error("例外処理が発生しました", e);
                        // アイテム名調整マップ
                        Map<String, String> itemNameAdjust = new HashMap<>();
                        itemNameAdjust.put("openStatus", "goodsOpenStatusPC");
                        itemNameAdjust.put("openStartTime", "openStartTimePC");
                        itemNameAdjust.put("openEndTime", "openEndTimePC");
                        handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                        return;
                    }

                    stockWishlistDtoList.add(wishlistDto);
                }

                if (CollectionUtil.isNotEmpty(stockWishlistDtoList)) {
                    goodsDetailHelper.toPageForLoadWishlist(stockWishlistDtoList, goodsDetailModel);
                }
            }

            jp.co.itechh.quad.wishlist.presentation.api.param.PageInfoRequest pageInforWishlistRequest =
                            goodsDetailHelper.toPageInfoWishlistRequest(wishlistConditionDto);

            WishlistListResponse wishlistLstResponse = null;
            try {
                wishlistLstResponse = wishlistApi.get(pageInforWishlistRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                return;
            }

            List<WishlistEntity> wishlistList = goodsDetailHelper.toWishlistEntityList(wishlistLstResponse);
            goodsDetailHelper.setWishlistView(wishlistList, goodsGroupDto, goodsDetailModel);
        }

        // 関連商品表示処理
        goodsRelationDisplay(relatedGoodsLimit, goodsDetailModel, error);

    }

    /**
     * 遷移元のカテゴリ情報をページにセット<br/>
     *
     * @param goodsDetailModel 商品詳細画面 Model
     */
    protected void setCategoryInfo(GoodsDetailModel goodsDetailModel, BindingResult error) {
        String categoryId = goodsDetailModel.getCid();

        // カテゴリ情報を取得
        CategoryDetailsDto categoryDetailsDto;

        if (StringUtils.isNotEmpty(categoryId)) {
            CategorySearchForDaoConditionDto conditionDto =
                            ApplicationContextUtility.getBean(CategorySearchForDaoConditionDto.class);
            conditionDto.setCategoryId(categoryId);
            conditionDto.setOpenStatus(HTypeOpenStatus.OPEN);

            CategoryResponse categoryResponse = null;
            try {
                categoryResponse = categoryApi.getByCategoryId(conditionDto.getCategoryId(), null);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                return;
            }

            categoryDetailsDto = goodsDetailHelper.toCategoryDetailsDto(categoryResponse);
            goodsDetailHelper.toPageForLoadCategoryDto(categoryDetailsDto, goodsDetailModel);

            // パンくず情報を取得
            List<CategoryDetailsDto> categoryDetailsDtoList = null;

            if (categoryDetailsDto != null) {
                TopicPathListResponse topicPathListResponse = null;

                try {
                    TopicPathGetRequest topicPathGetRequest = new TopicPathGetRequest();
                    topicPathGetRequest.setHierarchicalSerialNumber(goodsDetailModel.getHsn());

                    if (StringUtils.isNotBlank(goodsDetailModel.getPreKey())) {
                        topicPathGetRequest.setFrontDisplayReferenceDate(
                                        this.dateUtility.toTimestampValue(goodsDetailModel.getPreTime(),
                                                                          this.dateUtility.YMD_HMS
                                                                         ));
                    }

                    topicPathListResponse =
                                    categoryApi.getCategoryTopicPath(goodsDetailModel.getCid(), topicPathGetRequest);

                } catch (HttpClientErrorException | HttpServerErrorException e) {
                    LOGGER.error("例外処理が発生しました", e);
                    // アイテム名調整マップ
                    Map<String, String> itemNameAdjust = new HashMap<>();
                    handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                    return;
                }

                categoryDetailsDtoList = goodsDetailHelper.toCategoryDetailsDtoList(topicPathListResponse);
            }
            goodsDetailHelper.toPageForLoadForTopicPath(categoryDetailsDtoList, goodsDetailModel);
        }

    }

    /**
     * 在庫一覧の非販売商品を除外<br/>
     *
     * @param goodsGroupDto    商品グループDTO
     * @param goodsDetailModel 商品詳細画面 Model
     */
    protected void excludeNoSaleGoodsFromStockList(GoodsGroupDto goodsGroupDto, GoodsDetailModel goodsDetailModel) {
        if (CollectionUtil.isEmpty(goodsDetailModel.getGoodsStockItems())) {
            return;
        }

        // 現在日 or プレビュー日時を設定
        Timestamp targetTime = this.dateUtility.getCurrentTime();
        if (StringUtils.isNotBlank(goodsDetailModel.getPreKey())) {
            targetTime = this.dateUtility.toTimestampValue(goodsDetailModel.getPreTime(), this.dateUtility.YMD_HMS);
        }

        // 商品系Helper取得
        GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);
        Iterator<GoodsStockItem> it = goodsDetailModel.getGoodsStockItems().iterator();
        goodsDetailModel.setExistsSaleStatusGoods(false);
        while (it.hasNext()) {
            GoodsStockItem item = it.next();
            for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
                if (!ObjectUtils.isEmpty(goodsDto.getGoodsEntity())
                    && goodsDto.getGoodsEntity().getGoodsSeq() != null) {
                    if (goodsDto.getGoodsEntity().getGoodsSeq().equals(item.getGoodsSeq())) {
                        // 販売不可の商品は削除する
                        if (goodsUtility.isGoodsSales(goodsDto.getGoodsEntity().getSaleStatusPC(),
                                                      goodsDto.getGoodsEntity().getSaleStartTimePC(),
                                                      goodsDto.getGoodsEntity().getSaleEndTimePC(), targetTime
                                                     )) {
                            goodsDetailModel.setExistsSaleStatusGoods(true);
                        } else {
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    /**
     * 関連商品表示処理<br/>
     * ※アクセス修飾子は、public 商品詳細画面からサブアプリケーション切れの対応の為
     *
     * @param relatedGoodsLimit 関連商品件数
     * @param goodsDetailModel  商品詳細画面 Model
     */
    public void goodsRelationDisplay(int relatedGoodsLimit, GoodsDetailModel goodsDetailModel, BindingResult error) {
        List<GoodsGroupDto> relatedGoodsGroupDtoList = new ArrayList<>();

        // 商品グループコード 又は、 商品コードがある場合のみ
        if (StringUtils.isNotEmpty(goodsDetailModel.getGgcd()) || StringUtils.isNotEmpty(goodsDetailModel.getGcd())) {

            ProductDisplayGetRequest productDisplayGetRequest = goodsDetailHelper.toProductGetRequest(goodsDetailModel);

            ProductDisplayResponse productDisplayResponse = null;
            try {
                // 商品グループ情報を取得
                productDisplayResponse = productApi.getForDisplay(productDisplayGetRequest);

            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                itemNameAdjust.put("goodCode", "gcd");
                itemNameAdjust.put("goodsGroupCode", "ggcd");
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                return;
            }
            GoodsGroupDto goodsGroupDto = goodsDetailHelper.toGoodsGroupDto(productDisplayResponse);

            if (goodsGroupDto != null) {
                Integer currentGoodsGroupSeq = goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq();
                GoodsRelationSearchForDaoConditionDto conditionDto =
                                ApplicationContextUtility.getBean(GoodsRelationSearchForDaoConditionDto.class);
                conditionDto.setGoodsGroupSeq(currentGoodsGroupSeq);
                conditionDto.setOpenStatus(HTypeOpenDeleteStatus.OPEN);
                // PageInfoModule取得
                PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
                // ページングセットアップ
                conditionDto = pageInfoModule.setupPageInfoForSkipCount(conditionDto, relatedGoodsLimit, null, true);

                RelationGoodsListGetRequest relationGoodsListGetRequest =
                                goodsDetailHelper.toRelationGoodsListGetRequest(conditionDto, goodsDetailModel);
                jp.co.itechh.quad.relation.presentation.api.param.PageInfoRequest pageInfoRelationRequest =
                                goodsDetailHelper.toPageInfoRelationRequest(conditionDto);

                RelationGoodsListResponse relationGoodsListResponse = null;
                try {
                    relationGoodsListResponse =
                                    relationApi.get(conditionDto.getGoodsGroupSeq(), relationGoodsListGetRequest,
                                                    pageInfoRelationRequest
                                                   );
                } catch (HttpClientErrorException | HttpServerErrorException e) {
                    LOGGER.error("例外処理が発生しました", e);
                    // アイテム名調整マップ
                    Map<String, String> itemNameAdjust = new HashMap<>();
                    handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                    return;
                }
                relatedGoodsGroupDtoList = goodsDetailHelper.toGoodsGroupDtoList(relationGoodsListResponse);
            }
        }

        // ページに反映
        goodsDetailHelper.toPageForLoadRelated(relatedGoodsGroupDtoList, goodsDetailModel);

    }

    /**
     * プレビュー画面表示チェック
     *
     * @param goodsDetailModel 商品詳細画面 Model
     * @param preKey           プレビューアクセスキー
     * @param preTime          プレビュー日時
     */
    protected void checkPreviewOpen(GoodsDetailModel goodsDetailModel, String preKey, String preTime) {

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
        goodsDetailModel.setPreKey(preKey);
        goodsDetailModel.setPreTime(preTime);
        Timestamp tmp = this.dateUtility.toTimestampValue(preTime, this.dateUtility.YMD_HMS);
        goodsDetailModel.setPreviewDate(this.dateUtility.format(tmp, this.dateUtility.YMD_SLASH));
        goodsDetailModel.setPreviewTime(this.dateUtility.format(tmp, this.dateUtility.HMS));
    }

}