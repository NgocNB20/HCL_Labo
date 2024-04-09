/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.memberinfo.wishlist.WishlistDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistListDeleteLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * お気に入り情報リスト削除ロジック
 *
 * @author ueshima
 * @version $Revision: 1.4 $
 */
@Component
public class WishlistListDeleteLogicImpl extends AbstractShopLogic implements WishlistListDeleteLogic {

    /**
     * お気に入りDao
     */
    private final WishlistDao wishlistDao;

    @Autowired
    public WishlistListDeleteLogicImpl(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    /**
     * お気に入り削除処理
     *
     * @param memberInfoSeq 会員SEQ
     * @param goodsSeqs 商品SEQ配列
     * @return 削除件数
     */
    @Override
    public int execute(Integer memberInfoSeq, Integer[] goodsSeqs) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);
        ArgumentCheckUtil.assertNotEmpty("goodsSeqList", goodsSeqs);

        // お気に入り削除の実行
        return wishlistDao.deleteList(memberInfoSeq, goodsSeqs);
    }

    /**
     * お気に入り削除処理
     *
     * @param memberInfoSeq 会員SEQ
     * @return 削除件数
     */
    @Override
    public int execute(Integer memberInfoSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);

        // お気に入り削除の実行
        return wishlistDao.deleteListByMemberInfoSeq(memberInfoSeq);
    }

    /**
     * お気に入り削除処理
     *
     * @param memberInfoSeq 会員SEQ
     * @param goodsCodes 商品コード
     * @return 削除件数
     */
    @Override
    public int execute(Integer memberInfoSeq, String[] goodsCodes) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);
        ArgumentCheckUtil.assertNotEmpty("goodsCode", goodsCodes);

        // お気に入り削除の実行
        return wishlistDao.deleteListByGoodsCode(memberInfoSeq, goodsCodes);
    }

}
