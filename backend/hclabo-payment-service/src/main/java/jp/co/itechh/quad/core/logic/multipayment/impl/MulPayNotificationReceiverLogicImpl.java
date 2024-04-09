/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.multipayment.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeProcessedFlag;
import jp.co.itechh.quad.core.dao.multipayment.MulPayBillDao;
import jp.co.itechh.quad.core.dao.multipayment.MulPayResultDao;
import jp.co.itechh.quad.core.dao.multipayment.MulPayShopDao;
import jp.co.itechh.quad.core.dto.multipayment.MulPayNotificationReceiverDto;
import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayResultEntity;
import jp.co.itechh.quad.core.entity.multipayment.MulPayShopEntity;
import jp.co.itechh.quad.core.logic.multipayment.MulPayNotificationReceiverLogic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * マルチペイメント決済結果通知受付サーブレットロジック<br/>
 *
 * @author na65101 STS Nakamura 2020/03/11
 */
@Component
public class MulPayNotificationReceiverLogicImpl implements MulPayNotificationReceiverLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MulPayNotificationReceiverLogicImpl.class);

    /** 受信方法登録値 */
    protected static final String INS_RECEIVE_MODE = "PASSIVE";

    /** 入金通知 */
    protected static final String PAYSUCCESS = "PAYSUCCESS";

    /** ConversionUtility */
    private final ConversionUtility conversionUtility;

    /** DateUtility */
    private final DateUtility dateUtility;

    /** MulPayShopDao */
    private final MulPayShopDao mulPayShopDao;

    /** MulPayBillDao */
    private final MulPayBillDao mulPayBillDao;

    /** MulPayResultDao */
    private final MulPayResultDao mulPayResultDao;

    /** コンストラクタ */
    @Autowired
    public MulPayNotificationReceiverLogicImpl(ConversionUtility conversionUtility,
                                               DateUtility dateUtility,
                                               MulPayShopDao mulPayShopDao,
                                               MulPayBillDao mulPayBillDao,
                                               MulPayResultDao mulPayResultDao) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.mulPayShopDao = mulPayShopDao;
        this.mulPayBillDao = mulPayBillDao;
        this.mulPayResultDao = mulPayResultDao;
    }

    /**
     * 実行処理<br/>
     * <p>
     * 1.受信情報の妥当性チェック<br/>
     * 2.マルチペイメント請求データ取得<br/>
     * 3.受信データをマルチペイメント決済結果テーブルへの登録処理
     * </p>
     *
     * @param dto マルチペイメント決済結果通知受付サーブレットDto
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void execute(MulPayNotificationReceiverDto dto) {

        // 1.受信情報の妥当性チェック
        if (!checkRequiredParameter(dto)) {
            return;
        }

        // 1-2.DB（マルチペイメント用ショップ設定テーブル）存在チェック
        MulPayShopEntity mulPayShopEntity = getMulPayShopEntity(dto.getShopId());
        if (mulPayShopEntity == null) {
            return;
        }

        // 2.マルチペイメント請求データ取得
        MulPayBillEntity mulPayBillEntity = getMulPayBillEntity(dto.getOrderId(), dto.getAccessId());
        if (mulPayBillEntity == null) {
            return;
        }

        // 3.受信データをマルチペイメント決済結果テーブルへの登録処理
        insertMulPayResult(dto, mulPayShopEntity.getShopSeq(), mulPayBillEntity);
    }

    /**
     * 必須パラメータチェック
     * <pre>
     * 各決済方法別の必須項目が設定されていることを確認する。
     * </pre>
     *
     * @param dto マルチペイメント決済結果通知受付サーブレットDto
     * @return true or false
     */
    protected boolean checkRequiredParameter(MulPayNotificationReceiverDto dto) {

        if (!checkParameter(dto)) {
            LOGGER.error("受信情報妥当性エラー　必須項目がありません");
            return false;
        }
        return true;
    }

    /**
     * 汎用パラメータチェック
     * <pre>
     * 必須項目のパラメータが設定されていことを確認する。
     * いずれか一つのパラメータが存在しなければ falseを返す。
     * </pre>
     *
     * @param dto マルチペイメント決済結果通知受付サーブレットDto
     * @return true or false
     */
    protected boolean checkParameter(MulPayNotificationReceiverDto dto) {

        if (StringUtils.isBlank(dto.getShopId())) {
            return false;
        }
        if (StringUtils.isBlank(dto.getAccessId())) {
            return false;
        }
        if (StringUtils.isBlank(dto.getOrderId())) {
            return false;
        }
        if (StringUtils.isBlank(dto.getStatus())) {
            return false;
        }
        if (dto.getAmount() == null) {
            return false;
        }
        if (dto.getTax() == null) {
            return false;
        }
        if (StringUtils.isBlank(dto.getTranDate())) {
            return false;
        }
        if (StringUtils.isBlank(dto.getErrCode())) {
            return false;
        }
        if (StringUtils.isBlank(dto.getErrInfo())) {
            return false;
        }
        if (StringUtils.isBlank(dto.getPayType())) {
            return false;
        }
        // AmazonPay以外で利用する項目
        if (StringUtils.isBlank(dto.getCurrency())) {
            return false;
        }

        return true;
    }

    /**
     * 最新マルチペイメント請求エンティティ取得処理
     *
     * @param orderId  オーダーID
     * @param accessId 取引ID
     * @return マルチペイメント請求エンティティ
     */
    protected MulPayBillEntity getMulPayBillEntity(String orderId, String accessId) {

        try {
            // マルチペイメント請求データ取得処理
            MulPayBillEntity mulPayBillEntity = mulPayBillDao.getLatestEntityByOrderIdAndAccessId(orderId, accessId);
            if (mulPayBillEntity == null) {
                LOGGER.error("マルチペイメント請求データが取得できませんでした" + "　【オーダーID】:" + orderId + "　【取引ID】:" + accessId);
            }
            return mulPayBillEntity;
        } catch (Exception e) {
            LOGGER.error("マルチペイメント請求テーブル参照中にエラーが発生しました" + "　【オーダーID】:" + orderId + "　【取引ID】:" + accessId, e);
            throw e;
        }
    }

    /**
     * マルチペイメント用ショップ設定エンティティ取得処理
     *
     * @param shopId ショップID
     * @return マルチペイメント用ショップ設定エンティティ
     */
    protected MulPayShopEntity getMulPayShopEntity(String shopId) {

        try {
            // マルチペイメント用ショップ設定テーブル検索処理
            MulPayShopEntity mulPayShopEntity = mulPayShopDao.getEntityByShopId(shopId);
            if (mulPayShopEntity == null) {
                LOGGER.error("受信情報妥当性エラー　存在しないショップIDです" + "　【ショップID】:" + shopId);
                return null;
            }
            return mulPayShopEntity;
        } catch (Exception e) {
            LOGGER.error("マルチペイメント用ショップ設定テーブル参照中にエラーが発生しました" + "　【ショップID】:" + shopId, e);
            throw e;
        }
    }

    /**
     * マルチペイメント決済結果テーブルへのデータ登録処理
     *
     * @param dto              マルチペイメント決済結果通知受付サーブレットDto
     * @param shopSeq          ショップSEQ
     * @param mulPayBillEntity マルペイ請求エンティティ
     */
    protected void insertMulPayResult(MulPayNotificationReceiverDto dto,
                                      Integer shopSeq,
                                      MulPayBillEntity mulPayBillEntity) {

        String orderID = dto.getOrderId();
        String accessID = dto.getAccessId();

        MulPayResultEntity mulPayResultEntity = createMulPayResultEntity(dto, shopSeq, mulPayBillEntity);

        // 存在チェック処理
        try {
            String processedFlag = mulPayResultDao.checkSameNotificationRecord(mulPayResultEntity.getOrderId(),
                                                                               mulPayResultEntity.getStatus(),
                                                                               conversionUtility.toInteger(
                                                                                               mulPayResultEntity.getGanbTotalTransferAmount()),
                                                                               mulPayResultEntity.getErrCode(),
                                                                               mulPayResultEntity.getErrInfo()
                                                                              );
            if (!StringUtils.isBlank(processedFlag)) {
                LOGGER.warn("受信したマルチペイメント決済通知情報は既に登録済みです" + "　【オーダーID】:" + orderID + "　【取引ID】:" + accessID);
                return;
            }
        } catch (Exception e) {
            LOGGER.error("マルチペイメント決済結果テーブル参照中にエラーが発生しました" + "　【オーダーID】:" + orderID + "　【取引ID】:" + accessID, e);
            throw e;
        }

        // データ登録処理
        try {
            mulPayResultDao.insert(mulPayResultEntity);
        } catch (Exception e) {
            LOGGER.error("マルチペイメント決済通知情報の登録に失敗しました" + "　【オーダーID】:" + orderID + "　【取引ID】:" + accessID, e);
            throw e;
        }
    }

    /**
     * マルチペイメント決済結果エンティティ生成処理
     *
     * @param dto              マルチペイメント決済結果通知受付サーブレットDto
     * @param shopSeq          ショップSEQ
     * @param mulPayBillEntity マルペイ請求エンティティ
     * @return マルチペイメント決済結果エンティティ
     */
    protected MulPayResultEntity createMulPayResultEntity(MulPayNotificationReceiverDto dto,
                                                          Integer shopSeq,
                                                          MulPayBillEntity mulPayBillEntity) {

        MulPayResultEntity mulPayResultEntity = ApplicationContextUtility.getBean(MulPayResultEntity.class);

        mulPayResultEntity.setReceiveMode(INS_RECEIVE_MODE);

        if (PAYSUCCESS.equals(dto.getStatus())) {
            mulPayResultEntity.setProcessedFlag(HTypeProcessedFlag.PROCESSING_REQUIRED);
        } else {
            if ("TRADING".equals(dto.getStatus()) && dto.getGanbTotalTransferAmount() != null
                && dto.getGanbTotalTransferAmount() > 0) {
                // バーチャル口座あおぞらで入金不足
                mulPayResultEntity.setProcessedFlag(HTypeProcessedFlag.PROCESSING_REQUIRED);
            } else {
                mulPayResultEntity.setProcessedFlag(HTypeProcessedFlag.PROCESSED);
            }
        }

        mulPayResultEntity.setShopSeq(shopSeq);
        mulPayResultEntity.setOrderPaymentId(mulPayBillEntity.getOrderPaymentId());         // 保留のため注文決済ID未実装
        mulPayResultEntity.setOrderId(convBlankToNull(dto.getOrderId()));
        mulPayResultEntity.setStatus(convBlankToNull(dto.getStatus()));
        mulPayResultEntity.setJobCd(convBlankToNull(dto.getJobCd()));
        mulPayResultEntity.setAmount(dto.getAmount());
        mulPayResultEntity.setTax(dto.getTax());
        mulPayResultEntity.setCurrency(convBlankToNull(dto.getCurrency()));
        mulPayResultEntity.setForward(convBlankToNull(dto.getForward()));
        mulPayResultEntity.setMethod(convBlankToNull(dto.getMethod()));
        mulPayResultEntity.setPayTimes(dto.getPayTimes());
        mulPayResultEntity.setTranId(convBlankToNull(dto.getTranId()));
        mulPayResultEntity.setApprove(convBlankToNull(dto.getApprove()));
        mulPayResultEntity.setTranDate(convBlankToNull(dto.getTranDate()));
        mulPayResultEntity.setPayType(convBlankToNull(dto.getPayType()));
        mulPayResultEntity.setCvsCode(convBlankToNull(dto.getCvsCode()));
        mulPayResultEntity.setCvsConfNo(convBlankToNull(dto.getCvsConfNo()));
        mulPayResultEntity.setCvsReceiptNo(convBlankToNull(dto.getCvsReceiptNo()));
        mulPayResultEntity.setCustId(convBlankToNull(dto.getCustId()));
        mulPayResultEntity.setBkCode(convBlankToNull(dto.getBkCode()));
        mulPayResultEntity.setConfNo(convBlankToNull(dto.getConfNo()));
        mulPayResultEntity.setPaymentTerm(convBlankToNull(dto.getPaymentTerm()));
        mulPayResultEntity.setEncryptReceiptNo(convBlankToNull(dto.getEncryptReceiptNo()));
        mulPayResultEntity.setFinishDate(convBlankToNull(dto.getFinishDate()));
        mulPayResultEntity.setReceiptDate(convBlankToNull(dto.getReceiptDate()));
        mulPayResultEntity.setRegistTime(dateUtility.getCurrentTime());
        mulPayResultEntity.setUpdateTime(dateUtility.getCurrentTime());

        mulPayResultEntity.setErrCode(convBlankToNull(dto.getErrCode()));
        mulPayResultEntity.setErrInfo(convBlankToNull(dto.getErrInfo()));

        return mulPayResultEntity;
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