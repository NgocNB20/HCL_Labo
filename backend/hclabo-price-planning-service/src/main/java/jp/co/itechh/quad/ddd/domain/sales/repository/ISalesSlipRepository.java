/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.repository;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesSlipId;

import java.sql.Timestamp;

/**
 * 販売伝票集約リポジトリ
 */
public interface ISalesSlipRepository {

    /**
     * 販売伝票登録
     *
     * @param salesSlipEntity 販売伝票
     */
    void save(SalesSlipEntity salesSlipEntity);

    /**
     * 販売伝票更新
     *
     * @param salesSlipEntity 販売伝票
     * @return 更新件数
     */
    int update(SalesSlipEntity salesSlipEntity);

    /**
     * 販売伝票取得
     *
     * @param salesSlipId 販売伝票ID
     * @return SalesSlipEntity
     */
    SalesSlipEntity get(SalesSlipId salesSlipId);

    /**
     * 取引IDで販売伝票取得
     *
     * @param transactionId 取引ID
     * @return SalesSlipEntity
     */
    SalesSlipEntity getByTransactionId(String transactionId);

    /**
     * 特定顧客のクーポン利用済回数取得
     *
     * @param customerId 顧客ID
     * @param couponSeq クーポンSEQ
     * @param couponStartTime クーポン利用開始日時
     * @return クーポン利用済回数
     */
    Integer getCouponCountByCustomerId(String customerId, Integer couponSeq, Timestamp couponStartTime);

    /**
     * 不要データを削除する
     *
     * @param transactionId 取引ID
     * @return 更新件数
     */
    int deleteUnnecessaryByTransactionId(String transactionId);

}
