/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */


package jp.co.itechh.quad.ddd.usecase.mulpay;

import com.gmo_pg.g_pay.client.input.SearchTradeMultiInput;
import com.gmo_pg.g_pay.client.output.SearchTradeMultiOutput;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeProcessedFlag;
import jp.co.itechh.quad.core.dao.multipayment.MulPayResultDao;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayResultEntity;
import jp.co.itechh.quad.core.utility.CommunicateUtility;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.IMultiTradeSearchAdapter;
import jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy.MulPayProxyService;
import jp.co.itechh.quad.ddd.infrastructure.billing.dao.BillingSlipDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 決済代行へ入金を確認して結果を登録する ユースケース
 */
@Service
public class PaymentAgencyMulpayConfirmUseCase {

    /** ショップSEQ */
    public static final int SHOP_SEQ = 1001;

    /** GMOマルチ決済取引検索アダプター */
    private final IMultiTradeSearchAdapter multiTradeSearchAdapter;

    /** マルチペイメントプロキシサービス */
    private final MulPayProxyService mulpayProxyService;

    /** マルチペイメント決済結果Dao */
    private final MulPayResultDao mulPayResultDao;

    /** 請求伝票Dao */
    private final BillingSlipDao billingSlipDao;

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /** 通信ヘルパークラス */
    private final CommunicateUtility communicateUtility;

