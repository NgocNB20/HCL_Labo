package jp.co.itechh.quad.admin.pc.web.admin.totaling.ordersales;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.dto.totaling.orderSales.CancelOrderDto;
import jp.co.itechh.quad.admin.dto.totaling.orderSales.NewOrderDto;
import jp.co.itechh.quad.admin.dto.totaling.orderSales.OrderSalesDataDto;
import jp.co.itechh.quad.admin.dto.totaling.orderSales.OrderSalesDto;
import jp.co.itechh.quad.admin.dto.totaling.orderSales.SubTotalDto;
import jp.co.itechh.quad.admin.dto.totaling.orderSales.UpdateOrderDto;
import jp.co.itechh.quad.method.presentation.api.param.PaymentLinkResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodLinkListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesDataResponse;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesGetRequest;
import jp.co.itechh.quad.reports.presentation.api.param.OrderSalesResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ListUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 受注・売上集計Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSalesHelper {

    /** 合計 */
    private static final String TOTAL = "合計";

    /** 小計 */
    private static final String SUB_TOTAL = "小計";

    /**
     * 開始時刻（デフォルト値）
     */
    public static final String DEFAULT_START_TIME = "00:00:00";

    /**
     * 終了時刻（デフォルト値）
     */
    public static final String DEFAULT_END_TIME = "23:59:59";

    /** リンク決済ソート */
    public static final String LINK_PAY_NUMBER_SORT =
                    PropertiesUtil.getSystemPropertiesValue("aggregation.pay.id.linkpayment");

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public OrderSalesHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 受注・売上集計リクエストに変換
     *
     * @param orderSalesModel 商品詳細ページ
     * @return 受注・売上集計リクエスト
     */
    public OrderSalesGetRequest toOrderSalesGetRequest(OrderSalesModel orderSalesModel) {
        OrderSalesGetRequest orderSalesGetRequest = new OrderSalesGetRequest();

        orderSalesGetRequest.setAggregateTimeFrom(
                        conversionUtility.toTimeStamp(orderSalesModel.getProcessDateFrom(), DEFAULT_START_TIME));
        orderSalesGetRequest.setAggregateTimeTo(
                        conversionUtility.toTimeStamp(orderSalesModel.getProcessDateTo(), DEFAULT_END_TIME));
        orderSalesGetRequest.setAggregateUnit(orderSalesModel.getAggregateUnit());

        if (orderSalesModel.getOrderDeviceTypeArray() != null) {
            List<String> orderDeviceTypeList = Arrays.asList(orderSalesModel.getOrderDeviceTypeArray());
            List<String> orderDeviceList = new ArrayList<>();
            for (String item : orderDeviceTypeList) {
                orderDeviceList.add(item);
            }
            orderSalesGetRequest.setOrderDeviceList(orderDeviceList);
        }
        orderSalesGetRequest.setOrderStatus(orderSalesModel.getOrderStatus());

        if (orderSalesModel.getPaymentMethodIdArray() != null) {
            List<String> paymentMethodList = Arrays.asList(orderSalesModel.getPaymentMethodIdArray());
            List<String> paymentMethodIdList = new ArrayList<>();
            for (String item : paymentMethodList) {
                paymentMethodIdList.add(item);
            }
            orderSalesGetRequest.setPaymentMethodIdList(paymentMethodIdList);
        }

        return orderSalesGetRequest;
    }

    /**
     * 受注・売上集計リクエストに変換
     *
     * @param orderSalesResponse 受注・売上集計レスポンス
     * @return 受注・売上集計リクエスト
     */
    public List<OrderSalesDataDto> toOrderSalesTotalDtoList(OrderSalesResponse orderSalesResponse)
                    throws IllegalAccessException, InvocationTargetException {

        List<OrderSalesDataDto> orderSalesDataDtos = new ArrayList<>();
        Map<String, OrderSalesDataDto> orderSalesTotalDtoMap = new LinkedHashMap<>();
        Map<String, OrderSalesDto> totalDateOrderSalesDtoMap = new LinkedHashMap<>();

        // レスポンスチェック
        if (orderSalesResponse != null && !ListUtils.isEmpty(orderSalesResponse.getOrderSalesDataResponse())) {

            for (OrderSalesDataResponse orderSalesDataResponse : orderSalesResponse.getOrderSalesDataResponse()) {
                // 日付グループ
                OrderSalesDto totalDateOrderSalesDto = new OrderSalesDto();
                OrderSalesDataDto orderSalesDataDto = new OrderSalesDataDto();
                OrderSalesDto orderSalesDto = new OrderSalesDto();
                List<OrderSalesDto> orderSalesDtoList = new ArrayList<>();

                if (orderSalesDataResponse.getOrderSales() != null) {
                    // データ合計埋める
                    if (orderSalesTotalDtoMap.containsKey(orderSalesDataResponse.getOrderSales().getDate())) {

                        // データリスト取得
                        orderSalesDataDto = orderSalesTotalDtoMap.get(orderSalesDataResponse.getOrderSales().getDate());
                        orderSalesDtoList = orderSalesDataDto.getOrderSalesDtoList();
                        // 元合計を削除
                        orderSalesDtoList.remove(orderSalesDtoList.size() - 1);

                        copyPropertiesData(
                                        totalDateOrderSalesDto, totalDateOrderSalesDtoMap.get(
                                                        orderSalesDataResponse.getOrderSales().getDate()));

                    }
                    // 合計項目を作成
                    commonSetData(orderSalesDto, totalDateOrderSalesDto, orderSalesDataResponse,
                                  orderSalesDataResponse.getOrderSales().getPaymentMethodId(),
                                  orderSalesDataResponse.getOrderSales().getPaymentMethodName(),
                                  orderSalesDataResponse.getOrderSales().getPayType()
                                 );

                    if (StringUtils.isNotEmpty(orderSalesDto.getPaymentMethodId())) {
                        orderSalesDtoList.add(orderSalesDto);
                    }
                    orderSalesDtoList.add(totalDateOrderSalesDto);

                    orderSalesDataDto.setDate(orderSalesDataResponse.getOrderSales().getDate());
                    // 受注・売上集計Dto選別
                    orderSalesDataDto.setOrderSalesDtoList(orderSalesDtoList);

                    totalDateOrderSalesDtoMap.put(
                                    orderSalesDataResponse.getOrderSales().getDate(), totalDateOrderSalesDto);
                    orderSalesTotalDtoMap.put(orderSalesDataResponse.getOrderSales().getDate(), orderSalesDataDto);
                }

            }
            // Htmlに表示する項目取得
            orderSalesTotalDtoMap.forEach((k, v) -> orderSalesDataDtos.add(v));

            // 合計・決済方法によりデータ作成
            totalOrderSale(orderSalesDataDtos);
        }
        return orderSalesDataDtos;
    }

    /**
     * データクーロン
     *
     * @param totalDateOrderSalesDto 検索キーワード集計Dto
     * @param totalDateOrderSalesDtoMap subtotal data
     */
    public void copyPropertiesData(OrderSalesDto totalDateOrderSalesDto, OrderSalesDto totalDateOrderSalesDtoMap)
                    throws IllegalAccessException, InvocationTargetException {

        NewOrderDto newOrderDto = new NewOrderDto();
        UpdateOrderDto updateOrderDto = new UpdateOrderDto();
        CancelOrderDto cancelOrderDto = new CancelOrderDto();
        SubTotalDto subTotalDto = new SubTotalDto();

        BeanUtils.copyProperties(newOrderDto, totalDateOrderSalesDtoMap.getNewOrderDto());
        BeanUtils.copyProperties(updateOrderDto, totalDateOrderSalesDtoMap.getUpdateOrderDto());
        BeanUtils.copyProperties(cancelOrderDto, totalDateOrderSalesDtoMap.getCancelOrderDto());
        BeanUtils.copyProperties(subTotalDto, totalDateOrderSalesDtoMap.getSubTotalDto());

        totalDateOrderSalesDto.setNewOrderDto(newOrderDto);
        totalDateOrderSalesDto.setUpdateOrderDto(updateOrderDto);
        totalDateOrderSalesDto.setCancelOrderDto(cancelOrderDto);
        totalDateOrderSalesDto.setSubTotalDto(subTotalDto);
        totalDateOrderSalesDto.setPaymentMethodId(totalDateOrderSalesDtoMap.getPaymentMethodId());
        totalDateOrderSalesDto.setPaymentMethodName(totalDateOrderSalesDtoMap.getPaymentMethodName());
        totalDateOrderSalesDto.setPayType(totalDateOrderSalesDtoMap.getPayType());
        totalDateOrderSalesDto.setSortDisplay(totalDateOrderSalesDtoMap.getSortDisplay());
    }

    /**
     * 合計・決済方法によりデータ作成
     *
     * @param orderSalesDataDtos 受注・売上集計データDtoリスト
     */
    public void totalOrderSale(List<OrderSalesDataDto> orderSalesDataDtos)
                    throws IllegalAccessException, InvocationTargetException {
        OrderSalesDataDto totalOrderSalesDataDto = new OrderSalesDataDto();
        Map<String, OrderSalesDto> totalByPaymethodOrderSalesDtoMap = new LinkedHashMap<>();
        Map<String, OrderSalesDto> totalOrderSalesDtoMap = new LinkedHashMap<>();

        // 日ごとにループ
        for (OrderSalesDataDto orderSalesDataDto : orderSalesDataDtos) {
            if (!ListUtils.isEmpty(orderSalesDataDto.getOrderSalesDtoList())) {

                for (OrderSalesDto orderSalesDto : orderSalesDataDto.getOrderSalesDtoList()) {
                    // 日ごとの合計取得して計算
                    if (SUB_TOTAL.equals(orderSalesDto.getPaymentMethodId())) {
                        OrderSalesDto orderSalesDtoTotal = new OrderSalesDto();
                        if (totalOrderSalesDtoMap.containsKey(orderSalesDto.getPaymentMethodId())) {
                            copyPropertiesData(orderSalesDtoTotal,
                                               totalOrderSalesDtoMap.get(orderSalesDto.getPaymentMethodId())
                                              );
                            // 合計
                            sumTotal(orderSalesDtoTotal, orderSalesDto);

                            orderSalesDtoTotal.setPaymentMethodId(TOTAL);
                            orderSalesDtoTotal.setPaymentMethodName(TOTAL);
                            totalOrderSalesDtoMap.put(SUB_TOTAL, orderSalesDtoTotal);
                        } else {
                            copyPropertiesData(orderSalesDtoTotal, orderSalesDto);
                            orderSalesDtoTotal.setPaymentMethodId(TOTAL);
                            orderSalesDtoTotal.setPaymentMethodName(TOTAL);
                            totalOrderSalesDtoMap.put(SUB_TOTAL, orderSalesDtoTotal);
                        }

                    } else if (totalByPaymethodOrderSalesDtoMap.containsKey(orderSalesDto.getPaymentMethodName())) {
                        // 決済方法により計算
                        OrderSalesDto orderSalesDtoSumByPayMethod = new OrderSalesDto();
                        copyPropertiesData(orderSalesDtoSumByPayMethod,
                                           totalByPaymethodOrderSalesDtoMap.get(orderSalesDto.getPaymentMethodName())
                                          );

                        sumTotal(orderSalesDtoSumByPayMethod, orderSalesDto);
                        totalByPaymethodOrderSalesDtoMap.put(
                                        orderSalesDto.getPaymentMethodName(), orderSalesDtoSumByPayMethod);
                    } else {
                        // 決済方法により計算
                        totalByPaymethodOrderSalesDtoMap.put(orderSalesDto.getPaymentMethodName(), orderSalesDto);
                    }
                }
            }
        }
        List<OrderSalesDto> orderSalesDtoList = new ArrayList<>();
        // 画面表示するため
        totalByPaymethodOrderSalesDtoMap.forEach((k, v) -> orderSalesDtoList.add(v));
        totalOrderSalesDtoMap.forEach((k, v) -> orderSalesDtoList.add(v));

        totalOrderSalesDataDto.setDate(TOTAL);

        int total = 0;
        for (int i = 0; i < orderSalesDtoList.size() - 1; i++) {
            total += orderSalesDtoList.get(i).getSortDisplay();
        }
        orderSalesDtoList.get(orderSalesDtoList.size() - 1).setSortDisplay(total);

        Collections.sort(orderSalesDtoList, ((o1, o2) -> o1.getSortDisplay().compareTo(o2.getSortDisplay())));

        // 受注・売上集計Dto選別
        totalOrderSalesDataDto.setOrderSalesDtoList(orderSalesDtoList);

        orderSalesDataDtos.add(totalOrderSalesDataDto);
    }

    /**
     * 合計計算
     *
     * @param orderSalesDtoSum 受注・売上集計Dtoクラス
     * @param orderSalesDto 検索キーワード集計Dto
     */
    public void sumTotal(OrderSalesDto orderSalesDtoSum, OrderSalesDto orderSalesDto) {
        setDataNewOrder(orderSalesDtoSum, orderSalesDto.getNewOrderDto());
        setDataUpdateOrder(orderSalesDtoSum, orderSalesDto.getUpdateOrderDto());
        setDataCancelOrder(orderSalesDtoSum, orderSalesDto.getCancelOrderDto());
        setDataSubTotal(orderSalesDtoSum, orderSalesDto.getSubTotalDto());
    }

    /**
     * 新規受注Dtoを設定
     *
     * @param orderSalesDtoSum 受注・売上集計Dtoクラス
     * @param newOrderDto 新規受注Dtoクラス
     */
    public void setDataNewOrder(OrderSalesDto orderSalesDtoSum, NewOrderDto newOrderDto) {
        orderSalesDtoSum.getNewOrderDto()
                        .setNewOrderCount(orderSalesDtoSum.getNewOrderDto().getNewOrderCount()
                                          + newOrderDto.getNewOrderCount());
        orderSalesDtoSum.getNewOrderDto()
                        .setNewOrderItemSalesPriceTotal(
                                        orderSalesDtoSum.getNewOrderDto().getNewOrderItemSalesPriceTotal()
                                        + newOrderDto.getNewOrderItemSalesPriceTotal());
        orderSalesDtoSum.getNewOrderDto()
                        .setNewOrderCarriage(orderSalesDtoSum.getNewOrderDto().getNewOrderCarriage()
                                             + newOrderDto.getNewOrderCarriage());
        orderSalesDtoSum.getNewOrderDto()
                        .setNewOrderCommission(orderSalesDtoSum.getNewOrderDto().getNewOrderCommission()
                                               + newOrderDto.getNewOrderCommission());
        orderSalesDtoSum.getNewOrderDto()
                        .setNewOrderOtherPrice(orderSalesDtoSum.getNewOrderDto().getNewOrderOtherPrice()
                                               + newOrderDto.getNewOrderOtherPrice());
        orderSalesDtoSum.getNewOrderDto()
                        .setNewOrderTax(orderSalesDtoSum.getNewOrderDto().getNewOrderTax()
                                        + newOrderDto.getNewOrderTax());
        orderSalesDtoSum.getNewOrderDto()
                        .setNewOrderCouponPaymentPrice(orderSalesDtoSum.getNewOrderDto().getNewOrderCouponPaymentPrice()
                                                       + newOrderDto.getNewOrderCouponPaymentPrice());
        orderSalesDtoSum.getNewOrderDto()
                        .setNewOrderPrice(orderSalesDtoSum.getNewOrderDto().getNewOrderPrice()
                                          + newOrderDto.getNewOrderPrice());
    }

    /**
     * 更新受注Dto設定
     *
     * @param orderSalesDtoSum 受注・売上集計Dtoクラス
     * @param updateOrderDto 変更Dto
     */
    public void setDataUpdateOrder(OrderSalesDto orderSalesDtoSum, UpdateOrderDto updateOrderDto) {
        orderSalesDtoSum.getUpdateOrderDto()
                        .setUpdateOrderItemSalesPriceTotal(
                                        orderSalesDtoSum.getUpdateOrderDto().getUpdateOrderItemSalesPriceTotal()
                                        + updateOrderDto.getUpdateOrderItemSalesPriceTotal());
        orderSalesDtoSum.getUpdateOrderDto()
                        .setUpdateOrderCarriage(orderSalesDtoSum.getUpdateOrderDto().getUpdateOrderCarriage()
                                                + updateOrderDto.getUpdateOrderCarriage());
        orderSalesDtoSum.getUpdateOrderDto()
                        .setUpdateOrderCommission(orderSalesDtoSum.getUpdateOrderDto().getUpdateOrderCommission()
                                                  + updateOrderDto.getUpdateOrderCommission());
        orderSalesDtoSum.getUpdateOrderDto()
                        .setUpdateOrderOtherPrice(orderSalesDtoSum.getUpdateOrderDto().getUpdateOrderOtherPrice()
                                                  + updateOrderDto.getUpdateOrderOtherPrice());
        orderSalesDtoSum.getUpdateOrderDto()
                        .setUpdateOrderTax(orderSalesDtoSum.getUpdateOrderDto().getUpdateOrderTax()
                                           + updateOrderDto.getUpdateOrderTax());
        orderSalesDtoSum.getUpdateOrderDto()
                        .setUpdateOrderCouponPaymentPrice(
                                        orderSalesDtoSum.getUpdateOrderDto().getUpdateOrderCouponPaymentPrice()
                                        + updateOrderDto.getUpdateOrderCouponPaymentPrice());
        orderSalesDtoSum.getUpdateOrderDto()
                        .setUpdateOrderPrice(orderSalesDtoSum.getUpdateOrderDto().getUpdateOrderPrice()
                                             + updateOrderDto.getUpdateOrderPrice());
    }

    /**
     * キャンセル・返品Dtoを設定
     *
     * @param orderSalesDtoSum 受注・売上集計Dtoクラス
     * @param cancelOrderDto キャンセル・返品Dtoクラス
     */
    public void setDataCancelOrder(OrderSalesDto orderSalesDtoSum, CancelOrderDto cancelOrderDto) {
        orderSalesDtoSum.getCancelOrderDto()
                        .setCancelOrderCount(orderSalesDtoSum.getCancelOrderDto().getCancelOrderCount()
                                             + cancelOrderDto.getCancelOrderCount());
        orderSalesDtoSum.getCancelOrderDto()
                        .setCancelOrderItemSalesPriceTotal(
                                        orderSalesDtoSum.getCancelOrderDto().getCancelOrderItemSalesPriceTotal()
                                        + cancelOrderDto.getCancelOrderItemSalesPriceTotal());
        orderSalesDtoSum.getCancelOrderDto()
                        .setCancelOrderCarriage(orderSalesDtoSum.getCancelOrderDto().getCancelOrderCarriage()
                                                + cancelOrderDto.getCancelOrderCarriage());
        orderSalesDtoSum.getCancelOrderDto()
                        .setCancelOrderCommission(orderSalesDtoSum.getCancelOrderDto().getCancelOrderCommission()
                                                  + cancelOrderDto.getCancelOrderCommission());
        orderSalesDtoSum.getCancelOrderDto()
                        .setCancelOrderPrice(orderSalesDtoSum.getCancelOrderDto().getCancelOrderPrice()
                                             + cancelOrderDto.getCancelOrderPrice());
        orderSalesDtoSum.getCancelOrderDto()
                        .setCancelOrderTax(orderSalesDtoSum.getCancelOrderDto().getCancelOrderTax()
                                           + cancelOrderDto.getCancelOrderTax());
        orderSalesDtoSum.getCancelOrderDto()
                        .setCancelOrderCouponPaymentPrice(
                                        orderSalesDtoSum.getCancelOrderDto().getCancelOrderCouponPaymentPrice()
                                        + cancelOrderDto.getCancelOrderCouponPaymentPrice());
        orderSalesDtoSum.getCancelOrderDto()
                        .setCancelOrderOtherPrice(orderSalesDtoSum.getCancelOrderDto().getCancelOrderOtherPrice()
                                                  + cancelOrderDto.getCancelOrderOtherPrice());
    }

    /**
     * 小計Dto設定
     *
     * @param orderSalesDtoSum 受注・売上集計Dtoクラス
     * @param subTotalDto 小計Dto
     */
    public void setDataSubTotal(OrderSalesDto orderSalesDtoSum, SubTotalDto subTotalDto) {
        orderSalesDtoSum.getSubTotalDto()
                        .setSubTotalItemSalesPriceTotal(
                                        orderSalesDtoSum.getSubTotalDto().getSubTotalItemSalesPriceTotal()
                                        + subTotalDto.getSubTotalItemSalesPriceTotal());
        orderSalesDtoSum.getSubTotalDto()
                        .setSubTotalCarriage(orderSalesDtoSum.getSubTotalDto().getSubTotalCarriage()
                                             + subTotalDto.getSubTotalCarriage());
        orderSalesDtoSum.getSubTotalDto()
                        .setSubTotalCommission(orderSalesDtoSum.getSubTotalDto().getSubTotalCommission()
                                               + subTotalDto.getSubTotalCommission());
        orderSalesDtoSum.getSubTotalDto()
                        .setSubTotalOtherPrice(orderSalesDtoSum.getSubTotalDto().getSubTotalOtherPrice()
                                               + subTotalDto.getSubTotalOtherPrice());
        orderSalesDtoSum.getSubTotalDto()
                        .setSubTotalTax(orderSalesDtoSum.getSubTotalDto().getSubTotalTax()
                                        + subTotalDto.getSubTotalTax());
        orderSalesDtoSum.getSubTotalDto()
                        .setSubTotalCouponPaymentPrice(orderSalesDtoSum.getSubTotalDto().getSubTotalCouponPaymentPrice()
                                                       + subTotalDto.getSubTotalCouponPaymentPrice());
        orderSalesDtoSum.getSubTotalDto()
                        .setSubTotalPrice(orderSalesDtoSum.getSubTotalDto().getSubTotalPrice()
                                          + subTotalDto.getSubTotalPrice());
    }

    /**
     * 共通項目設定
     *
     * @param orderSalesDto 検索キーワード集計Dto
     * @param totalOrderSalesDto 受注・売上集計Dtoクラス
     * @param orderSalesDataResponse 受注・売上集計データ
     * @param paymentMethodId 決済方法ID
     * @param paymentMethodName 決済方法名
     */
    public void commonSetData(OrderSalesDto orderSalesDto,
                              OrderSalesDto totalOrderSalesDto,
                              OrderSalesDataResponse orderSalesDataResponse,
                              Integer paymentMethodId,
                              String paymentMethodName,
                              Integer payType) {
        NewOrderDto newOrderDto = new NewOrderDto();
        UpdateOrderDto updateOrderDto = new UpdateOrderDto();
        CancelOrderDto cancelOrderDto = new CancelOrderDto();
        SubTotalDto subTotalDto = new SubTotalDto();

        if (paymentMethodId != null) {
            orderSalesDto.setPaymentMethodId(paymentMethodId.toString());
            orderSalesDto.setSortDisplay(
                            payType == null ? paymentMethodId : Integer.parseInt(LINK_PAY_NUMBER_SORT + payType));
        }
        orderSalesDto.setPaymentMethodName(paymentMethodName);
        orderSalesDto.setPayType(payType);
        totalOrderSalesDto.setPaymentMethodId(SUB_TOTAL);
        totalOrderSalesDto.setPaymentMethodName(SUB_TOTAL);

        setDataNewOrderDto(newOrderDto, orderSalesDataResponse, totalOrderSalesDto);
        setDataUpdateOrderDto(updateOrderDto, orderSalesDataResponse, totalOrderSalesDto);
        setDataCancelOrderDto(cancelOrderDto, orderSalesDataResponse, totalOrderSalesDto);
        setDataSubTotalDto(subTotalDto, orderSalesDataResponse, totalOrderSalesDto);

        orderSalesDto.setNewOrderDto(newOrderDto);
        orderSalesDto.setUpdateOrderDto(updateOrderDto);
        orderSalesDto.setCancelOrderDto(cancelOrderDto);
        orderSalesDto.setSubTotalDto(subTotalDto);
    }

    /**
     * 新規受注Dto設定
     *
     * @param newOrderDto 新規受注Dto
     * @param orderSalesDataResponse 受注・売上集計データ
     * @param totalOrderSalesDto 受注・売上集計Dtoクラス
     */
    public void setDataNewOrderDto(NewOrderDto newOrderDto,
                                   OrderSalesDataResponse orderSalesDataResponse,
                                   OrderSalesDto totalOrderSalesDto) {

        if (orderSalesDataResponse != null) {
            newOrderDto.setNewOrderCount(this.nullToZero(orderSalesDataResponse.getNewOrderCount()));
            newOrderDto.setNewOrderItemSalesPriceTotal(
                            this.nullToZero(orderSalesDataResponse.getNewOrderItemSalesPriceTotal()));
            newOrderDto.setNewOrderCarriage(this.nullToZero(orderSalesDataResponse.getNewOrderCarriage()));
            newOrderDto.setNewOrderCommission(this.nullToZero(orderSalesDataResponse.getNewOrderCommission()));
            newOrderDto.setNewOrderOtherPrice(this.nullToZero(orderSalesDataResponse.getNewOrderOtherPrice()));
            newOrderDto.setNewOrderTax(this.nullToZero(orderSalesDataResponse.getNewOrderTax()));
            newOrderDto.setNewOrderCouponPaymentPrice(
                            this.nullToZero(orderSalesDataResponse.getNewOrderCouponPaymentPrice()));
            newOrderDto.setNewOrderPrice(this.nullToZero(orderSalesDataResponse.getNewOrderPrice()));

            if (totalOrderSalesDto != null && totalOrderSalesDto.getNewOrderDto() != null) {
                setDataNewOrder(totalOrderSalesDto, newOrderDto);
            } else if (totalOrderSalesDto != null) {
                totalOrderSalesDto.setNewOrderDto(newOrderDto);
            }
        }
    }

    /**
     * 更新受注Dto設定
     *
     * @param updateOrderDto 変更Dto
     * @param orderSalesDataResponse 受注・売上集計データ
     * @param totalOrderSalesDto 受注・売上集計Dtoクラス
     */
    public void setDataUpdateOrderDto(UpdateOrderDto updateOrderDto,
                                      OrderSalesDataResponse orderSalesDataResponse,
                                      OrderSalesDto totalOrderSalesDto) {

        if (orderSalesDataResponse != null) {
            updateOrderDto.setUpdateOrderItemSalesPriceTotal(
                            this.nullToZero(orderSalesDataResponse.getUpdateOrderItemSalesPriceTotal()));
            updateOrderDto.setUpdateOrderCarriage(this.nullToZero(orderSalesDataResponse.getUpdateOrderCarriage()));
            updateOrderDto.setUpdateOrderCommission(this.nullToZero(orderSalesDataResponse.getUpdateOrderCommission()));
            updateOrderDto.setUpdateOrderOtherPrice(this.nullToZero(orderSalesDataResponse.getUpdateOrderOtherPrice()));
            updateOrderDto.setUpdateOrderTax(this.nullToZero(orderSalesDataResponse.getUpdateOrderTax()));
            updateOrderDto.setUpdateOrderCouponPaymentPrice(
                            this.nullToZero(orderSalesDataResponse.getUpdateOrderCouponPaymentPrice()));
            updateOrderDto.setUpdateOrderPrice(this.nullToZero(orderSalesDataResponse.getUpdateOrderPrice()));
            if (totalOrderSalesDto != null && totalOrderSalesDto.getUpdateOrderDto() != null) {
                setDataUpdateOrder(totalOrderSalesDto, updateOrderDto);
            } else if (totalOrderSalesDto != null) {
                totalOrderSalesDto.setUpdateOrderDto(updateOrderDto);
            }
        }
    }

    /**
     * キャンセル・返品Dto設定
     *
     * @param cancelOrderDto ャンセル・返品Dto
     * @param orderSalesDataResponse 受注・売上集計データ
     * @param totalOrderSalesDto 受注・売上集計Dtoクラス
     */
    public void setDataCancelOrderDto(CancelOrderDto cancelOrderDto,
                                      OrderSalesDataResponse orderSalesDataResponse,
                                      OrderSalesDto totalOrderSalesDto) {

        if (orderSalesDataResponse != null) {
            cancelOrderDto.setCancelOrderCount(this.nullToZero(orderSalesDataResponse.getCancelOrderCount()));
            cancelOrderDto.setCancelOrderItemSalesPriceTotal(
                            this.nullToZero(orderSalesDataResponse.getCancelOrderItemSalesPriceTotal()));
            cancelOrderDto.setCancelOrderCarriage(this.nullToZero(orderSalesDataResponse.getCancelOrderCarriage()));
            cancelOrderDto.setCancelOrderCommission(this.nullToZero(orderSalesDataResponse.getCancelOrderCommission()));
            cancelOrderDto.setCancelOrderPrice(this.nullToZero(orderSalesDataResponse.getCancelOrderPrice()));
            cancelOrderDto.setCancelOrderTax(this.nullToZero(orderSalesDataResponse.getCancelOrderTax()));
            cancelOrderDto.setCancelOrderCouponPaymentPrice(
                            this.nullToZero(orderSalesDataResponse.getCancelOrderCouponPaymentPrice()));
            cancelOrderDto.setCancelOrderOtherPrice(this.nullToZero(orderSalesDataResponse.getCancelOrderOtherPrice()));
            if (totalOrderSalesDto != null && totalOrderSalesDto.getCancelOrderDto() != null) {
                setDataCancelOrder(totalOrderSalesDto, cancelOrderDto);
            } else if (totalOrderSalesDto != null) {
                totalOrderSalesDto.setCancelOrderDto(cancelOrderDto);
            }
        }
    }

    /**
     * 小計Dto設定
     *
     * @param subTotalDto 小計Dto
     * @param orderSalesDataResponse 受注・売上集計データ
     * @param totalOrderSalesDto 受注・売上集計Dtoクラス
     */
    public void setDataSubTotalDto(SubTotalDto subTotalDto,
                                   OrderSalesDataResponse orderSalesDataResponse,
                                   OrderSalesDto totalOrderSalesDto) {

        if (orderSalesDataResponse != null) {
            subTotalDto.setSubTotalItemSalesPriceTotal(
                            this.nullToZero(orderSalesDataResponse.getSubTotalItemSalesPriceTotal()));
            subTotalDto.setSubTotalCarriage(this.nullToZero(orderSalesDataResponse.getSubTotalCarriage()));
            subTotalDto.setSubTotalCommission(this.nullToZero(orderSalesDataResponse.getSubTotalCommission()));
            subTotalDto.setSubTotalOtherPrice(this.nullToZero(orderSalesDataResponse.getSubTotalOtherPrice()));
            subTotalDto.setSubTotalTax(this.nullToZero(orderSalesDataResponse.getSubTotalTax()));
            subTotalDto.setSubTotalCouponPaymentPrice(
                            this.nullToZero(orderSalesDataResponse.getSubTotalCouponPaymentPrice()));
            subTotalDto.setSubTotalPrice(this.nullToZero(orderSalesDataResponse.getSubTotalOrderPrice()));
            if (totalOrderSalesDto != null && totalOrderSalesDto.getSubTotalDto() != null) {
                setDataSubTotal(totalOrderSalesDto, subTotalDto);
            } else if (totalOrderSalesDto != null) {
                totalOrderSalesDto.setSubTotalDto(subTotalDto);
            }
        }
    }

    /**
     * null の場合は 0 を返却
     *
     * @param input 処理対象
     */
    private int nullToZero(Integer input) {
        if (input == null) {
            return 0;
        }
        return input;
    }

    /**
     * 決済方法レスポンスからモデルに変換
     *
     * @param paymentMethodListResponse     決済方法一覧レスポンス
     * @param orderSalesModel               受注・売上集計ページ
     */
    public boolean toPaymentMethod(PaymentMethodListResponse paymentMethodListResponse,
                                   OrderSalesModel orderSalesModel) {
        Map<String, String> paymentMethodIdItems = new LinkedHashMap<>();
        boolean hasLinkPaymentMethod = false;

        if (paymentMethodListResponse != null && !ListUtils.isEmpty(
                        paymentMethodListResponse.getPaymentMethodListResponse())) {
            for (PaymentMethodResponse paymentMethodResponse : paymentMethodListResponse.getPaymentMethodListResponse()) {
                // リンク決済方法でない場合
                if (paymentMethodResponse.getSettlementMethodSeq() != null
                    && paymentMethodResponse.getSettlementMethodType() != null
                    && !paymentMethodResponse.getSettlementMethodType()
                                             .contains(HTypeSettlementMethodType.LINK_PAYMENT.getValue())) {
                    paymentMethodIdItems.put(paymentMethodResponse.getSettlementMethodSeq().toString(),
                                             paymentMethodResponse.getSettlementMethodName()
                                            );
                }
                // リンク決済方法の場合
                else if (paymentMethodResponse.getSettlementMethodType() != null
                         && paymentMethodResponse.getSettlementMethodType()
                                                 .contains(HTypeSettlementMethodType.LINK_PAYMENT.getValue())) {
                    hasLinkPaymentMethod = true;
                }
            }
        }

        // すべての支払い方法をモデルに設定する
        orderSalesModel.setPaymentMethodIdArray(paymentMethodIdItems.keySet().toArray(new String[0]));
        orderSalesModel.setPaymentMethodIdItems(paymentMethodIdItems);

        return hasLinkPaymentMethod;
    }

    /**
     * リンク決済方法レスポンスからモデルに変換
     *
     * @param paymentMethodLinkListResponse リンク支払い方法リスト応答
     * @param orderSalesModel               受注・売上集計ページ
     */
    public void toLinkPaymentMethod(PaymentMethodLinkListResponse paymentMethodLinkListResponse,
                                    OrderSalesModel orderSalesModel) {
        Map<String, String> paymentMethodIdItems = orderSalesModel.getPaymentMethodIdItems();

        if (paymentMethodLinkListResponse != null && !ListUtils.isEmpty(
                        paymentMethodLinkListResponse.getPaymentMethodLinkList())) {
            for (PaymentLinkResponse paymentLinkResponse : paymentMethodLinkListResponse.getPaymentMethodLinkList()) {
                paymentMethodIdItems.put(paymentLinkResponse.getPaymethod(), paymentLinkResponse.getPayTypeName());
            }
        }
        // すべてのリンク決済方法支払い方法をモデルに設定する
        orderSalesModel.setPaymentMethodIdArray(paymentMethodIdItems.keySet().toArray(new String[0]));
        orderSalesModel.setPaymentMethodIdItems(paymentMethodIdItems);
    }

}