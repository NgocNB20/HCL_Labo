/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter;

/**
 * リンク支払いアダプター</br>
 * 決済マイクロサービス
 */
public interface ILinkPayAdapter {

    /**
     * リンク決済(後日払い)の支払期限切れ間近を判定する
     *
     * @param transactionId 取引ID
     * @return TRUE.. 支払期限間近フラグ
     */
    Boolean checkLaterDatePaymentReminder(String transactionId);

    /**
     * リンク決済(後日払い)の支払期限切れ判定する
     *
     * @param transactionId 取引ID
     * @return TRUE.. 支払期限切れフラグ
     */
    Boolean checkLaterDatePaymentExpired(String transactionId);
}