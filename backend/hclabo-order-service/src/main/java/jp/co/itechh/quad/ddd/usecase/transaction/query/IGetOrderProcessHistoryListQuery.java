/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction.query;

import java.util.List;

/**
 * 受注処理履歴一覧取得クエリ
 */
public interface IGetOrderProcessHistoryListQuery {

    /**
     * 受注処理履歴一覧取得
     *
     * @param orderCode
     * @return OrderProcessHistoryQueryModelリスト
     */
    List<OrderProcessHistoryQueryModel> getOrderProcessHistoryList(String orderCode);

}