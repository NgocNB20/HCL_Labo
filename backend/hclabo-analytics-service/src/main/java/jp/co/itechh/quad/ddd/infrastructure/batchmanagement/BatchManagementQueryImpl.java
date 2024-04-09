package jp.co.itechh.quad.ddd.infrastructure.batchmanagement;

import jp.co.itechh.quad.core.constant.type.HTypeBatchStatus;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQuery;
import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * バッチ管理クエリ―実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Repository
public class BatchManagementQueryImpl implements BatchManagementQuery {

    /**
     * バッチ名
     */
    private final static String BATCH_NAME = "batch-log.batch-id";

    /**
     * バッチ状態
     */
    private final static String BATCH_STATUS = "batch-log.result";

    /**
     * 処理日付
     */
    private final static String PROCESS_DATE = "batch-log.start-time";

    /**
     * 正規表現
     */
    private final static String REGEX_PATTERN = ".*";

    /**
     * Mongo template
     */
    private final MongoTemplate mongoTemplate;

    /**
     * コンストラクタ
     *
     * @param mongoTemplate MongoTemplate
     */
    @Autowired
    public BatchManagementQueryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * バッチ管理検索
     *
     * @param condition バッチ管理クエリ―条件
     * @return バッチ管理クエリ―モデルリスト
     */
    @Override
    public List<BatchManagementQueryModel> find(BatchManagementQueryCondition condition) {
        AssertChecker.assertNotNull("condition is null", condition);

        Query query = createQuery(condition);
        query.allowDiskUse(true);
        if (condition.getPageInfoForQuery() != null) {
            if (condition.getPageInfoForQuery().getSort() != null) {
                query.with(condition.getPageInfoForQuery().getSort());
            }

            if (condition.getPageInfoForQuery().getPageable() != null) {
                query.with(condition.getPageInfoForQuery().getPageable());
            }
        }

        return mongoTemplate.find(query, BatchManagementQueryModel.class);
    }

    /**
     * 合計レコーダーを数える
     *
     * @param condition バッチ管理クエリ―条件
     * @return 合計記録
     */
    @Override
    public long count(BatchManagementQueryCondition condition) {
        AssertChecker.assertNotNull("condition is null", condition);

        Query query = createQuery(condition);
        query.allowDiskUse(true);
        return mongoTemplate.count(query, BatchManagementQueryModel.class);
    }

    /**
     * クライテリアを作成する
     *
     * @param condition バッチ管理クエリ―条件
     * @return Query クエリ―
     */
    private Query createQuery(BatchManagementQueryCondition condition) {
        Query query = new Query();
        List<Criteria> expression = new ArrayList<>();
        if (condition.getBatchName() != null) {
            Criteria batchName = Criteria.where(BATCH_NAME).regex(REGEX_PATTERN + condition.getBatchName());
            expression.add(batchName);
        }

        if (condition.getStatus() != null) {
            Criteria batchStatus = Criteria.where(BATCH_STATUS)
                                           .is(EnumTypeUtil.getEnumFromValue(HTypeBatchStatus.class,
                                                                             condition.getStatus()
                                                                            ).getLabel());
            expression.add(batchStatus);
        }

        Long processDateFrom = condition.getProcessDateFrom() != null ? condition.getProcessDateFrom().getTime() : null;
        Long processDateTo = condition.getProcessDateTo() != null ? condition.getProcessDateTo().getTime() : null;

        createFromToQuery(expression, PROCESS_DATE, processDateFrom, processDateTo);

        if (expression.size() > 0) {
            query.addCriteria(new Criteria().andOperator(expression.toArray(new Criteria[0])));
        }

        return query;
    }

    /**
     * クライテリア（from~to）を作成する
     *
     * @param expression クライテリアリスト
     * @param queryField クエリ―
     * @param gt         Object
     * @param lt         Object
     */
    private void createFromToQuery(List<Criteria> expression, String queryField, Object gt, Object lt) {
        Criteria criteria = null;
        if (lt != null) {
            criteria = Criteria.where(queryField).lte(lt);
        }
        if (gt != null) {
            if (criteria == null) {
                criteria = Criteria.where(queryField).gte(gt);
            } else {
                criteria.gte(gt);
            }
        }
        if (criteria != null) {
            expression.add(criteria);
        }
    }
}