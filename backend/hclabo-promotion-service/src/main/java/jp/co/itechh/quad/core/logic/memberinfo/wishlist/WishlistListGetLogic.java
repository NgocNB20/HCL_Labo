/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist;

import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;

import java.util.List;

/**
 * お気に入り情報リスト取得ロジック
 *
 * @author ueshima
 * @version $Revision: 1.4 $
 */
public interface WishlistListGetLogic {

    /**
     * ロジック実行
     *
     * @param wishlistConditionDto お気に入り検索条件DTO
     * @return お気に入りエンティティリスト
     */
    List<WishlistEntity> execute(WishlistSearchForDaoConditionDto wishlistConditionDto);
}
