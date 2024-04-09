/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist;

import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;

/**
 * お気に入り情報登録ロジック
 *
 * @author ueshima
 * @version $Revision: 1.2 $
 */
public interface WishlistRegistLogic {

    /**
     * ロジック実行
     *
     * @param wishlistEntity お気に入りエンティティ
     * @return 登録件数
     */
    int execute(WishlistEntity wishlistEntity);
}
