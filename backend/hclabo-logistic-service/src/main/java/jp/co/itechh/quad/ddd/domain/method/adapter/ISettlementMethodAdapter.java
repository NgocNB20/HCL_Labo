/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.method.adapter;

import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;

import java.util.List;

/**
 * 決済方法 アダプター
 */
public interface ISettlementMethodAdapter {

    /**
     * 決済方法一覧取得 決済方法一覧取得
     *
     * @return List<SettlementMethodEntity> 決済方法クラスリスト
     */
    List<SettlementMethodEntity> getSettlementMethod();
}
