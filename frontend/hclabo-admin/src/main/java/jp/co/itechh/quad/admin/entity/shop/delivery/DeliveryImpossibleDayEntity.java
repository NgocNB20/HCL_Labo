/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.admin.entity.shop.delivery;

import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class DeliveryImpossibleDayEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ */
    private Integer deliveryMethodSeq;

    /** 年月日（必須） */
    private Date date;

    /** 年 */
    private Integer year;

    /** 理由 */
    private String reason;

    /** 登録日時（必須） */
    private Timestamp registTime;

}