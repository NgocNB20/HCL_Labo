/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.settlement.SettlementMethodPriceCommissionDao;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodPriceCommissionEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodPriceCommissionDeleteLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 決済方法金額別手数料削除
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
@Component
public class SettlementMethodPriceCommissionDeleteLogicImpl extends AbstractShopLogic
                implements SettlementMethodPriceCommissionDeleteLogic {

    /**
     * 決済方法金額別手数料Dao
     */
    private final SettlementMethodPriceCommissionDao settlementMethodPriceCommissionDao;

    @Autowired
    public SettlementMethodPriceCommissionDeleteLogicImpl(SettlementMethodPriceCommissionDao settlementMethodPriceCommissionDao) {
        this.settlementMethodPriceCommissionDao = settlementMethodPriceCommissionDao;
    }

    /**
     * 実行メソッド
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 処理件数
     */
    @Override
    public int execute(Integer settlementMethodSeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("settlementMethodSeq", settlementMethodSeq);

        return settlementMethodPriceCommissionDao.deleteListBySettlementMethodSeq(settlementMethodSeq);
    }

    /**
     * 実行メソッド
     *
     * @param settlementMethodPriceCommissionEntity 決済方法金額別手数料エンティティ
     * @return 処理件数
     */
    @Override
    public int execute(SettlementMethodPriceCommissionEntity settlementMethodPriceCommissionEntity) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("settlementMethodPriceCommissionEntity", settlementMethodPriceCommissionEntity);

        return settlementMethodPriceCommissionDao.delete(settlementMethodPriceCommissionEntity);
    }

}