/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.product.adapter.ICategoryAdapter;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * カテゴリーアダプター実装クラス
 */
@Component
public class CategoryAdapterImpl implements ICategoryAdapter {

    /**
     * カテゴリーAPI
     */
    private final CategoryApi categoryApi;

    /**
     * カテゴリーアダプター実装Helperクラス
     */
    private final CategoryAdapterHelper categoryAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param categoryApi           カテゴリーAPI
     * @param categoryAdapterHelper カテゴリーアダプター実装Helperクラス
     * @param headerParamsUtil      ヘッダパラメーターユーティル
     */
    @Autowired
    public CategoryAdapterImpl(CategoryApi categoryApi,
                               CategoryAdapterHelper categoryAdapterHelper,
                               HeaderParamsUtility headerParamsUtil) {
        this.categoryApi = categoryApi;
        this.categoryAdapterHelper = categoryAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.categoryApi.getApiClient());
    }

    /**
     * 全リストカテゴリ取得
     *
     * @return カテゴリー登録商品一覧
     */
    @Override
    public List<ProductCategory> getCategoryList() {
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        pageInfoRequest.setPage(1);
        pageInfoRequest.setLimit(Integer.MAX_VALUE);
        pageInfoRequest.setOrderBy("categoryId");
        pageInfoRequest.setSort(true);

        CategoryListResponse categoryListResponse = categoryApi.get(new CategoryListGetRequest(), pageInfoRequest);

        return categoryAdapterHelper.toProductCategoryList(categoryListResponse);
    }
}