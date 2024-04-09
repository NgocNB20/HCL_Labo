/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.product.adapter;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;

import java.util.List;

/**
 * 商品アダプター
 */
public interface IProductAdapter {

    /**
     * 商品マイクロサービス<br/>
     * 商品詳細情報リスト取得
     * TODO ボトムアップ定義の商品API
     *  GET /products/details : 商品詳細一覧取得（getDetails）を呼び出す
     *  上記、APIはPageInfoRequestも引数に含むが、NullでOK
     *  別の層でStringからIntegerに変換すること
     *
     * @param productIdList 商品IDリスト
     * @return 商品詳細リスト
     */
    List<GoodsDetailsDto> getDetails(List<String> productIdList);

    /**
     * GET /products/details : 商品詳細一覧取得（getDetails）を呼び出す
     * 上記、APIはPageInfoRequestも引数に含むが、NullでOK
     * 別の層でStringからIntegerに変換すること
     *
     * @param goodsCode
     * @param openstatus
     * @return
     */
    GoodsDetailsDto getDetailsByGoodCode(String goodsCode, HTypeOpenDeleteStatus openstatus);

    /**
     * GET /products : 商品グループ一覧取得
     * 商品グループ一覧取得
     *
     * @param productListGetRequest 商品グループ一覧取得リクエスト (required)
     * @param pageInfoRequest       ページ情報リクエスト (optional)
     * @return 商品グループ一覧レスポンス
     * or その他エラー
     */
    List<GoodsGroupDto> getGoodsGroupDtos(ProductListGetRequest productListGetRequest, PageInfoRequest pageInfoRequest);
}
