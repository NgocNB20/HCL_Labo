/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * クーポン
 */
@Data
public class Coupon {

    /** 割引額 */
    private BigDecimal couponDiscount;

    /** クーポンコード */
    private String couponCode;

    /** クーポン名 */
    private String couponName;

}
