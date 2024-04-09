/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement;

import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;

/**
 * 決済方法取得ロジック
 *
 * @author ueshima
 * @version $Revision: 1.3 $
 */
public interface SettlementMethodGetLogic {

    // LST0001;

    /**
     * ロジック実行
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 決済方法エンティティ
     */
    SettlementMethodEntity execute(Integer settlementMethodSeq);

    /**
     * 実行メソッド
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @param shopSeq ショップSEQ
     * @return 決済方法エンティティ
     */
    SettlementMethodEntity execute(Integer settlementMethodSeq, Integer shopSeq);

    /**
     * エンティティ取得
     *
     * @param settlementMethodType 決済方法種別
     * @param openStatus status of settlement
     * @return 決済方法エンティティ
     */
    SettlementMethodEntity execute(HTypeSettlementMethodType settlementMethodType, HTypeOpenStatus openStatus);

}