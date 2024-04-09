package jp.co.itechh.quad.ddd.usecase.reports;

import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesQuery;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesSearchQueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * 受注・売上集計ユースケース
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Service
public class OrderSalesUseCase {

    /**
     * 受注・売上集計クエリ―クラス
     */
    private final OrderSalesQuery orderSalesQuery;

    /**
     * コンストラクタ
     *
     * @param orderSalesQuery 受注・売上集計クエリ―クラス
     */
    @Autowired
    public OrderSalesUseCase(OrderSalesQuery orderSalesQuery) {
        this.orderSalesQuery = orderSalesQuery;
    }

    /**
     * 受注・売上集計
     *
     * @param condition 受注・売上集計用検索条件クラス
     * @return 受注・売上集計クエリーモデルリスト
     */
    public List<OrderSalesQueryModel> get(OrderSalesSearchQueryCondition condition) {
        return orderSalesQuery.find(condition);
    }

    /**
     * Download stream.
     *
     * @param condition the condition
     * @return the stream
     */
    public Stream<OrderSalesQueryModel> download(OrderSalesSearchQueryCondition condition) {
        return orderSalesQuery.download(condition).stream();
    }
}