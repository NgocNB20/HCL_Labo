package jp.co.itechh.quad.admin.dto.totaling.orderSales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 受注・売上集計Dtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class OrderSalesDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 決済方法ID */
    private String paymentMethodId;

    /** 決済方法名 */
    private String paymentMethodName;

    /** 決済手段 */
    private Integer payType;

    /** ソート表示 */
    private Integer sortDisplay;

    /** 新規受注Dto */
    private NewOrderDto newOrderDto;

    /** 変更Dto */
    private UpdateOrderDto updateOrderDto;

    /** キャンセル・返品Dto */
    private CancelOrderDto cancelOrderDto;

    /** 小計Dto */
    private SubTotalDto subTotalDto;

}