package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import lombok.Data;

/**
 * 改訂用配送伝票
 */
@Data
public class ShippingSlipForRevision extends ShippingSlip {

    /** 改訂用配送伝票ID */
    private String shippingSlipRevisionId;

    /** 改訂用取引ID */
    private String transactionRevisionId;

}
