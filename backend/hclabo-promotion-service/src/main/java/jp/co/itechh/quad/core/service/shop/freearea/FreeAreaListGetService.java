/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.freearea;

import jp.co.itechh.quad.core.dto.shop.freearea.FreeAreaSearchDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;

import java.util.List;

/**
 * フリーエリアリスト取得
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface FreeAreaListGetService {

    /**
     *
     * フリーエリアリスト取得
     *
     * @param conditionDto 検索条件
     * @return フリーエリアエンティティリスト
     */
    List<FreeAreaEntity> execute(FreeAreaSearchDaoConditionDto conditionDto);

}
