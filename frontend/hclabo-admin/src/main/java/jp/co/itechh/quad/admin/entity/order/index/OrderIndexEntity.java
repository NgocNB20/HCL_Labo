/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order.index;

import jp.co.itechh.quad.admin.constant.type.HTypeCouponLimitTargetType;
import jp.co.itechh.quad.admin.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.admin.constant.type.HTypeProcessType;
import jp.co.itechh.quad.admin.constant.type.HTypeSend;
import jp.co.itechh.quad.admin.constant.type.HTypeWaitingFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 受注インデックスクラス
 *
 * @author EntityGenerator
 */
@Data
@Component
@Scope("prototype")
public class OrderIndexEntity implements Serializable {

    /**
     * シリアル
     */
    private static final long serialVersionUID = 4446598724831644063L;

    /** 受注SEQ (FK)（必須） */
    private Integer orderSeq;

    /** 受注履歴連番（必須） */
    private Integer orderVersionNo;

    /** 処理日時（必須） */
    private Timestamp processTime;

    /** 受注ご注文主連番 (FK)（必須） */
    private Integer orderPersonVersionNo;

    /** 受注配送連番 (FK)（必須） */
    private Integer orderDeliveryVersionNo;

    /** 受注決済連番 (FK)（必須） */
    private Integer orderSettlementVersionNo;

    /** 受注商品連番（必須） */
    private Integer orderGoodsVersionNo;

    /** 受注追加料金連番 */
    private Integer orderAdditionalChargeVersionNo;

    /** 受注請求連番 (FK)（必須） */
    private Integer orderBillVersionNo;

    /** 受注入金連番 */
    private Integer orderReceiptOfMoneyVersionNo;

    /** 受注メモ連番 */
    private Integer orderMemoVersionNo;

    /** 処理担当者名 */
    private String processPersonName;

    /** 処理種別（必須） */
    private HTypeProcessType processType;

    /** 保留中フラグ（必須） */
    private HTypeWaitingFlag waitingFlag = HTypeWaitingFlag.OFF;

    /** ショップSEQ (FK)（必須） */
    private Integer shopSeq;

    /** 決済関連メール要否フラグ */
    private HTypeMailRequired settlementMailRequired = HTypeMailRequired.NO_NEED;

    /** 督促メール送信済みフラグ */
    private HTypeSend reminderSentFlag = HTypeSend.UNSENT;

    /** 期限切れメール送信済みフラグ */
    private HTypeSend expiredSentFlag = HTypeSend.UNSENT;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** クーポンSEQ */
    private Integer couponSeq;

    /** クーポン連番 */
    private Integer couponVersionNo;

    /** クーポン利用制限対象フラグ */
    private HTypeCouponLimitTargetType couponLimitTargetType = HTypeCouponLimitTargetType.ON;

    /** 注文保留理由 */
    private String orderWaitingMemo;

    /** 請求状態 */
    private String billStatus;
}
