/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.multipayment;

import jp.co.itechh.quad.core.entity.multipayment.CardBrandEntity;

import java.util.List;

/**
 * カードブランド情報取得ロジック
 *
 * @author ito
 *
 */
public interface CardBrandGetLogic {

    /**
     * カードブランド情報を取得
     *
     * @param cardBrandCode クレジットカード会社コード
     * @return カードブランド情報エンティティ
     */
    CardBrandEntity execute(String cardBrandCode);

    /**
     * カードブランドリスト取得
     *
     * @param frontDisplayFlag Front表示フラグ
     * @return カードブランド情報リスト
     */
    List<CardBrandEntity> execute(boolean frontDisplayFlag);

}