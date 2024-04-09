/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.order.settlement;

import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeCarrierType;
import jp.co.itechh.quad.core.constant.type.HTypeDeviceType;
import jp.co.itechh.quad.core.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.core.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.core.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.core.constant.type.HTypeRepeatPurchaseType;
import jp.co.itechh.quad.core.constant.type.HTypeSalesFlag;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
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
 * 受注決済クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "OrderSettlement")
@Data
@Component
@Scope("prototype")
public class OrderSettlementEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    @Column(name = "orderSeq")
    @Id
    private Integer orderSeq;

    /** 受注決済連番（必須） */
    @Column(name = "orderSettlementVersionNo")
    @Id
    private Integer orderSettlementVersionNo;

    /** 受注コード（必須） */
    @Column(name = "orderCode")
    private String orderCode;

    /** 処理日時（必須） */
    @Column(name = "processTime")
    private Timestamp processTime;

    /** 処理年月日（必須） */
    @Column(name = "processYmd")
    private String processYmd;

    /** 処理年月（必須） */
    @Column(name = "processYm")
    private String processYm;

    /** 受注日（必須） */
    @Column(name = "orderDate")
    private Timestamp orderDate;

    /** 売上日時 */
    @Column(name = "salesTime")
    private Timestamp salesTime;

    /** 受注サイト種別（必須） */
    @Column(name = "orderSiteType")
    private HTypeSiteType orderSiteType = HTypeSiteType.FRONT_PC;

    /** 受注デバイス種別（必須） */
    @Column(name = "orderDeviceType")
    private HTypeDeviceType orderDeviceType = HTypeDeviceType.PC;

    /** キャリア種別（必須） */
    @Column(name = "carrierType")
    private HTypeCarrierType carrierType = HTypeCarrierType.PC;

    /** 決済方法SEQ（必須） */
    @Column(name = "settlementMethodSeq")
    private Integer settlementMethodSeq;

    /** 決済方法種別（必須） */
    @Column(name = "settlementMethodType")
    private HTypeSettlementMethodType settlementMethodType;

    /** クレジットカード会社コード */
    @Column(name = "creditCompanyCode")
    private String creditCompanyCode;

    /** クレジットカード会社 */
    @Column(name = "creditCompany")
    private String creditCompany;

    /** 集計種別（必須） */
    @Column(name = "totalingType")
    private HTypeTotalingType totalingType;

    /** 売上フラグ（必須） */
    @Column(name = "salesFlag")
    private HTypeSalesFlag salesFlag = HTypeSalesFlag.OFF;

    /** 請求種別（必須） */
    @Column(name = "billType")
    private HTypeBillType billType;

    /** リピート購入種別（必須） */
    @Column(name = "repeatPurchaseType")
    private HTypeRepeatPurchaseType repeatPurchaseType = HTypeRepeatPurchaseType.GUEST;

    /** 都道府県種別 */
    @Column(name = "prefectureType")
    private HTypePrefectureType prefectureType;

    /** ご注文主性別 */
    @Column(name = "orderSex")
    private HTypeOrderSex orderSex = HTypeOrderSex.UNKNOWN;

    /** ご注文主年代 */
    @Column(name = "orderAgeType")
    private HTypeOrderAgeType orderAgeType;

    /** 割引前受注金額（必須） */
    @Column(name = "beforeDiscountOrderPrice")
    private BigDecimal beforeDiscountOrderPrice = BigDecimal.ZERO;

    /** 受注金額（必須） */
    @Column(name = "orderPrice")
    private BigDecimal orderPrice = BigDecimal.ZERO;

    /** 消費税（必須） */
    @Column(name = "taxPrice")
    private BigDecimal taxPrice = BigDecimal.ZERO;

    /** 標準税率消費税（必須） */
    @Column(name = "standardTaxPrice")
    private BigDecimal standardTaxPrice = BigDecimal.ZERO;

    /** 軽減税率消費税（必須） */
    @Column(name = "reducedTaxPrice")
    private BigDecimal reducedTaxPrice = BigDecimal.ZERO;

    /** 標準税率対象金額（必須） */
    @Column(name = "standardTaxTargetPrice")
    private BigDecimal standardTaxTargetPrice = BigDecimal.ZERO;

    /** 軽減税率対象金額（必須） */
    @Column(name = "reducedTaxTargetPrice")
    private BigDecimal reducedTaxTargetPrice = BigDecimal.ZERO;

    /** 商品金額合計(税抜き)（必須） */
    @Column(name = "goodsPriceTotal")
    private BigDecimal goodsPriceTotal = BigDecimal.ZERO;

    /** 商品原価合計（必須） */
    @Column(name = "goodsCostTotal")
    private BigDecimal goodsCostTotal = BigDecimal.ZERO;

    /** 決済手数料（必須） */
    @Column(name = "settlementCommission")
    private BigDecimal settlementCommission = BigDecimal.ZERO;

    /** 送料 */
    @Column(name = "carriage")
    private BigDecimal carriage = BigDecimal.ZERO;

    /** その他金額合計 */
    @Column(name = "othersPriceTotal")
    private BigDecimal othersPriceTotal = BigDecimal.ZERO;

    /** クーポン割引額 */
    @Column(name = "couponDiscountPrice")
    private BigDecimal couponDiscountPrice = BigDecimal.ZERO;

    /** 前回割引前受注金額（必須） */
    @Column(name = "preBeforeDiscountOrderPrice")
    private BigDecimal preBeforeDiscountOrderPrice = BigDecimal.ZERO;

    /** 前回受注金額（必須） */
    @Column(name = "preOrderPrice")
    private BigDecimal preOrderPrice = BigDecimal.ZERO;

    /** 前回消費税（必須） */
    @Column(name = "preTaxPrice")
    private BigDecimal preTaxPrice = BigDecimal.ZERO;

    /** 前回商品金額合計 */
    @Column(name = "preGoodsPriceTotal")
    private BigDecimal preGoodsPriceTotal = BigDecimal.ZERO;

    /** 前回商品原価合計 */
    @Column(name = "preGoodsCostTotal")
    private BigDecimal preGoodsCostTotal = BigDecimal.ZERO;

    /** 前回決済手数料 */
    @Column(name = "preSettlementCommission")
    private BigDecimal preSettlementCommission = BigDecimal.ZERO;

    /** 前回送料 */
    @Column(name = "preCarriage")
    private BigDecimal preCarriage = BigDecimal.ZERO;

    /** 前回その他金額合計 */
    @Column(name = "preOthersPriceTotal")
    private BigDecimal preOthersPriceTotal = BigDecimal.ZERO;

    /** 前回クーポン割引額 */
    @Column(name = "preCouponDiscountPrice")
    private BigDecimal preCouponDiscountPrice = BigDecimal.ZERO;

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