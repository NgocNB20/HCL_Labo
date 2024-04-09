/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.freearea;

import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

/**
 * フリーエリア情報取得ロジック
 *
 * @author natume
 * @version $Revision: 1.2 $
 *
 */
public interface FreeAreaGetLogic {

    // LSF0001

    /**
     * フリーエリア情報取得処理
     *
     * @param shopSeq ショップSEQ
     * @param freeAreaKey フリーエリアキー
     * @return フリーエリアエンティティ
     */
    FreeAreaEntity execute(Integer shopSeq, String freeAreaKey);

    /**
     * フリーエリア情報取得処理
     *
     * @param shopSeq ショップSEQ
     * @param freeAreaSeq フリーエリアSEQ
     * @return フリーエリアエンティティ
     */
    FreeAreaEntity execute(Integer shopSeq, Integer freeAreaSeq);
}
