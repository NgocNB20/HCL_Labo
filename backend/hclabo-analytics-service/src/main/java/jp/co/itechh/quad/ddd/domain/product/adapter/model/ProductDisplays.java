package jp.co.itechh.quad.ddd.domain.product.adapter.model;

import lombok.Data;

import java.util.List;

/**
 * 商品表示
 */
@Data
public class ProductDisplays {

    /**
     * 商品管理番号
     */
    private String goodsGroupCode;

    /**
     * 商品アイコン情報一覧
     */
    private List<ProductIcons> iconList;

    /**
     * 商品タグ一覧
     */
    private List<String> goodsTagList;

    /**
     * カテゴリSEQ一覧
     */
    private List<Integer> categorySeqList;

}
