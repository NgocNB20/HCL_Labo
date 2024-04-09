/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.memberinfo.customeraddress;

import jp.co.itechh.quad.core.entity.memberinfo.customeraddress.CustomerAddressBookEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * 会員住所DAOクラス<br/>
 *
 * @author kimura
 */
@Dao
@ConfigAutowireable
public interface CustomerAddressDao {

    /**
     * インサート
     *
     * @param customerAddressBookEntity 顧客住所
     * @return 登録件数
     */
    @Insert(excludeNull = true)
    int insert(CustomerAddressBookEntity customerAddressBookEntity);

    /**
     * アップデート
     *
     * @param customerAddressBookEntity 顧客住所
     * @return 更新件数
     */
    @Update
    int update(CustomerAddressBookEntity customerAddressBookEntity);

    /**
     * エンティティ取得
     *
     * @param customerId 顧客ID
     * @return 会員エンティティ
     */
    @Select
    CustomerAddressBookEntity getEntity(String customerId);

}
