/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailByGoodCodeGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class ProductAdapterImpl implements IProductAdapter {

    /** 商品API */
    private final ProductApi productApi;

    /** 商品アダプターHelperクラス */
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
     * 商品マイクロサービス<br/>
     * 商品詳細情報リスト取得
     *  GET /products/details : 商品詳細一覧取得（getDetails）を呼び出す
     *  上記、APIはPageInfoRequestも引数に含むが、NullでOK
     *  別の層でStringからIntegerに変換すること
     *
     * @param productIdList 商品IDリスト
     * @return 商品詳細リスト
     */
    @Override
    public List<GoodsDetailsDto> getDetails(List<String> productIdList) {

        ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
        productDetailListGetRequest.setGoodsSeqList(productAdapterHelper.toIntegerList(productIdList));

        ProductDetailListResponse productDetailListResponse = productApi.getDetails(productDetailListGetRequest, null);

        return productAdapterHelper.toGoodsDetailsDtoList(productDetailListResponse);
    }

    /**
     * @param goodsCode 商品コード
     * @param openstatus 削除状態付き公開状態
     * @return 商品詳細Dto
     */
    @Override
    public GoodsDetailsDto getDetailsByGoodCode(String goodsCode, HTypeOpenDeleteStatus openstatus) {

        ProductDetailByGoodCodeGetRequest productDetailByGoodCodeGetRequest = new ProductDetailByGoodCodeGetRequest();
        productDetailByGoodCodeGetRequest.setOpenStatus(String.valueOf(openstatus));

        GoodsDetailsResponse goodsDetailsResponse =
                        productApi.getDetailsByGoodsCode(goodsCode, productDetailByGoodCodeGetRequest);

        return productAdapterHelper.productDetailGetByGoodCode(goodsDetailsResponse);
    }

    /**
     * @param productListGetRequest
     * @param pageInfoRequest
     * @return
     */
    @Override
    public List<GoodsGroupDto> getGoodsGroupDtos(ProductListGetRequest productListGetRequest,
                                                 PageInfoRequest pageInfoRequest) {
        ProductListResponse productListResponse = productApi.getList(productListGetRequest, pageInfoRequest);

        return productAdapterHelper.toGoodsGroupDtos(productListResponse);
    }

}