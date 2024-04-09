/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.billingslip.presentation.api.param.WarningContent;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 改訂用請求伝票を取消するユースケースDto
 */
@Data
public class CancelBillingSlipForRevisionUseCaseDto {

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
