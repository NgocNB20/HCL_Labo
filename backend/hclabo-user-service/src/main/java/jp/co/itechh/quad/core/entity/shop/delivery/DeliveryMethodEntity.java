/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.delivery;

import jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeShortfallDisplayFlag;
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
 * 配送方法クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "DeliveryMethod")
@Data
@Component
@Scope("prototype")
public class DeliveryMethodEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ（必須） */
    @Column(name = "deliveryMethodSeq")
    @Id
    private Integer deliveryMethodSeq;

    /** ショップSEQ (FK)（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 配送方法名（必須） */
    @Column(name = "deliveryMethodName")
    private String deliveryMethodName;

    /** 配送方法表示名PC */
    @Column(name = "deliveryMethodDisplayNamePC")
    private String deliveryMethodDisplayNamePC;

    /** 公開状態PC（必須） */
    @Column(name = "openStatusPC")
    private HTypeOpenDeleteStatus openStatusPC = HTypeOpenDeleteStatus.NO_OPEN;

    /** 配送方法説明文PC */
    @Column(name = "deliveryNotePC")
    private String deliveryNotePC;

    /** 配送方法種別（必須） */
    @Column(name = "deliveryMethodType")
    private HTypeDeliveryMethodType deliveryMethodType;

    /** 一律送料 */
    @Column(name = "equalsCarriage")
    private BigDecimal equalsCarriage = new BigDecimal(0);

    /** 高額割引下限金額 */
    @Column(name = "largeAmountDiscountPrice")
    private BigDecimal largeAmountDiscountPrice = new BigDecimal(0);

    /** 高額割引送料 */
    @Column(name = "largeAmountDiscountCarriage")
    private BigDecimal largeAmountDiscountCarriage = new BigDecimal(0);

    /** 不足金額表示フラグ（必須） */
    @Column(name = "shortfallDisplayFlag")
    private HTypeShortfallDisplayFlag shortfallDisplayFlag = HTypeShortfallDisplayFlag.OFF;

    /** リードタイム */
    @Column(name = "deliveryLeadTime")
    private int deliveryLeadTime = 0;

    /** 配送追跡URL */
    @Column(name = "deliveryChaseURL")
    private String deliveryChaseURL;

    /** 配送追跡URL表示期間 */
    @Column(name = "deliveryChaseURLDisplayPeriod")
    private BigDecimal deliveryChaseURLDisplayPeriod;

    /** 選択可能日数 */
    @Column(name = "possibleSelectDays")
    private int possibleSelectDays = 0;

    /** お届け時間帯1 */
    @Column(name = "receiverTimeZone1")
    private String receiverTimeZone1;

    /** お届け時間帯2 */
    @Column(name = "receiverTimeZone2")
    private String receiverTimeZone2;

    /** お届け時間帯3 */
    @Column(name = "receiverTimeZone3")
    private String receiverTimeZone3;

    /** お届け時間帯4 */
    @Column(name = "receiverTimeZone4")
    private String receiverTimeZone4;

    /** お届け時間帯5 */
    @Column(name = "receiverTimeZone5")
    private String receiverTimeZone5;

    /** お届け時間帯6 */
    @Column(name = "receiverTimeZone6")
    private String receiverTimeZone6;

    /** お届け時間帯7 */
    @Column(name = "receiverTimeZone7")
    private String receiverTimeZone7;

    /** お届け時間帯8 */
    @Column(name = "receiverTimeZone8")
    private String receiverTimeZone8;

    /** お届け時間帯9 */
    @Column(name = "receiverTimeZone9")
    private String receiverTimeZone9;

    /** お届け時間帯10 */
    @Column(name = "receiverTimeZone10")
    private String receiverTimeZone10;

    /** 表示順 */
    @Column(name = "orderDisplay")
    private Integer orderDisplay;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}