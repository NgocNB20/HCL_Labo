/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;

import java.util.List;

/**
 * 商品詳細DTOリスト取得ロジック
 *
 * @author USER
 * @version $Revision: 1.3 $
 *
 */
public interface GoodsDetailsListGetBySeqLogic {

    /**
     *
     * 実行メソッド
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 商品詳細DTOリスト
     */
    List<GoodsDetailsDto> execute(List<Integer> goodsSeqList);

}