/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter;

import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipForRevision;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.UpdateOrderSlipForRevisionParam;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;

import java.util.Date;

/**
 * 注文アダプター<br/>
 * ※プロモーションマイクロサービス
 */
public interface IOrderSlipAdapter {

    /**
     * 取引開始
     *
     * @param transactionId 取引ID
     * @param customerBirthday 顧客生年月日
     */
    void startTransaction(TransactionId transactionId, Date customerBirthday);

    /**
     * 下書き注文票チェック
     *
     * @param transactionId 取引ID
     * @param customerBirthday 顧客生年月日
     */
    void checkDraftOrderSlip(TransactionId transactionId, Date customerBirthday);

    /**
     * 注文票最新化
     *
     * @param transactionId 取引ID
     */
    void modernizeOrderSlip(TransactionId transactionId);

    /**
     * 注文票確定
     *
     * @param transactionId 取引ID
     */
    void openOrderSlip(TransactionId transactionId);

    /**
     * 改訂用注文票発行
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    void publishTransactionForRevision(TransactionId transactionId, TransactionRevisionId transactionRevisionId);

    /**
     * 改訂用注文票チェック
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param transactionId         改訂元取引ID
     */
    void checkOrderSlipForRevision(TransactionRevisionId transactionRevisionId, TransactionId transactionId);

    /**
     * 改訂用配送伝票取消
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    void cancelOrderSlipForRevision(TransactionRevisionId transactionRevisionId);

    /**
     * 改訂用注文票更新
     *
     * @param orderSlipForRevisionParam
     */
    void updateOrderSlipForRevision(UpdateOrderSlipForRevisionParam orderSlipForRevisionParam);

    /**
     * 改訂用注文票に注文商品を追加
     *
     * @param transactionRevisionId
     * @param itemId
     * @param itemIdCount
     */
    void addOrderItemToOrderSlipForRevision(TransactionRevisionId transactionRevisionId,
                                            String itemId,
                                            Integer itemIdCount);

    /**
     * 改訂用注文票から注文商品を削除
     *
     * @param transactionRevisionId
     * @param itemSeq
     */
    void deleteOrderItemToOrderSlipForRevision(TransactionRevisionId transactionRevisionId, Integer itemSeq);

    /**
     * 改訂用注文票を確定
     *
     * @param transactionRevisionId
     */
    void openOrderSlipForRevision(TransactionRevisionId transactionRevisionId);

    /**
     * 注文票取得
     *
     * @param transactionId 取引ID
     * @return 注文票
     */
    OrderSlip getOrderSlipByTransactionId(TransactionId transactionId);

    /**
     * 改訂用注文票取得
     *
     * @param transactionRevisionId 取引ID
     * @return OrderSlipForRevision 改訂用注文票
     */
    OrderSlipForRevision getOrderSlipForRevision(String transactionRevisionId);

}