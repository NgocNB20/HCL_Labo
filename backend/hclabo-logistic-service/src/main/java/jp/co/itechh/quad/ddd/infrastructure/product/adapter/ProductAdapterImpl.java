/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsGroupDisplayDto;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsStockDisplayUpsertRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDisplayResponse;
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
     * 注文API
     */
    private final ProductApi productApi;

    /**
     * 商品アダプターHelperクラス
     */
    private final ProductAdapterHelper productAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param productApi 注文API
     * @param productAdapterHelper 商品アダプターHelperクラス
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

        ProductDetailListResponse productDetailListResponse = productApi.getDetails(productDetailListGetRequest, null);

        List<GoodsDetailsDto> goodsDetailsDtos = productAdapterHelper.toGoodsDetailsDtoList(productDetailListResponse);

        if (goodsDetailsDtos != null) {
            return goodsDetailsDtos.get(0);
        }

        return null;
    }

    /**
     * 商品マイクロサービス<br/>
     * 商品詳細情報リスト取得
     * GET /products/details : 商品詳細一覧取得（getDetails）を呼び出す
     * 上記、APIはPageInfoRequestも引数に含むが、NullでOK
     * 別の層でStringからIntegerに変換すること
     *
     * @param productIdList 商品IDリスト
     * @return 商品詳細リスト
     */
    @Override
    public List<GoodsDetailsDto> getDetails(List<String> productIdList) {

        ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
        productDetailListGetRequest.setGoodsSeqList(productAdapterHelper.toIntegerList(productIdList));

        ProductDetailListResponse productDetailListResponse = productApi.getDetails(productDetailListGetRequest, null);

        List<GoodsDetailsDto> goodsDetailsDtos = productAdapterHelper.toGoodsDetailsDtoList(productDetailListResponse);

        return goodsDetailsDtos;
    }

    /**
     * 商品グループコードで商品グループ表示DTOを取得する
     *
     * @param goodsGroupCode 商品グループコード
     * @return 商品グループ表示DTO
     */
    @Override
    public GoodsGroupDisplayDto getGoodsGroupDisplayByGoodsGroupCode(String goodsGroupCode) {
        ProductDisplayGetRequest productDisplayGetRequest = new ProductDisplayGetRequest();
        productDisplayGetRequest.setGoodsGroupCode(goodsGroupCode);
        productDisplayGetRequest.setGoodCode(null);
        productDisplayGetRequest.setOpenStatus(EnumTypeUtil.getValue(HTypeOpenDeleteStatus.OPEN));
        productDisplayGetRequest.setSiteType(EnumTypeUtil.getValue(HTypeSiteType.BACK));

        ProductDisplayResponse response = productApi.getForDisplay(productDisplayGetRequest);
        return productAdapterHelper.toGoodsGroupDisplayDto(response);
    }

    /**
     * 在庫登録更新結果を商品サービス側に反映<br/>
     * 在庫関連のテーブルが更新されコミットされた後に呼び出すこと
     *
     * @param goodsSeqList 商品SEQリスト
     * @return BatchExecuteResponse
     */
    @Override
    public BatchExecuteResponse syncUpsertGoodsStockDisplay(List<Integer> goodsSeqList) {
        GoodsStockDisplayUpsertRequest goodsStockDisplayUpsertRequest = new GoodsStockDisplayUpsertRequest();
        goodsStockDisplayUpsertRequest.setGoodsSeqList(goodsSeqList);

        return productApi.syncUpsertGoodsStockDisplay(goodsStockDisplayUpsertRequest);
    }
}