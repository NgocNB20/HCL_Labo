/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.wishlist;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistDto;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;

import java.util.List;

/**
 * お気に入り情報リスト取得サービス
 *
 * @author ueshima
 * @version $Revision: 1.3 $
 */
public interface WishlistListGetService {

    /**
     * お気に入り情報リスト取得処理
     * <p>
     * ログイン会員のお気に入り情報を取得する。
     *
     * @param siteType             サイト種別
     * @param wishlistConditionDto お気に入り検索条件Dto
     * @return お気に入りDTOリスト list
     */
    List<WishlistDto> execute(HTypeSiteType siteType, WishlistSearchForDaoConditionDto wishlistConditionDto);

}
