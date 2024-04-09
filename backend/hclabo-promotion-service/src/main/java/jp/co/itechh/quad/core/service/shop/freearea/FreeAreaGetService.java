/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.freearea;

import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

/**
 * フリーエリア情報取得サービス
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public interface FreeAreaGetService {

    // SSF0001

    /**
     * フリーエリア情報取得処理
     *
     * @param freeAreaKey フリーエリアキー
     * @return フリーエリアエンティティ
     */
    FreeAreaEntity execute(String freeAreaKey);

    /**
     * フリーエリア情報取得処理
     *
     * @param freeAreaSeq フリーエリアSEQ
     * @return フリーエリアエンティティ
     */
    FreeAreaEntity execute(Integer freeAreaSeq);

}