/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlipForRevision;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送伝票アダプター実装クラス
 */
@Component
public class ShippingSlipAdapterImpl implements IShippingSlipAdapter {

    /** 出荷スリップAPI */
    private final ShippingSlipApi shippingSlipApi;

    /** 配送アダプターヘルパー */
    private final ShippingAdapterHelper shippingAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param shippingSlipApi
     * @param shippingAdapterHelper
     * @param headerParamsUtil      ヘッダパラメーターユーティル
     */
    @Autowired
    public ShippingSlipAdapterImpl(ShippingSlipApi shippingSlipApi,
                                   ShippingAdapterHelper shippingAdapterHelper,
                                   HeaderParamsUtility headerParamsUtil) {
        this.shippingSlipApi = shippingSlipApi;
        this.shippingAdapterHelper = shippingAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.shippingSlipApi.getApiClient());
    }

    /**
     * 配送伝票取得
     *
     * @param transactionId
     * @return ShippingSlip
     */
    @Override
    public ShippingSlip getShippingSlip(String transactionId) {

        ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();
        shippingSlipGetRequest.setTransactionId(transactionId);

        ShippingSlipResponse shippingSlipResponse = shippingSlipApi.get(shippingSlipGetRequest);
        return shippingAdapterHelper.toShippingSlip(shippingSlipResponse);
    }

    /**
     * 改訂用配送伝票取得
     *
     * @param transactionRevisionId
     * @return ShippingSlipForRevision
     */
    @Override
    public ShippingSlipForRevision getShippingSlipForRevision(String transactionRevisionId) {
        ShippingSlipForRevisionGetRequest shippingSlipGetByTransactionIdRequest =
                        new ShippingSlipForRevisionGetRequest();

        shippingSlipGetByTransactionIdRequest.setTransactionRevisionId(transactionRevisionId);
        ShippingSlipResponse shippingSlipResponse =
                        shippingSlipApi.getForRevisionByTransactionRevisionId(shippingSlipGetByTransactionIdRequest);

        return shippingAdapterHelper.toShippingSlipForRevision(shippingSlipResponse);
    }

}