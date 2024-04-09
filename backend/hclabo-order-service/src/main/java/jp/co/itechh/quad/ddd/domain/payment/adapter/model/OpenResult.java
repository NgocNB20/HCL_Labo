/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 請求伝票確定結果
 */
@Data
@Component
@Scope("prototype")
public class OpenResult {

    /** 入金済みフラグ */
    private boolean paidFlag = false;

    /** 入金関連通知実施フラグ */
    private boolean notificationFlag = false;

    /** 前請求フラグ */
    private boolean preClaimFlag = false;

}
