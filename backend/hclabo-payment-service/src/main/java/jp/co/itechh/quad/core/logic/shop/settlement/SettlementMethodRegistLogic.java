/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.settlement;

import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;

/**
 * 決済方法登録
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
public interface SettlementMethodRegistLogic {

    /**
     * 決済方法登録失敗エラー
     * <code>MSGCD_SETTLEMENTMETHOD_INSERT_FAIL</code>
     */
    public static final String MSGCD_SETTLEMENTMETHOD_INSERT_FAIL = "LST000501";

    /**
     * 実行メソッド
     *
     * @param settlementMethodEntity 決済方法エンティティ
     * @return 処理件数
     */
    int execute(SettlementMethodEntity settlementMethodEntity);
}