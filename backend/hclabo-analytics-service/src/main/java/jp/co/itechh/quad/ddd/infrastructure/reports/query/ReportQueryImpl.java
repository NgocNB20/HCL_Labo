package jp.co.itechh.quad.ddd.infrastructure.reports.query;

import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.IReportQuery;
import jp.co.itechh.quad.ddd.usecase.query.reports.registreports.ReportsQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * 集計用販売データ登録―実装クラス
 */
@Repository
public class ReportQueryImpl implements IReportQuery {

    /**
     * Mongo template
     */
    private final MongoTemplate mongoTemplate;

    /**
     * 受注ID（DB用）
     */
    private final String DB_FIELD_TRANSACTION_ID = "transactionId";

    /**
     * コレクション名
     */
    private final String DB_COLLECTION_NAME = "reportsQueryModel";

    /**
     * コンストラクタ
     *
     * @param mongoTemplate Mongo template
     */
    @Autowired
    public ReportQueryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 集計用販売データ登録
     *
     * @param orderSalesQueryModel 集計用販売データ
     */
    @Override
    public void regist(ReportsQueryModel orderSalesQueryModel) {

        if (orderSalesQueryModel == null) {

            throw new DomainException("ANALYTIC-OSQM0001-E");

        }

        mongoTemplate.save(orderSalesQueryModel, DB_COLLECTION_NAME);

    }

    /**
     * 「改訂前取引ID」で1つ前の集計用販売データを取得
     *
     * @param transactionBeforeRevisionId 改訂前取引ID
     * @return 集計用販売データ（集計機能共通用データ）
     */
    @Override
    public ReportsQueryModel getByBeforeTransactionId(String transactionBeforeRevisionId) {

        Query query = new Query();
        query.addCriteria(Criteria.where(DB_FIELD_TRANSACTION_ID).is(transactionBeforeRevisionId));
        query.allowDiskUse(true);
        return mongoTemplate.findOne(query, ReportsQueryModel.class);
    }

}