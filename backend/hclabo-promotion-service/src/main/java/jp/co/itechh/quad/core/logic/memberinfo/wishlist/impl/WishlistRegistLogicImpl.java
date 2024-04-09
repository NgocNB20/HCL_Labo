/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.memberinfo.wishlist.WishlistDao;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * お気に入り情報登録ロジック
 *
 * @author ueshima
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
@Component
public class WishlistRegistLogicImpl extends AbstractShopLogic implements WishlistRegistLogic {

    /**
     * お気に入りDao
     */
    private final WishlistDao wishlistDao;

    @Autowired
    public WishlistRegistLogicImpl(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    /**
     * ロジック実行
     *
     * @param wishlistEntity お気に入りエンティティ
     * @return 登録件数
     */
    @Override
    public int execute(WishlistEntity wishlistEntity) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("wishlistEntity", wishlistEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 登録日時・更新日時の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        wishlistEntity.setRegistTime(currentTime);
        wishlistEntity.setUpdateTime(currentTime);

        // お気に入り登録の実行
        return wishlistDao.insert(wishlistEntity);
    }

}
