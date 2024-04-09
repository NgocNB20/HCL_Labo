package jp.co.itechh.quad.ddd.domain.product.adapter;

import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductCategory;

import java.util.List;

/**
 * カテゴリ アダプタ
 */
public interface ICategoryAdapter {

    /**
     * すべてのカテゴリリストを取得する
     *
     * @return カテゴリー登録商品一覧
     */
    List<ProductCategory> getCategoryList();
}
