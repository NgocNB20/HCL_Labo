/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.order;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultForOrderRegistDto;

import java.util.List;

/**
 * 新規受注登録用商品検索結果リスト取得サービス
 *
 * @author nakamura
 * @version $Revision: 1.1 $
 */
public interface GoodsSearchResultListForOrderRegistGetService {

    // SOO0025

    /**
     * 実行メソッド
     *
     * @param siteType        サイト区分
     * @param searchCondition 商品Dao用検索条件Dto
     * @return 商品検索結果Dtoリスト
     */
    List<GoodsSearchResultForOrderRegistDto> execute(HTypeSiteType siteType,
                                                     GoodsSearchForBackDaoConditionDto searchCondition);
}