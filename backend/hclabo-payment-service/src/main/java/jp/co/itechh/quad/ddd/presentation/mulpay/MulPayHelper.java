/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */


package jp.co.itechh.quad.ddd.presentation.mulpay;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeProcessedFlag;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayResultEntity;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultConfirmDepositedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * マルチペイメントHelperクラス
 */
@Component
public class MulPayHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     */
    @Autowired
    public MulPayHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * マルペイレスポンスに変換
     *
     * @param mulPayBillEntity マルチペイメント請求
     * @return マルチペイメント請求レスポンス
     */
    public MulPayBillResponse toPaymentMulpayBillResponse(MulPayBillEntity mulPayBillEntity) {

        if (mulPayBillEntity == null) {
            return null;
        }

        MulPayBillResponse mulpayBillResponse = new MulPayBillResponse();
        mulpayBillResponse.setMulPayBillSeq(mulPayBillEntity.getMulPayBillSeq());
        mulpayBillResponse.setPayType(mulPayBillEntity.getPayType());
        mulpayBillResponse.setTranType(mulPayBillEntity.getTranType());
        mulpayBillResponse.setOrderPaymentId(mulPayBillEntity.getOrderPaymentId());
        mulpayBillResponse.setOrderId(mulPayBillEntity.getOrderId());
        mulpayBillResponse.setAccessId(mulPayBillEntity.getAccessId());
        mulpayBillResponse.setAccessPass(mulPayBillEntity.getAccessPass());
        mulpayBillResponse.setJobCd(mulPayBillEntity.getJobCd());
        mulpayBillResponse.setMethod(mulPayBillEntity.getMethod());
        mulpayBillResponse.setPayTimes(mulPayBillEntity.getPayTimes());
        mulpayBillResponse.setSeqMode(mulPayBillEntity.getSeqMode());
        mulpayBillResponse.setCardSeq(mulPayBillEntity.getCardSeq());
        mulpayBillResponse.setAmount(mulPayBillEntity.getAmount());
        mulpayBillResponse.setTax(mulPayBillEntity.getTax());
        mulpayBillResponse.setTdFlag(mulPayBillEntity.getTdFlag());
        mulpayBillResponse.setAsc(mulPayBillEntity.getAcs());
        mulpayBillResponse.setForward(mulPayBillEntity.getForward());
        mulpayBillResponse.setApprove(mulPayBillEntity.getApprove());
        mulpayBillResponse.setTranId(mulPayBillEntity.getTranId());
        mulpayBillResponse.setTranDate(mulPayBillEntity.getTranDate());
        mulpayBillResponse.setConvenience(mulPayBillEntity.getConvenience());
        mulpayBillResponse.setConfNo(mulPayBillEntity.getConfNo());
        mulpayBillResponse.setReceiptNo(mulPayBillEntity.getReceiptNo());
        mulpayBillResponse.setPaymentTerm(mulPayBillEntity.getPaymentTerm());
        mulpayBillResponse.setCustId(mulPayBillEntity.getCustId());
        mulpayBillResponse.setBkCode(mulPayBillEntity.getBkCode());
        mulpayBillResponse.setEncryptReceiptNo(mulPayBillEntity.getEncryptReceiptNo());
        mulpayBillResponse.setErrInfo(mulPayBillEntity.getErrInfo());
        mulpayBillResponse.setPaymentURL(mulPayBillEntity.getPaymentURL());
        mulpayBillResponse.setExprireDate(mulPayBillEntity.getExpireDate());
        mulpayBillResponse.setTradeReason(mulPayBillEntity.getTradeReason());
        mulpayBillResponse.setTradeClientName(mulPayBillEntity.getTradeClientName());
        mulpayBillResponse.setTradeClientMailAddress(mulPayBillEntity.getTradeClientMailAddress());
        mulpayBillResponse.setBankCode(mulPayBillEntity.getBankCode());
        mulpayBillResponse.setBankName(mulPayBillEntity.getBankName());
        mulpayBillResponse.setAccountType(mulPayBillEntity.getAccountType());
        mulpayBillResponse.setAccountNumber(mulPayBillEntity.getAccountNumber());
        mulpayBillResponse.setBranchName(mulPayBillEntity.getBranchName());
        mulpayBillResponse.setBranchCode(mulPayBillEntity.getBranchCode());
        mulpayBillResponse.setRegistTime(mulPayBillEntity.getRegistTime());
        mulpayBillResponse.setUpdateTime(mulPayBillEntity.getUpdateTime());

        return mulpayBillResponse;
    }

    /**
     * マルチペイメント決済結果レスポンスに変換
     *
     * @param mulPayResultEntity ﾏﾙﾁﾍﾟｲﾒﾝﾄ決済結果
     * @return マルチペイメント決済結果レスポンス
     */
    public MulPayResultConfirmDepositedResponse toMulPayResultConfirmDepositedResponse(MulPayResultEntity mulPayResultEntity) {

        if (ObjectUtils.isEmpty(mulPayResultEntity)) {
            return new MulPayResultConfirmDepositedResponse();
        } else {

            MulPayResultConfirmDepositedResponse mulPayResultConfirmDepositedResponse =
                            new MulPayResultConfirmDepositedResponse();

            mulPayResultConfirmDepositedResponse.setMulPayResultSeq(mulPayResultEntity.getMulPayResultSeq());
            if (HTypeProcessedFlag.PROCESSING_REQUIRED.equals(mulPayResultEntity.getProcessedFlag())) {
                mulPayResultConfirmDepositedResponse.setRequiredReflectionProcessingFlag(true);
            } else {
                mulPayResultConfirmDepositedResponse.setRequiredReflectionProcessingFlag(false);
            }

            return mulPayResultConfirmDepositedResponse;
        }
    }

}