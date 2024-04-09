/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.settlement;

import jp.co.itechh.quad.core.dto.shop.settlement.SettlementMethodDto;

/**
 * 決済方法設定チェックサービス
 *
 * 設定可能な決済方法であるかをチェックします。
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
public interface SettlementMethodConfigurationCheckService {

    /** 手数料未設定エラー */
    public static final String MSGCD_COMMISSION_NO_SET = "SST000601";

    /** 手数料未設定エラー */
    public static final String MSGCD_EQUALS_COMMISSION_NO_SET = "SST000602";

    /** 最大購入金額未設定エラー */
    public static final String MSGCD_MAX_PURCHASED_PRICE_NO_SET = "SST000603";

    /** 最大購入金額0円エラー */
    public static final String MSGCD_MAX_PURCHASED_PRICE_ZERO = "SST000604";

    /** 一律手数料エラー(円) */
    public static final String MSGCD_COMMISSION_ORVER_MAX_PURCHASED_PRICE = "SST000605";

    /** 一律手数料エラー(％) */
    public static final String MSGCD_COMMISSION_ORVER_MAX_PERCENTAGE_PRICE = "SST000606";

    /**
     * 実行メソッド
     *
     * @param settlementMethodDto 決済方法DTO
     */
    void execute(SettlementMethodDto settlementMethodDto);
}
