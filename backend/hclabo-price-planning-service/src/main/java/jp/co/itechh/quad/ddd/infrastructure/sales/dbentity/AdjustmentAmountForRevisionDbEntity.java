package jp.co.itechh.quad.ddd.infrastructure.sales.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 改訂用クーポンDbEntityクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "AdjustmentAmountForRevision")
@Data
@Component
@Scope("prototype")
public class AdjustmentAmountForRevisionDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** クーポン ID */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "adjustmentamountforrevision_id_seq")
    private Long id;

    /** 調整金額連番 */
    private int adjustmentAmountSeq;

    /** 調整項目名 */
    private String adjustName;

    /** 調整金額 */
    private int adjustPrice;

    /** 改訂用販売伝票ID */
    private String salesSlipRevisionId;

}
