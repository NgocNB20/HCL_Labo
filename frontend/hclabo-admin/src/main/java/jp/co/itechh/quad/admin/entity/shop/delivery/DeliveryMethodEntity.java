/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.shop.delivery;

import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeShortfallDisplayFlag;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class DeliveryMethodEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ（必須） */
    private Integer deliveryMethodSeq;

    /** ショップSEQ (FK)（必須） */
    private Integer shopSeq;

    /** 配送方法名（必須） */
    private String deliveryMethodName;

    /** 配送方法表示名PC */
    private String deliveryMethodDisplayNamePC;

    /** 公開状態PC（必須） */
    private HTypeOpenDeleteStatus openStatusPC = HTypeOpenDeleteStatus.NO_OPEN;

    /** 配送方法説明文PC */
    private String deliveryNotePC;

    /** 配送方法種別（必須） */
    private HTypeDeliveryMethodType deliveryMethodType;

    /** 一律送料 */
    private BigDecimal equalsCarriage = new BigDecimal(0);

    /** 高額割引下限金額 */
    private BigDecimal largeAmountDiscountPrice = new BigDecimal(0);

    /** 高額割引送料 */
    private BigDecimal largeAmountDiscountCarriage = new BigDecimal(0);

    /** 不足金額表示フラグ（必須） */
    private HTypeShortfallDisplayFlag shortfallDisplayFlag = HTypeShortfallDisplayFlag.OFF;

    /** リードタイム */
    private int deliveryLeadTime = 0;

    /** 配送追跡URL */
    private String deliveryChaseURL;

    /** 配送追跡URL表示期間 */
    private BigDecimal deliveryChaseURLDisplayPeriod;

    /** 選択可能日数 */
    private int possibleSelectDays = 0;

    /** お届け時間帯1 */
    private String receiverTimeZone1;

    /** お届け時間帯2 */
    private String receiverTimeZone2;

    /** お届け時間帯3 */
    private String receiverTimeZone3;

    /** お届け時間帯4 */
    private String receiverTimeZone4;

    /** お届け時間帯5 */
    private String receiverTimeZone5;

    /** お届け時間帯6 */
    private String receiverTimeZone6;

    /** お届け時間帯7 */
    private String receiverTimeZone7;

    /** お届け時間帯8 */
    private String receiverTimeZone8;

    /** お届け時間帯9 */
    private String receiverTimeZone9;

    /** お届け時間帯10 */
    private String receiverTimeZone10;

    /** 表示順 */
    private Integer orderDisplay;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

}