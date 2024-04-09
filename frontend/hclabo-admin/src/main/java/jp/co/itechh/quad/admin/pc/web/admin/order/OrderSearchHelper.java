package jp.co.itechh.quad.admin.pc.web.admin.order;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeEmergencyFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderStatusDdd;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeRepeatPurchaseType;
import jp.co.itechh.quad.admin.dto.order.OrderSearchConditionDto;
import jp.co.itechh.quad.admin.dto.order.OrderSearchOrderResultDto;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderProductResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvGetRequest;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryModelListResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryModelResponse;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchQueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 受注検索Helperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSearchHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    private final static List<String> ORDER_STATUS_DDD_VALUE_LIST =
                    new ArrayList<>(Arrays.asList("0", "1", "3", "5", "7", "8"));

    /**
     * コンストラクター
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public OrderSearchHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 受注検索条件に変換.
     *
     * @param conditionDto 受注検索一覧用条件Dtoクラス
     * @return 受注検索条件
     */
    public OrderSearchQueryRequest toOrderSearchQueryRequest(OrderSearchConditionDto conditionDto) {

        OrderSearchQueryRequest request = new OrderSearchQueryRequest();

        request.setOrderCode(conditionDto.getOrderCode());

        request.setEmergencyFlag(conditionDto.getEmergencyFlag());
        request.setTimeType(conditionDto.getTimeType());
        request.setTimeFrom(conditionDto.getTimeFrom());
        request.setTimeTo(conditionDto.getTimeTo());
        request.setSearchName(conditionDto.getSearchName());
        request.setSearchTel(conditionDto.getSearchTel());
        request.setCustomerMail(conditionDto.getCustomerMail());
        request.setGoodsGroupCode(conditionDto.getGoodsGroupCode());
        request.setGoodsCode(conditionDto.getGoodsCode());
        request.setGoodsGroupName(conditionDto.getGoodsGroupName());
        request.setJanCode(conditionDto.getJanCode());
        request.setShipmentStatus(conditionDto.getShipmentStatus());
        request.setOrderPriceFrom(conditionDto.getOrderPriceFrom());
        request.setOrderPriceTo(conditionDto.getOrderPriceTo());

        if (conditionDto.getCustomerMail() != null) {
            request.setCustomerMail(conditionDto.getCustomerMail());
        }

        if (conditionDto.getSearchName() != null) {
            request.setSearchName(StringUtils.upperCase(conditionDto.getSearchName()));
        }
        if (conditionDto.getSearchTel() != null) {
            request.setSearchTel(conditionDto.getSearchTel());
        }

        if (conditionDto.getGoodsGroupName() != null) {
            request.setGoodsGroupName(conditionDto.getGoodsGroupName());
        }

        if (conditionDto.getDeliveryMethodSeq() != null) {
            request.setDeliveryMethod(conditionDto.getDeliveryMethodSeq().toString());

        }

        request.setDeliveryCode(conditionDto.getDeliveryCode());

        if (conditionDto.getSettlementMethodSeq() != null) {
            request.setSettlememntMethod(conditionDto.getSettlementMethodSeq().toString());

        }

        if (conditionDto.getMemberInfoSeq() != null) {
            request.setCustomerId(conditionDto.getMemberInfoSeq());
        }

        // 受注状態
        if (conditionDto.getOrderStatus() == null && conditionDto.getCancelFlag() != null
            && conditionDto.getCancelFlag().equals("1")) {
            // キャンセル
            request.setOrderStatus("5");
        } else if (conditionDto.getOrderStatus() == null && conditionDto.getCancelFlag() != null
                   && conditionDto.getCancelFlag().equals("0") && conditionDto.getWaitingFlag() != null
                   && conditionDto.getWaitingFlag().equals("1")) {
            // 保留中
            request.setOrderStatus("6");
        } else if (conditionDto.getOrderStatus() == null && conditionDto.getEmergencyFlag() != null
                   && conditionDto.getEmergencyFlag().equals("1")) {
            // 請求決済エラー
            request.setOrderStatus("7");
        } else if (conditionDto.getOrderStatus() == null && conditionDto.getCancelFlag() != null
                   && conditionDto.getCancelFlag().equals("0")) {
            // キャンセル以外
            request.setOrderStatus("8");
        } else if (conditionDto.getOrderStatus() != null) {
            // 「入金確認中」、「商品準備中」、「出荷完了」
            request.setOrderStatus(conditionDto.getOrderStatus());
        }

        request.setOrderCodeList(conditionDto.getOrderCodeList());

        if (conditionDto.getExamStatus() != null) {
            request.setExamStatus(conditionDto.getExamStatus());
        }

        if (conditionDto.getSpecimenCode() != null) {
            request.setSpecimenCode(conditionDto.getSpecimenCode());
        }

        request.setExamKitCodeList(conditionDto.getExamKitCodeList());

        return request;
    }

    /**
     * 受注検索受注一覧用Dtoクラスリストに変換.
     *
     * @param responseList 受注検索クエリーモデルリストレスポンス
     * @return 受注検索受注一覧用Dtoクラスリスト
     */
    public List<OrderSearchOrderResultDto> toOrderSearchOrderResultDtoList(OrderSearchQueryModelListResponse responseList) {

        List<OrderSearchOrderResultDto> resultList = new ArrayList<>();

        List<OrderSearchQueryModelResponse> orderSearchQueryModelResponseList =
                        responseList.getOrderSearchQueryModelList();

        if (CollectionUtil.isEmpty(orderSearchQueryModelResponseList)) {
            return new ArrayList<>();
        }

        orderSearchQueryModelResponseList.forEach(item -> {
            OrderSearchOrderResultDto resultDto = new OrderSearchOrderResultDto();

            resultDto.setOrderCode(item.getOrderCode());
            resultDto.setOrderTime(conversionUtility.toTimestamp(item.getOrderTime()));
            resultDto.setCancelTime(conversionUtility.toTimestamp(item.getCancelTime()));

            int goodsPriceTotal = 0;
            if (!CollectionUtil.isEmpty(item.getOrderProductList())) {
                goodsPriceTotal = item.getOrderProductList()
                                      .stream()
                                      .map(OrderProductResponse::getGoodsPriceTotal)
                                      .filter(Objects::nonNull)
                                      .reduce(0, Integer::sum);
            }

            resultDto.setGoodsPriceTotal(new BigDecimal(goodsPriceTotal));

            if (item.getOrderDeliveryCarriage() != null) {
                resultDto.setOrderDeliveryCarriage(new BigDecimal(item.getOrderDeliveryCarriage()));
            }

            if (item.getOrderPrice() != null) {
                resultDto.setOrderPrice(new BigDecimal(item.getOrderPrice()));
            }

            resultDto.setMemberInfoSeq(item.getCustomerId());
            resultDto.setPrefectureType(
                            EnumTypeUtil.getEnumFromValue(HTypePrefectureType.class, item.getBillingPrefecture()));
            resultDto.setOrderSex(EnumTypeUtil.getEnumFromValue(HTypeOrderSex.class, item.getCustomerSex()));
            resultDto.setOrderAgeType(
                            EnumTypeUtil.getEnumFromValue(HTypeOrderAgeType.class, item.getCustomerAgeType()));

            if (item.getRepeatPurchaseType() != null) {
                resultDto.setRepeatPurchaseType(EnumTypeUtil.getEnumFromValue(HTypeRepeatPurchaseType.class,
                                                                              item.getRepeatPurchaseType().toString()
                                                                             ));
            }

            if (item.getSettlementCommission() != null) {
                resultDto.setSettlementMailRequired(EnumTypeUtil.getEnumFromValue(HTypeMailRequired.class,
                                                                                  item.getSettlementCommission()
                                                                                      .toString()
                                                                                 ));
            }

            if (item.getCouponPaymentAmount() != null) {
                resultDto.setCouponDiscountPrice(new BigDecimal(item.getCouponPaymentAmount()));
            }

            resultDto.setPaymentMethodName(item.getPaymentMethodName());
            resultDto.setLinkPaymentName(item.getLinkPaymentMethodName());
            resultDto.setReceiverTimeZone(item.getReceiverTimeZone());
            resultDto.setOrderName(item.getCustomerLastName() + item.getCustomerFirstName());
            resultDto.setOrderKana(item.getCustomerLastKana() + item.getCustomerFirstKana());
            resultDto.setBillingTel(item.getBillingTel());
            resultDto.setCustomerTel(item.getCustomerTel());
            resultDto.setOrderMail(item.getCustomerMail());
            resultDto.setShippingName(item.getShippingLastName() + item.getShippingFirstName());
            resultDto.setShippingKana(item.getShippingLastKana() + item.getShippingFirstKana());
            resultDto.setShippingTel(item.getShippingTel());
            resultDto.setDeliveryCode(item.getDeliveryStatusConfirmationNo());
            resultDto.setShipmentdate(conversionUtility.toTimestamp(item.getShipmentDate()));
            resultDto.setShipmentStatus(item.getShipmentStatus());
            resultDto.setDeliveryNote(item.getDeliveryNote());

            resultDto.setEmergencyFlag(
                            EnumTypeUtil.getEnumFromValue(HTypeEmergencyFlag.class, item.getEmergencyFlag()));
            resultDto.setPaymentStatus(item.getPaymentStatus());
            resultDto.setShippingMethodName(item.getShippingMethodName());

            if (StringUtil.isNotEmpty(item.getOrderStatus()) && ORDER_STATUS_DDD_VALUE_LIST.contains(
                            item.getOrderStatus())) {
                String orderStatus = EnumTypeUtil.getEnumFromValue(HTypeOrderStatusDdd.class, item.getOrderStatus())
                                                 .getValue();
                resultDto.setOrderStatusForSearchResult(orderStatus);
            }

            resultList.add(resultDto);
        });

        return resultList;
    }

    /**
     * 受注番号に変換.
     *
     * @param conditionOrderCodeList 受注番号（複数番号検索用）
     * @return 受注番号
     */
    public List<String> toOrderIds(String conditionOrderCodeList) {

        List<String> orderIds = new ArrayList<>();

        // 受注番号（複数番号検索用）
        if (conditionOrderCodeList != null && StringUtil.isNotEmpty(conditionOrderCodeList.replaceAll("[\\s|　]", ""))) {
            // 検索に有効な文字列が存在する場合
            // 選択肢区切り文字を設定ファイルから取得
            String divchar = PropertiesUtil.getSystemPropertiesValue("order.search.order.code.list.divchar");
            String orderCodeList = conditionOrderCodeList;
            // 空白を削除する
            orderCodeList = orderCodeList.replaceAll("[ 　\t\\x0B\f]", "");
            // 2つ以上連続した改行コードを1つにまとめる
            orderCodeList = orderCodeList.replaceAll("(" + divchar + "){2,}", "\n");
            // 先頭または最後尾の改行コードを削除する
            orderCodeList = orderCodeList.replaceAll("^[" + divchar + "]+|[" + divchar + "]$", "");
            // 検索用複数番号を配列化する
            String[] orderCodeArray = orderCodeList.split(divchar);
            if (orderCodeArray.length > 0) {
                orderIds = Arrays.asList(orderCodeArray);
            }
        }
        return orderIds;
    }

    /**
     * 受注検索CSVDLリクエストに変換.
     *
     * @param conditionDto 受注検索一覧用条件Dtoクラス
     * @return 受注検索CSVDLリクエスト
     */
    public OrderSearchCsvGetRequest toOrderSearchCsvGetRequest(OrderSearchConditionDto conditionDto) {

        OrderSearchCsvGetRequest request = new OrderSearchCsvGetRequest();

        request.setOrderCode(conditionDto.getOrderCode());
        request.setEmergencyFlag(conditionDto.getEmergencyFlag());
        request.setTimeType(conditionDto.getTimeType());
        request.setTimeFrom(conditionDto.getTimeFrom());
        request.setTimeTo(conditionDto.getTimeTo());
        request.setCustomerId(conditionDto.getMemberInfoSeq());
        request.setSearchName(conditionDto.getSearchName());
        request.setSearchTel(conditionDto.getSearchTel());
        request.setCustomerMail(conditionDto.getCustomerMail());
        request.setGoodsGroupCode(conditionDto.getGoodsGroupCode());
        request.setGoodsCode(conditionDto.getGoodsCode());
        request.setGoodsGroupName(conditionDto.getGoodsGroupName());
        request.setJanCode(conditionDto.getJanCode());
        request.setShipmentStatus(conditionDto.getShipmentStatus());

        if (conditionDto.getDeliveryMethodSeq() != null) {
            request.setDeliveryMethod(conditionDto.getDeliveryMethodSeq().toString());

        }

        request.setDeliveryCode(conditionDto.getDeliveryCode());

        if (conditionDto.getSettlementMethodSeq() != null) {
            request.setSettlememntMethod(conditionDto.getSettlementMethodSeq().toString());

        }

        request.setOrderStatus(conditionDto.getOrderStatus());

        request.setOrderCodeList(conditionDto.getOrderCodeList());

        request.setOrderPriceFrom(conditionDto.getOrderPriceFrom());
        request.setOrderPriceTo(conditionDto.getOrderPriceTo());

        request.setExamStatus(conditionDto.getExamStatus());
        request.setSpecimenCode(conditionDto.getSpecimenCode());
        request.setExamKitCodeList(conditionDto.getExamKitCodeList());

        request.setFilterOrderedProductFlag(conditionDto.isFilterOrderedProductFlag());

        return request;
    }

}
