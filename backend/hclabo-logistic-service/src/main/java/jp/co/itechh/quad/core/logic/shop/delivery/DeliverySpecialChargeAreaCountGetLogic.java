/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

/**
 * 配送特別料金エリアカウント取得ロジック
 *
 * @author negishi
 * @version $Revision: 1.1 $
 */
public interface DeliverySpecialChargeAreaCountGetLogic {

    /**
     * ロジック実行<br/>
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 件数
     */
    int execute(Integer deliveryMethodSeq);

}
