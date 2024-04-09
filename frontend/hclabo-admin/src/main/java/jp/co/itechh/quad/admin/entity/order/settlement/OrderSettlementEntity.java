/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order.settlement;

import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeCarrierType;
import jp.co.itechh.quad.admin.constant.type.HTypeDeviceType;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeRepeatPurchaseType;
import jp.co.itechh.quad.admin.constant.type.HTypeSalesFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.constant.type.HTypeTotalingType;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class OrderSettlementEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    private Integer orderSeq;

    /** 受注決済連番（必須） */
    private Integer orderSettlementVersionNo;

    /** 受注コード（必須） */
    private String orderCode;

    /** 処理日時（必須） */
    private Timestamp processTime;

    /** 処理年月日（必須） */
    private String processYmd;

    /** 処理年月（必須） */
    private String processYm;

    /** 受注日（必須） */
    private Timestamp orderDate;

    /** 売上日時 */
    private Timestamp salesTime;

    /** 受注サイト種別（必須） */
    private HTypeSiteType orderSiteType = HTypeSiteType.FRONT_PC;

    /** 受注デバイス種別（必須） */
    private HTypeDeviceType orderDeviceType = HTypeDeviceType.PC;

    /** キャリア種別（必須） */
    private HTypeCarrierType carrierType = HTypeCarrierType.PC;

    /** 決済方法SEQ（必須） */
    private Integer settlementMethodSeq;

    /** 決済方法種別（必須） */
    private HTypeSettlementMethodType settlementMethodType;

    /** クレジットカード会社コード */
    private String creditCompanyCode;

    /** クレジットカード会社 */
    private String creditCompany;

    /** 集計種別（必須） */
    private HTypeTotalingType totalingType;

    /** 売上フラグ（必須） */
    private HTypeSalesFlag salesFlag = HTypeSalesFlag.OFF;

    /** 請求種別（必須） */
    private HTypeBillType billType;

    /** リピート購入種別（必須） */
    private HTypeRepeatPurchaseType repeatPurchaseType = HTypeRepeatPurchaseType.GUEST;

    /** 都道府県種別 */
    private HTypePrefectureType prefectureType;

    /** ご注文主性別 */
    private HTypeOrderSex orderSex = HTypeOrderSex.UNKNOWN;

    /** ご注文主年代 */
    private HTypeOrderAgeType orderAgeType;

    /** 割引前受注金額（必須） */
    private BigDecimal beforeDiscountOrderPrice = BigDecimal.ZERO;

    /** 受注金額（必須） */
    private BigDecimal orderPrice = BigDecimal.ZERO;

    /** 消費税（必須） */
    private BigDecimal taxPrice = BigDecimal.ZERO;

    /** 標準税率消費税（必須） */
    private BigDecimal standardTaxPrice = BigDecimal.ZERO;

    /** 軽減税率消費税（必須） */
    private BigDecimal reducedTaxPrice = BigDecimal.ZERO;

    /** 標準税率対象金額（必須） */
    private BigDecimal standardTaxTargetPrice = BigDecimal.ZERO;

    /** 軽減税率対象金額（必須） */
    private BigDecimal reducedTaxTargetPrice = BigDecimal.ZERO;

    /** 商品金額合計(税抜き)（必須） */
    private BigDecimal goodsPriceTotal = BigDecimal.ZERO;

    /** 商品原価合計（必須） */
    private BigDecimal goodsCostTotal = BigDecimal.ZERO;

    /** 決済手数料（必須） */
    private BigDecimal settlementCommission = BigDecimal.ZERO;

    /** 送料 */
    private BigDecimal carriage = BigDecimal.ZERO;

    /** その他金額合計 */
    private BigDecimal othersPriceTotal = BigDecimal.ZERO;

    /** クーポン割引額 */
    private BigDecimal couponDiscountPrice = BigDecimal.ZERO;

    /** 前回割引前受注金額（必須） */
    private BigDecimal preBeforeDiscountOrderPrice = BigDecimal.ZERO;

    /** 前回受注金額（必須） */
    private BigDecimal preOrderPrice = BigDecimal.ZERO;

    /** 前回消費税（必須） */
    private BigDecimal preTaxPrice = BigDecimal.ZERO;

    /** 前回商品金額合計 */
    private BigDecimal preGoodsPriceTotal = BigDecimal.ZERO;

    /** 前回商品原価合計 */
    private BigDecimal preGoodsCostTotal = BigDecimal.ZERO;

    /** 前回決済手数料 */
    private BigDecimal preSettlementCommission = BigDecimal.ZERO;

    /** 前回送料 */
    private BigDecimal preCarriage = BigDecimal.ZERO;

    /** 前回その他金額合計 */
    private BigDecimal preOthersPriceTotal = BigDecimal.ZERO;

    /** 前回クーポン割引額 */
    private BigDecimal preCouponDiscountPrice = BigDecimal.ZERO;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

}