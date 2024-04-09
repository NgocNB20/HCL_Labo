package jp.co.itechh.quad.admin.pc.web.admin.totaling.goodssales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 商品アイテム
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class GoodsItem {

    /** 商品管理番号 */
    private String goodsGroupCode;

    /** 商品コード */
    private String goodsCode;

    /** 商品名 */
    private String goodsName;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /** JANコード */
    private String janCode;

    /** 商品単価 */
    private Integer unitPrice;
}
