/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;

/**
 * 入金済み結果
 */
@Data
public class DepositedResult {

    /** 入金不足フラグ */
    private boolean insufficientMoneyFlag;

    /** 入金超過フラグ */
    private boolean overMoneyFlag;
}
