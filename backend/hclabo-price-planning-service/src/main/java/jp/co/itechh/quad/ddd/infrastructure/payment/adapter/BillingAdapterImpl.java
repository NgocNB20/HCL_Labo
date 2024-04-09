/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.GetBillingSlipForRevisionByTransactionRevisionIdRequest;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlipForRevision;
import jp.co.itechh.quad.ddd.exception.AssertException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * 請求アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class BillingAdapterImpl implements IBillingAdapter {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(BillingAdapterImpl.class);

    /** 請求する */
    private final BillingSlipApi billingSlipApi;

    /** 請求アダプタヘルパー */
    private final BillingAdapterHelper billingAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param billingAdapterHelper
     * @param billingSlipApi
     * @param headerParamsUtil     ヘッダパラメーターユーティル
     */
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

    /**
     * 改訂用請求伝票取得
     * ※注文決済情報含む
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return BillingSlipForRevision
     */
    @Override
    public BillingSlipForRevision getBillingSlipForRevision(String transactionRevisionId) {

        GetBillingSlipForRevisionByTransactionRevisionIdRequest request =
                        new GetBillingSlipForRevisionByTransactionRevisionIdRequest();
        request.setTransactionRevisionId(transactionRevisionId);

        BillingSlipForRevisionByTransactionRevisionIdResponse response =
                        billingSlipApi.getBillingSlipForRevisionByTransactionRevisionId(request);

        // 戻り値
        BillingSlipForRevision billingSlipForRevision = new BillingSlipForRevision();
        try {
            BeanUtils.copyProperties(billingSlipForRevision, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new AssertException(e.getMessage());
        }
        return billingSlipForRevision;
    }

}