package jp.co.itechh.quad.ddd.presentation.ordersearch.api;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.dto.order.OrderCSVDto;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import jp.co.itechh.quad.ddd.infrastructure.utility.PageInfoForResponse;
import jp.co.itechh.quad.ddd.infrastructure.utility.PageInfoModule;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchCsvOptionQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryModel;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OptionContent;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvGetRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionListResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryModelListResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryModelResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.PageInfoResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 受注検索Helperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSearchHelper {

    /**
     * 変換Helper
     */
    private final ConversionUtility conversionUtility;

    /**
     * ページ情報モジュール
     */
    private final PageInfoModule pageInfoModule;

    private final static String DB_FIELD_ORDER_CODE = "_id";

    private final static String DB_FIELD_CUSTOMER_MAIL = "customerMail";

    private final static String DB_FIELD_GOODS_GROUP_CODE = "orderProductList.goodsGroupCode";

    private final static String DB_FIELD_GOODS_CODE = "orderProductList.goodsCode";

    private final static String DB_FIELD_GOODS_GROUP_NAME = "orderProductList.goodsGroupName";

    private final static String DB_FIELD_JAN_CODE = "orderProductList.janCode";

    private final static String DB_FIELD_DELIVERY_METHOD = "shippingMethodId";

    private final static String DB_FIELD_SETTLEMENT_METHOD = "paymentMethodId";

    private final static String DB_FIELD_ORDER_STATUS = "orderStatus";

    public final static String DEFAULT_VALUE_INTEGER = "0";

    private final static Map<String, String> DB_FIELD_ORDER_BY_CONVERT_MAP = initDbFieldOrderByCommentMap();

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換Helper
     * @param pageInfoModule    ページ情報モジュール
     */
    @Autowired
    public OrderSearchHelper(ConversionUtility conversionUtility, PageInfoModule pageInfoModule) {
        this.conversionUtility = conversionUtility;
        this.pageInfoModule = pageInfoModule;
    }

    /**
     * 受注検索条件に変換.
     *
     * @param queryRequest 受注検索条件
     * @param pageInfo     ページ情報リクエスト（ページネーションのため）
     * @return 受注検索条件
     */
    public OrderSearchQueryCondition toOrderSearchQueryCondition(OrderSearchQueryRequest queryRequest,
                                                                 PageInfoRequest pageInfo) {

        OrderSearchQueryCondition queryCondition = new OrderSearchQueryCondition();
        queryCondition.setOrderCode(queryRequest.getOrderCode());
        queryCondition.setOrderStatus(queryRequest.getOrderStatus());
        queryCondition.setEmergencyFlag(queryRequest.getEmergencyFlag());
        queryCondition.setCancelFlag(queryRequest.getCancelFlag());
        queryCondition.setTimeType(queryRequest.getTimeType());
        queryCondition.setTimeFrom(conversionUtility.toTimestamp(queryRequest.getTimeFrom()));
        queryCondition.setTimeTo(conversionUtility.toTimestamp(queryRequest.getTimeTo()));
        queryCondition.setSearchNameEmUc(queryRequest.getSearchName());
        queryCondition.setSearchTelEn(queryRequest.getSearchTel());
        queryCondition.setCustomerMail(queryRequest.getCustomerMail());
        queryCondition.setGoodsGroupCode(queryRequest.getGoodsGroupCode());
        queryCondition.setGoodsCode(queryRequest.getGoodsCode());
        queryCondition.setGoodsGroupName(queryRequest.getGoodsGroupName());
        queryCondition.setJanCode(queryRequest.getJanCode());
        queryCondition.setShipmentStatus(queryRequest.getShipmentStatus());
        queryCondition.setDeliveryMethod(queryRequest.getDeliveryMethod());
        queryCondition.setDeliveryCode(queryRequest.getDeliveryCode());
        queryCondition.setSettlememntMethod(queryRequest.getSettlememntMethod());
        queryCondition.setOrderCodeList(queryRequest.getOrderCodeList());
        queryCondition.setOrderPriceFrom(queryRequest.getOrderPriceFrom());
        queryCondition.setOrderPriceTo(queryRequest.getOrderPriceTo());
        queryCondition.setExamStatus(queryRequest.getExamStatus());
        queryCondition.setSpecimenCode(queryRequest.getSpecimenCode());
        queryCondition.setExamKitCodeList(queryRequest.getExamKitCodeList());

        if (queryRequest.getCustomerId() != null) {
            queryCondition.setCustomerId(queryRequest.getCustomerId().toString());
        }

        if (queryRequest.getNoveltyPresentJudgmentStatus() != null) {
            queryCondition.setNoveltyPresentJudgmentStatus(queryRequest.getNoveltyPresentJudgmentStatus());
        }

        if (pageInfo != null) {
            if (pageInfo.getOrderBy() != null) {
                String orderBy = this.convertOrderBy(pageInfo.getOrderBy());
                pageInfo.setOrderBy(orderBy);
            }
            queryCondition.setPageInfoForQuery(
                            pageInfoModule.setPageInfoForQuery(pageInfo.getPage(), pageInfo.getLimit(),
                                                               pageInfo.getOrderBy(), pageInfo.getSort()
                                                              ));
        }

        return queryCondition;
    }

    /**
     * 受注検索クエリーモデルリストレスポンスに変換.
     *
     * @param orderSearchQueryModelList 受注検索クエリーモデルリスト
     * @param pageInfoRequest           ページ情報リクエスト（ページネーションのため）
     * @param count                     合計レコーダー
     * @return 受注検索クエリーモデルリストレスポンス
     */
    public OrderSearchQueryModelListResponse toOrderSearchQueryModelResponse(List<OrderSearchQueryModel> orderSearchQueryModelList,
                                                                             PageInfoRequest pageInfoRequest,
                                                                             int count)
                    throws InvocationTargetException, IllegalAccessException {

        OrderSearchQueryModelListResponse orderListResponse = new OrderSearchQueryModelListResponse();

        if (orderSearchQueryModelList == null) {
            return orderListResponse;
        }

        List<OrderSearchQueryModelResponse> orderSearchQueryModelResponseList = new ArrayList<>();

        for (OrderSearchQueryModel orderSearchQueryModel : orderSearchQueryModelList) {
            OrderSearchQueryModelResponse orderResponse = new OrderSearchQueryModelResponse();
            BeanUtils.copyProperties(orderResponse, orderSearchQueryModel);

            orderResponse.setBillingLastName(orderSearchQueryModel.getBillingLastName());
            orderResponse.setBillingFirstName(orderSearchQueryModel.getBillingFirstName());
            orderResponse.setBillingLastKana(orderSearchQueryModel.getBillingLastKana());
            orderResponse.setBillingFirstKana(orderSearchQueryModel.getBillingFirstKana());
            orderResponse.setBillingZipCode(orderSearchQueryModel.getBillingZipCode());
            orderResponse.setBillingAddress1(orderSearchQueryModel.getBillingAddress1());
            orderResponse.setBillingAddress2(orderSearchQueryModel.getBillingAddress2());
            orderResponse.setBillingAddress3(orderSearchQueryModel.getBillingAddress3());
            orderResponse.setBillingTel(orderSearchQueryModel.getBillingTel());
            orderResponse.setCustomerTel(orderSearchQueryModel.getCustomerTel());
            orderResponse.setShippingMethodName(orderSearchQueryModel.getShippingMethodName());
            orderResponse.setPaymentMethodName(orderSearchQueryModel.getPaymentMethodName());
            orderResponse.setShippingLastName(orderSearchQueryModel.getShippingLastName());
            orderResponse.setShippingFirstName(orderSearchQueryModel.getShippingFirstName());
            orderResponse.setShippingLastKana(orderSearchQueryModel.getShippingLastKana());
            orderResponse.setShippingFirstKana(orderSearchQueryModel.getShippingFirstKana());
            orderResponse.setShippingZipCode(orderSearchQueryModel.getShippingZipCode());
            orderResponse.setShippingPrefecture(orderSearchQueryModel.getShippingPrefecture());
            orderResponse.setShippingAddress1(orderSearchQueryModel.getShippingAddress1());
            orderResponse.setShippingAddress2(orderSearchQueryModel.getShippingAddress2());
            orderResponse.setShippingAddress3(orderSearchQueryModel.getShippingAddress3());
            orderResponse.setShippingTel(orderSearchQueryModel.getShippingTel());
            orderSearchQueryModelResponseList.add(orderResponse);
        }

        orderListResponse.setOrderSearchQueryModelList(orderSearchQueryModelResponseList);

        PageInfoForResponse pageInfoForResponse =
                        pageInfoModule.setPageInfoForResponse(pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                                              count
                                                             );
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        BeanUtils.copyProperties(pageInfoResponse, pageInfoForResponse);

        orderListResponse.setPageInfo(pageInfoResponse);

        return orderListResponse;
    }

    public OrderSearchQueryCondition toDownloadQueryCondition(OrderSearchCsvGetRequest queryRequest,
                                                              PageInfoRequest pageInfo) {

        OrderSearchQueryCondition queryCondition = new OrderSearchQueryCondition();

        if (!CollectionUtils.isEmpty(queryRequest.getOrderIDs())) {
            queryCondition.setOrderCodeList(queryRequest.getOrderIDs());
        } else {
            queryCondition.setOrderCodeList(queryRequest.getOrderCodeList());
        }
        queryCondition.setOrderCode(queryRequest.getOrderCode());
        queryCondition.setOrderStatus(queryRequest.getOrderStatus());
        queryCondition.setEmergencyFlag(queryRequest.getEmergencyFlag());
        queryCondition.setCancelFlag(queryRequest.getCancelFlag());
        queryCondition.setTimeType(queryRequest.getTimeType());
        queryCondition.setTimeFrom(conversionUtility.toTimestamp(queryRequest.getTimeFrom()));
        queryCondition.setTimeTo(conversionUtility.toTimestamp(queryRequest.getTimeTo()));
        queryCondition.setSearchNameEmUc(queryRequest.getSearchName());
        queryCondition.setSearchTelEn(queryRequest.getSearchTel());

        if (queryRequest.getCustomerId() != null) {
            queryCondition.setCustomerId(String.valueOf(queryRequest.getCustomerId()));
        }

        queryCondition.setCustomerMail(queryRequest.getCustomerMail());
        queryCondition.setGoodsGroupCode(queryRequest.getGoodsGroupCode());
        queryCondition.setGoodsCode(queryRequest.getGoodsCode());
        queryCondition.setGoodsGroupName(queryRequest.getGoodsGroupName());
        queryCondition.setJanCode(queryRequest.getJanCode());
        queryCondition.setShipmentStatus(queryRequest.getShipmentStatus());
        queryCondition.setDeliveryMethod(queryRequest.getDeliveryMethod());
        queryCondition.setDeliveryCode(queryRequest.getDeliveryCode());
        queryCondition.setSettlememntMethod(queryRequest.getSettlememntMethod());
        queryCondition.setOrderPriceFrom(queryRequest.getOrderPriceFrom());
        queryCondition.setOrderPriceTo(queryRequest.getOrderPriceTo());
        queryCondition.setExamStatus(queryRequest.getExamStatus());
        queryCondition.setSpecimenCode(queryRequest.getSpecimenCode());
        queryCondition.setExamKitCodeList(queryRequest.getExamKitCodeList());
        queryCondition.setFilterOrderedProductFlag(queryRequest.getFilterOrderedProductFlag());

        if (pageInfo != null) {
            if (pageInfo.getOrderBy() != null) {
                String orderBy = this.convertOrderBy(pageInfo.getOrderBy());
                pageInfo.setOrderBy(orderBy);
            }

            queryCondition.setPage(pageInfo.getPage());
            queryCondition.setLimit(pageInfo.getLimit());
            queryCondition.setOrderBy(pageInfo.getOrderBy());
            queryCondition.setSort(pageInfo.getSort());

        }

        return queryCondition;
    }

    /**
     * ソート項目に変換.
     *
     * @param input input
     * @return ソート項目
     */
    public String convertOrderBy(String input) {

        if (DB_FIELD_ORDER_BY_CONVERT_MAP.containsKey(input)) {
            return DB_FIELD_ORDER_BY_CONVERT_MAP.get(input);
        }

        return input;
    }

    public OrderSearchCsvOptionResponse toOrderSearchCsvOptionResponse(OrderSearchCsvOptionQueryModel model) {
        OrderSearchCsvOptionResponse csvOption = new OrderSearchCsvOptionResponse();
        csvOption.setOptionId(model.get_id());
        csvOption.setDefaultOptionName(model.getDefaultOptionName());
        csvOption.setOptionName(model.getOptionName());
        csvOption.setOutHeader(model.isOutHeader());

        List<OptionContent> optionContentList = new ArrayList<>();
        for (OptionContentDto optionContentDto : model.getOptionContent()) {
            OptionContent optionContent = new OptionContent();
            optionContent.setItemName(optionContentDto.getItemName());
            optionContent.setOrder(optionContentDto.getOrder());
            optionContent.setDefaultColumnLabel(optionContentDto.getDefaultColumnLabel());
            optionContent.setColumnLabel(optionContentDto.getColumnLabel());
            optionContent.setOutFlag(optionContentDto.isOutFlag());
            optionContent.setDefaultOrder(optionContentDto.getDefaultOrder());
            optionContentList.add(optionContent);
        }
        csvOption.setOptionContent(optionContentList);

        return csvOption;
    }

    public OrderSearchCsvOptionListResponse toOrderSearchCsvOptionListResponse(List<OrderSearchCsvOptionQueryModel> queryModelList) {

        if (ObjectUtils.isEmpty(queryModelList)) {
            return null;
        }

        OrderSearchCsvOptionListResponse response = new OrderSearchCsvOptionListResponse();
        List<OrderSearchCsvOptionResponse> optionList = new ArrayList<>();

        for (OrderSearchCsvOptionQueryModel queryModel : queryModelList) {
            optionList.add(this.toOrderSearchCsvOptionResponse(queryModel));
        }
        response.setCsvDownloadOptionList(optionList);

        return response;
    }

    /**
     * デフォルトの CSV オプションの初期化 (リセット時に使用)
     *
     * @return 注検索CSVオプションクエリモデル
     */
    public OrderSearchCsvOptionQueryModel getDefaultOrderSearchCsvOption() {
        List<OptionContentDto> contentDtoList = CsvOptionUtil.getListOptionContentDto(OrderCSVDto.class);
        OrderSearchCsvOptionQueryModel defaultModel = new OrderSearchCsvOptionQueryModel();
        defaultModel.setOptionName(StringUtils.EMPTY);
        defaultModel.setOutHeader(true);
        defaultModel.setOptionContent(contentDtoList);

        return defaultModel;
    }

    /**
     * Init db field order by comment map map.
     *
     * @return the map
     */
    private static Map<String, String> initDbFieldOrderByCommentMap() {
        Map<String, String> initMap = new HashMap<>();
        initMap.put("orderCode", DB_FIELD_ORDER_CODE);
        initMap.put("customerMail", DB_FIELD_CUSTOMER_MAIL);
        initMap.put("goodsGroupCode", DB_FIELD_GOODS_GROUP_CODE);
        initMap.put("goodsCode", DB_FIELD_GOODS_CODE);
        initMap.put("goodsGroupName", DB_FIELD_GOODS_GROUP_NAME);
        initMap.put("janCode", DB_FIELD_JAN_CODE);
        initMap.put("deliveryMethod", DB_FIELD_DELIVERY_METHOD);
        initMap.put("settlementMethod", DB_FIELD_SETTLEMENT_METHOD);
        initMap.put("orderStatusForSearchResult", DB_FIELD_ORDER_STATUS);
        return initMap;
    }
}
