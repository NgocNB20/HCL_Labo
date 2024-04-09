/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.gmo.adapter;

import com.gmo_pg.g_pay.client.output.ChangeTranOutput;
import jp.co.itechh.quad.core.dto.multipayment.HmChangeTranInput;

/**
 * GMOクレジット金額変更 アダプター
 */
public interface ICreditChangeTranAdapter {

    /**
     * 通信中に例外発生
     */
    public static final String CHANGETRAN_COM_ERR_MSG_ID = "LMC000031";

    /**
     * GMOクレジット金額変更通信
     *
     * @param hmChangeTranInput
     * @return ChangeTranOutput GMO取引変更結果
     */
    ChangeTranOutput doChangeTran(HmChangeTranInput hmChangeTranInput);

}