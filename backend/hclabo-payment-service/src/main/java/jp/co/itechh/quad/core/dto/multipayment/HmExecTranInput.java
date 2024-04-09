/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.multipayment;

import com.gmo_pg.g_pay.client.input.ExecTranInput;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * HitMall用に項目追加された XxxInput DTO
 *
 * @author tm27400
 */
@Component
@Scope("prototype")
public class HmExecTranInput extends ExecTranInput implements HmPaymentClientInput {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /** orderPaymentId */
    protected String orderPaymentId;

    /** JobCd */
    private String jobCd;

    /** Amount */
    private Integer amount;

    @Override
    public void setOrderPaymentId(String orderPaymentId) {
        this.orderPaymentId = orderPaymentId;
    }

    @Override
    public String getOrderPaymentId() {
        return this.orderPaymentId;
    }

    /**
     * @return jobCd
     */
    public String getJobCd() {
        return jobCd;
    }

    /**
     * @param jobCd the jobCd to set
     */
    public void setJobCd(String jobCd) {
        this.jobCd = jobCd;
    }

    /**
     * @return amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
