/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter;

import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementExpirationNotificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementMismatchRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementReminderRequest;

import java.util.List;

/**
 * ユーザーマイクロサービス
 * 通知アダプター
 */
public interface INotificationSubAdapter {

    /**
     * 注文確認要求（注文確認メール送信）
     *
     * @param orderCode 受注コード
     */
    void orderConfirmation(OrderCode orderCode);

    /**
     * 出荷完了メール送信要求
     *
     * @param orderCode 受注コード
     */
    void shipmentNotification(OrderCode orderCode);

    /**
     * 出荷登録バッチエラーメール送信
     *
     * @param message メッセージ
     */
    void shipmenRegist(String message);

    /**
     * 入金完了メール送信要求
     *
     * @param orderCodes 受注番号リスト
     * @param overFlag   超過フラグ
     */
    void paymentDeposited(List<String> orderCodes, boolean overFlag);

    /**
     * 入金過不足アラートメール送信要求（管理者）
     *
     * @param orderCodes 受注番号リスト
     * @param overFlag   超過フラグ
     */
    void paymentExcessAlert(List<String> orderCodes, boolean overFlag);

    /**
     * 支払督促
     *
     * @param settlementReminderRequest 受注決済督促
     */
    void reminderPayment(SettlementReminderRequest settlementReminderRequest);

    /**
     * 支払期限切れ
     *
     * @param settlementExpirationNotificationRequest 受注決済期限切れメール送信
     */
    void expiredPayment(SettlementExpirationNotificationRequest settlementExpirationNotificationRequest);

    /**
     * 請求不整合報告
     *
     * @param settlementMismatchRequest 請求不整合報告リクエスト
     */
    void settlementMisMatch(SettlementMismatchRequest settlementMismatchRequest);
}
