/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import lombok.Data;

/**
 * 改訂用請求伝票確定ユースケースDto
 */
@Data
public class OpenBillingSlipReviseUseCaseDto {

    /** 請求決済エラー解消フラグ */
    private boolean billPaymentErrorReleaseFlag;

    /** GMO通信フラグ */
    private boolean gmoCommunicationExec;
}
