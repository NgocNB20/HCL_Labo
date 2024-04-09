package jp.co.itechh.quad.admin.pc.web.admin.totaling.goodssales;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品別販売価格別集計の合計.
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class GoodsSalesSummary {

    /** 商品規格単位の販売総数 */
    private Integer totalSales;

    /** 商品規格単位のキャンセル総数 */
    private Integer totalCancel;

    /** 商品別販売価格別集計一覧 */
    private List<GoodsSalesAggregateItem> aggregateItems;
}
