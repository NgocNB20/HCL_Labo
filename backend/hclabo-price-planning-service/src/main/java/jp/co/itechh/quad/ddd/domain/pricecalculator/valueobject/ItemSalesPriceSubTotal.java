/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 商品販売金額小計 値オブジェクト
 */
@Getter
public class ItemSalesPriceSubTotal {

    /** 注文商品連番 */
    private final Integer orderItemSeq;

    /** 注文商品 */
    private final Item orderItem;

    /** 注文商注文品数量 */
    private final int orderItemCount;

    /** コンストラクタ */
    public ItemSalesPriceSubTotal(Integer orderItemSeq, Item item, int itemCount) {

        // チェック
        AssertChecker.assertNotNull("item is null", item);

        this.orderItemSeq = orderItemSeq;
        this.orderItem = item;
        this.orderItemCount = itemCount;
    }

    /**
     * 商品販売金額小計取得
     *
     * @return 商品販売金額小計
     */
    public int getItemSalesPriceSubTotal() {
        return this.orderItem.getItemUnitPrice() * this.orderItemCount;
    }

    /**
     * 税込商品販売金額小計取得
     *
     * @return 税込商品販売金額小計
     */
    public int getItemSalesPriceSubTotalInTax() {
        return this.orderItem.getItemUnitPriceInTax() * this.orderItemCount;
    }

}
