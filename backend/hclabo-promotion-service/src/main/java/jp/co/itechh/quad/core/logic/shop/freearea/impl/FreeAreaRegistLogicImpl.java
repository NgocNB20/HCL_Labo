/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.freearea.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.shop.freearea.FreeAreaDao;
import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.freearea.FreeAreaRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * フリーエリア登録
 *
 * @author shibuya
 */
@Component
public class FreeAreaRegistLogicImpl extends AbstractShopLogic implements FreeAreaRegistLogic {

    /** フリーエリアDao */
    private final FreeAreaDao freeAreaDao;

    @Autowired
    public FreeAreaRegistLogicImpl(FreeAreaDao freeAreaDao) {
        this.freeAreaDao = freeAreaDao;
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

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 登録・更新日をセット
        Timestamp currentTime = dateUtility.getCurrentTime();
        freeAreaEntity.setRegistTime(currentTime);
        freeAreaEntity.setUpdateTime(currentTime);

        // 登録処理
        return freeAreaDao.insert(freeAreaEntity);
    }

    /**
     * パラメータチェック
     *
     * @param freeAreaEntity フリーエリアエンティティ
     */
    protected void checkParam(FreeAreaEntity freeAreaEntity) {

        ArgumentCheckUtil.assertNotNull("FreeAreaEntity", freeAreaEntity);
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", freeAreaEntity.getShopSeq());
        ArgumentCheckUtil.assertNotEmpty("freeAreaKey", freeAreaEntity.getFreeAreaKey());
        ArgumentCheckUtil.assertNotNull("openStartTime", freeAreaEntity.getOpenStartTime());
    }

}
