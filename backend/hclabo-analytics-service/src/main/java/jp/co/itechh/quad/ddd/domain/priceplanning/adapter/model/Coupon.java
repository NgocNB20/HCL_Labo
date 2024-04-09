package jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model;

import lombok.Data;

/**
 * クーポン
 */
@Data
public class Coupon {

    /**
     * クーポンSEQ
     */
    private Integer couponSeq;

    /**
     * クーポンID
     */
    private String couponId;

    /**
     * クーポン名
     */
    private String couponName;

}
