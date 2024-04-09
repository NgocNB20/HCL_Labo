/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 商品 値オブジェクト
 */
@Getter
public class Item {

    /** 商品ID（商品SEQ） */
    private final String itemId;

    /** 商品コード */
    private final String itemCode;

    /** 商品単価 */
    private final int itemUnitPrice;

    /** 税率 */
    private final BigDecimal taxRate;

    /** 送料無料商品フラグ */
    private final boolean freeCarriageItemFlag;

    // 定数------------------------------------------------
    /** 税金算出時のROUNDINGモード（切り上げ、切り捨て）の定数 */
    public static final RoundingMode DEFAULT_TAX_ROUNDING_MODE = RoundingMode.UP;

    /**
     * コンストラクタ
     *
     * @param itemId
     * @param itemUnitPrice
     * @param taxRate
     * @param freeDeliveryFlag
     * @param customParam
     */
    public Item(String itemId, int itemUnitPrice, BigDecimal taxRate, boolean freeDeliveryFlag, String... customParam) {
        this.itemId = itemId;
        this.itemUnitPrice = itemUnitPrice;
        this.taxRate = taxRate;
        this.freeCarriageItemFlag = freeDeliveryFlag;
        this.itemCode = customParam.length > 0 ? customParam[0] : null;
    }

    /**
     * 税込商品単価取得
     *
     * @return 税込商品単価
     */
    public int getItemUnitPriceInTax() {

        // 税額を計算
        BigDecimal resultPrice = BigDecimal.valueOf(itemUnitPrice).multiply(taxRate.divide(BigDecimal.valueOf(100)));

        // 税額端数切り上げて商品単価に加算
        return itemUnitPrice + resultPrice.setScale(0, DEFAULT_TAX_ROUNDING_MODE).intValue();
    }

}
