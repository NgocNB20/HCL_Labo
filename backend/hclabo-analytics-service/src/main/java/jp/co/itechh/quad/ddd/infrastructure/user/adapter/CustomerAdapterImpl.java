/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.ddd.domain.user.adapter.ICustomerAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 顧客アダプター実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class CustomerAdapterImpl implements ICustomerAdapter {

    /**
     * 会員API
     **/
    private final CustomerApi customerApi;

    /**
     * 顧客アダプタHelperクラス
     **/
    private final CustomerAdapterHelper customerAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param customerApi           会員API
     * @param customerAdapterHelper 顧客アダプタHelperクラス
     * @param headerParamsUtil      ヘッダパラメーターユーティル
     */
    @Autowired
    public CustomerAdapterImpl(CustomerApi customerApi,
                               CustomerAdapterHelper customerAdapterHelper,
                               HeaderParamsUtility headerParamsUtil) {
        this.customerApi = customerApi;
        this.customerAdapterHelper = customerAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.customerApi.getApiClient());
    }

    /**
     * ユーザーマイクロサービス
     * 顧客取得
     *
     * @param memberinfoSeq 会員SEQ
     * @return 顧客
     */
    @Override
    public Customer getCustomer(Integer memberinfoSeq) {

        CustomerResponse response = customerApi.getByMemberinfoSeq(memberinfoSeq);

        return customerAdapterHelper.toCustomer(response);
    }

}