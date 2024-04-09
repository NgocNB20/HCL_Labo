/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.freearea;

import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

/**
 * フリーエリア登録
 *
 * @author shibuya
 */
public interface FreeAreaRegistLogic {

    /**
     * フリーエリア登録
     *
     * @param  freeAreaEntity フリーエリアエンティティ
     * @return 処理件数
     */
    int execute(FreeAreaEntity freeAreaEntity);
}
