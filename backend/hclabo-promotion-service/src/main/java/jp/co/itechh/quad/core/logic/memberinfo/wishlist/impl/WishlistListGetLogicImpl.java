/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.dao.memberinfo.wishlist.WishlistDao;
import jp.co.itechh.quad.core.dto.memberinfo.wishlist.WishlistSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * お気に入り情報リスト取得ロジック
 *
 * @author ueshima
 * @version $Revision: 1.5 $
 */
@Component
public class WishlistListGetLogicImpl extends AbstractShopLogic implements WishlistListGetLogic {

    /**
     * お気に入りDao
     **/
    private final WishlistDao wishlistDao;

    @Autowired
    public WishlistListGetLogicImpl(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    /**
     * ロジック実行
     *
     * @param wishlistConditionDto お気に入り検索条件DTO
     * @return お気に入りエンティティリスト
     */
    @Override
    public List<WishlistEntity> execute(WishlistSearchForDaoConditionDto wishlistConditionDto) {

        // 引数チェック
        AssertionUtil.assertNotNull("wishlistConditionDto", wishlistConditionDto);

        return wishlistDao.getSearchWishlistList(
                        wishlistConditionDto, wishlistConditionDto.getPageInfo().getSelectOptions());
    }

}