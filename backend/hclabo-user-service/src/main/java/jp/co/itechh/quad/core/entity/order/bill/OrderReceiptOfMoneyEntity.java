/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.order.bill;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
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
 * 受注入金クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "OrderReceiptOfMoney")
@Data
@Component
@Scope("prototype")
public class OrderReceiptOfMoneyEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    @Column(name = "orderSeq")
    @Id
    private Integer orderSeq;

    /** 受注入金連番（必須） */
    @Column(name = "orderReceiptOfMoneyVersionNo")
    @Id
    private Integer orderReceiptOfMoneyVersionNo;

    /** 入金日時 */
    @Column(name = "receiptTime")
    private Timestamp receiptTime;

    /** 入金年月日 */
    @Column(name = "receiptYmd")
    private String receiptYmd;

    /** 入金年月 */
    @Column(name = "receiptYm")
    private String receiptYm;

    /** 入金金額（必須） */
    @Column(name = "receiptPrice")
    private BigDecimal receiptPrice = new BigDecimal(0);

    /** 入金累計（必須） */
    @Column(name = "receiptPriceTotal")
    private BigDecimal receiptPriceTotal = new BigDecimal(0);

    /** 入金方法SEQ */
    @Column(name = "receiptMethodSeq")
    private Integer receiptMethodSeq;

    /** 受注日時（必須） */
    @Column(name = "orderTime")
    private Timestamp orderTime;

    /** 売上日時 */
    @Column(name = "salesTime")
    private Timestamp salesTime;

    /** 受注サイト種別（必須） */
    @Column(name = "orderSiteType")
    private HTypeSiteType orderSiteType = HTypeSiteType.FRONT_PC;

    /** 決済方法SEQ */
    @Column(name = "settlementMethodSeq")
    private Integer settlementMethodSeq;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}