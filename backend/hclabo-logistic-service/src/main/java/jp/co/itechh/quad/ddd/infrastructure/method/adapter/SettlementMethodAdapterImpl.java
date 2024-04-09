/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.method.adapter;

import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.method.adapter.ISettlementMethodAdapter;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 決済方法 アダプター
 *
 * @author PHAM QUANG DIEU
 */
@Component
public class SettlementMethodAdapterImpl implements ISettlementMethodAdapter {

    private final SettlementMethodApi settlementMethodApi;

    private final SettlementMethodHelper settlementMethodHelper;

    @Autowired
    public SettlementMethodAdapterImpl(SettlementMethodApi settlementMethodApi,
                                       SettlementMethodHelper settlementMethodHelper,
                                       HeaderParamsUtility headerParamsUtil) {
        this.settlementMethodApi = settlementMethodApi;
        this.settlementMethodHelper = settlementMethodHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.settlementMethodApi.getApiClient());
    }

    @Override
    public List<SettlementMethodEntity> getSettlementMethod() {
        PaymentMethodListResponse paymentMethodListResponse = settlementMethodApi.get();
        return settlementMethodHelper.toSettlementMethodEntityList(paymentMethodListResponse);
    }
}