package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 金額情報
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportsPrice {
    /**
     * 商品金額合計
     */
    private Integer itemSalesPriceTotal;

    /**
     * 送料
     */
    private Integer carriage;

    /**
     * 手数料
     */
    private Integer commission;

    /**
     * その他料金
     */
    private Integer otherPrice;

    /**
     * 消費税
     */
    private Integer tax;

    /**
     * クーポン割引
     */
    private Integer couponPaymentPrice;

    /**
     * 受注金額
     */
    private Integer orderPrice;

}
