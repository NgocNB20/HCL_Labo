/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 商品購入価格小計 値オブジェクト
 */
@Getter
public class ItemPurchasePriceSubTotal {

    /** 販売商品連番 */
    private final Integer salesItemSeq;

    /** 商品購入価格小計 */
    private final int itemPurchasePriceSubTotal;

    /** 商品ID（商品SEQ） */
    private final String itemId;

    /** 商品単価 */
    private final int itemUnitPrice;

    /** 商品数量 */
    private final int itemCount;

    /** 商品税率 */
    private final BigDecimal itemTaxRate;

    /** 送料無料商品フラグ */
    private final boolean freeCarriageItemFlag;

    /** コンストラクタ */
    public ItemPurchasePriceSubTotal(Integer salesItemSeq,
                                     int itemPurchasePriceSubTotal,
                                     String itemId,
                                     int itemUnitPrice,
                                     int itemCount,
                                     BigDecimal itemTaxRate,
                                     boolean freeCarriageFlag) {

        // チェック
        AssertChecker.assertIntegerNotNegative("itemPurchasePriceSubTotal is Exist", itemPurchasePriceSubTotal);
        AssertChecker.assertIntegerNotNegative("itemUnitPrice is Exist", itemUnitPrice);
        AssertChecker.assertIntegerNotNegative("itemCount is Exist", itemCount);
        AssertChecker.assertNotNull("itemTaxRate is Exist", itemTaxRate);

        // 設定
        this.salesItemSeq = salesItemSeq;
        this.itemPurchasePriceSubTotal = itemPurchasePriceSubTotal;
        this.itemId = itemId;
        this.itemUnitPrice = itemUnitPrice;
        this.itemCount = itemCount;
        this.itemTaxRate = itemTaxRate;
        this.freeCarriageItemFlag = freeCarriageFlag;
    }
}