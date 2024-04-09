/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import lombok.Data;

/**
 * 請求パラメータ
 */
@Data
public class BillingUseCaseParam {

    /** 請求先住所ID */
    private String billingAddressId;
}
