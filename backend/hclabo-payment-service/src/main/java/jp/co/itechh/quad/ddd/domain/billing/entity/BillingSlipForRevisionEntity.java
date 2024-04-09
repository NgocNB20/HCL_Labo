/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingAddressId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingSlipId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.BillingSlipRevisionId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus.IBillingStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.Date;

/**
 * 改訂用請求伝票エンティティ
 */
@Getter
public class BillingSlipForRevisionEntity extends BillingSlipEntity {

    /** 改訂用請求伝票ID */
    private BillingSlipRevisionId billingSlipRevisionId;

    /** 改訂用取引ID */
    private String transactionRevisionId;

    /** 改訂用注文決済 */
    private OrderPaymentForRevisionEntity orderPaymentForRevisionEntity;

    /**
     * コンストラクタ
     * 改訂用請求伝票発行
     *
     * @param originBillingSlipEntity       改訂元請求伝票
     * @param transactionRevisionId         改訂用取引ID
     * @param orderPaymentForRevisionEntity 改訂用注文決済エンティティ
     * @param registDate                    登録日時
     */
    public BillingSlipForRevisionEntity(BillingSlipEntity originBillingSlipEntity,
                                        String transactionRevisionId,
                                        OrderPaymentForRevisionEntity orderPaymentForRevisionEntity,
                                        Date registDate) {

        super();

        // チェック
        AssertChecker.assertNotNull("originBillingSlipEntity is null", originBillingSlipEntity);
        AssertChecker.assertNotNull("transactionRevisionId is null", transactionRevisionId);
        AssertChecker.assertNotNull("orderPaymentForRevisionEntity is null", orderPaymentForRevisionEntity);
        AssertChecker.assertNotNull("registDate is null", registDate);

        /* 設定 */
        // 元請求伝票をコピー
        super.copyProperties(originBillingSlipEntity);
        // 改訂用請求伝票設定
        this.billingSlipRevisionId = new BillingSlipRevisionId();
        this.transactionRevisionId = transactionRevisionId;
        this.orderPaymentForRevisionEntity = orderPaymentForRevisionEntity;
        this.registDate = registDate;
    }

    /**
     * 請求金額設定
     *
     * @param billedPrice
     */
    public void settingBilledPrice(int billedPrice) {
        // チェック
        // TODO 未実装

        this.billedPrice = billedPrice;
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public BillingSlipForRevisionEntity(BillingSlipId billingSlipId,
                                        IBillingStatus billingStatus,
                                        int billedPrice,
                                        HTypeBillType billingType,
                                        Date registDate,
                                        String transactionId,
                                        BillingAddressId billingAddressId,
                                        Date moneyReceiptTime,
                                        int moneyReceiptAmountTotal,
                                        OrderPaymentEntity orderPaymentEntity,
                                        BillingSlipRevisionId billingSlipRevisionId,
                                        String transactionRevisionId,
                                        OrderPaymentForRevisionEntity orderPaymentForRevisionEntity) {
        super(billingSlipId, billingStatus, billedPrice, billingType, registDate, transactionId, billingAddressId,
              moneyReceiptTime, moneyReceiptAmountTotal, orderPaymentEntity
             );
        this.billingSlipRevisionId = billingSlipRevisionId;
        this.transactionRevisionId = transactionRevisionId;
        this.orderPaymentForRevisionEntity = orderPaymentForRevisionEntity;
    }
}