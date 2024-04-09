/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品管理：商品詳細ページ 商品在庫アイテム<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsDetailsStockItem implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /************************************
     ** 商品規格項目
     ************************************/
    /**
     * No<br/>
     */
    private Integer stockDspNo;

    /**
     * 商品SEQ<br/>
     */
    private Integer goodsSeq;

    /**
     * 商品コード<br/>
     */
    private String goodsCode;

    /**
     * JANコード<br/>
     */
    private String janCode;

    /**
     * 規格値１<br/>
     */
    private String unitValue1;

    /**
     * 規格値２<br/>
     */
    private String unitValue2;

    /**
     * 販売状態PC<br/>
     */
    private String goodsSaleStatusPC;

    /**
     * 残少表示在庫数<br/>
     */
    private BigDecimal remainderFewStock;

    /**
     * 発注点在庫数<br/>
     */
    private BigDecimal orderPointStock;

    /**
     * 安全在庫数<br/>
     */
    private BigDecimal safetyStock;

    /**
     * 実在庫数<br/>
     */
    private BigDecimal realStock;

    /**
     * 販売可能在庫数<br/>
     */
    private BigDecimal salesPossibleStock;

    /**
     * 受注確保在庫数<br/>
     */
    private BigDecimal orderReserveStock;
}