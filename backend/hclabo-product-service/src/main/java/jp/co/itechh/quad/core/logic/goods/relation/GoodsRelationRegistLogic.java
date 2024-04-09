/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.relation;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 関連商品登録
 *
 * @author hirata
 * @version $Revision: 1.1 $
 */
public interface GoodsRelationRegistLogic {
    // LGR0002

    /**
     * 処理件数マップ　関連商品登録件数
     * <code>GOODS_RELATION_REGIST</code>
     */
    public static final String GOODS_RELATION_REGIST = "GoodsRelationRegist";

    /**
     * 処理件数マップ　関連商品更新件数
     * <code>GOODS_RELATION_UPDATE</code>
     */
    public static final String GOODS_RELATION_UPDATE = "GoodsRelationUpdate";

    /**
     * 処理件数マップ　関連商品削除件数
     * <code>GOODS_RELATION_DELETE</code>
     */
    public static final String GOODS_RELATION_DELETE = "GoodsRelationDelete";

    /**
     * 商品グループSEQ不一致エラー
     * <code>MSGCD_GOODSGROUP_MISMATCH_FAIL</code>
     */
    public static final String MSGCD_GOODSGROUP_MISMATCH_FAIL = "LGR000201";

    /**
     *
     * 関連商品登録
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsRelationEntityList 関連商品エンティティリスト
     * @return 処理件数マップ
     */
    Map<String, Integer> execute(Integer goodsGroupSeq, List<GoodsRelationEntity> goodsRelationEntityList);
}