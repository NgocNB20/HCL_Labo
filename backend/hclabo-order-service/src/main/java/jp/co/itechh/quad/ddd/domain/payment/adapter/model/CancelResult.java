/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 入金済み結果
 */
@Data
public class CancelResult {

    /** 入金済フラグ（入金充足したかどうか） */
    private boolean paidFlag;

    /** 入金不足フラグ */
    private boolean insufficientMoneyFlag;

    /** 入金超過フラグ */
    private boolean overMoneyFlag;

    /** GMO通信フラグ */
    private boolean gmoCommunicationFlag;

    /** 警告メッセージ */
    private Map<String, List<WarningContent>> warningMessage;
}