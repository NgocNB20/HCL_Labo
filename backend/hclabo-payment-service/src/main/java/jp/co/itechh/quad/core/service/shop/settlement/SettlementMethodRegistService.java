/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.service.shop.settlement;

import jp.co.itechh.quad.core.dto.shop.settlement.SettlementMethodDto;

/**
 * 決済方法登録
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
public interface SettlementMethodRegistService {

    /** SST000301 */
    public static final String MSGCD_COMMISSION_INSERT_ERROR = "SST000301";

    /** SST000302 */
    public static final String MSGCD_DELIVERY_DELETE_ERROR = "SST000302";

    /** SST000303 */
    public static final String MSGCD_DELIVERY_NULL_ERROR = "SST000303";

    /**
     * 実行メソッド
     *
     * @param settlementMethodDto 決済方法DTO
     * @return 処理件数
     */
    int execute(SettlementMethodDto settlementMethodDto);
}