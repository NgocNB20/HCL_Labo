package jp.co.itechh.quad.ddd.usecase.reports;

import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQuery;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * 商品販売個数集計ユーザケース
 */
@Service
public class GoodsSaleUseCase {

    /**
     * 商品販売個数集計クエリ
     */
    private final GoodsSaleQuery goodsSaleQuery;

    /**
     * コンストラクタ
     *
     * @param goodsSaleQuery 商品販売個数集計クエリ
     */
    @Autowired
    public GoodsSaleUseCase(GoodsSaleQuery goodsSaleQuery) {
        this.goodsSaleQuery = goodsSaleQuery;
    }

    /**
     * 商品販売個数集計取得
     *
     * @param queryCondition 商品販売クエリ条件
     * @return 商品販売個数集計
     */
    public List<GoodsSaleQueryModel> get(GoodsSaleQueryCondition queryCondition) {

        if (queryCondition.getAggregateTimeFrom() == null || queryCondition.getAggregateTimeTo() == null) {
            throw new DomainException("ANALYTICS-GOODSALESE0002-E");
        }

        return goodsSaleQuery.find(queryCondition);
    }

    /**
     * 商品販売個数集計ダウンロード
     *
     * @param condition 商品販売クエリ条件
     * @return stream
     */
    public Stream<GoodsSaleQueryModel> download(GoodsSaleQueryCondition condition) {
        return goodsSaleQuery.download(condition).stream();
    }
}