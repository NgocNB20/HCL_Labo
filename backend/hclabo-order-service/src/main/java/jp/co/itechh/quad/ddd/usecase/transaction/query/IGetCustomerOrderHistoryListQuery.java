/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction.query;

import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 * 顧客注文履歴一覧取得 クエリ
 */
public interface IGetCustomerOrderHistoryListQuery {

    /**
     * 顧客注文履歴一覧取得
     *
     * @param customerId
     * @return CustomerOrderHistoryQueryModelリスト
     */
    List<CustomerOrderHistoryQueryModel> getCustomerOrderHistoryList(String customerId, SelectOptions selectOptions);

}
