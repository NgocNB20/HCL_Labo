/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingAddressId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingSlipId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.IBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PendingBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PostClaimBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.PreClaimBillingStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;

import java.util.Date;

/**
 * 請求伝票エンティティ
 */
@Getter
public class BillingSlipEntity {

    /** 請求伝票ID */
    protected BillingSlipId billingSlipId;

    /** 請求ステータス */
    protected IBillingStatus billingStatus;

    /** 請求済金額 */
    protected int billedPrice;

    /** 請求種別 */
    protected HTypeBillType billingType;

    /** 入金日時 */
    protected Date moneyReceiptTime;

    /** 累計入金額 */
    protected int moneyReceiptAmountTotal;

    /** 登録日時 */
    protected Date registDate;

    /** 取引ID */
    protected String transactionId;

    /** 請求先住所ID */
    protected BillingAddressId billingAddressId;

    /** 注文決済エンティティ ※この改訂用エンティティは、ここは常にnull(DB管理なし) */
    protected OrderPaymentEntity orderPaymentEntity;

    /**
     * コンストラクタ<br/>
     * 請求伝票発行
     *
     * @param transactionId      取引ID
     * @param registDate         登録日時
     * @param orderPaymentEntity 注文決済
     */
    public BillingSlipEntity(String transactionId, Date registDate, OrderPaymentEntity orderPaymentEntity) {

        // チェック
        AssertChecker.assertNotEmpty("transactionId is empty", transactionId);
        AssertChecker.assertNotNull("registDate is null", registDate);
        AssertChecker.assertNotNull("orderPaymentEntity is null", orderPaymentEntity);

        // 設定
        this.billingSlipId = new BillingSlipId();
        this.billingType = null;
        // 請求種別未定時の下書き状態
        this.billingStatus = PendingBillingStatus.DRAFT;
        this.orderPaymentEntity = orderPaymentEntity;
        this.transactionId = transactionId;
        this.registDate = registDate;
    }

    /**
     * コンストラクタ<br/>
     * 請求伝票改訂(改訂用請求伝票から請求伝票生成)
     *
     * @param billingSlipForRevisionEntity
     * @param registDate
     * @param paymentAgencyReleaseFlagForOrigin
     */
    public BillingSlipEntity(BillingSlipForRevisionEntity billingSlipForRevisionEntity,
                             Date registDate,
                             boolean paymentAgencyReleaseFlagForOrigin) {

        // チェック
        AssertChecker.assertNotNull("billingSlipForRevisionEntity is null", billingSlipForRevisionEntity);
        AssertChecker.assertNotNull("registDate is null", registDate);

        /* 請求決済エラー解消のチェック */
        // 判定用フラグ
        boolean billPaymentErrorReleaseFlag = false;
        // クレジット決済の場合
        if (billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity()
                                        .getPaymentRequest() instanceof CreditPayment) {
            CreditPayment creditPaymentForRevision =
                            (CreditPayment) billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity()
                                                                        .getPaymentRequest();

            // 改訂元の決済代行連携解除フラグがOFFであり、改訂後がONの場合
            if (!paymentAgencyReleaseFlagForOrigin && creditPaymentForRevision.isGmoReleaseFlag()) {
                billPaymentErrorReleaseFlag = true;
            }
        }

        /* 設定 */
        // 改訂取引をコピー
        copyProperties(billingSlipForRevisionEntity);
        // 改訂用請求伝票用設定
        this.billingSlipId = new BillingSlipId(billingSlipForRevisionEntity.getBillingSlipRevisionId().getValue());
        this.transactionId = billingSlipForRevisionEntity.getTransactionRevisionId();
        // 注文決済改訂
        this.orderPaymentEntity =
                        new OrderPaymentEntity(billingSlipForRevisionEntity.getOrderPaymentForRevisionEntity());
        this.registDate = registDate;

        // 請求決済エラーが解消された場合
        if (billPaymentErrorReleaseFlag) {
            // 請求伝票.請求ステータスを「一時停止」⇒「売上」に更新
            this.billingStatus = PostClaimBillingStatus.SALES;
        }

    }

    /**
     * 請求先設定
     *
     * @param billingAddressId 請求先住所ID
     */
    public void settingBillingAddress(BillingAddressId billingAddressId) {

        // チェック
        AssertChecker.assertNotNull("billingAddressId is null", billingAddressId);

        // 設定
        this.billingAddressId = billingAddressId;
    }

