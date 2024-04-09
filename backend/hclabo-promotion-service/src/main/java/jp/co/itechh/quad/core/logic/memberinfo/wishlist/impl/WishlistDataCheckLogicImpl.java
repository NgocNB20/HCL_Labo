/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.dao.memberinfo.wishlist.WishlistDao;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistDataCheckLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * お気に入りデータチェックロジック
 *
 * @author ueshima
 * @version $Revision: 1.4 $
 */
@Component
public class WishlistDataCheckLogicImpl extends AbstractShopLogic implements WishlistDataCheckLogic {

    /**
     * お気に入りDao
     */
    private final WishlistDao wishlistDao;

    @Autowired
    public WishlistDataCheckLogicImpl(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    /**
     * ロジック実行
     *
     * @param wishlistEntity お気に入りエンティティ
     */
    @Override
    public void execute(WishlistEntity wishlistEntity) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("wishlistEntity", wishlistEntity);

        // お気に入り商品の重複確認
        List<Integer> goodsSeqList = wishlistDao.getGoodsSeqList(wishlistEntity.getMemberInfoSeq());

        // お気に入り登録されていない場合
        if (!goodsSeqList.contains(wishlistEntity.getGoodsSeq())) {
            goodsSeqList.add(wishlistEntity.getGoodsSeq());
        }

        // お気に入り最大登録数超過チェック
        int wishlistGoodsMax = Integer.parseInt(PropertiesUtil.getSystemPropertiesValue("wishlist.goods.max"));
        if (goodsSeqList.size() > wishlistGoodsMax) {
            addErrorMessage(MSGCD_WISHLIST_GOODS_MAX_COUNT_FAIL, new Object[] {wishlistGoodsMax});
        }

        // 例外出力
        if (hasErrorList()) {
            throwMessage();
        }
    }

}