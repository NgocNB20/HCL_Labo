/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.transaction.query;

import jp.co.itechh.quad.ddd.infrastructure.transaction.dao.OrderReceivedDao;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.OrderProcessHistoryDto;
import jp.co.itechh.quad.ddd.usecase.transaction.query.IGetOrderProcessHistoryListQuery;
import jp.co.itechh.quad.ddd.usecase.transaction.query.OrderProcessHistoryQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 受注処理履歴一覧取得クエリ 実装クラス
 */
@Component
public class GetOrderProcessHistoryListQueryImpl implements IGetOrderProcessHistoryListQuery {

    /** 受注Daoクラス */
    private final OrderReceivedDao orderReceivedDao;

    /** ヘルパー */
    private final GetOrderProcessHistoryListQueryHelper helper;

    /** コンストラクタ */
    @Autowired
    public GetOrderProcessHistoryListQueryImpl(OrderReceivedDao orderReceivedDao,
                                               GetOrderProcessHistoryListQueryHelper helper) {
        this.orderReceivedDao = orderReceivedDao;
        this.helper = helper;
    }

    /**
     * 受注処理履歴一覧取得
     *
     * @param orderCode
     * @return OrderProcessHistoryQueryModelリスト
     */
    @Override
    public List<OrderProcessHistoryQueryModel> getOrderProcessHistoryList(String orderCode) {

        List<OrderProcessHistoryDto> orderProcessHistoryDtoList =
                        orderReceivedDao.getOrderProcessHistoryList(orderCode);

        return helper.toOrderProcessHistoryQueryModelList(orderProcessHistoryDtoList);
    }
}