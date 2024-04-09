package jp.co.itechh.quad.admin.dto.totaling.orderSales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 小計Dtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class SubTotalDto {

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
