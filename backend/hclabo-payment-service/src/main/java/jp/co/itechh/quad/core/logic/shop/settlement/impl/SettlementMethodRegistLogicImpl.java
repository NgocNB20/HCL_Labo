/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.shop.settlement.SettlementMethodDao;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodRegistLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 決済方法登録
 *
 * @author YAMAGUCHI
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class SettlementMethodRegistLogicImpl extends AbstractShopLogic implements SettlementMethodRegistLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementMethodRegistLogicImpl.class);

    /** SettlementMethodDao */
    private final SettlementMethodDao settlementMethodDao;

    @Autowired
    public SettlementMethodRegistLogicImpl(SettlementMethodDao settlementMethodDao) {
        this.settlementMethodDao = settlementMethodDao;
    }

    /**
     * 実行メソッド
     *
     * @param settlementMethodEntity 決済方法エンティティ
     * @return 処理件数
     */
    @Override
    public int execute(SettlementMethodEntity settlementMethodEntity) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("settlementMethodEntity", settlementMethodEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        Timestamp currentTime = dateUtility.getCurrentTime();

        // 決済方法SEQ採番
        settlementMethodEntity.setRegistTime(currentTime);
        settlementMethodEntity.setUpdateTime(currentTime);

        int count = 0;
        try {
            count = settlementMethodDao.insert(settlementMethodEntity);
        } catch (DataAccessException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage(MSGCD_SETTLEMENTMETHOD_INSERT_FAIL);
        }

        return count;
    }

}
