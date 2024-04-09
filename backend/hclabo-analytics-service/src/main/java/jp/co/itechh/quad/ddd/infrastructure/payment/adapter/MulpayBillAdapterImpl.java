/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IMulpayBillAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.MulpayBill;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * マルチペイメントー実装クラス
 */
@Component
public class MulpayBillAdapterImpl implements IMulpayBillAdapter {

    /**
     * マルチペイメントAPI
     */
    private final MulpayApi mulpayApi;

    /**
     * マルチペイメントアダプターHelperクラス
     */
    private final MulpayBillAdapterHelper mulpayBillAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param mulpayApi               マルチペイメントAPI
     * @param mulpayBillAdapterHelper マルチペイメントアダプターHelperクラス
     * @param headerParamsUtil        ヘッダパラメーターユーティル
     */
    @Autowired
    public MulpayBillAdapterImpl(MulpayApi mulpayApi,
                                 MulpayBillAdapterHelper mulpayBillAdapterHelper,
                                 HeaderParamsUtility headerParamsUtil) {
        this.mulpayApi = mulpayApi;
        this.mulpayBillAdapterHelper = mulpayBillAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.mulpayApi.getApiClient());
    }

    /**
     * マルチペイメント請求取得
     *
     * @param orderCode 受注番号
     * @return マルチペイメント
     */
    @Override
    public MulpayBill getByOrderCode(String orderCode) {
        MulPayBillRequest mulPayBillRequest = new MulPayBillRequest();
        mulPayBillRequest.setOrderCode(orderCode);

        MulPayBillResponse mulPayBillResponse = mulpayApi.getByOrderCode(mulPayBillRequest);

        return mulpayBillAdapterHelper.toMulpayBill(mulPayBillResponse);
    }
}