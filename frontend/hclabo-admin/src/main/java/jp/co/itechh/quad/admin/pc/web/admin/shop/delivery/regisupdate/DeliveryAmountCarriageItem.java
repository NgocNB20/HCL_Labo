package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.regisupdate;

import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope("prototype")
public class DeliveryAmountCarriageItem implements Serializable {
    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** 上限金額 */
    @HCHankaku
    @HCNumber
    private String maxPrice;

    /** 送料 */
    @HCHankaku
    @HCNumber
    private String amountCarriage;
}
