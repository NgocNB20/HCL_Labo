/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.customeraddress;

import jp.co.itechh.quad.core.entity.memberinfo.customeraddress.CustomerAddressBookEntity;

/**
 * 顧客住所登録ロジック<br/>
 * TODO 管理画面で会員情報に紐づけて住所情報を取得するための登録ロジック。将来的には不要
 *
 * @author kimura
 */
public interface CustomerAddressBookRegistLogic {

    /**
     * 顧客住所登録<br/>
     *
     * @param entity 顧客住所エンティティ
     * @return 登録件数
     */
    int execute(CustomerAddressBookEntity entity);
}
