/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.goods.stock;

import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 入庫実績クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class StockResultEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 入庫実績SEQ（必須） */
    private Integer stockResultSeq;

    /** 商品SEQ (FK)（必須） */
    private Integer goodsSeq;

    /** 入庫日時（必須） */
    private Timestamp supplementTime;

    /** 入庫数（必須） */
    private BigDecimal supplementCount;

    /** 実在庫数（必須） */
    private BigDecimal realStock;

    /** 処理担当者名 */
    private String processPersonName;

    /** 備考 */
    private String note;

    /** 在庫管理フラグ */
    private HTypeStockManagementFlag stockManagementFlag = HTypeStockManagementFlag.ON;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}