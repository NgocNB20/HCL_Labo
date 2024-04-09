/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.multipayment.impl;

import jp.co.itechh.quad.core.dao.multipayment.MulPayShopDao;
import jp.co.itechh.quad.core.entity.multipayment.MulPayShopEntity;
import jp.co.itechh.quad.core.logic.multipayment.MulPayShopLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * マルチペイショップロジック実装クラス
 *
 * @author is40701
 */
@Component
public class MulPayShopLogicImpl implements MulPayShopLogic {

    /** マルチペイメントショップDao */
    private final MulPayShopDao mulPayShopDao;

    @Autowired
    public MulPayShopLogicImpl(MulPayShopDao mulPayShopDao) {
        this.mulPayShopDao = mulPayShopDao;
    }

    /**
     * マルペイショップID取得
     * @param shopSeq ショップSEQ
     * @return String マルペイショップID
     */
    @Override
    public String getMulPayShopId(Integer shopSeq) {
        MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopSeq(shopSeq);
        if (null == mulPayShopEntity) {
            return null;
        }
        return mulPayShopEntity.getShopId();
    }

}