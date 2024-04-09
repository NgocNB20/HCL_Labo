/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter;

import jp.co.itechh.quad.ddd.domain.payment.adapter.model.MulpayBill;

/**
 * マルチペイメントアダプター
 */
public interface IMulpayBillAdapter {

    /**
     * マルチペイメント請求取得
     *
     * @param orderCode 受注番号
     * @return マルチペイメント
     */
    MulpayBill getByOrderCode(String orderCode);
}
