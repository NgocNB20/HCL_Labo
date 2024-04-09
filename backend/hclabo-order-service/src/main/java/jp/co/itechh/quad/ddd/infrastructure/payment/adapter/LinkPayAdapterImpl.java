/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.payment.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.payment.adapter.ILinkPayAdapter;
import jp.co.itechh.quad.linkpay.presentation.api.LinkPayApi;
import jp.co.itechh.quad.linkpay.presentation.api.param.LaterDatePaymentExpiredCheckRequest;
import jp.co.itechh.quad.linkpay.presentation.api.param.LaterDatePaymentReminderCheckRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * リンク支払いアダプタクラス
 *
 * @author Pham Quang Dieu (VJP)
 */
@Component
public class LinkPayAdapterImpl implements ILinkPayAdapter {

    /** リンク決済API */
    private final LinkPayApi linkPayApi;

    /** コンストラクタ */
    @Autowired
    public LinkPayAdapterImpl(LinkPayApi linkPayApi, HeaderParamsUtility headerParamsUtil) {
        this.linkPayApi = linkPayApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.linkPayApi.getApiClient());
    }

    /**
     * リンク決済(後日払い)の支払期限切れ間近を判定する
     *
     * @param transactionId 取引ID
     * @return TRUE.. 支払期限間近フラグ
     */
    @Override
    public Boolean checkLaterDatePaymentReminder(String transactionId) {
        LaterDatePaymentReminderCheckRequest laterDatePaymentReminderCheckRequest =
                        new LaterDatePaymentReminderCheckRequest();
        laterDatePaymentReminderCheckRequest.setTransactionId(transactionId);
        return linkPayApi.checkLaterDatePaymentReminder(laterDatePaymentReminderCheckRequest).getDueDateFlag();
    }

    /**
     * リンク決済(後日払い)の支払期限切れ判定する
     *
     * @param transactionId 取引ID
     * @return TRUE.. 支払期限切れフラグ
     */
    @Override
    public Boolean checkLaterDatePaymentExpired(String transactionId) {
        LaterDatePaymentExpiredCheckRequest laterDatePaymentExpiredCheckRequest =
                        new LaterDatePaymentExpiredCheckRequest();
        laterDatePaymentExpiredCheckRequest.setTransactionId(transactionId);
        return linkPayApi.checkLaterDatePaymentExpired(laterDatePaymentExpiredCheckRequest).getExpiredFlag();
    }
}