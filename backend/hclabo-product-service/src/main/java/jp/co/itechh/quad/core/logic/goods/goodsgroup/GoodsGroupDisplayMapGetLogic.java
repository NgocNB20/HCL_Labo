/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品グループ表示マップ取得
 *
 * @author hirata
 * @version $Revision: 1.1 $
 */
public interface GoodsGroupDisplayMapGetLogic {
    // LGP0014

    /**
     * 商品グループ表示マップ取得
     * 商品グループSEQをもとに商品グループ表示エンティティマップを取得する。
     *
     * @param goodsGroupSeq 商品グループSEQリスト
     * @return 商品グループ表示マップ
     */
    Map<Integer, GoodsGroupDisplayEntity> execute(List<Integer> goodsGroupSeq);
}