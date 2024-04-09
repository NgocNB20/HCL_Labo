package jp.co.itechh.quad.ddd.infrastructure.order.dbentity;

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
 * 注文商品DbEntityクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "OrderItemRevision")
@Data
@Component
@Scope("prototype")
public class OrderItemRevisionDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "orderitemrevision_id_seq")
    private Long id;

    /** 商品ID（商品サービスの商品SEQ） */
    private String itemId;

    /** 注文商品連番 */
    private int orderItemSeq;

    /** 注文数量 */
    private int orderCount;

    /** 商品名 */
    private String itemName;

    /** 規格タイトル1 */
    private String unitTitle1;

    /** 規格値1 */
    private String unitValue1;

    /** 規格タイトル2 */
    private String unitTitle2;

    /** 規格値2 */
    private String unitValue2;

    /** JANコード */
    private String janCode;

    /** 改訂用注文票ID */
    private String orderSlipRevisionId;

    /** ノベルティ商品フラグ */
    private String noveltyGoodsType;

    /** 注文商品ID */
    private String orderItemId;
}
