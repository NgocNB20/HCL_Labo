/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.order.delivery;

import jp.co.itechh.quad.core.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.core.constant.type.HTypeReceiverDateDesignationFlag;
import jp.co.itechh.quad.core.constant.type.HTypeReservationDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypeShipmentStatus;
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
 * 受注配送クラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "OrderDelivery")
@Data
@Component
@Scope("prototype")
public class OrderDeliveryEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    @Column(name = "orderSeq")
    @Id
    private Integer orderSeq;

    /** 受注配送連番（必須） */
    @Column(name = "orderDeliveryVersionNo")
    @Id
    private Integer orderDeliveryVersionNo;

    /** 注文連番（必須） */
    @Column(name = "orderConsecutiveNo")
    @Id
    private Integer orderConsecutiveNo = 1;

    /** 出荷状態（必須） */
    @Column(name = "shipmentStatus")
    private HTypeShipmentStatus shipmentStatus = HTypeShipmentStatus.UNSHIPMENT;

    /** 予約配送フラグ */
    @Column(name = "reservationDeliveryFlag")
    private HTypeReservationDeliveryFlag reservationDeliveryFlag = HTypeReservationDeliveryFlag.OFF;

    /** 出荷予定日 */
    @Column(name = "planDate")
    private Timestamp planDate;

    /** 出荷日 */
    @Column(name = "shipmentDate")
    private Timestamp shipmentDate;

    /** 伝票番号 */
    @Column(name = "deliveryCode")
    private String deliveryCode;

    /** 納品書添付フラグ（必須） */
    @Column(name = "invoiceAttachmentFlag")
    private HTypeInvoiceAttachmentFlag invoiceAttachmentFlag = HTypeInvoiceAttachmentFlag.OFF;

    /** 配送方法SEQ（必須） */
    @Column(name = "deliveryMethodSeq")
    private Integer deliveryMethodSeq;

    /** お届け先氏名(姓)（必須） */
    @Column(name = "receiverLastName")
    private String receiverLastName;

    /** お届け先氏名(名) */
    @Column(name = "receiverFirstName")
    private String receiverFirstName;

    /** お届け先フリガナ(姓)（必須） */
    @Column(name = "receiverLastKana")
    private String receiverLastKana;

    /** お届け先フリガナ(名) */
    @Column(name = "receiverFirstKana")
    private String receiverFirstKana;

    /** お届け先電話番号（必須） */
    @Column(name = "receiverTel")
    private String receiverTel;

    /** お届け先住所-郵便番号（必須） */
    @Column(name = "receiverZipCode")
    private String receiverZipCode;

    /** お届け先住所-都道府県（必須） */
    @Column(name = "receiverPrefecture")
    private String receiverPrefecture;

    /** お届け先住所-市区郡（必須） */
    @Column(name = "receiverAddress1")
    private String receiverAddress1;

    /** お届け先住所-町村・番地（必須） */
    @Column(name = "receiverAddress2")
    private String receiverAddress2;

    /** お届け先住所-それ以降の住所 */
    @Column(name = "receiverAddress3")
    private String receiverAddress3;

    /** お届け希望日指定フラグ（必須） */
    @Column(name = "receiverDateDesignationFlag")
    private HTypeReceiverDateDesignationFlag receiverDateDesignationFlag = HTypeReceiverDateDesignationFlag.OFF;

    /** お届け希望日 */
    @Column(name = "receiverDate")
    private Timestamp receiverDate;

    /** お届け時間帯 */
    @Column(name = "receiverTimeZone")
    private String receiverTimeZone;

    /** 配送方法備考 */
    @Column(name = "deliveryNote")
    private String deliveryNote;

    /** その他備考 */
    @Column(name = "othersNote")
    private String othersNote;

    /** お客様へのメッセージ */
    @Column(name = "message")
    private String message;

    /** 送料 */
    @Column(name = "carriage")
    private BigDecimal carriage = new BigDecimal(0);

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** 最短お届け日_日付登録用 */
    @Column(insertable = false, updatable = false)
    private Timestamp shortestDeliveryDateToRegist;

    /** 定期商品 お届け日 */
    @Column(insertable = false, updatable = false)
    private Integer deliveryDay;
}