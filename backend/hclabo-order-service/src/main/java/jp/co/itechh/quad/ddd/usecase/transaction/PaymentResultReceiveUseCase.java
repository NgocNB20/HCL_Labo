/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IMulPayAdapter;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultRequest;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionReflectDepositedRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 決済代行から入金結果受取
 */
@Service
public class PaymentResultReceiveUseCase {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentResultReceiveUseCase.class);

    /** 処理対象決済方法一覧（リンク決済後日払いのみ） */
    private static final List<String> TARGET_PAYMENTS = List.of("3", "4", "36");

    /** リクエストパラメータ名配列（共通項目） */
    private static final String[] requestParamsCommon =
                    new String[] {"ShopID", "ShopPass", "AccessID", "AccessPass", "OrderID", "Status", "Amount", "Tax",
                                    "TranDate", "ErrCode", "ErrInfo", "PayType"};

    /** マルチペイメントアダプター */
    private final IMulPayAdapter mulpayAdapter;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 取引API */
    private final TransactionApi transactionApi;

    /** コンストラクタ */
    @Autowired
    public PaymentResultReceiveUseCase(IMulPayAdapter mulpayAdapter,
                                       ConversionUtility conversionUtility,
                                       TransactionApi transactionApi) {
        this.mulpayAdapter = mulpayAdapter;
        this.conversionUtility = conversionUtility;
        this.transactionApi = transactionApi;
    }

    /**
     * 決済代行から入金結果受取
     *
     * @param request リクエスト
     * @return boolean 要入金反映処理フラグ
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Boolean receivePaymentResult(HttpServletRequest request) {

        // 受信情報の妥当性チェック
        if (!checkRequiredParameter(request)) {
            return false;
        }

        //GMOリクエストパラメータよりパラメータを取り出してマルチペイメント決済結果登録パラメータ作成
        MulPayResultRequest mulPayResultRequest = createMulPayRegistRequest(request);

        // 後日払いの場合
        // マルチペイメント決済結果登録
        Boolean requiredReflectionProcessingFlag = mulpayAdapter.mulPayRegistResult(mulPayResultRequest);
        return Boolean.TRUE.equals(requiredReflectionProcessingFlag);

    }

    /**
     * ユースケース処理実行後非同期処理
     *
     * @param requiredReflectionProcessingFlag
     */
    public void asyncAfterProcess(boolean requiredReflectionProcessingFlag, HttpServletRequest request) {

        // 要入金反映処理フラグ= true の場合
        if (requiredReflectionProcessingFlag) {

            // HM入金結果データを確認して受注を入金済みにする
            TransactionReflectDepositedRequest transactionReflectDepositedRequest =
                            new TransactionReflectDepositedRequest();
            transactionReflectDepositedRequest.setOrderCode(convBlankToNull(request.getParameter("OrderID")));
            try {
                // 非同期処理（MQは同サービスでもAPI呼出し）
                transactionApi.reflectDeposited(transactionReflectDepositedRequest);
            } catch (HttpServerErrorException | HttpClientErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                throw e;
            }
        }
    }

    /**
     * マルチペイメント決済結果通知受付リクエスト作成
     *
     * @param req リクエスト
     * @return マルチペイメント決済結果通知受付リクエスト
     */
    protected MulPayResultRequest createMulPayRegistRequest(HttpServletRequest req) {

        MulPayResultRequest mulPayResultRequest = new MulPayResultRequest();

        mulPayResultRequest.setReceiveMode("PASSIVE");
        mulPayResultRequest.setOrderId(convBlankToNull(req.getParameter("OrderID")));
        mulPayResultRequest.setStatus(convBlankToNull(req.getParameter("Status")));
        mulPayResultRequest.setJobCd(convBlankToNull(req.getParameter("JobCd")));
        mulPayResultRequest.setAmount(conversionUtility.toBigDecimal(convBlankToNull(req.getParameter("Amount"))));
        mulPayResultRequest.setTax(conversionUtility.toBigDecimal(convBlankToNull(req.getParameter("Tax"))));
        mulPayResultRequest.setSiteId(convBlankToNull(req.getParameter("SiteID")));
        mulPayResultRequest.setMemberId(convBlankToNull(req.getParameter("MemberID")));
        mulPayResultRequest.setCardNumber(convBlankToNull(req.getParameter("CardNo")));
        mulPayResultRequest.setExpire(convBlankToNull(req.getParameter("Expire")));
        mulPayResultRequest.setCurrency(convBlankToNull(req.getParameter("Currency")));
        mulPayResultRequest.setForward(convBlankToNull(req.getParameter("Forward")));
        mulPayResultRequest.setMethod(convBlankToNull(req.getParameter("Method")));
        mulPayResultRequest.setPayTimes(conversionUtility.toInteger(convBlankToNull(req.getParameter("PayTimes"))));
        mulPayResultRequest.setTranId(convBlankToNull(req.getParameter("TranId")));
        mulPayResultRequest.setApprove(convBlankToNull(req.getParameter("Approve")));
        mulPayResultRequest.setTranDate(convBlankToNull(req.getParameter("TranDate")));
        mulPayResultRequest.setErrCode(convBlankToNull(req.getParameter("ErrCode")));
        mulPayResultRequest.setErrInfo(convBlankToNull(req.getParameter("ErrInfo")));
        mulPayResultRequest.setClientField1(convBlankToNull(req.getParameter("ClientField1")));
        mulPayResultRequest.setClientField2(convBlankToNull(req.getParameter("ClientField2")));
        mulPayResultRequest.setClientField3(convBlankToNull(req.getParameter("ClientField3")));
        mulPayResultRequest.setPayType(convBlankToNull(req.getParameter("PayType")));
        mulPayResultRequest.setCvsCode(convBlankToNull(req.getParameter("CvsCode")));
        mulPayResultRequest.setCvsConfNo(convBlankToNull(req.getParameter("CvsConfNo")));
        mulPayResultRequest.setCvsReceiptNo(convBlankToNull(req.getParameter("CvsReceiptNo")));
        mulPayResultRequest.setCustId(convBlankToNull(req.getParameter("CustID")));
        mulPayResultRequest.setBkCode(convBlankToNull(req.getParameter("BkCode")));
        mulPayResultRequest.setConfNo(convBlankToNull(req.getParameter("ConfNo")));
        mulPayResultRequest.setPaymentTerm(convBlankToNull(req.getParameter("PaymentTerm")));
        mulPayResultRequest.setEncryptReceiptNo(convBlankToNull(req.getParameter("EncryptReceiptNo")));
        mulPayResultRequest.setFinishDate(convBlankToNull(req.getParameter("FinishDate")));
        mulPayResultRequest.setReceiptDate(convBlankToNull(req.getParameter("ReceiptDate")));

        if ("36".equals(mulPayResultRequest.getPayType())) {
            // 銀行振込(バーチャル口座 あおぞら)
            mulPayResultRequest.setRequestAmount(
                            conversionUtility.toBigDecimal(convBlankToNull(req.getParameter("GanbRequestAmount"))));
            mulPayResultRequest.setExpireDate(convBlankToNull(req.getParameter("GanbExpireDate")));
            mulPayResultRequest.setTradeReason(convBlankToNull(req.getParameter("GanbTradeReason")));
            mulPayResultRequest.setTradeClientName(convBlankToNull(req.getParameter("GanbTradeClientName")));
            mulPayResultRequest.setTradeClientMailAddress(
                            convBlankToNull(req.getParameter("GanbTradeClientMailaddress")));
            mulPayResultRequest.setBankCode(convBlankToNull(req.getParameter("GanbBankCode")));
            mulPayResultRequest.setBankName(convBlankToNull(req.getParameter("GanbBankName")));
            mulPayResultRequest.setBranchCode(convBlankToNull(req.getParameter("GanbBranchCode")));
            mulPayResultRequest.setBranchName(convBlankToNull(req.getParameter("GanbBranchName")));
            mulPayResultRequest.setAccountType(convBlankToNull(req.getParameter("GanbAccountType")));
            mulPayResultRequest.setAccountNumber(convBlankToNull(req.getParameter("GanbAccountNumber")));
            mulPayResultRequest.setInSettlementDate(convBlankToNull(req.getParameter("GanbInSettlementDate")));
            mulPayResultRequest.setInAmount(
                            conversionUtility.toBigDecimal(convBlankToNull(req.getParameter("GanbInAmount"))));
            mulPayResultRequest.setInClientName(convBlankToNull(req.getParameter("GanbInClientName")));
            mulPayResultRequest.setGanbProcessType(convBlankToNull(req.getParameter("GanbProcessType")));
            mulPayResultRequest.setGanbAccountHolderName(convBlankToNull(req.getParameter("GanbAccountHolderName")));
            mulPayResultRequest.setGanbInRemittingBankName(
                            convBlankToNull(req.getParameter("GanbInRemittingBankName")));
            mulPayResultRequest.setGanbInRemittingBranchName(
                            convBlankToNull(req.getParameter("GanbInRemittingBranchName")));
            mulPayResultRequest.setGanbTotalTransferAmount(conversionUtility.toBigDecimal(
                            convBlankToNull(req.getParameter("GanbTotalTransferAmount"))));
            mulPayResultRequest.setGanbTotalTransferCount(
                            conversionUtility.toInteger(convBlankToNull(req.getParameter("GanbTotalTransferCount"))));
        }

        return mulPayResultRequest;
    }

    /**
     * 処理対象/必須パラメータチェック
     * <pre>
     * 各決済方法別の必須項目がリクエストパラメータで送られてきていることを確認する。
     * </pre>
     *
     * @param req リクエストパラメータ
     * @return true or false
     */
    protected boolean checkRequiredParameter(HttpServletRequest req) {

        // 対象決済方法チェック
        if (!TARGET_PAYMENTS.contains(convBlankToNull(req.getParameter("PayType")))) {
            return false;
        }

        if (!this.checkParameter(req, requestParamsCommon)) {
            return false;
        }

        return true;
    }

    /**
     * 汎用パラメータチェック
     * <pre>
     * リクエストパラメータが送られてきていることを確認する。
     * いずれか一つのパラメータが存在しなければ falseを返す。
     * </pre>
     *
     * @param req        リクエスト
     * @param paramCheck リクエストパラメータ
     * @return true or false
     */
    protected boolean checkParameter(HttpServletRequest req, String[] paramCheck) {

        for (int i = 0; i < paramCheck.length; ++i) {
            String param = paramCheck[i];
            if (req.getParameter(param) == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * 文字列編集処理（空文字→Null）
     *
     * @param value 変換対象文字列
     * @return 変換後文字列
     */
    protected String convBlankToNull(String value) {
        return StringUtils.defaultIfEmpty(value, null);
    }
}