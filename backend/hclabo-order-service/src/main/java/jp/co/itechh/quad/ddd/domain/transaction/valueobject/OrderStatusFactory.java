/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.valueobject;

/**
 * 受注状態(ENUM) 値オブジェクト ファクトリ
 */
public class OrderStatusFactory {

    /**
     * 受注状態 生成<br/>
     *
     * @param transactionStatus
     * @param paidFlag
     * @param shippedFlag
     * @param billPaymentErrorFlag
     * @param preClaimFlag
     * @return OrderStatus
     */
    public static OrderStatus constructOrderStatus(TransactionStatus transactionStatus,
                                                   boolean paidFlag,
                                                   boolean shippedFlag,
                                                   boolean billPaymentErrorFlag,
                                                   boolean preClaimFlag) {

        // 【取引.取引ステータス =「取消」】
        if (transactionStatus == TransactionStatus.CANCEL) {
            // キャンセル
            return OrderStatus.CANCEL;
        }

        // 【取引.取引ステータス =「確定」】且つ 【取引.請求決済エラーフラグ =「ON」】
        if (transactionStatus == TransactionStatus.OPEN && billPaymentErrorFlag) {
            // 請求決済エラー
            return OrderStatus.PAYMENT_ERROR;
        }

        // 【取引.取引ステータス =「確定」】且つ 【取引.請求決済エラーフラグ =「OFF」】
        if (transactionStatus == TransactionStatus.OPEN && !billPaymentErrorFlag) {

            // 【取引.出荷済フラグ =「ON」】
            if (shippedFlag) {
                // 出荷完了
                return OrderStatus.SHIPMENT_COMPLETION;
            }

            /* 入金待ち か 商品準備中 の判定を行う */
            if (preClaimFlag && !paidFlag) {
                // 【取引.前請求フラグ= 「ON」 & 取引.入金済み済フラグ=「OFF」】
                // 入金待ち
                return OrderStatus.PAYMENT_CONFIRMING;
            } else {
                //【取引.出荷済フラグ =「OFF」】
                // 商品準備中
                return OrderStatus.ITEM_PREPARING;
            }
        }

        return null;
    }

}