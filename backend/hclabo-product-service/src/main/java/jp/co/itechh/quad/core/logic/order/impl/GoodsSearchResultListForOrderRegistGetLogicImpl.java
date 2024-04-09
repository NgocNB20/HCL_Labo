/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.order.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultForOrderRegistDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.order.GoodsSearchResultListForOrderRegistGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新規受注登録用商品検索結果リスト取得ロジック
 *
 * @author nakamura
 *
 */
@Component
public class GoodsSearchResultListForOrderRegistGetLogicImpl extends AbstractShopLogic
                implements GoodsSearchResultListForOrderRegistGetLogic {

    /** 商品DAO */
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsSearchResultListForOrderRegistGetLogicImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    /**
     * 実行メソッド
     *
     * @param searchCondition 商品Dao用検索条件Dto(管理機能)
     * @return 商品検索結果DTOリスト
     */
    @Override
    public List<GoodsSearchResultForOrderRegistDto> execute(GoodsSearchForBackDaoConditionDto searchCondition) {

        // (1)パラメータチェック
        ArgumentCheckUtil.assertNotNull("goodsSearchForBackDaoConditionDto", searchCondition);

        // (2)商品検索結果リスト取得処理
        List<GoodsSearchResultForOrderRegistDto> goodsSearchResultDtoList =
                        goodsDao.getSearchGoodsForOrderRegist(searchCondition,
                                                              searchCondition.getPageInfo().getSelectOptions()
                                                             );

        return goodsSearchResultDtoList;
    }
}