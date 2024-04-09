package jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model;

import lombok.Data;

/**
 * 商品金額
 */
@Data
public class ItemPrice {

    private String itemId;

    private Integer itemUnitPrice;

    private Integer itemPriceSubTotal;

}
