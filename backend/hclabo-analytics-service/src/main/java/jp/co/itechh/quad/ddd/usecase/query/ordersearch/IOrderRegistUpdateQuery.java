package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

public interface IOrderRegistUpdateQuery {

    /**
     * 受注情報登録・更新
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     */
    void registUpdate(OrderSearchQueryModel orderSearchQueryModel);
}
