/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.settlement.SettlementMethodDao;
import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodEntityListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 決済方法リスト取得ロジック実装クラス
 *
 * @author nakamura
 * @version $Revision: 1.4 $
 *
 */
@Component
public class SettlementMethodEntityListGetLogicImpl extends AbstractShopLogic
                implements SettlementMethodEntityListGetLogic {

    /** 決済方法Dao */
    private final SettlementMethodDao settlementMethodDao;

    @Autowired
    public SettlementMethodEntityListGetLogicImpl(SettlementMethodDao settlementMethodDao) {
        this.settlementMethodDao = settlementMethodDao;
    }

    /**
     * 実行メソッド
     *
     * @param shopSeq   ショップSeq
     * @return 決済方法エンティティ全件分のリスト（ソート：表示昇順）
     */
    @Override
    public List<SettlementMethodEntity> execute(Integer shopSeq) {

        // Daoメソッド実行
        return settlementMethodDao.getSettlementMethodList(shopSeq);
    }

    /**
     * パラメータのチェックを行います
     *
     * @param shopSeq   ショップSeq
     */
    protected void checkParameter(Integer shopSeq) {

        // ショップSEQ
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

    }

    /**
     * リンク決済一覧取得
     *
     * @return リンク決済個別決済手段
     */
    public List<SettlementMethodLinkEntity> getPaymentMethodLink() {
        return settlementMethodDao.getPaymentMethodLink();
    }

}