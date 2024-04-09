/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.cart;

import jp.co.itechh.quad.addressbook.presentation.api.param.ServerErrorResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.BrowsingHistoryApi;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListGetRequest;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.front.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.front.base.application.HmMessages;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.front.dto.cart.CartDto;
import jp.co.itechh.quad.front.dto.cart.CartGoodsDto;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsRelationSearchForDaoConditionDto;
import jp.co.itechh.quad.front.dto.memberinfo.wishlist.WishlistDto;
import jp.co.itechh.quad.front.entity.cart.CartGoodsEntity;
import jp.co.itechh.quad.front.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.front.utility.CartUtility;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.ClientErrorMessageUtility;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.inventory.presentation.api.InventoryApi;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryStatusDisplayGetRequest;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryStatusDisplayResponse;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemCountListUpdateRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemDeleteRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRegistRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.pricecalculator.presentation.api.PriceCalculatorApi;
import jp.co.itechh.quad.pricecalculator.presentation.api.param.ItemSalesPriceCalculateRequest;
import jp.co.itechh.quad.pricecalculator.presentation.api.param.ItemSalesPriceCalculateResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailByGoodCodeGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.relation.presentation.api.RelationApi;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListGetRequest;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListResponse;
import jp.co.itechh.quad.wishlist.presentation.api.WishlistApi;
import jp.co.itechh.quad.wishlist.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistListResponse;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ショッピングカートController<br/>
 *
 * @author Pham Quang Dieu
 */
@SessionAttributes(value = "cartModel")
@RequestMapping("/cart")
@Controller
public class CartController extends AbstractController {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    /** フラッシュスコープパラメータ：カートからの遷移判定 */
    public static final String FROM_CART = "fromCart";

    /** 該当商品がありません */
    public static final String MSGCD_NOT_FOUND_GCD = "SMF000101";

    /** お気に入りリスト：デフォルト最大表示件数 */
    private static final int DEFAULT_WISHLISH_GOODS_LIMIT =
                    PropertiesUtil.getSystemPropertiesValueToInt("wishlist.goods.max");

    /** あしあとリスト：デフォルト最大表示件数 */
    private static final int DEFAULT_FOOTPRINT_GOODS_LIMIT = 6;

    /** 関連商品リスト：デフォルト最大表示件数 */
    private static final int DEFAULT_RELATED_GOODS_LIMIT = 6;

    /** カート計算エラー */
    private static final String MSGCD_CART_CALC_ERROR = "ACX000116";

    /** ショッピングカートHelper */
    private CartHelper cartHelper;

    /** CommonInfoUtility */
    private CommonInfoUtility commonInfoUtility;

    /** CartUtility */
    private CartUtility cartUtility;

    /** あしあとスエンドポイント API */
    private final BrowsingHistoryApi browsinghistoryApi;

    /** お気に入りエンドポイント API **/
    private final WishlistApi wishlistApi;

    /** 関連商品API */
    private final RelationApi relationApi;

    /** 在庫API */
    private final InventoryApi inventoryApi;

    /** 注文票API */
    private final OrderSlipApi orderSlipApi;

    /** 商品API */
    private final ProductApi productApi;

    /** 計算ユーティリティ */
    private PriceCalculatorApi priceCalculatorApi;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** デフォルトページ番号 */
    private static final Integer DEFAULT_PNUM = 1;

    /** ショッピングカート : ソート項目 */
    private static final String DEFAULT_CARTSEARCH_ORDER_FIELD = "updateTime";

    /** グローバルエラーメッセージ用フィールド名 */
    public static final String GLOBAL_MESSAGE_FIELD_NAME = "common";

    /**
     * コンストラクタ
     * @param cartHelper
     * @param commonInfoUtility
     * @param cartUtility
     * @param browsinghistoryApi
     * @param wishlistApi
     * @param relationApi
     * @param inventoryApi
     * @param orderSlipApi
     * @param productApi
     * @param priceCalculatorApi
     * @param conversionUtility
     */
    @Autowired
    public CartController(CartHelper cartHelper,
                          CommonInfoUtility commonInfoUtility,
                          CartUtility cartUtility,
                          BrowsingHistoryApi browsinghistoryApi,
                          WishlistApi wishlistApi,
                          RelationApi relationApi,
                          InventoryApi inventoryApi,
                          OrderSlipApi orderSlipApi,
                          ProductApi productApi,
                          PriceCalculatorApi priceCalculatorApi,
                          ConversionUtility conversionUtility) {
        this.cartHelper = cartHelper;
        this.commonInfoUtility = commonInfoUtility;
        this.cartUtility = cartUtility;
        this.browsinghistoryApi = browsinghistoryApi;
        this.wishlistApi = wishlistApi;
        this.relationApi = relationApi;
        this.inventoryApi = inventoryApi;
        this.orderSlipApi = orderSlipApi;
        this.productApi = productApi;
        this.priceCalculatorApi = priceCalculatorApi;
        this.conversionUtility = conversionUtility;
    }

