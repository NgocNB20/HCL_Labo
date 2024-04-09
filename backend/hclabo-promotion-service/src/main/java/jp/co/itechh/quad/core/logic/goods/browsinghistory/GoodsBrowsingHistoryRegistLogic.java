/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.browsinghistory;

import jp.co.itechh.quad.core.entity.goods.browsinghistory.BrowsinghistoryEntity;

/**
 *
 * あしあと商品情報登録<br/>
 * あしあと商品情報を登録する。<br/>
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
public interface GoodsBrowsingHistoryRegistLogic {

    /**
     *
     * あしあと商品情報登録<br/>
     * あしあと商品情報を登録する。<br/>
     *
     * @param browsinghistoryEntity あしあと情報エンティティ
     * @return 登録・更新件数
     */
    int execute(BrowsinghistoryEntity browsinghistoryEntity);

}
