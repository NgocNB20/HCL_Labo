/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.order;

import jp.co.itechh.quad.admin.constant.type.HTypeCancelFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeCarrierType;
import jp.co.itechh.quad.admin.constant.type.HTypeDeviceType;
import jp.co.itechh.quad.admin.constant.type.HTypeEmergencyFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderType;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeRepeatPurchaseType;
import jp.co.itechh.quad.admin.constant.type.HTypeSalesFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeSend;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.constant.type.HTypeWaitingFlag;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class OrderSummaryEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ（必須） */
    private Integer orderSeq;

    /** 受注履歴連番（必須） */
    private Integer orderVersionNo;

    /** 受注コード（必須） */
    private String orderCode;

    /** 受注種別（必須） */
    private HTypeOrderType orderType;

    /** 受注日時（必須） */
    private Timestamp orderTime;

    /** 売上日時 */
    private Timestamp salesTime;

    /** キャンセル日時 */
    private Timestamp cancelTime;

    /** 売上フラグ（必須） */
    private HTypeSalesFlag salesFlag = HTypeSalesFlag.OFF;

    /** キャンセルフラグ（必須） */
    private HTypeCancelFlag cancelFlag = HTypeCancelFlag.OFF;

    /** 保留中フラグ（必須） */
    private HTypeWaitingFlag waitingFlag = HTypeWaitingFlag.OFF;

    /** 受注状態（必須） */
    private HTypeOrderStatus orderStatus = HTypeOrderStatus.PAYMENT_CONFIRMING;

    /** 割引前受注金額（必須） */
    private BigDecimal beforeDiscountOrderPrice = BigDecimal.ZERO;

    /** 受注金額（必須） */
    private BigDecimal orderPrice = BigDecimal.ZERO;

    /** 入金累計（必須） */
    private BigDecimal receiptPriceTotal = BigDecimal.ZERO;

    /** 受注サイト種別（必須） */
    private HTypeSiteType orderSiteType = HTypeSiteType.FRONT_PC;

    /** 受注デバイス種別（必須） */
    private HTypeDeviceType orderDeviceType = HTypeDeviceType.PC;

    /** キャリア種別（必須） */
    private HTypeCarrierType carrierType = HTypeCarrierType.PC;

    /** 決済方法SEQ（必須） */
    private Integer settlementMethodSeq;

    /** 消費税SEQ */
    private Integer taxSeq;

    /** 会員SEQ（必須） */
    private Integer memberInfoSeq;

    /** 都道府県種別 */
    private HTypePrefectureType prefectureType;

    /** ご注文主性別（必須） */
    private HTypeOrderSex orderSex = HTypeOrderSex.UNKNOWN;

    /** ご注文主年代 */
    private HTypeOrderAgeType orderAgeType;

    /** リピート購入種別（必須） */
    private HTypeRepeatPurchaseType repeatPurchaseType = HTypeRepeatPurchaseType.GUEST;

    /** 決済関連メール要否フラグ */
    private HTypeMailRequired settlementMailRequired = HTypeMailRequired.NO_NEED;

    /** 督促メール送信済みフラグ */
    private HTypeSend reminderSentFlag = HTypeSend.UNSENT;

    /** 期限切れメール送信済みフラグ */
    private HTypeSend expiredSentFlag = HTypeSend.UNSENT;

    /** ユーザーエージェント */
    private String userAgent;

    /** フリーエリアキー */
    private String freeAreaKey;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 更新カウンタ（必須） */
    private Integer versionNo = 0;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    // テーブル項目外追加フィールド

    // 受注インデックス情報

    /** 受注商品連番 */
    private Integer orderGoodsVersionNo;

    // 受注決済情報

    /** クーポン割引額 */
    private BigDecimal couponDiscountPrice;

    // 決済方法情報

    /** 決済方法名 */
    private String settlementMethodName;

    /** 決済方法表示名PC */
    private String settlementMethodDisplayNamePC;

    // 受注配送方法情報

    /** お届け時間帯 */
    private String receiverTimeZone;

    /** ご注文主氏名 */
    private String orderName;

    /** ご注文主フリガナ */
    private String orderKana;

    /** ご注文主電話番号 */
    private String orderTel;

    /** ご注文主連絡先電話番号 */
    private String orderContactTel;

    /** ご注文主メールアドレス */
    private String orderMail;

    /** お届け先氏名 */
    private String receiverName;

    /** お届け先フリガナ */
    private String receiverKana;

    /** お届け先電話番号 */
    private String receiverTel;

    /** 注文連番 */
    private Integer orderConsecutiveNo;

    /** 伝票番号 */
    private String deliveryCode;

    /** 出荷状態 */
    private String shipmentStatus;

    /** 配送方法備考 */
    private String deliveryNote;

    /** 入金日時 */
    private Timestamp receiptTime;

    /** 異常フラグ */
    private HTypeEmergencyFlag emergencyFlag;

    /**
     * 入金状態
     * "1"=未入金
     * "2"=入金済み
     * "3"=過不足
     */
    private String paymentStatus;

    /** 配送方法名称 */
    private String deliveryMethodName;

    /** 検索結果表示用受注状態 */
    private String orderStatusForSearchResult;

    /** ノベルティプレゼント判定状態 */
    private String noveltyPresentJudgmentStatus;
}
