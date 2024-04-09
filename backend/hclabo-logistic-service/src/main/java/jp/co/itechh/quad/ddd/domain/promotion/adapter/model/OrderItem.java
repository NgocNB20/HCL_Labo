/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter.model;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 注文商品
 */
@Data
@Component
@Scope("prototype")
public class OrderItem {

    /** 注文商品連番 */
    private int orderItemSeq;

    /** 商品ID（商品サービスの商品SEQ） */
    private String itemId;

    /** 注文数量 */
    private int orderCount;

    /** 商品名 */
    private String itemName;

    /** 規格タイトル1 */
    private String unitTitle1;

    /** 規格値1 */
    private String unitValue1;

    /** 規格タイトル2 */
    private String unitTitle2;

    /** 規格値2 */
    private String unitValue2;

    /** JANコード */
    private String janCode;

    /** ノベルティ商品フラグ */
    private HTypeNoveltyGoodsType noveltyGoodsType;

}
