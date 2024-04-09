/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.order.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 受注情報アダプター実装クラス
 */
@Component
public class OrderReceivedAdapterImpl implements IOrderReceivedAdapter {

    /** 受注API */
    private final OrderReceivedApi orderReceivedApi;

    /** helper */
    private final OrderReceivedAdapterHelper orderReceivedAdapterHelper;

    /**
     * コンストラクタ
     */
    @Autowired
    public OrderReceivedAdapterImpl(OrderReceivedApi orderReceivedApi,
                                    OrderReceivedAdapterHelper orderReceivedAdapterHelper,
                                    HeaderParamsUtility headerParamsUtil) {
        this.orderReceivedApi = orderReceivedApi;
        this.orderReceivedAdapterHelper = orderReceivedAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.orderReceivedApi.getApiClient());
    }

    /**
     * 受注情報取得
     *
     * @param transactionId
     * @return OrderReceived
     */
    @Override
    public OrderReceived get(String transactionId) {

        GetOrderReceivedRequest getOrderReceivedRequest = new GetOrderReceivedRequest();
        getOrderReceivedRequest.setTransactionId(transactionId);

        OrderReceivedResponse orderReceivedResponse = orderReceivedApi.get(getOrderReceivedRequest);

        return orderReceivedAdapterHelper.toOrderReceived(orderReceivedResponse);
    }
}