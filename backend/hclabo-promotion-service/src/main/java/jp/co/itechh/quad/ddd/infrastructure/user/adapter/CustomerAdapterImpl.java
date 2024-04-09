/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.ddd.domain.user.adapter.ICustomerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 顧客アダプター実装クラス
 *
 * @author yt23807
 */
@Component
public class CustomerAdapterImpl implements ICustomerAdapter {

    /** 会員API **/
    private final CustomerApi customerApi;

    /** 顧客アダプタHelper */
    private final CustomerAdapterHelper customerAdapterHelper;

    /** コンストラクタ */
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
     * ユーザーマイクロサービス<br/>
     * 会員詳細情報取得
     *
     * @param memberinfoSeq 会員SEQ
     * @return 会員詳細
     */
    @Override
    public MemberInfoEntity getMemberInfoEntity(Integer memberinfoSeq) {

        CustomerResponse response = customerApi.getByMemberinfoSeq(memberinfoSeq);

        return customerAdapterHelper.toMemberInfoEntity(response);

    }

}