package jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 受注Dbエンティティ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "OrderReceived")
@Data
@Component
@Scope("prototype")
public class OrderReceivedDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注ID */
    @Id
    private String orderReceivedId;

    /** 受注番号 */
    private String orderCode;

    /** 登録日時 */
    private Date registDate;

    /** 受注日時 */
    private Date orderReceivedDate;

    /** 取消日時 */
    private Date cancelDate;

    /** 最新取引ID */
    private String latestTransactionId;
}
