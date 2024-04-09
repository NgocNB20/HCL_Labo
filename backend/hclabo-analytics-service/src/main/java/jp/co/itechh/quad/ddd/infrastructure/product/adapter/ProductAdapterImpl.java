/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDetails;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductDisplays;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品アダプター実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ProductAdapterImpl implements IProductAdapter {

    /**
     * 商品API
     */
    private final ProductApi productApi;

    /**
     * 商品アダプター実装Helperクラス
     */
    private final ProductAdapterHelper productAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param productApi           商品API
     * @param productAdapterHelper 商品アダプター実装Helperクラス
     * @param headerParamsUtil     ヘッダパラメーターユーティル
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
     * 商品マイクロサービス
     * 商品詳細リスト取得
     *
     * @param goodsCodeList 商品コードリスト
     * @return 商品詳細リスト
     */
    @Override
    public List<ProductDetails> getProductDetails(List<Integer> goodsCodeList) {

        ProductDetailListGetRequest request = new ProductDetailListGetRequest();

        request.setGoodsSeqList(goodsCodeList);

        ProductDetailListResponse response = productApi.getDetails(request, null);

        return productAdapterHelper.toProductDetailsList(response.getGoodsDetailsList());
    }

    /**
     * 商品表示取得
     *
     * @param goodsGroupCode 商品グループ番号
     * @return 商品表示
     */
    @Override
    public ProductDisplays getProductDisplay(String goodsGroupCode) {
        ProductDisplayGetRequest productDisplayGetRequest = new ProductDisplayGetRequest();
        productDisplayGetRequest.setGoodCode(null);
        productDisplayGetRequest.setGoodsGroupCode(goodsGroupCode);
        productDisplayGetRequest.setOpenStatus("1");
        productDisplayGetRequest.setSiteType("3");

        ProductDisplayResponse response = productApi.getForDisplay(productDisplayGetRequest);

        return productAdapterHelper.toProductDisplay(response);
    }

}