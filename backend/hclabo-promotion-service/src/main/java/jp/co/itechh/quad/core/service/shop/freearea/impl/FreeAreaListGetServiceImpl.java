/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.freearea.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dto.shop.freearea.FreeAreaSearchDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.core.logic.shop.freearea.FreeAreaListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.freearea.FreeAreaListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * フリーエリアリスト取得
 *
 * @author shibuya
 *
 */
@Service
public class FreeAreaListGetServiceImpl extends AbstractShopService implements FreeAreaListGetService {

    /** フリーエリアリスト取得ロジック */
    private final FreeAreaListGetLogic freeAreaListGetLogic;

    @Autowired
    public FreeAreaListGetServiceImpl(FreeAreaListGetLogic freeAreaListGetLogic) {
        this.freeAreaListGetLogic = freeAreaListGetLogic;
    }

    /**
     * フリーエリアリスト取得
     *
     * @param conditionDto 検索条件
     * @return フリーエリアリスト
     */
    @Override
    public List<FreeAreaEntity> execute(FreeAreaSearchDaoConditionDto conditionDto) {

        // パラメータチェック
        this.checkParam(conditionDto);

        // データの取得
        List<FreeAreaEntity> list = this.getData(conditionDto);

        return list;
    }

    /**
     * パラメータチェック
     *
     * @param conditionDto 検索条件
     */
    protected void checkParam(FreeAreaSearchDaoConditionDto conditionDto) {

        // 検索条件
        ArgumentCheckUtil.assertNotNull("FreeAreaSearchDaoConditionDto", conditionDto);

        // 共通情報の取得
        Integer shopSeq = 1001;

        // パラメータにセット
        conditionDto.setShopSeq(shopSeq);
    }

    /**
     * データ取得
     *
     * @param conditionDto 検索条件Dto
     * @return フリーエリアリスト
     */
    protected List<FreeAreaEntity> getData(FreeAreaSearchDaoConditionDto conditionDto) {
        return freeAreaListGetLogic.execute(conditionDto);
    }

}
