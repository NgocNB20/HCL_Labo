/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlipForRevision;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class ShippingAdapterImpl implements IShippingAdapter {

    /** 配送伝票API */
    private final ShippingSlipApi shippingSlipApi;

    /** 配送アダプター実装Helperクラス */
    private final ShippingAdapterHelper shippingAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param shippingSlipApi
     * @param shippingAdapterHelper
     * @param headerParamsUtil ヘッダパラメーターユーティル
     */
    @Autowired
    public ShippingAdapterImpl(ShippingSlipApi shippingSlipApi,
                               ShippingAdapterHelper shippingAdapterHelper,
                               HeaderParamsUtility headerParamsUtil) {
        this.shippingSlipApi = shippingSlipApi;
        this.shippingAdapterHelper = shippingAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.shippingSlipApi.getApiClient());
    }

    /**
     * 物流マイクロサービス<br/>
     * 配送伝票取得
     *
     * @param transactionId 取引ID
     * @return ShippingSlip 配送伝票
     */
    @Override
    public ShippingSlip getShippingSlip(String transactionId) {
        ShippingSlipGetRequest request = new ShippingSlipGetRequest();
        request.setTransactionId(transactionId);

        ShippingSlipResponse response = shippingSlipApi.get(request);

        return shippingAdapterHelper.toShippingSlip(response);
    }

    /**
     * 物流マイクロサービス<br/>
     * 配送伝票取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return ShippingSlipFor 改訂用配送伝票
     */
    @Override
    public ShippingSlipForRevision getShippingSlipForRevision(String transactionRevisionId) {

        ShippingSlipForRevisionGetRequest request = new ShippingSlipForRevisionGetRequest();
        request.setTransactionRevisionId(transactionRevisionId);

        ShippingSlipResponse response = shippingSlipApi.getForRevisionByTransactionRevisionId(request);

        return shippingAdapterHelper.toShippingSlipForRevision(response);
    }
}