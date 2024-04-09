/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.priceplanning.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.IPriceCalculatorAdapter;
import jp.co.itechh.quad.pricecalculator.presentation.api.PriceCalculatorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 金額計算アダプター 実装クラス
 */
@Component
public class PriceCalculatorAdapterImpl implements IPriceCalculatorAdapter {

    /** 販売伝票API */
    private final PriceCalculatorApi calculatorApi;

    /**
     * コンストラクタ
     */
    @Autowired
    public PriceCalculatorAdapterImpl(PriceCalculatorApi calculatorApi, HeaderParamsUtility headerParamsUtil) {

        this.calculatorApi = calculatorApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.calculatorApi.getApiClient());
    }

}
