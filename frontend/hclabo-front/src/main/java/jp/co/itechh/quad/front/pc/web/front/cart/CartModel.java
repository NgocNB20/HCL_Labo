/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.cart;

import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * ショッピングカートModel
 * @author kaneda
 */
@Data
public class CartModel extends AbstractModel {

    /** カート投入エラー */
    public static final String MSGCD_CART_ADD_ERROR = "ACX000101";

    /** カートエラー */
    public static final String MSGCD_CART_ERROR = "ACX000102";

    /** 個別配送商品エラー */
    public static final String MSGCD_INDIVIDUAL_DELIVERY_ERROR = "ACX000103";

    /** ページ再表示 のメッセージ */
    public static final String MSGCD_PAGE_RELOAD = "ACX000104";

    /** カート投入_公開エラー */
    public static final String MSGCD_CART_OPEN_ERROR = "ACX000106";

    /** カート投入_販売エラー */
    public static final String MSGCD_CART_SALSE_ERROR = "ACX000107";

    /** カート投入_在庫切れエラー */
    public static final String MSGCD_CART_STOCK_ERROR = "ACX000108";

    /** カート投入_在庫不足 */
    public static final String MSGCD_CART_LESS_STOCK_ERROR = "ACX000109";

    /** カート投入_酒類購入不可エラー */
    public static final String MSGCD_CART_ALCOHOL_ERROR = "ACX000115";

    /**
     * 公開状態チェックメッセージ<非公開><br/>
     * <code>MSGCD_OPEN_STATUS_HIKOUKAI</code>
     */
    public static final String MSGCD_OPEN_STATUS_HIKOUKAI = "LCC000602";

    /**
     * 公開期間チェックメッセージ<公開前><br/>
     * <code>MSGCD_OPEN_BEFORE</code>
     */
    public static final String MSGCD_OPEN_BEFORE = "LCC000604";

    /**
     * 公開期間チェックメッセージ<公開終了><br/>
     * <code>MSGCD_OPEN_END</code>
     */
    public static final String MSGCD_OPEN_END = "LCC000605";

    /**
     * 販売状態チェックメッセージ<非販売><br/>
     * <code>MSGCD_SALE_STATUS_HIHANBAI</code>
     */
    public static final String MSGCD_SALE_STATUS_HIHANBAI = "LCC000606";

    /**
     * 販売期間チェックメッセージ<販売前><br/>
     * <code>MSGCD_SALE_BEFORE</code>
     */
    public static final String MSGCD_SALE_BEFORE = "LCC000608";

    /**
     * 販売期間チェックメッセージ<販売終了><br/>
     * <code>MSGCD_SALE_END</code>
     */
    public static final String MSGCD_SALE_END = "LCC000609";

    /**
     * 在庫切れチェックメッセージ<在庫切れ><br/>
     * <code>MSGCD_SALE_END</code>
     */
    public static final String MSGCD_NO_STOCK = "LCC000610";

    /**
     * 在庫不足チェックメッセージ<在庫不足><br/>
     * <code>MSGCD_SALE_END</code>
     */
    public static final String MSGCD_LESS_STOCK = "LCC000611";

    /**
     * 最大購入可能数超過チェックメッセージ<br/>
     * <code>MSGCD_SALE_END</code>
     */
    public static final String MSGCD_PURCHASED_MAX_OVER = "LCC000612";

    /**
     * 個別配送商品チェックメッセージ<br/>
     * <code>MSGCD_INDIVIDUAL_DELIVERY</code>
     */
    public static final String MSGCD_INDIVIDUAL_DELIVERY = "LCC000617";

    /**
     * 個別配送商品<br/>
     * <code>INDIVIDUAL_DELIVERY</code>
     */
    public static final String INDIVIDUAL_DELIVERY_WARNING = "LCC000617W";

    /**
     * 酒類購入不可チェックメッセージ<br/>
     * <code>MSGCD_ALCOHOL_CANNOT_BE_PURCHASED</code>
     */
    public static final String MSGCD_ALCOHOL_CANNOT_BE_PURCHASED = "PKG-4113-004-L";

    /**
     * 酒類<br/>
     * <code>ALCOHOL_CANNOT_BE_PURCHASED_WARNING</code>
     */
    public static final String ALCOHOL_CANNOT_BE_PURCHASED_WARNING = "PKG-4113-004-L-W";

    /** 商品合計数量 */
    private BigDecimal goodsCountTotal;

    /** 商品合計金額(税抜き) */
    private BigDecimal priceTotal;

    /** 商品合計金額(税込み) */
    private BigDecimal priceTotalInTax;

    /* 各種Items */

    /** お気に入り情報リスト */
    private List<CartModelItem> wishlistGoodsItems;

    /** お気に入り情報リストのインデックス */
    private int wishlistGoodsIndex;

    /** お気に入り情報リスト表示最大件数 */
    private int wishlistGoodsLimit;

    /** あしあと情報リスト */
    private List<CartModelItem> browsingHistoryGoodsItems;

    /** あしあと情報リストのインデックス */
    private int browsingHistoryGoodsIndex;

    /** あしあと情報リスト表示最大件数 */
    private int browsingHistoryGoodsLimit;

    /** 関連商品情報リスト */
    private List<CartModelItem> relatedGoodsItems;

    /** 関連商品情報リストのインデックス */
    private int relatedGoodsIndex;

    /** 関連商品情報リスト表示最大件数 */
    private int relatedGoodsLimit;

    /** カート一覧情報 */
    @Valid
    private List<CartModelItem> cartGoodsItems;

    /** カート一覧情報のインデックス */
    private int cartGoodsIndex;

    /* Condition */

    /**
     * カートチェック<br/>
     *
     * @return true:カート内が空ではない
     */
    public boolean isCartIn() {
        return CollectionUtil.getSize(cartGoodsItems) > 0;
    }

    /**
     * お気に入り情報表示チェック<br/>
     *
     * @return true:表示
     */
    public boolean isViewWishlistGoods() {
        return CollectionUtil.getSize(wishlistGoodsItems) > 0;
    }

    /**
     * 関連商品情報表示チェック<br/>
     *
     * @return true:表示
     */
    public boolean isViewRelatedGoods() {
        return CollectionUtil.getSize(relatedGoodsItems) > 0;
    }

}