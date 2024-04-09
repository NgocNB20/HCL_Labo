/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order.delivery;

import jp.co.itechh.quad.admin.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeReceiverDateDesignationFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeReservationDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeShipmentStatus;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class OrderDeliveryEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    private Integer orderSeq;

    /** 受注配送連番（必須） */
    private Integer orderDeliveryVersionNo;

    /** 注文連番（必須） */
    private Integer orderConsecutiveNo = 1;

    /** 出荷状態（必須） */
    private HTypeShipmentStatus shipmentStatus = HTypeShipmentStatus.UNSHIPMENT;

    /** 予約配送フラグ */
    private HTypeReservationDeliveryFlag reservationDeliveryFlag = HTypeReservationDeliveryFlag.OFF;

    /** 出荷予定日 */
    private Timestamp planDate;

    /** 出荷日 */
    private Timestamp shipmentDate;

    /** 伝票番号 */
    private String deliveryCode;

    /** 納品書添付フラグ（必須） */
    private HTypeInvoiceAttachmentFlag invoiceAttachmentFlag = HTypeInvoiceAttachmentFlag.OFF;

    /** 配送方法SEQ（必須） */
    private Integer deliveryMethodSeq;

    /** お届け先氏名(姓)（必須） */
    private String receiverLastName;

    /** お届け先氏名(名) */
    private String receiverFirstName;

    /** お届け先フリガナ(姓)（必須） */
    private String receiverLastKana;

    /** お届け先フリガナ(名) */
    private String receiverFirstKana;

    /** お届け先電話番号（必須） */
    private String receiverTel;

    /** お届け先住所-郵便番号（必須） */
    private String receiverZipCode;

    /** お届け先住所-都道府県（必須） */
    private String receiverPrefecture;

    /** お届け先住所-市区郡（必須） */
    private String receiverAddress1;

    /** お届け先住所-町村・番地（必須） */
    private String receiverAddress2;

    /** お届け先住所-それ以降の住所 */
    private String receiverAddress3;

    /** お届け希望日指定フラグ（必須） */
    private HTypeReceiverDateDesignationFlag receiverDateDesignationFlag = HTypeReceiverDateDesignationFlag.OFF;

    /** お届け希望日 */
    private Timestamp receiverDate;

    /** お届け時間帯 */
    private String receiverTimeZone;

    /** 配送方法備考 */
    private String deliveryNote;

    /** その他備考 */
    private String othersNote;

    /** お客様へのメッセージ */
    private String message;

    /** 送料 */
    private BigDecimal carriage = new BigDecimal(0);

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** 最短お届け日_日付登録用 */
    private Timestamp shortestDeliveryDateToRegist;

    /** 定期商品 お届け日 */
    private Integer deliveryDay;
}