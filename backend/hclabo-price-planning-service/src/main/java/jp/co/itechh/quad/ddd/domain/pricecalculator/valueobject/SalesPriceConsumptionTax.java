/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 販売金額消費税 値オブジェクト
 */
@Getter
public class SalesPriceConsumptionTax {

    /** 標準税率対象金額 */
    private final int standardTaxTargetPrice;

    /** 軽減税率対象金額 */
    private final int reducedTaxTargetPrice;

    /** 標準税額 */
    private final int standardTax;

    /** 軽減税額 */
    private final int reducedTax;

    /** 標準税率 */
    public static final BigDecimal STANDARD_TAX_RATE = BigDecimal.valueOf(10);

    /** 軽減税率 */
    public static final BigDecimal REDUCED_TAX_RATE = BigDecimal.valueOf(8);

    /** 消費税端数処理モード（切り捨て） */
    public static final RoundingMode ROUNDING_MODE = RoundingMode.DOWN;

    /**
     * 算出コンストラクタ
     *
     * @param itemSalesPriceTotal
     * @param carriage
     * @param commission
     * @param adjustmentAmountList
     */
    public SalesPriceConsumptionTax(ItemSalesPriceTotal itemSalesPriceTotal,
                                    Carriage carriage,
                                    Commission commission,
                                    List<AdjustmentAmount> adjustmentAmountList) {

        // チェック
        AssertChecker.assertNotNull("SalesPriceConsumptionTax : itemSalesPriceTotal is null", itemSalesPriceTotal);
        AssertChecker.assertNotNull("SalesPriceConsumptionTax : carriage is null", carriage);

        // 決済手数料
        int commissionValue = 0;
        if (commission != null) {
            commissionValue = commission.getValue();
        }

        // 調整金額合計
        int adjustmentPriceTotal = 0;
        if (!CollectionUtils.isEmpty(adjustmentAmountList)) {
            for (AdjustmentAmount adjustmentAmount : adjustmentAmountList) {
                adjustmentPriceTotal += adjustmentAmount.getAdjustPrice();
            }
        }

        // 標準税率対象金額
        this.standardTaxTargetPrice =
                        itemSalesPriceTotal.getStandardTaxItemPrice() + carriage.getValue() + commissionValue
                        + adjustmentPriceTotal;

        // 軽減税率対象金額
        this.reducedTaxTargetPrice = itemSalesPriceTotal.getReducedTaxItemPrice();

        // 税額算出
        BigDecimal standardTaxTmp = BigDecimal.valueOf(standardTaxTargetPrice)
                                              .multiply(STANDARD_TAX_RATE.divide(BigDecimal.valueOf(100)));
        BigDecimal reducedTaxTmp = BigDecimal.valueOf(reducedTaxTargetPrice)
                                             .multiply(REDUCED_TAX_RATE.divide(BigDecimal.valueOf(100)));

        // 税額端数切り捨て
        this.standardTax = standardTaxTmp.setScale(0, ROUNDING_MODE).intValue();
        this.reducedTax = reducedTaxTmp.setScale(0, ROUNDING_MODE).intValue();
    }

    /**
     * 消費税額取得
     *
     * @return 消費税額
     */
    public int getSalesPriceConsumptionTax() {
        return this.standardTax + this.reducedTax;
    }

    public SalesPriceConsumptionTax(int standardTaxTargetPrice,
                                    int reducedTaxTargetPrice,
                                    int standardTax,
                                    int reducedTax) {
        this.standardTaxTargetPrice = standardTaxTargetPrice;
        this.reducedTaxTargetPrice = reducedTaxTargetPrice;
        this.standardTax = standardTax;
        this.reducedTax = reducedTax;
    }
}