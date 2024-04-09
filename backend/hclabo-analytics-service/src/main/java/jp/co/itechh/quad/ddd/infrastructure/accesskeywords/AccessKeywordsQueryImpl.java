package jp.co.itechh.quad.ddd.infrastructure.accesskeywords;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQuery;
import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ScriptOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 検索キーワード集計クエリ―実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Repository
public class AccessKeywordsQueryImpl implements AccessKeywordsQuery {

    /**
     * Mongo template
     */
    private final MongoTemplate mongoTemplate;

    /**
     * コレクション名
     */
    private final static String COLLECTION_NAME = "accessKeywordsQueryModel";

    /**
     * 正規表現
     */
    private final static String REGEX_PATTERN = ".*";

    /**
     * DB用
     */
    private final static String DB_FILED_ID = "_id";

    /**
     * 検索キーワード集計（DB用）
     */
    private final static String DB_FIELD_SEARCH_KEYWORDS = "AccessSearchKeyword.searchKeyword";

    /**
     * アクセス日時（DB用）
     */
    private final static String DB_FIELD_ACCESS_TIME = "AccessSearchKeyword.accessTime";

    /**
     * 検索結果件数（DB用）
     */
    private final static String DB_FIELD_SEARCH_RESULT_COUNT = "AccessSearchKeyword.searchResultCount";

    /**
     * 検索キーワード集計（JAVA用）
     */
    private final static String JAVA_FIELD_SEARCH_KEYWORDS = "searchKeyword";

    /**
     * 検索回数（JAVA用）
     */
    private final static String JAVA_FIELD_SEARCH_COUNT = "searchCount";

    /**
     * 検索結果件数（JAVA用）
     */
    private final static String JAVA_FIELD_SEARCH_RESULT_COUNT = "searchResultCount";

    /**
     * コンストラクタ
     *
     * @param mongoTemplate MongoTemplate
     */
    @Autowired
    public AccessKeywordsQueryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 検索キーワード集計検索
     *
     * @param condition 検索キーワード集計
     * @return 検索キーワード集計クエリーモデルリスト
     */
    @Override
    public List<AccessKeywordsQueryModel> find(AccessKeywordsQueryCondition condition) {

        AssertChecker.assertNotNull("condition is null", condition);

        AggregationOperation group = createGroup();

        Criteria criteria = createCriteria(condition);
        AggregationOperation match = null;
        if (criteria != null) {
            match = Aggregation.match(criteria);
        }

        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, JAVA_FIELD_SEARCH_COUNT)
                                               .and(Sort.Direction.ASC, JAVA_FIELD_SEARCH_KEYWORDS);

        AggregationOperation limit;
        if (condition.getPageInfoForQuery().getPageable() != null) {
            limit = Aggregation.limit(condition.getPageInfoForQuery().getPageable().getPageSize());
        } else {
            limit = Aggregation.limit(sumMaxAccessKeyword(MAX_ACCESS_KEYWORD, 1));
        }

        List<AggregationOperation> aggregationOperation = new ArrayList<>();
        if (match != null) {
            aggregationOperation.add(match);
        }
        aggregationOperation.add(createProject());
        aggregationOperation.add(group);
        aggregationOperation.add(createProject2());
        aggregationOperation.add(sort);
        aggregationOperation.add(limit);

