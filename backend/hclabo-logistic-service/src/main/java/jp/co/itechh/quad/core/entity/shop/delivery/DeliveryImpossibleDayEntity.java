/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.entity.shop.delivery;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * お届け不可日クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "DeliveryImpossibleDay")
@Data
@Component
@Scope("prototype")
public class DeliveryImpossibleDayEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ */
    @Column(name = "deliveryMethodSeq")
    @Id
    private Integer deliveryMethodSeq;

    /** 年月日（必須） */
    @Column(name = "date")
    @Id
    private Date date;

    /** 年 */
    @Column(name = "year")
    private Integer year;

    /** 理由 */
    @Column(name = "reason")
    private String reason;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

}