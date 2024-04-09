/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.relation.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationSearchForDaoConditionDto;
import jp.co.itechh.quad.core.logic.goods.relation.GoodsRelationListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.relation.RelationGoodsListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 関連商品情報取得ロジック
 *
 * @author ozaki
 */
@Service
public class RelationGoodsListGetServiceImpl extends AbstractShopService implements RelationGoodsListGetService {

    /**
     * 関連商品情報取得ロジッククラス
     */
    private final GoodsRelationListGetLogic goodsRelationListGetLogic;

    @Autowired
    public RelationGoodsListGetServiceImpl(GoodsRelationListGetLogic goodsRelationListGetLogic) {
        this.goodsRelationListGetLogic = goodsRelationListGetLogic;
    }

    /**
     * 関連商品情報取得（公開状態のみ取得の場合はコンディションに設定すること）
     *
     * @param conditionDto 関連商品検索条件DTO
     * @return 関連商品リスト情報DTO
     */
    @Override
    public List<GoodsRelationDto> execute(GoodsRelationSearchForDaoConditionDto conditionDto) {
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);
        conditionDto.getPageInfo().setSkipCountFlg(true);

        // 関連商品詳細情報リスト取得処理を実行する
        return goodsRelationListGetLogic.execute(conditionDto);
    }

}