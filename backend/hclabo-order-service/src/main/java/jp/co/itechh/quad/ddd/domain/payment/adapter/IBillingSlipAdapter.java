/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionOpenSalesResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.OpenBillingSlipReviseResponse;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.CancelResult;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.DepositedResult;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.OpenResult;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.PaymentInfo;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;

/**
 * 請求伝票アダプター</br>
 * 決済マイクロサービス
 */
public interface IBillingSlipAdapter {

    /**
     * 請求伝票発行
     *
     * @param transactionId 取引ID
     * @param orderCode     受注コード
     * @param addressId     住所ID
     */
    void publishBillingSlip(TransactionId transactionId, OrderCode orderCode, String addressId);

    /**
     * 請求伝票最新化
     *
     * @param transactionId 取引ID
     */
    void modernizeBillingSlip(TransactionId transactionId);

    /**
     * 請求伝票チェック
     *
     * @param transactionId 取引ID
     */
    void checkBillingSlip(TransactionId transactionId);

    /**
     * 決済代行請求委託
     *
     * @param transactionId            取引ID
     * @param securityCode             セキュリティコード
     * @param callBackType             3D本人認証結果コールバック方法
     * @param creditTdResultReceiveUrl ３D本人認証結果受け取り用URL
     * @param paymentLinkReturnUrl
     * @return 3Dセキュア決済以外の場合 ... null / 3Dセキュア決済の場合 ... PaymentInfo
     */
    PaymentInfo entrustPaymentAgency(TransactionId transactionId,
                                     String securityCode,
                                     String callBackType,
                                     String creditTdResultReceiveUrl,
                                     String paymentLinkReturnUrl);

    /**
     * 請求伝票確定
     *
     * @param transactionId 取引ID
     * @return OpenResult 確定結果
     */
    OpenResult openBillingSlip(TransactionId transactionId);

    /**
     * 改訂用請求伝票取消
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param revisionOpenFlag      改訂確定フラグ
     * @return CancelResult
     */
    CancelResult cancelBillingSlipForRevision(TransactionRevisionId transactionRevisionId, Boolean revisionOpenFlag);

    /**
     * 改訂用売上確定
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return BillingSlipForRevisionOpenSalesResponse 売上結果
     */
    BillingSlipForRevisionOpenSalesResponse openSalesForRevision(TransactionRevisionId transactionRevisionId,
                                                                 Boolean revisionOpenFlag);

    /**
     * 改訂用請求伝票発行
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    void publishBillingSlipForRevision(TransactionId transactionId, TransactionRevisionId transactionRevisionId);

    /**
     * 改訂用請求伝票チェック
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param transactionId         改訂元取引ID
     */
    void checkBillingSlipForRevision(TransactionRevisionId transactionRevisionId, TransactionId transactionId);

    /**
     * 改訂用請求伝票の請求先住所更新
     *
     * @param transactionRevisionId
     * @param billingAddressId
     */
    void updateBillingAddressOfBillingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                      String billingAddressId);

    /**
     * 改訂用請求伝票を確定
     *
     * @param transactionRevisionId
     * @param inventorySettlementSkipFlag
     * @return OpenBillingSlipReviseResponse 改訂用請求伝票確定結果
     */
    OpenBillingSlipReviseResponse openBillingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                             Boolean inventorySettlementSkipFlag);

    /**
     * 改訂用請求伝票の再オーソリを実行
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    void doReAuthBillingSlipForRevision(TransactionRevisionId transactionRevisionId, boolean revisionOpenFlag);

    /**
     * 改訂用請求伝票の決済代行連携解除設定を実行
     *
     * @param transactionRevisionId    改訂用取引ID
     * @param paymentAgencyReleaseFlag 　決済代行連携解除フラグ
     */
    void settingReleasePaymentAgencyBillingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                           boolean paymentAgencyReleaseFlag);

    /**
     * 改訂用請求伝票を入金済みにする
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param mulPayResultSeq
     */
    DepositedResult depositedForRevision(String transactionRevisionId, Integer mulPayResultSeq);

    /**
     * 請求伝票取得
     * ※注文決済情報含む
     *
     * @param transactionId 取引ID
     * @return 請求伝票
     */
    BillingSlip getBillingSlip(String transactionId);

    /**
     * 改訂用取引に紐づく改訂用請求伝票取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return 改訂用取引に紐づく改訂用請求伝票
     */
    BillingSlipForRevisionByTransactionRevisionIdResponse getBillingSlipForRevision(TransactionRevisionId transactionRevisionId);
}