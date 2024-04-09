/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金額計算のUtilityクラス
 *
 */
@Component
public class CalculatePriceUtility {

    /** 税率算出用の定数 100 */
    protected static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    /** 税金算出時のROUNDINGモード（切り上げ、切り捨て）の定数 */
    public static final RoundingMode DEFAULT_TAX_ROUNDING_MODE = RoundingMode.UP;

    /**
     * 消費税額を算出(丸めモードを指定)
     *
     * @param price 金額(税抜)
     * @param taxRate 消費税率(10, 8)
     * @param mode 丸めモード
     * @return 消費税額
     */
    protected BigDecimal getTaxPrice(BigDecimal price, BigDecimal taxRate, RoundingMode mode) {

        // 税率を算出
        BigDecimal rate = taxRate.divide(ONE_HUNDRED);
        // 金額(税込)を計算
        BigDecimal resultPrice = price.multiply(rate);
        // 端数切り上げ
        resultPrice = resultPrice.setScale(0, mode);

        return resultPrice;
    }

    /**
     * 税込金額を算出
     *
     * @param price 金額(税抜)
     * @param taxRate 消費税率（％）
     * @return 金額(税込)
     */
    public BigDecimal getTaxIncludedPrice(BigDecimal price, BigDecimal taxRate) {
        if (price == null) {
            return null;
        }

        return price.add(getTaxPrice(price, taxRate, DEFAULT_TAX_ROUNDING_MODE));
    }
}
