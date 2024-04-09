/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;

import java.util.List;

/**
 *
 * 商品データチェック
 *
 * @author hirata
 * @version $Revision: 1.5 $
 *
 */
public interface GoodsDataCheckLogic {
    // LGG0005

    /**
     * 商品コード重複エラー
     * <code>MSGCD_GOODSCODE_REPETITION_FAIL</code>
     */
    public static final String MSGCD_GOODSCODE_REPETITION_FAIL = "LGG000501";

    /**
     * 商品データチェック
     * 商品エンティティリストの登録・更新前チェックを行う。
     *
     * @param goodsEntityList 商品エンティティリスト
     * @param shopSeq ショップSEQ
     * @param goodsGroupCode 商品グループコード
     */
    void execute(List<GoodsEntity> goodsEntityList, Integer shopSeq, String goodsGroupCode);
}