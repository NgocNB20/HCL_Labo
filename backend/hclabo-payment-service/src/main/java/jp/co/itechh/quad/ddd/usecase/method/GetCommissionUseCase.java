/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.method;

import jp.co.itechh.quad.ddd.domain.method.proxy.PaymentMethodProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 手数料取得 ユースケース
 */
@Service
public class GetCommissionUseCase {

    /** 決済方法プロキシサービス */
    private final PaymentMethodProxyService paymentMethodProxyService;

    /** コンストラクタ */
    @Autowired
    public GetCommissionUseCase(PaymentMethodProxyService paymentMethodProxyService) {
        this.paymentMethodProxyService = paymentMethodProxyService;
    }

    /**
     * 手数料を取得する
     *
     * @param paymentMethodId             決済方法ID（決済方法SEQ）
     * @param priceForLargeAmountDiscount 計算対象金額（一律手数料高額割引）
     * @param priceForPriceCommission     計算対象金額（金額別手数料）
     * @return 手数料 ※算出不可能な場合、null
     */
    public Integer getCommission(String paymentMethodId,
                                 Integer priceForLargeAmountDiscount,
                                 Integer priceForPriceCommission) {
        return paymentMethodProxyService.getCommission(
                        paymentMethodId, priceForLargeAmountDiscount, priceForPriceCommission);
    }

}
