/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.order.index;

import jp.co.itechh.quad.core.constant.type.HTypeCouponLimitTargetType;
import jp.co.itechh.quad.core.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.core.constant.type.HTypeSend;
import jp.co.itechh.quad.core.constant.type.HTypeWaitingFlag;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 受注インデックスクラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "OrderIndex")
@Data
@Component
@Scope("prototype")
public class OrderIndexEntity implements Serializable {

    /**
     * シリアル
     */
    private static final long serialVersionUID = 4446598724831644063L;

    /** 受注SEQ (FK)（必須） */
    @Column(name = "orderSeq")
    @Id
    private Integer orderSeq;

    /** 受注履歴連番（必須） */
    @Column(name = "orderVersionNo")
    @Id
    private Integer orderVersionNo;

    /** 処理日時（必須） */
    @Column(name = "processTime")
    private Timestamp processTime;

    /** 受注ご注文主連番 (FK)（必須） */
    @Column(name = "orderPersonVersionNo")
    private Integer orderPersonVersionNo;

    /** 受注配送連番 (FK)（必須） */
    @Column(name = "orderDeliveryVersionNo")
    private Integer orderDeliveryVersionNo;

    /** 受注決済連番 (FK)（必須） */
    @Column(name = "orderSettlementVersionNo")
    private Integer orderSettlementVersionNo;

    /** 受注商品連番（必須） */
    @Column(name = "orderGoodsVersionNo")
    private Integer orderGoodsVersionNo;

    /** 受注追加料金連番 */
    @Column(name = "orderAdditionalChargeVersionNo")
    private Integer orderAdditionalChargeVersionNo;

    /** 受注請求連番 (FK)（必須） */
    @Column(name = "orderBillVersionNo")
    private Integer orderBillVersionNo;

    /** 受注入金連番 */
    @Column(name = "orderReceiptOfMoneyVersionNo")
    private Integer orderReceiptOfMoneyVersionNo;

    /** 受注メモ連番 */
    @Column(name = "orderMemoVersionNo")
    private Integer orderMemoVersionNo;

    /** 処理担当者名 */
    @Column(name = "processPersonName")
    private String processPersonName;

    /** 処理種別（必須） */
    @Column(name = "processType")
    private HTypeProcessType processType;

    /** 保留中フラグ（必須） */
    @Column(name = "waitingFlag")
    private HTypeWaitingFlag waitingFlag = HTypeWaitingFlag.OFF;

    /** ショップSEQ (FK)（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 決済関連メール要否フラグ */
    @Column(name = "settlementMailRequired")
    private HTypeMailRequired settlementMailRequired = HTypeMailRequired.NO_NEED;

    /** 督促メール送信済みフラグ */
    @Column(name = "reminderSentFlag")
    private HTypeSend reminderSentFlag = HTypeSend.UNSENT;

    /** 期限切れメール送信済みフラグ */
    @Column(name = "expiredSentFlag")
    private HTypeSend expiredSentFlag = HTypeSend.UNSENT;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    /** クーポンSEQ */
    @Column(name = "couponSeq")
    private Integer couponSeq;

    /** クーポン連番 */
    @Column(name = "couponVersionNo")
    private Integer couponVersionNo;

    /** クーポン利用制限対象フラグ */
    @Column(name = "couponLimitTargetType")
    private HTypeCouponLimitTargetType couponLimitTargetType = HTypeCouponLimitTargetType.ON;

    /** 注文保留理由 */
    @Column(name = "orderWaitingMemo")
    private String orderWaitingMemo;
}