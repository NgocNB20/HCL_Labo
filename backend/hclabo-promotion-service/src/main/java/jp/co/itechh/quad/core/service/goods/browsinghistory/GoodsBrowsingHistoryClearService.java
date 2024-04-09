/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.browsinghistory;

/**
 *
 * あしあと商品クリアクラス<br/>
 * あしあと商品情報を削除します。<br/>
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
public interface GoodsBrowsingHistoryClearService {

    // SGT0002

    /**
     * あしあと商品クリア<br/>
     * あしあと商品情報を削除します。<br/>
     *
     * @param accessUid 端末識別情報
     */
    void execute(String accessUid);
}
