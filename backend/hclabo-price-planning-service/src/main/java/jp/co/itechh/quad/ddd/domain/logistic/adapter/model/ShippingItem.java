/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 配送商品
 */
@Data
@Component
@Scope("prototype")
public class ShippingItem {

    /** 商品ID（商品サービスの商品SEQ） */
    private String itemId;

    /** 商品名 */
    private String itemName;

    /** 規格1 */
    private String unit1;

    /** 規格2 */
    private String unit2;

    /** 配送数量 */
    private int shippingCount;

}
