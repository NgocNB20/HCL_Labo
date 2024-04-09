/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.settlement;

import jp.co.itechh.quad.core.dto.shop.settlement.SettlementMethodDto;

/**
 * 決済方法更新
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
public interface SettlementMethodUpdateService {

    /**
     * 実行メソッド
     *
     * @param settlementMethodDto 決済方法DTO
     * @return 処理件数
     */
    int execute(SettlementMethodDto settlementMethodDto);
}
