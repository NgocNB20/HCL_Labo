package jp.co.itechh.quad.ddd.usecase.query.reports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商品販売
 */
@Getter
@Setter
@NoArgsConstructor
public class GoodsSale {

    /**
     * 商品管理番号
     */
    private String goodsGroupCode;

    /**
     * 商品番号
     */
    private String goodsCode;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 規格1
     */
    private String unitValue1;

    /**
     * 規格2
     */
    private String unitValue2;

    /**
     * JANコート
     */
    private String janCode;

    /**
     * 商品単価
     */
    private Integer unitPrice;
}
