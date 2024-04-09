/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.stock;

import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
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
@Entity
@Table(name = "StockResult")
@Data
@Component
@Scope("prototype")
public class StockResultEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 入庫実績SEQ（必須） */
    @Column(name = "stockResultSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "stockResultSeq")
    private Integer stockResultSeq;

    /** 商品SEQ (FK)（必須） */
    @Column(name = "goodsSeq")
    private Integer goodsSeq;

    /** 入庫日時（必須） */
    @Column(name = "supplementTime")
    private Timestamp supplementTime;

    /** 入庫数（必須） */
    @Column(name = "supplementCount")
    private BigDecimal supplementCount;

    /** 実在庫数（必須） */
    @Column(name = "realStock")
    private BigDecimal realStock;

    /** 処理担当者名 */
    @Column(name = "processPersonName")
    private String processPersonName;

    /** 備考 */
    @Column(name = "note")
    private String note;

    /** 在庫管理フラグ */
    @Column(name = "stockManagementFlag")
    private HTypeStockManagementFlag stockManagementFlag = HTypeStockManagementFlag.ON;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}