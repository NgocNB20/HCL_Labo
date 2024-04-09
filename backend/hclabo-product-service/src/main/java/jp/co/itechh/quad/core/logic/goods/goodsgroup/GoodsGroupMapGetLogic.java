/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;

import java.util.List;
import java.util.Map;

/**
 * 商品グループマップ取得
 *
 * @author ozaki
 * @version $Revision: 1.2 $
 */
public interface GoodsGroupMapGetLogic {

    // LGP0002

    /**
     * 商品グループマップ取得
     * 商品グループDTOを保持するマップを取得する。
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return 商品グループMAP
     */
    Map<Integer, GoodsGroupDto> execute(List<Integer> goodsGroupSeqList);
}