    /**
     * カート画面：初期表示処理<br/>
     * 呼び出し元画面より渡された商品SEQ、数量を元にカート商品情報を作成し、<br/>
     * カート情報に追加する。<br/>
     * 現在のカート情報、お気に入り商品情報、あしあと情報、関連商品情報を<br/>
     * 取得し、ページ情報にセットする。<br/>
     *
     * @param gcd
     * @param gcnt
     * @param cartModel
     * @param bindingResult
     * @param redirectAttributes
     * @param model
     * @return カート画面
     */
    @GetMapping("/")
    @HEHandler(exception = AppLevelListException.class, returnView = "cart/index")
    protected String doLoadIndex(@RequestParam(required = false) String gcd,
                                 @RequestParam(required = false) String gcnt,
                                 CartModel cartModel,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        clearModel(CartModel.class, cartModel, model);

        // （１） カートに追加する商品の商品情報を取得し、カート商品DTOの作成
        boolean cartAddFlag = addGoodsToCart(gcd, gcnt, redirectAttributes, model, bindingResult);

        if (bindingResult.hasErrors()) {
            return "cart/index";
        }

        // （２） ≪画面表示情報取得処理≫実行
        setDisplayInfo(cartModel, bindingResult, redirectAttributes, model);

        if (bindingResult.getAllErrors().stream().anyMatch(err -> MSGCD_CART_CALC_ERROR.equals(err.getCode()))) {
            addMessage(MSGCD_CART_CALC_ERROR, redirectAttributes, model);
            return "redirect:/error";
        }

        if (bindingResult.hasErrors()) {
            return "cart/index";
        }

        // カート追加を行った時URLパラメータを消す
        if (cartAddFlag) {
            return "redirect:/cart/";
        }

        // カート画面を表示する
        return "cart/index";
    }

    /**
     * カート画面：再計算処理<br/>
     * 再計算ボタン、商品合計再計算ボタン押下時に呼び出される。<br/>
     * カート内商品情報、数量を元に、金額、合計金額、数量の再計算を行う。<br/>
     *
     * @param cartModel
     * @param error
     * @param redirectAttributes
     * @return model
     * @return カート画面
     */
    @PostMapping(value = "/", params = "doCalculate")
    @HEHandler(exception = AppLevelListException.class, returnView = "cart/index")
    public String doCalculate(@Validated CartModel cartModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (error.hasErrors()) {
            return "cart/index";
        }

        // （１） カート画面より、カート一覧情報と数量を取得する。
        CartDto cartDto = cartHelper.toCartDtoForCalculate(cartModel);
        List<CartGoodsDto> cartGoodsDtoList = cartDto.getCartGoodsDtoList();

        try {
            OrderItemCountListUpdateRequest orderItemCountListUpdateRequest =
                            cartHelper.toOrderItemCountListUpdateRequest(cartGoodsDtoList);
            // （２） 【カート商品数量変更サービス】実行
            orderSlipApi.updateOrderItemCount(orderItemCountListUpdateRequest);
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // サーバーエラーの場合、エラー画面へ遷移
            handleServerError(se.getResponseBodyAsString());

        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // クライアントエラーの場合、、エラーメッセージを取得する
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
            ClientErrorResponse clientError =
                            conversionUtility.toObject(ce.getResponseBodyAsString(), ClientErrorResponse.class);
            // カート商品チェックサービスの結果をエラー情報に追加
            cartHelper.toPageForLoad(clientError.getMessages(), cartModel);
            addErrorInfo(clientError.getMessages(), cartDto, error, redirectAttributes, model);
            // カート画面を再表示
            return "cart/index";
        }

        // （３） ≪画面表示情報取得処理≫実行
        setDisplayInfo(cartModel, error, redirectAttributes, model);

        if (error.getAllErrors().stream().anyMatch(err -> MSGCD_CART_CALC_ERROR.equals(err.getCode()))) {
            addMessage(MSGCD_CART_CALC_ERROR, redirectAttributes, model);
            return "redirect:/error";
        }

        // （４） 自画面を表示
        return "cart/index";
    }

    /**
     * カート画面：カート内商品削除処理<br/>
     * 削除ボタン押下時に呼び出される。<br/>
     * カート内の指定した商品をカート情報より削除する。<br/>
     *
     * @param cartModel
     * @param bindingResult
     * @param redirectAttributes
     * @return model
     * @return カート画面
     */
    @PostMapping(value = "/", params = "doDeleteGoods")
    @HEHandler(exception = AppLevelListException.class, returnView = "cart/index")
    public String doDeleteGoods(CartModel cartModel,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // （１） カート画面より、カート情報を取得する。
        // 削除対象のカート商品SEQを取得
        int index = cartModel.getCartGoodsIndex();

        if (cartModel.getCartGoodsItems().size() > index) {
            // （２） 取得したカート情報をリストにセット
            Integer goodsSeq = cartModel.getCartGoodsItems().get(index).getGoodsSeq();

            try {
                OrderItemDeleteRequest orderItemDeleteRequest = new OrderItemDeleteRequest();
                orderItemDeleteRequest.setItemId(goodsSeq.toString());

                // （３） 【カート商品削除サービス】実行
                orderSlipApi.deleteOrderItem(orderItemDeleteRequest);
            } catch (HttpServerErrorException se) {
                LOGGER.error("例外処理が発生しました", se);
                // サーバーエラーの場合、エラー画面へ遷移
                handleServerError(se.getResponseBodyAsString());

            } catch (HttpClientErrorException ce) {
                LOGGER.error("例外処理が発生しました", ce);
                // クライアントエラーの場合、、エラーメッセージを取得する
                ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
                ClientErrorResponse clientError =
                                conversionUtility.toObject(ce.getResponseBodyAsString(), ClientErrorResponse.class);
                // カート商品チェックサービスの結果をエラー情報に追加
                CartDto cartDto = cartHelper.toCartDtoForCalculate(cartModel);
                cartHelper.toPageForLoad(clientError.getMessages(), cartModel);
                addErrorInfo(clientError.getMessages(), cartDto, bindingResult, redirectAttributes, model);
                // カート画面を再表示
                return "cart/index";
            }
        }

        // （４） ≪画面表示情報取得処理≫実行
        setDisplayInfo(cartModel, bindingResult, redirectAttributes, model);

        // （５）自画面を表示
        return "cart/index";
    }

