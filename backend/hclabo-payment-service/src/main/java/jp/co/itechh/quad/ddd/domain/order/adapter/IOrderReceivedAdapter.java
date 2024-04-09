/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.adapter;

/**
 * 受注アダプター
 */
public interface IOrderReceivedAdapter {

    /**
     * 受注番号より最新日付の取引IDを取得
     *
     * @param orderCode 受注番号
     * @return 取引ID
     */
    String getTransactionIdByOrderCodeLatest(String orderCode);
}
