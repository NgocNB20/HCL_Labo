/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.entity.shop.settlement;

import jp.co.itechh.quad.front.constant.type.HTypeBillType;
import jp.co.itechh.quad.front.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.front.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 決済方法クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class SettlementMethodEntity implements Serializable {

    /**
     * シリアル
     */
    private static final long serialVersionUID = -2430963983831930190L;

    /** 決済方法SEQ（必須） */
    private Integer settlementMethodSeq;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 決済方法名（必須） */
    private String settlementMethodName;

    /** 決済方法表示名PC */
    private String settlementMethodDisplayNamePC;

    /** 公開状態PC（必須） */
    private HTypeOpenDeleteStatus openStatusPC = HTypeOpenDeleteStatus.NO_OPEN;

    /** 決済方法説明文PC */
    private String settlementNotePC;

    /** 決済方法種別（必須） */
    private HTypeSettlementMethodType settlementMethodType;

    /** 決済方法手数料種別（必須） */
    private HTypeSettlementMethodCommissionType settlementMethodCommissionType;

    /** 請求種別（必須） */
    private HTypeBillType billType;

    /** 配送方法SEQ (FK) */
    private Integer deliveryMethodSeq;

    /** 一律手数料 */
    private BigDecimal equalsCommission = new BigDecimal(0);

    /** 決済方法金額別手数料フラグ（必須） */
    private HTypeSettlementMethodPriceCommissionFlag settlementMethodPriceCommissionFlag;

    /** 高額割引下限金額 */
    private BigDecimal largeAmountDiscountPrice = new BigDecimal(0);

    /** 高額割引手数料 */
    private BigDecimal largeAmountDiscountCommission = new BigDecimal(0);

    /** 表示順 */
    private Integer orderDisplay;

    /** 最大購入金額 */
    private BigDecimal maxPurchasedPrice = new BigDecimal(0);

    /** 支払期限猶予日数 */
    private Integer paymentTimeLimitDayCount = 0;

    /** 最小購入金額 */
    private BigDecimal minPurchasedPrice = new BigDecimal(0);

    /** 期限後取消猶予日数 */
    private Integer cancelTimeLimitDayCount = 0;

    /** 決済関連メール要否フラグ */
    private HTypeMailRequired settlementMailRequired;

    /** ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ */
    private HTypeEffectiveFlag enableCardNoHolding;

    /** ｸﾚｼﾞｯﾄｾｷｭﾘﾃｨｺｰﾄﾞ有効化ﾌﾗｸﾞ */
    private HTypeEffectiveFlag enableSecurityCode;

    /** ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ */
    private HTypeEffectiveFlag enable3dSecure;

    /** ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ */
    private HTypeEffectiveFlag enableInstallment;

    /** ｸﾚｼﾞｯﾄﾎﾞｰﾅｽ一括支払有効化ﾌﾗｸﾞ */
    private HTypeEffectiveFlag enableBonusSinglePayment = HTypeEffectiveFlag.INVALID;

    /** ｸﾚｼﾞｯﾄﾎﾞｰﾅｽ分割支払有効化ﾌﾗｸﾞ */
    private HTypeEffectiveFlag enableBonusInstallment = HTypeEffectiveFlag.INVALID;

    /** ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ */
    private HTypeEffectiveFlag enableRevolving;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

}