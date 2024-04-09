/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.usecase.transaction.query.IGetOrderProcessHistoryListQuery;
import jp.co.itechh.quad.ddd.usecase.transaction.query.OrderProcessHistoryQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 受注処理履歴一覧取得 ユースケース
 */
@Service
public class GetOrderProcessHistoryListUseCase {

    /** 受注処理履歴一覧取得クエリ */
    private final IGetOrderProcessHistoryListQuery orderProcessHistoryListQuery;

    /** コンストラクタ */
    @Autowired
    public GetOrderProcessHistoryListUseCase(IGetOrderProcessHistoryListQuery orderProcessHistoryListQuery) {
        this.orderProcessHistoryListQuery = orderProcessHistoryListQuery;
    }

    /**
     * 受注処理履歴一覧取得
     *
     * @param orderCode 受注番号
     * @return OrderProcessHistoryQueryModelリスト
     */
    public List<OrderProcessHistoryQueryModel> getOrderProcessHistoryList(String orderCode) {

        // 受注処理履歴一覧取得
        return orderProcessHistoryListQuery.getOrderProcessHistoryList(orderCode);
    }

}
