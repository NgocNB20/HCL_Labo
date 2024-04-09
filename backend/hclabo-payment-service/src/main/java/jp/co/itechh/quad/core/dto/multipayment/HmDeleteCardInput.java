/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.dto.multipayment;

import com.gmo_pg.g_pay.client.input.DeleteCardInput;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * HitMall用に項目追加された XxxInput DTO
 */
@Component
@Scope("prototype")
public class HmDeleteCardInput extends DeleteCardInput implements HmPaymentClientInput {
    /** シリアル */
    private static final long serialVersionUID = 1L;

    /** orderPaymentId */
    protected String orderPaymentId;

    @Override
    public void setOrderPaymentId(String orderPaymentId) {
        this.orderPaymentId = orderPaymentId;
    }

    @Override
    public String getOrderPaymentId() {
        return orderPaymentId;
    }
}