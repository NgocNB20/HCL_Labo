/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 確保済み配送商品 値オブジェクト
 */
@Getter
public class SecuredShippingItem {

    /** 配送商品連番（注文商品連番） */
    private final int shippingItemSeq;

    /** 商品ID（商品サービスの商品SEQ） */
    private final String itemId;

    /** 商品名 */
    private final String itemName;

    /** 規格タイトル1 */
    private final String unitTitle1;

    /** 規格値1 */
    private final String unitValue1;

    /** 規格タイトル2 */
    private final String unitTitle2;

    /** 規格値2 */
    private final String unitValue2;

    /** 配送数量 */
    private final ShippingCount shippingCount;

    /**
     * コンストラクタ
     *
     * @param shippingItemSeq 配送商品連番
     * @param itemId          商品ID
     * @param itemName        商品名
     * @param unitTitle1      規格タイトル1
     * @param unitValue1      規格値1
     * @param unitTitle2      規格タイトル2
     * @param unitValue2      規格値2
     * @param shippingCount   配送数量
     */
    public SecuredShippingItem(int shippingItemSeq,
                               String itemId,
                               String itemName,
                               String unitTitle1,
                               String unitValue1,
                               String unitTitle2,
                               String unitValue2,
                               ShippingCount shippingCount) {

        // チェック
        AssertChecker.assertNotEmpty("itemId is null", itemId);
        AssertChecker.assertNotEmpty("itemName is null", itemName);
        // 規格1情報が未設定だが、規格2情報が設定されている場合はエラー
        if ((StringUtils.isBlank(unitTitle1) && StringUtils.isNotBlank(unitTitle2)) || (StringUtils.isBlank(unitValue1)
                                                                                        && StringUtils.isNotBlank(
                        unitValue2))) {
            throw new DomainException("LOGISTIC-SHIP0003-E");
        }
        // 規格のタイトル＋値がセットで設定されていない場合はエラー
        if ((StringUtils.isBlank(unitTitle1) && StringUtils.isNotBlank(unitValue1)) || (
                        StringUtils.isNotBlank(unitTitle1) && StringUtils.isBlank(unitValue1))) {
            throw new DomainException("LOGISTIC-SHIP0004-E");
        }
        if ((StringUtils.isBlank(unitTitle2) && StringUtils.isNotBlank(unitValue2)) || (
                        StringUtils.isNotBlank(unitTitle2) && StringUtils.isBlank(unitValue2))) {
            throw new DomainException("LOGISTIC-SHIP0004-E");
        }

        // 設定
        this.shippingItemSeq = shippingItemSeq;
        this.itemId = itemId;
        this.itemName = itemName;
        this.unitTitle1 = unitTitle1;
        this.unitValue1 = unitValue1;
        this.unitTitle2 = unitTitle2;
        this.unitValue2 = unitValue2;
        this.shippingCount = shippingCount;
    }

}