/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order.goods;

import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeSalesFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeTotalingType;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class OrderGoodsEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    private Integer orderSeq;

    /** 受注商品連番（必須） */
    private Integer orderGoodsVersionNo;

    /** 注文連番（必須） */
    private Integer orderConsecutiveNo = 1;

    /** 商品SEQ（必須） */
    private Integer goodsSeq;

    /** 表示順（必須） */
    private Integer orderDisplay;

    /** 集計種別（必須） */
    private HTypeTotalingType totalingType;

    /** 売上フラグ（必須） */
    private HTypeSalesFlag salesFlag = HTypeSalesFlag.OFF;

    /** 処理日時（必須） */
    private Timestamp processTime;

    /** 商品グループコード（必須） */
    private String goodsGroupCode;

    /** 商品コード（必須） */
    private String goodsCode;

    /** JANコード */
    private String janCode;

    /** 商品グループ名 */
    private String goodsGroupName;

    /** 商品消費税種別（必須） */
    private HTypeGoodsTaxType goodsTaxType = HTypeGoodsTaxType.OUT_TAX;

    /** 税率（必須） */
    private BigDecimal taxRate;

    /** 商品単価（必須） */
    private BigDecimal goodsPrice = BigDecimal.ZERO;

    /** 前回商品数量 */
    private BigDecimal preGoodsCount = BigDecimal.ZERO;

    /** 商品数量（必須） */
    private BigDecimal goodsCount = BigDecimal.ZERO;

    /** 無料配送フラグ（必須） */
    private HTypeFreeDeliveryFlag freeDeliveryFlag = HTypeFreeDeliveryFlag.OFF;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /** 商品個別配送種別（必須） */
    private HTypeIndividualDeliveryType individualDeliveryType = HTypeIndividualDeliveryType.OFF;

    /** 商品納期 */
    private String deliveryType;

    /** 受注連携設定１ */
    private String orderSetting1;

    /** 受注連携設定２ */
    private String orderSetting2;

    /** 受注連携設定３ */
    private String orderSetting3;

    /** 受注連携設定４ */
    private String orderSetting4;

    /** 受注連携設定５ */
    private String orderSetting5;

    /** 受注連携設定６ */
    private String orderSetting6;

    /** 受注連携設定７ */
    private String orderSetting7;

    /** 受注連携設定８ */
    private String orderSetting8;

    /** 受注連携設定９ */
    private String orderSetting9;

    /** 受注連携設定１０ */
    private String orderSetting10;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}