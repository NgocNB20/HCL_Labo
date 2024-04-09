/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import lombok.Data;

/**
 * 改訂用請求伝票を入金済みにするユースケースDto
 */
@Data
public class DepositedBillingSlipForRevisionUseCaseDto {

    /** 入金不足フラグ */
    private boolean insufficientMoneyFlag;

    /** 入金超過フラグ */
    private boolean overMoneyFlag;
}
