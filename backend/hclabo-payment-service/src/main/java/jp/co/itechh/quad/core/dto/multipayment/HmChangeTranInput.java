/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.multipayment;

import com.gmo_pg.g_pay.client.input.ChangeTranInput;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * HitMall用に項目追加された XxxInput DTO
 *
 * @author tm27400
 */
@Component
@Scope("prototype")
public class HmChangeTranInput extends ChangeTranInput implements HmPaymentClientInput {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /** orderPaymentId */
    protected String orderPaymentId;

    /** Method */
    private String method;

    /** PayTimes */
    private Integer payTimes;

    @Override
    public void setOrderPaymentId(String orderPaymentId) {
        this.orderPaymentId = orderPaymentId;
    }

    @Override
    public String getOrderPaymentId() {
        return orderPaymentId;
    }

    /**
     * @return method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return payTimes
     */
    public Integer getPayTimes() {
        return payTimes;
    }

    /**
     * @param payTimes the payTimes to set
     */
    public void setPayTimes(Integer payTimes) {
        this.payTimes = payTimes;
    }
}