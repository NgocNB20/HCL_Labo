/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.gmo.adapter;

import com.gmo_pg.g_pay.client.input.SearchTradeInput;
import com.gmo_pg.g_pay.client.output.SearchTradeOutput;

/**
 * GMOクレジット取引状態参照 アダプター
 */
public interface ICreditSearchTradeAdapter {

    /** 通信処理中エラー発生時 */
    public static final String MSGCD_PAYMENT_COM_FAIL = "LMC000061";

    /**
     * GMOクレジット決済変更通信
     *
     * @param searchTradeInput GMO取引状態参照通信パラメータ
     * @return SearchTradeOutput 取引状態参照出力
     */
    SearchTradeOutput doSearchTrade(SearchTradeInput searchTradeInput);

}