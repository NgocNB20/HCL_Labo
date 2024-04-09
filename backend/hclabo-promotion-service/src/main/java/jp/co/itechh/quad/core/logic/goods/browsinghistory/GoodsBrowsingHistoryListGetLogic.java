/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.browsinghistory;

import jp.co.itechh.quad.core.dto.goods.browsinghistory.BrowsinghistorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;

import java.util.List;

/**
 * あしあと商品詳細リスト取得<br/>
 * あしあと商品のリストを取得する。<br/>
 *
 * @author ozaki
 *
 */
public interface GoodsBrowsingHistoryListGetLogic {

    /**
     * あしあと商品詳細リスト取得<br/>
     * あしあと商品のリストを取得する。<br/>
     *
     * @param browsinghistorySearchForDaoConditionDto あしあと商品Dao用検索条件Dto
     * @return 商品グループDTO一覧
     */
    List<GoodsGroupDto> execute(BrowsinghistorySearchForDaoConditionDto browsinghistorySearchForDaoConditionDto);

}
