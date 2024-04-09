/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.multipayment.communicate;

import com.gmo_pg.g_pay.client.output.SearchMemberOutput;

/**
 * 会員参照通信Logic
 *
 * @author s_tsuru
 *
 */
public interface SearchMemberLogic {

    /** 通信処理中エラー発生時：LMC000051 */
    public static final String MSGCD_PAYMENT_COM_FAIL = "LMC000051";

    /**
     * 実行メソッド
     *
     * @param paymentMemberId 決済代行会員ID
     * @return SearchMemberOutput 会員参照出力パラメータ
     */
    SearchMemberOutput execute(String paymentMemberId);
}
