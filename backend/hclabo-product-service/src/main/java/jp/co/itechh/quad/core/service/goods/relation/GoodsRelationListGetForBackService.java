/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.relation;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;

import java.util.List;

/**
 * 関連商品リスト取得（バック用）
 *
 * @author hirata
 * @version $Revision: 1.3 $
 */
public interface GoodsRelationListGetForBackService {

    /**
     * 実行メソッド
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 関連商品エンティティリスト
     */
    List<GoodsRelationEntity> execute(Integer goodsGroupSeq);
}
