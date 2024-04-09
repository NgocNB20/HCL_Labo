package jp.co.itechh.quad.admin.dto.totaling.orderSales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 変更Dtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class UpdateOrderDto {
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

}
