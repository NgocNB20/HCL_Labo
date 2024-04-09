/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop.delivery;

import jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.core.constant.type.HTypeShortfallDisplayFlag;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 配送方法詳細DTOクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Data
@Component
@Scope("prototype")
public class DeliveryDetailsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ */
    private Integer deliveryMethodSeq;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 配送方法名 */
    private String deliveryMethodName;

    /** 配送方法表示名PC */
    private String deliveryMethodDisplayNamePC;

    /** 公開状態PC */
    private HTypeOpenDeleteStatus openStatusPC;

    /** 配送方法説明文PC */
    private String deliveryNotePC;

    /** 配送方法区分 */
    private HTypeDeliveryMethodType deliveryMethodType;

    /** 一律送料 */
    private BigDecimal equalsCarriage;

    /** 高額割引下限金額 */
    private BigDecimal largeAmountDiscountPrice;

    /** 高額割引送料 */
    private BigDecimal largeAmountDiscountCarriage;

    /** 不足金額表示フラグ（必須） */
    private HTypeShortfallDisplayFlag shortfallDisplayFlag;

    /** リードタイム */
    private int deliveryLeadTime;

    /** 選択可能日数 */
    private int possibleSelectDays;

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

    /** 都道府県種別 */
    private HTypePrefectureType prefectureType;

    /** 上限金額 */
    private BigDecimal maxPrice;

    /** 送料 */
    private BigDecimal carriage;
}
