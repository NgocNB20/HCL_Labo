/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 注文票
 */
@Data
public class OrderSlip {

    /**
     * 注文票ID
     */
    private String orderSlipId;

    /**
     * 注文ステータス
     */
    private String orderStatus;

    /**
     * 注文合計数量
     */
    private int totalItemCount;

    /**
     * 注文商品リスト
     */
    private List<OrderSlipItem> itemList;

    /**
     * 顧客ID
     */
    private String customerId;

    /**
     * 取引ID
     */
    private String transactionId;

    /**
     * 登録日時
     */
    private Date registDate;

    /**
     * ユーザーエージェント
     */
    private String userAgent;

}
