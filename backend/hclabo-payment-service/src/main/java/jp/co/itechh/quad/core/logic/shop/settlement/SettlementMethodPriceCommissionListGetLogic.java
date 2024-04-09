/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement;

import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodPriceCommissionEntity;

import java.util.List;

/**
 * 決済方法金額別手数料リスト取得
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
public interface SettlementMethodPriceCommissionListGetLogic {

    /**
     * 実行メソッド
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 決済方法金額別手数料リスト
     */
    List<SettlementMethodPriceCommissionEntity> execeute(Integer settlementMethodSeq);
}
