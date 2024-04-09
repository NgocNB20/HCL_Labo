package jp.co.itechh.quad.ddd.infrastructure.reports;

import com.mongodb.BasicDBObject;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQuery;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

/**
 * 商品販売個数集計クエリImpl
 */
@Repository
public class GoodsSaleQueryImpl implements GoodsSaleQuery {

    /**
     * Mongo template
     */
    private final MongoTemplate mongoTemplate;

    /**
     * 処理日時
     */
    private final static String DB_FIELD_PROCESS_TIME = "processTime";

    /**
     * 受注デバイス
     */
    private final static String DB_FIELD_ORDER_DEVICE = "orderDevice";

    /**
     * 受注ステータス
     */
    private final static String DB_FIELD_ORDER_STATUS = "orderStatus";

    /**
     * 商品管理番号
     */
    private final static String SORT_FIELD_GOODS_GROUP_CODE = "goodsSale.goodsGroupCode";

    /**
     * 商品番号
     */
    private final static String SORT_FIELD_GOODS_CODE = "goodsSale.goodsCode";

    /**
     * 商品名
     */
    private final static String SORT_FIELD_GOODS_NAME = "goodsSale.goodsName";

    /**
     * 規格1
     */
    private final static String SORT_FIELD_UNIT_VALUE1 = "goodsSale.unitValue1";

    /**
     * 規格2
     */
    private final static String SORT_FIELD_UNIT_VALUE2 = "goodsSale.unitValue2";

    /**
     * JANコード
     */
    private final static String SORT_FIELD_JAN_CODE = "goodsSale.janCode";

    /**
     * 単価
     */
    private final static String SORT_FIELD_UNIT_PRICE = "goodsSale.unitPrice";

    /**
     * 商品番号
     */
    private final static String DB_FIELD_GOODS_CODE = "orderItemList.goodsCode";

    /**
     * 商品名
     */
    private final static String DB_FIELD_GOODS_NAME = "orderItemList.goodsName";

    /**
     * カテゴリーID
     */
    private final static String DB_FIELD_CATEGORY_ID = "orderItemList.categoryId";

    /**
     * 規格1
     */
    private final static String DB_FIELD_UNIT1 = "orderItemList.unitValue1";

    /**
     * 規格2
     */
    private final static String DB_FIELD_UNIT2 = "orderItemList.unitValue2";

    /**
     * コレクション名
     */
    private final static String COLLECTION_NAME = "reportsQueryModel";

    /**
     * 処理日時FromTo
     */
    private final static String SEARCH_ORDER_TIME_FROM_TO = "1";

    /**
     * 処理日時From
     */
    private final static String SEARCH_ORDER_TIME_FROM = "2";

    /**
     * 処理日時To
     */
    private final static String SEARCH_ORDER_TIME_TO = "3";

    /**
     * 集計単位 : 日
     */
    private final static String AGGREGATE_UNIT_DAY = "1";

    /**
     * 日付形式 : 月
     */
    private final static String DATE_FORMAT_MONTH = "%Y/%m";

    /**
     * 日付形式 : 日
     */
    private final static String DATE_FORMAT_DAY = "%Y/%m/%d";

    /**
     * 正規表現 start
     */
    private final static String REGEX_ESCAPE_START = ".*\\Q";

    /**
     * 正規表現 end
     */
    private final static String REGEX_ESCAPE_END = "\\E.*";

    /**
     * SPLIT_DELIMITER
     */
    private final static String SPLIT_DELIMITER = ", ";

