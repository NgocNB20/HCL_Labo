/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.order.impl;

import jp.co.itechh.quad.core.dao.conveni.ConvenienceDao;
import jp.co.itechh.quad.core.entity.conveni.ConvenienceEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.order.ConvenienceLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * コンビニ名称取得ロジック
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
@Component
public class ConvenienceLogicImpl extends AbstractShopLogic implements ConvenienceLogic {

    /**
     * コンビニ名称Dao
     */
    private final ConvenienceDao convenienceDao;

    @Autowired
    public ConvenienceLogicImpl(ConvenienceDao convenienceDao) {
        this.convenienceDao = convenienceDao;
    }

    /**
     * コンビニ名称リスト取得処理
     * @return コンビニ名称エンティティリスト
     */
    @Override
    public List<ConvenienceEntity> getConvenienceList() {

        Integer shopSeq = 1001;
        // コンビニ名称リスト取得
        return convenienceDao.getConvenienceList(shopSeq, false);
    }

}
