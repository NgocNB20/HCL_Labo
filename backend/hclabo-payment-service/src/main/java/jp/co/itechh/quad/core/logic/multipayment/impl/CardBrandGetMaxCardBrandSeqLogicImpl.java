/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.multipayment.impl;

import jp.co.itechh.quad.core.dao.multipayment.CardBrandDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.multipayment.CardBrandGetMaxCardBrandSeqLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MAXカードブランドSEQ取得Logicクラス
 *
 * @author ito
 *
 */
@Component
public class CardBrandGetMaxCardBrandSeqLogicImpl extends AbstractShopLogic
                implements CardBrandGetMaxCardBrandSeqLogic {

    /** カードブランドDao */
    private final CardBrandDao cardBrandDao;

    @Autowired
    public CardBrandGetMaxCardBrandSeqLogicImpl(CardBrandDao cardBrandDao) {
        this.cardBrandDao = cardBrandDao;
    }

    /**
     * MAXカードブランドSEQを取得
     *
     * @return MAXカードブランドSEQ
     */
    @Override
    public int execute() {
        return cardBrandDao.getMaxCardBrandSeq();
    }

}