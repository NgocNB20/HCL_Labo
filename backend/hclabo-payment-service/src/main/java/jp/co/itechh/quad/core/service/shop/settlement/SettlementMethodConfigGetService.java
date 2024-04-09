/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.settlement;

import jp.co.itechh.quad.core.dto.shop.settlement.SettlementMethodDto;

/**
 * 決済方法詳細設定取得
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
public interface SettlementMethodConfigGetService {

    /**
     * 実行メソッド
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 決済方法DTO
     */
    SettlementMethodDto execute(Integer settlementMethodSeq);
}
