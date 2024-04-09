/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.settlement.impl;

import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodEntityListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodEntityListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 決済方法リスト取得サービス実装クラス
 *
 * @author nakamura
 * @version $Revision: 1.3 $
 *
 */
@Service
public class SettlementMethodEntityListGetServiceImpl extends AbstractShopService
                implements SettlementMethodEntityListGetService {

    /** 決済方法リスト取得ロジック */
    private final SettlementMethodEntityListGetLogic settlementMethodEntityListGetLogic;

    @Autowired
    public SettlementMethodEntityListGetServiceImpl(SettlementMethodEntityListGetLogic settlementMethodEntityListGetLogic) {
        this.settlementMethodEntityListGetLogic = settlementMethodEntityListGetLogic;
    }

    /**
     * 実行メソッド
     *
     * @return 決済方法エンティティ全件分のリスト（ソート：表示昇順）
     */
    @Override
    public List<SettlementMethodEntity> execute() {

        // ショップSEQを取得
        Integer shopSeq = 1001;

        // 決済方法リスト取得ロジック実行
        return settlementMethodEntityListGetLogic.execute(shopSeq);
    }

    /**
     * リンク決済一覧取得
     *
     * @return リンク決済個別決済手段
     */
    @Override
    public List<SettlementMethodLinkEntity> getPaymentMethodLink() {
        return settlementMethodEntityListGetLogic.getPaymentMethodLink();
    }

}