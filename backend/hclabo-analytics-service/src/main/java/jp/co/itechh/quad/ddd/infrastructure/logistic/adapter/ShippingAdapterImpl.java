/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送アダプター実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingAdapterImpl implements IShippingAdapter {

    /**
     * 出荷スリップAPI
     */
    private final ShippingSlipApi shippingSlipApi;

    /**
     * 配送アダプターヘルパー
     */
    private final ShippingAdapterHelper shippingAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param shippingSlipApi       出荷スリップAPI
     * @param shippingAdapterHelper 配送アダプターヘルパー
     * @param headerParamsUtil      ヘッダパラメーターユーティル
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
     * 配送伝票取得
     *
     * @param transactionId 取引ID
     * @return 配送伝票
     */
    @Override
    public ShippingSlip getShippingSlip(String transactionId) {

        ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();
        shippingSlipGetRequest.setTransactionId(transactionId);

        ShippingSlipResponse shippingSlipResponse = shippingSlipApi.get(shippingSlipGetRequest);

        return shippingAdapterHelper.toShippingSlip(shippingSlipResponse);
    }

}