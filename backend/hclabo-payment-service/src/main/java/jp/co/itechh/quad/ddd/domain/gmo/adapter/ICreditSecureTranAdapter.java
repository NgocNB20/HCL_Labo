/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.gmo.adapter;

import com.gmo_pg.g_pay.client.input.SecureTran2Input;
import com.gmo_pg.g_pay.client.output.SecureTran2Output;

/**
 * GMOクレジット認証後決済アダプター
 */
public interface ICreditSecureTranAdapter {

    /** 通信処理中エラー発生時 */
    String MSGCD_PAYMENT_COM_FAIL = "LMC001001";

    /**
     * GMOクレジット認証後決済実行通信
     *
     * @param secureTran2Input
     * @return SecureTran2Output
     */
    SecureTran2Output creditSecureTran(SecureTran2Input secureTran2Input);
}