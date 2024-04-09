/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.AssertException;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 商品販売金額合計 値オブジェクト
 * ※ファクトリあり
 */
@Getter
public class ItemSalesPriceTotal {

    /** 商品販売金額小計リスト */
    private final List<ItemSalesPriceSubTotal> itemSalesPriceSubTotalList;

    /**
     * ファクトリ用コンストラクタ
     * ※パッケージプライベート
     *
     * @param itemSalesPriceSubTotalList 商品販売金額小計リスト
     */
    ItemSalesPriceTotal(List<ItemSalesPriceSubTotal> itemSalesPriceSubTotalList) {

        // チェック
        AssertChecker.assertNotNull("itemSalesPriceSubTotalList is null", itemSalesPriceSubTotalList);

        this.itemSalesPriceSubTotalList = itemSalesPriceSubTotalList;

        // インスタンス生成が不正かどうかチェックする
        getItemPriceTotal();
        getItemPriceTotalInTax();
    }

    /**
     * 商品金額合計取得
     *
     * @return 商品金額合計
     */
    public int getItemPriceTotal() {

        long itemPriceTotal = 0;

        if (!CollectionUtils.isEmpty(itemSalesPriceSubTotalList)) {
            for (ItemSalesPriceSubTotal itemSalesPriceSubTotal : itemSalesPriceSubTotalList) {
                itemPriceTotal += itemSalesPriceSubTotal.getItemSalesPriceSubTotal();
            }
        }

        if (itemPriceTotal < 0 || itemPriceTotal > Integer.MAX_VALUE) {
            throw new AssertException("不正を検知しました。一定期間経過後にアクセスください。");
        }

        return (int) itemPriceTotal;
    }

    /**
     * 税込商品金額合計取得
     *
     * @return 税込商品金額合計
     */
    public int getItemPriceTotalInTax() {

        long itemPriceTotalInTax = 0;

        if (!CollectionUtils.isEmpty(itemSalesPriceSubTotalList)) {
            for (ItemSalesPriceSubTotal itemSalesPriceSubTotal : itemSalesPriceSubTotalList) {
                itemPriceTotalInTax += itemSalesPriceSubTotal.getItemSalesPriceSubTotalInTax();
            }
        }
        if (itemPriceTotalInTax < 0 || itemPriceTotalInTax > Integer.MAX_VALUE) {
            throw new AssertException("不正を検知しました。一定期間経過後にアクセスください。");
        }

        return (int) itemPriceTotalInTax;
    }

    /**
     * 標準税率商品金額取得
     *
     * @return 標準税率商品金額
     */
    public int getStandardTaxItemPrice() {

        int standardTaxItemPriceTotal = 0;

        if (!CollectionUtils.isEmpty(itemSalesPriceSubTotalList)) {
            for (ItemSalesPriceSubTotal itemSalesPriceSubTotal : itemSalesPriceSubTotalList) {
                // 商品が標準税率か判定
                if (SalesPriceConsumptionTax.STANDARD_TAX_RATE.compareTo(
                                itemSalesPriceSubTotal.getOrderItem().getTaxRate()) == 0) {
                    standardTaxItemPriceTotal += itemSalesPriceSubTotal.getItemSalesPriceSubTotal();
                }
            }
        }

        return standardTaxItemPriceTotal;
    }

    /**
     * 軽減税率商品金額取得
     *
     * @return 軽減税率商品金額
     */
    public int getReducedTaxItemPrice() {

        int reducedTaxItemPrice = 0;

        if (!CollectionUtils.isEmpty(itemSalesPriceSubTotalList)) {
            for (ItemSalesPriceSubTotal itemSalesPriceSubTotal : itemSalesPriceSubTotalList) {
                // 商品が軽減税率か判定
                if (SalesPriceConsumptionTax.REDUCED_TAX_RATE.compareTo(
                                itemSalesPriceSubTotal.getOrderItem().getTaxRate()) == 0) {
                    reducedTaxItemPrice += itemSalesPriceSubTotal.getItemSalesPriceSubTotal();
                }
            }
        }

        return reducedTaxItemPrice;
    }

    /**
     * 商品販売金額小計リスト getter
     *
     * @return 商品販売金額小計リスト ※unmodifiableList
     */
    public List<ItemSalesPriceSubTotal> getItemSalesPriceSubTotalList() {
        return Collections.unmodifiableList(this.itemSalesPriceSubTotalList);
    }

}
