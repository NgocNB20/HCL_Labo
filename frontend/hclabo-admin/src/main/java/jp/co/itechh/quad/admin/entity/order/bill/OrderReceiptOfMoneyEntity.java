/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order.bill;

import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 受注入金クラス
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class OrderReceiptOfMoneyEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    private Integer orderSeq;

    /** 受注入金連番（必須） */
    private Integer orderReceiptOfMoneyVersionNo;

    /** 入金日時 */
    private Timestamp receiptTime;

    /** 入金年月日 */
    private String receiptYmd;

    /** 入金年月 */
    private String receiptYm;

    /** 入金金額（必須） */
    private BigDecimal receiptPrice = new BigDecimal(0);

    /** 入金累計（必須） */
    private BigDecimal receiptPriceTotal = new BigDecimal(0);

    /** 入金方法SEQ */
    private Integer receiptMethodSeq;

    /** 受注日時（必須） */
    private Timestamp orderTime;

    /** 売上日時 */
    private Timestamp salesTime;

    /** 受注サイト種別（必須） */
    private HTypeSiteType orderSiteType = HTypeSiteType.FRONT_PC;

    /** 決済方法SEQ */
    private Integer settlementMethodSeq;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}