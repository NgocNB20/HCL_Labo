/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 注文票<br/>
 */
@Data
@Component
@Scope("prototype")
public class OrderSlip {

    /** 注文票ID */
    private String orderSlipId;

    /** 注文ステータス */
    private String orderStatus;

    /** 注文商品リスト */
    private List<OrderItem> orderItemList;

    /** 改訂用注文商品リスト */
    private List<OrderItem> revisionOrderItemList;

    /** 顧客ID */
    private String customerId;

    /** 取引ID */
    private String transactionId;

    /** 登録日時 */
    private Date registDate;

}
