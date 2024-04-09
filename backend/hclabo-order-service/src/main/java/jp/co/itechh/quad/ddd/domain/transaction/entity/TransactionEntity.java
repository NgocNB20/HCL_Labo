/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.entity;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderReceivedId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.PaymentStatusDetail;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * 取引エンティティ
 */
@Getter
public class TransactionEntity {

    /** 取引ID */
    protected TransactionId transactionId;

    /** 取引ステータス */
    protected TransactionStatus transactionStatus;

    /** 入金済みフラグ */
    protected boolean paidFlag;

    /** 入金状態態詳細 */
    protected PaymentStatusDetail paymentStatusDetail;

    /** 出荷済みフラグ */
    protected boolean shippedFlag;

    /** 前請求フラグ */
    protected boolean preClaimFlag;

    /** 請求決済エラーフラグ */
    protected boolean billPaymentErrorFlag;

    /** 入金関連通知実施フラグ */
    protected boolean notificationFlag;

    /** 入金督促通知済みフラグ */
    protected boolean reminderSentFlag;

    /** 入金期限切れ通知済みフラグ */
    protected boolean expiredSentFlag;

    /** 管理メモ */
    protected String adminMemo;

    /** 顧客ID */
    protected String customerId;

    /** 受注ID */
    protected OrderReceivedId orderReceivedId;

    /** 登録日時 */
    protected Date registDate;

    /** 処理日時 */
    protected Date processTime;

    /** 処理種別 */
    protected HTypeProcessType processType;

    /** 処理担当者名 */
    protected String processPersonName;

    /** ノベルティプレゼント判定状態 */
    protected HTypeNoveltyPresentJudgmentStatus noveltyPresentJudgmentStatus;

    /**
     * コンストラクタ
     * 取引開始
     *
     * @param customerId      顧客ID
     * @param orderReceivedId 受注ID
     * @param registDate      登録日時
     */
    public TransactionEntity(String customerId, OrderReceivedId orderReceivedId, Date registDate) {

        // アサートチェック
        AssertChecker.assertNotEmpty("customerId is empty", customerId);
        AssertChecker.assertNotNull("orderReceivedId is null", orderReceivedId);
        AssertChecker.assertNotNull("registDate is null", registDate);

        // 設定
        this.transactionId = new TransactionId();
        // 取引ステータス下書き
        this.transactionStatus = TransactionStatus.DRAFT;
        this.customerId = customerId;
        this.orderReceivedId = orderReceivedId;
        this.registDate = registDate;
        // ノベルティプレゼント判定状態
        this.noveltyPresentJudgmentStatus = HTypeNoveltyPresentJudgmentStatus.UNJUDGMENT;
    }

    /**
     * コンストラクタ
     * 取引改訂(改訂用取引から取引生成)
     *
     * @param transactionForRevisionEntity
     * @param registDate
     */
    public TransactionEntity(TransactionForRevisionEntity transactionForRevisionEntity, Date registDate) {

        // アサートチェック
        AssertChecker.assertNotNull("transactionForRevisionEntity is null", transactionForRevisionEntity);
        AssertChecker.assertNotNull("registDate is null", registDate);

        /* 設定 */
        // 改訂取引をコピー
        copyProperties(transactionForRevisionEntity);
        // 改訂用取引IDを引き継いで、取引IDを設定
        this.transactionId = new TransactionId(transactionForRevisionEntity.getTransactionRevisionId().getValue());
        this.registDate = registDate;
    }

    /**
     * 取引確定
     */
    public void openTransaction() {

        // チェック
        // 下書き、終了 状態でないならエラー
        if (this.transactionStatus != TransactionStatus.DRAFT && this.transactionStatus != TransactionStatus.CLOSE) {
            throw new DomainException("ORDER-TRAN0001-E");
        }

        //設定
        // 取引ステータス確定
        this.transactionStatus = TransactionStatus.OPEN;
    }

    /**
     * 前請求フラグ設定
     *
     * @param preClaimFlag
     */
    public void settingPreClaim(boolean preClaimFlag) {
        this.preClaimFlag = preClaimFlag;
    }

