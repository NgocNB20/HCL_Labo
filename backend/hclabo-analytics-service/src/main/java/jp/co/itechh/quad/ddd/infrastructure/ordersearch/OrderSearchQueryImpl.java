package jp.co.itechh.quad.ddd.infrastructure.ordersearch;

import jp.co.itechh.quad.core.constant.type.HTypeOrderStatusDdd;
import jp.co.itechh.quad.core.dto.order.OrderCSVDto;
import jp.co.itechh.quad.core.dto.shipment.ShipmentCSVDto;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.CountOrderProductModel;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQuery;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

/**
 * 受注検索クエリ―実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Repository
public class OrderSearchQueryImpl implements OrderSearchQuery {

    private final MongoTemplate mongoTemplate;

    private final static String DB_FIELD_ORDER_CODE = "_id";

    private final static String DB_FIELD_ORDER_STATUS = "orderStatus";

    private final static String DB_FIELD_EMERGENCY_FLAG = "emergencyFlag";

    private final static String DB_FIELD_CANCEL_FLAG = "cancelFlag";

    private final static String DB_FIELD_SHIPMENT_STATUS = "shipmentStatus";

    private final static String DB_FIELD_SEARCH_NAME_EM_UC = "searchNameEmUc";

    private final static String DB_FIELD_SEARCH_TEL_UC = "searchTelEn";

    private final static String DB_FIELD_CUSTOMER_MAIL = "customerMail";

    private final static String DB_FIELD_ORDER_PRICE = "orderPrice";

    private final static String DB_FIELD_ORDER_CUSTOMER_ID = "customerId";

    private final static String DB_FIELD_GOODS_GROUP_CODE = "orderProductList.goodsGroupCode";

    private final static String DB_FIELD_GOODS_CODE = "orderProductList.goodsCode";

    private final static String DB_FIELD_GOODS_GROUP_NAME = "orderProductList.goodsGroupName";

    private final static String DB_FIELD_UNIT1 = "orderProductList.unitValue1";

    private final static String DB_FIELD_UNIT2 = "orderProductList.unitValue2";

    private final static String DB_FIELD_JAN_CODE = "orderProductList.janCode";

    private final static String DB_FIELD_EXAMKIT_CODE = "orderProductList.examKitCode";

    private final static String DB_FIELD_EXAM_STATUS = "orderProductList.examStatus";

    private final static String DB_FIELD_SPECIMEN_CODE = "orderProductList.specimenCode";

    private final static String DB_FIELD_RECEPTION_DATE = "orderProductList.receptionDate";

    private final static String DB_FIELD_DELIVERY_METHOD = "shippingMethodId";

    private final static String DB_FIELD_DELIVERY_STATUS_CONFIRMATION_NO = "deliveryStatusConfirmationNo";

    private final static String DB_FIELD_SETTLEMENT_METHOD = "paymentMethodId";

    private final static String DB_FIELD_ORDER_TIME = "orderTime";

    private final static String DB_FIELD_CANCEL_TIME = "cancelTime";

    private final static String DB_FIELD_SHIPMENT_DATE = "shipmentDate";

    private final static String DB_FIELD_PAYMENT_DATE_AND_TIME = "paymentDateAndTime";

    private final static String DB_FIELD_PROCESS_TIME = "processTime";

    private final static String DB_FIELD_PAYMENT_TIME_LIMIT_DATE = "paymentTimeLimitDate";

    private final static String DB_FIELD_RECEIVER_DATE = "receiverDate";

    private final static String DB_FIELD_NOVELTY_PRESENT_JUDGMENT_STATUS = "noveltyPresentJudgmentStatus";

    private final static String COLLECTION_NAME = "orderSearchQueryModel";

    private final static String SEARCH_ORDER_TIME_FROM_TO = "1";

    private final static String SEARCH_ORDER_TIME_FROM = "2";

    private final static String SEARCH_ORDER_TIME_TO = "3";

    private final static String SEARCH_SHIPMENT_DATE_FROM_TO = "4";

    private final static String SEARCH_SHIPMENT_DATE_FROM = "5";

    private final static String SEARCH_SHIPMENT_DATE_TO = "6";

    private final static String SEARCH_RECEIPT_YMD_FROM_TO = "7";

    private final static String SEARCH_RECEIPT_YMD_FROM = "8";

    private final static String SEARCH_RECEIPT_YMD_TO = "9";

    private final static String SEARCH_PROCESS_TIME_FROM_TO = "13";

    private final static String SEARCH_PROCESS_TIME_FROM = "14";

    private final static String SEARCH_PROCESS_TIME_TO = "15";

    private final static String SEARCH_PAYMENT_TIME_LIMIT_DATE_FROM_TO = "16";

    private final static String SEARCH_PAYMENT_TIME_LIMIT_DATE_FROM = "17";

    private final static String SEARCH_PAYMENT_TIME_LIMIT_DATE_TO = "18";

    private final static String SEARCH_RECEIVER_DATE_FROM_TO = "19";

    private final static String SEARCH_RECEIVER_DATE_FROM = "20";

    private final static String SEARCH_RECEIVER_DATE_TO = "21";

    private final static String SEARCH_CANCEL_TIME_FROM_TO = "22";

    private final static String SEARCH_CANCEL_TIME_FROM = "23";

    private final static String SEARCH_CANCEL_TIME_TO = "24";

    private final static String SEARCH_RECEPTION_DATE_FROM_TO = "25";

    private final static String SEARCH_RECEPTION_DATE_FROM = "26";

    private final static String SEARCH_RECEPTION_DATE_TO = "27";

    private final static String JAVA_FIELD_ORDER_CODE = "orderCode";

    /**
     * 正規表現 start
     */
    private final static String REGEX_ESCAPE_START = ".*\\Q";

    /**
     * 正規表現 end
     */
    private final static String REGEX_ESCAPE_END = "\\E.*";

    /**
     * Instantiates a new Order search query.
     *
     * @param mongoTemplate the mongo template
     */
    @Autowired
    public OrderSearchQueryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 受注検索クエリーモデルリスト取得.
     *
     * @param condition 受注検索条件
     * @return 受注検索クエリーモデルリスト
     */
    @Override
    public List<OrderSearchQueryModel> find(OrderSearchQueryCondition condition) {

        Criteria criteria = createCriteria(condition);
        AggregationOperation match = null;
        if (criteria != null) {
            match = Aggregation.match(criteria);
        }

        List<AggregationOperation> aggregationOperation = new ArrayList<>();
        if (match != null) {
            aggregationOperation.add(match);
        }

        //        aggregationOperation.add(Aggregation.match(StringOperators.RegexMatch.valueOf(DB_FIELD_CUSTOMER_MAIL).regex(condition.getCustomerMail()).options("i")));

        if (condition.getPageInfoForQuery().getSort() != null) {
            SortOperation sort = new SortOperation(condition.getPageInfoForQuery().getSort());
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

        AggregationResults<OrderSearchQueryModel> results =
                        mongoTemplate.aggregate(aggregation, COLLECTION_NAME, OrderSearchQueryModel.class);

        return results.getMappedResults();
    }

    /**
     * 検索キーワード集計ダウンロード
     *
     * @param condition 検索キーワード集計
     * @return 検索キーワード集計クエリーモデル CloseableIterator
     */
    @Override
    public CloseableIterator<OrderCSVDto> download(OrderSearchQueryCondition condition) {

        AssertChecker.assertNotNull("condition is null", condition);

        Criteria criteria = createCriteria(condition);
        AggregationOperation match = null;
        if (criteria != null) {
            match = Aggregation.match(criteria);
        }

        List<AggregationOperation> aggregationOperation = new ArrayList<>();
        if (match != null) {
            aggregationOperation.add(match);
        }

        AggregationOperation unwind = unwind("orderProductList");
        aggregationOperation.add(unwind);

        //「受注商品単位で絞り込む」チェックボックスがONの場合、再度matchを実行する
        if (condition.getFilterOrderedProductFlag() && match != null) {
            aggregationOperation.add(match);
        }

        if (condition.getPageInfoForQuery() != null && condition.getPageInfoForQuery().getSort() != null) {
            SortOperation sort = new SortOperation(condition.getPageInfoForQuery().getSort());
            aggregationOperation.add(sort);
        }

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperation)
                                             .withOptions(AggregationOptions.builder()
                                                                            .allowDiskUse(true)
                                                                            .cursorBatchSize(1000)
                                                                            .build());

        return mongoTemplate.aggregateStream(aggregation, COLLECTION_NAME, OrderCSVDto.class);
    }

    /**
     * 検索キーワード集計ダウンロード
     *
     * @param condition 検索キーワード集計
     * @return 検索キーワード集計クエリーモデル CloseableIterator
     */
    @Override
    public CloseableIterator<ShipmentCSVDto> shipment(OrderSearchQueryCondition condition) {

        AssertChecker.assertNotNull("condition is null", condition);

        Criteria criteria = createCriteria(condition);
        AggregationOperation match = null;
        if (criteria != null) {
            match = Aggregation.match(criteria);
        }

        List<AggregationOperation> aggregationOperation = new ArrayList<>();
        if (match != null) {
            aggregationOperation.add(match);
        }

        if (condition.getPageInfoForQuery() != null && condition.getPageInfoForQuery().getSort() != null) {
            SortOperation sort = new SortOperation(condition.getPageInfoForQuery().getSort());
            aggregationOperation.add(sort);
        }

        AggregationOperation projectForOrderCode = Aggregation.project().and("_id").as("_id");
        aggregationOperation.add(projectForOrderCode);

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperation)
                                             .withOptions(AggregationOptions.builder()
                                                                            .allowDiskUse(true)
                                                                            .cursorBatchSize(1000)
                                                                            .build());

        return mongoTemplate.aggregateStream(aggregation, COLLECTION_NAME, ShipmentCSVDto.class);
    }

    /**
     * 合計レコーダーを数える
     *
     * @param condition 受注検索条件
     * @return 合計記録
     */
    public int count(OrderSearchQueryCondition condition) {

        Criteria criteria = createCriteria(condition);

        AggregationOperation match = null;
        if (criteria != null) {
            match = Aggregation.match(criteria);
        }

        List<AggregationOperation> aggregationOperation = new ArrayList<>();
        if (match != null) {
            aggregationOperation.add(match);
        }

        CountOperation count = Aggregation.count().as("totalRecords");
        aggregationOperation.add(count);

        Aggregation aggregation = Aggregation.newAggregation(aggregationOperation)
                                             .withOptions(AggregationOptions.builder().allowDiskUse(true).build());

        Map results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, Map.class).getUniqueMappedResult();

        if (results != null && results.containsKey("totalRecords")) {
            return (int) results.get("totalRecords");
        }

        return 0;
    }

    /**
     * 売上商品数カウント
     *
     * @param goodsGroupCode
     * @param timeFrom
     * @return 売上商品数
     */
    @Override
    public int countOrderProduct(String goodsGroupCode, Timestamp timeFrom) {

        // criteria作成
        List<Criteria> expression = new ArrayList<>();
        // 商品グループ番号
        expression.add(Criteria.where(DB_FIELD_GOODS_GROUP_CODE).is(goodsGroupCode));
        // 期間
        expression.add(getCriteriaFrom(DB_FIELD_ORDER_TIME, timeFrom));
        // 受注キャンセル以外
        expression.add(Criteria.where(DB_FIELD_ORDER_STATUS).ne(HTypeOrderStatusDdd.CANCEL.getValue()));
        Criteria criteria = new Criteria().andOperator(expression.toArray(new Criteria[0]));

        // matchオペレーション作成
        AggregationOperation match = Aggregation.match(criteria);
        List<AggregationOperation> aggregationOperation = new ArrayList<>();
        aggregationOperation.add(match);

        // goodsGroupCodeのオペレーション作成
        AggregationOperation unwind = unwind("orderProductList");
        aggregationOperation.add(unwind);
        AggregationOperation projectForGoodsGroupCodeSearch = Aggregation.project()
                                                                         .and("orderProductList.goodsGroupCode")
                                                                         .as("goodsGroupCode")
                                                                         .and("orderProductList.goodsCount")
                                                                         .as("goodsCount");
        aggregationOperation.add(projectForGoodsGroupCodeSearch);

        // 商品グループ番号のオペレーション作成
        List<Criteria> expression2 = new ArrayList<>();
        expression2.add(Criteria.where("goodsGroupCode").is(goodsGroupCode));
        Criteria criteria2 = new Criteria().andOperator(expression2.toArray(new Criteria[0]));
        AggregationOperation match2 = Aggregation.match(criteria2);
        aggregationOperation.add(match2);

        // グルーピングする
        AggregationOperation group = Aggregation.group("goodsGroupCode").sum("goodsCount").as("goodsCount");
        aggregationOperation.add(group);

        // Aggregation作成
        Aggregation aggregation = Aggregation.newAggregation(aggregationOperation);

        // 検索実行
        AggregationResults<CountOrderProductModel> results =
                        mongoTemplate.aggregate(aggregation, COLLECTION_NAME, CountOrderProductModel.class);
        if (results.getMappedResults().size() > 0) {
            return (int) results.getMappedResults().get(0).getGoodsCount();
        } else {
            return 0;
        }
    }

    /**
     * Create criteria criteria.
     *
     * @param condition the condition
     * @return the criteria
     */
    private Criteria createCriteria(OrderSearchQueryCondition condition) {

        List<Criteria> expression = new ArrayList<>();

        if (StringUtils.isNotEmpty(condition.getOrderCode())) {
            Criteria searchOrderCode = Criteria.where(DB_FIELD_ORDER_CODE).regex(condition.getOrderCode());
            expression.add(searchOrderCode);
        }
        if (StringUtils.isNotEmpty(condition.getOrderStatus())) {
            Criteria searchOrderStatus;
            if (HTypeOrderStatusDdd.OTHER.getValue().equals(condition.getOrderStatus())) {
                searchOrderStatus = Criteria.where(DB_FIELD_ORDER_STATUS).ne(HTypeOrderStatusDdd.CANCEL.getValue());
            } else {
                searchOrderStatus = Criteria.where(DB_FIELD_ORDER_STATUS).is(condition.getOrderStatus());
            }

            expression.add(searchOrderStatus);
        }
        if (StringUtils.isNotEmpty(condition.getEmergencyFlag())) {
            Criteria searchEmergencyFlag = Criteria.where(DB_FIELD_EMERGENCY_FLAG).is(condition.getEmergencyFlag());
            expression.add(searchEmergencyFlag);
        }
        if (StringUtils.isNotEmpty(condition.getCancelFlag())) {
            Criteria searchCancelFlag = Criteria.where(DB_FIELD_CANCEL_FLAG).is(condition.getCancelFlag());
            expression.add(searchCancelFlag);
        }

        createFromToQuery(expression, condition.getTimeType(), condition.getTimeFrom(), condition.getTimeTo());

        if (StringUtils.isNotEmpty(condition.getCustomerId())) {
            Criteria searchCustomerId =
                            Criteria.where(DB_FIELD_ORDER_CUSTOMER_ID).is(Integer.parseInt(condition.getCustomerId()));
            expression.add(searchCustomerId);
        }

        if (StringUtils.isNotEmpty(condition.getSearchNameEmUc())) {
            Criteria searchNameEmUc = Criteria.where(DB_FIELD_SEARCH_NAME_EM_UC).regex(condition.getSearchNameEmUc());
            expression.add(searchNameEmUc);
        }

        if (StringUtils.isNotEmpty(condition.getSearchTelEn())) {
            Criteria searchTelEn = Criteria.where(DB_FIELD_SEARCH_TEL_UC).regex(condition.getSearchTelEn());
            expression.add(searchTelEn);
        }

        if (StringUtils.isNotEmpty(condition.getOrderPriceFrom())) {
            Criteria searchOrderPriceFrom =
                            Criteria.where(DB_FIELD_ORDER_PRICE).gte(Integer.parseInt(condition.getOrderPriceFrom()));
            expression.add(searchOrderPriceFrom);
        }

        if (StringUtils.isNotEmpty(condition.getOrderPriceTo())) {
            Criteria searchOrderPriceTo =
                            Criteria.where(DB_FIELD_ORDER_PRICE).lte(Integer.parseInt(condition.getOrderPriceTo()));
            expression.add(searchOrderPriceTo);
        }

        if (StringUtils.isNotEmpty(condition.getCustomerMail())) {
            Criteria searchCustomerMail = Criteria.where(DB_FIELD_CUSTOMER_MAIL)
                                                  .regex(REGEX_ESCAPE_START + condition.getCustomerMail()
                                                         + REGEX_ESCAPE_END, "i");
            expression.add(searchCustomerMail);
        }

        if (StringUtils.isNotEmpty(condition.getGoodsGroupCode())) {
            Criteria searchGoodsGroupCode =
                            Criteria.where(DB_FIELD_GOODS_GROUP_CODE).regex(condition.getGoodsGroupCode());
            expression.add(searchGoodsGroupCode);
        }

        if (StringUtils.isNotEmpty(condition.getGoodsCode())) {
            Criteria searchGoodsCode = Criteria.where(DB_FIELD_GOODS_CODE).regex(condition.getGoodsCode());
            expression.add(searchGoodsCode);
        }

        if (StringUtils.isNotEmpty(condition.getGoodsGroupName())) {
            Criteria searchGoodsGroupName = new Criteria().orOperator(
                            Criteria.where(DB_FIELD_GOODS_GROUP_NAME)
                                    .regex(REGEX_ESCAPE_START + condition.getGoodsGroupName() + REGEX_ESCAPE_END, "i"),
                            Criteria.where(DB_FIELD_UNIT1)
                                    .regex(REGEX_ESCAPE_START + condition.getGoodsGroupName() + REGEX_ESCAPE_END, "i"),
                            Criteria.where(DB_FIELD_UNIT2)
                                    .regex(REGEX_ESCAPE_START + condition.getGoodsGroupName() + REGEX_ESCAPE_END, "i")
                                                                     );
            expression.add(searchGoodsGroupName);
        }

        if (StringUtils.isNotEmpty(condition.getJanCode())) {
            Criteria searchJanCode = Criteria.where(DB_FIELD_JAN_CODE).regex(condition.getJanCode());
            expression.add(searchJanCode);
        }

        if (CollectionUtils.isNotEmpty(condition.getShipmentStatus())) {
            Criteria searchShipmentStatus = Criteria.where(DB_FIELD_SHIPMENT_STATUS).in(condition.getShipmentStatus());
            expression.add(searchShipmentStatus);
        }

        if (StringUtils.isNotEmpty(condition.getDeliveryMethod())) {
            Criteria searchDeliveryMethod = Criteria.where(DB_FIELD_DELIVERY_METHOD)
                                                    .is(Integer.parseInt(condition.getDeliveryMethod()));
            expression.add(searchDeliveryMethod);
        }

        if (StringUtils.isNotEmpty(condition.getDeliveryCode())) {
            Criteria searchDeliveryStatusConfirmationNo =
                            Criteria.where(DB_FIELD_DELIVERY_STATUS_CONFIRMATION_NO).is(condition.getDeliveryCode());
            expression.add(searchDeliveryStatusConfirmationNo);
        }

        if (StringUtils.isNotEmpty(condition.getSettlememntMethod())) {
            Criteria searchSettlementMethod = Criteria.where(DB_FIELD_SETTLEMENT_METHOD)
                                                      .is(Integer.parseInt(condition.getSettlememntMethod()));
            expression.add(searchSettlementMethod);
        }

        if (CollectionUtils.isNotEmpty(condition.getOrderCodeList())) {
            Criteria searchOrderCodeList = Criteria.where(DB_FIELD_ORDER_CODE).in(condition.getOrderCodeList());
            expression.add(searchOrderCodeList);
        }

        if (StringUtils.isNotEmpty(condition.getNoveltyPresentJudgmentStatus())) {
            Criteria searchNoveltyPresentJudgmentStatus = Criteria.where(DB_FIELD_NOVELTY_PRESENT_JUDGMENT_STATUS)
                                                                  .is(condition.getNoveltyPresentJudgmentStatus());
            expression.add(searchNoveltyPresentJudgmentStatus);
        }

        if (CollectionUtils.isNotEmpty(condition.getExamKitCodeList())) {
            Criteria searchExamKitCodeList = Criteria.where(DB_FIELD_EXAMKIT_CODE).in(condition.getExamKitCodeList());
            expression.add(searchExamKitCodeList);
        }

        if (StringUtils.isNotEmpty(condition.getExamStatus())) {
            Criteria searchExamStatus = Criteria.where(DB_FIELD_EXAM_STATUS).is(condition.getExamStatus());
            expression.add(searchExamStatus);
        }

        if (StringUtils.isNotEmpty(condition.getSpecimenCode())) {
            Criteria searchSpecimenCode = Criteria.where(DB_FIELD_SPECIMEN_CODE).is(condition.getSpecimenCode());
            expression.add(searchSpecimenCode);
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
     * Gets criteria time type.
     *
     * @param timeType the time type
     * @param gt       Object
     * @param lt       Object
     * @return the criteria time type
     */
    private Criteria getCriteriaTimeType(String timeType, Object gt, Object lt) {
        switch (timeType) {
            case SEARCH_ORDER_TIME_FROM_TO: //1
                return getCriteriaFromTo(DB_FIELD_ORDER_TIME, gt, lt);
            case SEARCH_ORDER_TIME_FROM: //2
                return getCriteriaFrom(DB_FIELD_ORDER_TIME, gt);
            case SEARCH_ORDER_TIME_TO: //3
                return getCriteriaTo(DB_FIELD_ORDER_TIME, lt);
            case SEARCH_SHIPMENT_DATE_FROM_TO: //4
                return getCriteriaFromTo(DB_FIELD_SHIPMENT_DATE, gt, lt);
            case SEARCH_SHIPMENT_DATE_FROM: //5
                return getCriteriaFrom(DB_FIELD_SHIPMENT_DATE, gt);
            case SEARCH_SHIPMENT_DATE_TO: //6
                return getCriteriaTo(DB_FIELD_SHIPMENT_DATE, lt);
            case SEARCH_RECEIPT_YMD_FROM_TO: //7
                return getCriteriaFromTo(DB_FIELD_PAYMENT_DATE_AND_TIME, gt, lt);
            case SEARCH_RECEIPT_YMD_FROM: //8
                return getCriteriaFrom(DB_FIELD_PAYMENT_DATE_AND_TIME, gt);
            case SEARCH_RECEIPT_YMD_TO: //9
                return getCriteriaTo(DB_FIELD_PAYMENT_DATE_AND_TIME, lt);
            case SEARCH_PROCESS_TIME_FROM_TO: //13
                return getCriteriaFromTo(DB_FIELD_PROCESS_TIME, gt, lt);
            case SEARCH_PROCESS_TIME_FROM: //14
                return getCriteriaFrom(DB_FIELD_PROCESS_TIME, gt);
            case SEARCH_PROCESS_TIME_TO: //15
                return getCriteriaTo(DB_FIELD_PROCESS_TIME, lt);
            case SEARCH_PAYMENT_TIME_LIMIT_DATE_FROM_TO: //16
                return getCriteriaFromTo(DB_FIELD_PAYMENT_TIME_LIMIT_DATE, gt, lt);
            case SEARCH_PAYMENT_TIME_LIMIT_DATE_FROM: //17
                return getCriteriaFrom(DB_FIELD_PAYMENT_TIME_LIMIT_DATE, gt);
            case SEARCH_PAYMENT_TIME_LIMIT_DATE_TO: //18
                return getCriteriaTo(DB_FIELD_PAYMENT_TIME_LIMIT_DATE, lt);
            case SEARCH_RECEIVER_DATE_FROM_TO: // 19
                return getCriteriaFromTo(DB_FIELD_RECEIVER_DATE, gt, lt);
            case SEARCH_RECEIVER_DATE_FROM: //20
                return getCriteriaFrom(DB_FIELD_RECEIVER_DATE, gt);
            case SEARCH_RECEIVER_DATE_TO: //21
                return getCriteriaTo(DB_FIELD_RECEIVER_DATE, lt);
            case SEARCH_CANCEL_TIME_FROM_TO: // 22
                return getCriteriaFromTo(DB_FIELD_CANCEL_TIME, gt, lt);
            case SEARCH_CANCEL_TIME_FROM: //23
                return getCriteriaFrom(DB_FIELD_CANCEL_TIME, gt);
            case SEARCH_CANCEL_TIME_TO: //24
                return getCriteriaTo(DB_FIELD_CANCEL_TIME, lt);
            case SEARCH_RECEPTION_DATE_FROM_TO: //25
                return getCriteriaFromTo(DB_FIELD_RECEPTION_DATE, gt, lt);
            case SEARCH_RECEPTION_DATE_FROM: //26
                return getCriteriaFrom(DB_FIELD_RECEPTION_DATE, gt);
            case SEARCH_RECEPTION_DATE_TO: //27
                return getCriteriaTo(DB_FIELD_RECEPTION_DATE, lt);
            default:
                return null;
        }
    }

    /**
     * Gets criteria from to.
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
     * Gets criteria from.
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
     * Gets criteria to.
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
