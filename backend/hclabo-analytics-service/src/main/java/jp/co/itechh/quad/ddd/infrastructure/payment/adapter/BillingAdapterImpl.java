/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 請求アダプター実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class BillingAdapterImpl implements IBillingAdapter {

    /**
     * 請求伝票API
     */
    private final BillingSlipApi billingSlipApi;

    /**
     * 請求アダプタヘルパー
     */
    private final BillingAdapterHelper billingAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param billingAdapterHelper 請求アダプターHelperクラス
     * @param billingSlipApi       請求伝票API
     * @param headerParamsUtil     ヘッダパラメーターユーティル
     */
    @Autowired
    public BillingAdapterImpl(BillingSlipApi billingSlipApi,
                              BillingAdapterHelper billingAdapterHelper,
                              HeaderParamsUtility headerParamsUtil) {
        this.billingSlipApi = billingSlipApi;
        this.billingAdapterHelper = billingAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.billingSlipApi.getApiClient());
    }

    /**
     * 請求伝票取得
     * ※注文決済情報含む
     *
     * @param transactionId 取引ID
     * @return BillingSlip
     */
    @Override
    public BillingSlip getBillingSlip(String transactionId) {

        BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
        billingSlipGetRequest.setTransactionId(transactionId);

        BillingSlipResponse billingSlipResponse = billingSlipApi.get(billingSlipGetRequest);

        return billingAdapterHelper.toBillingSlip(billingSlipResponse);
    }

}