/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist;

import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;

/**
 * お気に入りデータチェックロジック
 *
 * @author ueshima
 * @version $Revision: 1.2 $
 */
public interface WishlistDataCheckLogic {

    /**
     * お気に入り最大登録件数を超えた場合
     * <code>MSGCD_WISHLIST_GOODS_MAX_COUNT_FAIL</code>
     */
    String MSGCD_WISHLIST_GOODS_MAX_COUNT_FAIL = "LMF000401";

    /**
     * ロジック実行
     *
     * @param wishlistEntity お気に入りエンティティ
     */
    void execute(WishlistEntity wishlistEntity);

}
