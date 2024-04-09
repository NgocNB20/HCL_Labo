/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.infrastructure.analytics.adapter;

import jp.co.itechh.quad.hclabo.core.web.HeaderParamsUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.analytics.adapter.IOrderSearchAdapter;
import jp.co.itechh.quad.ordersearch.presentation.api.OrderSearchApi;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchRegistUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 受注検索アダプター.
 */
@Component
public class OrderSearchAdapterImpl implements IOrderSearchAdapter {

    /** 受注検索API */
    private final OrderSearchApi orderSearchApi;

    /** コンストラクタ */
    @Autowired
    public OrderSearchAdapterImpl(OrderSearchApi orderSearchApi, HeaderParamsUtil headerParamsUtil) {
        this.orderSearchApi = orderSearchApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.orderSearchApi.getApiClient());
    }

    /**
     * 分析マイクロサービス<br/>
     * 受注検索情報を登録・更新する
     *
     * @param orderReceivedId 受注ID
     */
    @Override
    public void registUpdateOrderSearch(String orderReceivedId) {

        OrderSearchRegistUpdateRequest orderSearchRegistUpdateRequest = new OrderSearchRegistUpdateRequest();
        orderSearchRegistUpdateRequest.setOrderReceivedId(orderReceivedId);

        this.orderSearchApi.registUpdate(orderSearchRegistUpdateRequest);
    }


}