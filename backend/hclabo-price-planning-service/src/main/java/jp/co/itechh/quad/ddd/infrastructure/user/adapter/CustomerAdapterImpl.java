/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.ddd.domain.user.adapter.ICustomerAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * 会員 アダプター実装クラス
 */
@Component
public class CustomerAdapterImpl implements ICustomerAdapter {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAdapterImpl.class);

    /** 会員API */
    private final CustomerApi customerApi;

    public CustomerAdapterImpl(CustomerApi customerApi, HeaderParamsUtility headerParamsUtil) {
        this.customerApi = customerApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.customerApi.getApiClient());
    }

    /**
     * 会員情報取得情報取得
     *
     * @param memberInfoSeq
     * @return Customer
     */
    @Override
    public Customer getByMemberInfoSeq(Integer memberInfoSeq) {

        Customer customer = null;

        CustomerResponse customerResponse = customerApi.getByMemberinfoSeq(memberInfoSeq);
        if (customerResponse != null) {
            customer = new Customer();
            try {
                BeanUtils.copyProperties(customer, customerResponse);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("例外処理が発生しました", e);
                throw new RuntimeException(e);
            }
        }

        return customer;
    }
}