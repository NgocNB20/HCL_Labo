/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsUnitDto;

import java.util.List;

/**
 * 商品規格リスト取得ロジック
 *
 * @author hs32101
 *
 */
public interface GoodsUnitListGetLogic {

    /**
     * 規格値2リスト取得処理
     *
     * @param ggcd 商品グループコード
     * @param gcd 商品コード
     * @return 規格値2リスト
     */
    List<GoodsUnitDto> getUnit2List(String ggcd, String gcd);

}