    /**
     * コンストラクタ
     *
     * @param mongoTemplate mongo template
     */
    public GoodsSaleQueryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 一覧取得
     *
     * @param condition 商品販売クエリ条件
     * @return 商品販売個数集計クエリモデルリスト
     */
    @Override
    public List<GoodsSaleQueryModel> find(GoodsSaleQueryCondition condition) {

        List<AggregationOperation> aggregationOperation = new ArrayList<>();

        createAggregationOperation(aggregationOperation, condition);

        if (condition.getPageInfoForQuery().getSort() != null) {
            SortOperation sort = new SortOperation(condition.getPageInfoForQuery().getSort());

            String fieldSort = condition.getPageInfoForQuery().getSort().iterator().next().getProperty();
            if (SORT_FIELD_GOODS_GROUP_CODE.equals(fieldSort)) {
                sort = sort.and(Sort.Direction.ASC, SORT_FIELD_GOODS_CODE, SORT_FIELD_GOODS_NAME,
                                SORT_FIELD_UNIT_VALUE1, SORT_FIELD_UNIT_VALUE2, SORT_FIELD_JAN_CODE,
                                SORT_FIELD_UNIT_PRICE
                               );
            } else if (SORT_FIELD_GOODS_CODE.equals(fieldSort)) {
                sort = sort.and(Sort.Direction.ASC, SORT_FIELD_GOODS_NAME, SORT_FIELD_UNIT_VALUE1,
                                SORT_FIELD_UNIT_VALUE2, SORT_FIELD_JAN_CODE, SORT_FIELD_UNIT_PRICE
                               );
            } else {
                sort = sort.and(Sort.Direction.ASC, SORT_FIELD_UNIT_VALUE1, SORT_FIELD_UNIT_VALUE2, SORT_FIELD_JAN_CODE,
                                SORT_FIELD_UNIT_PRICE
                               );
            }
            aggregationOperation.add(sort);
        }

        if (condition.getPageInfoForQuery().getPageable() != null) {
            AggregationOperation page = Aggregation.skip(condition.getPageInfoForQuery().getPageable().getPageNumber()
                                                         * condition.getPageInfoForQuery().getPageable().getPageSize());
            aggregationOperation.add(page);

            AggregationOperation limit = Aggregation.limit(condition.getPageInfoForQuery().getPageable().getPageSize());
            aggregationOperation.add(limit);
        }

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperation)
                                             .withOptions(AggregationOptions.builder().allowDiskUse(true).build());

        AggregationResults<GoodsSaleQueryModel> results =
                        mongoTemplate.aggregate(aggregation, COLLECTION_NAME, GoodsSaleQueryModel.class);

