/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品管理：商品登録更新（商品在庫設定）ページ情報<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsRegistUpdateStockItem implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** No */
    private Integer stockDspNo;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** 商品コード */
    private String goodsCode;

    /** JANコード */
    private String janCode;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /** 販売状態PC */
    private String goodsSaleStatusPC;

    /** 残少表示在庫数 */
    @HVNumber(groups = {ConfirmGroup.class})
    @Digits(integer = 6, fraction = 0, groups = {ConfirmGroup.class})
    @HCNumber
    private String remainderFewStock;

    /**
     * 発注点在庫数<br/>
     */
    @HVNumber(groups = {ConfirmGroup.class})
    @Digits(integer = 6, fraction = 0, groups = {ConfirmGroup.class})
    @HCNumber
    private String orderPointStock;

    /**
     * 安全在庫数<br/>
     */
    @HVNumber(groups = {ConfirmGroup.class})
    @Digits(integer = 6, fraction = 0, groups = {ConfirmGroup.class})
    @HCNumber
    private String safetyStock;

    /**
     * 実在庫数<br/>
     */
    private BigDecimal realStock;

    /**
     * 入庫数<br/>
     */
    @HVNumber(minus = true, groups = {ConfirmGroup.class})
    @Digits(integer = 6, fraction = 0, groups = {ConfirmGroup.class})
    @HCNumber
    private String supplementCount = "0";
}
