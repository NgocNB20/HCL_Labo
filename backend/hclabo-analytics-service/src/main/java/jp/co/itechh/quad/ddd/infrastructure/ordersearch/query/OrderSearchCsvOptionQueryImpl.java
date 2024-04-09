package jp.co.itechh.quad.ddd.infrastructure.ordersearch.query;

import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.IOrderSearchCsvOptionQuery;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchCsvOptionQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 注検索CSVオプションクエリ実装クラス
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@Repository
public class OrderSearchCsvOptionQueryImpl implements IOrderSearchCsvOptionQuery {

    /**
     * Mongoテンプレート
     */
    private final MongoTemplate mongoTemplate;

    /**
     * 注検索CSVオプションクエリモデルコレクション
     */
    private final static String DB_COLLECTION_NAME = "orderSearchCsvOptionQueryModel";

    /**
     * DBのIdフィールド名
     */
    private final static String DB_ID_FIELD_NAME = "_id";

    /**
     * コンストラクタ
     *
     * @param mongoTemplate Mongoテンプレート
     */
    @Autowired
    public OrderSearchCsvOptionQueryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 受注検索CSVオプション更新
     *
     * @param orderSearchCsvOptionQueryModel クエリモデル
     */
    @Override
    public void update(OrderSearchCsvOptionQueryModel orderSearchCsvOptionQueryModel) {

        if (orderSearchCsvOptionQueryModel == null) {
            throw new DomainException("ANALYTIC-OSQM0002-E");
        }

        mongoTemplate.save(orderSearchCsvOptionQueryModel);
    }

    /**
     * オプションIdで注検索CSVオプション初期化
     *
     * @param orderSearchCsvOptionQueryModel クエリモデル
     */
    @Override
    public void initial(OrderSearchCsvOptionQueryModel orderSearchCsvOptionQueryModel) {
        mongoTemplate.insert(orderSearchCsvOptionQueryModel, DB_COLLECTION_NAME);
    }

    /**
     * 注検索CSVオプション取得
     *
     * @return 検索CSVオプションクエリモデルリスト
     */
    @Override
    public List<OrderSearchCsvOptionQueryModel> get() {
        Query query = new Query();
        query.allowDiskUse(true);
        return mongoTemplate.find(query, OrderSearchCsvOptionQueryModel.class);
    }

    /**
     * オプションIdで注検索CSVオプション取得
     *
     * @param Id _id
     * @return 検索CSVオプションクエリモデル
     */
    @Override
    public OrderSearchCsvOptionQueryModel getById(String Id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(DB_ID_FIELD_NAME).is(Id));
        query.allowDiskUse(true);
        return mongoTemplate.findOne(query, OrderSearchCsvOptionQueryModel.class);
    }
}
