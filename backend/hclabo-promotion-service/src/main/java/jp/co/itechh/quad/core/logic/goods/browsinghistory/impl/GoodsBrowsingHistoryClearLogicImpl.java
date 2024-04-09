/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.browsinghistory.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.dao.goods.browsinghistory.BrowsinghistoryDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.browsinghistory.GoodsBrowsingHistoryClearLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * あしあとクリア<br/>
 * あしあと商品リストを削除する。<br/>
 *
 * @author ozaki
 * @version $Revision: 1.3 $
 *
 */
@Component
public class GoodsBrowsingHistoryClearLogicImpl extends AbstractShopLogic implements GoodsBrowsingHistoryClearLogic {

    /**
     * あしあと情報DAO
     */
    private final BrowsinghistoryDao browsinghistoryDao;

    @Autowired
    public GoodsBrowsingHistoryClearLogicImpl(BrowsinghistoryDao browsinghistoryDao) {
        this.browsinghistoryDao = browsinghistoryDao;
    }

    /**
     *
     * あしあとクリア<br/>
     * あしあと商品リストを削除する。<br/>
     *
     * @param shopSeq ショップSEQ
     * @param accessUid 端末識別ID
     * @return 削除件数
     */
    @Override
    public int execute(Integer shopSeq, String accessUid) {

        // (1) パラメータチェック
        // 端末識別情報が null でないかをチェック
        AssertionUtil.assertNotNull("accessUid", accessUid);

        // (3) あしあとクリア
        // あしあと情報Daoのあしあとクリア処理を実行する。
        int deleteCount = browsinghistoryDao.deleteBrowsinghistory(shopSeq, accessUid);

        // (4) 戻り値
        // 削除した件数を返す。
        return deleteCount;
    }
}
