package jp.co.itechh.quad.ddd.presentation.reports.api;

import jp.co.itechh.quad.core.base.util.common.TimestampConversionUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dto.OrderSales.OrderSalesCsvDto;
import jp.co.itechh.quad.core.dto.soldgoods.DateCSVDto;
import jp.co.itechh.quad.core.dto.soldgoods.SoldGoodsCSVDto;
import jp.co.itechh.quad.ddd.infrastructure.utility.PageInfoModule;
import jp.co.itechh.quad.ddd.usecase.query.reports.DateCount;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSale;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSales;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesQueryModel;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesSearchQueryCondition;
import jp.co.itechh.quad.reports.presentation.api.param.AggregateGoodsResponse;
import jp.co.itechh.quad.reports.presentation.api.param.AggregateResponse;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSaleResponse;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSalesGetRequest;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSalesResponse;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesDataResponse;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesGetRequest;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesId;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesResponse;
import jp.co.itechh.quad.reports.presentation.api.param.PageInfoRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jp.co.itechh.quad.ddd.presentation.reports.api.processor.OrderSalesDownloadCsvProcessor.MAX_ORDER_SALES_CSV;

/**
 * 受注・売上集計ヘルパー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ReportsHelper {

    /**
     * ページ情報モジュール
     */
    private final PageInfoModule pageInfoModule;

    /**
     * 変換 Helper
     */
    private final ConversionUtility conversionUtility;

    /**
     * 商品管理番号
     */
    private final static String DB_FIELD_GOODS_GROUP_CODE = "goodsSale.goodsGroupCode";

    /**
     * 商品番号
     */
    private final static String DB_FIELD_GOODS_CODE = "goodsSale.goodsCode";

    /**
     * 商品名
     */
    private final static String DB_FIELD_GOODS_NAME = "goodsSale.goodsName";

    /**
     * 集計単位
     */
    public final static String AGGREGATE_UNIT_DAY = "1";

    /**
     * 日付フォーマット yyyy-MM-dd
     */
    public static final String YMD_SLASH = "yyyy/MM/dd";

    /**
     * 日付フォーマット yyyy-MM
     */
    public static final String YM_SLASH = "yyyy/MM";

    /**
     * 日付フォーマット yyyy年M月
     */
    public static final String YM_JP = "yyyy年MM月";

    /**
     * 日付フォーマット yyyy年M月d日
     */
    public static final String YMD_JP = "yyyy年MM月dd日";

    /**
     * 日付関連Helper取得
     */
    private final DateUtility dateUtility;

    /**
     * ハイフン
     */
    public static final String HYPHEN_JP = "－";

    /**
     * ナンバー「０」
     */
    public static final Integer NUMBER_ZERO = 0;

    /**
     * The constant DB_FIELD_ORDER_BY_CONVERT_MAP.
     */
    private final static Map<String, String> DB_FIELD_ORDER_BY_CONVERT_MAP = initDbFieldOrderByConvertMap();

    /**
     * コンストラクタ
     *
     * @param pageInfoModule    ページ情報モジュール
     * @param conversionUtility
     * @param dateUtility
     */
    @Autowired
    public ReportsHelper(PageInfoModule pageInfoModule, ConversionUtility conversionUtility, DateUtility dateUtility) {
        this.pageInfoModule = pageInfoModule;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * 受注・売上集計に変換
     *
     * @param orderSalesGetRequest 受注・売上集計リクエスト
     * @param pageInfo             ページ情報リクエスト（ページネーションのため）
     * @return 受注・売上集計用検索条件クラス
     */
    public OrderSalesSearchQueryCondition toOrderSalesSearchQueryCondition(OrderSalesGetRequest orderSalesGetRequest,
                                                                           PageInfoRequest pageInfo) {

        OrderSalesSearchQueryCondition getQueryCondition = new OrderSalesSearchQueryCondition();
        if (orderSalesGetRequest.getAggregateTimeFrom() != null) {
            getQueryCondition.setAggregateTimeFrom(orderSalesGetRequest.getAggregateTimeFrom());
        }
        if (orderSalesGetRequest.getAggregateTimeTo() != null) {
            getQueryCondition.setAggregateTimeTo(orderSalesGetRequest.getAggregateTimeTo());
        }
        if (StringUtils.isNotEmpty(orderSalesGetRequest.getAggregateUnit())) {
            getQueryCondition.setAggregateUnit(orderSalesGetRequest.getAggregateUnit());
        }
        if (orderSalesGetRequest.getOrderDeviceList() != null) {
            getQueryCondition.setOrderDeviceList(orderSalesGetRequest.getOrderDeviceList());
        }
        if (orderSalesGetRequest.getOrderStatus() != null) {
            getQueryCondition.setOrderStatus(orderSalesGetRequest.getOrderStatus());
        }
        if (orderSalesGetRequest.getPaymentMethodIdList() != null) {
            getQueryCondition.setPaymentMethodIdList(orderSalesGetRequest.getPaymentMethodIdList());
        }
        if (pageInfo != null) {
            getQueryCondition.setPageInfoForQuery(
                            pageInfoModule.setPageInfoForQuery(pageInfo.getPage(), pageInfo.getLimit() + 1,
                                                               pageInfo.getOrderBy(), pageInfo.getSort()
                                                              ));
        }

        return getQueryCondition;
    }

    /**
     * 受注検索クエリーモデルリストレスポンスに変換.
     *
     * @param orderSalesQueryModelList 受注・売上集計クエリーモデルリスト
     * @param queryCondition
     * @return 受注・売上集計レスポンス
     */
    public OrderSalesResponse toOrderSalesSearchQueryModelResponse(List<OrderSalesQueryModel> orderSalesQueryModelList,
                                                                   OrderSalesSearchQueryCondition queryCondition)
                    throws InvocationTargetException, IllegalAccessException {

        OrderSalesResponse orderSalesResponse = new OrderSalesResponse();

        if (orderSalesQueryModelList == null || orderSalesQueryModelList.size() == 0) {
            return orderSalesResponse;
        }

        List<OrderSalesQueryModel> newOrderSales = new ArrayList<>();
        addEmptyDate(newOrderSales, orderSalesQueryModelList, queryCondition);

        List<OrderSalesDataResponse> orderSalesDateResponseList = new ArrayList<>();

        int size = queryCondition.getPageInfoForQuery().getPageable().getPageSize() - 1;
        if (newOrderSales.size() > size) {
            orderSalesResponse.setOverFlag(true);
        } else {
            orderSalesResponse.setOverFlag(false);
            size = newOrderSales.size();
        }

        for (int i = 0; i < size; i++) {
            OrderSalesQueryModel orderSalesQueryModel = newOrderSales.get(i);

            for (OrderSales data : orderSalesQueryModel.getDataList()) {
                OrderSalesDataResponse orderResponse = new OrderSalesDataResponse();
                OrderSalesId orderSalesId = new OrderSalesId();
                orderSalesId.setDate(convertDateJp(orderSalesQueryModel.getDate()));
                orderSalesId.setPaymentMethodId(data.getPaymentMethodId());
                orderSalesId.setPaymentMethodName(data.getPaymentMethodName());
                orderSalesId.setPayType(data.getPayType());
                orderResponse.setOrderSales(orderSalesId);
                BeanUtils.copyProperties(orderResponse, data);
                orderSalesDateResponseList.add(orderResponse);
            }
        }

        orderSalesResponse.setOrderSalesDataResponse(orderSalesDateResponseList);

        return orderSalesResponse;
    }

    /**
     * 受注・売上集計クエリーモデルリストに変換
     *
     * @param newOrderSalesQueryModelList 受注・売上集計クエリーモデルリスト
     * @param orderSalesQueryModelList    受注・売上集計クエリーモデルリスト
     * @param queryCondition              受注・売上集計用検索条件クラス
     * @return OverFlag True : Over
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public boolean addEmptyDate(List<OrderSalesQueryModel> newOrderSalesQueryModelList,
                                List<OrderSalesQueryModel> orderSalesQueryModelList,
                                OrderSalesSearchQueryCondition queryCondition)
                    throws InvocationTargetException, IllegalAccessException {
        List<LocalDate> dateList = getDates(TimestampConversionUtil.toTimestamp(queryCondition.getAggregateTimeFrom()),
                                            TimestampConversionUtil.toTimestamp(queryCondition.getAggregateTimeTo()),
                                            queryCondition.getAggregateUnit()
                                           );

        for (LocalDate localDate : dateList) {

            OrderSalesQueryModel orderSalesQueryModel = new OrderSalesQueryModel();

            String formattedDate;

            if (AGGREGATE_UNIT_DAY.equals(queryCondition.getAggregateUnit())) {
                formattedDate = formatDate(localDate, YMD_SLASH);
            } else {
                formattedDate = formatDate(localDate, YM_SLASH);
            }
            orderSalesQueryModel.setDate(formattedDate);
            newOrderSalesQueryModelList.add(orderSalesQueryModel);
        }

        // limitを設定　※画面検索の場合はAPIリクエストのページャー指定値を設定。CSV出力の場合はページャー未設定のため無制限設定
        int limit = ObjectUtils.isNotEmpty(queryCondition.getPageInfoForQuery()) && ObjectUtils.isNotEmpty(
                        queryCondition.getPageInfoForQuery().getPageable()) ?
                        queryCondition.getPageInfoForQuery().getPageable().getPageSize() - 1 :
                        MAX_ORDER_SALES_CSV;
        return toNewOrderSalesQueryModelList(newOrderSalesQueryModelList, orderSalesQueryModelList, limit);
    }

    /**
     * 受注・売上集計クエリーモデルリストに変換
     *
     * @param newOrderSalesQueryModelList 受注・売上集計クエリーモデルリスト
     * @param orderSalesQueryModelList    受注・売上集計クエリーモデルリスト
     * @param limit                       最大表示件数
     * @return OverFlag True : Over
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public boolean toNewOrderSalesQueryModelList(List<OrderSalesQueryModel> newOrderSalesQueryModelList,
                                                 List<OrderSalesQueryModel> orderSalesQueryModelList,
                                                 int limit) throws InvocationTargetException, IllegalAccessException {

        int totalData = 0;
        int totalRecord = 0;
        boolean overFlag = false;
        for (OrderSalesQueryModel newOrderSales : newOrderSalesQueryModelList) {
            boolean isEqual = false;

            if (orderSalesQueryModelList.size() != totalData) {
                for (int i = totalData; i < orderSalesQueryModelList.size(); i++) {
                    if (StringUtils.isNotEmpty(newOrderSales.getDate()) && newOrderSales.getDate()
                                                                                        .equals(orderSalesQueryModelList.get(
                                                                                                        i).getDate())) {
                        BeanUtils.copyProperties(newOrderSales, orderSalesQueryModelList.get(i));
                        isEqual = true;
                        totalData++;
                        break;
                    }
                }
            }
            if (!isEqual) {
                List<OrderSales> orderSalesList = new ArrayList<>();
                OrderSales orderSales = new OrderSales();
                orderSales.setPaymentMethodId(null);
                orderSales.setPaymentMethodName(HYPHEN_JP);
                orderSales.setNewOrderCount(NUMBER_ZERO);
                orderSales.setNewOrderItemSalesPriceTotal(NUMBER_ZERO);
                orderSales.setNewOrderCarriage(NUMBER_ZERO);
                orderSales.setNewOrderCommission(NUMBER_ZERO);
                orderSales.setNewOrderOtherPrice(NUMBER_ZERO);
                orderSales.setNewOrderTax(NUMBER_ZERO);
                orderSales.setNewOrderCouponPaymentPrice(NUMBER_ZERO);
                orderSales.setNewOrderPrice(NUMBER_ZERO);
                orderSales.setCancelOrderCount(NUMBER_ZERO);
                orderSales.setCancelOrderItemSalesPriceTotal(NUMBER_ZERO);
                orderSales.setCancelOrderCarriage(NUMBER_ZERO);
                orderSales.setCancelOrderCommission(NUMBER_ZERO);
                orderSales.setCancelOrderOtherPrice(NUMBER_ZERO);
                orderSales.setCancelOrderTax(NUMBER_ZERO);
                orderSales.setCancelOrderCouponPaymentPrice(NUMBER_ZERO);
                orderSales.setCancelOrderPrice(NUMBER_ZERO);
                orderSales.setUpdateOrderItemSalesPriceTotal(NUMBER_ZERO);
                orderSales.setUpdateOrderCarriage(NUMBER_ZERO);
                orderSales.setUpdateOrderCommission(NUMBER_ZERO);
                orderSales.setUpdateOrderOtherPrice(NUMBER_ZERO);
                orderSales.setUpdateOrderTax(NUMBER_ZERO);
                orderSales.setUpdateOrderCouponPaymentPrice(NUMBER_ZERO);
                orderSales.setUpdateOrderPrice(NUMBER_ZERO);
                orderSales.setSubTotalItemSalesPriceTotal(NUMBER_ZERO);
                orderSales.setSubTotalCarriage(NUMBER_ZERO);
                orderSales.setSubTotalCommission(NUMBER_ZERO);
                orderSales.setSubTotalOtherPrice(NUMBER_ZERO);
                orderSales.setSubTotalTax(NUMBER_ZERO);
                orderSales.setSubTotalCouponPaymentPrice(NUMBER_ZERO);
                orderSales.setSubTotalOrderPrice(NUMBER_ZERO);

                orderSalesList.add(orderSales);

                newOrderSales.setDataList(orderSalesList);
            }
            totalRecord++;
            if (totalRecord == limit && orderSalesQueryModelList.size() != totalData) {
                overFlag = true;
            }
        }
        return overFlag;
    }

    /**
     * 商品販売個数集計レスポンスに変換
     *
     * @param goodsSaleQueryModelList 商品販売個数集計クエリモデルリスト
     * @param pageInfoRequest         ページ情報リクエスト（ページネーションのため）
     * @return 商品販売個数集計レスポンス
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public GoodsSalesResponse toGoodsSaleGetListResponse(List<GoodsSaleQueryModel> goodsSaleQueryModelList,
                                                         PageInfoRequest pageInfoRequest)
                    throws InvocationTargetException, IllegalAccessException {

        GoodsSalesResponse goodsSaleGetListResponse = new GoodsSalesResponse();

        if (CollectionUtils.isEmpty(goodsSaleQueryModelList)) {
            return goodsSaleGetListResponse;
        }

        int size = pageInfoRequest.getLimit();
        if (goodsSaleQueryModelList.size() > size) {
            goodsSaleGetListResponse.setOverFlag(true);
        } else {
            goodsSaleGetListResponse.setOverFlag(false);
            size = goodsSaleQueryModelList.size();
        }

        List<GoodsSaleResponse> goodsSaleListGetResponse = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            GoodsSaleQueryModel goodsSaleQueryModel = goodsSaleQueryModelList.get(i);
            GoodsSaleResponse goodsSaleGetResponse = new GoodsSaleResponse();
            GoodsSale goodsSale = goodsSaleQueryModel.getGoodsSale();
            List<DateCount> dateCountList = goodsSaleQueryModel.getDateList();

            if (!ObjectUtils.isEmpty(goodsSale)) {
                AggregateGoodsResponse goodsGetResponse = new AggregateGoodsResponse();
                BeanUtils.copyProperties(goodsGetResponse, goodsSale);
                goodsSaleGetResponse.setAggregateGoodsResponse(goodsGetResponse);
            }

            if (CollectionUtils.isNotEmpty(dateCountList)) {
                List<AggregateResponse> dateList = new ArrayList<>();
                for (DateCount dateCount : dateCountList) {
                    AggregateResponse goodsSaleDateResponse = new AggregateResponse();
                    goodsSaleDateResponse.setDate(dateCount.getDate());
                    goodsSaleDateResponse.setSalesCount(dateCount.getSales());
                    goodsSaleDateResponse.setCancelCount(dateCount.getCancel());
                    dateList.add(goodsSaleDateResponse);
                }
                goodsSaleGetResponse.setDateList(dateList);
            }

            goodsSaleGetResponse.setTotalSales(goodsSaleQueryModel.getTotalSales());
            goodsSaleGetResponse.setTotalCancel(goodsSaleQueryModel.getTotalCancel());

            goodsSaleListGetResponse.add(goodsSaleGetResponse);
        }

        goodsSaleGetListResponse.setGoodsSalesList(goodsSaleListGetResponse);

        return goodsSaleGetListResponse;
    }

    /**
     * 商品販売クエリ条件に変換
     *
     * @param goodsSaleGetRequest 商品販売個数集計リクエスト
     * @param pageInfo            ページ情報リクエスト
     * @return 商品販売クエリ条件
     */
    public GoodsSaleQueryCondition toGoodsSaleQueryCondition(GoodsSalesGetRequest goodsSaleGetRequest,
                                                             PageInfoRequest pageInfo) {
        GoodsSaleQueryCondition queryCondition = new GoodsSaleQueryCondition();

        queryCondition.setAggregateTimeFrom(conversionUtility.toTimestamp(goodsSaleGetRequest.getAggregateTimeFrom()));
        queryCondition.setAggregateTimeTo(conversionUtility.toTimestamp(goodsSaleGetRequest.getAggregateTimeTo()));
        queryCondition.setAggregateUnit(goodsSaleGetRequest.getAggregateUnit());
        queryCondition.setOrderDeviceList(goodsSaleGetRequest.getOrderDeviceList());
        queryCondition.setOrderStatus(goodsSaleGetRequest.getOrderStatus());
        queryCondition.setGoodsCode(goodsSaleGetRequest.getGoodsCode());
        queryCondition.setGoodsName(goodsSaleGetRequest.getGoodsName());
        queryCondition.setCategoryIdList(goodsSaleGetRequest.getCategoryIdList());

        if (pageInfo != null) {
            if (pageInfo.getOrderBy() != null) {
                String orderBy = this.convertOrderBy(pageInfo.getOrderBy());
                pageInfo.setOrderBy(orderBy);
            } else {
                String orderBy = this.convertOrderBy("goodsGroupCode");
                pageInfo.setOrderBy(orderBy);
            }
            if (pageInfo.getPage() == null) {
                pageInfo.setPage(1);
            }
            queryCondition.setPageInfoForQuery(
                            pageInfoModule.setPageInfoForQuery(pageInfo.getPage(), pageInfo.getLimit() + 1,
                                                               pageInfo.getOrderBy(), pageInfo.getSort()
                                                              ));
        }

        return queryCondition;
    }

    /**
     * 商品販売クエリ条件に変換
     *
     * @param goodsSaleGetRequest 商品販売個数集計リクエスト
     * @param pageInfo            ページ情報リクエスト
     * @return 商品販売クエリ条件
     */
    public GoodsSaleQueryCondition toGoodsSaleQueryConditionDownloadCsv(GoodsSalesGetRequest goodsSaleGetRequest,
                                                                        PageInfoRequest pageInfo) {
        GoodsSaleQueryCondition queryCondition = new GoodsSaleQueryCondition();

        queryCondition.setAggregateTimeFrom(conversionUtility.toTimestamp(goodsSaleGetRequest.getAggregateTimeFrom()));
        queryCondition.setAggregateTimeTo(conversionUtility.toTimestamp(goodsSaleGetRequest.getAggregateTimeTo()));
        queryCondition.setAggregateUnit(goodsSaleGetRequest.getAggregateUnit());
        queryCondition.setOrderDeviceList(goodsSaleGetRequest.getOrderDeviceList());
        queryCondition.setOrderStatus(goodsSaleGetRequest.getOrderStatus());
        queryCondition.setGoodsCode(goodsSaleGetRequest.getGoodsCode());
        queryCondition.setGoodsName(goodsSaleGetRequest.getGoodsName());
        queryCondition.setCategoryIdList(goodsSaleGetRequest.getCategoryIdList());

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
     * 商品販売個数集計CSVDTOを作成
     *
     * @param goodsSaleQueryModel     商品販売個数集計クエリモデル
     * @param goodsSaleQueryCondition 商品販売クエリ条件
     * @return 商品販売個数集計CSVDTO
     */
    public SoldGoodsCSVDto toSoldGoodsCSVDto(GoodsSaleQueryModel goodsSaleQueryModel,
                                             GoodsSaleQueryCondition goodsSaleQueryCondition) {
        SoldGoodsCSVDto soldGoodsCSVDto = new SoldGoodsCSVDto();

        if (ObjectUtils.isEmpty(goodsSaleQueryModel)) {
            return null;
        }

        soldGoodsCSVDto.setGoodsGroupCode(goodsSaleQueryModel.getGoodsSale().getGoodsGroupCode());
        soldGoodsCSVDto.setGoodsCode(goodsSaleQueryModel.getGoodsSale().getGoodsCode());
        soldGoodsCSVDto.setGoodsName(goodsSaleQueryModel.getGoodsSale().getGoodsName());
        soldGoodsCSVDto.setJanCode(goodsSaleQueryModel.getGoodsSale().getJanCode());
        soldGoodsCSVDto.setUnitValue1(goodsSaleQueryModel.getGoodsSale().getUnitValue1());
        soldGoodsCSVDto.setUnitValue2(goodsSaleQueryModel.getGoodsSale().getUnitValue2());
        soldGoodsCSVDto.setUnitPrice(goodsSaleQueryModel.getGoodsSale().getUnitPrice());

        List<LocalDate> dates = getDates(goodsSaleQueryCondition.getAggregateTimeFrom(),
                                         goodsSaleQueryCondition.getAggregateTimeTo(),
                                         goodsSaleQueryCondition.getAggregateUnit()
                                        );
        List<DateCSVDto> dateCSVDtos = new ArrayList<>();
        for (LocalDate localDate : dates) {
            DateCSVDto dateCSVDto = new DateCSVDto();
            String formattedDate;
            if (AGGREGATE_UNIT_DAY.equals(goodsSaleQueryCondition.getAggregateUnit())) {
                formattedDate = formatDate(localDate, YMD_SLASH);
            } else {
                formattedDate = formatDate(localDate, YM_SLASH);
            }
            dateCSVDto.setDate(formattedDate);
            dateCSVDto.setSales(0);
            dateCSVDto.setCancel(0);

            dateCSVDtos.add(dateCSVDto);
        }

        for (DateCSVDto dateCSVDto : dateCSVDtos) {
            for (DateCount dateCount : goodsSaleQueryModel.getDateList()) {
                if (StringUtils.isNotEmpty(dateCSVDto.getDate()) && dateCSVDto.getDate().equals(dateCount.getDate())) {
                    dateCSVDto.setSales(dateCount.getSales());
                    dateCSVDto.setCancel(dateCount.getCancel());
                }
            }
        }

        soldGoodsCSVDto.setDateCSVDtoList(dateCSVDtos);
        soldGoodsCSVDto.setSalesTotal(goodsSaleQueryModel.getTotalSales());
        soldGoodsCSVDto.setCancelTotal(goodsSaleQueryModel.getTotalCancel());

        return soldGoodsCSVDto;
    }

    /**
     * 日付取得
     *
     * @param from
     * @param to
     * @param type
     * @return
     */
    public List<LocalDate> getDates(Timestamp from, Timestamp to, String type) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate start = from.toLocalDateTime().toLocalDate();
        LocalDate end = to.toLocalDateTime().toLocalDate();

        long daysBetween = 0;
        if (AGGREGATE_UNIT_DAY.equals(type)) {
            daysBetween = ChronoUnit.DAYS.between(start, end);
        } else {
            daysBetween = ChronoUnit.MONTHS.between(start, end);
        }

        for (int i = 0; i <= daysBetween; i++) {
            if (AGGREGATE_UNIT_DAY.equals(type)) {
                dates.add(start.plusDays(i));
            } else {
                dates.add(start.plusMonths(i));
            }
        }

        return dates;
    }

    /**
     * 日付フォーマット
     *
     * @param date
     * @param format
     * @return
     */
    public String formatDate(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
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

    /**
     * 受注・売上集計に変換
     *
     * @param orderSalesGetRequest 受注・売上集計リクエスト
     * @return 受注・売上集計用検索条件クラス
     */
    public OrderSalesSearchQueryCondition toOrderSalesSearchQueryCondition(OrderSalesGetRequest orderSalesGetRequest) {

        OrderSalesSearchQueryCondition getQueryCondition = new OrderSalesSearchQueryCondition();
        if (orderSalesGetRequest.getAggregateTimeFrom() != null) {
            getQueryCondition.setAggregateTimeFrom(orderSalesGetRequest.getAggregateTimeFrom());
        }
        if (orderSalesGetRequest.getAggregateTimeTo() != null) {
            getQueryCondition.setAggregateTimeTo(orderSalesGetRequest.getAggregateTimeTo());
        }
        if (StringUtils.isNotEmpty(orderSalesGetRequest.getAggregateUnit())) {
            getQueryCondition.setAggregateUnit(orderSalesGetRequest.getAggregateUnit());
        }
        if (orderSalesGetRequest.getOrderDeviceList() != null) {
            getQueryCondition.setOrderDeviceList(orderSalesGetRequest.getOrderDeviceList());
        }
        if (orderSalesGetRequest.getOrderStatus() != null) {
            getQueryCondition.setOrderStatus(orderSalesGetRequest.getOrderStatus());
        }
        if (orderSalesGetRequest.getPaymentMethodIdList() != null) {
            getQueryCondition.setPaymentMethodIdList(orderSalesGetRequest.getPaymentMethodIdList());
        }

        return getQueryCondition;
    }

    /**
     * 受注・売上集計CSVDto作成
     *
     * @param orderSalesQueryModel 受注・売上集計クエリーモデル
     * @return 受注・売上集計CSVDto
     */
    public OrderSalesCsvDto toOrderSalesCsvDto(OrderSalesQueryModel orderSalesQueryModel, OrderSales orderSales)
                    throws InvocationTargetException, IllegalAccessException {
        OrderSalesCsvDto orderSalesCsvDto = new OrderSalesCsvDto();

        if (ObjectUtils.isEmpty(orderSalesQueryModel)) {
            return orderSalesCsvDto;
        }

        orderSalesCsvDto.setDate(convertDateJp(orderSalesQueryModel.getDate()));
        orderSalesCsvDto.setPaymentMethodName(orderSales.getPaymentMethodName());
        BeanUtils.copyProperties(orderSalesCsvDto, orderSales);

        return orderSalesCsvDto;
    }

    /**
     * 日付に変換
     *
     * @param date 日付
     * @return 日付
     */
    private String convertDateJp(String date) {
        if (date.length() > 7) {
            return dateUtility.format(TimestampConversionUtil.toTimestamp(date, YMD_SLASH), YMD_JP);
        } else {
            return dateUtility.format(TimestampConversionUtil.toTimestamp(date, YM_SLASH), YM_JP);
        }
    }

    /**
     * Init db field order by convert map map.
     *
     * @return the map
     */
    private static Map<String, String> initDbFieldOrderByConvertMap() {
        Map<String, String> initMap = new HashMap<>();
        initMap.put("goodsGroupCode", DB_FIELD_GOODS_GROUP_CODE);
        initMap.put("goodsCode", DB_FIELD_GOODS_CODE);
        initMap.put("goodsName", DB_FIELD_GOODS_NAME);
        return initMap;
    }
}