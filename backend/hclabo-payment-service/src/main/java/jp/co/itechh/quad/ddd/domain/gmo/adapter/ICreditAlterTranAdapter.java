/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.gmo.adapter;

import com.gmo_pg.g_pay.client.output.AlterTranOutput;
import jp.co.itechh.quad.core.dto.multipayment.HmAlterTranInput;

/**
 * GMOクレジット決済変更 アダプター
 */
public interface ICreditAlterTranAdapter {

    /**
     * 通信中に例外発生
     */
    public static final String ALTERTRAN_COM_ERR_MSG_ID = "LMC000021";

    /**
     * GMOクレジット決済変更通信
     *
     * @param hmAlterTranInput
     * @return AlterTranOutput GMO取引変更結果
     */
    AlterTranOutput doAlterTran(HmAlterTranInput hmAlterTranInput);

}