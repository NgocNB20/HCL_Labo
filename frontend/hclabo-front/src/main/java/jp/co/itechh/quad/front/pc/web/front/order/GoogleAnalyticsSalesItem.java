package jp.co.itechh.quad.front.pc.web.front.order;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Google Analytics ModelItem
 * eコマース用データ送信
 */
@Data
public class GoogleAnalyticsSalesItem {

    /** 商品コード */
    private String goodsCode;

    /**
     * 商品名
     */
    private String goodsGroupName;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /**
     * 商品単価(税抜)
     */
    private BigDecimal goodsPrice;

    /** 商品数量 */
    private BigDecimal orderGoodsCount;

}
