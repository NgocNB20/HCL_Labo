/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.entity;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderReceivedId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.PaymentStatusDetail;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Date;

/**
 * 改訂用取引エンティティ
 */
@Getter
public class TransactionForRevisionEntity extends TransactionEntity {

    /** 改訂用取引ID */
    private final TransactionRevisionId transactionRevisionId;

    /**
     * コンストラクタ<br>
     * 改訂用取引発行
     *
     * @param originalTransactionEntity
     * @param registDate
     */
    public TransactionForRevisionEntity(TransactionEntity originalTransactionEntity, Date registDate) {

        super();

        // アサートチェック
        AssertChecker.assertNotNull("originalTransactionEntity is null", originalTransactionEntity);
        AssertChecker.assertNotNull("registDate is null", registDate);

        /* 設定 */
        // 元取引をコピー
        super.copyProperties(originalTransactionEntity);
        // 改訂取引用設定
        this.transactionRevisionId = new TransactionRevisionId();
        this.registDate = registDate;
        this.processTime = null;
        this.processType = null;
        this.processPersonName = null;
    }

    /**
     * 出荷確定
     */
    public void openShipment() {

        // チェック
        // 出荷済みならエラー
        if (this.shippedFlag) {
            throw new DomainException("ORDER-TREV0001-E");
        }
        // 確定でないならエラー
        if (this.transactionStatus != TransactionStatus.OPEN) {
            throw new DomainException("ORDER-TREV0002-E");
        }

        //設定
        this.shippedFlag = true;
    }

    /**
     * 取引取消
     *
     * @param adminMemo
     * @param paidFlag
     * @param paymentStatusDetail
     */
    public void cancelTransaction(String adminMemo, boolean paidFlag, PaymentStatusDetail paymentStatusDetail) {

        // 確定、終了、一時停止状態でないならエラー
        if (this.transactionStatus != TransactionStatus.OPEN && this.transactionStatus != TransactionStatus.CLOSE) {
            throw new DomainException("ORDER-TREV0003-E");
        }

        // 取引ステータス取消
        this.transactionStatus = TransactionStatus.CANCEL;
        this.adminMemo = adminMemo;
        this.paidFlag = paidFlag;
        this.paymentStatusDetail = paymentStatusDetail;
        this.billPaymentErrorFlag = false;
    }

    /**
     * 入金督促通知済み設定
     */
    public void settingReminderSent() {

        // チェック
        // 入金関連通知実施フラグがtrueでないならエラー
        if (!this.notificationFlag) {
            throw new DomainException("ORDER-TREV0005-E");
        }
        // 確定でないならエラー
        if (this.transactionStatus != TransactionStatus.OPEN) {
            throw new DomainException("ORDER-TREV0006-E");
        }

        //設定
        this.reminderSentFlag = true;
    }

    /**
     * 管理メモ設定
     *
     * @param adminMemo
     */
    public void settingAdminMemo(String adminMemo) {
        this.adminMemo = adminMemo;
    }

    /**
     * @param noveltyPresentJudgmentStatus ノベルティプレゼント判定状態
     */
    public void settingNoveltyPresentJudgmentStatus(String noveltyPresentJudgmentStatus) {
        this.noveltyPresentJudgmentStatus = EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentJudgmentStatus.class,
                                                                          noveltyPresentJudgmentStatus
                                                                         );
    }

    /**
     * 再オーソリの結果設定
     */
    public void settingReAuthResult() {

        StringBuilder authoryMemo = new StringBuilder();
        // 現在のメモが設定されている場合は、改行を設定
        if (StringUtils.isNotBlank(this.adminMemo)) {
            authoryMemo.append(this.adminMemo);
            authoryMemo.append(System.getProperty("line.separator"));
        }

        // 実施日及び固定文言を設定
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        authoryMemo.append(MessageFormat.format(PropertiesUtil.getSystemPropertiesValue("order.reauthory.memo"),
                                                dateUtility.formatYmdWithSlash(dateUtility.getCurrentDate())
                                               ));

        this.adminMemo = authoryMemo.toString();
    }

    /**
     * 請求決済エラー解除
     */
    public void releaseBillPaymentError() {

        // チェック
        // 確定状態でないならエラー
        if (this.transactionStatus != TransactionStatus.OPEN) {
            throw new DomainException("ORDER-TREV0010-E");
        }
        // 請求決済エラーフラグがOFFの場合エラー
        if (!this.billPaymentErrorFlag) {
            throw new DomainException("ORDER-TREV0012-E");
        }

        this.billPaymentErrorFlag = false;
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public TransactionForRevisionEntity(TransactionId transactionId,
                                        TransactionStatus transactionStatus,
                                        Boolean paidFlag,
                                        PaymentStatusDetail paymentStatusDetail,
                                        Boolean shippedFlag,
                                        Boolean billPaymentErrorFlag,
                                        Boolean notificationFlag,
                                        Boolean reminderSentFlag,
                                        Boolean expiredSentFlag,
                                        String adminMemo,
                                        String customerId,
                                        OrderReceivedId orderReceivedId,
                                        Date registDate,
                                        Date processTime,
                                        HTypeProcessType processType,
                                        String processPersonName,
                                        TransactionRevisionId transactionRevisionId,
                                        boolean preClaim,
                                        HTypeNoveltyPresentJudgmentStatus noveltyPresentJudgmentStatus) {
        super(transactionId, transactionStatus, paidFlag, paymentStatusDetail, shippedFlag, billPaymentErrorFlag,
              notificationFlag, reminderSentFlag, expiredSentFlag, adminMemo, customerId, orderReceivedId, registDate,
              processTime, processType, processPersonName, preClaim, noveltyPresentJudgmentStatus
             );
        this.transactionRevisionId = transactionRevisionId;
    }
}