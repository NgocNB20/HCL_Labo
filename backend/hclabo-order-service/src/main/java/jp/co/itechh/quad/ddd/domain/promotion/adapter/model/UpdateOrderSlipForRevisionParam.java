/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter.model;

import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import lombok.Data;

import java.util.List;

/**
 * 改訂用注文票更新パラメータ
 */
@Data
public class UpdateOrderSlipForRevisionParam {

    /** 改訂用取引ID */
    private TransactionRevisionId transactionRevisionId;

    /** 注文商品リスト */
    private List<OrderItemCountParam> orderItemCountParamList;

}
