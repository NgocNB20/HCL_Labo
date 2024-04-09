/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.repository;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderSlipId;

import java.util.List;

/**
 * 注文票リポジトリ
 */
public interface IOrderSlipRepository {

    /**
     * 注文票登録
     *
     * @param orderSlipEntity 注文票エンティティ
     */
    void save(OrderSlipEntity orderSlipEntity);

    /**
     * 注文票更新
     *
     * @param orderSlipEntity 注文票エンティティ
     * @return 更新件数
     */
    int update(OrderSlipEntity orderSlipEntity);

    /**
     * 注文票更新【確定用】
     *
     * @param orderSlipEntity 注文票エンティティ
     * @param transactionId 取引ID
     * @return 更新件数
     */
    int updateForOpen(OrderSlipEntity orderSlipEntity, String transactionId);

    /**
     * 注文票取得
     *
     * @param orderSlipId 注文票ID
     * @return OrderSlipEntity 注文票エンティティ
     */
    OrderSlipEntity get(OrderSlipId orderSlipId);

    /**
     * 注文票削除
     *
     * @param orderSlipId 注文票ID
     * @return 更新件数
     */
    int delete(OrderSlipId orderSlipId);

    /**
     * 注文票IDで下書き注文票取得
     *
     * @param orderSlipId 注文票ID
     * @return OrderSlipEntity 下書き注文票エンティティ
     */
    OrderSlipEntity getDraftOrderSlipByOrderSlipId(OrderSlipId orderSlipId);

    /**
     * 取引IDで下書き注文票取得
     *
     * @param transactionId 取引ID
     * @return OrderSlipEntity 下書き注文票エンティティ
     */
    OrderSlipEntity getDraftOrderSlipByTransactionId(String transactionId);

    /**
     * 取引IDで注文票取得
     *
     * @param transactionId 取引ID
     * @return OrderSlipEntity 注文票エンティティ
     */
    OrderSlipEntity getOrderSlipByTransactionId(String transactionId);

    /**
     * 顧客IDで下書き注文票取得
     *
     * @param customerId 顧客ID
     * @return OrderSlipEntity 下書き注文票エンティティ
     */
    OrderSlipEntity getDraftOrderSlipByCustomerId(String customerId);

    /**
     * 注文商品のデータを削除（下書き状態のみ削除）
     * 注文票のデータを削除（下書き状態のみ削除）
     *
     * @param customerId 顧客ID
     * @return 注文票の削除件数を返却する
     */
    int deleteDraftOrderSlipByCustomerId(String customerId);

    /**
     * 削除対象の下書き注文票リスト取得
     *
     * @return OrderSlipEntityList 下書き注文票エンティティリスト
     */
    List<OrderSlipEntity> getDraftOrderSlipListForDelete();

    /**
     * 不要データを削除する
     *
     * @param transactionId 取引ID
     * @return 更新件数
     */
    int deleteUnnecessaryByTransactionId(String transactionId);
}
