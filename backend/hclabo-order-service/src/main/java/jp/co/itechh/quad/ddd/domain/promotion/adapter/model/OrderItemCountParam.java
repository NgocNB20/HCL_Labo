/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter.model;

import lombok.Data;

/**
 * 注文商品パラメータ
 */
@Data
public class OrderItemCountParam {

    /** 注文商品連番 */
    private Integer orderItemNo;

    /** 注文数量 */
    private Integer orderCount;
}
