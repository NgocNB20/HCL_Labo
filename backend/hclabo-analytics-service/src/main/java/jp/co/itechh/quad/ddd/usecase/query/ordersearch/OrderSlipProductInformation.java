package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 受注商品
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderSlipProductInformation {

    /**
     * 商品SEQ
     */
    private String goodsSeq;

    /**
     * 注文商品連番
     */
    private Integer orderItemSeq;

    /**
     * 商品管理番号
     */
    private String goodsGroupCode;

    /**
     * 商品コード
     */
    private String goodsCode;

    /**
     * カテゴリーID
     */
    private String categoryId;

    /**
     * カテゴリー名
     */
    private String categoryName;

    /**
     * 商品タグ
     */
    private String goodsTag;

    /**
     * アイコンID
     */
    private String iconId;

    /**
     * アイコン名
     */
    private String iconName;

    /**
     * 商品名
     */
    private String goodsGroupName;

    /**
     * 規格1
     */
    private String unitValue1;

    /**
     * 規格2
     */
    private String unitValue2;

    /**
     * 規格タイトル1
     */
    private String unitTitle1;

    /**
     * 規格タイトル2
     */
    private String unitTitle2;

    /**
     * 商品数量
     */
    private Integer itemCount;

    /**
     * 商品単価
     */
    private Integer unitPrice;

    /**
     * JANコード
     */
    private String janCode;

}
