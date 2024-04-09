/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.logic.order;

import jp.co.itechh.quad.core.dto.order.OrderReceivedDto;

/**
 * 受注取得ロジック
 * TODO 外部APIを呼び出すので、本来はロジックではなく、アダプターが正しい
 *
 * @author kimura
 */
public interface OrderReceivedGetLogic {

    /**
     * 受注ご注文主情報取得失敗<br/>
     * <code>MSGCD_ORDERPERSONENTITYDTO_NULL</code><br/>
     */
    public static final String MSGCD_ORDERPERSONENTITYDTO_NULL = "SOO000601";

    /**
     * ロジック実行<br/>
     *
     * @param orderCode 受注番号
     * @return 受注Dto
     */
    OrderReceivedDto execute(String orderCode);
}
