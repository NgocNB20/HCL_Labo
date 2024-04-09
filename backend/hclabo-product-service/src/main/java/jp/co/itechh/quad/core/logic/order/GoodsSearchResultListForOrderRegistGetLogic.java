/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.order;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultForOrderRegistDto;

import java.util.List;

/**
 *
 * 新規受注登録用商品検索結果リスト取得
 *
 * @author nakamura
 * @version $Revision: 1.1 $
 *
 */
public interface GoodsSearchResultListForOrderRegistGetLogic {

    // LOO0037

    /**
     *
     * 実行メソッド
     *
     * @param searchCondition 商品Dao用検索条件（管理機能）Dto
     * @return 商品検索結果DTOリスト
     */
    List<GoodsSearchResultForOrderRegistDto> execute(GoodsSearchForBackDaoConditionDto searchCondition);
}