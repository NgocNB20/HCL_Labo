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
import java.math.BigDecimal;

/**
 * 改訂用商品購入価格小計 DbEntityクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "ItemPurchasePriceSubTotalForRevision")
@Data
@Component
@Scope("prototype")
public class ItemPurchasePriceSubTotalForRevisionDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "itempurchasepricesubtotalforrevision_id_seq")
    private Long id;

    /** 注文商品連番 */
    private int orderItemSeq;

    /** 商品購入価格小計 */
    private int itemPurchasePriceSubTotal;

    /** 商品ID（商品SEQ） */
    private String itemId;

    /** 商品単価 */
    private int itemUnitPrice;

    /** 商品数量 */
    private int itemCount;

    /** 商品税率 */
    private BigDecimal itemTaxRate;

    /** 送料無料商品フラグ */
    private boolean freeCarriageItemFlag;

    /** 改訂用販売伝票ID */
    private String salesSlipRevisionId;

}
