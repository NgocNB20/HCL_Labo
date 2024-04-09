package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import jp.co.itechh.quad.core.dto.order.OrderCSVDto;
import jp.co.itechh.quad.core.dto.shipment.ShipmentCSVDto;
import org.springframework.data.util.CloseableIterator;

import java.sql.Timestamp;
import java.util.List;

/**
 * 受注検索クエリ―クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface OrderSearchQuery {

    /**
     * 受注検索クエリーモデルリスト取得.
     *
     * @param condition 受注検索条件
     * @return 受注検索クエリーモデルリスト
     */
    List<OrderSearchQueryModel> find(OrderSearchQueryCondition condition);

    /**
     * 合計レコーダーを数える
     *
     * @param condition 受注検索条件
     * @return 合計記録
     */
    int count(OrderSearchQueryCondition condition);

    /**
     * 受注CSVクエリに集計ダウンロード
     *
     * @param condition 検索キーワード集計
     * @return 受注CSVクエリに集計クエリーモデル CloseableIterator
     */
    CloseableIterator<OrderCSVDto> download(OrderSearchQueryCondition condition);

    /**
     * 出荷CSVクエリに集計ダウンロード
     *
     * @param condition 検索キーワード集計
     * @return 出荷CSVクエリに集計クエリーモデル CloseableIterator
     */
    CloseableIterator<ShipmentCSVDto> shipment(OrderSearchQueryCondition condition);

    /**
     * 売上商品数カウント
     *
     * @param goodsGroupCode
     * @param timeFrom
     * @return 売上商品数
     */
    int countOrderProduct(String goodsGroupCode, Timestamp timeFrom);
}
