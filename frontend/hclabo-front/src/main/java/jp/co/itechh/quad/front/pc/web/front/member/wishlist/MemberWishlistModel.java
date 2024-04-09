/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.wishlist;

import jp.co.itechh.quad.front.pc.web.front.goods.common.GoodsItem;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import java.util.List;

/**
 * 会員お気に入り Model
 *
 * @author PHAM QUANG DIEU (VJP)
 * @version $Revision: 1.0 $
 *
 */
@Data
public class MemberWishlistModel extends AbstractModel {

    /**
     * 一覧情報
     */

    /** 削除か追加を判断するモード（URLパラメータ） */
    private String md;

    /** お気に入り一覧情報 */
    private List<GoodsItem> wishlistItems;

    /** 商品コード */
    private String gcd;

    /**
     * 遷移元情報
     */

    /** カートからの遷移 */
    private boolean fromCart;

    /** 商品詳細からの遷移 */
    private boolean fromGoodsDetails;

    /**
     * 一覧情報の空判定<br/>
     *
     * @return 一覧情報の空判定
     */
    public boolean isWishlistEmpty() {

        if (wishlistItems == null) {
            return true;
        }
        return wishlistItems.isEmpty();
    }

}