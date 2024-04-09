/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist;

import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;

/**
 * お気に入り更新ロジック
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public interface WishlistUpdateLogic {

    // LMF0005

    /**
     * お気に入り更新処理
     *
     * @param wishlistEntity お気に入りエンティティ
     * @return 更新処理
     */
    int execute(WishlistEntity wishlistEntity);
}