    /**
     * カート画面：注文処理_購入手続き<br/>
     * 「ご購入手続きに進む」ボタン押下時に呼び出される。<br/>
     * 現在のカート情報で再計算を行い、注文確認画面を呼び出す。<br/>
     *
     * @param cartModel
     * @param error
     * @param redirectAttributes
     * @return model
     * @return 注文確認画面
     */
    @PostMapping(value = "/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "cart/index")
    public String doConfirm(@Validated CartModel cartModel,
                            BindingResult error,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (error.hasErrors()) {
            return "cart/index";
        }

        CartDto cartDto = orderCommonAction(cartModel, error, redirectAttributes, model);

        if (error.getAllErrors().stream().anyMatch(err -> MSGCD_CART_CALC_ERROR.equals(err.getCode()))) {
            addMessage(MSGCD_CART_CALC_ERROR, redirectAttributes, model);
            return "redirect:/error";
        }

        if (hasMessages(model)) {
            return "cart/index";
        }

        try {
            // （３） 【カート商品チェックサービス】実行
            orderSlipApi.checkDraft(getCommonInfo().getCommonInfoUser().getMemberInfoBirthday(), null);
        }
        // サーバーエラーの場合、エラー画面へ遷移
        catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        }
        // クライアントエラーの場合、、エラーメッセージを取得する
        catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // クライアントエラー
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
            ClientErrorResponse clientError =
                            conversionUtility.toObject(ce.getResponseBodyAsString(), ClientErrorResponse.class);
            // カート商品チェックサービスの結果をエラー情報に追加
            cartHelper.toPageForLoad(clientError.getMessages(), cartModel);
            addErrorInfo(clientError.getMessages(), cartDto, error, redirectAttributes, model);
            // （４） エラーが存在する場合は、カート商品チェックサービスの結果をエラー情報に追加して終了
            return "cart/index";
        }

