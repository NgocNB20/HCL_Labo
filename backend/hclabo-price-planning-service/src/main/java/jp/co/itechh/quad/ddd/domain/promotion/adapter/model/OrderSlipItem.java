/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter.model;

import lombok.Data;

/**
 * 注文商品
 */
@Data
public class OrderSlipItem {

    /** 注文商品連番 */
    private int orderItemSeq;

    /** 商品ID（商品サービスの商品SEQ） */
    private String itemId;

    /** 注文数量 */
    private int itemCount;

}
