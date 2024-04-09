package jp.co.itechh.quad.ddd.infrastructure.ordersearch.query;

import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.IOrderRegistUpdateQuery;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * バッチ管理クエリ―実装クラス
 *
 * @author Doan Thang (VJP)
 */
@Repository
public class OrderRegistUpdateQueryImpl implements IOrderRegistUpdateQuery {

    /**
     * Mongo template
     */
    private final MongoTemplate mongoTemplate;

    /**
     * コレクション名
     */
    private final String DB_COLLECTION_NAME = "orderSearchQueryModel";

    /**
     * コンストラクタ
     *
     * @param mongoTemplate Mongo template
     */
    @Autowired
    public OrderRegistUpdateQueryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 受注情報登録・更新
     *
     * @param orderSearchQueryModel 受注検索クエリーモデル
     */
    @Override
    public void registUpdate(OrderSearchQueryModel orderSearchQueryModel) {

        if (orderSearchQueryModel == null) {

            throw new DomainException("ANALYTIC-OSQM0001-E");

        }

        mongoTemplate.save(orderSearchQueryModel, DB_COLLECTION_NAME);

    }

}