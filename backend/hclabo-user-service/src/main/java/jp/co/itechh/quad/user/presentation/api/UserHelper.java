/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.user.presentation.api;

import jp.co.itechh.quad.user.presentation.api.param.AccessUidResponse;
import jp.co.itechh.quad.user.presentation.api.param.CustomerIdResponse;
import org.springframework.stereotype.Component;

/**
 * ユーザー Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class UserHelper {

    /**
     * 端末識別情報レスポンスに変換
     *
     * @param accessUid 端末識別情報
     * @return 端末識別情報レスポンス
     */
    public AccessUidResponse toAccessUidResponse(StringBuilder accessUid) {
        AccessUidResponse accessUidResponse = new AccessUidResponse();

        accessUidResponse.setAccessUid(accessUid.toString());

        return accessUidResponse;
    }

    /**
     * 顧客IDレスポンスに変換
     *
     * @param custmerId 顧客ID
     * @return 顧客IDレスポンス
     */
    public CustomerIdResponse toCustomerIdResponse(String customerId) {
        CustomerIdResponse customerIdResponse = new CustomerIdResponse();

        customerIdResponse.setCustomerId(customerId);

        return customerIdResponse;
    }

}
