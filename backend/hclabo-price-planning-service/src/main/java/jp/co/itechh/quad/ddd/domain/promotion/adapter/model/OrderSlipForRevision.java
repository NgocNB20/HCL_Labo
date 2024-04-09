/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter.model;

import lombok.Data;

import java.util.List;

/**
 * 改訂用注文票
 */
@Data
public class OrderSlipForRevision extends OrderSlip {

    /** 改訂用注文票ID */
    private String orderSlipRevisionId;

    /** 改訂用取引ID */
    private String transactionRevisionId;

    /** 改訂用注文商品リスト */
    private List<OrderSlipItem> orderItemRevisionList;

    /** 顧客ID */
    private String customerId;

}
