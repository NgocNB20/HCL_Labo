package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import lombok.Data;

/**
 * 配送方法
 */
@Data
public class ShippingMethod {

    /**
     * 配送方法
     */
    private String deliveryMethodName;

    /**
     * 配送方法ID
     */
    private Integer shippingMethodId;

}
