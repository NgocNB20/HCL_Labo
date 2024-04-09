/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import lombok.Data;

/**
 * 注文商品パラメータ
 */
@Data
public class OrderItemCountUseCaseParam {

    /** 注文商品連番 */
    private Integer orderItemSeq;

    /** 注文数量 */
    private Integer orderCount;
}
