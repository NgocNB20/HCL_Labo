package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 注文商品リスト情報
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportsProductItem {

    /**
     * 商品管理番号
     */
    private String goodsGroupCode;

    /**
     * 商品番号
     */
    private String goodsCode;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 規格1表示名
     */
    @Field(write = Field.Write.ALWAYS)
    private String unitTitle1;

    /**
     * 規格2表示名
     */
    @Field(write = Field.Write.ALWAYS)
    private String unitTitle2;

    /**
     * 規格1
     */
    @Field(write = Field.Write.ALWAYS)
    private String unitValue1;

    /**
     * 規格2
     */
    @Field(write = Field.Write.ALWAYS)
    private String unitValue2;

    /**
     * JANコード
     */
    @Field(write = Field.Write.ALWAYS)
    private String janCode;

    /**
     * ノベルティ商品フラグ
     */
    private String noveltyGoodsType;

    /**
     * カテゴリーID
     */
    @Field(write = Field.Write.ALWAYS)
    private String categoryId;

    /**
     * カテゴリー名
     */
    @Field(write = Field.Write.ALWAYS)
    private String categoryName;

    /**
     * 商品タグ
     */
    @Field(write = Field.Write.ALWAYS)
    private String goodsTag;

    /**
     * アイコンID
     */
    @Field(write = Field.Write.ALWAYS)
    private String iconId;

    /**
     * アイコン名
     */
    @Field(write = Field.Write.ALWAYS)
    private String iconName;

    /**
     * 商品単価
     */
    private Integer unitPrice;

    /**
     * 商品税率
     */
    private Integer taxRate;

    /**
     * 販売数
     */
    private Integer salesCount;

    /**
     * キャンセル数
     */
    private Integer cancelCount;

}
