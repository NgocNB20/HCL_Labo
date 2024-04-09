/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.AssertException;
import lombok.Getter;

/**
 * 請求金額 値オブジェクト
 */
@Getter
public class BillingAmount {

    /** 値 */
    private final int value;

    /**
     * コンストラクタ
     *
     * @param couponPaymentPrice クーポン支払い額
     * @param salesPriceConsumptionTax 販売金額消費税
     */
    public BillingAmount(CouponPaymentPrice couponPaymentPrice, SalesPriceConsumptionTax salesPriceConsumptionTax) {

        // チェック
        AssertChecker.assertNotNull("BillingAmount : couponPaymentPrice is null", couponPaymentPrice);
        AssertChecker.assertNotNull("BillingAmount : salesPriceConsumptionTax is null", salesPriceConsumptionTax);

        long billingAmount = ((long) salesPriceConsumptionTax.getStandardTaxTargetPrice())
                             + salesPriceConsumptionTax.getReducedTaxTargetPrice()
                             + salesPriceConsumptionTax.getSalesPriceConsumptionTax() - couponPaymentPrice.getValue();

        try {
            this.value = Math.toIntExact(billingAmount);
        } catch (ArithmeticException e) {
            throw new AssertException("不正を検知しました。一定期間経過後にアクセスください。");
        }
    }

}
