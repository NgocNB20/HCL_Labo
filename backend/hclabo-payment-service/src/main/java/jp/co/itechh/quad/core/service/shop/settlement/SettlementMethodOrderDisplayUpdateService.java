/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.settlement;

import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;

import java.util.List;

/**
 * 決済方法表示順更新サービス
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
public interface SettlementMethodOrderDisplayUpdateService {

    /**
     * 実行メソッド
     *
     * @param settlementMethodList 決済方法エンティティリスト
     * @return 処理件数
     */
    int execute(List<SettlementMethodEntity> settlementMethodList);
}
