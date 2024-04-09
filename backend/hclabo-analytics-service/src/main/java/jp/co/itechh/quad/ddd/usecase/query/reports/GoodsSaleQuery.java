package jp.co.itechh.quad.ddd.usecase.query.reports;

import org.springframework.data.util.CloseableIterator;

import java.util.List;

/**
 * 商品販売個数集計クエリ
 */
public interface GoodsSaleQuery {

    /**
     * 商品販売個数集計取得
     *
     * @param condition 商品販売クエリ条件
     * @return the list
     */
    List<GoodsSaleQueryModel> find(GoodsSaleQueryCondition condition);

    /**
     * 商品販売個数集計ダウンロード
     *
     * @param condition 商品販売クエリ条件
     * @return
     */
    CloseableIterator<GoodsSaleQueryModel> download(GoodsSaleQueryCondition condition);
}
