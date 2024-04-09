/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist;

import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistDto;

import java.util.List;

/**
 * お気に入り商品の在庫状態取得ロジック
 *
 * @author Kaneko　2013/03/01
 */
public interface WishlistGoodsStockStatusGetLogic {

    /**
     * お気に入り商品の在庫状態の設定。
     * <pre>
     * 商品の公開状態、公開期間、販売状態、販売期間、在庫数条件に基づいて在庫状態を決定する。
     * 在庫状態判定の詳細は「26_HM3_共通部仕様書_在庫状態表示条件.xls」参照。
     * </pre>
     * @param wishlistDtoList お気に入り商品DTOリスト
     * @return 在庫ステータス
     */
    List<WishlistDto> execute(List<WishlistDto> wishlistDtoList);
}
