/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.order.goods;

import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeSalesFlag;
import jp.co.itechh.quad.core.constant.type.HTypeTotalingType;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 受注商品クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "OrderGoods")
@Data
@Component
@Scope("prototype")
public class OrderGoodsEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    @Column(name = "orderSeq")
    @Id
    private Integer orderSeq;

    /** 受注商品連番（必須） */
    @Column(name = "orderGoodsVersionNo")
    @Id
    private Integer orderGoodsVersionNo;

    /** 注文連番（必須） */
    @Column(name = "orderConsecutiveNo")
    private Integer orderConsecutiveNo = 1;

    /** 商品SEQ（必須） */
    @Column(name = "goodsSeq")
    @Id
    private Integer goodsSeq;

    /** 表示順（必須） */
    @Column(name = "orderDisplay")
    @Id
    private Integer orderDisplay;

    /** 集計種別（必須） */
    @Column(name = "totalingType")
    private HTypeTotalingType totalingType;

    /** 売上フラグ（必須） */
    @Column(name = "salesFlag")
    private HTypeSalesFlag salesFlag = HTypeSalesFlag.OFF;

    /** 処理日時（必須） */
    @Column(name = "processTime")
    private Timestamp processTime;

    /** 商品グループコード（必須） */
    @Column(name = "goodsGroupCode")
    private String goodsGroupCode;

    /** 商品コード（必須） */
    @Column(name = "goodsCode")
    private String goodsCode;

    /** JANコード */
    @Column(name = "janCode")
    private String janCode;

    /** 商品グループ名 */
    @Column(name = "goodsGroupName")
    private String goodsGroupName;

    /** 商品消費税種別（必須） */
    @Column(name = "goodsTaxType")
    private HTypeGoodsTaxType goodsTaxType = HTypeGoodsTaxType.OUT_TAX;

    /** 税率（必須） */
    @Column(name = "taxRate")
    private BigDecimal taxRate;

    /** 商品単価（必須） */
    @Column(name = "goodsPrice")
    private BigDecimal goodsPrice = BigDecimal.ZERO;

    /** 前回商品数量 */
    @Column(name = "preGoodsCount")
    private BigDecimal preGoodsCount = BigDecimal.ZERO;

    /** 商品数量（必須） */
    @Column(name = "goodsCount")
    private BigDecimal goodsCount = BigDecimal.ZERO;

    /** 無料配送フラグ（必須） */
    @Column(name = "freeDeliveryFlag")
    private HTypeFreeDeliveryFlag freeDeliveryFlag = HTypeFreeDeliveryFlag.OFF;

    /** 規格値１ */
    @Column(name = "unitValue1")
    private String unitValue1;

    /** 規格値２ */
    @Column(name = "unitValue2")
    private String unitValue2;

    /** 商品個別配送種別（必須） */
    @Column(name = "individualDeliveryType")
    private HTypeIndividualDeliveryType individualDeliveryType = HTypeIndividualDeliveryType.OFF;

    /** 商品納期 */
    @Column(name = "deliveryType")
    private String deliveryType;

    /** 受注連携設定１ */
    @Column(name = "orderSetting1")
    private String orderSetting1;

    /** 受注連携設定２ */
    @Column(name = "orderSetting2")
    private String orderSetting2;

    /** 受注連携設定３ */
    @Column(name = "orderSetting3")
    private String orderSetting3;

    /** 受注連携設定４ */
    @Column(name = "orderSetting4")
    private String orderSetting4;

    /** 受注連携設定５ */
    @Column(name = "orderSetting5")
    private String orderSetting5;

    /** 受注連携設定６ */
    @Column(name = "orderSetting6")
    private String orderSetting6;

    /** 受注連携設定７ */
    @Column(name = "orderSetting7")
    private String orderSetting7;

    /** 受注連携設定８ */
    @Column(name = "orderSetting8")
    private String orderSetting8;

    /** 受注連携設定９ */
    @Column(name = "orderSetting9")
    private String orderSetting9;

    /** 受注連携設定１０ */
    @Column(name = "orderSetting10")
    private String orderSetting10;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}