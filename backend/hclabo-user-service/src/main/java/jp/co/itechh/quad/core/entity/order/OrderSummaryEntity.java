/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.order;

import jp.co.itechh.quad.core.constant.type.HTypeCancelFlag;
import jp.co.itechh.quad.core.constant.type.HTypeCarrierType;
import jp.co.itechh.quad.core.constant.type.HTypeDeviceType;
import jp.co.itechh.quad.core.constant.type.HTypeEmergencyFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.core.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.core.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.core.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOrderType;
import jp.co.itechh.quad.core.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.core.constant.type.HTypeRepeatPurchaseType;
import jp.co.itechh.quad.core.constant.type.HTypeSalesFlag;
import jp.co.itechh.quad.core.constant.type.HTypeSend;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.constant.type.HTypeWaitingFlag;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 受注サマリクラス
 *
 * @author EntityGenerator
 */
@Entity
@Table(name = "OrderSummary")
@Data
@Component
@Scope("prototype")
public class OrderSummaryEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    @Column(name = "orderSeq")
    @Id
    private Integer orderSeq;

    /** 受注履歴連番（必須） */
    @Column(name = "orderVersionNo")
    private Integer orderVersionNo;

    /** 受注コード（必須） */
    @Column(name = "orderCode")
    private String orderCode;

    /** 受注種別（必須） */
    @Column(name = "orderType")
    private HTypeOrderType orderType;

    /** 受注日時（必須） */
    @Column(name = "orderTime")
    private Timestamp orderTime;

    /** 売上日時 */
    @Column(name = "salesTime")
    private Timestamp salesTime;

    /** キャンセル日時 */
    @Column(name = "cancelTime")
    private Timestamp cancelTime;

    /** 売上フラグ（必須） */
    @Column(name = "salesFlag")
    private HTypeSalesFlag salesFlag = HTypeSalesFlag.OFF;

    /** キャンセルフラグ（必須） */
    @Column(name = "cancelFlag")
    private HTypeCancelFlag cancelFlag = HTypeCancelFlag.OFF;

    /** 保留中フラグ（必須） */
    @Column(name = "waitingFlag")
    private HTypeWaitingFlag waitingFlag = HTypeWaitingFlag.OFF;

    /** 受注状態（必須） */
    @Column(name = "orderStatus")
    private HTypeOrderStatus orderStatus = HTypeOrderStatus.PAYMENT_CONFIRMING;

    /** 割引前受注金額（必須） */
    @Column(name = "beforeDiscountOrderPrice")
    private BigDecimal beforeDiscountOrderPrice = BigDecimal.ZERO;

    /** 受注金額（必須） */
    @Column(name = "orderPrice")
    private BigDecimal orderPrice = BigDecimal.ZERO;

    /** 入金累計（必須） */
    @Column(name = "receiptPriceTotal")
    private BigDecimal receiptPriceTotal = BigDecimal.ZERO;

    /** 受注サイト種別（必須） */
    @Column(name = "orderSiteType")
    private HTypeSiteType orderSiteType = HTypeSiteType.FRONT_PC;

    /** 受注デバイス種別（必須） */
    @Column(name = "orderDeviceType")
    private HTypeDeviceType orderDeviceType = HTypeDeviceType.PC;

    /** キャリア種別（必須） */
    @Column(name = "carrierType")
    private HTypeCarrierType carrierType = HTypeCarrierType.PC;

    /** 決済方法SEQ（必須） */
    @Column(name = "settlementMethodSeq")
    private Integer settlementMethodSeq;

    /** 消費税SEQ */
    @Column(name = "taxSeq")
    private Integer taxSeq;

    /** 会員SEQ（必須） */
    @Column(name = "memberInfoSeq")
    private Integer memberInfoSeq;

    /** 都道府県種別 */
    @Column(name = "prefectureType")
    private HTypePrefectureType prefectureType;

    /** ご注文主性別（必須） */
    @Column(name = "orderSex")
    private HTypeOrderSex orderSex = HTypeOrderSex.UNKNOWN;

    /** ご注文主年代 */
    @Column(name = "orderAgeType")
    private HTypeOrderAgeType orderAgeType;

    /** リピート購入種別（必須） */
    @Column(name = "repeatPurchaseType")
    private HTypeRepeatPurchaseType repeatPurchaseType = HTypeRepeatPurchaseType.GUEST;

    /** 決済関連メール要否フラグ */
    @Column(name = "settlementMailRequired")
    private HTypeMailRequired settlementMailRequired = HTypeMailRequired.NO_NEED;

    /** 督促メール送信済みフラグ */
    @Column(name = "reminderSentFlag")
    private HTypeSend reminderSentFlag = HTypeSend.UNSENT;

    /** 期限切れメール送信済みフラグ */
    @Column(name = "expiredSentFlag")
    private HTypeSend expiredSentFlag = HTypeSend.UNSENT;

    /** ユーザーエージェント */
    @Column(name = "userAgent")
    private String userAgent;

    /** フリーエリアキー */
    @Column(name = "freeAreaKey")
    private String freeAreaKey;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 更新カウンタ（必須） */
    @Version
    @Column(name = "versionNo")
    private Integer versionNo = 0;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

    // テーブル項目外追加フィールド

    // 受注インデックス情報

    /** 受注商品連番 */
    @Column(insertable = false, updatable = false)
    private Integer orderGoodsVersionNo;

    // 受注決済情報

    /** クーポン割引額 */
    @Column(insertable = false, updatable = false)
    private BigDecimal couponDiscountPrice;

    // 決済方法情報

    /** 決済方法名 */
    @Column(insertable = false, updatable = false)
    private String settlementMethodName;

    /** 決済方法表示名PC */
    @Column(insertable = false, updatable = false)
    private String settlementMethodDisplayNamePC;

    // 受注配送方法情報

    /** お届け時間帯 */
    @Column(insertable = false, updatable = false)
    private String receiverTimeZone;

    /** ご注文主氏名 */
    @Column(insertable = false, updatable = false)
    private String orderName;

    /** ご注文主フリガナ */
    @Column(insertable = false, updatable = false)
    private String orderKana;

    /** ご注文主電話番号 */
    @Column(insertable = false, updatable = false)
    private String orderTel;

    /** ご注文主連絡先電話番号 */
    @Column(insertable = false, updatable = false)
    private String orderContactTel;

    /** ご注文主メールアドレス */
    @Column(insertable = false, updatable = false)
    private String orderMail;

    /** お届け先氏名 */
    @Column(insertable = false, updatable = false)
    private String receiverName;

    /** お届け先フリガナ */
    @Column(insertable = false, updatable = false)
    private String receiverKana;

    /** お届け先電話番号 */
    @Column(insertable = false, updatable = false)
    private String receiverTel;

    /** 注文連番 */
    @Column(insertable = false, updatable = false)
    private Integer orderConsecutiveNo;

    /** 伝票番号 */
    @Column(insertable = false, updatable = false)
    private String deliveryCode;

    /** 出荷状態 */
    @Column(insertable = false, updatable = false)
    private String shipmentStatus;

    /** 配送方法備考 */
    @Column(insertable = false, updatable = false)
    private String deliveryNote;

    /** 入金日時 */
    @Column(insertable = false, updatable = false)
    private Timestamp receiptTime;

    /** 異常フラグ */
    @Column(insertable = false, updatable = false)
    private HTypeEmergencyFlag emergencyFlag;

    /**
     * 入金状態
     * "1"=未入金
     * "2"=入金済み
     * "3"=過不足
     */
    @Column(insertable = false, updatable = false)
    private String paymentStatus;

    /** 配送方法名称 */
    @Column(insertable = false, updatable = false)
    private String deliveryMethodName;

    /** 検索結果表示用受注状態 */
    @Column(insertable = false, updatable = false)
    private String orderStatusForSearchResult;
}