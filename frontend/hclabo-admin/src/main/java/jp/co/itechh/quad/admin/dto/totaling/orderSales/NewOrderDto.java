package jp.co.itechh.quad.admin.dto.totaling.orderSales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 新規受注Dtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class NewOrderDto {

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
}
