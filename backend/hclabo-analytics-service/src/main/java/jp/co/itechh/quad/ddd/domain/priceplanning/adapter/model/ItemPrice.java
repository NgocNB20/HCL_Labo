package jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品金額
 */
@Data
public class ItemPrice {

    /**
     * 販売商品連番
     */
    private Integer salesItemSeq;

    /**
     * 商品ID（商品SEQ）
     */
    private String itemId;

    /**
     * 商品単価
     */
    private Integer itemUnitPrice;

    /**
     * 商品購入価格小計
     */
    private Integer itemPriceSubTotal;

    /**
     * 商品数量
     */
    private Integer itemCount;

    /**
     * 商品税率
     */
    private BigDecimal itemTaxRate;
}
