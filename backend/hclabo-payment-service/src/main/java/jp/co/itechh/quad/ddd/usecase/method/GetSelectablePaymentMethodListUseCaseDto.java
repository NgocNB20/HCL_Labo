/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.method;

import lombok.Data;

/**
 * 選択可能決済方法一覧取得ユースケースDto
 */
@Data
public class GetSelectablePaymentMethodListUseCaseDto {

    /** 決済方法ID */
    private String paymentMethodId;

    /** 決済方法名 */
    private String paymentMethodName;

    /** 決済方法説明文 */
    private String paymentMethodNote;

    /** 請求種別 */
    private String billingType;

    /** 決済種別 */
    private String settlementMethodType;

    /** クレジットリボ有効化フラグ */
    private boolean enableRevolvingFlag;

    /** クレジット分割支払有効化フラク */
    private boolean enableInstallmentFlag;

    /** クレジット選択可能分割回数 */
    private String installmentCounts;

}
