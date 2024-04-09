/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.freearea.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.core.logic.shop.freearea.FreeAreaCheckLogic;
import jp.co.itechh.quad.core.logic.shop.freearea.FreeAreaRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.freearea.FreeAreaRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * フリーエリア登録サービス
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Service
public class FreeAreaRegistServiceImpl extends AbstractShopService implements FreeAreaRegistService {

    /** フリーエリア登録前チェックロジック */
    private final FreeAreaCheckLogic freeAreaCheckLogic;

    /** フリーエリア登録ロジック */
    private final FreeAreaRegistLogic freeAreaRegistLogic;

    @Autowired
    public FreeAreaRegistServiceImpl(FreeAreaCheckLogic freeAreaCheckLogic, FreeAreaRegistLogic freeAreaRegistLogic) {
        this.freeAreaCheckLogic = freeAreaCheckLogic;
        this.freeAreaRegistLogic = freeAreaRegistLogic;
    }

    /**
     * フリーエリア登録
     *
     * @param freeAreaEntity フリーエリアエンティティ
     * @return 処理件数
     */
    @Override
    public int execute(FreeAreaEntity freeAreaEntity) {
        // パラメータチェック
        this.checkParam(freeAreaEntity);

        // 共通情報の取得
        Integer shopSeq = 1001;
        // パラメータにセット
        freeAreaEntity.setShopSeq(shopSeq);

        // 登録前チェック(登録不可のばあい、エラーにより処理中断)
        freeAreaCheckLogic.execute(freeAreaEntity);

        // 登録処理
        int result = this.insert(freeAreaEntity);

        return result;
    }

    /**
     * パラメータチェック
     *
     * @param freeAreaEntity フリーエリアエンティティ
     */
    protected void checkParam(FreeAreaEntity freeAreaEntity) {

        ArgumentCheckUtil.assertNotNull("FreeAreaEntity", freeAreaEntity);
        ArgumentCheckUtil.assertNotEmpty("freeAreaKey", freeAreaEntity.getFreeAreaKey());
        ArgumentCheckUtil.assertNotNull("openStartTime", freeAreaEntity.getOpenStartTime());
    }

    /**
     * 登録処理
     *
     * @param freeAreaEntity フリーエリアエンティティ
     * @return 登録件数
     */
    protected int insert(FreeAreaEntity freeAreaEntity) {
        int result = freeAreaRegistLogic.execute(freeAreaEntity);
        if (result == 0) {
            // 登録件数0件の場合、エラー
            throwMessage(MSGCD_FREEAREA_REGIST_FAIL);
        }

        return result;
    }
}