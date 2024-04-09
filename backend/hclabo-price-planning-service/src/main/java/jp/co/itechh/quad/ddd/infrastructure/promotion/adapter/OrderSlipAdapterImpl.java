/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.promotion.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipForRevision;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.GetOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注文票アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class OrderSlipAdapterImpl implements IOrderSlipAdapter {

    /** 注文票API */
    private final OrderSlipApi orderSlipApi;

    /** 注文アダプタークラスHelperクラス */
    private final OrderSlipAdapterHelper orderAdapterHelper;

    /**
     * コンストラクタ.
     *
     * @param orderSlipApi       注文票API
     * @param orderAdapterHelper 注文アダプタークラスHelperクラス
     * @param headerParamsUtil   ヘッダパラメーターユーティル
     */
    @Autowired
    public OrderSlipAdapterImpl(OrderSlipApi orderSlipApi,
                                OrderSlipAdapterHelper orderAdapterHelper,
                                HeaderParamsUtility headerParamsUtil) {
        this.orderSlipApi = orderSlipApi;
        this.orderAdapterHelper = orderAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.orderSlipApi.getApiClient());
    }

    /**
     * プロモーションサービス：トランサクションIDで下書き注文票を取得する
     *
     * @param transactionId
     * @return OrderSlip
     */
    @Override
    public OrderSlip getDraftOrderSlipByTransactionId(String transactionId) {
        OrderSlipGetRequest orderSlipGetRequest = new OrderSlipGetRequest();
        orderSlipGetRequest.setTransactionId(transactionId);
        OrderSlipResponse orderSlipResponse = orderSlipApi.getDraft(orderSlipGetRequest);
        return orderAdapterHelper.toDraftOrderSlip(orderSlipResponse);
    }

    /**
     * プロモーションサービス：注文票IDで下書き注文票を取得する
     *
     * @param orderSlipId
     * @return OrderSlip
     */
    @Override
    public OrderSlip getDraftOrderSlipById(String orderSlipId) {
        OrderSlipGetRequest orderSlipGetRequest = new OrderSlipGetRequest();
        orderSlipGetRequest.setOrderSlipId(orderSlipId);
        OrderSlipResponse orderSlipResponse = orderSlipApi.getDraft(orderSlipGetRequest);
        return orderAdapterHelper.toDraftOrderSlip(orderSlipResponse);
    }

    /**
     * プロモーションサービス：トランサクションIDで注文票を取得する
     *
     * @param transactionId
     * @return OrderSlip
     */
    @Override
    public OrderSlip getOrderSlipByTransactionId(String transactionId) {

        OrderSlipGetRequest orderSlipGetRequest = new OrderSlipGetRequest();
        orderSlipGetRequest.setTransactionId(transactionId);

        OrderSlipResponse orderSlipResponse = orderSlipApi.getDraft(orderSlipGetRequest);
        return orderAdapterHelper.toOrderSlip(orderSlipResponse);
    }

    /**
     * 改訂用トランサクションIDで改訂用注文票を取得する
     *
     * @param transactionRevisionId
     * @return OrderSlip
     */
    @Override
    public OrderSlipForRevision getOrderSlipByTransactionRevisionId(String transactionRevisionId) {

        GetOrderSlipForRevisionRequest getOrderSlipForRevisionRequest = new GetOrderSlipForRevisionRequest();
        getOrderSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId);
        OrderSlipForRevisionResponse orderSlipForRevisionResponse =
                        orderSlipApi.getOrderSlipForRevision(getOrderSlipForRevisionRequest);

        return orderAdapterHelper.toOrderSlipForRevision(orderSlipForRevisionResponse);
    }

}