/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.order.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultForOrderRegistDto;
import jp.co.itechh.quad.core.logic.order.GoodsSearchResultListForOrderRegistGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.order.GoodsSearchResultListForOrderRegistGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新規受注登録用商品管理機能詳細リスト取得サービス実装クラス
 *
 * @author nakamura
 */
@Service
public class GoodsSearchResultListForOrderRegistGetServiceImpl extends AbstractShopService
                implements GoodsSearchResultListForOrderRegistGetService {

    /**
     * 商品検索結果リスト取得ロジック
     */
    private final GoodsSearchResultListForOrderRegistGetLogic goodsSearchResultListForOrderRegistGetLogic;

    @Autowired
    public GoodsSearchResultListForOrderRegistGetServiceImpl(GoodsSearchResultListForOrderRegistGetLogic goodsSearchResultListForOrderRegistGetLogic) {
        this.goodsSearchResultListForOrderRegistGetLogic = goodsSearchResultListForOrderRegistGetLogic;
    }

    /**
     * 実行メソッド
     *
     * @param siteType        サイト区分
     * @param searchCondition 商品Dao用検索条件Dto
     * @return 商品検索結果Dtoリスト
     */
    @Override
    public List<GoodsSearchResultForOrderRegistDto> execute(HTypeSiteType siteType,
                                                            GoodsSearchForBackDaoConditionDto searchCondition) {

        // (1)パラメータチェック
        ArgumentCheckUtil.assertNotNull("searchCondition", searchCondition);

        // (2) 共通情報チェック
        // ショップSEQ ： null(or 0) の場合 エラーとして処理を終了する
        // サイト区分 ： null(or 空文字) の場合 エラーとして処理を終了する
        Integer shopSeq = 1001;

        // (3)検索条件Dtoの編集
        searchCondition.setShopSeq(shopSeq);
        searchCondition.setSiteType(siteType);

        // (4)Logic処理を実行
        List<GoodsSearchResultForOrderRegistDto> goodsSearchResultDtoList =
                        goodsSearchResultListForOrderRegistGetLogic.execute(searchCondition);

        // (5)戻り値
        return goodsSearchResultDtoList;
    }
}