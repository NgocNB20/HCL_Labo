package jp.co.itechh.quad.ddd.infrastructure.product.adapter;

import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductCategory;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * カテゴリーアダプター実装Helperクラス
 */
@Component
public class CategoryAdapterHelper {

    /**
     * カテゴリー登録商品一覧に変換
     *
     * @param responseList カテゴリ一覧レスポンス
     * @return カテゴリー登録商品一覧
     */
    public List<ProductCategory> toProductCategoryList(CategoryListResponse responseList) {

        if (CollectionUtils.isEmpty(responseList.getCategoryList())) {
            return new ArrayList<>();
        }

        List<ProductCategory> productCategoryList = new ArrayList<>();

        responseList.getCategoryList().forEach(item -> {
            ProductCategory productCategory = new ProductCategory();

            productCategory.setCategorySeq(item.getCategorySeq());
            productCategory.setCategoryId(item.getCategoryId());
            productCategory.setCategoryName(item.getCategoryName());

            productCategoryList.add(productCategory);
        });

        return productCategoryList;
    }

}
