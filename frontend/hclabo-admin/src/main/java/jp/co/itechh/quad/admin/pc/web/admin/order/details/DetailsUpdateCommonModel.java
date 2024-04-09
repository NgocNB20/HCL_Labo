/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import lombok.Data;

/**
 * 受注修正共通共通Model
 */
@Data
public class DetailsUpdateCommonModel {

    /** 受注番号 */
    private String orderCode;

    /** 取引ID */
    private String transactionId;

    /** 取引ID */
    private String transactionRevisionId;

}
