/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import com.gmo_pg.g_pay.client.input.SecureTran2Input;
import com.gmo_pg.g_pay.client.output.SecureTran2Output;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditSecureTranAdapter;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 途中決済請求委託ユースケース
 */
@Service
public class EntrustInterimPaymentAgencyUseCase {

    /** GMOクレジット認証後決済アダプター */
    private final ICreditSecureTranAdapter creditSecureTranAdapter;

    /** マルチペイメントプロキシサービス */
    private final MulPayProxyService mulPayProxyService;

    /** コンストラクタ */
    @Autowired
    public EntrustInterimPaymentAgencyUseCase(ICreditSecureTranAdapter creditSecureTranAdapter,
                                              MulPayProxyService mulPayProxyService) {
        this.creditSecureTranAdapter = creditSecureTranAdapter;
        this.mulPayProxyService = mulPayProxyService;
    }

    /**
     * 途中決済を決済代行に請求委託する<br/>
     * 3Dセキュアをキャンセルした場合も呼ばれる
     *
     * @param accessId 決済代行の取引ID
     * @reutrn SecureTran2Output
     */
    public SecureTran2Output entrustInterimPaymentAgency(String accessId) {

        MulPayBillEntity mulPayBillEntity = mulPayProxyService.getMulPayBillbyAccessId(accessId);

        // 本メソッドはGMO直接呼出しを行うこと（ドメインサービス呼び出し不可）

        // GMOクレジット認証後決済用パラメータ
        SecureTran2Input secureTran2Input = new SecureTran2Input();
        secureTran2Input.setAccessId(accessId);
        secureTran2Input.setAccessPass(mulPayBillEntity != null ? mulPayBillEntity.getAccessPass() : null);

        // GMOクレジット認証後決済実行通信
        return creditSecureTranAdapter.creditSecureTran(secureTran2Input);

    }

}