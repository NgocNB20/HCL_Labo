/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import lombok.Data;

import java.util.Date;

/**
 * お届け先パラメータ
 */
@Data
public class ReceiverUseCaseParam {

    /** 配送先住所ID */
    private String shippingAddressId;

    /** 配送方法ID */
    private String shippingMethodId;

    /** お届け希望日 */
    private Date receiverDate;

    /** お届け希望時間帯 */
    private String receiverTimeZone;

    /** 納品書要否フラグ */
    private boolean invoiceNecessaryFlag;

    /** 配送状況確認番号 */
    private String shipmentStatusConfirmCode;
}
