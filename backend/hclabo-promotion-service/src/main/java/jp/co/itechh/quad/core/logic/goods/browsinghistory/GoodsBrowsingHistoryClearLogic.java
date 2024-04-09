/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.browsinghistory;

/**
 *
 * あしあとクリア<br/>
 * あしあと商品リストを削除する。<br/>
 *
 * @author ozaki
 * @version $Revision: 1.2 $
 *
 */
public interface GoodsBrowsingHistoryClearLogic {

    // LGT0003

    /**
     *
     * あしあとクリア<br/>
     * あしあと商品リストを削除する。<br/>
     *
     * @param shopSeq ショップSEQ
     * @param accessUid 端末識別ID
     * @return 削除件数
     */
    int execute(Integer shopSeq, String accessUid);
}
