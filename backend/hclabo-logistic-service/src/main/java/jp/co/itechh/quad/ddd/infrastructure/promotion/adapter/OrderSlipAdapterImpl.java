/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.promotion.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.GetOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注文アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class OrderSlipAdapterImpl implements IOrderSlipAdapter {

    /**
     * 注文票API
     */
    private final OrderSlipApi orderSlipApi;

    /**
     * 注文アダプターHelperクラス
     */
    private final OrderSlipAdapterHelper orderSlipAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param orderSlipApi       注文票API
     * @param orderSlipAdapterHelper 注文アダプターHelperクラス
     * @param headerParamsUtil   ヘッダパラメーターユーティル
     */
    @Autowired
    public OrderSlipAdapterImpl(OrderSlipApi orderSlipApi,
                                OrderSlipAdapterHelper orderSlipAdapterHelper,
                                HeaderParamsUtility headerParamsUtil) {
        this.orderSlipApi = orderSlipApi;
        this.orderSlipAdapterHelper = orderSlipAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.orderSlipApi.getApiClient());
    }

    /**
     * プロモーションマイクロサービス
     * 下書き注文票取得
     *
     * @param transactionId 取引ID
     * @return 注文票
     */
    @Override
    public OrderSlip getDraftOrderSlip(String transactionId) {

        OrderSlipGetRequest orderSlipSlipGetRequest = new OrderSlipGetRequest();
        orderSlipSlipGetRequest.setTransactionId(transactionId);

        OrderSlipResponse orderSlipResponse = orderSlipApi.getDraft(orderSlipSlipGetRequest);

        OrderSlip orderSlip = orderSlipAdapterHelper.toOrderSlipFromOrderSlipResponse(orderSlipResponse);

        return orderSlip;

    }

    /**
     * 改訂用注文票取得
     *
     * @param transactionRevisionId 取引ID
     * @return OrderSlip 改訂用注文票
     */
    @Override
    public OrderSlip getOrderSlipForRevision(String transactionRevisionId) {

        GetOrderSlipForRevisionRequest getOrderSlipForRevisionRequest = new GetOrderSlipForRevisionRequest();

        getOrderSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId);

        OrderSlipForRevisionResponse orderSlipForRevision =
                        orderSlipApi.getOrderSlipForRevision(getOrderSlipForRevisionRequest);

        return orderSlipAdapterHelper.toOrderSlipFromOrderSlipForRevisionResponse(orderSlipForRevision);
    }

    /**
     * 注文票取得
     *
     * @param transactionId 取引ID
     * @return OrderSlip 注文票
     */
    @Override
    public OrderSlip getOrderSlip(String transactionId) {

        OrderSlipGetRequest request = new OrderSlipGetRequest();

        request.setTransactionId(transactionId);

        OrderSlipResponse response = orderSlipApi.get(request);

        return orderSlipAdapterHelper.toOrderSlipFromOrderSlipResponse(response);
    }

}