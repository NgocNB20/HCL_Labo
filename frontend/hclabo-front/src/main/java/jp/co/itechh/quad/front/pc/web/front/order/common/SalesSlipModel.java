/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order.common;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 販売伝票Model
 * @author Pham Quang Dieu
 */
@Data
public class SalesSlipModel extends AbstractModel {

    /**  販売伝票 */
    public static final String ATTRIBUTE_NAME_KEY = "salesSlip";

    /** 商品金額合計 */
    private BigDecimal itemSalesPriceTotal;

    /** 合計商品数量 */
    private BigDecimal totalOrderGoodsCount;

    /** 送料 */
    private BigDecimal carriage;

    /** 手数料 */
    private BigDecimal commission;

    /** 決済手数料 */
    private BigDecimal charge;

    /** 消費税 */
    private BigDecimal taxPrice;

    /** 標準税率消費税 */
    private BigDecimal standardTax;

    /** 軽減税率消費税 */
    private BigDecimal reducedTax;

    /** 標準税率対象金額 */
    private BigDecimal standardTaxTargetPrice;

    /** 軽減税率対象金額 */
    private BigDecimal reducedTaxTargetPrice;

    /** クーポン名 */
    private String couponName;

    /** クーポン割引額 */
    private BigDecimal couponPaymentPrice;

    /** 支払い合計金額 */
    private BigDecimal billingAmount;

    /**
     * クーポン割引情報の表示／非表示判定処理。<br />
     * 注文情報エリアにクーポン割引情報を表示するかどうか判定する為に利用する。
     * @return クーポン割引額が１円以上の場合 true
     */
    public boolean isDisplayCouponDiscount() {
        return couponPaymentPrice != null && couponPaymentPrice.compareTo(BigDecimal.ZERO) != 0;
    }
}