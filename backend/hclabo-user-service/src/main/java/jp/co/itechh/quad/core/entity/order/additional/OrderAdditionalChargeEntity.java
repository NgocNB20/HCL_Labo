/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.order.additional;

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
 * 受注追加料金クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "OrderAdditionalCharge")
@Data
@Component
@Scope("prototype")
public class OrderAdditionalChargeEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    @Column(name = "orderSeq")
    @Id
    private Integer orderSeq;

    /** 受注追加料金連番（必須） */
    @Column(name = "orderAdditionalChargeVersionNo")
    @Id
    private Integer orderAdditionalChargeVersionNo;

    /** 表示順（必須） */
    @Column(name = "orderDisplay")
    @Id
    private Integer orderDisplay;

    /** 追加明細項目名（必須） */
    @Column(name = "additionalDetailsName")
    private String additionalDetailsName;

    /** 追加明細金額（必須） */
    @Column(name = "additionalDetailsPrice")
    private BigDecimal additionalDetailsPrice = BigDecimal.ZERO;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}