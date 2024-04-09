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

    /**
     * 注文商品連番
     */
    private int orderItemSeq;

    /**
     * 商品ID（商品サービスの商品SEQ）
     */
    private String itemId;

    /**
     * 注文数量
     */
    private int itemCount;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 規格1値
     */
    private String unitValue1;

    /**
     * 規格2値
     */
    private String unitValue2;

    /**
     * 規格タイトル1
     */
    private String unitTitle1;

    /**
     * 規格タイトル2
     */
    private String unitTitle2;

    /**
     * JANコード
     */
    private String janCode;

    /**
     * ノベルティ商品フラグ
     */
    private String noveltyGoodsType;

    /**
     * 注文商品ID
     */
    private String orderItemId;

}