        return results.getMappedResults();
    }

    /**
     * ダウンロードStream
     *
     * @param condition 商品販売クエリ条件
     * @return
     */
    @Override
    public CloseableIterator<GoodsSaleQueryModel> download(GoodsSaleQueryCondition condition) {

        List<AggregationOperation> aggregationOperation = new ArrayList<>();

        createAggregationOperation(aggregationOperation, condition);

        // ①商品管理番号：昇順 ②商品番号：昇順
        SortOperation sort = Aggregation.sort(Sort.Direction.ASC, SORT_FIELD_GOODS_GROUP_CODE, SORT_FIELD_GOODS_CODE,
                                              SORT_FIELD_GOODS_NAME, SORT_FIELD_UNIT_VALUE1, SORT_FIELD_UNIT_VALUE2,
                                              SORT_FIELD_JAN_CODE, SORT_FIELD_UNIT_PRICE
                                             );
        aggregationOperation.add(sort);

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperation)
                                             .withOptions(AggregationOptions.builder().allowDiskUse(true).build());

        return mongoTemplate.aggregateStream(aggregation, COLLECTION_NAME, GoodsSaleQueryModel.class);
    }

    /**
     * AggregationOperation作成
     *
     * @param aggregationOperation AggregationOperation
     * @param condition            商品販売クエリ条件
     */
    private void createAggregationOperation(List<AggregationOperation> aggregationOperation,
                                            GoodsSaleQueryCondition condition) {

        AggregationOperation matchFirst = null;
        Criteria criteriaFirst = createCriteriaMatchFirst(condition);
        if (criteriaFirst != null) {
            matchFirst = Aggregation.match(criteriaFirst);
        }

        if (matchFirst != null) {
            aggregationOperation.add(matchFirst);
        }

        AggregationOperation unwind = unwind("orderItemList");
        aggregationOperation.add(unwind);

        AggregationOperation projectForCategorySearch = Aggregation.project()
                                                                   .and("orderItemList.goodsGroupCode")
                                                                   .as("orderItemList.goodsGroupCode")
                                                                   .and("orderItemList.goodsCode")
                                                                   .as("orderItemList.goodsCode")
                                                                   .and("orderItemList.goodsName")
                                                                   .as("orderItemList.goodsName")
                                                                   .and("orderItemList.unitValue1")
                                                                   .as("orderItemList.unitValue1")
                                                                   .and("orderItemList.unitValue2")
                                                                   .as("orderItemList.unitValue2")
                                                                   .and("orderItemList.janCode")
                                                                   .as("orderItemList.janCode")
                                                                   .and("orderItemList.unitPrice")
                                                                   .as("orderItemList.unitPrice")
                                                                   .and("processTime")
                                                                   .as("processTime")
                                                                   .and("orderItemList.salesCount")
                                                                   .as("orderItemList.salesCount")
                                                                   .and("orderItemList.cancelCount")
                                                                   .as("orderItemList.cancelCount")
                                                                   .and(StringOperators.Split.valueOf(
                                                                                                       "orderItemList.categoryId")
                                                                                             .split(SPLIT_DELIMITER))
                                                                   .as("orderItemList.categoryId");
        aggregationOperation.add(projectForCategorySearch);

        Criteria criteriaSecond = createCriteriaMatchSecond(condition);

        AggregationOperation matchSecond = null;
        if (criteriaSecond != null) {
            matchSecond = Aggregation.match(criteriaSecond);
        }

        if (matchSecond != null) {
            aggregationOperation.add(matchSecond);
        }

        String formatDate = DATE_FORMAT_MONTH;
        if (StringUtils.isNotEmpty(condition.getAggregateUnit()) && condition.getAggregateUnit()
                                                                             .equals(AGGREGATE_UNIT_DAY)) {
            formatDate = DATE_FORMAT_DAY;
        }

        AggregationOperation project = Aggregation.project()
                                                  .and("orderItemList.goodsGroupCode")
                                                  .as("goodsSale.goodsGroupCode")
                                                  .and("orderItemList.goodsCode")
                                                  .as("goodsSale.goodsCode")
                                                  .and("orderItemList.goodsName")
                                                  .as("goodsSale.goodsName")
                                                  .and("orderItemList.unitValue1")
                                                  .as("goodsSale.unitValue1")
                                                  .and("orderItemList.unitValue2")
                                                  .as("goodsSale.unitValue2")
                                                  .and("orderItemList.janCode")
                                                  .as("goodsSale.janCode")
                                                  .and("orderItemList.unitPrice")
                                                  .as("goodsSale.unitPrice")
                                                  .and(DateOperators.zonedDateOf("processTime",
                                                                                 DateOperators.Timezone.valueOf(
                                                                                                 "Asia/Tokyo")
                                                                                ).toString(formatDate))
                                                  .as("date")
                                                  .and("orderItemList.salesCount")
                                                  .as("totalSales")
                                                  .and("orderItemList.cancelCount")
                                                  .as("totalCancel");

        aggregationOperation.add(project);

        AggregationOperation groupFirst =
                        Aggregation.group("goodsSale.goodsGroupCode", "goodsSale.goodsCode", "goodsSale.goodsName",
                                          "goodsSale.unitValue1", "goodsSale.unitValue2", "goodsSale.janCode",
                                          "goodsSale.unitPrice", "date"
                                         )
                                   .sum("totalSales")
                                   .as("salesCount")
                                   .sum("totalCancel")
                                   .as("cancelCount");
        aggregationOperation.add(groupFirst);

        SortOperation sortDate = new SortOperation(Sort.by(Sort.Direction.ASC, "date"));
        aggregationOperation.add(sortDate);

        AggregationOperation groupSecond = Aggregation.group(
                                                                      Fields.from(Fields.field("goodsGroupCode", "$_id.goodsGroupCode"),
                                                                                  Fields.field("goodsCode", "$_id.goodsCode"),
                                                                                  Fields.field("goodsName", "$_id.goodsName"),
                                                                                  Fields.field("unitValue1", "$_id.unitValue1"),
                                                                                  Fields.field("unitValue2", "$_id.unitValue2"),
                                                                                  Fields.field("janCode", "$_id.janCode"), Fields.field("unitPrice", "$_id.unitPrice")
                                                                                 ))
                                                      .push(new BasicDBObject("date", "$_id.date").append("sales",
                                                                                                          "$salesCount"
                                                                                                         )
                                                                                                  .append("cancel",
                                                                                                          "$cancelCount"
                                                                                                         ))
                                                      .as("dateList")
                                                      .sum("salesCount")
                                                      .as("totalSales")
                                                      .sum("cancelCount")
                                                      .as("totalCancel");

        aggregationOperation.add(groupSecond);

        AggregationOperation projectFinal = Aggregation.project()
                                                       .and("_id")
                                                       .as("goodsSale")
                                                       .and("dateList")
                                                       .as("dateList")
                                                       .and("totalSales")
                                                       .as("totalSales")
                                                       .and("totalCancel")
                                                       .as("totalCancel");
        aggregationOperation.add(projectFinal);
    }

    /**
     * criteria作成
     *
     * @param condition 商品販売クエリ条件
     * @return criteria
     */
    private Criteria createCriteriaMatchFirst(GoodsSaleQueryCondition condition) {

        List<Criteria> expression = new ArrayList<>();

        String timeType = SEARCH_ORDER_TIME_FROM_TO;

        createFromToQuery(expression, timeType, condition.getAggregateTimeFrom(), condition.getAggregateTimeTo());

        if (CollectionUtils.isNotEmpty(condition.getOrderDeviceList())) {
            Criteria searchOrderStatus = Criteria.where(DB_FIELD_ORDER_DEVICE).in(condition.getOrderDeviceList());
            expression.add(searchOrderStatus);
        }

        if (StringUtils.isNotEmpty(condition.getOrderStatus())) {
            Criteria searchOrderStatus = Criteria.where(DB_FIELD_ORDER_STATUS).is(condition.getOrderStatus());
            expression.add(searchOrderStatus);
        }

        if (expression.size() > 0) {
            return new Criteria().andOperator(expression.toArray(new Criteria[0]));
        } else {
            return null;
        }
    }

    /**
     * criteria作成
     *
     * @param condition 商品販売クエリ条件
     * @return criteria
     */
    private Criteria createCriteriaMatchSecond(GoodsSaleQueryCondition condition) {

        List<Criteria> expression = new ArrayList<>();

        if (StringUtils.isNotEmpty(condition.getGoodsCode())) {
            Criteria searchGoodsCode = Criteria.where(DB_FIELD_GOODS_CODE)
                                               .regex(REGEX_ESCAPE_START + condition.getGoodsCode() + REGEX_ESCAPE_END,
                                                      "i"
                                                     );
            expression.add(searchGoodsCode);
        }

        if (StringUtils.isNotEmpty(condition.getGoodsName())) {
            Criteria searchGoodsName = new Criteria().orOperator(
                            Criteria.where(DB_FIELD_GOODS_NAME)
                                    .regex(REGEX_ESCAPE_START + condition.getGoodsName() + REGEX_ESCAPE_END, "i"),
                            Criteria.where(DB_FIELD_UNIT1)
                                    .regex(REGEX_ESCAPE_START + condition.getGoodsName() + REGEX_ESCAPE_END, "i"),
                            Criteria.where(DB_FIELD_UNIT2)
                                    .regex(REGEX_ESCAPE_START + condition.getGoodsName() + REGEX_ESCAPE_END, "i")
                                                                );

            expression.add(searchGoodsName);
        }

        if (CollectionUtils.isNotEmpty(condition.getCategoryIdList())) {
            Criteria SearchCategoryId = Criteria.where(DB_FIELD_CATEGORY_ID).in(condition.getCategoryIdList());
            expression.add(SearchCategoryId);
        }

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
     * @param gt         Object
     * @param lt         Object
     */
    private void createFromToQuery(List<Criteria> expression, String timeType, Object gt, Object lt) {

        if (StringUtils.isEmpty(timeType)) {
            return;
        }

        Criteria criteria = getCriteriaTimeType(timeType, gt, lt);

        if (criteria != null) {
            expression.add(criteria);
        }
    }

    /**
     * criteria取得
     *
     * @param timeType
     * @param gt       Object
     * @param lt       Object
     * @return
     */
    private Criteria getCriteriaTimeType(String timeType, Object gt, Object lt) {
        switch (timeType) {
            case SEARCH_ORDER_TIME_FROM_TO: //1
                return getCriteriaFromTo(DB_FIELD_PROCESS_TIME, gt, lt);
            case SEARCH_ORDER_TIME_FROM: //2
                return getCriteriaFrom(DB_FIELD_PROCESS_TIME, gt);
            case SEARCH_ORDER_TIME_TO: //3
                return getCriteriaTo(DB_FIELD_PROCESS_TIME, lt);
            default:
                return null;
        }
    }

    /**
     * criteria取得
     *
     * @param field
     * @param gt
     * @param lt
     * @return
     */
    private Criteria getCriteriaFromTo(String field, Object gt, Object lt) {
        Criteria criteria = null;
        if (lt != null) {
            criteria = Criteria.where(field).lte(lt);
        }
        if (gt != null) {
            if (criteria == null) {
                criteria = Criteria.where(field).gte(gt);
            } else {
                criteria.gte(gt);
            }
        }
        return criteria;
    }

    /**
     * criteria取得
     *
     * @param field
     * @param gt
     * @return
     */
    private Criteria getCriteriaFrom(String field, Object gt) {
        if (gt != null) {
            return Criteria.where(field).gte(gt);
        }

        return null;
    }

    /**
     * the criteria from
     *
     * @param field
     * @param lt
     * @return
     */
    private Criteria getCriteriaTo(String field, Object lt) {
        if (lt != null) {
            return Criteria.where(field).lte(lt);
        }

        return null;
    }
}