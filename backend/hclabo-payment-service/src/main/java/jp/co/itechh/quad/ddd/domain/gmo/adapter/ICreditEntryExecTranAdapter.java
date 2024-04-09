/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.gmo.adapter;

import com.gmo_pg.g_pay.client.output.EntryTranOutput;
import com.gmo_pg.g_pay.client.output.ExecTranOutput;
import jp.co.itechh.quad.core.dto.multipayment.HmEntryTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmExecTranInput;

/**
 * GMOクレジット取引登録決済アダプター
 */
public interface ICreditEntryExecTranAdapter {

    /** 通信処理中エラー発生時 */
    public static final String MSGCD_PAYMENT_COM_FAIL = "LMC000011";

    /**
     * GMOクレジット取引登録通信
     *
     * @param hmEntryTranInput
     * @return EntryTranOutput
     */
    EntryTranOutput creditEntryTran(HmEntryTranInput hmEntryTranInput);

    /**
     * GMOクレジット決済実行通信
     *
     * @param hmExecTranInput
     * @return ExecTranOutput
     */
    ExecTranOutput creditEntryExecTran(HmExecTranInput hmExecTranInput);
}