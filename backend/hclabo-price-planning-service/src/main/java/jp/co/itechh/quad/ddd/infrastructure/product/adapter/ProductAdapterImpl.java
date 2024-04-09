/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class ProductAdapterImpl implements IProductAdapter {

    /**
     * 商品API
     */
    private final ProductApi productApi;

    /**
     * 商品アダプタークラスHelperクラス
     */
    private final ProductAdapterHelper productAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param productApi
     * @param productAdapterHelper
     * @param headerParamsUtil ヘッダパラメーターユーティル
     */
    @Autowired
    public ProductAdapterImpl(ProductApi productApi,
                              ProductAdapterHelper productAdapterHelper,
                              HeaderParamsUtility headerParamsUtil) {
        this.productApi = productApi;
        this.productAdapterHelper = productAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.productApi.getApiClient());
    }

    /**
     * 商品詳細情報取得
     *
     * @param goodsSeq
     * @return GoodsDetailsDto
     */
    @Override
    public GoodsDetailsDto getGoodsDetailsDto(Integer goodsSeq) {

        ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
        List<Integer> goodsSeqList = new ArrayList<>();
        goodsSeqList.add(goodsSeq);
        productDetailListGetRequest.setGoodsSeqList(goodsSeqList);

        // TODO 「GET /products/details : 商品詳細一覧取得」で取得しても常にNull。マスタで税込み価格を新たに保持するか、goodsPriceが常に税込みか商品サービスの修正が必要
        ProductDetailListResponse productDetailListResponse = productApi.getDetails(productDetailListGetRequest, null);
        List<GoodsDetailsDto> goodsDetailsDtos = productAdapterHelper.toGoodsDetailsDtoList(productDetailListResponse);
        if (goodsDetailsDtos != null) {
            return goodsDetailsDtos.get(0);
        }
        return null;
    }

    /**
     * 商品詳細一覧取得取得
     *
     * @param goodsSeqList
     * @return GoodsDetailsDtoリスト
     */
    @Override
    public List<GoodsDetailsDto> getGoodsDetailsList(List<Integer> goodsSeqList) {

        ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
        productDetailListGetRequest.setGoodsSeqList(goodsSeqList);

        ProductDetailListResponse productDetailListResponse = productApi.getDetails(productDetailListGetRequest, null);

        return productAdapterHelper.toGoodsDetailsDtoList(productDetailListResponse);
    }

}