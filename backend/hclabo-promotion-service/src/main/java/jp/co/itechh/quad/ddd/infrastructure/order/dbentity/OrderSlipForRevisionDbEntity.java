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
 * 改訂用注文票Dbエンティティクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "OrderSlipForRevision")
@Data
@Component
@Scope("prototype")
public class OrderSlipForRevisionDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 改訂用注文票ID */
    @Id
    private String orderSlipRevisionId;

    /** 改訂用取引ID */
    private String transactionRevisionId;

    /** 注文票ID */
    private String orderSlipId;

    /** 注文ステータス */
    private String orderStatus;

    /** 顧客ID */
    private String customerId;

    /** 取引ID */
    private String transactionId;

    /** 登録日時 */
    private Date registDate;

}
