/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.dto.order;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 受注Dto<br/>
 * 各種APIレスポンス結果を格納<br/>
 * 受注系メール送信のコンシューマークラスで利用
 *
 * @author kimura
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderReceivedDto {

    /** 会員詳細Dto */
    MemberInfoDetailsDto memberInfoDetailsDto;

    /** 受注レスポンス */
    OrderReceivedResponse orderReceivedResponse;

    /** 配送伝票レスポンス */
    ShippingSlipResponse shippingSlipResponse;

    /** 販売伝票レスポンス */
    SalesSlipResponse salesSlipResponse;

    /** 決済方法レスポンス */
    PaymentMethodResponse paymentMethodResponse;

    /** 配送方法レスポンス */
    ShippingMethodResponse shippingMethodResponse;

    /** マルチペイメント請求レスポンス */
    MulPayBillResponse mulPayBillResponse;

    /** 請求伝票レスポンス */
    BillingSlipResponse billingSlipResponse;

}