        Criteria criteriaAfterQuery = createCriteriaAfterQuery(condition);
        if (criteriaAfterQuery != null) {
            aggregationOperation.add(Aggregation.match(criteriaAfterQuery));
        }

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperation)
                                             .withOptions(AggregationOptions.builder().allowDiskUse(true).build());

        AggregationResults<AccessKeywordsQueryModel> results =
                        mongoTemplate.aggregate(aggregation, COLLECTION_NAME, AccessKeywordsQueryModel.class);
        return results.getMappedResults();
    }

    /**
     * the sum of two arguments
     *
     * @param maxAccessKeyword
     * @param increaseByOne
     * @return It will return the sum of two arguments
     */
    private int sumMaxAccessKeyword(int maxAccessKeyword, int increaseByOne) {
        return maxAccessKeyword + increaseByOne;
    }

    /**
     * 検索キーワード集計ダウンロード
     *
     * @param condition 検索キーワード集計
     * @return 検索キーワード集計クエリーモデル CloseableIterator
     */
    @Override
    public CloseableIterator<AccessKeywordsQueryModel> download(AccessKeywordsQueryCondition condition) {

        AssertChecker.assertNotNull("condition is null", condition);

        AggregationOperation group = createGroup();

        Criteria criteria = createCriteria(condition);
        AggregationOperation match = null;
        if (criteria != null) {
            match = Aggregation.match(criteria);
        }
        AggregationOperation sort = Aggregation.sort(Sort.Direction.DESC, JAVA_FIELD_SEARCH_COUNT)
                                               .and(Sort.Direction.ASC, JAVA_FIELD_SEARCH_KEYWORDS);

        List<AggregationOperation> aggregationOperation = new ArrayList<>();
        if (match != null) {
            aggregationOperation.add(match);
        }
        aggregationOperation.add(createProject());
        aggregationOperation.add(group);
        aggregationOperation.add(createProject2());
        aggregationOperation.add(sort);

        Criteria criteriaAfterQuery = createCriteriaAfterQuery(condition);
        if (criteriaAfterQuery != null) {
            aggregationOperation.add(Aggregation.match(criteriaAfterQuery));
        }

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperation)
                                             .withOptions(AggregationOptions.builder().allowDiskUse(true).build());

        return mongoTemplate.aggregateStream(aggregation, COLLECTION_NAME, AccessKeywordsQueryModel.class);
    }

    /**
     * プロジェクトを作成する
     *
     * @return AggregationOperation
     */
    private AggregationOperation createProject() {
        String body =
                        "function (string, searchString, replaceString) { var _String, i, len, index; _String = String; searchString = _String(searchString);replaceString = _String(replaceString).split('');len = searchString.length;i = replaceString.length; while (len > i) { replaceString[i] = ''; } string = _String(string).split('');i = string.length; while (i--) { index = searchString.indexOf(string[i]);if (index !== -1) { string[i] = replaceString[index]; } } return string.join(''); }";
        String args1 = "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ";
        String args2 = "ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ";

        return Aggregation.project()
                          //                .and(StringOperators.valueOf(DB_FIELD_SEARCH_KEYWORDS).toUpper()).as(DB_FIELD_SEARCH_KEYWORDS)
                          .and(ScriptOperators.function(body).args("$AccessSearchKeyword.searchKeyword", args1, args2))
                          .as(DB_FIELD_SEARCH_KEYWORDS)
                          .and(DB_FIELD_SEARCH_RESULT_COUNT)
                          .as(DB_FIELD_SEARCH_RESULT_COUNT);
    }

    /**
     * グループを作成する
     *
     * @return AggregationOperation
     */
    private AggregationOperation createGroup() {
        return Aggregation.group(DB_FIELD_SEARCH_KEYWORDS)
                          .count()
                          .as(JAVA_FIELD_SEARCH_COUNT)
                          .avg(DB_FIELD_SEARCH_RESULT_COUNT)
                          .as(JAVA_FIELD_SEARCH_RESULT_COUNT);
    }

    /**
     * プロジェクトを作成する
     *
     * @return AggregationOperation
     */
    private AggregationOperation createProject2() {
        return Aggregation.project()
                          .and(DB_FILED_ID)
                          .as(JAVA_FIELD_SEARCH_KEYWORDS)
                          .andExclude(DB_FILED_ID)
                          .and(JAVA_FIELD_SEARCH_COUNT)
                          .as(JAVA_FIELD_SEARCH_COUNT)
                          .and(JAVA_FIELD_SEARCH_RESULT_COUNT)
                          .as(JAVA_FIELD_SEARCH_RESULT_COUNT);
    }

    /**
     * クライテリアを作成する
     *
     * @param condition 検索キーワード集計
     * @return Criteria クライテリア
     */
    private Criteria createCriteria(AccessKeywordsQueryCondition condition) {
        List<Criteria> expression = new ArrayList<>();
        if (condition.getSearchKeyword() != null) {
            Criteria searchKeyword = Criteria.where(DB_FIELD_SEARCH_KEYWORDS)
                                             .regex(REGEX_PATTERN + condition.getSearchKeyword() + REGEX_PATTERN, "i");
            expression.add(searchKeyword);
        }

        createFromToQuery(expression, DB_FIELD_ACCESS_TIME, condition.getProcessDateFrom().getTime(),
                          condition.getProcessDateTo().getTime()
                         );

        if (expression.size() > 0) {
            return new Criteria().andOperator(expression.toArray(new Criteria[0]));
        } else {
            return null;
        }
    }

    /**
     * クエリ―後でクライテリアを作成する
     *
     * @param condition 検索キーワード集計
     * @return Criteria クライテリア
     */
    private Criteria createCriteriaAfterQuery(AccessKeywordsQueryCondition condition) {
        List<Criteria> expression = new ArrayList<>();

        createFromToQuery(expression, JAVA_FIELD_SEARCH_COUNT, condition.getSearchCountFrom(),
                          condition.getSearchCountTo()
                         );

        if (expression.size() > 0) {
            return new Criteria().andOperator(expression.toArray(new Criteria[0]));
        } else {
            return null;
        }
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