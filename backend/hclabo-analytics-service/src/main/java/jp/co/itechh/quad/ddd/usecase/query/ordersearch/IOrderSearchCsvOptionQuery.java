package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import java.util.List;

/**
 * 注検索CSVオプション クエリ
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
public interface IOrderSearchCsvOptionQuery {

    /**
     * 受注検索CSVオプション更新
     *
     * @param orderSearchCsvOptionQueryModel クエリモデル
     */
    void update(OrderSearchCsvOptionQueryModel orderSearchCsvOptionQueryModel);

    /**
     * オプションIdで注検索CSVオプション初期化
     *
     * @param orderSearchCsvOptionQueryModel クエリモデル
     */
    void initial(OrderSearchCsvOptionQueryModel orderSearchCsvOptionQueryModel);

    /**
     * 注検索CSVオプション取得
     *
     * @return 検索CSVオプションクエリモデルリスト
     */
    List<OrderSearchCsvOptionQueryModel> get();

    /**
     * オプションIdで注検索CSVオプション取得
     *
     * @param Id _id
     * @return 検索CSVオプションクエリモデル
     */
    OrderSearchCsvOptionQueryModel getById(String Id);
}
