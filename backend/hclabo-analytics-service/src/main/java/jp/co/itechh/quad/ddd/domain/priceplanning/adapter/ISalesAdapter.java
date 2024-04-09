/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.priceplanning.adapter;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;

/**
 * 販売企画アダプター
 */
public interface ISalesAdapter {

    /**
     * 販売企画マイクロサービス
     * 販売伝票取得
     *
     * @param transactionId 取引ID
     * @return SalesSlip 販売伝票
     */
    SalesSlip getSalesSlip(String transactionId);

}
