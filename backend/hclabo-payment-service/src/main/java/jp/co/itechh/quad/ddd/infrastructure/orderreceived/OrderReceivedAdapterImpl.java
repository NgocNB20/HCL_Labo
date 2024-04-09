/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.orderreceived;

import jp.co.itechh.quad.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.TransactionIdResponse;
import org.springframework.stereotype.Component;

/**
 * 受注アダプタ実装クラス
 */
@Component
public class OrderReceivedAdapterImpl implements IOrderReceivedAdapter {

    /** 受注API */
    private final OrderReceivedApi orderReceivedApi;

    /** コンストラクタ */
    public OrderReceivedAdapterImpl(OrderReceivedApi orderReceivedApi) {
        this.orderReceivedApi = orderReceivedApi;
    }

    /**
     * 受注番号より最新日付の取引IDを取得
     *
     * @param orderCode 受注番号
     * @return 取引ID
     */
    @Override
    public String getTransactionIdByOrderCodeLatest(String orderCode) {

        TransactionIdResponse transactionIdResponse = orderReceivedApi.getTransactionIdByOrderCodeLatest(orderCode);
        return transactionIdResponse.getTransactionId();
    }
}
