/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.settlement;

import jp.co.itechh.quad.core.constant.type.HTypeBillType;
import jp.co.itechh.quad.core.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
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
@Entity
@Table(name = "SettlementMethod")
@Data
@Component
@Scope("prototype")
public class SettlementMethodEntity implements Serializable {

    /**
     * シリアル
     */
    private static final long serialVersionUID = -2430963983831930190L;

    /** 決済方法SEQ（必須） */
    @Column(name = "settlementMethodSeq")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "settlementMethodSeq")
    private Integer settlementMethodSeq;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 決済方法名（必須） */
    @Column(name = "settlementMethodName")
    private String settlementMethodName;

    /** 決済方法表示名PC */
    @Column(name = "settlementMethodDisplayNamePC")
    private String settlementMethodDisplayNamePC;

    /** 公開状態PC（必須） */
    @Column(name = "openStatusPC")
    private HTypeOpenDeleteStatus openStatusPC = HTypeOpenDeleteStatus.NO_OPEN;

    /** 決済方法説明文PC */
    @Column(name = "settlementNotePC")
    private String settlementNotePC;

    /** 決済方法種別（必須） */
    @Column(name = "settlementMethodType")
    private HTypeSettlementMethodType settlementMethodType;

    /** 決済方法手数料種別（必須） */
    @Column(name = "settlementMethodCommissionType")
    private HTypeSettlementMethodCommissionType settlementMethodCommissionType;

    /** 請求種別（必須） */
    @Column(name = "billType")
    private HTypeBillType billType;

    /** 配送方法SEQ (FK) */
    @Column(name = "deliveryMethodSeq")
    private Integer deliveryMethodSeq;

    /** 一律手数料 */
    @Column(name = "equalsCommission")
    private BigDecimal equalsCommission = new BigDecimal(0);

    /** 決済方法金額別手数料フラグ（必須） */
    @Column(name = "settlementMethodPriceCommissionFlag")
    private HTypeSettlementMethodPriceCommissionFlag settlementMethodPriceCommissionFlag;

    /** 高額割引下限金額 */
    @Column(name = "largeAmountDiscountPrice")
    private BigDecimal largeAmountDiscountPrice = new BigDecimal(0);

    /** 高額割引手数料 */
    @Column(name = "largeAmountDiscountCommission")
    private BigDecimal largeAmountDiscountCommission = new BigDecimal(0);

    /** 表示順 */
    @Column(name = "orderDisplay")
    private Integer orderDisplay;

    /** 最大購入金額 */
    @Column(name = "maxPurchasedPrice")
    private BigDecimal maxPurchasedPrice = new BigDecimal(0);

    /** 最小購入金額 */
    @Column(name = "minPurchasedPrice")
    private BigDecimal minPurchasedPrice = new BigDecimal(0);

    /** 決済関連メール要否フラグ */
    @Column(name = "settlementMailRequired")
    private HTypeMailRequired settlementMailRequired;

    /** ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ */
    @Column(name = "enableCardNoHolding")
    private HTypeEffectiveFlag enableCardNoHolding;

    /** ｸﾚｼﾞｯﾄｾｷｭﾘﾃｨｺｰﾄﾞ有効化ﾌﾗｸﾞ */
    @Column(name = "enableSecurityCode")
    private HTypeEffectiveFlag enableSecurityCode;

    /** ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ */
    @Column(name = "enable3dSecure")
    private HTypeEffectiveFlag enable3dSecure;

    /** ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ */
    @Column(name = "enableInstallment")
    private HTypeEffectiveFlag enableInstallment;

    /** ｸﾚｼﾞｯﾄﾎﾞｰﾅｽ一括支払有効化ﾌﾗｸﾞ */
    @Column(name = "enableBonusSinglePayment")
    private HTypeEffectiveFlag enableBonusSinglePayment = HTypeEffectiveFlag.INVALID;

    /** ｸﾚｼﾞｯﾄﾎﾞｰﾅｽ分割支払有効化ﾌﾗｸﾞ */
    @Column(name = "enableBonusInstallment")
    private HTypeEffectiveFlag enableBonusInstallment = HTypeEffectiveFlag.INVALID;

    /** ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ */
    @Column(name = "enableRevolving")
    private HTypeEffectiveFlag enableRevolving;

    /** 登録日時（必須） */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}
