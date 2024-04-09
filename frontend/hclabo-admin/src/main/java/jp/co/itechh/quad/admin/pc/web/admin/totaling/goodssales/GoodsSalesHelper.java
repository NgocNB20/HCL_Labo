package jp.co.itechh.quad.admin.pc.web.admin.totaling.goodssales;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAggregateUnit;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.reports.presentation.api.param.AggregateResponse;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSaleResponse;
import jp.co.itechh.quad.reports.presentation.api.param.GoodsSalesGetRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * 商品別販売価格別集計ヘルパー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class GoodsSalesHelper {

    /** SIMPLE_DATE_FORMAT_YEAR_MONTH */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_YEAR_MONTH = new SimpleDateFormat("yyyy/MM");

    /** SIMPLE_DATE_FORMAT_YEAR_MONTH_DAY */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_YEAR_MONTH_DAY = new SimpleDateFormat("yyyy/MM/dd");

    /** DATE_TIME_FORMATTER_YEAR_MONTH_DAY */
    private static final DateTimeFormatter DATE_TIME_FORMATTER_YEAR_MONTH_DAY =
                    DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /** DATE_TIME_FORMATTER_YEAR_MONTH */
    private static final DateTimeFormatter DATE_TIME_FORMATTER_YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy/MM");

    /** コンストラクター */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility コンストラクター
     */
    @Autowired
    public GoodsSalesHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 商品別販売価格別集計生成
     *
     * @param goodsSaleResponseList
     * @param goodsSalesModel
     * @throws ParseException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void buildGoodsSalesItemList(List<GoodsSaleResponse> goodsSaleResponseList, GoodsSalesModel goodsSalesModel)
                    throws ParseException, InvocationTargetException, IllegalAccessException {

        if (CollectionUtil.isNotEmpty(goodsSalesModel.getCategoryList())) {
            List<String> categoryNameList = new ArrayList<>();

            for (CategoryEntity categoryEntity : goodsSalesModel.getCategoryList()) {
                categoryNameList.add(categoryEntity.getCategoryName());
            }
            goodsSalesModel.setCategoryNameList(categoryNameList);
        }

        if (CollectionUtils.isEmpty(goodsSaleResponseList)) {
            goodsSalesModel.setResultItems(new ArrayList<>());
            return;
        }

        List<GoodsSalesItem> goodsSalesItemList = new ArrayList<>();
        GoodsSalesSummary goodsSalesSummary = new GoodsSalesSummary();
        goodsSalesSummary.setTotalSales(0);
        goodsSalesSummary.setTotalCancel(0);
        goodsSalesSummary.setAggregateItems(new ArrayList<>());

        List<String> rangeDateStr = goodsSalesModel.getRangeDateStr();

        for (GoodsSaleResponse goodsSaleResponse : goodsSaleResponseList) {

            goodsSalesSummary.setTotalSales(
                            goodsSalesSummary.getTotalSales() + this.nullToZero(goodsSaleResponse.getTotalSales()));
            goodsSalesSummary.setTotalCancel(
                            goodsSalesSummary.getTotalCancel() + this.nullToZero(goodsSaleResponse.getTotalCancel()));

            int index = 0;
            while (index < rangeDateStr.size()) {
                if (CollectionUtils.isEmpty(goodsSaleResponse.getDateList())) {
                    goodsSaleResponse.setDateList(new ArrayList<>());
                }
                if (index >= goodsSaleResponse.getDateList().size()) {
                    addEmptyToDateList(rangeDateStr, goodsSaleResponse, index);
                    updateAggregateItem(rangeDateStr, goodsSalesSummary, goodsSaleResponse, index);
                    index++;
                } else {
                    int compare = compareStringDate(rangeDateStr.get(index),
                                                    goodsSaleResponse.getDateList().get(index).getDate(),
                                                    goodsSalesModel.getAggregateUnit()
                                                   );
                    if (compare < 0) {
                        addEmptyToDateList(rangeDateStr, goodsSaleResponse, index);
                        updateAggregateItem(rangeDateStr, goodsSalesSummary, goodsSaleResponse, index);
                    } else {
                        updateAggregateItem(rangeDateStr, goodsSalesSummary, goodsSaleResponse, index);
                        index++;
                    }
                }
            }
            GoodsSalesItem goodsSalesItem = new GoodsSalesItem();

            GoodsItem goodsItem = new GoodsItem();
            BeanUtils.copyProperties(goodsItem, goodsSaleResponse.getAggregateGoodsResponse());

            List<GoodsSalesAggregateItem> goodsSalesAggregateItemList =
                            Objects.requireNonNull(goodsSaleResponse.getDateList()).stream().map(dateList -> {
                                GoodsSalesAggregateItem goodsSalesAggregateItem = new GoodsSalesAggregateItem();

                                goodsSalesAggregateItem.setDate(dateList.getDate());
                                goodsSalesAggregateItem.setSalesCount(dateList.getSalesCount());
                                goodsSalesAggregateItem.setCancelCount(dateList.getCancelCount());

                                return goodsSalesAggregateItem;
                            }).collect(Collectors.toList());

            goodsSalesItem.setGoodsItem(goodsItem);
            goodsSalesItem.setTotalSales(goodsSaleResponse.getTotalSales());
            goodsSalesItem.setTotalCancel(goodsSaleResponse.getTotalCancel());
            goodsSalesItem.setGoodsSalesAggregateItemList(goodsSalesAggregateItemList);

            goodsSalesItemList.add(goodsSalesItem);
        }

        goodsSalesModel.setResultItems(goodsSalesItemList);
        goodsSalesModel.setResultSummary(goodsSalesSummary);
    }

    /**
     * 商品別販売価格別集計リクエストに変換
     *
     * @param goodsSalesModel
     * @return GoodsSalesGetRequest
     */
    public GoodsSalesGetRequest toGoodsSalesGetRequest(GoodsSalesModel goodsSalesModel) {

        GoodsSalesGetRequest goodsSalesGetRequest = new GoodsSalesGetRequest();
        goodsSalesGetRequest.setAggregateUnit(goodsSalesModel.getAggregateUnit());

        Date aggregateTimeFrom = conversionUtility.toDate(
                        goodsSalesModel.getAggregateTimeFrom() + " " + ConversionUtility.DEFAULT_START_TIME,
                        DateUtility.YMD_SLASH_HMS
                                                         );
        Date aggregateTimeTo = conversionUtility.toDate(
                        goodsSalesModel.getAggregateTimeTo() + " " + ConversionUtility.DEFAULT_END_TIME,
                        DateUtility.YMD_SLASH_HMS
                                                       );
        goodsSalesGetRequest.setAggregateTimeFrom(aggregateTimeFrom);
        goodsSalesGetRequest.setAggregateTimeTo(aggregateTimeTo);

        goodsSalesGetRequest.setGoodsCode(goodsSalesModel.getSearchGoodsCode());
        goodsSalesGetRequest.setGoodsName(goodsSalesModel.getSearchGoodsName());
        goodsSalesGetRequest.setOrderStatus(goodsSalesModel.getOrderStatus());
        goodsSalesGetRequest.setCategoryIdList(goodsSalesModel.getCategoryIdList());
        goodsSalesGetRequest.setOrderDeviceList(Arrays.asList(goodsSalesModel.getDeviceArray()));

        return goodsSalesGetRequest;
    }

    /**
     * 日付に空白を設定
     *
     * @param rangeDateStr
     * @param goodsSaleResponse
     * @param index
     */
    private void addEmptyToDateList(List<String> rangeDateStr, GoodsSaleResponse goodsSaleResponse, int index) {

        if (CollectionUtils.isEmpty(goodsSaleResponse.getDateList())) {
            return;
        }

        AggregateResponse emptyAggregate = new AggregateResponse();
        emptyAggregate.setDate(rangeDateStr.get(index));
        emptyAggregate.setSalesCount(0);
        emptyAggregate.setCancelCount(0);

        goodsSaleResponse.getDateList().add(index, emptyAggregate);
    }

    /**
     * 商品別販売価格別集計項目更新
     *
     * @param rangeDateStr
     * @param goodsSalesSummary
     * @param goodsSaleResponse
     * @param index
     */
    private void updateAggregateItem(List<String> rangeDateStr,
                                     GoodsSalesSummary goodsSalesSummary,
                                     GoodsSaleResponse goodsSaleResponse,
                                     int index) {

        if (goodsSalesSummary.getAggregateItems().size() <= index) {
            goodsSalesSummary.getAggregateItems().add(new GoodsSalesAggregateItem());
        }

        AggregateResponse aggregateResponse = Objects.requireNonNull(goodsSaleResponse.getDateList()).get(index);
        GoodsSalesAggregateItem goodsSalesAggregateItem = goodsSalesSummary.getAggregateItems().get(index);

        goodsSalesAggregateItem.setDate(rangeDateStr.get(index));
        goodsSalesAggregateItem.setSalesCount(
                        goodsSalesAggregateItem.getSalesCount() + this.nullToZero(aggregateResponse.getSalesCount()));
        goodsSalesAggregateItem.setCancelCount(
                        goodsSalesAggregateItem.getCancelCount() + this.nullToZero(aggregateResponse.getCancelCount()));
        goodsSalesAggregateItem.setDate(rangeDateStr.get(index));
    }

    /**
     * 日付比較
     *
     * @param left
     * @param right
     * @param type
     * @return the int
     * @throws ParseException
     */
    private int compareStringDate(String left, String right, String type) throws ParseException {
        long dateLeft = 0;
        long dateRight = 0;

        if (HTypeAggregateUnit.DAY.getValue().equals(type)) {
            dateLeft = SIMPLE_DATE_FORMAT_YEAR_MONTH_DAY.parse(left).getTime();
            dateRight = SIMPLE_DATE_FORMAT_YEAR_MONTH_DAY.parse(right).getTime();
        } else if (HTypeAggregateUnit.MONTH.getValue().equals(type)) {
            dateLeft = SIMPLE_DATE_FORMAT_YEAR_MONTH.parse(left).getTime();
            dateRight = SIMPLE_DATE_FORMAT_YEAR_MONTH.parse(right).getTime();
        }

        if (dateLeft > dateRight) {
            return 1;
        } else if (dateLeft == dateRight) {
            return 0;
        }
        return -1;
    }

    /**
     * 集計期間取得
     *
     * @return the search time from
     */
    public String getSearchTimeFrom() {
        LocalDate localDate = LocalDate.now().withDayOfMonth(1);
        return localDate.format(DATE_TIME_FORMATTER_YEAR_MONTH_DAY);
    }

    /**
     * 集計期間
     *
     * @return the search time to
     */
    public String getSearchTimeTo() {
        LocalDate localDate = LocalDate.now().with(lastDayOfMonth());
        return localDate.format(DATE_TIME_FORMATTER_YEAR_MONTH_DAY);
    }

    /**
     * 集計期間作成
     *
     * @param searchFromStr   the search from str
     * @param searchToStr     the search to str
     * @param aggregationUnit the aggregation unit
     * @return the list
     */
    public List<String> buildRangeSearchTime(String searchFromStr, String searchToStr, String aggregationUnit) {
        List<String> rangeSearchTime = new ArrayList<>();

        LocalDate from = LocalDate.parse(searchFromStr, DATE_TIME_FORMATTER_YEAR_MONTH_DAY);
        LocalDate to = LocalDate.parse(searchToStr, DATE_TIME_FORMATTER_YEAR_MONTH_DAY);

        if (HTypeAggregateUnit.MONTH.getValue().equals(aggregationUnit)) {
            from = LocalDate.parse(searchFromStr, DATE_TIME_FORMATTER_YEAR_MONTH_DAY).withDayOfMonth(1);
            to = LocalDate.parse(searchToStr, DATE_TIME_FORMATTER_YEAR_MONTH_DAY).withDayOfMonth(1);
        }

        while (from.isBefore(to) || from.isEqual(to)) {
            if (HTypeAggregateUnit.DAY.getValue().equals(aggregationUnit)) {
                rangeSearchTime.add(from.format(DATE_TIME_FORMATTER_YEAR_MONTH_DAY));
                from = from.plusDays(1);
            } else {
                rangeSearchTime.add(from.format(DATE_TIME_FORMATTER_YEAR_MONTH));
                from = from.plusMonths(1);
            }
        }

        return rangeSearchTime;
    }

    /**
     * ヌール場合０に設定
     *
     * @param value the value
     * @return the int
     */
    private int nullToZero(Integer value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

}