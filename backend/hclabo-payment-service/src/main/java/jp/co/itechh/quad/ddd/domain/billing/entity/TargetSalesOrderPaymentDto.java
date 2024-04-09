package jp.co.itechh.quad.ddd.domain.billing.entity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 対象受注番号Dto
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Data
@Component
@Scope("prototype")
public class TargetSalesOrderPaymentDto {

    /** 受注番号 */
    private String orderCode;

    /** 決済方法(GMO) */
    private String payType;

    /** GMOキャンセル期限日 */
    private Date cancelLimit;
}
