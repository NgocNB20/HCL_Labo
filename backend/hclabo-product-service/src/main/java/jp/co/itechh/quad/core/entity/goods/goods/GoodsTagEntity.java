package jp.co.itechh.quad.core.entity.goods.goods;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * タグ商品クラス
 */
@Entity
@Table(name = "GoodsTag")
@Data
@Component
@Scope("prototype")
public class GoodsTagEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** タグ（必須） */
    @Column(name = "tag")
    @Id
    private String tag;

    /** 参照数（必須） */
    @Column(name = "count")
    private Integer count;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}
