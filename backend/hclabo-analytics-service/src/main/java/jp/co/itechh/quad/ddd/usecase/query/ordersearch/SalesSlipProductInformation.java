package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 受注商品
 */
@Getter
@Setter
@NoArgsConstructor
public class SalesSlipProductInformation {

    /**
     * 価格
     */
    private Integer goodsPrice;

    /**
     * 数量
     */
    private Integer goodsCount;

    /**
     * 小計
     */
    private Integer summaryPrice;

    /**
     * 商品金額合計
     */
    private Integer goodsPriceTotal;

    /**
     * 商品税率
     */
    private BigDecimal itemTaxRate;

}
