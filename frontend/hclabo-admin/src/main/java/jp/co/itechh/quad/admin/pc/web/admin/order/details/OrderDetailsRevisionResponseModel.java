/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 改訂用受注詳細共通 レスポンスモデル<br/>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderDetailsRevisionResponseModel implements Serializable {

    /** 改訂用取引ID */
    private String transactionRevisionId;

    /** 改訂元取引ID */
    private String transactionId;

    /** 受注レスポンス */
    OrderReceivedResponse orderReceivedResponse;

    /** 改訂用配送伝票レスポンス */
    ShippingSlipResponse shippingSlipForRevisionResponse;

    /** 改訂用請求伝票レスポンス */
    BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipForRevisionResponse;

    /** 改訂用販売伝票レスポンス */
    GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse;

    /** 改訂元販売伝票レスポンス */
    SalesSlipResponse salesSlipResponse;

    /** 改訂用配注文票レスポンス */
    OrderSlipForRevisionResponse orderSlipForRevisionResponse;

    /** 顧客レスポンス マスタ情報 */
    CustomerResponse customerResponse;

    /** お届け先住所レスポンス マスタ情報 */
    AddressBookAddressResponse shippingAddressResponse;

    /** 請求先住所レスポンス マスタ情報 */
    AddressBookAddressResponse billingAddressResponse;

    /** 決済方法レスポンス マスタ情報 */
    PaymentMethodResponse paymentMethodResponse;

    /** 配送方法レスポンス マスタ情報 */
    ShippingMethodResponse shippingMethodResponse;

    /** マルチペイメント請求レスポンス マスタ情報 */
    MulPayBillResponse mulPayBillResponse;

    /** クーポン情報レスポンス マスタ情報 */
    CouponResponse couponResponse;

    /** 商品詳細Dtoリスト マスタ情報 */
    List<GoodsDetailsDto> goodsDtoList;

    /** コンビニ一覧レスポンス */
    ConvenienceListResponse convenienceListResponse;

    /** 検査キットリストレスポンス */
    ExamKitListResponse examKitListResponse;

}
