/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter;

import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;

/**
 * 顧客アダプター
 */
public interface ICustomerAdapter {

    /**
     * ユーザーマイクロサービス
     * 会員詳細情報取得
     *
     * @param memberinfoSeq 会員SEQ
     * @return 会員詳細
     */
    Customer getCustomer(Integer memberinfoSeq);

}
