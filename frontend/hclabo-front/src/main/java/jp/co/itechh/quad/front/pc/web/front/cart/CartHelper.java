/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.cart;

import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.GoodsGroupResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.GoodsInformationIconDetailResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.StockStatusDisplayResponse;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.front.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.front.dto.cart.CartDto;
import jp.co.itechh.quad.front.dto.cart.CartGoodsDto;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.front.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.front.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.front.dto.memberinfo.wishlist.WishlistDto;
import jp.co.itechh.quad.front.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;
import jp.co.itechh.quad.front.entity.cart.CartGoodsEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.front.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.utility.GoodsUtility;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryStatusDisplayGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemCountListUpdateRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemCountListUpdateRequestItemList;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import jp.co.itechh.quad.pricecalculator.presentation.api.param.ItemSalesPriceCalculateResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListResponse;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistListResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * カートHelper
 *
 * @author kaneda
 * @author Pham Quang Dieu
 */
@Component
public class CartHelper {

    /** GoodsUtility */
    private GoodsUtility goodsUtility;

    /** ConversionUtility */
    private ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     * @param goodsUtility
     * @param conversionUtility
     */
    @Autowired
    public CartHelper(GoodsUtility goodsUtility, ConversionUtility conversionUtility) {
        this.goodsUtility = goodsUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     *
     * 画面表示・再表示<br/>
     *
     * @param cartDto カート情報DTO
     * @param cartModel カートModel
     */
    public void toPageForLoad(CartDto cartDto, CartModel cartModel) {

        if (cartDto == null || CollectionUtil.isEmpty(cartDto.getCartGoodsDtoList())) {
            // カート情報が存在しないのでクリア
            cartModel.setCartGoodsItems(null);
            cartModel.setGoodsCountTotal(null);
            cartModel.setPriceTotal(null);
            cartModel.setPriceTotalInTax(null);
            return;
        }

        List<CartModelItem> cartGoodsItems = new ArrayList<>();

        int cartNo = 1;
        for (CartGoodsDto cartGoodsDto : cartDto.getCartGoodsDtoList()) {

            CartModelItem cartModelItem = ApplicationContextUtility.getBean(CartModelItem.class);

            CartGoodsEntity cartGoodsEntity = cartGoodsDto.getCartGoodsEntity();
            GoodsDetailsDto goodsDetailsDto = cartGoodsDto.getGoodsDetailsDto();
            BigDecimal goodsCount = conversionUtility.toBigDecimal(cartGoodsEntity.getCartGoodsCount());

            cartModelItem.setGcd(goodsDetailsDto.getGoodsCode());
            cartModelItem.setGoodsGroupName(goodsDetailsDto.getGoodsGroupName());
            cartModelItem.setGoodsPrice(goodsDetailsDto.getGoodsPrice());
            cartModelItem.setGoodsPriceInTax(goodsDetailsDto.getGoodsPriceInTax());
            cartModelItem.setGoodsSeq(goodsDetailsDto.getGoodsSeq());
            cartModelItem.setDeliveryType(goodsDetailsDto.getDeliveryType());
            cartModelItem.setGoodsTaxType(goodsDetailsDto.getGoodsTaxType());
            cartModelItem.setTaxRate(goodsDetailsDto.getTaxRate());
            cartModelItem.setAlcoholFlag(goodsDetailsDto.getAlcoholFlag());
            cartModelItem.setUnitTitle1(goodsDetailsDto.getUnitTitle1());
            cartModelItem.setUnitValue1(goodsDetailsDto.getUnitValue1());
            cartModelItem.setUnitTitle2(goodsDetailsDto.getUnitTitle2());
            cartModelItem.setUnitValue2(goodsDetailsDto.getUnitValue2());
            cartModelItem.setGoodsOpenStatus(goodsDetailsDto.getGoodsOpenStatusPC());
            cartModelItem.setOpenStartTime(goodsDetailsDto.getOpenStartTimePC());
            cartModelItem.setOpenEndTime(goodsDetailsDto.getOpenEndTimePC());

            // 商品画像リストを取り出す。
            List<String> goodsImageList = new ArrayList<>();
            if (goodsDetailsDto.getGoodsGroupImageEntityList() != null) {
                for (GoodsGroupImageEntity goodsGroupImageEntity : goodsDetailsDto.getGoodsGroupImageEntityList()) {
                    goodsImageList.add(goodsGroupImageEntity.getImageFileName());
                }
            }
            cartModelItem.setGoodsImageItems(goodsImageList);

            cartModelItem.setGcnt(cartGoodsEntity.getCartGoodsCount().toString());
            cartModelItem.setCartSeq(cartGoodsEntity.getCartSeq());
            if (goodsCount != null) {
                cartModelItem.setGoodsTotalPrice(cartModelItem.getGoodsPrice().multiply(goodsCount));
                cartModelItem.setGoodsTotalPriceInTax(cartModelItem.getGoodsPriceInTax().multiply(goodsCount));
            }

            // 個別配送商品設定
            if (goodsDetailsDto.getIndividualDeliveryType() != null && HTypeIndividualDeliveryType.ON.equals(
                            goodsDetailsDto.getIndividualDeliveryType())) {
                cartModelItem.setIndividualDeliveryType(goodsDetailsDto.getIndividualDeliveryType().getLabel());
            }

            // アイコン設定
            cartModelItem.setGoodsIconItems(createGoodsIconList(cartGoodsDto.getGoodsInformationIconDetailsDtoList()));

            if (cartGoodsDto.getGoodsDetailsDto() != null
                && cartGoodsDto.getGoodsDetailsDto().getWhatsnewDate() != null) {
                // 新着画像の表示期間を取得
                Timestamp whatsnewDate =
                                goodsUtility.getRealWhatsNewDate(cartGoodsDto.getGoodsDetailsDto().getWhatsnewDate());
                cartModelItem.setWhatsnewDate(whatsnewDate);
            }

            // カート行番号設定
            cartModelItem.setCartNo(cartNo++);

            // カート商品リストに追加
            cartGoodsItems.add(cartModelItem);
        }

        cartModel.setCartGoodsItems(cartGoodsItems);
        cartModel.setGoodsCountTotal(cartDto.getGoodsTotalCount());
        cartModel.setPriceTotal(cartDto.getGoodsTotalPrice());
        cartModel.setPriceTotalInTax(cartDto.getGoodsTotalPriceInTax());
    }

    /**
     *
     * 画面表示・再表示(お気に入り情報)<br/>
     *
     * @param wishlistDtoList お気に入りDTOリスト
     * @param cartModel カートModel
     */
    public void toPageForLoadWishlist(List<WishlistDto> wishlistDtoList, CartModel cartModel) {

        List<CartModelItem> goodsItems = new ArrayList<>();

        for (WishlistDto wishlistDto : wishlistDtoList) {
            // お気に入り商品情報取得
            GoodsDetailsDto goodsDetailsDto = wishlistDto.getGoodsDetailsDto();
            String goodsImage = null;

            // Modelアイテムクラスにセット
            CartModelItem cartModelItem = ApplicationContextUtility.getBean(CartModelItem.class);
            if (goodsDetailsDto != null) {
                cartModelItem.setGoodsGroupSeq(goodsDetailsDto.getGoodsGroupSeq());
                cartModelItem.setGgcd(goodsDetailsDto.getGoodsGroupCode());
                cartModelItem.setGoodsSeq(goodsDetailsDto.getGoodsSeq());
                cartModelItem.setGcd(goodsDetailsDto.getGoodsCode());
                cartModelItem.setUnitTitle1(goodsDetailsDto.getUnitTitle1());
                cartModelItem.setUnitValue1(goodsDetailsDto.getUnitValue1());
                cartModelItem.setUnitTitle2(goodsDetailsDto.getUnitTitle2());
                cartModelItem.setUnitValue2(goodsDetailsDto.getUnitValue2());
                cartModelItem.setGoodsGroupName(goodsDetailsDto.getGoodsGroupName());
                cartModelItem.setDeliveryType(goodsDetailsDto.getDeliveryType());
                cartModelItem.setGcnt("1");// カートに入れるボタンのために必要
                cartModelItem.setGoodsPrice(goodsDetailsDto.getGoodsPrice());

                // 税率、税種別の変換
                cartModelItem.setTaxRate(goodsDetailsDto.getTaxRate());
                cartModelItem.setGoodsTaxType(goodsDetailsDto.getGoodsTaxType());

                // 税込価格の計算
                cartModelItem.setGoodsPriceInTax(goodsDetailsDto.getGoodsPriceInTax());

                // 新着画像の表示期間を取得
                if (goodsDetailsDto.getWhatsnewDate() != null) {
                    Timestamp whatsnewDate = goodsUtility.getRealWhatsNewDate(goodsDetailsDto.getWhatsnewDate());
                    cartModelItem.setWhatsnewDate(whatsnewDate);
                }
                cartModelItem.setGoodsOpenStatus(goodsDetailsDto.getGoodsOpenStatusPC());
                cartModelItem.setOpenStartTime(goodsDetailsDto.getOpenStartTimePC());
                cartModelItem.setOpenEndTime(goodsDetailsDto.getOpenEndTimePC());
                cartModelItem.setSaleStatus(goodsDetailsDto.getSaleStatusPC());
                cartModelItem.setSaleStartTime(goodsDetailsDto.getSaleStartTimePC());
                cartModelItem.setSaleEndTime(goodsDetailsDto.getSaleEndTimePC());

                // 商品画像リストを取り出す。
                List<String> goodsImageList = new ArrayList<>();
                if (goodsDetailsDto.getGoodsGroupImageEntityList() != null) {
                    for (GoodsGroupImageEntity goodsGroupImageEntity : goodsDetailsDto.getGoodsGroupImageEntityList()) {
                        goodsImageList.add(goodsGroupImageEntity.getImageFileName());
                    }
                }
                cartModelItem.setGoodsImageItems(goodsImageList);
            }

            // 在庫状況表示
            cartModelItem.setListStockStatusPc(wishlistDto.getStockStatus());

            // アイコン情報の取得
            cartModelItem.setGoodsIconItems(createGoodsIconList(wishlistDto.getGoodsInformationIconDetailsDtoList()));

            goodsItems.add(cartModelItem);
        }

        cartModel.setWishlistGoodsItems(goodsItems);
    }

    /**
     *
     * 画面表示・再表示(あしあと情報)<br/>
     *
     * @param goodsGroupDtoList 商品グループDTOリスト
     * @param cartModel カートModel
     */
    public void toPageForLoadBrowsingHistory(List<GoodsGroupDto> goodsGroupDtoList, CartModel cartModel) {
        cartModel.setBrowsingHistoryGoodsItems(getGoodsItemsForGoodsListDto(goodsGroupDtoList, cartModel));
    }

    /**
     *
     * 画面表示・再表示(関連商品情報)<br/>
     *
     * @param goodsGroupDtoList 商品グループDTOリスト
     * @param cartModel カートModel
     */
    public void toPageForLoadRelated(List<GoodsGroupDto> goodsGroupDtoList, CartModel cartModel) {
        cartModel.setRelatedGoodsItems(getGoodsItemsForGoodsListDto(goodsGroupDtoList, cartModel));
    }

    /**
     *
     * 画面表示・再表示(関連商品情報)<br/>
     *
     * @param errorMap チェックメッセージマップ
     * @param cartModel カートModel
     */
    public void toPageForLoad(Map<String, List<ErrorContent>> errorMap, CartModel cartModel) {

        if (CollectionUtil.isEmpty(cartModel.getCartGoodsItems())) {
            return;
        }

        for (CartModelItem cartModelItem : cartModel.getCartGoodsItems()) {
            boolean viewFlg = true;
            if (errorMap != null && errorMap.containsKey(cartModelItem.getGoodsSeq().toString())) {
                StringBuilder s = new StringBuilder();
                for (ErrorContent errorContent : errorMap.get(cartModelItem.getGoodsSeq().toString())) {
                    // 備考欄には出力しないメッセージは飛ばす
                    if (!isOutputGoodsMemoField(Objects.requireNonNull(errorContent.getCode()))) {
                        continue;
                    }

                    if (CartModel.INDIVIDUAL_DELIVERY_WARNING.equals(errorContent.getCode())
                        || CartModel.ALCOHOL_CANNOT_BE_PURCHASED_WARNING.equals(errorContent.getCode())) {
                        continue;
                    }
                    s.append(errorContent.getMessage()).append("<br />");

                    // 公開状態、販売状態、在庫きれ状況に応じて、お気に入りに追加ボタンの表示を制御する。
                    if (CartModel.MSGCD_OPEN_STATUS_HIKOUKAI.equals(errorContent.getCode())
                        || CartModel.MSGCD_OPEN_END.equals(errorContent.getCode())
                        || CartModel.MSGCD_SALE_STATUS_HIHANBAI.equals(errorContent.getCode())
                        || CartModel.MSGCD_SALE_BEFORE.equals(errorContent.getCode())
                        || CartModel.MSGCD_SALE_END.equals(errorContent.getCode())) {
                        viewFlg = false;
                    }
                }
                cartModelItem.setGoodsInfomation(s.toString());
            }
            cartModelItem.setWishlistButtonView(viewFlg);
        }
    }

    /**
     * メッセージを備考欄に出力するメッセージか判定します。<br/>
     *
     * @param messageId メッセージDto
     * @return true:出力する false:出力しない
     */

    protected boolean isOutputGoodsMemoField(String messageId) {
        switch (messageId) {

            case CartModel.MSGCD_INDIVIDUAL_DELIVERY:
                return false;
            case CartModel.MSGCD_ALCOHOL_CANNOT_BE_PURCHASED:
                return false;
            default:
                return true;
        }
    }

    /**
     * 再計算<br/>
     *
     * @param cartModel カートModel
     * @return カート情報DTO
     */
    public CartDto toCartDtoForCalculate(CartModel cartModel) {

        CartDto cartDto = ApplicationContextUtility.getBean(CartDto.class);

        if (!ObjectUtils.isEmpty(cartModel) && !CollectionUtils.isEmpty(cartModel.getCartGoodsItems())) {
            List<CartGoodsDto> cartGoodsDtoList = new ArrayList<>();

            for (CartModelItem cartModelItem : cartModel.getCartGoodsItems()) {

                // ブラウザバックなどで値がクリアされている場合、無視する
                // ※ブラウザバック直後などでは、itemsがクリアされ再計算用のボタンを押下したitemしか取得できない
                if (cartModelItem.getCartSeq() == null) {
                    continue;
                }
                // カート商品Itemをカート商品Dtoに変換
                CartGoodsDto cartGoodsDto = toCartGoodsDto(cartModelItem);
                cartGoodsDtoList.add(cartGoodsDto);
            }

            cartDto.setCartGoodsDtoList(cartGoodsDtoList);
        }
        return cartDto;
    }

    /**
     * 注文<br/>
     *
     * @param cartModel カートModel
     * @return カート情報DTO
     */
    public CartDto toCartDtoForOrderLogin(CartModel cartModel) {

        CartDto cartDto = ApplicationContextUtility.getBean(CartDto.class);

        if (!ObjectUtils.isEmpty(cartModel) && !CollectionUtils.isEmpty(cartModel.getCartGoodsItems())) {
            List<CartGoodsDto> cartGoodsDtoList = new ArrayList<>();

            for (CartModelItem cartModelItem : cartModel.getCartGoodsItems()) {
                // カート商品Itemをカート商品Dtoに変換
                CartGoodsDto cartGoodsDto = toCartGoodsDto(cartModelItem);
                cartGoodsDtoList.add(cartGoodsDto);
            }

            cartDto.setCartGoodsDtoList(cartGoodsDtoList);
        }
        return cartDto;
    }

    /**
     *
     * 商品情報一覧ページアイテムリストの作成<br/>
     *
     * @param goodsGroupDtoList 商品グループ情報DTO一覧
     * @param cartModel カートモデル
     * @return 商品情報一覧ページアイテムリスト
     */
    protected List<CartModelItem> getGoodsItemsForGoodsListDto(List<GoodsGroupDto> goodsGroupDtoList,
                                                               CartModel cartModel) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsGroupDtoList)) {
            return null;
        }

        List<CartModelItem> goodsItems = new ArrayList<>();
        for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {
            CartModelItem cartModelItem = ApplicationContextUtility.getBean(CartModelItem.class);

            GoodsGroupEntity goodsGroupEntity = goodsGroupDto.getGoodsGroupEntity();
            if (!CollectionUtils.isEmpty(goodsGroupDto.getGoodsGroupImageEntityList())) {
                // 商品画像リストを取り出す。
                List<String> goodsImageList = new ArrayList<>();
                for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupDto.getGoodsGroupImageEntityList()) {
                    goodsImageList.add(goodsGroupImageEntity.getImageFileName());
                }
                cartModelItem.setGoodsImageItems(goodsImageList);
            }

            if (goodsGroupEntity != null) {
                cartModelItem.setGoodsGroupSeq(goodsGroupEntity.getGoodsGroupSeq());
                cartModelItem.setGgcd(goodsGroupEntity.getGoodsGroupCode());
                cartModelItem.setGoodsGroupName(goodsGroupEntity.getGoodsGroupName());
                cartModelItem.setGoodsOpenStatus(goodsGroupEntity.getGoodsOpenStatusPC());
                cartModelItem.setOpenStartTime(goodsGroupEntity.getOpenStartTimePC());
                cartModelItem.setOpenEndTime(goodsGroupEntity.getOpenEndTimePC());

                // 税率
                BigDecimal taxRate = goodsGroupEntity.getTaxRate();
                cartModelItem.setTaxRate(taxRate);

                // 通常価格 - 税込計算
                BigDecimal goodsPrice = goodsGroupEntity.getGoodsPrice();
                cartModelItem.setGoodsPrice(goodsPrice);
                cartModelItem.setGoodsPriceInTax(goodsGroupEntity.getGoodsPriceInTax());

                if (goodsGroupEntity.getWhatsnewDate() != null) {
                    // 新着画像の表示期間を取得
                    Timestamp whatsnewDate = goodsUtility.getRealWhatsNewDate(goodsGroupEntity.getWhatsnewDate());
                    cartModelItem.setWhatsnewDate(whatsnewDate);
                }
            }

            // 在庫状況表示
            StockStatusDisplayEntity stockStatusDisplayEntity = goodsGroupDto.getBatchUpdateStockStatus();
            if (stockStatusDisplayEntity != null && stockStatusDisplayEntity.getStockStatusPc() != null) {
                cartModelItem.setListStockStatusPc(EnumTypeUtil.getValue(stockStatusDisplayEntity.getStockStatusPc()));
            }

            // アイコン情報の取得
            cartModelItem.setGoodsIconItems(createGoodsIconList(goodsGroupDto.getGoodsInformationIconDetailsDtoList()));

            goodsItems.add(cartModelItem);
        }
        return goodsItems;
    }

    /**
     * お気に入り商品検索条件Dtoの作詞絵
     *
     * @param cartModel カートモデル
     * @return お気に入り検索条件Dto
     */
    public WishlistSearchForDaoConditionDto toWishlistConditionDtoForSearchWishlistList(CartModel cartModel) {
        // お気に入り検索条件Dtoの作成 公開状態＝指定なし
        WishlistSearchForDaoConditionDto wishlistConditionDto =
                        ApplicationContextUtility.getBean(WishlistSearchForDaoConditionDto.class);

        wishlistConditionDto.setMemberInfoSeq(cartModel.getCommonInfo().getCommonInfoUser().getMemberInfoSeq());

        return wishlistConditionDto;
    }

    /**
     * アイコン情報を設定
     *
     * @param goodsInformationIconDetailsDtoList 登録されているアイコン情報
     * @return 画面表示用に作成したアイコンリスト
     */
    protected List<CartModelItem> createGoodsIconList(List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsInformationIconDetailsDtoList)) {
            return null;
        }

        List<CartModelItem> goodsIconList = new ArrayList<>();
        for (GoodsInformationIconDetailsDto goodsInformationIconDetailsDto : goodsInformationIconDetailsDtoList) {
            CartModelItem iconPageItem = ApplicationContextUtility.getBean(CartModelItem.class);

            iconPageItem.setIconName(goodsInformationIconDetailsDto.getIconName());
            iconPageItem.setIconColorCode(goodsInformationIconDetailsDto.getColorCode());

            goodsIconList.add(iconPageItem);
        }
        return goodsIconList;
    }

    /**
     * 在庫チェック<br/>
     *
     * @param goodsGroupDto 商品グループDTO
     * @return true..1つ以上が在庫あり
     */
    protected boolean isALLStock(GoodsGroupDto goodsGroupDto) {
        if (CollectionUtils.isEmpty(goodsGroupDto.getGoodsDtoList())) {
            return false;
        }

        for (GoodsDto goodsDto : goodsGroupDto.getGoodsDtoList()) {
            // 在庫管理フラグ
            HTypeStockManagementFlag stockManagementFlag = goodsDto.getGoodsEntity().getStockManagementFlag();
            // 在庫チェック
            if (HTypeStockManagementFlag.OFF.equals(stockManagementFlag) || (
                            HTypeStockManagementFlag.ON.equals(stockManagementFlag)
                            && goodsDto.getStockDto().getSalesPossibleStock().intValue() > 0)) {
                return true;
            }
        }

        return false;
    }

    /**
     * お気に入り表示設定
     *
     * @param wishlistList お気に入りリスト
     * @param cartModel    カートModel
     */
    protected void setWishlistView(List<WishlistEntity> wishlistList, CartModel cartModel) {
        if (CollectionUtil.isNotEmpty(cartModel.getWishlistGoodsItems()) && CollectionUtil.isNotEmpty(
                        cartModel.getCartGoodsItems()) && !CollectionUtils.isEmpty(wishlistList)) {
            for (CartModelItem indexPageItem : cartModel.getCartGoodsItems()) {
                for (WishlistEntity wishlistEntity : wishlistList) {
                    if (indexPageItem.getGoodsSeq() != null && indexPageItem.getGoodsSeq()
                                                                            .equals(wishlistEntity.getGoodsSeq())) {
                        indexPageItem.setWishlistView(false);
                    }
                }
            }
        }
    }

    /**
     * お気に入りDto一覧に変換
     *
     * @param wishlistListResponse お気に入り情報一覧レスポンス
     * @return お気に入りDto一覧
     */
    public List<WishlistDto> toWishlistDtoList(WishlistListResponse wishlistListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(wishlistListResponse) || CollectionUtils.isEmpty(
                        wishlistListResponse.getWishListList())) {
            return null;
        }
        List<WishlistDto> wishlistDtoList = new ArrayList<>();
        for (WishlistResponse wishlistResponse : wishlistListResponse.getWishListList()) {
            WishlistDto wishlistDto = new WishlistDto();
            WishlistEntity wishlistEntity = new WishlistEntity();

            wishlistEntity.setMemberInfoSeq(wishlistResponse.getMemberInfoSeq());
            wishlistEntity.setGoodsSeq(wishlistResponse.getGoodsSeq());
            wishlistEntity.setRegistTime(conversionUtility.toTimestamp(wishlistResponse.getRegistTime()));
            wishlistEntity.setUpdateTime(conversionUtility.toTimestamp(wishlistResponse.getUpdateTime()));
            wishlistDto.setWishlistEntity(wishlistEntity);

            GoodsDetailsDto goodsDetailsDto = new GoodsDetailsDto();
            if (!CollectionUtils.isEmpty(wishlistResponse.getGoodsGroupImageResponseList())) {
                List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();
                for (GoodsGroupImageResponse goodsGroupImageResponse : wishlistResponse.getGoodsGroupImageResponseList()) {
                    GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

                    goodsGroupImageEntity.setGoodsGroupSeq(goodsGroupImageResponse.getGoodsGroupSeq());
                    goodsGroupImageEntity.setImageTypeVersionNo(goodsGroupImageResponse.getImageTypeVersionNo());
                    goodsGroupImageEntity.setImageFileName(goodsGroupImageResponse.getImageFileName());
                    goodsGroupImageEntity.setRegistTime(
                                    conversionUtility.toTimestamp(goodsGroupImageResponse.getRegistTime()));
                    goodsGroupImageEntity.setUpdateTime(
                                    conversionUtility.toTimestamp(goodsGroupImageResponse.getUpdateTime()));
                    goodsGroupImageEntityList.add(goodsGroupImageEntity);
                }
                wishlistDto.setGoodsGroupImageEntityList(goodsGroupImageEntityList);
                goodsDetailsDto.setGoodsGroupImageEntityList(goodsGroupImageEntityList);
            }
            goodsDetailsDto.setGoodsGroupSeq(wishlistResponse.getGoodsGroupSeq());
            goodsDetailsDto.setVersionNo(wishlistResponse.getVersionNo());
            goodsDetailsDto.setGoodsCode(wishlistResponse.getGoodsCode());
            if (wishlistResponse.getGoodsTaxType() != null) {
                goodsDetailsDto.setGoodsTaxType(EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class,
                                                                              wishlistResponse.getGoodsTaxType()
                                                                             ));
            }
            goodsDetailsDto.setTaxRate(wishlistResponse.getTaxRate());
            if (wishlistResponse.getAlcoholFlag() != null) {
                goodsDetailsDto.setAlcoholFlag(EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class,
                                                                             wishlistResponse.getAlcoholFlag()
                                                                            ));
            }
            goodsDetailsDto.setGoodsPriceInTax(wishlistResponse.getGoodsPriceInTax());
            goodsDetailsDto.setGoodsPrice(wishlistResponse.getGoodsPrice());
            goodsDetailsDto.setDeliveryType(wishlistResponse.getDeliveryType());
            if (wishlistResponse.getSaleStatusPC() != null) {
                goodsDetailsDto.setSaleStatusPC(EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class,
                                                                              wishlistResponse.getSaleStatusPC()
                                                                             ));
            }
            goodsDetailsDto.setSaleStartTimePC(conversionUtility.toTimestamp(wishlistResponse.getSaleStartTimePC()));
            goodsDetailsDto.setSaleEndTimePC(conversionUtility.toTimestamp(wishlistResponse.getSaleEndTimePC()));
            if (wishlistResponse.getUnitManagementFlag() != null) {
                goodsDetailsDto.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                                    wishlistResponse.getUnitManagementFlag()
                                                                                   ));
            }
            if (wishlistResponse.getStockManagementFlag() != null) {
                goodsDetailsDto.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                                     wishlistResponse.getStockManagementFlag()
                                                                                    ));
            }
            if (wishlistResponse.getIndividualDeliveryType() != null) {
                goodsDetailsDto.setIndividualDeliveryType(
                                EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                              wishlistResponse.getIndividualDeliveryType()
                                                             ));
            }
            goodsDetailsDto.setPurchasedMax(wishlistResponse.getPurchasedMax());
            if (wishlistResponse.getFreeDeliveryFlag() != null) {
                goodsDetailsDto.setFreeDeliveryFlag(EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class,
                                                                                  wishlistResponse.getFreeDeliveryFlag()
                                                                                 ));
            }
            goodsDetailsDto.setOrderDisplay(wishlistResponse.getOrderDisplay());
            goodsDetailsDto.setUnitValue1(wishlistResponse.getUnitValue1());
            goodsDetailsDto.setUnitValue2(wishlistResponse.getUnitValue2());
            goodsDetailsDto.setJanCode(wishlistResponse.getJanCode());
            goodsDetailsDto.setSalesPossibleStock(wishlistResponse.getSalesPossibleStock());
            goodsDetailsDto.setRealStock(wishlistResponse.getRealStock());
            goodsDetailsDto.setOrderReserveStock(wishlistResponse.getOrderReserveStock());
            goodsDetailsDto.setRemainderFewStock(wishlistResponse.getRemainderFewStock());
            goodsDetailsDto.setOrderPointStock(wishlistResponse.getOrderPointStock());
            goodsDetailsDto.setSafetyStock(wishlistResponse.getSafetyStock());
            goodsDetailsDto.setGoodsGroupCode(wishlistResponse.getGoodsGroupCode());
            goodsDetailsDto.setWhatsnewDate(conversionUtility.toTimestamp(wishlistResponse.getWhatsnewDate()));
            if (wishlistResponse.getGoodsOpenStatusPC() != null) {
                goodsDetailsDto.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   wishlistResponse.getGoodsOpenStatusPC()
                                                                                  ));
            }
            goodsDetailsDto.setOpenStartTimePC(conversionUtility.toTimestamp(wishlistResponse.getOpenStartTimePC()));
            goodsDetailsDto.setOpenEndTimePC(conversionUtility.toTimestamp(wishlistResponse.getOpenEndTimePC()));
            goodsDetailsDto.setGoodsGroupName(wishlistResponse.getGoodsGroupName());
            goodsDetailsDto.setUnitTitle1(wishlistResponse.getUnitTitle1());
            goodsDetailsDto.setUnitTitle2(wishlistResponse.getUnitTitle2());
            if (wishlistResponse.getSnsLinkFlag() != null) {
                goodsDetailsDto.setSnsLinkFlag(EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class,
                                                                             wishlistResponse.getSnsLinkFlag()
                                                                            ));
            }
            goodsDetailsDto.setMetaDescription(wishlistResponse.getMetaDescription());
            if (wishlistResponse.getStockStatusPc() != null) {
                goodsDetailsDto.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                               wishlistResponse.getStockStatusPc()
                                                                              ));
            }
            goodsDetailsDto.setGoodsNote1(wishlistResponse.getGoodsNote1());
            goodsDetailsDto.setGoodsNote2(wishlistResponse.getGoodsNote2());
            goodsDetailsDto.setGoodsNote3(wishlistResponse.getGoodsNote3());
            goodsDetailsDto.setGoodsNote4(wishlistResponse.getGoodsNote4());
            goodsDetailsDto.setGoodsNote5(wishlistResponse.getGoodsNote5());
            goodsDetailsDto.setGoodsNote6(wishlistResponse.getGoodsNote6());
            goodsDetailsDto.setGoodsNote7(wishlistResponse.getGoodsNote7());
            goodsDetailsDto.setGoodsNote8(wishlistResponse.getGoodsNote8());
            goodsDetailsDto.setGoodsNote9(wishlistResponse.getGoodsNote9());
            goodsDetailsDto.setGoodsNote10(wishlistResponse.getGoodsNote10());
            goodsDetailsDto.setOrderSetting1(wishlistResponse.getOrderSetting1());
            goodsDetailsDto.setOrderSetting2(wishlistResponse.getOrderSetting2());
            goodsDetailsDto.setOrderSetting3(wishlistResponse.getOrderSetting3());
            goodsDetailsDto.setOrderSetting4(wishlistResponse.getOrderSetting4());
            goodsDetailsDto.setOrderSetting5(wishlistResponse.getOrderSetting5());
            goodsDetailsDto.setOrderSetting6(wishlistResponse.getOrderSetting6());
            goodsDetailsDto.setOrderSetting7(wishlistResponse.getOrderSetting7());
            goodsDetailsDto.setOrderSetting8(wishlistResponse.getOrderSetting8());
            goodsDetailsDto.setOrderSetting9(wishlistResponse.getOrderSetting9());
            goodsDetailsDto.setOrderSetting10(wishlistResponse.getOrderSetting10());
            wishlistDto.setGoodsDetailsDto(goodsDetailsDto);

            if (!CollectionUtils.isEmpty(wishlistResponse.getGoodsInformationIconDetailsResponseList())) {
                List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();
                for (GoodsInformationIconDetailsResponse goodsInformationIconDetailsResponse : wishlistResponse.getGoodsInformationIconDetailsResponseList()) {
                    GoodsInformationIconDetailsDto goodsInformationIconDetailsDto =
                                    new GoodsInformationIconDetailsDto();

                    goodsInformationIconDetailsDto.setGoodsGroupSeq(
                                    goodsInformationIconDetailsResponse.getGoodsGroupSeq());
                    goodsInformationIconDetailsDto.setIconSeq(goodsInformationIconDetailsResponse.getIconSeq());
                    goodsInformationIconDetailsDto.setIconName(goodsInformationIconDetailsResponse.getIconName());
                    goodsInformationIconDetailsDto.setColorCode(goodsInformationIconDetailsResponse.getColorCode());
                    goodsInformationIconDetailsDto.setOrderDisplay(
                                    goodsInformationIconDetailsResponse.getOrderDisplay());
                    goodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);

                }
                wishlistDto.setGoodsInformationIconDetailsDtoList(goodsInformationIconDetailsDtoList);
            }

            wishlistDto.setStockStatus(wishlistResponse.getStockStatus());

            wishlistDtoList.add(wishlistDto);
        }

        return wishlistDtoList;
    }

    /**
     * 商品グループDto一覧スに変換
     *
     * @param browsinghistoryListResponse あしあと商品情報一覧レスポンス
     * @return 商品グループDto一覧
     */
    public List<GoodsGroupDto> toBrowsingHistoryList(BrowsingHistoryListResponse browsinghistoryListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(browsinghistoryListResponse) || CollectionUtils.isEmpty(
                        browsinghistoryListResponse.getBrowsingHistoryResponse())) {
            return null;
        }

        List<GoodsGroupDto> goodsGroupDtoList = new ArrayList<>();
        for (BrowsingHistoryResponse browsinghistoryResponse : browsinghistoryListResponse.getBrowsingHistoryResponse()) {
            GoodsGroupDto goodsGroupDto = new GoodsGroupDto();

            GoodsGroupEntity goodsGroupEntity = toGoodsGroupEntity(browsinghistoryResponse.getGoodsGroupResponse());
            goodsGroupDto.setGoodsGroupEntity(goodsGroupEntity);

            List<GoodsGroupImageEntity> goodsGroupImageEntityList =
                            toGoodsGroupImageEntityList(browsinghistoryResponse.getGoodsGroupImageResponseList());
            goodsGroupDto.setGoodsGroupImageEntityList(goodsGroupImageEntityList);

            StockStatusDisplayEntity stockStatusDisplayEntity =
                            toStockStatusDisplayEntity(browsinghistoryResponse.getStockStatusDisplayResponse());
            goodsGroupDto.setBatchUpdateStockStatus(stockStatusDisplayEntity);

            List<GoodsInformationIconDetailsDto> goodsInformationIconDetailDtoList =
                            toGoodsInformationIconDetailDtoList(
                                            browsinghistoryResponse.getGoodsInformationIconDetailResponse());
            goodsGroupDto.setGoodsInformationIconDetailsDtoList(goodsInformationIconDetailDtoList);

            goodsGroupDtoList.add(goodsGroupDto);
        }

        return goodsGroupDtoList;
    }

    /**
     * 商品グループスに変換
     *
     * @param response あしあと商品
     * @return 商品グループ
     */
    public GoodsGroupEntity toGoodsGroupEntity(GoodsGroupResponse response) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        GoodsGroupEntity goodsGroupEntity = new GoodsGroupEntity();

        goodsGroupEntity.setGoodsGroupSeq(response.getGoodsGroupSeq());
        goodsGroupEntity.setGoodsGroupCode(response.getGoodsGroupCode());
        goodsGroupEntity.setGoodsGroupName(response.getGoodsGroupName());
        goodsGroupEntity.setGoodsPrice(response.getGoodsPrice());
        goodsGroupEntity.setGoodsPriceInTax(response.getGoodsPriceInTax());
        goodsGroupEntity.setWhatsnewDate(conversionUtility.toTimestamp(response.getWhatsnewDate()));
        if (response.getGoodsOpenStatusPC() != null) {
            goodsGroupEntity.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                response.getGoodsOpenStatusPC()
                                                                               ));
        }
        goodsGroupEntity.setOpenStartTimePC(conversionUtility.toTimestamp(response.getOpenStartTimePC()));
        goodsGroupEntity.setOpenEndTimePC(conversionUtility.toTimestamp(response.getOpenEndTimePC()));
        if (response.getGoodsTaxType() != null) {
            goodsGroupEntity.setGoodsTaxType(
                            EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class, response.getGoodsTaxType()));
        }
        goodsGroupEntity.setTaxRate(response.getTaxRate());
        if (response.getAlcoholFlag() != null) {
            goodsGroupEntity.setAlcoholFlag(
                            EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class, response.getAlcoholFlag()));
        }
        if (response.getSnsLinkFlag() != null) {
            goodsGroupEntity.setSnsLinkFlag(
                            EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class, response.getSnsLinkFlag()));
        }
        goodsGroupEntity.setShopSeq(response.getShopSeq());
        goodsGroupEntity.setVersionNo(response.getVersionNo());
        goodsGroupEntity.setRegistTime(conversionUtility.toTimestamp(response.getRegistTime()));
        goodsGroupEntity.setUpdateTime(conversionUtility.toTimestamp(response.getUpdateTime()));

        return goodsGroupEntity;
    }

    /**
     * 商品グループスに変換
     *
     * @param response 関連商品レスポンス
     * @return 商品グループ
     */
    public GoodsGroupEntity toGoodsGroupEntity(RelationGoodsResponse response) {
        GoodsGroupEntity goodsGroupEntity = new GoodsGroupEntity();

        goodsGroupEntity.setGoodsGroupSeq(response.getGoodsGroupSeq());
        goodsGroupEntity.setGoodsGroupCode(response.getGoodsGroupCode());
        goodsGroupEntity.setGoodsGroupName(response.getGoodsGroupName());
        goodsGroupEntity.setGoodsPrice(response.getGoodsPrice());
        goodsGroupEntity.setGoodsPriceInTax(response.getGoodsPriceInTax());
        goodsGroupEntity.setWhatsnewDate(conversionUtility.toTimestamp(response.getWhatsnewDate()));
        if (response.getGoodsOpenStatus() != null) {
            goodsGroupEntity.setGoodsOpenStatusPC(
                            EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class, response.getGoodsOpenStatus()));
        }
        goodsGroupEntity.setOpenStartTimePC(conversionUtility.toTimestamp(response.getOpenStartTime()));
        goodsGroupEntity.setOpenEndTimePC(conversionUtility.toTimestamp(response.getOpenEndTime()));
        goodsGroupEntity.setTaxRate(response.getTaxRate());
        goodsGroupEntity.setRegistTime(conversionUtility.toTimestamp(response.getRegistTime()));
        goodsGroupEntity.setUpdateTime(conversionUtility.toTimestamp(response.getUpdateTime()));

        return goodsGroupEntity;
    }

    /**
     * 商品グループ画像レスポンスリストスに変換
     *
     * @param goodsGroupImageResponseList　browsinghistory商品グループ画像レスポンスリスト一覧
     * @return 商品グループ画像一覧
     */
    public List<GoodsGroupImageEntity> toGoodsGroupImageEntityList(List<jp.co.itechh.quad.browsinghistory.presentation.api.param.GoodsGroupImageResponse> goodsGroupImageResponseList) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsGroupImageResponseList)) {
            return null;
        }

        List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();
        goodsGroupImageResponseList.forEach(item -> {
            GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

            goodsGroupImageEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsGroupImageEntity.setImageTypeVersionNo(item.getImageTypeVersionNo());
            goodsGroupImageEntity.setImageFileName(item.getImageFileName());
            goodsGroupImageEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            goodsGroupImageEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

            goodsGroupImageEntityList.add(goodsGroupImageEntity);
        });

        return goodsGroupImageEntityList;
    }

    /**
     * 商品グループ画像レスポンスリストに変換
     *
     * @param goodsGroupImageResponseList　relation商品グループ画像クラス
     * @return 商品グループ画像レスポンスリスト
     */
    public List<GoodsGroupImageEntity> toGoodsGroupImageEntityListRelation(List<jp.co.itechh.quad.relation.presentation.api.param.GoodsGroupImageResponse> goodsGroupImageResponseList) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsGroupImageResponseList)) {
            return null;
        }

        List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();
        goodsGroupImageResponseList.forEach(item -> {
            GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

            goodsGroupImageEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsGroupImageEntity.setImageTypeVersionNo(item.getImageTypeVersionNo());
            goodsGroupImageEntity.setImageFileName(item.getImageFileName());
            goodsGroupImageEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
            goodsGroupImageEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

            goodsGroupImageEntityList.add(goodsGroupImageEntity);
        });

        return goodsGroupImageEntityList;
    }

    /**
     * 商品グループ在庫表示スに変換
     *
     * @param response browsinghistoryあしあと商品
     * @return 商品グループ在庫表示
     */
    public StockStatusDisplayEntity toStockStatusDisplayEntity(StockStatusDisplayResponse response) {

        // 商品グループ在庫表示更新バッチの処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        StockStatusDisplayEntity stockStatusDisplayEntity = new StockStatusDisplayEntity();

        stockStatusDisplayEntity.setGoodsGroupSeq(response.getGoodsGroupSeq());
        if (response.getStockStatusPc() != null) {
            stockStatusDisplayEntity.setStockStatusPc(
                            EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class, response.getStockStatusPc()));
        }
        stockStatusDisplayEntity.setRegistTime(conversionUtility.toTimestamp(response.getRegistTime()));
        stockStatusDisplayEntity.setUpdateTime(conversionUtility.toTimestamp(response.getUpdateTime()));

        return stockStatusDisplayEntity;
    }

    /**
     * 商品グループ在庫表示クラス スに変換
     *
     * @param response relation商品グループ在庫表示クラス
     * @return 在庫状態表示用レスポンス
     */
    public StockStatusDisplayEntity toStockStatusDisplayEntity(jp.co.itechh.quad.relation.presentation.api.param.StockStatusDisplayResponse response) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        StockStatusDisplayEntity stockStatusDisplayEntity = new StockStatusDisplayEntity();

        stockStatusDisplayEntity.setGoodsGroupSeq(response.getGoodsGroupSeq());
        if (response.getStockStatus() != null) {
            stockStatusDisplayEntity.setStockStatusPc(
                            EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class, response.getStockStatus()));
        }
        stockStatusDisplayEntity.setRegistTime(conversionUtility.toTimestamp(response.getRegistTime()));
        stockStatusDisplayEntity.setUpdateTime(conversionUtility.toTimestamp(response.getUpdateTime()));

        return stockStatusDisplayEntity;
    }

    /**
     * アイコン詳細レスポンス一覧スに変換
     *
     * @param goodsInformationIconDetailResponseList browsinghistoryアイコン詳細レスポンス一覧
     * @return アイコン詳細DTO一覧
     */
    public List<GoodsInformationIconDetailsDto> toGoodsInformationIconDetailDtoList(List<GoodsInformationIconDetailResponse> goodsInformationIconDetailResponseList) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsInformationIconDetailResponseList)) {
            return null;
        }

        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();
        goodsInformationIconDetailResponseList.forEach(item -> {
            GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();

            goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
            goodsInformationIconDetailsDto.setIconName(item.getIconName());
            goodsInformationIconDetailsDto.setColorCode(item.getColorCode());
            goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());

            goodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
        });

        return goodsInformationIconDetailsDtoList;
    }

    /**
     * 商品レスポンスクラススに変換
     *
     * @param goodsInformationIconDetailResponseList relation商品レスポンスクラス
     * @return アイコン詳細DTO一覧
     */
    public List<GoodsInformationIconDetailsDto> toGoodsInformationIconDetailDtoListRelation(List<jp.co.itechh.quad.relation.presentation.api.param.GoodsInformationIconDetailsResponse> goodsInformationIconDetailResponseList) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(goodsInformationIconDetailResponseList)) {
            return null;
        }

        List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();
        goodsInformationIconDetailResponseList.forEach(item -> {
            GoodsInformationIconDetailsDto goodsInformationIconDetailsDto = new GoodsInformationIconDetailsDto();

            goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
            goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
            goodsInformationIconDetailsDto.setIconName(item.getIconName());
            goodsInformationIconDetailsDto.setColorCode(item.getColorCode());
            goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());

            goodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
        });

        return goodsInformationIconDetailsDtoList;
    }

    /**
     * 関連商品一覧レスポンススに変換
     *
     * @param relationGoodsListResponse 関連商品一覧レスポンス
     * @return 商品グループDto一覧
     */
    public List<GoodsGroupDto> toRelationGoodsGroupDtoList(RelationGoodsListResponse relationGoodsListResponse) {
        if (ObjectUtils.isEmpty(relationGoodsListResponse) || CollectionUtils.isEmpty(
                        relationGoodsListResponse.getRelationGoodsList())) {
            return null;
        }

        List<GoodsGroupDto> relatedGoodsGroupDtoList = new ArrayList<>();
        for (RelationGoodsResponse relationGoodsResponse : relationGoodsListResponse.getRelationGoodsList()) {
            GoodsGroupDto goodsGroupDto = new GoodsGroupDto();

            GoodsGroupEntity goodsGroupEntity = toGoodsGroupEntity(relationGoodsResponse);
            goodsGroupDto.setGoodsGroupEntity(goodsGroupEntity);

            List<GoodsGroupImageEntity> goodsGroupImageEntityList =
                            toGoodsGroupImageEntityListRelation(relationGoodsResponse.getGoodsGroupImageList());
            goodsGroupDto.setGoodsGroupImageEntityList(goodsGroupImageEntityList);

            StockStatusDisplayEntity stockStatusDisplayEntity =
                            toStockStatusDisplayEntity(relationGoodsResponse.getBatchUpdateStockStatus());
            goodsGroupDto.setBatchUpdateStockStatus(stockStatusDisplayEntity);

            List<GoodsInformationIconDetailsDto> goodsInformationIconDetailDtoList =
                            toGoodsInformationIconDetailDtoListRelation(
                                            relationGoodsResponse.getGoodsInformationIconDetailsList());
            goodsGroupDto.setGoodsInformationIconDetailsDtoList(goodsInformationIconDetailDtoList);

            relatedGoodsGroupDtoList.add(goodsGroupDto);
        }

        return relatedGoodsGroupDtoList;
    }

    /**
     * お気に入り情報一覧レスポンススに変換
     *
     * @param wishlistListResponse お気に入り情報一覧レスポンス
     * @return お気に入り一覧
     */
    public List<WishlistEntity> toWishlistEntityList(WishlistListResponse wishlistListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(wishlistListResponse) || CollectionUtils.isEmpty(
                        wishlistListResponse.getWishListList())) {
            return null;
        }

        List<WishlistEntity> wishlistEntityList = new ArrayList<>();
        for (WishlistResponse wishlistResponse : wishlistListResponse.getWishListList()) {
            WishlistEntity wishlistEntity = new WishlistEntity();

            wishlistEntity.setGoodsSeq(wishlistResponse.getGoodsSeq());
            wishlistEntity.setMemberInfoSeq(wishlistResponse.getMemberInfoSeq());
            wishlistEntity.setRegistTime(conversionUtility.toTimestamp(wishlistResponse.getRegistTime()));
            wishlistEntity.setUpdateTime(conversionUtility.toTimestamp(wishlistResponse.getUpdateTime()));

            wishlistEntityList.add(wishlistEntity);
        }

        return wishlistEntityList;
    }

    /**
     * 在庫状況表示取得リクエストスに変換<br/>
     *
     * @param goodsDetailsDto 商品詳細DTO
     * @return 在庫状況表示取得リクエスト
     */
    protected InventoryStatusDisplayGetRequest toInventoryStatusDisplayGetRequest(GoodsDetailsDto goodsDetailsDto) {
        InventoryStatusDisplayGetRequest inventoryStatusDisplayGetRequest = new InventoryStatusDisplayGetRequest();
        inventoryStatusDisplayGetRequest.setSalesPossibleStock(goodsDetailsDto.getSalesPossibleStock());
        // 残少表示在庫数
        inventoryStatusDisplayGetRequest.setRemainderFewStock(goodsDetailsDto.getRemainderFewStock());
        // 在庫管理フラグ
        inventoryStatusDisplayGetRequest.setStockManagementFlag(goodsDetailsDto.getStockManagementFlag().getValue());
        // 販売状態、販売開始日、販売終了日
        if (goodsDetailsDto.getSaleStatusPC() != null) {
            inventoryStatusDisplayGetRequest.setSaleStatus(goodsDetailsDto.getSaleStatusPC().getValue());
        }
        if (goodsDetailsDto.getGoodsOpenStatusPC() != null) {
            inventoryStatusDisplayGetRequest.setOpenStatus(goodsDetailsDto.getGoodsOpenStatusPC().getValue());
        }
        inventoryStatusDisplayGetRequest.setSaleStartTime(goodsDetailsDto.getSaleStartTimePC());
        inventoryStatusDisplayGetRequest.setSaleEndTime(goodsDetailsDto.getSaleEndTimePC());

        return inventoryStatusDisplayGetRequest;
    }

    /**
     * カート商品Itemをカート商品Dtoに変換
     *
     * @param cartModelItem カート商品Item
     * @return カート商品Dto
     */
    protected CartGoodsDto toCartGoodsDto(CartModelItem cartModelItem) {
        CartGoodsDto cartGoodsDto = ApplicationContextUtility.getBean(CartGoodsDto.class);
        GoodsDetailsDto goodsDetailsDto = ApplicationContextUtility.getBean(GoodsDetailsDto.class);
        CartGoodsEntity cartGoodsEntity = ApplicationContextUtility.getBean(CartGoodsEntity.class);

        goodsDetailsDto.setGoodsSeq(cartModelItem.getGoodsSeq());
        goodsDetailsDto.setGoodsCode(cartModelItem.getGcd());
        goodsDetailsDto.setGoodsGroupCode(cartModelItem.getGgcd());
        goodsDetailsDto.setGoodsGroupName(cartModelItem.getGoodsGroupName());
        goodsDetailsDto.setUnitManagementFlag(
                        cartModelItem.isUnit1() ? HTypeUnitManagementFlag.ON : HTypeUnitManagementFlag.OFF);
        goodsDetailsDto.setUnitTitle1(cartModelItem.getUnitTitle1());
        goodsDetailsDto.setUnitTitle2(cartModelItem.getUnitTitle2());
        goodsDetailsDto.setUnitValue1(cartModelItem.getUnitValue1());
        goodsDetailsDto.setUnitValue2(cartModelItem.getUnitValue2());
        goodsDetailsDto.setGoodsPrice(cartModelItem.getGoodsPrice());
        goodsDetailsDto.setGoodsPriceInTax(cartModelItem.getGoodsPriceInTax());

        // 商品数量
        BigDecimal cartGoodsCount = conversionUtility.toBigDecimal(cartModelItem.getGcnt());
        cartGoodsEntity.setCartGoodsCount(cartGoodsCount);
        cartGoodsEntity.setCartSeq(cartModelItem.getCartSeq());
        cartGoodsEntity.setGoodsSeq(cartModelItem.getGoodsSeq());

        cartGoodsDto.setGoodsDetailsDto(goodsDetailsDto);
        cartGoodsDto.setCartGoodsEntity(cartGoodsEntity);

        return cartGoodsDto;
    }

    /**
     * カートDtoに変換<br/>
     *
     * @param orderSlipResponse 注文票レスポンス
     * @param productDetailListResponse 商品詳細一覧レスポンス
     * @param itemSalesPriceCalculateResponse 商品販売金額計算レスポンス
     * @return カートDto
     */
    protected CartDto convertToCartInfo(OrderSlipResponse orderSlipResponse,
                                        ProductDetailListResponse productDetailListResponse,
                                        ItemSalesPriceCalculateResponse itemSalesPriceCalculateResponse) {
        int index = 0;
        CartDto cartDto = new CartDto();

        cartDto.setGoodsTotalCount(conversionUtility.toBigDecimal(orderSlipResponse.getTotalItemCount()));
        if (!ObjectUtils.isEmpty(itemSalesPriceCalculateResponse)) {
            // 合計製品価格を設定します
            cartDto.setGoodsTotalPriceInTax(conversionUtility.toBigDecimal(
                            itemSalesPriceCalculateResponse.getItemSalesPriceTotalInTax()));
            cartDto.setGoodsTotalPrice(
                            conversionUtility.toBigDecimal(itemSalesPriceCalculateResponse.getItemSalesPriceTotal()));
        }

        if (!ObjectUtils.isEmpty(productDetailListResponse) && CollectionUtil.isNotEmpty(
                        productDetailListResponse.getGoodsDetailsList())) {
            List<CartGoodsDto> cartGoodsDtoList = new ArrayList<>();
            for (GoodsDetailsResponse goodsDetailsResponse : productDetailListResponse.getGoodsDetailsList()) {
                CartGoodsDto cartGoodsDto = new CartGoodsDto();

                CartGoodsEntity cartGoodsEntity = new CartGoodsEntity();
                cartGoodsEntity.setCartSeq(index);
                cartGoodsEntity.setGoodsSeq(goodsDetailsResponse.getGoodsSeq());
                if (!CollectionUtils.isEmpty(orderSlipResponse.getItemList())) {
                    cartGoodsEntity.setCartGoodsCount(conversionUtility.toBigDecimal(
                                    orderSlipResponse.getItemList().get(index).getItemCount()));
                }
                cartGoodsDto.setCartGoodsEntity(cartGoodsEntity);

                GoodsDetailsDto goodsDetailsDto = new GoodsDetailsDto();
                goodsDetailsDto.setGoodsGroupName(goodsDetailsResponse.getGoodsGroupName());
                goodsDetailsDto.setGoodsGroupSeq(goodsDetailsResponse.getGoodsGroupSeq());
                goodsDetailsDto.setVersionNo(goodsDetailsResponse.getVersionNo());
                goodsDetailsDto.setGoodsCode(goodsDetailsResponse.getGoodsCode());
                goodsDetailsDto.setGoodsSeq(goodsDetailsResponse.getGoodsSeq());
                goodsDetailsDto.setGoodsPriceInTax(goodsDetailsResponse.getGoodsPriceInTax());
                if (goodsDetailsResponse.getGoodsTaxType() != null) {
                    goodsDetailsDto.setGoodsTaxType(EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class,
                                                                                  goodsDetailsResponse.getGoodsTaxType()
                                                                                 ));
                }
                goodsDetailsDto.setTaxRate(goodsDetailsResponse.getTaxRate());
                if (goodsDetailsResponse.getAlcoholFlag() != null) {
                    goodsDetailsDto.setAlcoholFlag(EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class,
                                                                                 goodsDetailsResponse.getAlcoholFlag()
                                                                                ));
                }
                goodsDetailsDto.setGoodsPrice(goodsDetailsResponse.getGoodsPrice());
                goodsDetailsDto.setDeliveryType(goodsDetailsResponse.getDeliveryType());
                if (goodsDetailsResponse.getSaleStatus() != null) {
                    goodsDetailsDto.setSaleStatusPC(EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class,
                                                                                  goodsDetailsResponse.getSaleStatus()
                                                                                 ));
                }
                goodsDetailsDto.setSaleStartTimePC(
                                conversionUtility.toTimestamp(goodsDetailsResponse.getSaleStartTime()));
                goodsDetailsDto.setSaleEndTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getSaleEndTime()));
                if (goodsDetailsResponse.getUnitManagementFlag() != null) {
                    goodsDetailsDto.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                                        goodsDetailsResponse.getUnitManagementFlag()
                                                                                       ));
                }
                if (goodsDetailsResponse.getStockManagementFlag() != null) {
                    goodsDetailsDto.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                                         goodsDetailsResponse.getStockManagementFlag()
                                                                                        ));
                }
                if (goodsDetailsResponse.getIndividualDeliveryType() != null) {
                    goodsDetailsDto.setIndividualDeliveryType(
                                    EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                                  goodsDetailsResponse.getIndividualDeliveryType()
                                                                 ));
                }
                goodsDetailsDto.setPurchasedMax(goodsDetailsResponse.getPurchasedMax());
                if (goodsDetailsResponse.getFreeDeliveryFlag() != null) {
                    goodsDetailsDto.setFreeDeliveryFlag(EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class,
                                                                                      goodsDetailsResponse.getFreeDeliveryFlag()
                                                                                     ));
                }
                goodsDetailsDto.setOrderDisplay(goodsDetailsResponse.getOrderDisplay());
                goodsDetailsDto.setUnitValue1(goodsDetailsResponse.getUnitValue1());
                goodsDetailsDto.setUnitValue2(goodsDetailsResponse.getUnitValue2());
                goodsDetailsDto.setJanCode(goodsDetailsResponse.getJanCode());
                goodsDetailsDto.setSalesPossibleStock(goodsDetailsResponse.getSalesPossibleStock());
                goodsDetailsDto.setRealStock(goodsDetailsResponse.getRealStock());
                goodsDetailsDto.setOrderReserveStock(goodsDetailsResponse.getOrderReserveStock());
                goodsDetailsDto.setRemainderFewStock(goodsDetailsResponse.getRemainderFewStock());
                goodsDetailsDto.setOrderPointStock(goodsDetailsResponse.getOrderPointStock());
                goodsDetailsDto.setSafetyStock(goodsDetailsResponse.getSafetyStock());
                goodsDetailsDto.setGoodsGroupCode(goodsDetailsResponse.getGoodsGroupCode());
                goodsDetailsDto.setWhatsnewDate(conversionUtility.toTimestamp(goodsDetailsResponse.getWhatsnewDate()));
                if (goodsDetailsResponse.getGoodsOpenStatus() != null) {
                    goodsDetailsDto.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                       goodsDetailsResponse.getGoodsOpenStatus()
                                                                                      ));
                }
                goodsDetailsDto.setOpenStartTimePC(
                                conversionUtility.toTimestamp(goodsDetailsResponse.getOpenStartTime()));
                goodsDetailsDto.setOpenEndTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getOpenEndTime()));
                goodsDetailsDto.setUnitTitle1(goodsDetailsResponse.getUnitTitle1());
                goodsDetailsDto.setUnitTitle2(goodsDetailsResponse.getUnitTitle2());

                List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(goodsDetailsResponse.getGoodsGroupImageList())) {
                    for (jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse goodsGroupImageResponse : goodsDetailsResponse.getGoodsGroupImageList()) {
                        GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();
                        goodsGroupImageEntity.setGoodsGroupSeq(goodsGroupImageResponse.getGoodsGroupSeq());
                        goodsGroupImageEntity.setImageTypeVersionNo(goodsGroupImageResponse.getImageTypeVersionNo());
                        goodsGroupImageEntity.setImageFileName(goodsGroupImageResponse.getImageFileName());
                        goodsGroupImageEntity.setRegistTime(
                                        conversionUtility.toTimestamp(goodsGroupImageResponse.getRegistTime()));
                        goodsGroupImageEntity.setUpdateTime(
                                        conversionUtility.toTimestamp(goodsGroupImageResponse.getUpdateTime()));
                        goodsGroupImageEntityList.add(goodsGroupImageEntity);
                    }
                }
                goodsDetailsDto.setGoodsGroupImageEntityList(goodsGroupImageEntityList);

                List<GoodsInformationIconDetailsDto> goodsInformationIconDetailsDtoList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(goodsDetailsResponse.getGoodsIconList())) {
                    for (jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse goodsInformationIconDetailsResponse : goodsDetailsResponse.getGoodsIconList()) {
                        GoodsInformationIconDetailsDto goodsInformationIconDetailsDto =
                                        new GoodsInformationIconDetailsDto();
                        goodsInformationIconDetailsDto.setGoodsGroupSeq(
                                        goodsInformationIconDetailsResponse.getGoodsGroupSeq());
                        goodsInformationIconDetailsDto.setIconName(goodsInformationIconDetailsResponse.getIconName());
                        goodsInformationIconDetailsDto.setIconSeq(goodsInformationIconDetailsResponse.getIconSeq());
                        goodsInformationIconDetailsDto.setOrderDisplay(
                                        goodsInformationIconDetailsResponse.getOrderDisplay());
                        goodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
                    }
                }
                goodsDetailsDto.setGoodsIconList(goodsInformationIconDetailsDtoList);

                cartGoodsDto.setGoodsInformationIconDetailsDtoList(goodsInformationIconDetailsDtoList);

                if (goodsDetailsResponse.getSnsLinkFlag() != null) {
                    goodsDetailsDto.setSnsLinkFlag(EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class,
                                                                                 goodsDetailsResponse.getSnsLinkFlag()
                                                                                ));
                }
                goodsDetailsDto.setMetaDescription(goodsDetailsResponse.getMetaDescription());
                if (goodsDetailsResponse.getStockStatus() != null) {
                    goodsDetailsDto.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                                   goodsDetailsResponse.getStockStatus()
                                                                                  ));
                }
                goodsDetailsDto.setGoodsNote1(goodsDetailsResponse.getGoodsNote1());
                goodsDetailsDto.setGoodsNote2(goodsDetailsResponse.getGoodsNote2());
                goodsDetailsDto.setGoodsNote3(goodsDetailsResponse.getGoodsNote3());
                goodsDetailsDto.setGoodsNote4(goodsDetailsResponse.getGoodsNote4());
                goodsDetailsDto.setGoodsNote5(goodsDetailsResponse.getGoodsNote5());
                goodsDetailsDto.setGoodsNote6(goodsDetailsResponse.getGoodsNote6());
                goodsDetailsDto.setGoodsNote7(goodsDetailsResponse.getGoodsNote7());
                goodsDetailsDto.setGoodsNote8(goodsDetailsResponse.getGoodsNote8());
                goodsDetailsDto.setGoodsNote9(goodsDetailsResponse.getGoodsNote9());
                goodsDetailsDto.setGoodsNote10(goodsDetailsResponse.getGoodsNote10());
                goodsDetailsDto.setOrderSetting1(goodsDetailsResponse.getOrderSetting1());
                goodsDetailsDto.setOrderSetting2(goodsDetailsResponse.getOrderSetting2());
                goodsDetailsDto.setOrderSetting3(goodsDetailsResponse.getOrderSetting3());
                goodsDetailsDto.setOrderSetting4(goodsDetailsResponse.getOrderSetting4());
                goodsDetailsDto.setOrderSetting5(goodsDetailsResponse.getOrderSetting5());
                goodsDetailsDto.setOrderSetting6(goodsDetailsResponse.getOrderSetting6());
                goodsDetailsDto.setOrderSetting7(goodsDetailsResponse.getOrderSetting7());
                goodsDetailsDto.setOrderSetting8(goodsDetailsResponse.getOrderSetting8());
                goodsDetailsDto.setOrderSetting9(goodsDetailsResponse.getOrderSetting9());
                goodsDetailsDto.setOrderSetting10(goodsDetailsResponse.getOrderSetting10());
                cartGoodsDto.setGoodsDetailsDto(goodsDetailsDto);

                cartGoodsDto.setGoodsPriceSubtotal(goodsDetailsResponse.getGoodsPrice());
                cartGoodsDto.setGoodsPriceInTaxSubtotal(goodsDetailsResponse.getGoodsPriceInTax());

                cartGoodsDtoList.add(cartGoodsDto);
                index++;
            }

            cartDto.setCartGoodsDtoList(cartGoodsDtoList);
        }
        return cartDto;
    }

    /**
     * 注文票レスポンスから商品SEQリストを取得<br/>
     *
     * @param orderSlipResponse 注文票レスポンス
     * @return 商品SEQリスト
     */
    protected List<Integer> toItemIdList(OrderSlipResponse orderSlipResponse) {
        // 処理前は存在しないためnullを返す
        if (CollectionUtils.isEmpty(orderSlipResponse.getItemList())) {
            return null;
        }

        List<Integer> listItemId = new ArrayList<>();
        for (OrderSlipResponseItemList orderSlipResponseItemList : orderSlipResponse.getItemList()) {
            if (orderSlipResponseItemList.getItemId() != null) {
                listItemId.add(Integer.parseInt(orderSlipResponseItemList.getItemId()));
            }
        }

        return listItemId;
    }

    /**
     * ソート商品詳細一覧レスポンス<br/>
     *
     * @param listIdOrderBy 商品SEQリスト
     * @param productDetailListResponseList 商品詳細一覧レスポンス
     * @return ソート済み商品詳細一覧レスポンス
     */
    protected void reSortByItemListForDisplay(List<Integer> listIdOrderBy,
                                              ProductDetailListResponse productDetailListResponseList) {
        if (!ObjectUtils.isEmpty(productDetailListResponseList) && !CollectionUtils.isEmpty(
                        productDetailListResponseList.getGoodsDetailsList())) {
            List<GoodsDetailsResponse> goodsDetailsResponseList = productDetailListResponseList.getGoodsDetailsList();
            goodsDetailsResponseList.sort(Comparator.comparing(item -> listIdOrderBy.indexOf(item.getGoodsSeq())));
        }
    }

    /**
     * 注文商品数量更新リクエストに変換<br/>
     *
     * @param cartGoodsDtoList カート商品Dtoリスト
     * @return 注文商品数量更新リクエスト
     */
    protected OrderItemCountListUpdateRequest toOrderItemCountListUpdateRequest(List<CartGoodsDto> cartGoodsDtoList) {

        OrderItemCountListUpdateRequest orderItemCountListUpdateRequest = new OrderItemCountListUpdateRequest();

        if (!CollectionUtils.isEmpty(cartGoodsDtoList)) {
            List<OrderItemCountListUpdateRequestItemList> orderItemCountListUpdateRequestItemLists = new ArrayList<>();

            for (CartGoodsDto cartGoodsDto : cartGoodsDtoList) {
                if (!ObjectUtils.isEmpty(cartGoodsDto.getCartGoodsEntity())) {
                    OrderItemCountListUpdateRequestItemList orderItemCountListUpdateRequestItemList =
                                    new OrderItemCountListUpdateRequestItemList();

                    if (cartGoodsDto.getCartGoodsEntity().getGoodsSeq() != null) {
                        orderItemCountListUpdateRequestItemList.setItemId(
                                        cartGoodsDto.getCartGoodsEntity().getGoodsSeq().toString());
                    }
                    if (cartGoodsDto.getCartGoodsEntity().getCartGoodsCount() != null) {
                        orderItemCountListUpdateRequestItemList.setItemCount(
                                        cartGoodsDto.getCartGoodsEntity().getCartGoodsCount().intValue());
                    }

                    orderItemCountListUpdateRequestItemLists.add(orderItemCountListUpdateRequestItemList);
                }
            }
            orderItemCountListUpdateRequest.setItemList(orderItemCountListUpdateRequestItemLists);
        }

        return orderItemCountListUpdateRequest;
    }

    protected Map<String, List<ErrorContent>> getErrorMap(ClientErrorResponse errorResponse) {
        if (errorResponse.getMessages() != null) {
            return errorResponse.getMessages();
        } else {
            return null;
        }
    }
}