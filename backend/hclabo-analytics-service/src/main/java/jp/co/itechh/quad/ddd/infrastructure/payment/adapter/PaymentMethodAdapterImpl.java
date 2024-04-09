/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IPaymentMethodAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.PaymentMethod;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 請求アダプター実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class PaymentMethodAdapterImpl implements IPaymentMethodAdapter {

    /**
     * 決済方法API
     */
    private final SettlementMethodApi settlementMethodApi;

    /**
     * 決済方法アダプターHelperクラス
     */
    private final PaymentMethodAdapterHelper paymentMethodAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param paymentMethodAdapterHelper 決済方法アダプターHelperクラス
     * @param settlementMethodApi        決済方法API
     * @param headerParamsUtil           ヘッダパラメーターユーティル
     */
    @Autowired
    public PaymentMethodAdapterImpl(SettlementMethodApi settlementMethodApi,
                                    PaymentMethodAdapterHelper paymentMethodAdapterHelper,
                                    HeaderParamsUtility headerParamsUtil) {
        this.settlementMethodApi = settlementMethodApi;
        this.paymentMethodAdapterHelper = paymentMethodAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.settlementMethodApi.getApiClient());
    }

    /**
     * 決済方法取得
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 決済方法
     */
    @Override
    public PaymentMethod getPaymentMethod(Integer settlementMethodSeq) {

        PaymentMethodResponse response = settlementMethodApi.getBySettlementMethodSeq(settlementMethodSeq);

        return paymentMethodAdapterHelper.toPaymentMethod(response);
    }

}