/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentId;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.OrderPaymentStatus;
import jp.co.itechh.quad.ddd.domain.billing.valueobject.paymentrequest.IPaymentRequest;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 注文決済エンティティ
 */
@Getter
public class OrderPaymentEntity {

    /** 注文決済ID */
    protected OrderPaymentId orderPaymentId;

    /** 注文決済ステータス */
    protected OrderPaymentStatus orderPaymentStatus;

    /** 決済方法種別 */
    protected HTypeSettlementMethodType settlementMethodType;

    /** 決済依頼 */
    protected IPaymentRequest paymentRequest;

    /** 決済方法ID */
    protected String paymentMethodId;

    /** 決済方法名 */
    protected String paymentMethodName;

    /** 受注番号 */
    protected String orderCode;

    /**
     * コンストラクタ<br/>
     * 注文決済作成
     *
     * @param orderCode
     */
    public OrderPaymentEntity(String orderCode) {

        // チェック
        AssertChecker.assertNotEmpty("orderCode is empty", orderCode);

        // 設定
        this.orderPaymentId = new OrderPaymentId();
        this.orderPaymentStatus = OrderPaymentStatus.DRAFT;
        this.orderCode = orderCode;
    }

    /**
     * ステータス確定
     */
    public void openStatus() {

        // ステータスが下書き状態でない場合はエラー
        if (this.orderPaymentStatus != OrderPaymentStatus.DRAFT
            && this.orderPaymentStatus != OrderPaymentStatus.UNDER_SETTLEMENT) {
            throw new DomainException("PAYMENT_ORDP0001-E");
        }

        // 注文決済を確定状態にする
        this.orderPaymentStatus = OrderPaymentStatus.OPEN;
    }

    /**
     * ステータス途中決済
     */
    public void underSettlementStatus() {

        // クレジット決済でない、ステータスが下書き状態でない場合はエラー
        if (this.settlementMethodType != HTypeSettlementMethodType.CREDIT
            || this.orderPaymentStatus != OrderPaymentStatus.DRAFT) {
            throw new DomainException("PAYMENT_ORDP0005-E");
        }

        // 注文決済を途中決済状態にする
        this.orderPaymentStatus = OrderPaymentStatus.UNDER_SETTLEMENT;
    }

    /**
     * ステータス途中決済: リンク決済
     */
    public void underSettlementStatusLinkPay() {

        // リンク決済でない、ステータスが下書き状態でない場合はエラー
        if (this.settlementMethodType != HTypeSettlementMethodType.LINK_PAYMENT
            || this.orderPaymentStatus != OrderPaymentStatus.DRAFT) {
            throw new DomainException("PAYMENT_ORDP0006-E");
        }

        // 注文決済を途中決済状態にする
        this.orderPaymentStatus = OrderPaymentStatus.UNDER_SETTLEMENT;
    }

    /**
     * 認証後ステータス確定
     */
    public void openStatusAfterSecure() {

        // クレジット決済でない、またはステータスが決済途中でない場合はエラー
        if (this.settlementMethodType != HTypeSettlementMethodType.CREDIT
            || this.orderPaymentStatus != OrderPaymentStatus.UNDER_SETTLEMENT) {
            throw new DomainException("PAYMENT_ORDP0002-E");
        }

        // 注文決済を確定状態にする
        this.orderPaymentStatus = OrderPaymentStatus.OPEN;
    }

    /**
     * 途中決済を復元
     */
    public void restoreUnderSettlement(String orderCode) {

        // ステータスが確定の場合はエラー
        if (this.orderPaymentStatus == OrderPaymentStatus.OPEN) {
            throw new DomainException("PAYMENT_ORDP0001-E");
        }

        // 注文決済を下書き状態にする
        this.orderPaymentStatus = OrderPaymentStatus.DRAFT;

        // 受注番号再設定
        if (!StringUtils.isBlank(orderCode)) {
            this.orderCode = orderCode;
        }
    }

    /**
     * コンストラクタ<br/>
     * 決済改訂(改訂用注文決済から注文決済生成)
     *
     * @param orderPaymentForRevisionEntity
     */
    public OrderPaymentEntity(OrderPaymentForRevisionEntity orderPaymentForRevisionEntity) {

        // チェック
        AssertChecker.assertNotNull("orderPaymentForRevisionEntity is null", orderPaymentForRevisionEntity);

        /* 設定 */
        // 改訂用注文決済をコピー
        copyProperties(orderPaymentForRevisionEntity);
        // 改訂用注文決済用IDを引き継いでID設定
        this.orderPaymentId = new OrderPaymentId(orderPaymentForRevisionEntity.getOrderPaymentRevisionId().getValue());
    }

    /**
     * 決済方法ID setter
     * ※パッケージプライベート
     *
     * @param paymentMethodId
     */
    void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    /**
     * 決済方法名 setter
     * ※パッケージプライベート
     *
     * @param paymentMethodName
     */
    void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    /**
     * 決済種別 setter
     * ※パッケージプライベート
     *
     * @param settlementMethodType
     */
    void setSettlementMethodType(HTypeSettlementMethodType settlementMethodType) {
        this.settlementMethodType = settlementMethodType;
    }

    /**
     * 決済依頼 setter
     * ※パッケージプライベート
     *
     * @param paymentRequest
     */
    void setPaymentRequest(IPaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    /**
     * ステータス setter<br/>
     * ※パッケージプライベート
     */
    void setOrderPaymentStatus(OrderPaymentStatus orderPaymentStatus) {
        this.orderPaymentStatus = orderPaymentStatus;
    }

    /**
     * コンストラクタ<br/>
     * ※改訂取引用パッケージプライベート
     */
    OrderPaymentEntity() {
    }

    /**
     * プロパティコピー<br/>
     * ※改訂注文決済用
     *
     * @param orderPaymentEntity
     */
    protected void copyProperties(OrderPaymentEntity orderPaymentEntity) {

        this.orderPaymentId = orderPaymentEntity.getOrderPaymentId();
        this.orderPaymentStatus = orderPaymentEntity.getOrderPaymentStatus();
        this.settlementMethodType = orderPaymentEntity.getSettlementMethodType();
        this.paymentRequest = orderPaymentEntity.getPaymentRequest();
        this.paymentMethodId = orderPaymentEntity.getPaymentMethodId();
        this.paymentMethodName = orderPaymentEntity.getPaymentMethodName();
        this.orderCode = orderPaymentEntity.getOrderCode();
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public OrderPaymentEntity(OrderPaymentId orderPaymentId,
                              OrderPaymentStatus orderPaymentStatus,
                              HTypeSettlementMethodType settlementMethodType,
                              IPaymentRequest paymentRequest,
                              String paymentMethodId,
                              String paymentMethodName,
                              String orderCode) {
        this.orderPaymentId = orderPaymentId;
        this.orderPaymentStatus = orderPaymentStatus;
        this.settlementMethodType = settlementMethodType;
        this.paymentRequest = paymentRequest;
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodName = paymentMethodName;
        this.orderCode = orderCode;
    }
}