/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.customeraddress.impl;

import jp.co.itechh.quad.core.dao.memberinfo.customeraddress.CustomerAddressDao;
import jp.co.itechh.quad.core.entity.memberinfo.customeraddress.CustomerAddressBookEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.customeraddress.CustomerAddressBookRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 顧客住所登録ロジック実装クラス<br/>
 *
 * @author kimura
 */
@Component
public class CustomerAddressBookRegistLogicImpl extends AbstractShopLogic implements CustomerAddressBookRegistLogic {

    /** CustomerAddressDao */
    private final CustomerAddressDao customerAddressDao;

    /** コンストラクタ */
    @Autowired
    public CustomerAddressBookRegistLogicImpl(CustomerAddressDao customerAddressDao) {
        this.customerAddressDao = customerAddressDao;
    }

    /**
     * 顧客住所登録<br/>
     *
     * @param entity 顧客住所エンティティ
     * @return 登録件数
     */
    @Override
    public int execute(CustomerAddressBookEntity entity) {

        int result = 0;

        if (this.customerAddressDao.getEntity(entity.getCustomerId()) != null) {
            result = this.customerAddressDao.update(entity);
        } else {
            result = this.customerAddressDao.insert(entity);
        }

        return result;
    }

}
