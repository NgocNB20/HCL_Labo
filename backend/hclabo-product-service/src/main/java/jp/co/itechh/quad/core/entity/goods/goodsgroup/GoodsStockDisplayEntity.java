package jp.co.itechh.quad.core.entity.goods.goodsgroup;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品在庫表示
 *
 * @author PHAM QUANG DIEU
 */
@Entity
@Table(name = "GoodsStockDisplay")
@Data
@Component
@Scope("prototype")
public class GoodsStockDisplayEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品SEQ (FK)（必須） */
    @Column(name = "goodsSeq")
    @Id
    private Integer goodsSeq;

    /** 残少表示在庫数（必須） */
    @Column(name = "remainderFewStock")
    private BigDecimal remainderFewStock;

    /** 発注点在庫数（必須） */
    @Column(name = "orderPointStock")
    private BigDecimal orderPointStock;

    /** 安全在庫数（必須） */
    @Column(name = "safetyStock")
    private BigDecimal safetyStock;

    /** 実在庫数（必須） */
    @Column(name = "realStock")
    private BigDecimal realStock;

    /** 注文確保在庫数（必須） */
    @Column(name = "orderReserveStock")
    private BigDecimal orderReserveStock;

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;
}