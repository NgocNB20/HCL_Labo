package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 受注商品
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderProduct {

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
     * 数量
     */
    private Integer goodsCount;

    /**
     * 小計
     */
    private Integer summaryPrice;

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

    /**
     * 注文商品ID
     */
    private String orderItemId;

    /**
     * 検査キット番号
     */
    private String examKitCode;

    /**
     * 検査状態
     */
    private String examStatus;

    /**
     * 検体番号
     */
    private String specimenCode;

    /**
     * 受付日
     */
    private Date receptionDate;

    /**
     * 検体コメント
     */
    private String specimenComment;

    /**
     * 検査結果PDF
     */
    private String examResultsPdf;
}
