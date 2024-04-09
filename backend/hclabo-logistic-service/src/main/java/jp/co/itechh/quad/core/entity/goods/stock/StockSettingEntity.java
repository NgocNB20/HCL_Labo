/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.stock;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 在庫設定クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "StockSetting")
@Data
@Component
@Scope("prototype")
public class StockSettingEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品SEQ (FK)（必須） */
    @Column(name = "goodsSeq")
    @Id
    private Integer goodsSeq;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 残少表示在庫数（必須） */
    @Column(name = "remainderFewStock")
    private BigDecimal remainderFewStock;

    /** 発注点在庫数（必須） */
    @Column(name = "orderPointStock")
    private BigDecimal orderPointStock;

    /** 安全在庫数（必須） */
    @Column(name = "safetyStock")
    private BigDecimal safetyStock;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** 在庫管理フラグ（必須） */
    @Column(name = "stockManagementFlag")
    private String stockManagementFlag = "1";
}