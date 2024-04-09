/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.mulpay.entity.proxy;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeProcessedFlag;
import jp.co.itechh.quad.core.dao.multipayment.MulPayBillDao;
import jp.co.itechh.quad.core.dao.multipayment.MulPayResultDao;
import jp.co.itechh.quad.core.dao.multipayment.MulPaySiteDao;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayResultEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPaySiteEntity;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.usecase.card.consumer.CreditLineReleaseTargetDto;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultConfirmRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayResultRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * マルチペイメントプロキシサービス<br/>
 * ※フェーズ1のマルチペイメント用のプロキシサービスクラス。Daoは項目定義以外はフェーズ1をそのまま流用<br/>
 * ※GMOと通信するロジックは呼び出さない
 */
@Service
public class MulPayProxyService {

    /** マルチペイメント用サイト設定Dao */
    private final MulPaySiteDao mulPaySiteDao;

    /** マルチペイメント請求Dao */
    private final MulPayBillDao mulPayBillDao;

    /** マルチペイメント決済結果Dao */
    private final MulPayResultDao mulPayResultDao;

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /** 変換Helper取得 */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public MulPayProxyService(MulPaySiteDao mulPaySiteDao,
                              MulPayBillDao mulPayBillDao,
                              MulPayResultDao mulPayResultDao,
                              DateUtility dateUtility,
                              ConversionUtility conversionUtility) {
        this.mulPaySiteDao = mulPaySiteDao;
        this.mulPayBillDao = mulPayBillDao;
        this.mulPayResultDao = mulPayResultDao;
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * マルチペイメント用サイト設定情報取得<br/>
     *
     * @return マルチペイメント用サイト設定
     */
    public MulPaySiteEntity getMulPaySiteEntity() {

        return this.mulPaySiteDao.getEntity();
    }

    /**
     * マルチペイメント請求テーブルから、オーダーIDに紐づく最新レコード情報を取得
     *
     * @param orderId 受注Id ※受注番号(orderCode)と同値
     * @return マルチペイメント請求
     */
    public MulPayBillEntity getMulPayBillByOrderId(String orderId) {

        ArgumentCheckUtil.assertNotNull("orderId", orderId);

        // マルチペイメント請求情報の検索
        return mulPayBillDao.getLatestEntityByOrderId(orderId);
    }

    /**
     * マルチペイメント請求テーブルから、オーダーIDに紐づくエラー情報がない最新レコード情報を取得
     *
     * @param orderId オーダーID ※受注番号(orderCode)と同値
     * @return mulPayBillEntity　マルチペイメント請求テーブル
     */
    public MulPayBillEntity getLatestNoErrorEntityByOrderId(String orderId) {

        ArgumentCheckUtil.assertNotNull("orderId", orderId);

        // マルチペイメント請求テーブルからレコードを取得する
        return mulPayBillDao.getLatestNoErrorEntityByOrderId(orderId);
    }

    /**
     * マルチペイメント請求テーブルから、注文決済IDに紐づくレコード情報を取得<br/>
     * フェーズ１のmulPayBillDao.getLatestEntityが該当
     *
     * @param orderPaymentId 注文決済ID（OrderSeq）
     * @return mulPayBillEntity　マルチペイメント請求テーブル
     */
    public MulPayBillEntity getLatestEntityByOrderPaymentId(String orderPaymentId) {

        AssertChecker.assertNotEmpty("orderPaymentId is empty", orderPaymentId);

        // マルチペイメント請求テーブルからレコードを取得する
        return mulPayBillDao.getLatestEntity(orderPaymentId);
    }

    /**
     * 与信枠確保未解放受注リスト取得<br/>
     * フェーズ１のOrderCreditLineReportBatchで直接呼び出している、mulPayBillDao.getReserveCreditLineMulPayBillListが該当
     * フェーズ２用にSQLを修正
     *
     * @param thresholdTime 現在日時の時間-指定時間
     * @param specifiedDay  現在日-指定日数
     * @return 与信枠確保未解放受注リスト
     */
    public List<CreditLineReleaseTargetDto> getCreditLineReleaseTargetList(Timestamp thresholdTime,
                                                                           Timestamp specifiedDay) {

        AssertChecker.assertNotNull("thresholdTime is null", thresholdTime);
        AssertChecker.assertNotNull("specifiedDay is null", specifiedDay);

        // 与信枠解放対象の 受注請求リストを取得する
        return mulPayBillDao.getReserveCreditLineMulPayBillList(thresholdTime, specifiedDay);
    }

    /**
     * ﾏﾙﾁﾍﾟｲﾒﾝﾄ決済結果取得
     *
     * @param mulPayResultSeq マルチペイメント決済結果SEQ
     * @return ﾏﾙﾁﾍﾟｲﾒﾝﾄ決済結果
     */
    public MulPayResultEntity getEntityByMulPayResultSeq(Integer mulPayResultSeq) {

        ArgumentCheckUtil.assertNotNull("mulPayResultSeq", mulPayResultSeq);
        return mulPayResultDao.getEntity(mulPayResultSeq);
    }

    /**
     * 入金登録後のマルチペイメント決済結果を更新する
     *
     * @param mulPayResultDbEntity ﾏﾙﾁﾍﾟｲﾒﾝﾄ決済結果
     * @return count　レコード更新件数
     */
    public int updateMulPayResultAfterDeposit(MulPayResultEntity mulPayResultDbEntity) {

        // マルチペイメント決済結果の入金登録済みフラグを更新して、レコードを更新する
        mulPayResultDbEntity.setProcessedFlag(HTypeProcessedFlag.PROCESSED);
        mulPayResultDbEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        int count = mulPayResultDao.update(mulPayResultDbEntity);

        return count;
    }

    /**
     * TODO v0.3.0のマルペイ請求テーブルで実装のため、修正対象
     * マルチペイメント決済結果リストを取得する<br/>
     *
     * @return マルチペイメント決済結果リスト
     */
    public List<MulPayResultEntity> getUnprocessedPaySuccessEntityList() {

        int shopSeq = 1001;
        return mulPayResultDao.getUnprocessedPaySuccessEntityList(shopSeq);
    }

    /**
     * TODO v0.3.0のマルペイ請求テーブルで実装のため、修正対象
     * マルチペイメント決済結果テーブルに、レコードを登録<br/>
     * フェーズ１のMultipaymentConfirmationBatchで直接呼び出している、createMulPayResultEntity、mulPayResultDao.insertが該当
     *
     * @param mulPayResultRequest マルチペイメント決済結果通知受付リクエスト
     * @return 登録を行ったマルチペイメント決済結果(Seqなし)
     */
    public MulPayResultEntity insertMulPayResult(MulPayResultRequest mulPayResultRequest,
                                                 String orderPaymentId,
                                                 String receiveMode) {

        // マルチペイメント決済結果テーブルにレコードを登録する
        MulPayResultEntity mulPayResultEntity =
                        createMulPayResultEntity(mulPayResultRequest, orderPaymentId, receiveMode);

        mulPayResultDao.insert(mulPayResultEntity);

        return mulPayResultEntity;
    }

    /**
     * マルチペイメント請求テーブルから、取引IDに紐づく最新レコード情報を取得
     *
     * @param accessId 決済代行の取引ID
     * @return マルチペイメント請求
     */
    public MulPayBillEntity getMulPayBillbyAccessId(String accessId) {
        ArgumentCheckUtil.assertNotNull("accessId", accessId);

        // マルチペイメント請求情報の検索
        return mulPayBillDao.getLatestEntityByAccessId(accessId);
    }

    /**
     * TODO v0.3.0のマルペイ請求テーブルで実装のため、修正対象
     * 登録用のマルチペイメント決済結果エンティティを生成
     *
     * @param mulPayResultRequest マルチペイメント決済結果通知受付リクエスト
     * @return entity マルチペイメント決済結果エンティティ
     */
    private MulPayResultEntity createMulPayResultEntity(MulPayResultRequest mulPayResultRequest,
                                                        String orderPaymentId,
                                                        String receiveMode) {

        MulPayResultEntity entity = ApplicationContextUtility.getBean(MulPayResultEntity.class);

        entity.setReceiveMode(receiveMode);
        if ("PAYSUCCESS".equals(mulPayResultRequest.getStatus()) || "TRANSFERRED".equals(
                        mulPayResultRequest.getStatus()) || "CAPTURE".equals(mulPayResultRequest.getStatus())) {
            if ("36".equals(mulPayResultRequest.getPayType())) {
                // バーチャル口座あおぞらは入金時に"TRADING"で結果通知が来るが、全額分の入金が完了すると別途"PAYSUCCESS"の結果通知が来る。
                // HM上の入金処理は"TRADING"時に実施済なので、"PAYSUCCESS"は処理しない。
                entity.setProcessedFlag(HTypeProcessedFlag.PROCESSED);
            } else {
                entity.setProcessedFlag(HTypeProcessedFlag.PROCESSING_REQUIRED);
            }
        } else {
            // バーチャル口座あおぞらで入金不足
            if ("TRADING".equals(mulPayResultRequest.getStatus())
                && mulPayResultRequest.getGanbTotalTransferAmount() != null
                && mulPayResultRequest.getGanbTotalTransferAmount().compareTo(BigDecimal.ZERO) > 0) {
                entity.setProcessedFlag(HTypeProcessedFlag.PROCESSING_REQUIRED);
            } else {
                entity.setProcessedFlag(HTypeProcessedFlag.PROCESSED);
            }
        }
        entity.setShopSeq(1001);
        entity.setOrderPaymentId(orderPaymentId);
        entity.setOrderId(mulPayResultRequest.getOrderId());
        entity.setStatus(mulPayResultRequest.getStatus());
        entity.setJobCd(mulPayResultRequest.getJobCd());
        entity.setProcessDate(dateUtility.getCurrentYmdHms());
        entity.setItemCode(mulPayResultRequest.getItemCode());
        entity.setAmount(conversionUtility.toBigDecimal(mulPayResultRequest.getAmount()));
        entity.setTax(conversionUtility.toBigDecimal(mulPayResultRequest.getTax()));
        entity.setSiteId(mulPayResultRequest.getSiteId());
        entity.setMemberId(mulPayResultRequest.getMemberId());
        entity.setCardNo(mulPayResultRequest.getCardNumber());
        entity.setExpire(mulPayResultRequest.getExpire());
        entity.setCurrency(mulPayResultRequest.getCurrency());
        entity.setForward(mulPayResultRequest.getForward());
        entity.setMethod(mulPayResultRequest.getMethod());
        entity.setPayTimes(conversionUtility.toInteger(mulPayResultRequest.getPayTimes()));
        entity.setTranId(mulPayResultRequest.getTranId());
        entity.setApprove(mulPayResultRequest.getApprove());
        entity.setTranDate(mulPayResultRequest.getTranDate());
        entity.setErrCode(mulPayResultRequest.getErrCode());
        entity.setErrInfo(mulPayResultRequest.getErrInfo());
        entity.setClientField1(mulPayResultRequest.getClientField1());
        entity.setClientField2(mulPayResultRequest.getClientField2());
        entity.setClientField3(mulPayResultRequest.getClientField3());
        entity.setPayType(mulPayResultRequest.getPayType());
        entity.setPayMethod(mulPayResultRequest.getPaymethod());
        entity.setCvsCode(mulPayResultRequest.getCvsCode());
        entity.setCvsConfNo(mulPayResultRequest.getCvsConfNo());
        entity.setCvsReceiptNo(mulPayResultRequest.getCvsReceiptNo());
        entity.setCustId(mulPayResultRequest.getCustId());
        entity.setBkCode(mulPayResultRequest.getBkCode());
        entity.setConfNo(mulPayResultRequest.getConfNo());
        entity.setPaymentTerm(mulPayResultRequest.getPaymentTerm());
        entity.setEncryptReceiptNo(mulPayResultRequest.getEncryptReceiptNo());
        entity.setFinishDate(mulPayResultRequest.getFinishDate());
        entity.setReceiptDate(mulPayResultRequest.getReceiptDate());
        entity.setRequestAmount(conversionUtility.toBigDecimal(mulPayResultRequest.getRequestAmount()));
        entity.setExpireDate(mulPayResultRequest.getExpireDate());
        entity.setTradeReason(mulPayResultRequest.getTradeReason());
        entity.setClientName(mulPayResultRequest.getTradeClientName());
        entity.setClientMailAddress(mulPayResultRequest.getTradeClientMailAddress());
        entity.setBankCode(mulPayResultRequest.getBankCode());
        entity.setBankName(mulPayResultRequest.getBankName());
        entity.setBranchCode(mulPayResultRequest.getBranchCode());
        entity.setBranchName(mulPayResultRequest.getBranchName());
        entity.setAccountType(mulPayResultRequest.getAccountType());
        entity.setAccountNumber(mulPayResultRequest.getAccountNumber());
        entity.setInSettlementDate(mulPayResultRequest.getInSettlementDate());
        entity.setInAmount(conversionUtility.toBigDecimal(mulPayResultRequest.getInAmount()));
        entity.setInClientName(mulPayResultRequest.getInClientName());
        entity.setGanbProcessType(mulPayResultRequest.getGanbProcessType());
        entity.setGanbAccountHolderName(mulPayResultRequest.getGanbAccountHolderName());
        entity.setGanbInRemittingBankName(mulPayResultRequest.getGanbInRemittingBankName());
        entity.setGanbInRemittingBranchName(mulPayResultRequest.getGanbInRemittingBranchName());
        entity.setGanbTotalTransferAmount(
                        conversionUtility.toBigDecimal(mulPayResultRequest.getGanbTotalTransferAmount()));
        entity.setGanbTotalTransferCount(mulPayResultRequest.getGanbTotalTransferCount());
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
    private String convBlankToNull(String value) {
        return StringUtils.defaultIfEmpty(value, null);
    }

    /**
     * マルチペイメント決済結果登録
     *
     * @param mulPayResultRequest マルチペイメント決済結果通知受付リクエスト
     * @return 要処理フラグ
     */
    public boolean registMulpayResult(MulPayResultRequest mulPayResultRequest, String receiveMode) {

        if (StringUtils.isBlank(receiveMode)) {
            receiveMode = "ACTIVE";
        }

        //マルチペイメント請求テーブルよりデータを取得する
        MulPayBillEntity mulPayBillEntity = getMulPayBillByOrderId(mulPayResultRequest.getOrderId());
        if (mulPayBillEntity == null) {
            return false;
        }

        // 取得データがマルチペイメント決済結果テーブルに登録済みであるかをチェックする
        String processedFlag = mulPayResultDao.checkSameNotificationRecord(mulPayResultRequest.getOrderId(),
                                                                           mulPayResultRequest.getStatus(),
                                                                           conversionUtility.toInteger(
                                                                                           mulPayResultRequest.getGanbTotalTransferAmount()),
                                                                           mulPayResultRequest.getErrCode(),
                                                                           mulPayResultRequest.getErrInfo()
                                                                          );
        if (!StringUtils.isBlank(processedFlag)) {
            if ("0".equals(processedFlag)) {
                return true;
            } else {
                return false;
            }
        }

        // 決済結果テーブルにデータを登録する
        MulPayResultEntity mulPayResultEntity =
                        insertMulPayResult(mulPayResultRequest, mulPayBillEntity.getOrderPaymentId(), receiveMode);

        return mulPayResultEntity.getProcessedFlag() == HTypeProcessedFlag.PROCESSING_REQUIRED;
    }

    /**
     * 要処理のマルチペイメント決済結果取得
     *
     * @param mulpayResultConfirmRequest マルチペイメント決済結果確認リクエスト
     */
    public MulPayResultEntity getRequiredReflectionProcessing(MulPayResultConfirmRequest mulpayResultConfirmRequest) {

        MulPayResultEntity mulPayResultEntity =
                        mulPayResultDao.confirmDeposited(mulpayResultConfirmRequest.getOrderCode());

        return mulPayResultEntity;
    }

    /**
     * 与信枠確保未解放受注取得
     *
     * @param orderId 受注Id ※受注番号(orderCode)と同値
     * @return 与信枠確保未解放受注リスト
     */
    public CreditLineReleaseTargetDto getCreditLineReleaseTarget(String orderId) {

        AssertChecker.assertNotNull("orderId is null", orderId);

        // 与信枠解放対象の 受注請求リストを取得する
        return mulPayBillDao.getReserveCreditLineMulPayBillByOrderId(orderId);
    }

}