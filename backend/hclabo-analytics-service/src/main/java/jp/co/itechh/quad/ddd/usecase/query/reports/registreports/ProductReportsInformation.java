package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductCategory;
import jp.co.itechh.quad.ddd.domain.product.adapter.model.ProductIcons;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 商品表示情報
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductReportsInformation {

    /**
     * 商品SEQ
     */
    private String goodsSeq;

    /**
     * 商品管理番号
     */
    private String goodsGroupCode;

    /**
     * 商品番号
     */
    private String goodsCode;

    /**
     * アイコン一覧
     */
    private List<ProductIcons> iconList;

    /**
     * 商品タグ一覧
     */
    private List<String> goodsTagList;

    /**
     * カテゴリ一覧
     */
    private List<ProductCategory> categoryList;

}