    /**
     * 請求種別設定
     *
     * @param billingType 請求種別
     */
    public void settingBillingType(HTypeBillType billingType) {

        // チェック
        // 下書き状態でないならエラー
        if (!isDraft()) {
            throw new DomainException("PAYMENT_BLSE0001-E");
        }

        // 設定
        this.billingType = billingType;

        // 請求ステータスを請求種別に一致した最適なインスタンスの下書き状態に更新する
        optimizeDraftBillingStatus();

    }

    /**
     * 請求伝票確定
     *
     * @param billedPrice
     * @param orderPaymentEntity
     */
    public void openSlip(int billedPrice, OrderPaymentEntity orderPaymentEntity) {

        // チェック
        // 下書き状態でないならエラー
        if (!isDraft()) {
            throw new DomainException("PAYMENT_BLSE0001-E");
        }
        // 請求先未設定ならエラー
        AssertChecker.assertNotNull("billingAddressId is null", this.billingAddressId);
        // 注文決済が確定でないならエラー
        if (orderPaymentEntity.getOrderPaymentStatus() != OrderPaymentStatus.OPEN) {
            throw new DomainException("PAYMENT_BLSE0002-E");
        }

        // 請求ステータス確定
        AssertChecker.assertNotNull("billingType is null", this.billingType);
        if (this.billingType == HTypeBillType.PRE_CLAIM) {
            // 請求種別が前払いの場合は、入金待ち
            this.billingStatus = PreClaimBillingStatus.DEPOSIT_WAITING;
        } else if (this.billingType == HTypeBillType.POST_CLAIM) {
            // 請求種別が後払いの場合は、オーソリ済み
            this.billingStatus = PostClaimBillingStatus.AUTHORIZED;
        }

        // 設定
        this.billedPrice = billedPrice;
    }

    /**
     * 伝票取消
     */
    public void cancelSlip(Date receiptTime, Integer moneyReceiptAmountTotal) {

        // チェック
        AssertChecker.assertNotNull("receiptTime is null", receiptTime);

        // 取消不可の場合はエラー
        if (!isAbleCancel()) {
            throw new DomainException("PAYMENT_BLSE0004-E");
        }

        // 請求種別が前払いの場合
        if (this.billingType == HTypeBillType.PRE_CLAIM) {
            // 前請求ステータスを取消にする
            this.billingStatus = PreClaimBillingStatus.CANCEL;
        }
        // 請求種別が後払いの場合
        else if (this.billingType == HTypeBillType.POST_CLAIM) {
            // 後請求ステータスを取消にする
            this.billingStatus = PostClaimBillingStatus.CANCEL;
        }

        // 請求金額設定
        this.billedPrice = 0;
        this.moneyReceiptTime = receiptTime;
        if (moneyReceiptAmountTotal != null) {
            this.moneyReceiptAmountTotal = moneyReceiptAmountTotal;
        }

    }

    /**
     * 伝票入金
     * ※前請求用
     *
     * @param preClaimReceiptTime
     * @param moneyReceiptAmountTotal
     */
    public void depositSlip(Date preClaimReceiptTime, int moneyReceiptAmountTotal) {

        // チェック
        AssertChecker.assertNotNull("preClaimReceiptTime is null", preClaimReceiptTime);

        // チェック
        // 請求種別が前払い入金待ち以外の場合はエラー
        if (this.billingType != HTypeBillType.PRE_CLAIM
            || this.billingStatus != PreClaimBillingStatus.DEPOSIT_WAITING) {
            throw new DomainException("NOT DEPOSIT_WAITING");
        }

        // 入金日時
        this.moneyReceiptTime = preClaimReceiptTime;
        // 累計入金額
        this.moneyReceiptAmountTotal = moneyReceiptAmountTotal;

        // 前請求ステータスを入金にする
        if (!isInsufficientMoney()) {
            this.billingStatus = PreClaimBillingStatus.DEPOSITED;
        }
    }