    /**
     * @param noveltyPresentJudgmentStatus ノベルティプレゼント判定状態
     */
    public void settingNoveltyPresentJudgmentStatus(HTypeNoveltyPresentJudgmentStatus noveltyPresentJudgmentStatus) {
        this.noveltyPresentJudgmentStatus = noveltyPresentJudgmentStatus;
    }

    /**
     * 入金関連通知設定
     *
     * @param notificationFlag
     */
    public void settingNotification(boolean notificationFlag) {

        // チェック
        // 確定でないならエラー
        if (this.transactionStatus != TransactionStatus.OPEN) {
            throw new DomainException("ORDER-TRAN0003-E");
        }

        //設定
        this.notificationFlag = notificationFlag;
    }

    /**
     * 入金確定
     *
     * @param paymentStatusDetail
     */
    public void openPayment(PaymentStatusDetail paymentStatusDetail) {

        // チェック
        AssertChecker.assertNotNull("paymentStatusDetail is null", paymentStatusDetail);
        // 入金済みならエラー
        if (this.paidFlag) {
            throw new DomainException("ORDER-TRAN0004-E");
        }
        // 確定でないならエラー
        if (this.transactionStatus != TransactionStatus.OPEN) {
            throw new DomainException("ORDER-TRAN0005-E");
        }

        //設定
        this.paymentStatusDetail = paymentStatusDetail;
        if (!paymentStatusDetail.isInsufficientMoneyFlag()) {
            this.paidFlag = true;
        }

        // 入金過不足がある場合
        if (paymentStatusDetail.isInsufficientMoneyFlag() || paymentStatusDetail.isOverMoneyFlag()) {

            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
            String curDateTime = dateUtility.format(dateUtility.getCurrentTime(), DateUtility.YMD_SLASH_HMS);

            String message = "";
            // 入金不足管理メモへメッセージ
            if (paymentStatusDetail.isInsufficientMoneyFlag()) {
                message = AppLevelFacesMessageUtil.getAllMessage("ORDER-PAYMENT-001-W", new Object[] {curDateTime})
                                                  .getMessage();

            }

            // 入金超過管理メモへメッセージ
            if (paymentStatusDetail.isOverMoneyFlag()) {
                message = AppLevelFacesMessageUtil.getAllMessage("ORDER-PAYMENT-002-W", new Object[] {curDateTime})
                                                  .getMessage();
            }

            // 管理メモへ追加
            if (StringUtils.isBlank(this.adminMemo)) {
                this.adminMemo = message;
            } else {
                this.adminMemo = this.adminMemo + "\n" + message;
            }
        }
    }

    /**
     * 請求決済エラー設定
     */
    public void settingBillPaymentError() {

        // チェック
        // 確定状態でないならエラー
        if (this.transactionStatus != TransactionStatus.OPEN) {
            throw new DomainException("ORDER-TREV0010-E");
        }

        this.billPaymentErrorFlag = true;
    }

    /**
     * 取引終了
     */
    public void closeTransaction() {

        // チェック
        // 確定、取消状態でないならエラー
        if (this.transactionStatus != TransactionStatus.OPEN && this.transactionStatus != TransactionStatus.CANCEL) {
            throw new DomainException("ORDER-TRAN0006-E");
        }

        // 取引ステータス終了
        this.transactionStatus = TransactionStatus.CLOSE;
    }

    /**
     * 入金督促通知要否判定
     *
     * @return true ... 要通知
     */
    public boolean isNecessaryReminderSent() {

        // 入金関連通知実施フラグがtrueであり、入金督促済みフラグがfalseの場合、要通知
        return this.notificationFlag && !this.reminderSentFlag;
    }

    /**
     * 入金期限切れ通知要否判定
     *
     * @return true ... 要通知
     */
    public boolean isNecessaryExpiredSent() {

        // 入金関連通知実施フラグがtrueであり、入金期限切れ通知済みフラグがfalseの場合、要通知
        return this.notificationFlag && !this.expiredSentFlag;
    }

