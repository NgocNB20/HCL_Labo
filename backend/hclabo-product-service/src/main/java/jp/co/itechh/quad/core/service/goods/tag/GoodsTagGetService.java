/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.tag;

import jp.co.itechh.quad.core.dto.goods.goodstag.GoodsTagDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsTagEntity;

import java.util.List;

/**
 * タグ商品ロサービス
 *
 * @author Pham Quang Dieu
 */
public interface GoodsTagGetService {

    /**
     *
     * 商品タグリスト取得
     *
     * @param dto タグ商品Dtoクラス
     * @return タグ商品リスト
     */
    List<GoodsTagEntity> execute(GoodsTagDto dto);

}
