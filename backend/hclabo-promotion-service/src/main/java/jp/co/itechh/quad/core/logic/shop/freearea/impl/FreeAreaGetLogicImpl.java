/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.freearea.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.freearea.FreeAreaDao;
import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.freearea.FreeAreaGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * フリーエリア情報取得ロジック
 *
 * @author natume
 * @version $Revision: 1.2 $
 *
 */
@Component
public class FreeAreaGetLogicImpl extends AbstractShopLogic implements FreeAreaGetLogic {

    /**
     * フリーエリアDao
     */
    private final FreeAreaDao freeAreaDao;

    @Autowired
    public FreeAreaGetLogicImpl(FreeAreaDao freeAreaDao) {
        this.freeAreaDao = freeAreaDao;
    }

    /**
     * フリーエリア情報取得処理
     *
     * @param shopSeq ショップSEQ
     * @param freeAreaKey フリーエリアキー
     * @return フリーエリアエンティティ
     */
    @Override
    public FreeAreaEntity execute(Integer shopSeq, String freeAreaKey) {

        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotEmpty("freeAreaKey", freeAreaKey);

        // フリーエリア情報取得
        return freeAreaDao.getFreeAreaByKey(shopSeq, freeAreaKey);
    }

    /**
     * フリーエリア情報取得処理
     *
     * @param shopSeq ショップSEQ
     * @param freeAreaSeq フリーエリアSEQ
     * @return フリーエリアエンティティ
     */
    @Override
    public FreeAreaEntity execute(Integer shopSeq, Integer freeAreaSeq) {
        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);
        ArgumentCheckUtil.assertGreaterThanZero("freeAreaSeq", freeAreaSeq);

        // フリーエリア情報取得
        return freeAreaDao.getFreeAreaByShopSeq(shopSeq, freeAreaSeq);
    }
}
