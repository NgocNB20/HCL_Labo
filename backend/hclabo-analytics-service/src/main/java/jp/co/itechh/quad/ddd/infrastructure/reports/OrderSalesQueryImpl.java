package jp.co.itechh.quad.ddd.infrastructure.reports;

import com.mongodb.BasicDBObject;
import jp.co.itechh.quad.core.base.utility.NumberUtility;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesQuery;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesSearchQueryCondition;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 受注・売上集計クエリ―実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Repository
public class OrderSalesQueryImpl implements OrderSalesQuery {

    /**
     * mongoTemplate
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
     * 処理ステータス
     */
    private final static String DB_FIELD_PROCESS_STATUS = "processStatus";

    /**
     * 処理ステータス : NEW
     */
    private final static String PROCESS_STATUS_NEW = "0";

    /**
     * 処理ステータス : CANCEL
     */
    private final static String PROCESS_STATUS_CANCEL = "1";

    /**
     * 処理ステータス : UPDATE
     */
    private final static String PROCESS_STATUS_UPDATE = "2";

    /**
     * 決済方法ID
     */
    private final static String PAYMENT_METHOD_ID = "paymentMethodId";

    /**
     * 集計決済方法ID
     */
    private final static String AGGREGATION_PAY_ID = "aggregationPayId";

    /**
     * 日時
     */
    private final static String PAYMENT_DATE = "date";

    /**
     * 決済方法名
     */
    private final static String PAYMENT_METHOD_NAME = "paymentMethodName";

    /**
     * 決済方法名
     */
    private final static String AGGREGATION_PAY_NAME = "aggregationPayName";

    /**
     * 決済方法ID
     */
    private final static String DB_FIELD_PAYMENT_METHOD_ID = "payment.paymentMethodId";

    /**
     * DBの集計決済方法ID
     */
    private final static String DB_FIELD_AGGREGATION_PAY_ID = "payment.aggregationPayId";

    /**
     * 決済方法名
     */
    private final static String DB_FIELD_PAYMENT_METHOD_NAME = "payment.paymentMethodName";

    /**
     * リンク決済：決済手段識別子
     */
    private final static String DB_FIELD_PAYMENT_METHOD = "payment.payMethod";

    /**
     * リンク決済：手段名
     */
    private final static String DB_FIELD_PAYMENT_TYPE_NAME = "payment.payTypeName";

    /**
     * DB決済方法名
     */
    private final static String DB_FIELD_AGGREGATION_PAY_NAME = "payment.aggregationPayName";

    /**
     * DB決済方法
     */
    private final static String DB_FIELD_PAY_TYPE = "payment.payType";

    /**
     * 決済方法名
     */
    private final static String PAY_TYPE = "payType";

    /**
     * 集計用販売データ
     */
    private final static String COLLECTION_NAME = "reportsQueryModel";

    /**
     * 処理日時 :　月
     */
    private final static String DATE_FORMAT_MONTH = "%Y/%m";

    /**
     * 処理日時 :　日
     */
    private final static String DATE_FORMAT_DAY = "%Y/%m/%d";

    /**
     * 集計単位 : 日
     */
    private final static String AGGREGATE_UNIT_DAY = "1";

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
     * 数値関連ヘルパークラス
     */
    private final NumberUtility numberUtility;

    /**
     * コンストラクタ
     *
     * @param mongoTemplate MongoTemplate
     * @param numberUtility 数値関連ヘルパークラス
     */
    @Autowired
    public OrderSalesQueryImpl(MongoTemplate mongoTemplate, NumberUtility numberUtility) {
        this.mongoTemplate = mongoTemplate;
        this.numberUtility = numberUtility;
    }

