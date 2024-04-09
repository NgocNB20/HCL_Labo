/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingMethodAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingMethod;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送方法アダプター実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingMethodAdapterImpl implements IShippingMethodAdapter {

    /**
     * 配送方法API
     */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * 配送方法アダプターHelperクラス
     */
    private final ShippingMethodAdapterHelper shippingMethodAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param shippingMethodApi           配送方法API
     * @param shippingMethodAdapterHelper 配送方法アダプターHelperクラス
     * @param headerParamsUtil            ヘッダパラメーターユーティル
     */
    @Autowired
    public ShippingMethodAdapterImpl(ShippingMethodApi shippingMethodApi,
                                     ShippingMethodAdapterHelper shippingMethodAdapterHelper,
                                     HeaderParamsUtility headerParamsUtil) {
        this.shippingMethodApi = shippingMethodApi;
        this.shippingMethodAdapterHelper = shippingMethodAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.shippingMethodApi.getApiClient());
    }

    /**
     * 配送方法取得
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 住所録
     */
    @Override
    public ShippingMethod getShippingMethod(Integer deliveryMethodSeq) {

        ShippingMethodResponse response = shippingMethodApi.getByDeliveryMethodSeq(deliveryMethodSeq);

        return shippingMethodAdapterHelper.toShippingMethod(response);
    }

}