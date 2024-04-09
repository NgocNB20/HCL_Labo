/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.tax;

import jp.co.itechh.quad.core.constant.type.HTypeTaxRateType;
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
 * 税率クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "TaxRate")
@Data
@Component
@Scope("prototype")
public class TaxRateEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 消費税SEQ */
    @Column(name = "taxSeq")
    @Id
    private Integer taxSeq;

    /** 税率 */
    @Column(name = "rate")
    @Id
    private BigDecimal rate;

    /** 区分 */
    @Column(name = "rateType")
    private HTypeTaxRateType rateType;

    /** 表示順（必須） */
    @Column(name = "orderDisplay")
    private Integer orderDisplay;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}