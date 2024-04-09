package jp.co.itechh.quad.admin.dto.totaling.orderSales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * キャンセル・返品Dtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class CancelOrderDto {
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
}