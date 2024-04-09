/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 改訂用注文票<br/>
 */
@Data
@Component
@Scope("prototype")
public class OrderSlipForRevision {

    /** 注文商品リスト */
    private List<OrderSlipItem> orderItemList;

    /** 改訂用注文商品リスト */
    private List<OrderSlipItem> revisionOrderItemList;

}
