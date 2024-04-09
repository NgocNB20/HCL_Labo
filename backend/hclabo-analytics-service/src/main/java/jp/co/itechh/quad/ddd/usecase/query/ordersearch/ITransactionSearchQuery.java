package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import java.util.List;

/**
 * 取引検索クエリー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface ITransactionSearchQuery {

    /**
     * 取引検索
     *
     * @param condition 取引検索条件
     * @return 取引検索結果
     */
    List<OrderSearchQueryModel> search(OrderSearchQueryCondition condition);
}
