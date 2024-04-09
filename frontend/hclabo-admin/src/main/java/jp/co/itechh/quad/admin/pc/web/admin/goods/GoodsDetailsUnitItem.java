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
import java.sql.Timestamp;

/**
 * 商品管理：商品詳細ページ 商品規格アイテム<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsDetailsUnitItem implements Serializable {

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
    private Integer unitDspNo;

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
     * 販売開始年月日PC<br/>
     */
    private String saleStartDatePC;

    /**
     * 販売開始時刻PC<br/>
     */
    private String saleStartTimePC;

    /**
     * 販売開始日時PC<br/>
     */
    private Timestamp saleStartDateTimePC;

    /**
     * 販売終了年月日PC<br/>
     */
    private String saleEndDatePC;

    /**
     * 販売終了時刻PC<br/>
     */
    private String saleEndTimePC;

    /**
     * 販売終了日時PC<br/>
     */
    private Timestamp saleEndDateTimePC;

    /**
     * 最大購入可能数<br/>
     */
    private BigDecimal purchasedMax;

    /**
     * @return saleDateTimeExistPC
     */
    public boolean isSaleDateTimePCExist() {
        if (this.saleStartDateTimePC != null || this.saleEndDateTimePC != null) {
            return true;
        }
        return false;
    }

}
