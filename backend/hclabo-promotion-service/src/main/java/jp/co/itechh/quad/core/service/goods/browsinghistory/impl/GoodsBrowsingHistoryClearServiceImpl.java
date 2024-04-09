/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.browsinghistory.impl;

import jp.co.itechh.quad.core.logic.goods.browsinghistory.GoodsBrowsingHistoryClearLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.browsinghistory.GoodsBrowsingHistoryClearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * あしあと商品クリアクラス<br/>
 * あしあと商品情報を削除します。<br/>
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
@Service
public class GoodsBrowsingHistoryClearServiceImpl extends AbstractShopService
                implements GoodsBrowsingHistoryClearService {

    /**
     * あしあと情報クリアロジッククラス
     */
    private final GoodsBrowsingHistoryClearLogic goodsBrowsingHistoryClearLogic;

    @Autowired
    public GoodsBrowsingHistoryClearServiceImpl(GoodsBrowsingHistoryClearLogic goodsBrowsingHistoryClearLogic) {
        this.goodsBrowsingHistoryClearLogic = goodsBrowsingHistoryClearLogic;
    }

    /**
     * あしあと商品クリア<br/>
     * あしあと商品情報を削除します。<br/>
     *
     * @param accessUid 端末識別情報
     */
    @Override
    public void execute(String accessUid) {

        // ・ショップSEQ ： null（or空文字）の場合 処理を終了する
        // ・端末識別情報 ： null（or空文字）の場合 処理を終了する
        // ・会員SEQ ： null（or空文字）以外の場合
        // (2)の処理には会員SEQと端末識別情報をパラメータに設定する
        // null（or空文字）の場合
        // (2)の処理には端末識別情報をパラメータに設定する
        Integer shopSeq = 1001;
        if (accessUid == null || accessUid.equals("")) {
            return;
        }

        // (2) あしあとクリア処理実行
        goodsBrowsingHistoryClearLogic.execute(shopSeq, accessUid);

    }

}