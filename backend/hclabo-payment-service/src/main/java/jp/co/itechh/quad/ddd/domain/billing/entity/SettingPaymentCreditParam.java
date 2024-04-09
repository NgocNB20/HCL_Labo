/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.entity;

import lombok.Data;

/**
 * 決済方法設定 クレジットparam
 */
@Data
public class SettingPaymentCreditParam {

    /** 決済トークン */
    private String paymentToken;

    /** マスク済みカード番号 */
    private String maskedCardNo;

    /** 有効期限(月) */
    private String expirationMonth;

    /** 有効期限(年) */
    private String expirationYear;

    /** 支払区分（1：一括, 2：分割, 5：リボ） */
    private String paymentType;

    /** 分割回数 */
    private String dividedNumber;

    /** カード保存フラグ（保存時true） */
    private boolean registCardFlag;

    /** 登録済カード使用フラグ（登録済みtrue） */
    private boolean useRegistedCardFlag;
}
