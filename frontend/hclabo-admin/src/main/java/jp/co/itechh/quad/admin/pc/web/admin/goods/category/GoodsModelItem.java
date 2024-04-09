package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品モデル項目
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class GoodsModelItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリSEQ */
    private Integer categorySeq;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** 手動並び替え表示順 */
    private Integer manualOrderDisplay;

    /** 商品管理番号 */
    private String goodsGroupCode;

    /** 商品グループ名 */
    private String goodsGroupName;

    /** 新着日付 */
    private Date whatsnewDate;

    /** 商品単価 */
    private BigDecimal goodsPrice;

    /** 人気カウント（必須） */
    private Integer popularityCount;

    /** フロント表示 */
    private String frontDisplay;

    /** カテゴリID */
    private String categoryId;
}
