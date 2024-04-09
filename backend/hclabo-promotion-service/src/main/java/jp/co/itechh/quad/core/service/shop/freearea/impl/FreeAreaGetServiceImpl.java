/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.freearea.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.core.logic.shop.freearea.FreeAreaGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.freearea.FreeAreaGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * フリーエリア情報取得サービス
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
@Service
public class FreeAreaGetServiceImpl extends AbstractShopService implements FreeAreaGetService {
    /**
     * フリーエリア情報取得ロジック
     */
    private final FreeAreaGetLogic freeAreaGetLogic;

    @Autowired
    public FreeAreaGetServiceImpl(FreeAreaGetLogic freeAreaGetLogic) {
        this.freeAreaGetLogic = freeAreaGetLogic;
    }

    /**
     * フリーエリア情報取得処理
     *
     * @param freeAreaKey フリーエリアキー
     * @return フリーエリアエンティティ
     */
    @Override
    public FreeAreaEntity execute(String freeAreaKey) {
        // パラメータチェック
        Integer shopSeq = 1001;
        ArgumentCheckUtil.assertGreaterThanZero("commonInfo.shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotEmpty("freeAreaKey", freeAreaKey);

        // フリーエリア情報取得 取得エラーなし
        return freeAreaGetLogic.execute(shopSeq, freeAreaKey);
    }

    /**
     * プレビュー時フリーエリア情報取得処理
     *
     * @param freeAreaSeq フリーエリアSEQ
     * @return フリーエリアエンティティ
     */
    @Override
    public FreeAreaEntity execute(Integer freeAreaSeq) {

        // パラメータチェック
        Integer shopSeq = 1001;
        ArgumentCheckUtil.assertGreaterThanZero("commonInfo.shopSeq", shopSeq);

        // フリーエリア情報取得 取得エラーなし
        return freeAreaGetLogic.execute(shopSeq, freeAreaSeq);
    }
}