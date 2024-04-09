/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.order;

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
import jp.co.itechh.quad.admin.constant.type.HTypeReservationDeliveryFlag;
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
 * 受注検索受注一覧用Dtoクラス
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class OrderSearchOrderResultDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 受注SEQ */
    private Integer orderSeq;

    /** 受注履歴連番 */
    private Integer orderVersionNo;

    /** 受注種別 */
    private HTypeOrderType orderType;

    /** 受注コード */
    private String orderCode;

    /** 受注日時 */
    private Timestamp orderTime;

    /** 売上日時 */
    private Timestamp salesTime;

    /** キャンセル日時 */
    private Timestamp cancelTime;

    /** 売上フラグ */
    private HTypeSalesFlag salesFlag;

    /** キャンセルフラグ */
    private HTypeCancelFlag cancelFlag;

    /** 保留中フラグ */
    private HTypeWaitingFlag waitingFlag;

    /** 受注状態 */
    private HTypeOrderStatus orderStatus;

    /** 商品金額合計 */
    private BigDecimal goodsPriceTotal;

    /** 配送料 */
    private BigDecimal orderDeliveryCarriage;

    /** 受注金額 */
    private BigDecimal orderPrice;

    /** 入金累計 */
    private BigDecimal receiptPriceTotal;

    /** 受注サイト種別 */
    private HTypeSiteType orderSiteType;

    /** 受注デバイス種別 */
    private HTypeDeviceType orderDeviceType;

    /** キャリア種別 */
    private HTypeCarrierType carrierType;

    /** 決済方法SEQ */
    private Integer settlementMethodSeq;

    /** 会員SEQ */
    private Integer memberInfoSeq;

    /** 都道府県種別 */
    private HTypePrefectureType prefectureType;

    /** ご注文主性別 */
    private HTypeOrderSex orderSex;

    /** ご注文主年代 */
    private HTypeOrderAgeType orderAgeType;

    /** リピート購入種別 */
    private HTypeRepeatPurchaseType repeatPurchaseType;

    /** 更新カウンタ */
    private Integer versionNo;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 決済関連メール要否フラグ */
    private HTypeMailRequired settlementMailRequired;

    /** 督促メール送信済みフラグ */
    private HTypeSend reminderSentFlag;

    /** 期限切れメール送信済みフラグ */
    private HTypeSend expiredSentFlag;

    /** クーポン適用前受注金額 */
    private BigDecimal preCouponDiscountOrderPrice;

    /** ユーザーエージェント */
    private String userAgent;

    /** フリーエリアキー */
    private String freeAreaKey;

    /** 登録日時 */
    private Timestamp registTime;

    /** 更新日時 */
    private Timestamp updateTime;

    /** 受注商品連番 */
    private Integer orderGoodsVersionNo;

    // クーポン情報
    /** クーポン割引額 */
    private BigDecimal couponDiscountPrice;

    // 決済方法情報
    /** 決済方法名 */
    private String paymentMethodName;

    /** リンク決済手段名 */
    private String linkPaymentName;

    // 受注配送方法情報
    /** お届け時間帯 */
    private String receiverTimeZone;

    /** ご注文主氏名 */
    private String orderName;

    /** ご注文主フリガナ */
    private String orderKana;

    /** ご注文主電話番号 */
    private String billingTel;

    /** ご注文主連絡先電話番号 */
    private String customerTel;

    /** ご注文主メールアドレス */
    private String orderMail;

    /** お届け先氏名 */
    private String shippingName;

    /** お届け先フリガナ */
    private String shippingKana;

    /** お届け先電話番号 */
    private String shippingTel;

    /** 注文連番 */
    private Integer orderConsecutiveNo;

    /** 伝票番号 */
    private String deliveryCode;

    /** 出荷日 */
    private Timestamp shipmentdate;

    /** 出荷状態 */
    private String shipmentStatus;

    /** 予約配送フラグ */
    private HTypeReservationDeliveryFlag reservationDeliveryFlag;

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
    private String shippingMethodName;

    /** 検索結果表示用受注状態 */
    private String orderStatusForSearchResult;
}
