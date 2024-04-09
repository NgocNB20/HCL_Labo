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
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodOrderDisplayUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * 決済方法表示順更新ロジック
 *
 * @author YAMAGUCHI
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class SettlementMethodOrderDisplayUpdateLogicImpl extends AbstractShopLogic
                implements SettlementMethodOrderDisplayUpdateLogic {

    /**
     * 決済方法Dao
     */
    private final SettlementMethodDao settlementMethodDao;

    @Autowired
    public SettlementMethodOrderDisplayUpdateLogicImpl(SettlementMethodDao settlementMethodDao) {
        this.settlementMethodDao = settlementMethodDao;
    }

    /**
     * 実行メソッド
     *
     * @param settlementMethodList 決済方法エンティティリスト
     * @param shopSeq ショップSEQ
     * @return 処理件数
     */
    @Override
    public int execute(List<SettlementMethodEntity> settlementMethodList, Integer shopSeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotEmpty("settlementMethodList", settlementMethodList);

        // テーブルロック
        settlementMethodDao.updateLockTableShareModeNowait();

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        int count = 0;
        for (SettlementMethodEntity settlementMethod : settlementMethodList) {
            Integer settlementMethodSeq = settlementMethod.getSettlementMethodSeq();
            Integer orderDisplay = settlementMethod.getOrderDisplay();
            Timestamp updateTime = dateUtility.getCurrentTime();

            count += settlementMethodDao.updateOrderDisplay(settlementMethodSeq, shopSeq, orderDisplay, updateTime);
        }

        return count;
    }

}