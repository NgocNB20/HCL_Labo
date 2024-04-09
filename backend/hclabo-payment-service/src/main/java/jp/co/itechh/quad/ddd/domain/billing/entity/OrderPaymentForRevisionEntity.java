/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentRevisionId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.CreditPayment;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.IPaymentRequest;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 改訂用注文決済エンティティ
 */
@Getter
public class OrderPaymentForRevisionEntity extends OrderPaymentEntity {

    /** 改訂用注文決済ID */
    private OrderPaymentRevisionId orderPaymentRevisionId;

    /**
     * コンストラクタ<br/>
     * 改訂用注文決済作成
     *
     * @param originalOrderPaymentEntity 改訂元注文決済
     */
    public OrderPaymentForRevisionEntity(OrderPaymentEntity originalOrderPaymentEntity) {

        super();

        // チェック
        AssertChecker.assertNotNull("originalOrderPaymentEntity is null", originalOrderPaymentEntity);

        /* 設定 */
        // 元注文決済データをコピー
        super.copyProperties(originalOrderPaymentEntity);
        // 改訂用注文決済ID採番
        this.orderPaymentRevisionId = new OrderPaymentRevisionId();
    }

    /**
     * 決済代行連携解除設定
     * ※パッケージプライベート
     *
     * @param paymentAgencyReleaseFlag 決済代行連携解除フラグ
     */
    void settingPaymentAgencyRelease(boolean paymentAgencyReleaseFlag) {

        CreditPayment origin = (CreditPayment) this.paymentRequest;
        // コンストラクタを呼び出し、渡された連携解除フラグで再構築
        this.paymentRequest = new CreditPayment(origin.getPaymentToken(), origin.getMaskedCardNo(),
                                                origin.getExpirationDate(), origin.getPaymentType(),
                                                origin.getDividedNumber(), origin.isEnable3dSecureFlag(),
                                                origin.isRegistCardFlag(), origin.isUseRegistedCardFlag(),
                                                origin.getAuthLimitDate(), paymentAgencyReleaseFlag
        );
    }

    /**
     * オーソリ期限日再設定
     * ※パッケージプライベート
     */
    void resetAuthLimitDate() {

        CreditPayment origin = (CreditPayment) this.paymentRequest;
        // コンストラクタを呼び出す
        this.paymentRequest = new CreditPayment(origin.getPaymentToken(), origin.getMaskedCardNo(),
                                                origin.getExpirationDate(), origin.getPaymentType(),
                                                HTypeBillType.POST_CLAIM, origin.getDividedNumber(),
                                                origin.isEnable3dSecureFlag(), origin.isRegistCardFlag(),
                                                origin.isUseRegistedCardFlag()
        );
    }

    /**
     * オーソリ期限日削除
     * ※パッケージプライベート
     */
    void removeAuthLimitDate() {

        CreditPayment origin = (CreditPayment) this.paymentRequest;
        // コンストラクタを呼び出し、オーソリ期限日はnullで指定
        this.paymentRequest = new CreditPayment(origin.getPaymentToken(), origin.getMaskedCardNo(),
                                                origin.getExpirationDate(), origin.getPaymentType(),
                                                origin.getDividedNumber(), origin.isEnable3dSecureFlag(),
                                                origin.isRegistCardFlag(), origin.isUseRegistedCardFlag(), null,
                                                origin.isGmoReleaseFlag()
        );
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public OrderPaymentForRevisionEntity(OrderPaymentId orderPaymentId,
                                         OrderPaymentStatus orderPaymentStatus,
                                         HTypeSettlementMethodType settlementMethodType,
                                         IPaymentRequest paymentRequest,
                                         String paymentMethodId,
                                         String paymentMethodName,
                                         String orderCode,
                                         OrderPaymentRevisionId orderPaymentRevisionId) {
        super(orderPaymentId, orderPaymentStatus, settlementMethodType, paymentRequest, paymentMethodId,
              paymentMethodName, orderCode
             );
        this.orderPaymentRevisionId = orderPaymentRevisionId;
    }
}