    /**
     * 受注・売上集計クエリーモデルリスト取得.
     *
     * @param condition 受注・売上集計用検索条件クラス
     * @return 受注・売上集計クエリーモデルリスト
     */
    @Override
    public List<OrderSalesQueryModel> find(OrderSalesSearchQueryCondition condition) {

        List<AggregationOperation> aggregationOperation = new ArrayList<>();

        createAggregationOperation(aggregationOperation, condition);

        if (condition.getPageInfoForQuery() != null && condition.getPageInfoForQuery().getSort() != null) {
            SortOperation sort = new SortOperation(condition.getPageInfoForQuery().getSort());
            aggregationOperation.add(sort);
        } else {
            aggregationOperation.add(Aggregation.sort(Sort.by(Sort.Direction.ASC, "_id")));
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

        AggregationResults<OrderSalesQueryModel> results =
                        mongoTemplate.aggregate(aggregation, COLLECTION_NAME, OrderSalesQueryModel.class);

        return results.getMappedResults();
    }

    /**
     * ダウンロードStream
     *
     * @param condition 受注・売上集計用検索条件クラス
     * @return
     */
    @Override
    public CloseableIterator<OrderSalesQueryModel> download(OrderSalesSearchQueryCondition condition) {

        List<AggregationOperation> aggregationOperation = new ArrayList<>();

        createAggregationOperation(aggregationOperation, condition);

        SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "_id");
        aggregationOperation.add(sort);

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperation)
                                             .withOptions(AggregationOptions.builder().allowDiskUse(true).build());

