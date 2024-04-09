package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;

/**
 * 請求伝票
 */
@Data
public class BillingSlip {

    /** リンク決済タイプ（0:即時払い、1：後日払い */
    private String linkPayType;

}
