/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement;

import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodPriceCommissionEntity;

/**
 * 決済方法金額別手数料削除
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
public interface SettlementMethodPriceCommissionDeleteLogic {

    /**
     * 実行メソッド
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 処理件数
     */
    int execute(Integer settlementMethodSeq);

    /**
     * 実行メソッド
     *
     * @param settlementMethodPriceCommissionEntity 決済方法金額別手数料エンティティ
     * @return 処理件数
     */
    int execute(SettlementMethodPriceCommissionEntity settlementMethodPriceCommissionEntity);
}
