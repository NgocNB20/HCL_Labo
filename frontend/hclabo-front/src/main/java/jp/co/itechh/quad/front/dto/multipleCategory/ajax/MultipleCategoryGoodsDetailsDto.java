/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.dto.multipleCategory.ajax;

import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Multiple category goods data
 *
 * @author Shalaka kale
 *
 */
@Data
@Component
@Scope("prototype")
public class MultipleCategoryGoodsDetailsDto implements Serializable {

    /** serialVersionUID<br/> */
    private static final long serialVersionUID = 1L;
    /** 商品グループSEQ */
    private Integer goodsGroupSeq;
    /** 商品グループコード */
    private String ggcd;
    /** 商品グループコード */
    private String href;
    /** 商品名 */
    private String goodsGroupName;
    /** 商品グループサムネイル画像 */
    private String goodsGroupImageThumbnail;
    /** 商品画像アイテム */
    private String goodsImageItem;
    /** 商品単価 */
    private BigDecimal goodsPrice;
    /** 商品単価(税込) */
    private BigDecimal goodsPriceInTax;
    /** 商品説明1 */
    private String goodsNote1;

    /** 税率 */
    private BigDecimal taxRate;
    /** 商品消費税種別（必須） */
    private HTypeGoodsTaxType goodsTaxType = HTypeGoodsTaxType.OUT_TAX;

    /** 新着日付 */
    private Timestamp whatsnewDate;

    /** 在庫状況表示 */
    private String stockStatusPc;

    // アイコン
    /** 商品アイコンリスト*/
    private List<MultipleCategoryGoodsDetailsDto> goodsIconItems;
    /** 商品アイコン名*/
    private String iconName;
    /** 商品アイコンカラーコード*/
    private String iconColorCode;

    /** isNewDate */
    private boolean isNewDate;
    /** isStockStatusDisplay */
    private boolean isStockStatusDisplay;
    /** isStockNoSaleDisp */
    private boolean isStockNoSaleDisp;
    /** isStockSoldOutIconDisp */
    private boolean isStockSoldOutIconDisp;
    /** isStockBeforeSaleIconDisp */
    private boolean isStockBeforeSaleIconDisp;
    /** isStockNoStockIconDisp */
    private boolean isStockNoStockIconDisp;
    /** isStockFewIconDisp */
    private boolean isStockFewIconDisp;
    /** isStockPossibleSalesIconDisp */
    private boolean isStockPossibleSalesIconDisp;
    /** isGoodsGroupImage */
    private boolean isGoodsGroupImage;
}