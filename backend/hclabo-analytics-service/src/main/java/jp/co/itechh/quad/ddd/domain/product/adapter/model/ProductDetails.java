package jp.co.itechh.quad.ddd.domain.product.adapter.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品詳細
 */
@Data
public class ProductDetails {

    /**
     * 商品管理SEQ
     */
    private Integer goodsGroupSeq;

    /**
     * 商品SEQ
     */
    private Integer goodsSeq;

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
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 販売開始日時
     */
    private Date saleStartTime;

    /**
     * 受注連携設定01
     */
    private String orderSetting1;

    /**
     * 受注連携設定02
     */
    private String orderSetting2;

    /**
     * 受注連携設定03
     */
    private String orderSetting3;

    /**
     * 受注連携設定04
     */
    private String orderSetting4;

    /**
     * 受注連携設定05
     */
    private String orderSetting5;

    /**
     * 受注連携設定06
     */
    private String orderSetting6;

    /**
     * 受注連携設定07
     */
    private String orderSetting7;

    /**
     * 受注連携設定08
     */
    private String orderSetting8;

    /**
     * 受注連携設定09
     */
    private String orderSetting9;

    /**
     * 受注連携設定10
     */
    private String orderSetting10;

    /**
     * ノベルティ商品フラグ
     */
    private String noveltyGoodsType;
}
