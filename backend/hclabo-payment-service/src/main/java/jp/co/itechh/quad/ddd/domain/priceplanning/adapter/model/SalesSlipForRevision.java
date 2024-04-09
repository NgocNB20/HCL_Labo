/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model;

import lombok.Data;

/**
 * 改訂用販売伝票<br/>
 */
@Data
public class SalesSlipForRevision extends SalesSlip {

    public String salesSlipRevisionId;

    public String transactionId;

    public String salesSlipId;
}