    /**
     * 売上確定
     * ※後請求用
     */
    public void openSales(Date postClaimReceiptTime, int moneyReceiptAmountTotal) {

        // チェック
        AssertChecker.assertNotNull("postClaimReceiptTime is null", postClaimReceiptTime);
        // 請求種別が後払いオーソリ済みでないならエラー
        if (this.billingType != HTypeBillType.POST_CLAIM || this.billingStatus != PostClaimBillingStatus.AUTHORIZED) {
            throw new DomainException("PAYMENT_BLSE0005-E");
        }

        // 累計入金額
        this.moneyReceiptAmountTotal = moneyReceiptAmountTotal;
        // 請求伝票.入金日時=引数.入金日時
        this.moneyReceiptTime = postClaimReceiptTime;

        // 後請求ステータスを売上確定にする
        if (!isInsufficientMoney()) {
            this.billingStatus = PostClaimBillingStatus.SALES;
        }
    }

    /**
     * 入金更新
     */
    public void setMoneyReceipt(Date receiptTime, int moneyReceiptAmountTotal) {

        // チェック
        AssertChecker.assertNotNull("receiptTime is null", receiptTime);

        // 累計入金額
        this.moneyReceiptAmountTotal = moneyReceiptAmountTotal;
        // 請求伝票.入金日時=引数.入金日時
        this.moneyReceiptTime = receiptTime;
    }

    /**
     * 伝票一時停止
     * ※後払い請求用
     */
    public void suspendSlip() {

        // チェック
        // 請求種別が後払いオーソリ済み状態でないならエラー
        if (this.billingType != HTypeBillType.POST_CLAIM || this.billingStatus != PostClaimBillingStatus.AUTHORIZED) {
            throw new DomainException("PAYMENT_BLSE0005-E");
        }

        // 後請求ステータスを一時停止にする
        this.billingStatus = PostClaimBillingStatus.SUSPENDED;
    }

    /**
     * 下書き状態か判定
     *
     * @return boolean
     */
    public boolean isDraft() {

        if (this.billingType == null) {
            // 請求種別が未定の場合
            return this.billingStatus == PendingBillingStatus.DRAFT;
        } else if (this.billingType == HTypeBillType.PRE_CLAIM) {
            // 請求種別が前払いの場合
            return this.billingStatus == PreClaimBillingStatus.DRAFT;
        } else if (this.billingType == HTypeBillType.POST_CLAIM) {
            // 請求種別が後払いの場合
            return this.billingStatus == PostClaimBillingStatus.DRAFT;
        }

        return false;
    }

    /**
     * 確定状態か判定
     *
     * @return boolean
     */
    boolean isOpen() {

        if (this.billingType == HTypeBillType.PRE_CLAIM) {
            // 請求種別が前払いの場合
            return this.billingStatus == PreClaimBillingStatus.DEPOSIT_WAITING;
        } else if (this.billingType == HTypeBillType.POST_CLAIM) {
            // 請求種別が後払いの場合
            return this.billingStatus == PostClaimBillingStatus.AUTHORIZED;
        }

        return false;
    }

    /**
     * 売上確定状態か判定
     *
     * @return boolean
     */
    public boolean isOpenSales() {

        if (this.billingType == HTypeBillType.POST_CLAIM) {
            // 請求種別が後払いの場合
            return this.billingStatus == PostClaimBillingStatus.SALES;
        }

        return false;
    }

    /**
     * 取消状態か判定
     *
     * @return boolean
     */
    public boolean isCancel() {

        if (this.billingType == HTypeBillType.PRE_CLAIM) {
            // 請求種別が前払いの場合
            return this.billingStatus == PreClaimBillingStatus.CANCEL;
        } else if (this.billingType == HTypeBillType.POST_CLAIM) {
            // 請求種別が後払いの場合
            return this.billingStatus == PostClaimBillingStatus.CANCEL;
        }

        return false;
    }

    /**
     * 一時停止状態か判定
     * TODO 前払い請求未考慮
     *
     * @return boolean
     */
    public boolean isSuspended() {

        if (this.billingType == HTypeBillType.POST_CLAIM) {
            // 請求種別が後払いの場合
            return this.billingStatus == PostClaimBillingStatus.SUSPENDED;
        }

        return false;
    }

