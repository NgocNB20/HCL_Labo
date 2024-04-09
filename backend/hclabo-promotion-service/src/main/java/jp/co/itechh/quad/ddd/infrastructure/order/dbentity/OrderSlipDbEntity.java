package jp.co.itechh.quad.ddd.infrastructure.order.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 注文票DbEntityクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "OrderSlip")
@Data
@Component
@Scope("prototype")
public class OrderSlipDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 注文票ID */
    @Id
    private String orderSlipId;

    /** 注文ステータス */
    private String orderStatus;

    /** 顧客ID */
    private String customerId;

    /** 取引ID */
    private String transactionId;

    /** 登録日時 */
    private Date registDate;

    /** ユーザーエージェント */
    private String userAgent;
}
