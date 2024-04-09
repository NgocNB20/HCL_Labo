/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultDto;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsSearchResultListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.goods.GoodsSearchResultListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品管理機能詳細リスト取得サービス実装クラス
 *
 * @author hirata
 * @version $Revision: 1.2 $
 */
@Service
public class GoodsSearchResultListGetServiceImpl extends AbstractShopService
                implements GoodsSearchResultListGetService {

    /**
     * 商品検索結果リスト取得ロジック
     */
    private final GoodsSearchResultListGetLogic goodsSearchResultListGetLogic;

    @Autowired
    public GoodsSearchResultListGetServiceImpl(GoodsSearchResultListGetLogic goodsSearchResultListGetLogic) {
        this.goodsSearchResultListGetLogic = goodsSearchResultListGetLogic;
    }

    /**
     * 実行メソッド
     *
     * @param searchCondition 商品Dao用検索条件Dto
     * @return 商品検索結果Dtoリスト
     */
    @Override
    public List<GoodsSearchResultDto> execute(GoodsSearchForBackDaoConditionDto searchCondition) {

        // (1)パラメータチェック
        ArgumentCheckUtil.assertNotNull("searchCondition", searchCondition);

        // (2) 共通情報チェック
        Integer shopSeq = 1001;

        // (3)検索条件Dtoの編集
        searchCondition.setShopSeq(shopSeq);

        // (4)Logic処理を実行
        List<GoodsSearchResultDto> goodsSearchResultDtoList = goodsSearchResultListGetLogic.execute(searchCondition);

        // (5)戻り値
        return goodsSearchResultDtoList;
    }
}