    /**
     * 取消可能のステータスか判定
     *
     * @return true ... 取消可能
     */
    boolean isAbleCancel() {

        if (this.billingType == HTypeBillType.PRE_CLAIM) {
            // 請求種別が前払いの場合

            // 入金待ち、入金済み状態なら取消可能
            return (this.billingStatus == PreClaimBillingStatus.DEPOSITED
                    || this.billingStatus == PreClaimBillingStatus.DEPOSIT_WAITING);

        } else if (this.billingType == HTypeBillType.POST_CLAIM) {
            // 請求種別が後払いの場合

            // オーソリ済み、一時停止、または売上確定の場合なら取消可能
            return this.billingStatus == PostClaimBillingStatus.AUTHORIZED
                   || this.billingStatus == PostClaimBillingStatus.SUSPENDED
                   || this.billingStatus == PostClaimBillingStatus.SALES;
        }

        // それ以外は取り消し不可
        return false;
    }

    /**
     * 決済代行連携が解除されたか判定
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票
     * @return true ... 解除
     */
    public boolean isReleaseBillPaymentError(BillingSlipForRevisionEntity billingSlipForRevisionEntity) {

        // 改訂用請求伝票の請求ステータスが「一時停止」であり、請求伝票が「売上確定」の場合
        if (billingSlipForRevisionEntity.isSuspended() && isOpenSales()) {
            // 請求決済エラー解消
            return true;
        }
        return false;
    }

    /**
     * 入金済み判定
     *
     * @return true ... 入金済み
     */
    public boolean isPaid() {
        return moneyReceiptAmountTotal != 0 && billedPrice <= moneyReceiptAmountTotal;
    }

    /**
     * 入金不足か判定
     *
     * @return true ... 入金不足
     */
    public boolean isInsufficientMoney() {
        return billedPrice > moneyReceiptAmountTotal;
    }

    /**
     * 入金超過か判定
     *
     * @return true ... 入金超過
     */
    public boolean isOverMoney() {
        return billedPrice < moneyReceiptAmountTotal;
    }

    /**
     * 請求ステータスを請求種別に一致した最適なインスタンスの下書き状態に更新する
     */
    void optimizeDraftBillingStatus() {

        if (this.billingType == null) {
            // 請求種別が未定
            this.billingStatus = PendingBillingStatus.DRAFT;
        } else if (this.billingType == HTypeBillType.PRE_CLAIM) {
            // 請求種別が前払い
            this.billingStatus = PreClaimBillingStatus.DRAFT;
        } else if (this.billingType == HTypeBillType.POST_CLAIM) {
            // 請求種別が後払い
            this.billingStatus = PostClaimBillingStatus.DRAFT;
        }

        return;
    }

    /**
     * コンストラクタ<br/>
     * ※改訂取引用パッケージプライベート
     */
    BillingSlipEntity() {
    }

    /**
     * プロパティコピー<br/>
     * ※改訂請求伝票用
     *
     * @param billingSlipEntity
     */
    protected void copyProperties(BillingSlipEntity billingSlipEntity) {

        this.billingSlipId = billingSlipEntity.getBillingSlipId();
        this.billingStatus = billingSlipEntity.getBillingStatus();
        this.billedPrice = billingSlipEntity.getBilledPrice();
        this.billingType = billingSlipEntity.getBillingType();
        this.registDate = billingSlipEntity.getRegistDate();
        this.transactionId = billingSlipEntity.getTransactionId();
        this.billingAddressId = billingSlipEntity.getBillingAddressId();
        this.moneyReceiptTime = billingSlipEntity.getMoneyReceiptTime();
        this.moneyReceiptAmountTotal = billingSlipEntity.getMoneyReceiptAmountTotal();
        this.orderPaymentEntity = null;
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public BillingSlipEntity(BillingSlipId billingSlipId,
                             IBillingStatus billingStatus,
                             int billedPrice,
                             HTypeBillType billingType,
                             Date registDate,
                             String transactionId,
                             BillingAddressId billingAddressId,
                             Date moneyReceiptTime,
                             int moneyReceiptAmountTotal,
                             OrderPaymentEntity orderPaymentEntity) {
        this.billingSlipId = billingSlipId;
        this.billingStatus = billingStatus;
        this.billedPrice = billedPrice;
        this.billingType = billingType;
        this.registDate = registDate;
        this.transactionId = transactionId;
        this.billingAddressId = billingAddressId;
        this.moneyReceiptTime = moneyReceiptTime;
        this.moneyReceiptAmountTotal = moneyReceiptAmountTotal;
        this.orderPaymentEntity = orderPaymentEntity;
    }
}