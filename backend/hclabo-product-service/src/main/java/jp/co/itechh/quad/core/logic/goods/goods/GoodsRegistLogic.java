/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品登録
 *
 * @author hirata
 * @version $Revision: 1.1 $
 */
public interface GoodsRegistLogic {
    // LGG0003

    /**
     * 処理件数マップ　商品登録件数
     * <code>GOODS_REGIST</code>
     */
    public static final String GOODS_REGIST = "GoodsRegist";

    /**
     * 処理件数マップ　商品更新件数
     * <code>GOODS_UPDATE</code>
     */
    public static final String GOODS_UPDATE = "GoodsUpdate";

    /**
     * 商品グループSEQ不一致エラー
     * <code>MSGCD_GOODSGROUP_MISMATCH_FAIL</code>
     */
    public static final String MSGCD_GOODSGROUP_MISMATCH_FAIL = "LGG000301";

    /**
     *
     * <code>SUPPLEMENT_HISTORY_UPDATE</code>
     */
    public static final String SUPPLEMENT_HISTORY_UPDATE = "supplementHistoryUpdate";

    /**
     *
     * 商品登録
     *
     * @param administratorSeq 管理者SEQ
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsEntityList 商品エンティティリスト
     * @return 処理件数マップ
     */
    Map<String, Integer> execute(Integer administratorSeq, Integer goodsGroupSeq, List<GoodsEntity> goodsEntityList);
}