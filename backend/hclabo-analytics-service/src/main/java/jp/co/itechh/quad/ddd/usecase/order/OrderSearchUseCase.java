package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.core.dto.order.OrderCSVDto;
import jp.co.itechh.quad.core.dto.shipment.ShipmentCSVDto;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQuery;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

/**
 * 受注検索ユーザケース.
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Service
public class OrderSearchUseCase {

    /**
     * 受注検索クエリ
     */
    private final OrderSearchQuery orderSearchQuery;

    /**
     * コンストラクタ
     *
     * @param orderSearchQuery 受注検索クエリ
     */
    @Autowired
    public OrderSearchUseCase(OrderSearchQuery orderSearchQuery) {
        this.orderSearchQuery = orderSearchQuery;
    }

    /**
     * 受注検索クエリーモデルリスト取得.
     *
     * @param condition 受注検索条件
     * @return 受注検索クエリーモデルリスト
     */
    public List<OrderSearchQueryModel> get(OrderSearchQueryCondition condition) {
        return orderSearchQuery.find(condition);
    }

    /**
     * 合計レコーダーを数える
     *
     * @param condition バッチ管理クエリ―条件
     */
    public int count(OrderSearchQueryCondition condition) {
        return orderSearchQuery.count(condition);
    }

    /**
     * 受注CSVクエリに集計ダウンロード
     *
     * @param condition 検索キーワード集計
     * @return 受注CSVクエリに集計クエリーモデルのStream
     */
    public Stream<OrderCSVDto> download(OrderSearchQueryCondition condition) {
        return orderSearchQuery.download(condition).stream();
    }

    /**
     * 出荷CSVクエリに集計ダウンロード
     *
     * @param condition 検索キーワード集計
     * @return 出荷CSVクエリに集計クエリーモデルのStream
     */
    public Stream<ShipmentCSVDto> shipment(OrderSearchQueryCondition condition) {
        return orderSearchQuery.shipment(condition).stream();
    }

    /**
     * 売上商品数カウント
     *
     * @param goodsGroupCode
     * @param timeFrom
     * @return
     */
    public int countOrderProduct(String goodsGroupCode, Timestamp timeFrom) {

        AssertChecker.assertNotEmpty("goodsGroupSeq is null", goodsGroupCode);
        AssertChecker.assertNotNull("timeFrom is null", timeFrom);

        return orderSearchQuery.countOrderProduct(goodsGroupCode, timeFrom);
    }
}
