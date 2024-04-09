package jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 検索キーワード集計モデル
 */

@Getter
@Setter
@NoArgsConstructor
public class OrderSales {

    /**
     * 決済方法ID
     */
    private Integer paymentMethodId;

    /**
     * 決済方法
     */
    private String paymentMethodName;

    /**
     * 決済手段
     */
    private Integer payType;

    /**
     * 新規受注－件数
     */
    private Integer newOrderCount;

    /**
     * 新規受注－商品金額合計
     */
    private Integer newOrderItemSalesPriceTotal;

    /**
     * 新規受注－送料
     */
    private Integer newOrderCarriage;

    /**
     * 新規受注－手数料
     */
    private Integer newOrderCommission;

    /**
     * 新規受注－その他料金
     */
    private Integer newOrderOtherPrice;

    /**
     * 新規受注－消費税
     */
    private Integer newOrderTax;

    /**
     * 新規受注－クーポン割引
     */
    private Integer newOrderCouponPaymentPrice;

    /**
     * 新規受注－受注金額
     */
    private Integer newOrderPrice;

    /**
     * キャンセル－件数
     */
    private Integer cancelOrderCount;

    /**
     * キャンセル－商品金額合計
     */
    private Integer cancelOrderItemSalesPriceTotal;

    /**
     * キャンセル－送料
     */
    private Integer cancelOrderCarriage;

    /**
     * キャンセル－手数料
     */
    private Integer cancelOrderCommission;

    /**
     * キャンセル－その他料金
     */
    private Integer cancelOrderOtherPrice;

    /**
     * キャンセル－消費税
     */
    private Integer cancelOrderTax;

    /**
     * キャンセル－クーポン割引
     */
    private Integer cancelOrderCouponPaymentPrice;

    /**
     * キャンセル－受注金額
     */
    private Integer cancelOrderPrice;

    /**
     * 変更－商品金額合計
     */
    private Integer updateOrderItemSalesPriceTotal;

    /**
     * 変更－送料
     */
    private Integer updateOrderCarriage;

    /**
     * 変更－手数料
     */
    private Integer updateOrderCommission;

    /**
     * 変更－その他料金
     */
    private Integer updateOrderOtherPrice;

    /**
     * 変更－消費税
     */
    private Integer updateOrderTax;

    /**
     * 変更－クーポン割引
     */
    private Integer updateOrderCouponPaymentPrice;

    /**
     * 変更－受注金額
     */
    private Integer updateOrderPrice;

    /**
     * 小計－商品金額合計
     */
    private Integer subTotalItemSalesPriceTotal;

    /**
     * 小計－送料
     */
    private Integer subTotalCarriage;

    /**
     * 小計－手数料
     */
    private Integer subTotalCommission;

    /**
     * 小計－その他料金
     */
    private Integer subTotalOtherPrice;

    /**
     * 小計－消費税
     */
    private Integer subTotalTax;

    /**
     * 小計－クーポン割引
     */
    private Integer subTotalCouponPaymentPrice;

    /**
     * 小計－受注金額
     */
    private Integer subTotalOrderPrice;
}
