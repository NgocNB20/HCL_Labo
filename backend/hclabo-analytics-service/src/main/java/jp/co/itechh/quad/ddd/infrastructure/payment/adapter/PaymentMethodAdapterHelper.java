/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.ddd.domain.payment.adapter.model.PaymentMethod;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import org.springframework.stereotype.Component;

/**
 * 決済方法アダプターHelperクラス
 */
@Component
public class PaymentMethodAdapterHelper {

    /**
     * 決済方法に変換
     *
     * @param response 決済方法レスポンス
     * @return 決済方法
     */
    public PaymentMethod toPaymentMethod(PaymentMethodResponse response) {

        if (response == null) {
            return null;
        }

        PaymentMethod paymentMethod = new PaymentMethod();

        paymentMethod.setPaymentMethodId(response.getSettlementMethodSeq());
        paymentMethod.setBillType(response.getBillType());
        paymentMethod.setSettlementMethodName(response.getSettlementMethodName());

        return paymentMethod;
    }

}
