package jp.co.itechh.quad.admin.dto.totaling.orderSales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 受注・売上集計Dto
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class OrderSalesTotalDto implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 日付データ */
    private String date;

    /** 新規受注_件数 */
    private Integer newOrderCount;

    /** 新規受注_商品金額合計 */
    private Integer newOrderItemSalesPriceTotal;

    /** 新規受注_送料 */
    private Integer newOrderCarriage;

    /** 新規受注_手数料 */
    private Integer newOrderCommission;

    /** 新規受注_その他料金 */
    private Integer newOrderOtherPrice;

    /** 新規受注_消費税 */
    private Integer newOrderTax;

    /** 新規受注_クーポン割引 */
    private Integer newOrderCouponPaymentPrice;

    /** 新規受注_受注金額 */
    private Integer newOrderPrice;

    /** キャンセル_件数 */
    private Integer cancelOrderCount;

    /** キャンセル_商品金額合計 */
    private Integer cancelOrderItemSalesPriceTotal;

    /** キャンセル_送料 */
    private Integer cancelOrderCarriage;

    /** キャンセル_手数料 */
    private Integer cancelOrderCommission;

    /** キャンセル_その他料金 */
    private Integer cancelOrderPrice;

    /** キャンセル_消費税 */
    private Integer cancelOrderTax;

    /** キャンセル_クーポン割引 */
    private Integer cancelOrderCouponPaymentPrice;

    /** キャンセル_受注金額 */
    private Integer cancelOrderOtherPrice;

    /** 変更_商品金額合計 */
    private Integer updateOrderItemSalesPriceTotal;

    /** 変更_送料 */
    private Integer updateOrderCarriage;

    /** 変更_手数料 */
    private Integer updateOrderCommission;

    /** 変更_その他料金 */
    private Integer updateOrderOtherPrice;

    /** 変更_消費税 */
    private Integer updateOrderTax;

    /** 変更_クーポン割引 */
    private Integer updateOrderCouponPaymentPrice;

    /** 変更_受注金額 */
    private Integer updateOrderPrice;

    /** 小計_商品金額合計 */
    private Integer subTotalItemSalesPriceTotal;

    /** 小計_送料 */
    private Integer subTotalCarriage;

    /** 小計_手数料 */
    private Integer subTotalCommission;

    /** 小計_その他料金 */
    private Integer subTotalOtherPrice;

    /** 小計_消費税 */
    private Integer subTotalTax;

    /** 小計_クーポン割引 */
    private Integer subTotalCouponPaymentPrice;

    /** 小計_受注金額 */
    private Integer subTotalPrice;

}