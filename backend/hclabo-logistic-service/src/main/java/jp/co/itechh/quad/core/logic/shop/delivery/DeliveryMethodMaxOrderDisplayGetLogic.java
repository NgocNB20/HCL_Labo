/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

/**
 * 最大表示順取得ロジック
 *
 * @author negishi
 * @version $Revision: 1.1 $
 */
public interface DeliveryMethodMaxOrderDisplayGetLogic {

    /**
     * ロジック実行
     *
     * @param shopSeq ショップSEQ
     * @return 表示順の最大値
     */
    Integer execute(Integer shopSeq);
}
