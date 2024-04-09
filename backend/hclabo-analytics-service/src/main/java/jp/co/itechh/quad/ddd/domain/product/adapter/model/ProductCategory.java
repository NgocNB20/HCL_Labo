package jp.co.itechh.quad.ddd.domain.product.adapter.model;

import lombok.Data;

/**
 * カテゴリ登録商品
 */
@Data
public class ProductCategory {

    /**
     * カテゴリーSEQ
     */
    private Integer categorySeq;

    /**
     * 種別名
     */
    private String categoryName;

    /**
     * カテゴリーID
     */
    private String categoryId;

}
