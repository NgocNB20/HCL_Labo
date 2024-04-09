/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement;

import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;

import java.util.List;

/**
 * 決済方法リスト取得ロジック
 *
 * @author nakamura
 * @version $Revision: 1.2 $
 *
 */
public interface SettlementMethodEntityListGetLogic {

    /**
     * 実行メソッド
     *
     * @param shopSeq   ショップSeq
     * @return 決済方法エンティティ全件分のリスト（ソート：表示昇順）
     */
    List<SettlementMethodEntity> execute(Integer shopSeq);

    /**
     * リンク決済一覧取得
     *
     * @return リンク決済個別決済手段
     */
    List<SettlementMethodLinkEntity> getPaymentMethodLink();

}