/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.PaymentStatusDetail;
import lombok.Data;

/**
 * HIT-MALL入金結果データを確認して受注を入金済みにするユースケースDto
 */
@Data
public class ConfirmHMPaymentThenSettingPaidStatusUseCaseDto {

    /** 取引確定後受注データ */
    private OrderReceivedEntity orderReceived;

    /** 入金状態詳細 */
    private PaymentStatusDetail paymentStatusDetail;

}
