package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 受注商品
 */
@Getter
@Setter
@NoArgsConstructor
public class ReportsProduct implements Serializable {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 商品管理番号
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
     * JANコード
     */
    private String janCode;

    /**
     * 商品名
     */
    private String goodsGroupName;

    /**
     * カテゴリーID
     */
    private String categoryId;

    /**
     * 種別名
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
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 価格
     */
    private Integer goodsPrice;

    /**
     * 商品金額合計
     */
    private Integer goodsPriceTotal;

    /**
     * ノベルティ商品フラグ
     */
    private String noveltyGoodsType;

    /**
     * 販売数
     */
    private Integer salesCount;

    /**
     * キャンセル数
     */
    private Integer cancelCount;

}
