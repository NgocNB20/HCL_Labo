/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import lombok.Data;

/**
 * 請求伝票確定ユースケースDto
 */
@Data
public class OpenBillingSlipUseCaseDto {

    /** 入金済みフラグ */
    private Boolean paidFlag;

    /** 入金関連通知実施フラグ */
    private Boolean notificationFlag;

    /** 前請求フラグ */
    private Boolean preClaimFlag;

}
