/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.freearea;

import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

/**
 * フリーエリア更新
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface FreeAreaUpdateLogic {

    /**
     * フリーエリア更新
     *
     * @param  freeAreaEntity フリーエリアエンティティ
     * @return 処理件数
     */
    int execute(FreeAreaEntity freeAreaEntity);

}
