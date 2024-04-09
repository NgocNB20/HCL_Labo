package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipOpenResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentAgencyEntrustRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentAgencyEntrustSecureResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PublishBillingSlipForRevisionRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.SettingBillingAddressForRevisionRequest;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.OpenResult;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.PaymentInfo;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 請求アダプターHelperクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class BillingAdapterHelper {

    /**
     * 決済代行委託リクエストに変換
     *
     * @param transactionId            取引ID
     * @param securityCode             セキュリティーコード
     * @param callBackType             3D本人認証結果コールバック方法
     * @param creditTdResultReceiveUrl ３D本人認証結果受け取り用URL
     * @param paymentLinkReturnUrl
     * @return 決済代行委託リクエストに変換
     */
    public PaymentAgencyEntrustRequest toPaymentAgencyEntrustRequest(String transactionId,
                                                                     String securityCode,
                                                                     String callBackType,
                                                                     String creditTdResultReceiveUrl,
                                                                     String paymentLinkReturnUrl) {

        PaymentAgencyEntrustRequest paymentAgencyEntrustRequest = new PaymentAgencyEntrustRequest();

        paymentAgencyEntrustRequest.setTransactionId(transactionId);
        paymentAgencyEntrustRequest.setCallBackType(callBackType);
        paymentAgencyEntrustRequest.setCreditTdResultReceiveUrl(creditTdResultReceiveUrl);
        paymentAgencyEntrustRequest.setSecurityCode(securityCode);
        paymentAgencyEntrustRequest.setPaymentLinkReturnUrl(paymentLinkReturnUrl);

        return paymentAgencyEntrustRequest;
    }

    /**
     * 3Dセキュア認証用情報に変換<br/>
     *
     * @param secureResponse 決済代行本人認証レスポンス
     * @param statusCode     HTTP ステータス コード
     * @return 3Dセキュア認証用情報
     */
    public PaymentInfo toSecureInfo(PaymentAgencyEntrustSecureResponse secureResponse, int statusCode) {

        if (secureResponse == null) {
            return null;
        }

        PaymentInfo secureInfo = new PaymentInfo();
        secureInfo.setRedirectUrl(secureResponse.getRedirectUrl());
        secureInfo.setStatusCode(statusCode);

        return secureInfo;
    }

    /**
     * 改訂用請求伝票発行リクエストに変換
     *
     * @param transactionId         取引ID
     * @param transactionRevisionId 改訂用取引ID
     * @return 改訂用請求伝票発行リクエスト
     */
    public PublishBillingSlipForRevisionRequest toPublishBillingSlipForRevisionRequest(String transactionId,
                                                                                       String transactionRevisionId) {
        PublishBillingSlipForRevisionRequest publishBillingSlipForRevisionRequest =
                        new PublishBillingSlipForRevisionRequest();

        publishBillingSlipForRevisionRequest.setTransactionId(transactionId);
        publishBillingSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId);

        return publishBillingSlipForRevisionRequest;
    }

    /**
     * 改訂用請求伝票請求先更新クリクエストに変換
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param billingAddressId      請求先住所ID
     */
    public SettingBillingAddressForRevisionRequest toUpdateBillingAddressOfBillingSlipForRevision(String transactionRevisionId,
                                                                                                  String billingAddressId) {
        SettingBillingAddressForRevisionRequest settingBillingAddressForRevisionRequest =
                        new SettingBillingAddressForRevisionRequest();

        settingBillingAddressForRevisionRequest.setTransactionRevisionId(transactionRevisionId);
        settingBillingAddressForRevisionRequest.setBillingAddressId(billingAddressId);

        return settingBillingAddressForRevisionRequest;
    }

    /**
     * 請求伝票確定結果に変換
     *
     * @param billingSlipOpenResponse 請求伝票確定レスポンス
     * @return OpenResult 請求伝票確定結果
     */
    public OpenResult toOpenResult(BillingSlipOpenResponse billingSlipOpenResponse) {

        if (billingSlipOpenResponse == null) {
            return null;
        }

        OpenResult openResult = new OpenResult();
        openResult.setPaidFlag(Boolean.TRUE.equals(billingSlipOpenResponse.getPaidFlag()));
        openResult.setNotificationFlag(Boolean.TRUE.equals(billingSlipOpenResponse.getNotificationFlag()));
        openResult.setPreClaimFlag(Boolean.TRUE.equals(billingSlipOpenResponse.getPreClaim()));

        return openResult;
    }

    /**
     * 警告メッセージの変換
     *
     * @param billingSlipMessageMap 警告メッセージ（改訂用請求伝票取消）
     * @return 警告メッセージ（改訂用取引取消）
     */
    public Map<String, List<WarningContent>> toTransactionWarningMessageMap(Map<String, List<jp.co.itechh.quad.billingslip.presentation.api.param.WarningContent>> billingSlipMessageMap) {

        if (MapUtils.isEmpty(billingSlipMessageMap)) {
            return null;
        }

        Map<String, List<WarningContent>> transactionMessageMap = new HashMap<>();
        billingSlipMessageMap.forEach((key, messageList) -> {
            List<WarningContent> warningContentList = new ArrayList<>();

            for (jp.co.itechh.quad.billingslip.presentation.api.param.WarningContent content : messageList) {
                WarningContent transactionContent = new WarningContent();
                transactionContent.setCode(content.getCode());
                transactionContent.setMessage(content.getMessage());

                warningContentList.add(transactionContent);
            }
            transactionMessageMap.put(key, warningContentList);
        });

        return transactionMessageMap;
    }

}