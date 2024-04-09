/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.multipayment;

import com.gmo_pg.g_pay.client.input.AlterTranInput;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * HitMall用に項目追加された XxxInput DTO
 *
 * @author tm27400
 */
@Component
@Scope("prototype")
public class HmAlterTranInput extends AlterTranInput implements HmPaymentClientInput {

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
        return this.orderPaymentId;
    }
}