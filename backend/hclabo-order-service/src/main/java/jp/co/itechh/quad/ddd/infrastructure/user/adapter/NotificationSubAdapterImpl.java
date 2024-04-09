/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.user.adapter.INotificationSubAdapter;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.OrderConfirmationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PaymentDepositedRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PaymentExcessAlertRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementExpirationNotificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementReminderRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ShipmentNotificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ShipmentRegistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 通知アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class NotificationSubAdapterImpl implements INotificationSubAdapter {

    /** 通知サブAPI */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /**
     * コンストラクタ
     *
     * @param notificationSubApi 通知サブAPI
     * @param headerParamsUtil   ヘッダパラメーターユーティル
     * @param asyncService       非同期処理サービス
     */
    @Autowired
    public NotificationSubAdapterImpl(NotificationSubApi notificationSubApi,
                                      HeaderParamsUtility headerParamsUtil,
                                      AsyncService asyncService) {
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.notificationSubApi.getApiClient());
    }

    /**
     * 注文確認要求（注文確認メール送信）
     *
     * @param orderCode 受注コード
     * @return NotificationsResult 通知結果
     */
    @Override
    public void orderConfirmation(OrderCode orderCode) {

        OrderConfirmationRequest orderConfirmationRequest = new OrderConfirmationRequest();
        orderConfirmationRequest.setOrderCode(orderCode.getValue());

        Object[] args = new Object[] {orderConfirmationRequest};
        Class<?>[] argsClass = new Class<?>[] {OrderConfirmationRequest.class};

        asyncService.asyncService(notificationSubApi, "orderConfirmation", args, argsClass);
    }

    /**
     * 出荷完了メール送信要求
     *
     * @param orderCode 受注コード
     * @return NotificationsResult 通知結果
     */
    @Override
    public void shipmentNotification(OrderCode orderCode) {

        ShipmentNotificationRequest shipmentNotificationRequest = new ShipmentNotificationRequest();
        shipmentNotificationRequest.setOrderCodeList(Collections.singletonList(orderCode.getValue()));

        Object[] args = new Object[] {shipmentNotificationRequest};
        Class<?>[] argsClass = new Class<?>[] {ShipmentNotificationRequest.class};

        asyncService.asyncService(notificationSubApi, "shipmentNotification", args, argsClass);
    }

    /**
     * 出荷登録バッチエラーメール送信
     *
     * @param message メッセージ
     */
    @Override
    public void shipmenRegist(String message) {

        ShipmentRegistRequest request = new ShipmentRegistRequest();
        request.setErrorMailMessage(message);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {ShipmentRegistRequest.class};

        asyncService.asyncService(notificationSubApi, "shipmentRegist", args, argsClass);
    }

    /**
     * @param orderCodes 受注番号リスト
     * @param overFlag   超過フラグ
     */
    @Override
    public void paymentDeposited(List<String> orderCodes, boolean overFlag) {

        PaymentDepositedRequest request = new PaymentDepositedRequest();
        request.setTargetOrderList(orderCodes);
        request.setOverFlag(overFlag);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {PaymentDepositedRequest.class};

        asyncService.asyncService(notificationSubApi, "paymentDeposited", args, argsClass);
    }

    /**
     * 入金過不足アラートメール送信要求（管理者）
     *
     * @param orderCodes 受注番号リスト
     * @param overFlag   超過フラグ
     */
    @Override
    public void paymentExcessAlert(List<String> orderCodes, boolean overFlag) {

        PaymentExcessAlertRequest request = new PaymentExcessAlertRequest();
        request.setOrderCodeList(orderCodes);
        request.setOverFlag(overFlag);

        Object[] args = new Object[] {request};
        Class<?>[] argsClass = new Class<?>[] {PaymentExcessAlertRequest.class};

        asyncService.asyncService(notificationSubApi, "paymentExcessAlert", args, argsClass);
    }

    /**
     * 支払督促
     *
     * @param settlementReminderRequest 受注決済督促
     */
    @Override
    public void reminderPayment(SettlementReminderRequest settlementReminderRequest) {

        Object[] args = new Object[] {settlementReminderRequest};
        Class<?>[] argsClass = new Class<?>[] {SettlementReminderRequest.class};

        asyncService.asyncService(notificationSubApi, "settlementReminder", args, argsClass);
    }

    /**
     * 支払期限切れ
     *
     * @param settlementExpirationNotificationRequest 受注決済期限切れメール送信
     */
    @Override
    public void expiredPayment(SettlementExpirationNotificationRequest settlementExpirationNotificationRequest) {

        Object[] args = new Object[] {settlementExpirationNotificationRequest};
        Class<?>[] argsClass = new Class<?>[] {SettlementExpirationNotificationRequest.class};

        asyncService.asyncService(notificationSubApi, "settlementExpirationNotificationMail", args, argsClass);
    }

    /**
     * 請求不整合報告
     *
     * @param settlementMismatchRequest 請求不整合報告リクエスト
     */
    @Override
    public void settlementMisMatch(SettlementMismatchRequest settlementMismatchRequest) {

        Object[] args = new Object[] {settlementMismatchRequest};
        Class<?>[] argsClass = new Class<?>[] {SettlementMismatchRequest.class};

        asyncService.asyncService(notificationSubApi, "settlementMismatch", args, argsClass);
    }
}