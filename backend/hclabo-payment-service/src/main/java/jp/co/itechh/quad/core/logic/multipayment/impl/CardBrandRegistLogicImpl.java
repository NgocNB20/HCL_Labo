/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.multipayment.impl;

import jp.co.itechh.quad.core.dao.multipayment.CardBrandDao;
import jp.co.itechh.quad.core.entity.multipayment.CardBrandEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.multipayment.CardBrandRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カードブランド情報登録Logicクラス
 *
 * @author ito
 *
 */
@Component
public class CardBrandRegistLogicImpl extends AbstractShopLogic implements CardBrandRegistLogic {

    /** カードブランドDao */
    private final CardBrandDao cardBrandDao;

    @Autowired
    public CardBrandRegistLogicImpl(CardBrandDao cardBrandDao) {
        this.cardBrandDao = cardBrandDao;
    }

    /**
     * カードブランド情報を登録
     *
     * @param cardBrandEntity カードブランド情報エンティティ
     * @return 登録件数
     */
    @Override
    public int execute(CardBrandEntity cardBrandEntity) {
        return cardBrandDao.insert(cardBrandEntity);
    }

}
