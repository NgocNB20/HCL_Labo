/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.multipayment;

/**
 * マルチペイショップロジック
 *
 * @author is40701
 *
 */
public interface MulPayShopLogic {

    /**
     * マルペイショップID取得
     * @param shopSeq ショップSEQ
     * @return String マルペイショップID
     */
    String getMulPayShopId(Integer shopSeq);
}
