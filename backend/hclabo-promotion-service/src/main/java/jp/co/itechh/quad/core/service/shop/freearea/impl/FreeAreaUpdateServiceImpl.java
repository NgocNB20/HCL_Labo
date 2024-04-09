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
import jp.co.itechh.quad.core.logic.shop.freearea.FreeAreaUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.freearea.FreeAreaUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * フリーエリア更新サービス
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Service
public class FreeAreaUpdateServiceImpl extends AbstractShopService implements FreeAreaUpdateService {

    /** フリーエリア更新前チェックロジック */
    private final FreeAreaCheckLogic freeAreaCheckLogic;

    /** フリーエリア更新ロジック */
    private final FreeAreaUpdateLogic freeAreaUpdateLogic;

    @Autowired
    public FreeAreaUpdateServiceImpl(FreeAreaCheckLogic freeAreaCheckLogic, FreeAreaUpdateLogic freeAreaUpdateLogic) {
        this.freeAreaCheckLogic = freeAreaCheckLogic;
        this.freeAreaUpdateLogic = freeAreaUpdateLogic;
    }

    /**
     * フリーエリア更新
     *
     * @param freeAreaEntity フリーエリアエンティティ
     * @return 処理件数
     */
    @Override
    public int execute(FreeAreaEntity freeAreaEntity) {
        // パラメータチェック
        this.checkParam(freeAreaEntity);

        // 更新前チェック(更新不可のばあい、エラーにより処理中断)
        freeAreaCheckLogic.execute(freeAreaEntity);

        // 更新処理
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
        ArgumentCheckUtil.assertGreaterThanZero("freeAreaSeq", freeAreaEntity.getFreeAreaSeq());
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", freeAreaEntity.getShopSeq());
        ArgumentCheckUtil.assertNotEmpty("freeAreaKey", freeAreaEntity.getFreeAreaKey());
        ArgumentCheckUtil.assertNotNull("openStartTime", freeAreaEntity.getOpenStartTime());
    }

    /**
     * 更新処理
     *
     * @param freeAreaEntity フリーエリアエンティティ
     * @return 更新件数
     */
    protected int insert(FreeAreaEntity freeAreaEntity) {
        int result = freeAreaUpdateLogic.execute(freeAreaEntity);
        if (result == 0) {
            // 更新件数0件の場合、エラー
            throwMessage(MSGCD_FREEAREA_UPDATE_FAIL);
        }

        return result;
    }
}