    /** 変換Helper取得 */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public PaymentAgencyMulpayConfirmUseCase(IMultiTradeSearchAdapter multiTradeSearchAdapter,
                                             MulPayProxyService mulpayProxyService,
                                             MulPayResultDao mulPayResultDao,
                                             BillingSlipDao billingSlipDao,
                                             CommunicateUtility communicateUtility,
                                             ConversionUtility conversionUtility,
                                             DateUtility dateUtility) {
        this.multiTradeSearchAdapter = multiTradeSearchAdapter;
        this.mulpayProxyService = mulpayProxyService;
        this.mulPayResultDao = mulPayResultDao;
        this.billingSlipDao = billingSlipDao;
        this.communicateUtility = communicateUtility;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * 決済代行へ入金を確認して結果を登録する
     *
     * @param orderCode 受注番号
     * @return 要入金反映処理フラグ
     */
    public Boolean confirmPaymentAgencyResultAndRegist(String orderCode) {

        // マルチペイメント請求テーブルよりデータを取得する
        MulPayBillEntity mulPayBillEntity = mulpayProxyService.getMulPayBillByOrderId(orderCode);
        if (mulPayBillEntity == null) {
            return false;
        }

        // GMOマルチペイメントより決済状況を取得する
        SearchTradeMultiInput multiInput = new SearchTradeMultiInput();
        multiInput.setOrderId(orderCode);
        multiInput.setPayType(mulPayBillEntity.getPayType());
        SearchTradeMultiOutput searchTradeMultiOutput = multiTradeSearchAdapter.doSearchTradeMulti(multiInput);

        // GMOマルチペイメントより決済状況データを取得できませんでした。
        if (StringUtils.isEmpty(searchTradeMultiOutput.getStatus())) {
            return false;
        }

        // 取得したエラーコードをパイプで文字列結合する
        String[] error = communicateUtility.getError(searchTradeMultiOutput);
        String errCodes = error[0];
        String errInfos = error[1];

        // 取得データがマルチペイメント決済結果テーブルに登録済みであるかをチェックする
        String processedFlag =
                        mulPayResultDao.checkSameNotificationRecord(orderCode, searchTradeMultiOutput.getStatus(),
                                                                    conversionUtility.toInteger(
                                                                                    searchTradeMultiOutput.getGanbTotalTransferAmount()),
                                                                    errCodes, errInfos
                                                                   );
        // 既にマルチペイメント決済結果データが登録済みの場合
        if (!StringUtils.isBlank(processedFlag)) {
            if (processedFlag.equals("0")) {
                return true;
            } else {
                return false;
            }
        }

        // 決済結果テーブルにデータを登録する
        MulPayResultEntity mulPayResultEntity = createMulPayResultEntity(multiInput, searchTradeMultiOutput,
                                                                         mulPayBillEntity.getOrderPaymentId(), SHOP_SEQ,
                                                                         errCodes, errInfos
                                                                        );
        mulPayResultDao.insert(mulPayResultEntity);

        return mulPayResultEntity.getProcessedFlag() == HTypeProcessedFlag.PROCESSING_REQUIRED;
    }

    /**
     * 登録用のマルチペイメント決済結果エンティティを生成します。
     *
     * @param multiInput     マルチペイメント通信リクエストオブジェクト OrderId を取得するためのみに使用する
     * @param multiOutput    マルチペイメント通信結果オブジェクト
     * @param orderPaymentId 注文決済ID
     * @param shopSeq        ショップSEQ
     * @param errCodes       エラーコード（パイプ文字結合）
     * @param errInfos       エラー詳細コード（パイプ文字結合）
     * @return MulPayResult マルチペイメント決済結果エンティティ
     */
    protected MulPayResultEntity createMulPayResultEntity(SearchTradeMultiInput multiInput,
                                                          SearchTradeMultiOutput multiOutput,
                                                          String orderPaymentId,
                                                          Integer shopSeq,
                                                          String errCodes,
                                                          String errInfos) {

        MulPayResultEntity entity = ApplicationContextUtility.getBean(MulPayResultEntity.class);

        entity.setReceiveMode("ACTIVE");
        // 予備バッチは、即時払いの入金も判定する
        if ("PAYSUCCESS".equals(multiOutput.getStatus()) || "CAPTURE".equals(multiOutput.getStatus()) || "SALES".equals(
                        multiOutput.getStatus())) {
            entity.setProcessedFlag(HTypeProcessedFlag.PROCESSING_REQUIRED);
        } else {
            // バーチャル口座あおぞらで入金不足
            if ("TRADING".equals(multiOutput.getStatus()) && !StringUtils.isBlank(
                            multiOutput.getGanbTotalTransferAmount())
                && Integer.valueOf(multiOutput.getGanbTotalTransferAmount()) > 0) {
                entity.setProcessedFlag(HTypeProcessedFlag.PROCESSING_REQUIRED);
            } else {
                entity.setProcessedFlag(HTypeProcessedFlag.PROCESSED);
            }
        }
        entity.setShopSeq(shopSeq);
        entity.setOrderPaymentId(orderPaymentId);
        entity.setOrderId(multiInput.getOrderId());
        entity.setStatus(convBlankToNull(multiOutput.getStatus()));
        entity.setJobCd(convBlankToNull(multiOutput.getJobCd()));
        entity.setProcessDate(convBlankToNull(multiOutput.getProcessDate()));
        entity.setItemCode(convBlankToNull(multiOutput.getItemCode()));
        entity.setAmount(conversionUtility.toBigDecimal(multiOutput.getAmount()));
        entity.setTax(conversionUtility.toBigDecimal(multiOutput.getTax()));
        entity.setSiteId(convBlankToNull(multiOutput.getSiteId()));
        entity.setMemberId(convBlankToNull(multiOutput.getMemberId()));
        entity.setCardNo(convBlankToNull(multiOutput.getCardNo()));
        entity.setExpire(convBlankToNull(multiOutput.getExpire()));
        entity.setForward(convBlankToNull(multiOutput.getForward()));
        entity.setMethod(convBlankToNull(multiOutput.getMethod()));
        entity.setPayTimes(multiOutput.getPayTimes());
        entity.setTranId(convBlankToNull(multiOutput.getTranId()));
        entity.setApprove(convBlankToNull(multiOutput.getApprove()));
        entity.setErrCode(errCodes);
        entity.setErrInfo(errInfos);
        entity.setClientField1(convBlankToNull(multiOutput.getClientField1()));
        entity.setClientField2(convBlankToNull(multiOutput.getClientField2()));
        entity.setClientField3(convBlankToNull(multiOutput.getClientField3()));
        entity.setPayType(convBlankToNull(multiOutput.getPayType()));
        entity.setCvsCode(convBlankToNull(multiOutput.getCvsCode()));
        entity.setCvsConfNo(convBlankToNull(multiOutput.getCvsConfNo()));
        entity.setCvsReceiptNo(convBlankToNull(multiOutput.getCvsReceiptNo()));
        entity.setFinishDate(convBlankToNull(multiOutput.getFinishDate()));
        entity.setCustId(convBlankToNull(multiOutput.getCustId()));
        entity.setBkCode(convBlankToNull(multiOutput.getBkCode()));
        entity.setConfNo(convBlankToNull(multiOutput.getConfNo()));
        entity.setPaymentTerm(convBlankToNull(multiOutput.getPaymentTerm()));
        entity.setEncryptReceiptNo(convBlankToNull(multiOutput.getEncryptReceiptNo()));

        if (entity.getPayType().equals("36")) {
            // 銀行振込(バーチャル口座 あおぞら)
            entity.setExpireDate(convBlankToNull(multiOutput.getGanbExpireDate()));
            entity.setTradeReason(convBlankToNull(multiOutput.getGanbTradeReason()));
            entity.setClientName(convBlankToNull(multiOutput.getGanbTradeClientName()));
            entity.setBankCode(convBlankToNull(multiOutput.getGanbBankCode()));
            entity.setBankName(convBlankToNull(multiOutput.getGanbBankName()));
            entity.setBranchCode(convBlankToNull(multiOutput.getGanbBranchCode()));
            entity.setBranchName(convBlankToNull(multiOutput.getGanbBranchName()));
            entity.setAccountType(convBlankToNull(multiOutput.getGanbAccountType()));
            entity.setAccountNumber(convBlankToNull(multiOutput.getGanbAccountNumber()));
            entity.setGanbAccountHolderName(convBlankToNull(multiOutput.getGanbAccountHolderName()));
            entity.setGanbTotalTransferAmount(
                            conversionUtility.toBigDecimal(convBlankToNull(multiOutput.getGanbTotalTransferAmount())));
            entity.setGanbTotalTransferCount(
                            conversionUtility.toInteger(convBlankToNull(multiOutput.getGanbTotalTransferCount())));
        }

        entity.setRegistTime(dateUtility.getCurrentTime());
        entity.setUpdateTime(dateUtility.getCurrentTime());

        return entity;
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