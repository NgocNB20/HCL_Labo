package jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales;

import org.springframework.data.util.CloseableIterator;

import java.util.List;

/**
 * 受注・売上集計クエリ―クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface OrderSalesQuery {

    /**
     * 受注・売上集計クエリーモデルリスト取得.
     *
     * @param condition 受注・売上集計用検索条件クラス
     * @return 受注・売上集計クエリーモデルリスト
     */
    List<OrderSalesQueryModel> find(OrderSalesSearchQueryCondition condition);

    /**
     * Download stream.
     *
     * @param condition 受注・売上集計用検索条件クラス
     * @return the stream
     */
    CloseableIterator<OrderSalesQueryModel> download(OrderSalesSearchQueryCondition condition);

}
