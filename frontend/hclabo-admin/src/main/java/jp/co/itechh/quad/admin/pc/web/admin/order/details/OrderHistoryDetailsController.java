/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.convenience.presentation.api.ConvenienceApi;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponVersionNoRequest;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedCountRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedCountResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistory;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistoryListGetRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistoryListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 処理履歴詳細コントローラー
 *
 * @author kimura
 */
@RequestMapping("/order/details/historydetails")
@Controller
@SessionAttributes(value = "orderHistoryDetailsModel")
@PreAuthorize("hasAnyAuthority('ORDER:4')")
public class OrderHistoryDetailsController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderHistoryDetailsController.class);

    /** 不正操作 */
    public static final String MSGCD_REFERER_FAIL = "AOX001601";

    /** 顧客API */
    private final CustomerApi customerApi;

    /** 商品API */
    private final ProductApi productApi;

    /** 取引API */
    private final TransactionApi transactionApi;

    /** 受注API */
    private final OrderReceivedApi orderReceivedApi;

    /** 配送伝票API */
    private final ShippingSlipApi shippingSlipApi;

    /** 請求伝票API */
    private final BillingSlipApi billingSlipApi;

    /** 販売伝票API */
    private final SalesSlipApi salesSlipApi;

    /** 注文票API */
    private final OrderSlipApi orderSlipApi;

    /** 住所録API */
    private final AddressBookApi addressBookApi;

    /** 決済方法API */
    private final SettlementMethodApi settlementMethodApi;

    /** マルペイAPI */
    private final MulpayApi mulpayApi;

    /** クーポンAPI */
    private final CouponApi couponApi;

    /** コンビニApi */
    private final ConvenienceApi convenienceApi;

    /** helper */
    private final OrderHistoryDetailsHelper orderHistorydetailsHelper;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public OrderHistoryDetailsController(CustomerApi customerApi,
                                         ProductApi productApi,
                                         TransactionApi transactionApi,
                                         OrderReceivedApi orderReceivedApi,
                                         ShippingSlipApi shippingSlipApi,
                                         BillingSlipApi billingSlipApi,
                                         SalesSlipApi salesSlipApi,
                                         OrderSlipApi orderSlipApi,
                                         AddressBookApi addressBookApi,
                                         SettlementMethodApi settlementMethodApi,
                                         MulpayApi mulpayApi,
                                         CouponApi couponApi,
                                         ConvenienceApi convenienceApi,
                                         OrderHistoryDetailsHelper orderHistorydetailsHelper,
                                         ConversionUtility conversionUtility) {
        this.customerApi = customerApi;
        this.productApi = productApi;
        this.transactionApi = transactionApi;
        this.orderReceivedApi = orderReceivedApi;
        this.shippingSlipApi = shippingSlipApi;
        this.billingSlipApi = billingSlipApi;
        this.salesSlipApi = salesSlipApi;
        this.orderSlipApi = orderSlipApi;
        this.addressBookApi = addressBookApi;
        this.settlementMethodApi = settlementMethodApi;
        this.mulpayApi = mulpayApi;
        this.couponApi = couponApi;
        this.convenienceApi = convenienceApi;
        this.orderHistorydetailsHelper = orderHistorydetailsHelper;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期処理<br/>
     *
     * @param orderCode                受注番号
     * @param orderVersionNo           注文番号連番
     * @param orderHistoryDetailsModel 処理履歴詳細Model
     * @param error
     * @param model
     * @return 自画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/error")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> orderCode,
                                 @RequestParam(required = false) Optional<Integer> orderVersionNo,
                                 OrderHistoryDetailsModel orderHistoryDetailsModel,
                                 BindingResult error,
                                 Model model) {

        if (!orderCode.isPresent() || !orderVersionNo.isPresent()) {
            throwMessage(MSGCD_REFERER_FAIL);
        }

        // モデル初期化
        clearModel(OrderHistoryDetailsModel.class, orderHistoryDetailsModel, model);

        Integer orderVersionNoData = orderVersionNo.get();
        orderHistoryDetailsModel.setOrderCode(orderCode.get());

        // 処理履歴一覧取得
        ProcessHistoryListGetRequest processHistoryListGetRequest = new ProcessHistoryListGetRequest();
        processHistoryListGetRequest.setOrderCode(orderCode.get());
        ProcessHistoryListResponse processHistoryListResponse =
                        this.transactionApi.getProcessHistoryList(processHistoryListGetRequest);

        if (ObjectUtils.isEmpty(processHistoryListResponse) || CollectionUtils.isEmpty(
                        processHistoryListResponse.getProcessHistoryList())) {
            throwMessage(MSGCD_REFERER_FAIL);
        }

        // 対象の処理情報を取得する　※画面で渡された「注文番号連番 - 1」が対象の処理情報
        ProcessHistory target = processHistoryListResponse.getProcessHistoryList().get(orderVersionNoData - 1);

        // 最新の受注情報を取得し、セット
        orderHistoryDetailsModel.setModifiedOrderDetailsCommonModel(getOrderInfo(target.getTransactionId(), error));
        orderHistoryDetailsModel.setHistoryDetailsFlag(true);

        // 前回の受注情報を取得
        if (orderVersionNoData > 1) {

            // 前回の処理情報を取得する　※画面で渡された「注文番号連番 - 2」が対象の処理情報
            ProcessHistory original = processHistoryListResponse.getProcessHistoryList().get(orderVersionNoData - 2);

            // 前回の受注情報を取得し、セット
            orderHistoryDetailsModel.setOriginalOrderDetailsCommonModel(
                            getOrderInfo(original.getTransactionId(), error));

            // 受注詳細情報反映
            this.orderHistorydetailsHelper.toPage(
                            orderHistoryDetailsModel, orderHistoryDetailsModel.getModifiedOrderDetailsCommonModel());

            List<OrderSlipResponseItemList> newOrderGoodsUpdateItems =
                            orderHistoryDetailsModel.getModifiedOrderDetailsCommonModel()
                                                    .getOrderSlipResponse()
                                                    .getItemList();
            List<OrderSlipResponseItemList> oldOrderGoodsUpdateItems =
                            orderHistoryDetailsModel.getOriginalOrderDetailsCommonModel()
                                                    .getOrderSlipResponse()
                                                    .getItemList();
            if (newOrderGoodsUpdateItems.size() != oldOrderGoodsUpdateItems.size()) {
                int diffSize = newOrderGoodsUpdateItems.size() - oldOrderGoodsUpdateItems.size();
                if (diffSize > 0) {
                    for (int i = 0; i < diffSize; i++) {
                        oldOrderGoodsUpdateItems.add(0, new OrderSlipResponseItemList());
                    }
                }
            }
            this.orderHistorydetailsHelper.setDiff(orderHistoryDetailsModel);

        } else {
            // 受注詳細情報反映
            this.orderHistorydetailsHelper.toPage(
                            orderHistoryDetailsModel, orderHistoryDetailsModel.getModifiedOrderDetailsCommonModel());
        }

        return "order/details/historydetails";
    }

    /**
     * 受注処理履歴画面へ遷移<br/>
     *
     * @param orderHistoryDetailsModel
     * @return 受注処理履歴画面
     */
    @PostMapping(value = "/", params = "doProcesshistory")
    public String doProcesshistory(OrderHistoryDetailsModel orderHistoryDetailsModel) {
        return "redirect:/order/details/processhistory/?orderCode=" + orderHistoryDetailsModel.getOrderCode();
    }

    /**
     * 受注情報を取得
     *
     * @param transactionId 取引ID
     * @param error
     * @return orderDetailsCommonModel 受注詳細共通モデル
     */
    private OrderDetailsCommonModel getOrderInfo(String transactionId, BindingResult error) {

        OrderDetailsCommonModel orderDetailsCommonModel = new OrderDetailsCommonModel();

        // 各種伝票情報と注文時の情報を取得
        try {
            GetOrderReceivedRequest getOrderReceivedRequest = new GetOrderReceivedRequest();
            getOrderReceivedRequest.setTransactionId(transactionId);
            OrderReceivedResponse orderReceivedResponse = this.orderReceivedApi.get(getOrderReceivedRequest);

            // 受注情報が存在しない場合はエラー
            if (ObjectUtils.isEmpty(orderReceivedResponse)) {
                throwMessage(MSGCD_REFERER_FAIL);
            }

            ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();
            shippingSlipGetRequest.setTransactionId(transactionId);
            ShippingSlipResponse shippingSlipResponse = this.shippingSlipApi.get(shippingSlipGetRequest);

            BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
            billingSlipGetRequest.setTransactionId(transactionId);
            BillingSlipResponse billingSlipResponse = this.billingSlipApi.get(billingSlipGetRequest);

            SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
            salesSlipGetRequest.setTransactionId(transactionId);
            SalesSlipResponse salesSlipResponse = this.salesSlipApi.get(salesSlipGetRequest);

            OrderSlipGetRequest orderSlipSlipGetRequest = new OrderSlipGetRequest();
            orderSlipSlipGetRequest.setTransactionId(transactionId);
            OrderSlipResponse orderSlipResponse = this.orderSlipApi.get(orderSlipSlipGetRequest);

            CustomerResponse customerResponse = this.customerApi.getByMemberinfoSeq(
                            this.conversionUtility.toInteger(orderReceivedResponse.getCustomerId()));

            // 伝票情報、顧客情報が存在しない場合はエラー
            if (ObjectUtils.isEmpty(shippingSlipResponse) || ObjectUtils.isEmpty(billingSlipResponse)
                || ObjectUtils.isEmpty(salesSlipResponse) || ObjectUtils.isEmpty(orderSlipResponse)
                || ObjectUtils.isEmpty(customerResponse)) {
                throwMessage(MSGCD_REFERER_FAIL);
            }

            // 顧客受注件数
            GetOrderReceivedCountRequest getOrderReceivedCountRequest = new GetOrderReceivedCountRequest();
            getOrderReceivedCountRequest.setCustomerId(
                            this.conversionUtility.toString(customerResponse.getMemberInfoSeq()));
            OrderReceivedCountResponse orderReceivedCountResponse =
                            this.orderReceivedApi.getOrderReceivedCount(getOrderReceivedCountRequest);

            // 顧客住所情報の取得
            AddressBookAddressResponse customerAddressResponse =
                            this.addressBookApi.getAddressById(customerResponse.getMemberInfoAddressId());

            // お届け先住所情報の取得
            AddressBookAddressResponse shippingAddressResponse =
                            this.addressBookApi.getAddressById(shippingSlipResponse.getShippingAddressId());

            // 請求先住所情報の取得
            AddressBookAddressResponse billingAddressResponse =
                            this.addressBookApi.getAddressById(billingSlipResponse.getBillingAddressId());

            // 決済方法の取得
            PaymentMethodResponse paymentMethodResponse = this.settlementMethodApi.getBySettlementMethodSeq(
                            this.conversionUtility.toInteger(billingSlipResponse.getPaymentMethodId()));

            if (orderReceivedCountResponse == null || customerAddressResponse == null || shippingAddressResponse == null
                || billingAddressResponse == null || paymentMethodResponse == null) {
                // 顧客受注件数、顧客住所情報の取得、配送先住所情報、請求先住所情報、決済方法、配送方法が取得できない場合
                throwMessage(MSGCD_REFERER_FAIL);
            }

            // マルチペイメント情報を取得
            MulPayBillRequest mulPayBillRequest = new MulPayBillRequest();
            mulPayBillRequest.setOrderCode(orderReceivedResponse.getOrderCode());
            MulPayBillResponse mulPayBillResponse = this.mulpayApi.getByOrderCode(mulPayBillRequest);

            ConvenienceListResponse convenienceListResponse = null;
            if (EnumTypeUtil.getEnumFromValue(
                            HTypeSettlementMethodType.class, paymentMethodResponse.getSettlementMethodType())
                == HTypeSettlementMethodType.LINK_PAYMENT && billingSlipResponse.getPaymentLinkResponse() != null) {
                if ("3".equals(billingSlipResponse.getPaymentLinkResponse().getPayType())) {
                    // コンビニ一覧
                    convenienceListResponse = convenienceApi.get();
                }
            }

            // クーポンが適用されている場合
            CouponResponse couponResponse = null;
            if (salesSlipResponse.getCouponSeq() != null) {
                CouponVersionNoRequest couponVersionNoRequest = new CouponVersionNoRequest();
                couponVersionNoRequest.setCouponSeq(salesSlipResponse.getCouponSeq());
                couponVersionNoRequest.setCouponVersionNo(salesSlipResponse.getCouponVersionNo());
                couponResponse = this.couponApi.getByCouponVersionNo(couponVersionNoRequest);

                if (ObjectUtils.isEmpty(couponResponse)) {
                    // 公開後のクーポン情報は削除不可だが、取得できない場合
                    throwMessage(MSGCD_REFERER_FAIL);
                }
            }

            // 商品サービスから商品詳細リストを取得
            List<GoodsDetailsDto> goodsDtoList = getGoodsDetailsDtoList(orderSlipResponse);

            // 共通モデルに各レスポンス結果を格納し、セッションに保存
            orderDetailsCommonModel.setOrderReceivedResponse(orderReceivedResponse);
            orderDetailsCommonModel.setShippingSlipResponse(shippingSlipResponse);
            orderDetailsCommonModel.setBillingSlipResponse(billingSlipResponse);
            orderDetailsCommonModel.setSalesSlipResponse(salesSlipResponse);
            orderDetailsCommonModel.setOrderSlipResponse(orderSlipResponse);
            orderDetailsCommonModel.setCustomerResponse(customerResponse);
            orderDetailsCommonModel.setOrderReceivedCountResponse(orderReceivedCountResponse);
            orderDetailsCommonModel.setCustomerAddressResponse(customerAddressResponse);
            orderDetailsCommonModel.setShippingAddressResponse(shippingAddressResponse);
            orderDetailsCommonModel.setBillingAddressResponse(billingAddressResponse);
            orderDetailsCommonModel.setPaymentMethodResponse(paymentMethodResponse);
            orderDetailsCommonModel.setMulPayBillResponse(mulPayBillResponse);
            orderDetailsCommonModel.setCouponResponse(couponResponse);
            orderDetailsCommonModel.setGoodsDtoList(goodsDtoList);
            orderDetailsCommonModel.setConvenienceListResponse(convenienceListResponse);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                throwMessage();
            }
        }

        return orderDetailsCommonModel;
    }

    /**
     * 注文票をもとに、商品サービスから商品詳細リストを取得する<br/>
     * 商品サービスから取得した商品詳細リストは、注文商品リストの商品ID順に並び変える
     *
     * @param orderSlipResponse 注文票レスポンス
     * @return ソート後の商品詳細Dtoリスト
     */
    private List<GoodsDetailsDto> getGoodsDetailsDtoList(OrderSlipResponse orderSlipResponse) {

        // 注文商品リストから商品IDリストを作成
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < orderSlipResponse.getItemList().size(); i++) {
            idList.add(this.conversionUtility.toInteger(orderSlipResponse.getItemList().get(i).getItemId()));
        }

        // 商品サービスから商品詳細リストを取得
        ProductDetailListGetRequest request = new ProductDetailListGetRequest();
        request.setGoodsSeqList(idList);
        ProductDetailListResponse response = this.productApi.getDetails(request, null);

        // 商品詳細リストが取得できない場合はエラー
        if (ObjectUtils.isEmpty(response) || CollectionUtils.isEmpty(response.getGoodsDetailsList())) {
            throwMessage(MSGCD_REFERER_FAIL);
        }

        List<GoodsDetailsResponse> goodsDetailsResponseList = response.getGoodsDetailsList();
        goodsDetailsResponseList.sort(Comparator.comparing(item -> idList.indexOf(item.getGoodsSeq())));

        return this.orderHistorydetailsHelper.toProductDetailList(response, orderSlipResponse);
    }

}