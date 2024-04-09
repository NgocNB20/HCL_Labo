/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.freearea;

import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

/**
 * フリーエリア削除
 *
 * @author nose
 *
 */
public interface FreeAreaDeleteLogic {

    /**
     * フリーエリア削除
     *
     * @param freeAreaEntity フリーエリア情報
     * @return 処理件数
     */
    int execute(FreeAreaEntity freeAreaEntity);

}
