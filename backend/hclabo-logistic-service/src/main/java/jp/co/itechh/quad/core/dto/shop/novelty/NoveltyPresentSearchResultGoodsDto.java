/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.novelty;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * ノベルティプレゼント検索結果商品用Dto
 *
 * @author yt23807
 */
@Data
@Entity
@Component
@Scope("prototype")
public class NoveltyPresentSearchResultGoodsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ノベルティ条件SEQ */
    private Integer noveltyPresentConditionSeq;

    /** 優先順 */
    private Integer priorityOrder;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** 商品名 */
    private String noveltyGoodsName;

    /** 規格値1 */
    private String unitValue1;

    /** 規格値2 */
    private String unitValue2;

    /** 実在庫数 */
    private Integer realstock;

    /** 安全在庫数 */
    private Integer safetystock;

    /** 受注確保在庫数 */
    private Integer orderreservestock;

    /**
     * 販売可能在庫数取得
     * @return 販売可能在庫数
     */
    public Integer getSalesPossibleStock() {
        Integer salesPossibleStock = 0;
        if (realstock != null) {
            salesPossibleStock = realstock;
        }
        if (safetystock != null) {
            salesPossibleStock -= safetystock;
        }
        if (orderreservestock != null) {
            salesPossibleStock -= orderreservestock;
        }
        // 実在庫数ー安全在庫数ー受注確保在庫数
        return salesPossibleStock;
    }
}
