/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.product.adapter;

import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDetails;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDisplays;

import java.util.List;

/**
 * 商品アダプター
 */
public interface IProductAdapter {

    /**
     * 商品詳細リスト取得
     *
     * @param goodsCodeList 商品コードリスト
     * @return 商品取得リスト
     */
    List<ProductDetails> getProductDetails(List<Integer> goodsCodeList);

    /**
     * 商品表示取得
     *
     * @param goodsGroupCode
     * @return 製品表示
     */
    ProductDisplays getProductDisplay(String goodsGroupCode);

}