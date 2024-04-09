/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import lombok.Data;

import java.util.Date;

/**
 * 改訂用配送伝票の配送条件更新パラメータ
 */
@Data
public class UpdateShippingConditionParam {

    /** 改訂用取引ID */
    private TransactionRevisionId transactionRevisionId;

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