    /**
     * 処理履歴用情報設定
     *
     * @param processTime
     * @param processType
     * @param processPersonName
     */
    public void settingProcessHistoryInfo(Date processTime, HTypeProcessType processType, String processPersonName) {

        // アサートチェック
        AssertChecker.assertNotNull("processTime is null", processTime);
        AssertChecker.assertNotNull("processType is null", processType);

        this.processTime = processTime;
        this.processType = processType;
        this.processPersonName = processPersonName;
    }

    /**
     * 入金状態設定
     *
     * @param paidFlag
     * @param paymentStatusDetail
     */
    public void paymentStatusSetting(boolean paidFlag, PaymentStatusDetail paymentStatusDetail) {
        // 設定
        this.paidFlag = paidFlag;
        this.paymentStatusDetail = paymentStatusDetail;
    }

    /**
     * 入金期限切れ通知済み設定
     */
    public void settingExpiredSent() {

        // チェック
        // 入金関連通知実施フラグがtrueでないならエラー
        if (!this.notificationFlag) {
            throw new DomainException("ORDER-TREV0007-E");
        }

        //設定
        this.expiredSentFlag = true;
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public TransactionEntity(TransactionId transactionId,
                             TransactionStatus transactionStatus,
                             boolean paidFlag,
                             PaymentStatusDetail paymentStatusDetail,
                             boolean shippedFlag,
                             boolean billPaymentErrorFlag,
                             boolean notificationFlag,
                             boolean reminderSentFlag,
                             boolean expiredSentFlag,
                             String adminMemo,
                             String customerId,
                             OrderReceivedId orderReceivedId,
                             Date registDate,
                             Date processTime,
                             HTypeProcessType processType,
                             String processPersonName,
                             boolean preClaimFlag,
                             HTypeNoveltyPresentJudgmentStatus noveltyPresentJudgmentStatus) {
        this.transactionId = transactionId;
        this.transactionStatus = transactionStatus;
        this.paidFlag = paidFlag;
        this.paymentStatusDetail = paymentStatusDetail;
        this.shippedFlag = shippedFlag;
        this.billPaymentErrorFlag = billPaymentErrorFlag;
        this.notificationFlag = notificationFlag;
        this.reminderSentFlag = reminderSentFlag;
        this.expiredSentFlag = expiredSentFlag;
        this.adminMemo = adminMemo;
        this.customerId = customerId;
        this.orderReceivedId = orderReceivedId;
        this.registDate = registDate;
        this.processTime = processTime;
        this.processType = processType;
        this.processPersonName = processPersonName;
        this.preClaimFlag = preClaimFlag;
        this.noveltyPresentJudgmentStatus = noveltyPresentJudgmentStatus;
    }

    /**
     * コンストラクタ
     * ※改訂取引用空インスタンス生成
     */
    TransactionEntity() {
    }

    /**
     * プロパティコピー
     * ※改訂処理
     *
     * @param transactionEntity
     */
    protected void copyProperties(TransactionEntity transactionEntity) {

        this.transactionId = transactionEntity.getTransactionId();
        this.transactionStatus = transactionEntity.getTransactionStatus();
        this.paidFlag = transactionEntity.isPaidFlag();
        this.shippedFlag = transactionEntity.isShippedFlag();
        this.notificationFlag = transactionEntity.isNotificationFlag();
        this.paymentStatusDetail = transactionEntity.getPaymentStatusDetail();
        this.reminderSentFlag = transactionEntity.isReminderSentFlag();
        this.expiredSentFlag = transactionEntity.isExpiredSentFlag();
        this.billPaymentErrorFlag = transactionEntity.isBillPaymentErrorFlag();
        this.registDate = transactionEntity.getRegistDate();
        this.processTime = transactionEntity.getProcessTime();
        this.processType = transactionEntity.getProcessType();
        this.processPersonName = transactionEntity.getProcessPersonName();
        this.adminMemo = transactionEntity.getAdminMemo();
        this.customerId = transactionEntity.getCustomerId();
        this.orderReceivedId = transactionEntity.getOrderReceivedId();
        this.preClaimFlag = transactionEntity.isPreClaimFlag();
        this.noveltyPresentJudgmentStatus = transactionEntity.getNoveltyPresentJudgmentStatus();
    }
}