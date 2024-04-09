/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.priceplanning.adapter;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlip;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.SalesSlipForRevision;

/**
 * 販売伝票 アダプター<br/>
 * 販売企画マイクロサービス
 */
public interface ISalesAdapter {

    /**
     * 販売伝票取得
     *
     * @param transactionId 取引ID
     * @return SalesSlip 販売伝票
     */
    SalesSlip getSalesSlip(String transactionId);

    /**
     * 改訂用販売伝票取得
     *
     * @param transactionRevisionId 取引ID
     * @return SalesSlipForRevision 改訂用販売伝票
     */
    SalesSlipForRevision getSalesSlipForRevision(String transactionRevisionId);

}
