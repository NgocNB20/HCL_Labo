/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IPaymentMethodAdapter;
import jp.co.itechh.quad.method.presentation.api.PaymentMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.CommissionGetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 決済方法 アダプター 実装クラス
 *
 * @author kimura
 */
@Component
public class PaymentMethodAdapterImpl implements IPaymentMethodAdapter {

    /** 支払方法API */
    private final PaymentMethodApi paymentMethodApi;

    /**
     * コンストラクタ
     */
    @Autowired
    public PaymentMethodAdapterImpl(PaymentMethodApi paymentMethodApi, HeaderParamsUtility headerParamsUtil) {
        this.paymentMethodApi = paymentMethodApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.paymentMethodApi.getApiClient());
    }

    @Override
    public Integer getCommission(String paymentMethodId,
                                 Integer priceForLargeAmountDiscount,
                                 Integer priceForPriceCommission) {

        CommissionGetRequest request = new CommissionGetRequest();
        request.setPriceForLargeAmountDiscount(priceForLargeAmountDiscount);
        request.setPriceForPriceCommission(priceForPriceCommission);

        return paymentMethodApi.getCommission(paymentMethodId, request);
    }

}
