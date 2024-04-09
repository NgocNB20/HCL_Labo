/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.relation;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;

import java.util.List;

/**
 *
 * 関連商品リスト取得（バック用）
 * 対象商品の関連商品リストを取得する。
 *
 * @author hirata
 * @version $Revision: 1.1 $
 *
 */
public interface GoodsRelationListGetForBackLogic {

    // LGR0003

    /**
     *
     * 関連商品リスト取得（バック用）
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 関連商品エンティティリスト
     */
    List<GoodsRelationEntity> execute(Integer goodsGroupSeq);
}