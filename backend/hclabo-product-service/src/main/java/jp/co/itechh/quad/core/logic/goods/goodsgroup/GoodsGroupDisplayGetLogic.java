/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;

import java.util.List;

/**
 * 商品グループ表示取得
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 */
public interface GoodsGroupDisplayGetLogic {
    // LGP0007

    /**
     * 商品グループ表示取得
     * 商品グループSEQをもとに商品グループ表示エンティティを取得する。
     *
     * @param goodsGroupSeq 商品グループSEQリスト
     * @return 商品グループ表示情報
     */
    GoodsGroupDisplayEntity execute(Integer goodsGroupSeq);

    /**
     * セット子商品グループ表示取得
     * 商品グループSEQをもとに商品グループ表示エンティティを取得する。
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return 商品グループ表示情報
     */
    List<GoodsGroupDisplayEntity> execute(List<Integer> goodsGroupSeqList);

    /**
     * 商品検索キーワードからエンティティのリストを取得
     *
     * @param shopSeq ショップSEQ
     * @param searchKeyword 商品検索キーワード
     * @return エンティティのリスト
     */
    List<GoodsGroupDisplayEntity> getEntityListByKeyword(Integer shopSeq, String searchKeyword);
}