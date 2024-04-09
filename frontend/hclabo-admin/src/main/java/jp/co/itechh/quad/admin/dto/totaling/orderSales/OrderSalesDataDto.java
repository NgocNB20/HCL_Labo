package jp.co.itechh.quad.admin.dto.totaling.orderSales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 受注・売上集計データDtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class OrderSalesDataDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 日付データ */
    private String date;

    /** 受注・売上集計データリスト */
    private List<OrderSalesDto> orderSalesDtoList;

}
