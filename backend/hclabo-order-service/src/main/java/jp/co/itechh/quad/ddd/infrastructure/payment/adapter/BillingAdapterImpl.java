/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import com.fasterxml.jackson.core.type.TypeReference;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipCheckRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionCancelRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionCancelResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionDepositedRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionDepositedResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionOpenSalesRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionOpenSalesResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipModernizeRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipOpenRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipOpenResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipRegistRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.CheckBillingSlipForRevisionRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.GetBillingSlipForRevisionByTransactionRevisionIdRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.OpenBillingSlipReviseRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.OpenBillingSlipReviseResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentAgencyEntrustRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentAgencyEntrustSecureResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PublishBillingSlipForRevisionRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.ReAuthBillingSlipForRevisionRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.ReleasePaymentAgencyForRevisionRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.SettingBillingAddressForRevisionRequest;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.CancelResult;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.DepositedResult;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.OpenResult;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.PaymentInfo;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 請求アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class BillingAdapterImpl implements IBillingSlipAdapter {

    /** 請求伝票API */
    private final BillingSlipApi billingSlipApi;

    /** 請求アダプターHelperクラス */
    private final BillingAdapterHelper billingAdapterHelper;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public BillingAdapterImpl(BillingSlipApi billingSlipApi,
                              BillingAdapterHelper billingAdapterHelper,
                              ConversionUtility conversionUtility,
                              HeaderParamsUtility headerParamsUtil) {
        this.billingSlipApi = billingSlipApi;
        this.billingAdapterHelper = billingAdapterHelper;
        this.conversionUtility = conversionUtility;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.billingSlipApi.getApiClient());
    }

    /**
     * 決済サービス<br/>
     * 請求伝票発行
     *
     * @param transactionId 取引ID
     * @param orderCode     受注コード
     * @param addressId     住所ID
     */
    @Override
    public void publishBillingSlip(TransactionId transactionId, OrderCode orderCode, String addressId) {

        BillingSlipRegistRequest billingSlipRegistRequest = new BillingSlipRegistRequest();
        billingSlipRegistRequest.setTransactionId(transactionId.getValue());
        billingSlipRegistRequest.setOrderCode(orderCode.getValue());

        billingSlipApi.regist(addressId, billingSlipRegistRequest);
    }

    /**
     * 決済マイクロサービス<br/>
     * 請求伝票最新化
     *
     * @param transactionId 取引ID
     */
    @Override
    public void modernizeBillingSlip(TransactionId transactionId) {

        BillingSlipModernizeRequest billingSlipModernizeRequest = new BillingSlipModernizeRequest();
        billingSlipModernizeRequest.setTransactionId(transactionId.getValue());

        billingSlipApi.modernize(billingSlipModernizeRequest);
    }

    /**
     * 決済マイクロサービス<br/>
     * 請求伝票チェック
     *
     * @param transactionId 取引ID
     */
    @Override
    public void checkBillingSlip(TransactionId transactionId) {

        BillingSlipCheckRequest billingSlipCheckRequest = new BillingSlipCheckRequest();
        billingSlipCheckRequest.setTransactionId(transactionId.getValue());

        billingSlipApi.check(billingSlipCheckRequest);
    }

    /**
     * 決済マイクロサービス<br/>
     * 決済代行請求委託
     *
     * @param transactionId            取引ID
     * @param securityCode             セキュリティコード
     * @param callBackType             3D本人認証結果コールバック方法
     * @param creditTdResultReceiveUrl ３D本人認証結果受け取り用URL
     * @param paymentLinkReturnUrl
     * @return 3Dセキュア決済以外の場合 ... null / 3Dセキュア決済の場合 ... PaymentInfo
     */
    @Override
    public PaymentInfo entrustPaymentAgency(TransactionId transactionId,
                                            String securityCode,
                                            String callBackType,
                                            String creditTdResultReceiveUrl,
                                            String paymentLinkReturnUrl) {

        PaymentAgencyEntrustRequest paymentAgencyEntrustRequest =
                        billingAdapterHelper.toPaymentAgencyEntrustRequest(transactionId.getValue(), securityCode,
                                                                           callBackType, creditTdResultReceiveUrl,
                                                                           paymentLinkReturnUrl
                                                                          );

        ResponseEntity responseEntity = billingSlipApi.entrustPaymentAgencyWithHttpInfo(paymentAgencyEntrustRequest);

        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return null;
        } else if (HttpStatus.ACCEPTED.equals(responseEntity.getStatusCode())) {
            PaymentAgencyEntrustSecureResponse secureResponse = conversionUtility.toObject(new TypeReference<>() {
            }, responseEntity.getBody());
            int statusCode = HttpStatus.ACCEPTED.value();
            return billingAdapterHelper.toSecureInfo(secureResponse, statusCode);
        } else if (HttpStatus.CREATED.equals(responseEntity.getStatusCode())) {
            PaymentAgencyEntrustSecureResponse secureResponse = conversionUtility.toObject(new TypeReference<>() {
            }, responseEntity.getBody());
            int statusCode = HttpStatus.CREATED.value();
            return billingAdapterHelper.toSecureInfo(secureResponse, statusCode);
        }

        return null;
    }

    /**
     * 決済マイクロサービス<br/>
     * 請求伝票確定
     *
     * @param transactionId 取引ID
     * @return OpenResult 確定結果
     */
    @Override
    public OpenResult openBillingSlip(TransactionId transactionId) {

        BillingSlipOpenRequest billingSlipOpenRequest = new BillingSlipOpenRequest();
        billingSlipOpenRequest.setTransactionId(transactionId.getValue());

        BillingSlipOpenResponse billingSlipOpenResponse = billingSlipApi.open(billingSlipOpenRequest);

        return billingAdapterHelper.toOpenResult(billingSlipOpenResponse);
    }

    /**
     * 改訂用請求伝票取消
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param revisionOpenFlag      改訂確定フラグ
     * @return CancelResult
     */
    @Override
    public CancelResult cancelBillingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                     Boolean revisionOpenFlag) {

        BillingSlipForRevisionCancelRequest billingSlipForRevisionCancelRequest =
                        new BillingSlipForRevisionCancelRequest();

        billingSlipForRevisionCancelRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        billingSlipForRevisionCancelRequest.setRevisionOpenFlag(revisionOpenFlag);

        BillingSlipForRevisionCancelResponse cancelResponse =
                        billingSlipApi.cancelForRevision(billingSlipForRevisionCancelRequest);

        CancelResult cancelResult = null;
        if (cancelResponse != null) {
            cancelResult = new CancelResult();
            cancelResult.setPaidFlag(Boolean.TRUE.equals(cancelResponse.getPaidFlag()));
            cancelResult.setInsufficientMoneyFlag(Boolean.TRUE.equals(cancelResponse.getInsufficientMoneyFlag()));
            cancelResult.setOverMoneyFlag(Boolean.TRUE.equals(cancelResponse.getOverFlag()));
            cancelResult.setGmoCommunicationFlag(Boolean.TRUE.equals(cancelResponse.getGmoCommunicationFlag()));
            cancelResult.setWarningMessage(
                            billingAdapterHelper.toTransactionWarningMessageMap(cancelResponse.getWarningMessage()));
        }
        return cancelResult;
    }

    /**
     * 改訂用売上確定
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return BillingSlipForRevisionOpenSalesResponse 売上結果
     */
    @Override
    public BillingSlipForRevisionOpenSalesResponse openSalesForRevision(TransactionRevisionId transactionRevisionId,
                                                                        Boolean revisionOpenFlag) {

        BillingSlipForRevisionOpenSalesRequest billingSlipForRevisionOpenSalesRequest =
                        new BillingSlipForRevisionOpenSalesRequest();

        billingSlipForRevisionOpenSalesRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        billingSlipForRevisionOpenSalesRequest.setRevisionOpenFlag(revisionOpenFlag);

        return billingSlipApi.openSalesForRevision(billingSlipForRevisionOpenSalesRequest);
    }

    /**
     * 改訂用請求伝票発行
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    @Override
    public void publishBillingSlipForRevision(TransactionId transactionId,
                                              TransactionRevisionId transactionRevisionId) {

        PublishBillingSlipForRevisionRequest publishBillingSlipForRevisionRequest =
                        billingAdapterHelper.toPublishBillingSlipForRevisionRequest(transactionId.getValue(),
                                                                                    transactionRevisionId.getValue()
                                                                                   );

        billingSlipApi.publishBillingSlipForRevision(publishBillingSlipForRevisionRequest);
    }

    /**
     * 改訂用請求伝票チェック
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param transactionId         改訂元取引ID
     */
    @Override
    public void checkBillingSlipForRevision(TransactionRevisionId transactionRevisionId, TransactionId transactionId) {

        CheckBillingSlipForRevisionRequest checkBillingSlipForRevisionRequest =
                        new CheckBillingSlipForRevisionRequest();

        checkBillingSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());

        billingSlipApi.checkBillingSlipForRevision(checkBillingSlipForRevisionRequest);
    }

    /**
     * 改訂用請求伝票の請求先住所更新
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param billingAddressId      請求先住所ID
     */
    @Override
    public void updateBillingAddressOfBillingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                             String billingAddressId) {

        SettingBillingAddressForRevisionRequest settingBillingAddressForRevisionRequest =
                        billingAdapterHelper.toUpdateBillingAddressOfBillingSlipForRevision(
                                        transactionRevisionId.getValue(), billingAddressId);

        billingSlipApi.settingBillingAddressForRevision(settingBillingAddressForRevisionRequest);
    }

    /**
     * 改訂用請求伝票を確定
     *
     * @param transactionRevisionId       改訂用取引ID
     * @param inventorySettlementSkipFlag 在庫決済スキップフラグ
     * @return OpenBillingSlipReviseResponse 改訂用請求伝票確定結果
     */
    @Override
    public OpenBillingSlipReviseResponse openBillingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                                    Boolean inventorySettlementSkipFlag) {

        OpenBillingSlipReviseRequest openBillingSlipReviseRequest = new OpenBillingSlipReviseRequest();

        openBillingSlipReviseRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        openBillingSlipReviseRequest.setSettlementSkipFlag(inventorySettlementSkipFlag);

        return billingSlipApi.openBillingSlipRevise(openBillingSlipReviseRequest);
    }

    /**
     * 改訂用請求伝票の再オーソリを実行
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    @Override
    public void doReAuthBillingSlipForRevision(TransactionRevisionId transactionRevisionId, boolean revisionOpenFlag) {

        ReAuthBillingSlipForRevisionRequest reAuthBillingSlipForRevisionRequest =
                        new ReAuthBillingSlipForRevisionRequest();
        reAuthBillingSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        reAuthBillingSlipForRevisionRequest.setRevisionOpenFlag(revisionOpenFlag);

        billingSlipApi.doReAuthBillingSlipForRevision(reAuthBillingSlipForRevisionRequest);
    }

    /**
     * 改訂用請求伝票の決済代行連携解除設定を実行
     *
     * @param transactionRevisionId    改訂用取引ID
     * @param paymentAgencyReleaseFlag 決済代行連携解除フラグ
     */
    @Override
    public void settingReleasePaymentAgencyBillingSlipForRevision(TransactionRevisionId transactionRevisionId,
                                                                  boolean paymentAgencyReleaseFlag) {

        ReleasePaymentAgencyForRevisionRequest releasePaymentAgencyForRevisionRequest =
                        new ReleasePaymentAgencyForRevisionRequest();
        releasePaymentAgencyForRevisionRequest.setTransactionRevisionId(transactionRevisionId.getValue());
        releasePaymentAgencyForRevisionRequest.setPaymentAgencyReleaseFlag(paymentAgencyReleaseFlag);

        billingSlipApi.settingReleasePaymentAgencyBillingSlipForRevision(releasePaymentAgencyForRevisionRequest);
    }

    /**
     * 改訂用請求伝票を入金済みにする
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param mulPayResultSeq
     * @return 結果
     */
    @Override
    public DepositedResult depositedForRevision(String transactionRevisionId, Integer mulPayResultSeq) {

        BillingSlipForRevisionDepositedRequest billingSlipForRevisionDepositedRequest =
                        new BillingSlipForRevisionDepositedRequest();
        billingSlipForRevisionDepositedRequest.setTransactionRevisionId(transactionRevisionId);
        billingSlipForRevisionDepositedRequest.setMulPayResultSeq(mulPayResultSeq);

        BillingSlipForRevisionDepositedResponse response =
                        billingSlipApi.depositedForRevision(billingSlipForRevisionDepositedRequest);
        DepositedResult result = null;
        if (response != null) {
            result = new DepositedResult();
            result.setInsufficientMoneyFlag(Boolean.TRUE.equals(response.getInsufficientMoneyFlag()));
            result.setOverMoneyFlag(Boolean.TRUE.equals(response.getOverMoneyFlag()));
        }

        return result;
    }

    /**
     * 請求伝票取得
     * ※注文決済情報含む
     *
     * @param transactionId 取引ID
     * @return BillingSlip
     */
    @Override
    public BillingSlip getBillingSlip(String transactionId) {

        BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
        billingSlipGetRequest.setTransactionId(transactionId);

        BillingSlipResponse billingSlipResponse = billingSlipApi.get(billingSlipGetRequest);

        // 戻り値
        BillingSlip billingSlip = null;
        if (billingSlipResponse != null) {
            billingSlip = new BillingSlip();
            if (billingSlipResponse.getPaymentLinkResponse() != null) {
                billingSlip.setLinkPayType(billingSlipResponse.getPaymentLinkResponse().getLinkPayType());
            }
        }
        return billingSlip;
    }

    /**
     * 改訂用取引に紐づく改訂用請求伝票取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return 改訂用取引に紐づく改訂用請求伝票
     */
    @Override
    public BillingSlipForRevisionByTransactionRevisionIdResponse getBillingSlipForRevision(TransactionRevisionId transactionRevisionId) {

        GetBillingSlipForRevisionByTransactionRevisionIdRequest request =
                        new GetBillingSlipForRevisionByTransactionRevisionIdRequest();
        request.setTransactionRevisionId(transactionRevisionId.getValue());
        return billingSlipApi.getBillingSlipForRevisionByTransactionRevisionId(request);
    }
}