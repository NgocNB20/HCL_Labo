/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.settlement.impl;

import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodOrderDisplayUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodOrderDisplayUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 決済方法表示順更新サービス
 *
 * @author YAMAGUCHI
 * @author matsumoto(itec) 2012/02/06 #2761 対応
 *
 */
@Service
public class SettlementMethodOrderDisplayUpdateServiceImpl extends AbstractShopService
                implements SettlementMethodOrderDisplayUpdateService {

    /** 決済方法表示順更新ロジック */
    private final SettlementMethodOrderDisplayUpdateLogic settlementMethodOrderDisplayUpdatelogic;

    @Autowired
    public SettlementMethodOrderDisplayUpdateServiceImpl(SettlementMethodOrderDisplayUpdateLogic settlementMethodOrderDisplayUpdatelogic) {
        this.settlementMethodOrderDisplayUpdatelogic = settlementMethodOrderDisplayUpdatelogic;
    }

    /**
     * 実行メソッド
     *
     * @param settlementMethodList 決済方法エンティティリスト
     * @return 処理件数
     */
    @Override
    public int execute(List<SettlementMethodEntity> settlementMethodList) {

        // ショップSEQを取得
        Integer shopSeq = 1001;

        // パラメータチェック
        if (settlementMethodList == null || settlementMethodList.isEmpty()) {
            throwMessage();
        }

        return settlementMethodOrderDisplayUpdatelogic.execute(settlementMethodList, shopSeq);
    }

}