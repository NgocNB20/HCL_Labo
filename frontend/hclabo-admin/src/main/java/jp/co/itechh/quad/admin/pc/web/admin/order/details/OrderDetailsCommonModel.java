/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ConfigInfoResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedCountResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 受注詳細共通モデル<br/>
 * 各種APIレスポンスを保持するための共通モデル
 *
 * @author kimura
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderDetailsCommonModel {

    /** 受注レスポンス */
    OrderReceivedResponse orderReceivedResponse;

    /** 配送伝票レスポンス */
    ShippingSlipResponse shippingSlipResponse;

    /** 請求伝票レスポンス */
    BillingSlipResponse billingSlipResponse;

    /** 販売伝票レスポンス */
    SalesSlipResponse salesSlipResponse;

    /** 注文票レスポンス */
    OrderSlipResponse orderSlipResponse;

    /** 顧客レスポンス */
    CustomerResponse customerResponse;

    /** 顧客受注件数レスポンス */
    OrderReceivedCountResponse orderReceivedCountResponse;

    /** 顧客住所レスポンス */
    AddressBookAddressResponse customerAddressResponse;

    /** お届け先住所レスポンス */
    AddressBookAddressResponse shippingAddressResponse;

    /** 請求先住所レスポンス */
    AddressBookAddressResponse billingAddressResponse;

    /** 決済方法レスポンス */
    PaymentMethodResponse paymentMethodResponse;

    /** マルチペイメント請求レスポンス */
    MulPayBillResponse mulPayBillResponse;

    /** クーポン情報レスポンス */
    CouponResponse couponResponse;

    /** 商品詳細Dtoリスト ※レスポンス変換後のもの */
    List<GoodsDetailsDto> goodsDtoList;

    /** コンビニ一覧レスポンス */
    ConvenienceListResponse convenienceListResponse;

    /** 検査キットリストレスポンス */
    ExamKitListResponse examKitListResponse;

    /** 環境設定情報レスポンス */
    ConfigInfoResponse configInfoResponse;

}
