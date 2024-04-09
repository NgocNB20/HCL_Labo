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
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 配送特別料金エリアクラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "DeliverySpecialChargeArea")
@Data
@Component
@Scope("prototype")
public class DeliverySpecialChargeAreaEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ (FK)（必須） */
    @Column(name = "deliveryMethodSeq")
    @Id
    private Integer deliveryMethodSeq;

    /** 郵便番号（必須） */
    @Column(name = "zipCode")
    @Id
    private String zipCode;

    /** 送料 */
    @Column(name = "carriage")
    private BigDecimal carriage = new BigDecimal(0);

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}
