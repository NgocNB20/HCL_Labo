/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter;

import jp.co.itechh.quad.ddd.domain.payment.adapter.model.PaymentMethod;

/**
 * 決済方法アダプター
 */
public interface IPaymentMethodAdapter {

    /**
     * 決済方法取得
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 決済方法
     */
    PaymentMethod getPaymentMethod(Integer settlementMethodSeq);

}
