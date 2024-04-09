/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */


package jp.co.itechh.quad.core.logic.order.impl;

import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.core.dto.order.OrderReceivedDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.order.OrderReceivedGetLogic;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoDetailsGetService;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

/**
 * 受注取得ロジック
 * TODO 外部APIを呼び出すので、本来はロジックではなく、アダプターが正しい
 *
 * @author kimura
 */
@Component
public class OrderReceivedGetLogicImpl extends AbstractShopLogic implements OrderReceivedGetLogic {

    /** 会員詳細取得サービス */
    private final MemberInfoDetailsGetService memberInfoDetailsGetService;

    /** 受注API */
    private final OrderReceivedApi orderReceivedApi;

    /** 配送伝票API */
    private final ShippingSlipApi shippingSlipApi;

    /** 請求伝票API */
    private final BillingSlipApi billingSlipApi;

    /** 販売伝票API */
    private final SalesSlipApi salesSlipApi;

    /** 決済方法API */
    private final SettlementMethodApi settlementMethodApi;

    /** 配送方法API */
    private final ShippingMethodApi shippingMethodApi;

    /** マルペイAPI */
    private final MulpayApi mulpayApi;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    public OrderReceivedGetLogicImpl(MemberInfoDetailsGetService memberInfoDetailsGetService,
                                     OrderReceivedApi orderReceivedApi,
                                     ShippingSlipApi shippingSlipApi,
                                     BillingSlipApi billingSlipApi,
                                     SalesSlipApi salesSlipApi,
                                     SettlementMethodApi settlementMethodApi,
                                     ShippingMethodApi shippingMethodApi,
                                     MulpayApi mulpayApi,
                                     ConversionUtility conversionUtility) {
        this.memberInfoDetailsGetService = memberInfoDetailsGetService;
        this.orderReceivedApi = orderReceivedApi;
        this.shippingSlipApi = shippingSlipApi;
        this.billingSlipApi = billingSlipApi;
        this.salesSlipApi = salesSlipApi;
        this.settlementMethodApi = settlementMethodApi;
        this.shippingMethodApi = shippingMethodApi;
        this.mulpayApi = mulpayApi;
        this.conversionUtility = conversionUtility;
    }

    /**
     * ロジック実行<br/>
     *
     * @param orderCode 受注番号
     * @return 受注Dto
     */
    @Override
    public OrderReceivedDto execute(String orderCode) {

        OrderReceivedResponse orderReceivedResponse = this.orderReceivedApi.getByOrderCode(orderCode);

        // 受注情報が存在しない場合はエラー
        if (ObjectUtils.isEmpty(orderReceivedResponse)) {
            this.throwMessage(MSGCD_ORDERPERSONENTITYDTO_NULL);
        }

        ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();
        shippingSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
        ShippingSlipResponse shippingSlipResponse = this.shippingSlipApi.get(shippingSlipGetRequest);

        BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
        billingSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
        BillingSlipResponse billingSlipResponse = this.billingSlipApi.get(billingSlipGetRequest);

        SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
        salesSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
        SalesSlipResponse salesSlipResponse = this.salesSlipApi.get(salesSlipGetRequest);

        MemberInfoDetailsDto memberInfoDetailsDto = this.memberInfoDetailsGetService.execute(
                        this.conversionUtility.toInteger(orderReceivedResponse.getCustomerId()));

        // 伝票情報、顧客情報が存在しない場合はエラー
        if (ObjectUtils.isEmpty(shippingSlipResponse) || ObjectUtils.isEmpty(billingSlipResponse)
            || ObjectUtils.isEmpty(salesSlipResponse) || ObjectUtils.isEmpty(
                        memberInfoDetailsDto.getMemberInfoEntity())) {
            this.throwMessage(MSGCD_ORDERPERSONENTITYDTO_NULL);
        }

        // 決済方法の取得
        PaymentMethodResponse paymentMethodResponse = this.settlementMethodApi.getBySettlementMethodSeq(
                        this.conversionUtility.toInteger(billingSlipResponse.getPaymentMethodId()));

        // 配送方法の取得
        ShippingMethodResponse shippingMethodResponse = this.shippingMethodApi.getByDeliveryMethodSeq(
                        this.conversionUtility.toInteger(shippingSlipResponse.getShippingMethodId()));

        if (ObjectUtils.isEmpty(paymentMethodResponse) || ObjectUtils.isEmpty(shippingMethodResponse)
            || ObjectUtils.isEmpty(shippingMethodResponse.getDeliveryMethodResponse())) {
            // 顧客住所情報の取得、配送先住所情報、請求先住所情報、決済方法、配送方法が取得できない場合
            this.throwMessage(MSGCD_ORDERPERSONENTITYDTO_NULL);
        }

        // マルチペイメント情報を取得
        MulPayBillRequest mulPayBillRequest = new MulPayBillRequest();
        mulPayBillRequest.setOrderCode(orderReceivedResponse.getOrderCode());

        MulPayBillResponse mulPayBillResponse = this.mulpayApi.getByOrderCode(mulPayBillRequest);

        // dtoに各種レスポンスを格納
        OrderReceivedDto dto = new OrderReceivedDto();
        dto.setOrderReceivedResponse(orderReceivedResponse);
        dto.setShippingSlipResponse(shippingSlipResponse);
        dto.setMemberInfoDetailsDto(memberInfoDetailsDto);
        dto.setSalesSlipResponse(salesSlipResponse);
        dto.setPaymentMethodResponse(paymentMethodResponse);
        dto.setShippingMethodResponse(shippingMethodResponse);
        dto.setMulPayBillResponse(mulPayBillResponse);
        dto.setBillingSlipResponse(billingSlipResponse);

        return dto;
    }
}