/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.PaymentInfo;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 取引確定可能確認ユースケース
 */
@Service
public class ConfirmOpenOfTransactionUseCase {

    /** 配送アダプター */
    private final IShippingSlipAdapter shippingAdapter;

    /** 請求アダプター */
    private final IBillingSlipAdapter billingAdapter;

    /** コンストラクタ */
    @Autowired
    public ConfirmOpenOfTransactionUseCase(IShippingSlipAdapter shippingAdapter, IBillingSlipAdapter billingAdapter) {
        this.shippingAdapter = shippingAdapter;
        this.billingAdapter = billingAdapter;
    }

    /**
     * 取引が確定可能か確認する
     *
     * @param transactionId            取引ID
     * @param securityCode             セキュリティコード
     * @param callBackType             3D本人認証結果コールバック方法
     * @param creditTdResultReceiveUrl ３D本人認証結果受け取り用URL
     * @param paymentLinkReturnUrl
     * @return 3Dセキュア決済以外の場合 ... null / 3Dセキュア決済の場合 ... 取引確定可能確認ユースケースDto
     */
    public ConfirmOpenOfTransactionUseCaseDto confirmOpenOfTransaction(String transactionId,
                                                                       String securityCode,
                                                                       String callBackType,
                                                                       String creditTdResultReceiveUrl,
                                                                       String paymentLinkReturnUrl) {

        // ***ここから、各サービスの取引処理を下記の順番で実行 ※3Dセキュア認証/リンク決済の場合、このユースケース(API)はフロントエンドより2回呼び出しされる***
        // ①：配送商品の在庫確保（物流サービス）※2回目呼出しの場合、処理はサービス側で何も処理されずにスキップされる
        // ②：決済代行に請求委託（決済サービス）※2回目呼出しの場合、「GMO取引確定」「注文決済ステータス確定」「(必要な場合に)カード登録」が実施される
        // 　　分岐②-1：決済用情報(3Dセキュアの認証用情報/リンク決済URL)が決済サービスより返却された場合は、その決済用情報を返却する
        //              ※フロントエンドでは決済情報を使用して決済完了後に「途中決済を決済代行に委託」ユースケース(API)を呼び出す
        // 　　分岐②-2：決済用情報が返却されていない場合（認証不要決済）は、nullを返却

        // ①：配送商品の在庫確保
        shippingAdapter.secureShippingProductInventory(new TransactionId(transactionId));

        // ②：決済代行に請求委託
        PaymentInfo paymentInfo = billingAdapter.entrustPaymentAgency(new TransactionId(transactionId), securityCode,
                                                                      callBackType, creditTdResultReceiveUrl,
                                                                      paymentLinkReturnUrl
                                                                     );

        // 決済用情報が返却された場合
        if (paymentInfo != null) {
            // ②-1：決済情報を返却
            return toOpenTransactionUseCaseDto(paymentInfo);
        }

        // ②-2：決済用情報が返却されていない場合はnullを返却
        return null;
    }

    /**
     * ユースケース戻り値への変換処理
     *
     * @param paymentInfo 決済
     * @return dto ユースケース戻り値
     */
    private ConfirmOpenOfTransactionUseCaseDto toOpenTransactionUseCaseDto(PaymentInfo paymentInfo) {

        ConfirmOpenOfTransactionUseCaseDto dto = new ConfirmOpenOfTransactionUseCaseDto();
        dto.setRedirectUrl(paymentInfo.getRedirectUrl());
        dto.setStatusCode(paymentInfo.getStatusCode());

        return dto;
    }

}