/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.product.adapter;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;

import java.util.List;

/**
 * 商品アダプター
 */
public interface IProductAdapter {

    /**
     * 商品詳細情報取得
     *
     * @param goodsSeq
     * @return GoodsDetailsDto
     */
    GoodsDetailsDto getGoodsDetailsDto(Integer goodsSeq);

    /**
     * 商品詳細一覧取得取得
     *
     * @param goodsSeqList
     * @return GoodsDetailsDtoリスト
     */
    List<GoodsDetailsDto> getGoodsDetailsList(List<Integer> goodsSeqList);
}
