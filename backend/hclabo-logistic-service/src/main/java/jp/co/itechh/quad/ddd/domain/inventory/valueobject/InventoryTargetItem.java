/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.inventory.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 在庫対象商品 値オブジェクト
 */
@Getter
public class InventoryTargetItem {

    /** 商品ID（商品サービスの商品SEQ） */
    private String itemId;

    /** 商品数量 */
    private ItemCount itemCount;

    /**
     * コンストラクタ
     *
     * @param itemId    商品ID
     * @param itemCount 商品数量
     */
    public InventoryTargetItem(String itemId, ItemCount itemCount) {

        // チェック
        AssertChecker.assertNotEmpty("itemId is empty", itemId);
        // 設定
        this.itemId = itemId;
        this.itemCount = itemCount;
    }
}
