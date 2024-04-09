/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.repository;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderReceivedId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;

/**
 * 受注リポジトリ
 */
public interface IOrderReceivedRepository {

    /**
     * 受注番号連用番取得
     *
     * @return 受注番号用連番
     */
    Integer getOrderCodeSeq();

    /**
     * 受注取得
     *
     * @param orderReceivedId
     * @return OrderReceivedEntity
     */
    OrderReceivedEntity get(OrderReceivedId orderReceivedId);

    /**
     * 受注取得
     *
     * @param latestTransactionId
     * @return OrderReceivedEntity
     */
    OrderReceivedEntity getByLatestTransactionId(TransactionId latestTransactionId);

    /**
     * 受注番号に紐づく受注取得
     *
     * @param orderCode
     * @return OrderReceivedEntity
     */
    OrderReceivedEntity getByOrderCode(OrderCode orderCode);

    /**
     * 受注登録
     *
     * @param orderReceivedEntity
     */
    void save(OrderReceivedEntity orderReceivedEntity);

    /**
     * 受注更新
     * ※originTransactionIdを指定した場合は、latestTransactionIdに一致したものを更新
     * ※対象がなければRuntimeException
     *
     * @param orderReceivedEntity
     * @param originTransactionId
     * @return 更新件数
     */
    int updateWithTranCheck(OrderReceivedEntity orderReceivedEntity, String originTransactionId);

    /**
     * 顧客ごとの受注件数取得
     *
     * @param customerId 顧客ID
     * @return 顧客ごとの受注件数
     */
    int getOrderReceivedCountByCustomerId(String customerId);

}