        return mongoTemplate.aggregateStream(aggregation, COLLECTION_NAME, OrderSalesQueryModel.class);

    }

    /**
     * AggregationOperation作成
     *
     * @param aggregationOperation AggregationOperation
     * @param condition            商品販売クエリ条件
     */
    private void createAggregationOperation(List<AggregationOperation> aggregationOperation,
                                            OrderSalesSearchQueryCondition condition) {
        Criteria criteria = createCriteria(condition);
        AggregationOperation match = null;
        if (criteria != null) {
            match = Aggregation.match(criteria);
        }

        if (match != null) {
            aggregationOperation.add(match);
        }

        String formatDate = DATE_FORMAT_MONTH;
        if (StringUtils.isNotEmpty(condition.getAggregateUnit()) && condition.getAggregateUnit()
                                                                             .equals(AGGREGATE_UNIT_DAY)) {
            formatDate = DATE_FORMAT_DAY;
        }

        Criteria isNewProcessStatus = Criteria.where(DB_FIELD_PROCESS_STATUS).is(PROCESS_STATUS_NEW);
        Criteria isCancelProcessStatus = Criteria.where(DB_FIELD_PROCESS_STATUS).is(PROCESS_STATUS_CANCEL);
        Criteria isUpdateProcessStatus = Criteria.where(DB_FIELD_PROCESS_STATUS).is(PROCESS_STATUS_UPDATE);
        Criteria isLinkPayment = Criteria.where(DB_FIELD_PAYMENT_METHOD).ne(null);

        AggregationOperation project = Aggregation.project()
                                                  .and(DB_FIELD_PAYMENT_METHOD_ID)
                                                  .as(PAYMENT_METHOD_ID)
                                                  .and(ConditionalOperators.when(isLinkPayment)
                                                                           .thenValueOf(DB_FIELD_PAYMENT_TYPE_NAME)
                                                                           .otherwiseValueOf(
                                                                                           DB_FIELD_PAYMENT_METHOD_NAME))
                                                  .as(PAYMENT_METHOD_NAME)
                                                  .and(DB_FIELD_PAY_TYPE)
                                                  .as(PAY_TYPE)
                                                  .and(DB_FIELD_AGGREGATION_PAY_ID)
                                                  .as(AGGREGATION_PAY_ID)
                                                  .and(DB_FIELD_AGGREGATION_PAY_NAME)
                                                  .as(AGGREGATION_PAY_NAME)
                                                  .and(DateOperators.zonedDateOf(DB_FIELD_PROCESS_TIME,
                                                                                 DateOperators.Timezone.valueOf(
                                                                                                 "Asia/Tokyo")
                                                                                ).toString(formatDate))
                                                  .as("date")
                                                  .and(ConditionalOperators.when(isNewProcessStatus)
                                                                           .then(1)
                                                                           .otherwise(0))
                                                  .as("newOrderCount")
                                                  .and(ConditionalOperators.when(isNewProcessStatus)
                                                                           .thenValueOf("price.itemSalesPriceTotal")
                                                                           .otherwise(0))
                                                  .as("newOrderItemSalesPriceTotal")
                                                  .and(ConditionalOperators.when(isNewProcessStatus)
                                                                           .thenValueOf("price.carriage")
                                                                           .otherwise(0))
                                                  .as("newOrderCarriage")
                                                  .and(ConditionalOperators.when(isNewProcessStatus)
                                                                           .thenValueOf("price.commission")
                                                                           .otherwise(0))
                                                  .as("newOrderCommission")
                                                  .and(ConditionalOperators.when(isNewProcessStatus)
                                                                           .thenValueOf("price.otherPrice")
                                                                           .otherwise(0))
                                                  .as("newOrderOtherPrice")
                                                  .and(ConditionalOperators.when(isNewProcessStatus)
                                                                           .thenValueOf("price.tax")
                                                                           .otherwise(0))
                                                  .as("newOrderTax")
                                                  .and(ConditionalOperators.when(isNewProcessStatus)
                                                                           .thenValueOf("price.couponPaymentPrice")
                                                                           .otherwise(0))
                                                  .as("newOrderCouponPaymentPrice")
                                                  .and(ConditionalOperators.when(isNewProcessStatus)
                                                                           .thenValueOf("price.orderPrice")
                                                                           .otherwise(0))
                                                  .as("newOrderPrice")
                                                  .and(ConditionalOperators.when(isCancelProcessStatus)
                                                                           .then(1)
                                                                           .otherwise(0))
                                                  .as("cancelOrderCount")
                                                  .and(ConditionalOperators.when(isCancelProcessStatus)
                                                                           .thenValueOf("price.itemSalesPriceTotal")
                                                                           .otherwise(0))
                                                  .as("cancelOrderItemSalesPriceTotal")
                                                  .and(ConditionalOperators.when(isCancelProcessStatus)
                                                                           .thenValueOf("price.carriage")
                                                                           .otherwise(0))
                                                  .as("cancelOrderCarriage")
                                                  .and(ConditionalOperators.when(isCancelProcessStatus)
                                                                           .thenValueOf("price.commission")
                                                                           .otherwise(0))
                                                  .as("cancelOrderCommission")
                                                  .and(ConditionalOperators.when(isCancelProcessStatus)
                                                                           .thenValueOf("price.otherPrice")
                                                                           .otherwise(0))
                                                  .as("cancelOrderOtherPrice")
                                                  .and(ConditionalOperators.when(isCancelProcessStatus)
                                                                           .thenValueOf("price.tax")
                                                                           .otherwise(0))
                                                  .as("cancelOrderTax")
                                                  .and(ConditionalOperators.when(isCancelProcessStatus)
                                                                           .thenValueOf("price.couponPaymentPrice")
                                                                           .otherwise(0))
                                                  .as("cancelOrderCouponPaymentPrice")
                                                  .and(ConditionalOperators.when(isCancelProcessStatus)
                                                                           .thenValueOf("price.orderPrice")
                                                                           .otherwise(0))
                                                  .as("cancelOrderPrice")
                                                  .and(ConditionalOperators.when(isUpdateProcessStatus)
                                                                           .thenValueOf("price.itemSalesPriceTotal")
                                                                           .otherwise(0))
                                                  .as("updateOrderItemSalesPriceTotal")
                                                  .and(ConditionalOperators.when(isUpdateProcessStatus)
                                                                           .thenValueOf("price.carriage")
                                                                           .otherwise(0))
                                                  .as("updateOrderCarriage")
                                                  .and(ConditionalOperators.when(isUpdateProcessStatus)
                                                                           .thenValueOf("price.commission")
                                                                           .otherwise(0))
                                                  .as("updateOrderCommission")
                                                  .and(ConditionalOperators.when(isUpdateProcessStatus)
                                                                           .thenValueOf("price.otherPrice")
                                                                           .otherwise(0))
                                                  .as("updateOrderOtherPrice")
                                                  .and(ConditionalOperators.when(isUpdateProcessStatus)
                                                                           .thenValueOf("price.tax")
                                                                           .otherwise(0))
                                                  .as("updateOrderTax")
                                                  .and(ConditionalOperators.when(isUpdateProcessStatus)
                                                                           .thenValueOf("price.couponPaymentPrice")
                                                                           .otherwise(0))
                                                  .as("updateOrderCouponPaymentPrice")
                                                  .and(ConditionalOperators.when(isUpdateProcessStatus)
                                                                           .thenValueOf("price.orderPrice")
                                                                           .otherwise(0))
                                                  .as("updateOrderPrice")
                                                  .and("price.itemSalesPriceTotal")
                                                  .as("subTotalItemSalesPriceTotal")
                                                  .and("price.carriage")
                                                  .as("subTotalCarriage")
                                                  .and("price.commission")
                                                  .as("subTotalCommission")
                                                  .and("price.otherPrice")
                                                  .as("subTotalOtherPrice")
                                                  .and("price.tax")
                                                  .as("subTotalTax")
                                                  .and("price.couponPaymentPrice")
                                                  .as("subTotalCouponPaymentPrice")
                                                  .and("price.orderPrice")
                                                  .as("subTotalOrderPrice");
        aggregationOperation.add(project);

        AggregationOperation aggregationOperationSum =
                        Aggregation.group(PAYMENT_DATE, AGGREGATION_PAY_ID, AGGREGATION_PAY_NAME)
                                   .first(PAYMENT_METHOD_ID)
                                   .as(PAYMENT_METHOD_ID)
                                   .first(PAYMENT_METHOD_NAME)
                                   .as(PAYMENT_METHOD_NAME)
                                   .first(PAY_TYPE)
                                   .as(PAY_TYPE)
                                   .sum("newOrderCount")
                                   .as("newOrderCount")
                                   .sum("newOrderItemSalesPriceTotal")
                                   .as("newOrderItemSalesPriceTotal")
                                   .sum("newOrderCarriage")
                                   .as("newOrderCarriage")
                                   .sum("newOrderCommission")
                                   .as("newOrderCommission")
                                   .sum("newOrderOtherPrice")
                                   .as("newOrderOtherPrice")
                                   .sum("newOrderTax")
                                   .as("newOrderTax")
                                   .sum("newOrderCouponPaymentPrice")
                                   .as("newOrderCouponPaymentPrice")
                                   .sum("newOrderPrice")
                                   .as("newOrderPrice")
                                   .sum("cancelOrderCount")
                                   .as("cancelOrderCount")
                                   .sum("cancelOrderItemSalesPriceTotal")
                                   .as("cancelOrderItemSalesPriceTotal")
                                   .sum("cancelOrderCarriage")
                                   .as("cancelOrderCarriage")
                                   .sum("cancelOrderCommission")
                                   .as("cancelOrderCommission")
                                   .sum("cancelOrderOtherPrice")
                                   .as("cancelOrderOtherPrice")
                                   .sum("cancelOrderTax")
                                   .as("cancelOrderTax")
                                   .sum("cancelOrderCouponPaymentPrice")
                                   .as("cancelOrderCouponPaymentPrice")
                                   .sum("cancelOrderPrice")
                                   .as("cancelOrderPrice")
                                   .sum("updateOrderItemSalesPriceTotal")
                                   .as("updateOrderItemSalesPriceTotal")
                                   .sum("updateOrderCarriage")
                                   .as("updateOrderCarriage")
                                   .sum("updateOrderCommission")
                                   .as("updateOrderCommission")
                                   .sum("updateOrderOtherPrice")
                                   .as("updateOrderOtherPrice")
                                   .sum("updateOrderTax")
                                   .as("updateOrderTax")
                                   .sum("updateOrderCouponPaymentPrice")
                                   .as("updateOrderCouponPaymentPrice")
                                   .sum("updateOrderPrice")
                                   .as("updateOrderPrice")
                                   .sum("subTotalItemSalesPriceTotal")
                                   .as("subTotalItemSalesPriceTotal")
                                   .sum("subTotalCarriage")
                                   .as("subTotalCarriage")
                                   .sum("subTotalCommission")
                                   .as("subTotalCommission")
                                   .sum("subTotalOtherPrice")
                                   .as("subTotalOtherPrice")
                                   .sum("subTotalTax")
                                   .as("subTotalTax")
                                   .sum("subTotalCouponPaymentPrice")
                                   .as("subTotalCouponPaymentPrice")
                                   .sum("subTotalOrderPrice")
                                   .as("subTotalOrderPrice");
        aggregationOperation.add(aggregationOperationSum);

        aggregationOperation.add(Aggregation.sort(Sort.by(Sort.Direction.ASC, "_id", "_id." + AGGREGATION_PAY_ID,
                                                          "_id." + AGGREGATION_PAY_NAME
                                                         )));

        AggregationOperation aggregationOperationGroup = Aggregation.group(PAYMENT_DATE)
                                                                    .push(new BasicDBObject(PAYMENT_METHOD_ID,
                                                                                            "$" + PAYMENT_METHOD_ID
                                                                    ).append(PAYMENT_METHOD_NAME,
                                                                             "$" + PAYMENT_METHOD_NAME
                                                                            )
                                                                     .append(PAY_TYPE, "$" + PAY_TYPE)
                                                                     .append("newOrderCount", "$newOrderCount")
                                                                     .append(
                                                                                     "newOrderItemSalesPriceTotal",
                                                                                     "$newOrderItemSalesPriceTotal"
                                                                            )
                                                                     .append("newOrderCarriage", "$newOrderCarriage")
                                                                     .append(
                                                                                     "newOrderCommission",
                                                                                     "$newOrderCommission"
                                                                            )
                                                                     .append(
                                                                                     "newOrderOtherPrice",
                                                                                     "$newOrderOtherPrice"
                                                                            )
                                                                     .append("newOrderTax", "$newOrderTax")
                                                                     .append(
                                                                                     "newOrderCouponPaymentPrice",
                                                                                     "$newOrderCouponPaymentPrice"
                                                                            )
                                                                     .append("newOrderPrice", "$newOrderPrice")
                                                                     .append("cancelOrderCount", "$cancelOrderCount")
                                                                     .append(
                                                                                     "cancelOrderItemSalesPriceTotal",
                                                                                     "$cancelOrderItemSalesPriceTotal"
                                                                            )
                                                                     .append(
                                                                                     "cancelOrderCarriage",
                                                                                     "$cancelOrderCarriage"
                                                                            )
                                                                     .append(
                                                                                     "cancelOrderCommission",
                                                                                     "$cancelOrderCommission"
                                                                            )
                                                                     .append(
                                                                                     "cancelOrderOtherPrice",
                                                                                     "$cancelOrderOtherPrice"
                                                                            )
                                                                     .append("cancelOrderTax", "$cancelOrderTax")
                                                                     .append(
                                                                                     "cancelOrderCouponPaymentPrice",
                                                                                     "$cancelOrderCouponPaymentPrice"
                                                                            )
                                                                     .append("cancelOrderPrice", "$cancelOrderPrice")
                                                                     .append(
                                                                                     "updateOrderItemSalesPriceTotal",
                                                                                     "$updateOrderItemSalesPriceTotal"
                                                                            )
                                                                     .append(
                                                                                     "updateOrderCarriage",
                                                                                     "$updateOrderCarriage"
                                                                            )
                                                                     .append(
                                                                                     "updateOrderCommission",
                                                                                     "$updateOrderCommission"
                                                                            )
                                                                     .append(
                                                                                     "updateOrderOtherPrice",
                                                                                     "$updateOrderOtherPrice"
                                                                            )
                                                                     .append("updateOrderTax", "$updateOrderTax")
                                                                     .append(
                                                                                     "updateOrderCouponPaymentPrice",
                                                                                     "$updateOrderCouponPaymentPrice"
                                                                            )
                                                                     .append("updateOrderPrice", "$updateOrderPrice")
                                                                     .append(
                                                                                     "subTotalItemSalesPriceTotal",
                                                                                     "$subTotalItemSalesPriceTotal"
                                                                            )
                                                                     .append("subTotalCarriage", "$subTotalCarriage")
                                                                     .append(
                                                                                     "subTotalCommission",
                                                                                     "$subTotalCommission"
                                                                            )
                                                                     .append(
                                                                                     "subTotalOtherPrice",
                                                                                     "$subTotalOtherPrice"
                                                                            )
                                                                     .append("subTotalTax", "$subTotalTax")
                                                                     .append(
                                                                                     "subTotalCouponPaymentPrice",
                                                                                     "$subTotalCouponPaymentPrice"
                                                                            )
                                                                     .append(
                                                                                     "subTotalOrderPrice",
                                                                                     "$subTotalOrderPrice"
                                                                            ))
                                                                    .as("dataList");

        aggregationOperation.add(aggregationOperationGroup);
    }

    /**
     * criteriaを作成
     *
     * @param condition 受注・売上集計クエリ条件
     * @return criteria
     */
    private Criteria createCriteria(OrderSalesSearchQueryCondition condition) {

        Criteria criteria = new Criteria();
        List<Criteria> expression = new ArrayList<>();

        String timeType;
        if (condition.getAggregateTimeFrom() != null && condition.getAggregateTimeTo() == null) {
            timeType = SEARCH_ORDER_TIME_FROM;
        } else if (condition.getAggregateTimeFrom() == null && condition.getAggregateTimeTo() != null) {
            timeType = SEARCH_ORDER_TIME_TO;
        } else {
            timeType = SEARCH_ORDER_TIME_FROM_TO;
        }

        createFromToQuery(expression, timeType, condition.getAggregateTimeFrom(), condition.getAggregateTimeTo());

        if (CollectionUtils.isNotEmpty(condition.getOrderDeviceList())) {
            Criteria searchOrderDevice = Criteria.where(DB_FIELD_ORDER_DEVICE).in(condition.getOrderDeviceList());
            expression.add(searchOrderDevice);
        }

        if (StringUtils.isNotEmpty(condition.getOrderStatus())) {
            Criteria searchOrderStatus = Criteria.where(DB_FIELD_ORDER_STATUS).is(condition.getOrderStatus());
            expression.add(searchOrderStatus);
        }

        if (expression.size() <= 0 && CollectionUtils.isEmpty(condition.getPaymentMethodIdList())) {
            return null;
        } else if (expression.size() > 0) {
            criteria = new Criteria().andOperator(expression.toArray(new Criteria[0]));
        }

        if (CollectionUtils.isNotEmpty(condition.getPaymentMethodIdList())) {

            List<Integer> payMethodIdList = new ArrayList<>();
            List<String> payMethod = new ArrayList<>();
            for (String paymentMethodId : condition.getPaymentMethodIdList()) {
                if (numberUtility.isNumber(paymentMethodId)) {
                    payMethodIdList.add(Integer.parseInt(paymentMethodId));
                } else {
                    payMethod.add(paymentMethodId);
                }
            }

            criteria.orOperator(Criteria.where(DB_FIELD_PAYMENT_METHOD_ID).in(payMethodIdList),
                                Criteria.where(DB_FIELD_PAYMENT_METHOD).in(payMethod)
                               );

        }

        return criteria;
    }

    /**
     * クライテリア（from~to）を作成する
     *
     * @param expression クライテリアリスト
     * @param timeType   期間
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
     * CriteriaTimeタイプ取得
     *
     * @param timeType the time type
     * @param gt       Object
     * @param lt       Object
     * @return the criteria time type
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
     * CriteriaFromTo取得
     *
     * @param field the field
     * @param gt    the gt
     * @param lt    the lt
     * @return the criteria from to
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
     * CriteriaFrom取得
     *
     * @param field the field
     * @param gt    the gt
     * @return the criteria from
     */
    private Criteria getCriteriaFrom(String field, Object gt) {
        if (gt != null) {
            return Criteria.where(field).gte(gt);
        }

        return null;
    }

    /**
     * CriteriaTo取得
     *
     * @param field the field
     * @param lt    the lt
     * @return the criteria to
     */
    private Criteria getCriteriaTo(String field, Object lt) {
        if (lt != null) {
            return Criteria.where(field).lte(lt);
        }

        return null;
    }

}