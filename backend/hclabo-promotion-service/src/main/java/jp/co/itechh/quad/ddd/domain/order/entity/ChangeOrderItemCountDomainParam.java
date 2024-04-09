/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.entity;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 注文商品数量変更ユースケースパラメータ
 */
@Data
@Component
@Scope("prototype")
public class ChangeOrderItemCountDomainParam {

    /** 商品ID（商品サービスの商品SEQ） */
    private String itemId;

    /** 注文数量 */
    private Integer orderCount;
}
