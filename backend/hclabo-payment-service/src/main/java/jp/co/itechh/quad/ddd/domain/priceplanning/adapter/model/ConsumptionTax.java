/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 消費税
 */
@Data
public class ConsumptionTax {

    /** 消費税額 */
    private BigDecimal consumptionTaxAmount;

    /** 消費税率 */
    private BigDecimal consumptionTaxRate;

}
