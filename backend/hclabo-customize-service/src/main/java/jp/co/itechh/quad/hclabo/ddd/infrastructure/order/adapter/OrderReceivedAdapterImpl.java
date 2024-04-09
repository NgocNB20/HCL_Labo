/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.infrastructure.order.adapter;

import jp.co.itechh.quad.hclabo.core.web.HeaderParamsUtil;
import jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
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

    /** 受注情報アダプターHelperクラス */
    private final OrderReceivedAdapterHelper orderReceivedAdapterHelper;

    /**
     * コンストラクタ
     */
    @Autowired
    public OrderReceivedAdapterImpl(OrderReceivedApi orderReceivedApi,
                                    OrderReceivedAdapterHelper orderReceivedAdapterHelper,
                                    HeaderParamsUtil headerParamsUtil) {
        this.orderReceivedApi = orderReceivedApi;
        this.orderReceivedAdapterHelper = orderReceivedAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.orderReceivedApi.getApiClient());
    }

    /**
     * 受注情報取得
     *
     * @param orderCode
     * @return OrderReceived
     */
    @Override
    public OrderReceived getByOrderCode(String orderCode) {

        OrderReceivedResponse orderReceivedResponse = orderReceivedApi.getByOrderCode(orderCode);

        return orderReceivedAdapterHelper.toOrderReceived(orderReceivedResponse);
    }
}