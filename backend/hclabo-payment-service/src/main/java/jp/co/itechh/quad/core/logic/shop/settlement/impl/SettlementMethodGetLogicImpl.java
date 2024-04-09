/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dao.shop.settlement.SettlementMethodDao;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 決済方法取得ロジック
 *
 * @author ueshima
 * @version $Revision: 1.2 $
 */
@Component
public class SettlementMethodGetLogicImpl extends AbstractShopLogic implements SettlementMethodGetLogic {

    /** 決済方法分類リスト用valueカラム名 */
    protected static final String VALUE_COLNAME = "settlementmethodseq";
    /** 決済方法分類リスト用ラベルカラム名 */
    protected static final String LABEL_COLNAME = "settlementmethodname";

    /**
     * 決済方法Dao
     */
    private final SettlementMethodDao settlementMethodDao;

    @Autowired
    public SettlementMethodGetLogicImpl(SettlementMethodDao settlementMethodDao) {
        this.settlementMethodDao = settlementMethodDao;
    }

    /**
     * ロジック実行
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 決済方法エンティティ
     */
    @Override
    public SettlementMethodEntity execute(Integer settlementMethodSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("settlementMethodSeq", settlementMethodSeq);

        // 決済方法の検索
        return settlementMethodDao.getEntity(settlementMethodSeq);
    }

    /**
     * 実行メソッド
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @param shopSeq ショップSEQ
     * @return 決済方法エンティティ
     */
    @Override
    public SettlementMethodEntity execute(Integer settlementMethodSeq, Integer shopSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("settlementMethodSeq", settlementMethodSeq);
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);

        // 決済方法の検索
        return settlementMethodDao.getSettlementMethodEntity(settlementMethodSeq, shopSeq);
    }

    /**
     * Get settlement method entity by method type.
     *
     * @param settlementMethodType 決済方法種別
     * @param openStatus open status settlement.
     * @return 決済方法エンティティ
     */
    @Override
    public SettlementMethodEntity execute(HTypeSettlementMethodType settlementMethodType, HTypeOpenStatus openStatus) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("settlementMethodType", settlementMethodType);

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("openStatus", openStatus);
        // ショップSEQ
        Integer shopSeq = 1001;

        // 決済方法の検索
        return settlementMethodDao.getEntityBySettlementMethodTypeAndOpenStatus(
                        shopSeq, settlementMethodType, openStatus);
    }

}