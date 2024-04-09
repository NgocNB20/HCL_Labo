/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.gmo.adapter;

import com.gmo_pg.g_pay.client.input.SearchTradeMultiInput;
import com.gmo_pg.g_pay.client.output.SearchTradeMultiOutput;

/**
 * GMOマルチ決済取引検索アダプター
 */
public interface IMultiTradeSearchAdapter {

    /** 通信処理中エラー発生時 */
    public static final String MSGCD_PAYMENT_COM_FAIL = "LMC001001";

    /**
     * GMOマルチ決済取引検索
     *
     * @param searchTradeMultiInput
     * @return SearchTradeMultiOutput
     */
    SearchTradeMultiOutput doSearchTradeMulti(SearchTradeMultiInput searchTradeMultiInput);

}