        // （５） エラーが存在しない場合は、遷移元フラグを設定し、注文_依頼主入力画面に遷移
        redirectAttributes.addFlashAttribute(FROM_CART, null);
        return "redirect:/order/confirm";
    }

    /**
     * カート画面：あしあと情報クリア処理(Ajax)<br/>
     * あしあとを削除するボタン押下時に呼び出される。<br/>
     * 自分のあしあと情報を全てクリアする。
     *
     * @param cartModel
     */
    @GetMapping("/doClearAccessGoods")
    @ResponseBody
    public void doClearAccessGoods(CartModel cartModel) {

        // 【あしあと商品クリアサービス】実行
        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);
        String accessUid = commonInfo.getCommonInfoBase().getAccessUid();
        try {
            browsinghistoryApi.delete(accessUid);
        }
        // クライアントエラーの場合、何もしない
        catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            return;
        }
        // サーバーエラーの場合、エラー画面へ遷移
        catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        }

        //  セッションで保持しているモデルの足あと履歴をクリア
        cartModel.setBrowsingHistoryGoodsItems(null);
    }

    /**
     * お気に入り商品追加処理<br/>
     * ・ゲストがお気に入り追加した場合<br/>
     * ・Ajax通信でエラー発生した場合<br/>
     * にcall
     *
     * @param cartModel
     * @param redirectAttributes
     * @param model
     *
     * @return ページクラス
     */
    @PostMapping(value = "/", params = "doWishlistAdd")
    public String doWishlistAdd(CartModel cartModel, RedirectAttributes redirectAttributes, Model model) {

        //対象のカート商品を取得
        int index = cartModel.getCartGoodsIndex();
        CartModelItem goods = cartModel.getCartGoodsItems().get(index);

        if (goods != null && !StringUtils.isEmpty(goods.getGcd())) {
            redirectAttributes.addFlashAttribute("gcd", goods.getGcd());
        } else {
            // gcdが不足している場合、カート画面でエラーメッセージを表示
            addMessage(CartModel.MSGCD_PAGE_RELOAD, redirectAttributes, model);
            return "redirect:/cart/";
        }

        return "redirect:/member/wishlist/";
    }

    /**
     * カート投入処理<br/>
     * 商品コード、商品数量があれば、カートに投入する<br/>
     *
     * @param gcd 商品コード
     * @param gcnt 商品数量
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model Model
     * @param error BindingResult
     * @return カート投入フラグ true..エラーなし
     */
    protected boolean addGoodsToCart(String gcd,
                                     String gcnt,
                                     RedirectAttributes redirectAttributes,
                                     Model model,
                                     BindingResult error) {

        boolean cartAddFlag = false;

        // 商品情報を受け取っていない場合
        if (StringUtils.isEmpty(gcd) || !StringUtils.isNumeric(gcnt)) {
            // カート投入しなかった
            return cartAddFlag;
        }

        // ① 商品情報が取得できた場合
        // ・【カート投入サービス】実行
        OrderItemRegistRequest orderItemRegistRequest = new OrderItemRegistRequest();

        /* 商品コードを商品SEQに変換する START */
        ProductDetailByGoodCodeGetRequest productDetailByGoodCodeGetRequest = new ProductDetailByGoodCodeGetRequest();
        productDetailByGoodCodeGetRequest.setGoodsCode(gcd);
        GoodsDetailsResponse goodsDetailsResponse = null;
        try {
            goodsDetailsResponse = productApi.getDetailsByGoodsCode(gcd, productDetailByGoodCodeGetRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return cartAddFlag;
        }

        if (goodsDetailsResponse == null) {
            String[] arg = new String[] {AppLevelFacesMessageUtil.getAllMessage(MSGCD_NOT_FOUND_GCD,
                                                                                null
                                                                               ).getMessage()};
            this.addMessage(CartModel.MSGCD_CART_ADD_ERROR, arg, redirectAttributes, model);

            return cartAddFlag;
        } else {
            orderItemRegistRequest.setItemId(conversionUtility.toString(goodsDetailsResponse.getGoodsSeq()));
        }
        /* 商品コードを商品SEQに変換する END */

        orderItemRegistRequest.setItemCount(Integer.valueOf(gcnt));

        List<String> errorMsgList = null;
        try {
            // カート商品追加サービスの実行
            orderSlipApi.registOrderItem(
                            orderItemRegistRequest, conversionUtility.toDate(
                                            getCommonInfo().getCommonInfoUser().getMemberInfoBirthday()));
        }
        // サーバーエラーの場合、エラー画面へ遷移
        catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        }
        // クライアントエラーの場合、、エラーメッセージを取得する
        catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            ClientErrorMessageUtility clientErrorMessageUtility =
                            ApplicationContextUtility.getBean(ClientErrorMessageUtility.class);
            errorMsgList = clientErrorMessageUtility.getMessage(ce.getResponseBodyAsString());
        }
        // ② ・カート投入サービス実行時の戻り値を、エラー情報にセットする。
        if (CollectionUtil.isNotEmpty(errorMsgList)) {
            StringBuilder s = new StringBuilder();
            for (String errorMsg : errorMsgList) {
                if (s.length() > 0) {
                    // カート投入不可理由が複数ある場合は「・」で区切る
                    s.append("・");
                }
                // カート投入不可理由メッセージ(※)に引数がある場合は組み立て済みのメッセージに置き換える
                // ※エラーメッセージ引数となる別のメッセージ
                s.append(errorMsg);
            }
            String[] arg = new String[] {s.toString()};
            this.addMessage(CartModel.MSGCD_CART_ADD_ERROR, arg, redirectAttributes, model);
        } else {
            cartAddFlag = true;
        }

        return cartAddFlag;
    }

    /**
     * 画面表示情報取得<br/>
     * 画面に表示する情報を取得し、ページクラスにセットします。<br/>
     *
     * @param cartModel カートModel
     * @param bindingResult
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model Model
     */
    protected void setDisplayInfo(CartModel cartModel,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        // （１） 【カート商品リスト取得サービス】実行
        CartDto cartDto = getCartDto(bindingResult);

        // （２） 【お気に入り情報リスト取得サービス】実行
        List<WishlistDto> wishlistDtoList = null;
        WishlistListResponse wishlistListResponse = null;

        if (commonInfoUtility.isLogin(getCommonInfo())) {
            // ページング検索セットアップ
            PageInfoRequest pageInfoRequestWishlist = new PageInfoRequest();

            // ページング検索セットアップ
            PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModule.setupPageRequest(pageInfoRequestWishlist, DEFAULT_PNUM, DEFAULT_WISHLISH_GOODS_LIMIT,
                                            DEFAULT_CARTSEARCH_ORDER_FIELD, true
                                           );

            try {
                // お気に入りリスト検索実行
                wishlistListResponse = wishlistApi.get(pageInfoRequestWishlist);
                wishlistDtoList = cartHelper.toWishlistDtoList(wishlistListResponse);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), bindingResult, itemNameAdjust);
                return;
            }
        }

        // （３） 【公開あしあと商品詳細情報リスト取得サービス】実行
        // LIMIT指定無しの場合はデフォルト値をセット
        if (cartModel.getBrowsingHistoryGoodsLimit() == 0) {
            cartModel.setBrowsingHistoryGoodsLimit(DEFAULT_FOOTPRINT_GOODS_LIMIT);
        }

        BrowsingHistoryListGetRequest browsinghistoryListGetRequest = new BrowsingHistoryListGetRequest();
        browsinghistoryListGetRequest.setBrowsingHistoryGoodsLimit(cartModel.getBrowsingHistoryGoodsLimit());

        List<GoodsGroupDto> browsinghistoryGoodsGroupDtoList = null;
        try {
            BrowsingHistoryListResponse browsinghistoryListResponse =
                            browsinghistoryApi.get(getCommonInfo().getCommonInfoBase().getAccessUid(),
                                                   browsinghistoryListGetRequest, null
                                                  );
            browsinghistoryGoodsGroupDtoList = cartHelper.toBrowsingHistoryList(browsinghistoryListResponse);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), bindingResult, itemNameAdjust);
            return;
        }

        // （４） 【公開関連商品情報リスト取得サービス】実行
        List<GoodsGroupDto> relatedGoodsGroupDtoList = null;
        if (cartDto != null && CollectionUtil.isNotEmpty(cartDto.getCartGoodsDtoList())) {
            // 検索条件Dto生成
            Integer newestGoodsGroupSeq = cartDto.getCartGoodsDtoList().get(0).getGoodsDetailsDto().getGoodsGroupSeq();
            GoodsRelationSearchForDaoConditionDto conditionDto =
                            ApplicationContextUtility.getBean(GoodsRelationSearchForDaoConditionDto.class);
            conditionDto.setGoodsGroupSeq(newestGoodsGroupSeq);

            // LIMIT指定無しの場合はデフォルト値をセット
            if (cartModel.getRelatedGoodsLimit() == 0) {
                cartModel.setRelatedGoodsLimit(DEFAULT_RELATED_GOODS_LIMIT);
            }

            // 関連商品リクエストを設定
            RelationGoodsListGetRequest relationGoodsListGetRequest = new RelationGoodsListGetRequest();
            relationGoodsListGetRequest.setOpenStatus(HTypeOpenDeleteStatus.OPEN.getValue());

            jp.co.itechh.quad.relation.presentation.api.param.PageInfoRequest pageInfoRequestRelation =
                            new jp.co.itechh.quad.relation.presentation.api.param.PageInfoRequest();
            // ページング検索セットアップ
            PageInfoModule pageInfoModuleRelation = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModuleRelation.setupPageRequest(pageInfoRequestRelation, pageInfoRequestRelation.getPage(),
                                                    cartModel.getRelatedGoodsLimit(), null, true
                                                   );

            try {
                RelationGoodsListResponse relationGoodsListResponse =
                                relationApi.get(conditionDto.getGoodsGroupSeq(), relationGoodsListGetRequest,
                                                pageInfoRequestRelation
                                               );
                relatedGoodsGroupDtoList = cartHelper.toRelationGoodsGroupDtoList(relationGoodsListResponse);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), bindingResult, itemNameAdjust);
                return;
            }
        }

        // （６） 取得したカート商品情報、お気に入り情報、あしあと情報、関連商品情報、お知らせをModelクラスにセットする。
        // カート商品情報設定
        cartHelper.toPageForLoad(cartDto, cartModel);

        // お気に入り商品情報設定
        if (wishlistDtoList != null) {
            // お気に入り情報に在庫状況表示を付与
            List<WishlistDto> stockWishlistDtoList = executeWishlistGoodsStockStatus(wishlistDtoList, bindingResult);
            if (bindingResult.hasErrors()) {
                return;
            }

            cartHelper.toPageForLoadWishlist(stockWishlistDtoList, cartModel);

            List<WishlistEntity> wishlistList = cartHelper.toWishlistEntityList(wishlistListResponse);
            cartHelper.setWishlistView(wishlistList, cartModel);

        } else {
            cartModel.setWishlistGoodsItems(new ArrayList<CartModelItem>());
        }

        // あしあと商品情報設定
        cartHelper.toPageForLoadBrowsingHistory(browsinghistoryGoodsGroupDtoList, cartModel);

        // 関連商品情報設定
        if (CollectionUtil.isNotEmpty(relatedGoodsGroupDtoList)) {
            cartHelper.toPageForLoadRelated(relatedGoodsGroupDtoList, cartModel);
        } else {
            cartModel.setRelatedGoodsItems(new ArrayList<CartModelItem>());
        }

        // （７） 【カート商品チェックサービス】実行
        ClientErrorResponse clientError = new ClientErrorResponse();
        if (cartDto != null) {
            try {
                orderSlipApi.checkDraft(getCommonInfo().getCommonInfoUser().getMemberInfoBirthday(), null);
            }
            // サーバーエラーの場合、エラー画面へ遷移
            catch (HttpServerErrorException se) {
                LOGGER.error("例外処理が発生しました", se);
                handleServerError(se.getResponseBodyAsString());
            }
            // クライアントエラーの場合、、エラーメッセージを取得する
            catch (HttpClientErrorException ce) {
                LOGGER.error("例外処理が発生しました", ce);
                // クライアントエラー
                ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
                clientError = conversionUtility.toObject(ce.getResponseBodyAsString(), ClientErrorResponse.class);

            }
        }

        // （８） カート商品チェックサービスの結果をエラー情報に追加
        cartHelper.toPageForLoad(clientError.getMessages(), cartModel);
        addErrorInfo(clientError.getMessages(), cartDto, bindingResult, redirectAttributes, model);
    }

    /**
     * 商品SEQリストの作成<br/>
     *
     * @param goodsGroupDtoList 商品グループエンティティlリスト
     * @return 作成した商品SEQリスト
     */
    protected List<Integer> createGoodsGroupSeqList(List<GoodsGroupDto> goodsGroupDtoList) {
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {
            goodsGroupSeqList.add(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq());
        }
        return goodsGroupSeqList;
    }

    /**
     * 注文画面遷移用共通処理<br/>
     *
     * @param cartModel カートModel
     * @param redirectAttributes
     * @return model
     * @return カートDTO
     */
    protected CartDto orderCommonAction(CartModel cartModel,
                                        BindingResult error,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {

        // （１） カート画面より、カート一覧情報と数量を取得する。
        CartDto cartDtoPage = cartHelper.toCartDtoForOrderLogin(cartModel);

        try {
            OrderItemCountListUpdateRequest orderItemCountListUpdateRequest =
                            cartHelper.toOrderItemCountListUpdateRequest(cartDtoPage.getCartGoodsDtoList());
            // （２） 【カート商品数量変更サービス】実行
            orderSlipApi.updateOrderItemCount(orderItemCountListUpdateRequest);
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // サーバーエラーの場合、エラー画面へ遷移
            handleServerError(se.getResponseBodyAsString());

        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // クライアントエラーの場合、、エラーメッセージを取得する
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
            ClientErrorResponse clientError =
                            conversionUtility.toObject(ce.getResponseBodyAsString(), ClientErrorResponse.class);
            // カート商品チェックサービスの結果をエラー情報に追加
            cartHelper.toPageForLoad(clientError.getMessages(), cartModel);
            addErrorInfo(clientError.getMessages(), cartDtoPage, error, redirectAttributes, model);
        }

        CartDto cartDto = getCartDto(error);
        if (cartDto != null) {
            checkCartPriceChange(cartDto, cartDtoPage);
        }

        return cartDto;
    }

    /**
     * 単価変更のみのチェック<br/>
     * 別ブラウザでカート内容を変更した場合は関知しない。
     *
     * @param cartDto カートDTO
     * @param cartDtoOriginal 変更前のカートDTO
     */
    protected void checkCartPriceChange(CartDto cartDto, CartDto cartDtoOriginal) {

        for (CartGoodsDto cartGoods : cartDto.getCartGoodsDtoList()) {
            Integer goodsSeq = cartGoods.getCartGoodsEntity().getGoodsSeq();
            BigDecimal price = null;
            // カート商品の数だけループ
            for (CartGoodsDto cartGoodsOriginal : cartDtoOriginal.getCartGoodsDtoList()) {
                if (goodsSeq != null && goodsSeq.equals(cartGoodsOriginal.getCartGoodsEntity().getGoodsSeq())) {
                    price = cartGoodsOriginal.getGoodsDetailsDto().getGoodsPrice();
                    break;
                }
            }
            cartGoods.setGoodsPriceChanged(
                            (price != null && price.compareTo(cartGoods.getGoodsDetailsDto().getGoodsPrice()) != 0));
        }
    }

    /**
     * エラーグローバルメッセージセット
     *
     * @param errorMap メッセージMAP
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model Model
     */
    protected void addGlobalErrorInfo(Map<String, List<ErrorContent>> errorMap,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        if (errorMap == null || errorMap.isEmpty()) {
            return;
        }

        List<ErrorContent> checkMessageDtoList = errorMap.get(GLOBAL_MESSAGE_FIELD_NAME);
        if (checkMessageDtoList == null) {
            return;
        }

        // HIT-MALLメッセージ
        HmMessages hmMessages = getFlashMessages(redirectAttributes);

        for (ErrorContent errorContent : checkMessageDtoList) {
            // グローバルエラー追加
            AppLevelFacesMessage appLevelFacesMessage = new AppLevelFacesMessage(errorContent.getMessage());
            hmMessages.add(appLevelFacesMessage);
        }

        if (hmMessages.hasError()) {
            // 遷移先画面がリダイレクトあり／なしにかかわらずメッセージ出力できるよう、
            // RedirectAttributes、Model両方に属性セットする
            redirectAttributes.addFlashAttribute(FLASH_MESSAGES, hmMessages);
            model.addAttribute(FLASH_MESSAGES, hmMessages);
        }
    }

    /**
     * エラーメッセージセット
     *
     * @param errorMap メッセージMAP
     * @param cartDto カート情報DTO
     * @param bindingResult
     * @param redirectAttributes
     * @param model
     *
     */
    protected void addErrorInfo(Map<String, List<ErrorContent>> errorMap,
                                CartDto cartDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (cartDto == null) {
            return;
        }

        addGlobalErrorInfo(errorMap, redirectAttributes, model);

        if (!cartUtility.isErrorAbortProcessing(errorMap, cartDto.getCartGoodsDtoList())) {
            return;
        }
        boolean hasIndividualDeliveryError = false;
        for (int i = 0; i < cartDto.getCartGoodsDtoList().size(); i++) {
            CartGoodsDto cartGoodsDto = cartDto.getCartGoodsDtoList().get(i);
            CartGoodsEntity cartGoodsEntity = cartGoodsDto.getCartGoodsEntity();
            GoodsDetailsDto goodsDetailsDto = cartGoodsDto.getGoodsDetailsDto();

            List<ErrorContent> checkMessageDtoList = errorMap.get(cartGoodsEntity.getGoodsSeq().toString());
            if (checkMessageDtoList == null) {
                continue;
            }

            StringBuilder s = new StringBuilder();
            StringBuilder code = new StringBuilder();
            for (ErrorContent errorContent : checkMessageDtoList) {
                String msgCd = errorContent.getCode();
                switch (Objects.requireNonNull(msgCd)) {
                    case CartModel.MSGCD_INDIVIDUAL_DELIVERY:
                        // 個別配送商品エラー
                        hasIndividualDeliveryError = true;
                        break;
                    default:
                        if (s.length() > 0) {
                            s.append("　");
                        }
                        s.append(errorContent.getMessage());
                        code.append(msgCd);
                        break;
                }
            }

            // エラーメッセージ用商品名取得
            StringBuilder displayGoodsName = new StringBuilder();
            displayGoodsName.append(goodsDetailsDto.getGoodsGroupName());
            // 規格管理する場合は商品表示名（規格値１/規格値２）をエラーメッセージに表示する
            if (goodsDetailsDto.getUnitManagementFlag() == HTypeUnitManagementFlag.ON) {
                displayGoodsName.append("(");
                // 規格管理ありの場合は規格値１は必須項目なので必ず取得できる
                displayGoodsName.append(goodsDetailsDto.getUnitValue1());
                // 規格値２の値がNULLの場合は商品表示名（規格値１）のみをエラーメッセージに表示する
                // 規格値２の値が存在する場合は規格値２をエラーメッセージに表示する
                if (StringUtils.isNotEmpty(goodsDetailsDto.getUnitValue2())) {
                    displayGoodsName.append("/");
                    displayGoodsName.append(goodsDetailsDto.getUnitValue2());
                }
                displayGoodsName.append(")");
            }

            if (code.length() > 0) {
                if (code.indexOf(CartModel.MSGCD_OPEN_STATUS_HIKOUKAI) != -1
                    || code.indexOf(CartModel.MSGCD_OPEN_BEFORE) != -1
                    || code.indexOf(CartModel.MSGCD_OPEN_END) != -1) {
                    // 商品状態が非公開、公開中止、公開前、公開終了のいずれか場合
                    addWarnMessage(CartModel.MSGCD_CART_OPEN_ERROR, new String[] {displayGoodsName.toString()},
                                   redirectAttributes, model
                                  );
                } else if (code.indexOf(CartModel.MSGCD_SALE_STATUS_HIHANBAI) != -1
                           || code.indexOf(CartModel.MSGCD_SALE_BEFORE) != -1
                           || code.indexOf(CartModel.MSGCD_SALE_END) != -1) {
                    // 商品状態が非販売、販売前、販売終了のいずれか場合
                    addWarnMessage(CartModel.MSGCD_CART_SALSE_ERROR, new String[] {displayGoodsName.toString()},
                                   redirectAttributes, model
                                  );
                } else if (code.indexOf(CartModel.MSGCD_NO_STOCK) != -1) {
                    // 商品状態が在庫切れの場合
                    addWarnMessage(CartModel.MSGCD_CART_STOCK_ERROR, new String[] {displayGoodsName.toString()},
                                   redirectAttributes, model
                                  );
                } else if (code.indexOf(CartModel.MSGCD_LESS_STOCK) != -1) {
                    // 商品状態が在庫不足の場合
                    addWarnMessage(CartModel.MSGCD_CART_LESS_STOCK_ERROR, new String[] {displayGoodsName.toString()},
                                   redirectAttributes, model
                                  );
                } else if (code.indexOf(CartModel.MSGCD_ALCOHOL_CANNOT_BE_PURCHASED) != -1) {
                    // 酒類購入不可エラー
                    addWarnMessage(CartModel.MSGCD_CART_ALCOHOL_ERROR, new String[] {displayGoodsName.toString()},
                                   redirectAttributes, model
                                  );
                } else {
                    // 上記以外の場合
                    addWarnMessage(CartModel.MSGCD_CART_ERROR, new String[] {displayGoodsName.toString(), s.toString()},
                                   redirectAttributes, model
                                  );
                }

                if (code.indexOf(CartModel.MSGCD_LESS_STOCK) != -1
                    || code.indexOf(CartModel.MSGCD_PURCHASED_MAX_OVER) != -1) {
                    // 在庫不足 or 最大購入可能数超過 の場合、対象商品の数量入力欄にエラースタイルを適用する
                    bindingResult.rejectValue("cartGoodsItems[" + i + "].gcnt", null, "");
                }
            }
        }

        // 個別配送商品チェック。
        // 他の商品と一緒に個別配送商品が入っていたらエラー
        if (hasIndividualDeliveryError) {
            addWarnMessage(CartModel.MSGCD_INDIVIDUAL_DELIVERY_ERROR, null, redirectAttributes, model);
        }
    }

    /**
     * カートDto取得<br/>
     *
     * @return カートDto
     */
    protected CartDto getCartDto(BindingResult error) {

        CartDto cartDto = null;

        OrderSlipResponse orderSlipResponse = null;
        try {
            // 下書き注文票取得
            orderSlipResponse = orderSlipApi.getDraft(null);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return null;
        }

        if (!ObjectUtils.isEmpty(orderSlipResponse)) {
            List<Integer> goodsSeqList = cartHelper.toItemIdList(orderSlipResponse);

            if (CollectionUtil.isNotEmpty(goodsSeqList)) {
                ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
                productDetailListGetRequest.setGoodsSeqList(goodsSeqList);
                try {
                    // 商品詳細一覧取得
                    ProductDetailListResponse productDetailListResponse =
                                    productApi.getDetails(productDetailListGetRequest, null);

                    // ソート商品詳細一覧レスポンス
                    cartHelper.reSortByItemListForDisplay(goodsSeqList, productDetailListResponse);

                    // 商品販売金額計算
                    ItemSalesPriceCalculateRequest itemSalesPriceCalculateRequest =
                                    new ItemSalesPriceCalculateRequest();
                    itemSalesPriceCalculateRequest.setOrderSlipId(orderSlipResponse.getOrderSlipId());
                    ItemSalesPriceCalculateResponse itemSalesPriceCalculateResponse =
                                    priceCalculatorApi.calculateItemSalesPrice(itemSalesPriceCalculateRequest);

                    cartDto = cartHelper.convertToCartInfo(orderSlipResponse, productDetailListResponse,
                                                           itemSalesPriceCalculateResponse
                                                          );
                } catch (HttpClientErrorException | HttpServerErrorException e) {

                    if (isCartCalcError(e.getResponseBodyAsString())) {
                        error.reject(MSGCD_CART_CALC_ERROR);
                        return null;
                    }

                    ApplicationLogUtility logUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
                    logUtility.writeExceptionLog(e);
                    // アイテム名調整マップ
                    Map<String, String> itemNameAdjust = new HashMap<>();
                    handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                }
            }
        }

        return cartDto;
    }

    /**
     * お気に入り商品の在庫状態の設定。<br/>
     * <pre>
     * 商品の公開状態、公開期間、販売状態、販売期間、在庫数条件に基づいて在庫状態を決定する。
     * 在庫状態判定の詳細は「26_HM3_共通部仕様書_在庫状態表示条件.xls」参照。
     * </pre>
     * @param wishlistDtoList お気に入り商品DTOリスト
     * @return お気に入り商品DTOリスト内の最大優先度の在庫状態
     */
    protected List<WishlistDto> executeWishlistGoodsStockStatus(List<WishlistDto> wishlistDtoList,
                                                                BindingResult error) {

        for (WishlistDto wishlistDto : wishlistDtoList) {

            GoodsDetailsDto goodsDetailsDto = wishlistDto.getGoodsDetailsDto();

            InventoryStatusDisplayGetRequest inventoryStatusDisplayGetRequest =
                            cartHelper.toInventoryStatusDisplayGetRequest(goodsDetailsDto);
            try {
                InventoryStatusDisplayResponse inventoryStatusDisplayResponse =
                                inventoryApi.getGoodsStatus(inventoryStatusDisplayGetRequest);

                if (ObjectUtils.isNotEmpty(inventoryStatusDisplayResponse)) {
                    wishlistDto.setStockStatus(inventoryStatusDisplayResponse.getCurrentStatus());
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                itemNameAdjust.put("saleStartTime", "saleStartTimePC");
                itemNameAdjust.put("saleEndTime", "saleEndTimePC");
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }

        }
        return wishlistDtoList;
    }

    /**
     * エラー処理
     *
     * @param cartModel
     * @param errorBody
     * @param error
     * @param redirectAttributes
     * @param model
     */
    protected void errorProcess(CartModel cartModel,
                                String errorBody,
                                BindingResult error,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // エラー内容取得
        ClientErrorMessageUtility clientErrorMessageUtility =
                        ApplicationContextUtility.getBean(ClientErrorMessageUtility.class);
        Map<String, List<String>> errorContent = clientErrorMessageUtility.getErrorContent(errorBody);

        // HIT-MALLメッセージ
        HmMessages hmMessages = new HmMessages();

        // エラー処理
        for (Map.Entry<String, List<String>> entry : errorContent.entrySet()) {
            int goodsSeq = Integer.parseInt(entry.getKey());
            for (int i = 0; i < cartModel.getCartGoodsItems().size(); i++) {
                CartModelItem cartModelItem = cartModel.getCartGoodsItems().get(i);
                if (cartModelItem.getGoodsSeq() == goodsSeq) {
                    for (String err : entry.getValue()) {
                        // グローバルエラー追加
                        AppLevelFacesMessage appLevelFacesMessage =
                                        new AppLevelFacesMessage(cartModelItem.getGoodsGroupName() + "：" + err);
                        hmMessages.add(appLevelFacesMessage);
                    }
                    // 該当項目エラー追加
                    error.rejectValue("cartGoodsItems[" + i + "].gcnt", null, "");
                }
            }
        }

        // グローバルエラー設定
        if (hmMessages.hasError()) {
            redirectAttributes.addFlashAttribute("allMessages", hmMessages);
            model.addAttribute("allMessages", hmMessages);
        }
    }

    /**
     * 計算されたカートのにエラーが発生したかどうかを確認します
     *
     * @param responseBody レスポンスボディ
     * @return TRUE...カート計算エラー
     */
    private boolean isCartCalcError(String responseBody) {
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // カート計算エラーメッセージ詳細
        String messageCartCalculationErr = getMessage(MSGCD_CART_CALC_ERROR, null);

        // サーバーエラー
        ServerErrorResponse serverError = conversionUtility.toObject(responseBody, ServerErrorResponse.class);
        if ((serverError != null) && (MapUtils.isNotEmpty(serverError.getMessages()))) {

            for (List<jp.co.itechh.quad.addressbook.presentation.api.param.ErrorContent> errorResponseList : serverError.getMessages()
                                                                                                                        .values()) {
                if (CollectionUtil.isNotEmpty(errorResponseList)) {
                    return errorResponseList.get(0).getMessage().contains(messageCartCalculationErr);
                }
            }
        }

        return false;
    }

}
