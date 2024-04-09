package jp.co.itechh.quad.admin.pc.web.admin.totaling.goodssales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 商品別販売価格別集計
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class GoodsSalesAggregateItem {

    /** 日付データ */
    private String date;

    /** 販売個数 */
    private Integer salesCount = 0;

    /** キャンセル数 */
    private Integer cancelCount = 0;
}
