/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.relation.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsRelationDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.relation.GoodsRelationListGetForBackLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 関連商品リスト取得（バック用）
 * 対象商品の関連商品リストを取得する。
 *
 * @author hirata
 * @version $Revision: 1.1 $
 *
 */
@Component
public class GoodsRelationListGetForBackLogicImpl extends AbstractShopLogic
                implements GoodsRelationListGetForBackLogic {

    /**
     * 関連商品DAO
     */
    private final GoodsRelationDao goodsRelationDao;

    @Autowired
    public GoodsRelationListGetForBackLogicImpl(GoodsRelationDao goodsRelationDao) {
        this.goodsRelationDao = goodsRelationDao;
    }

    /**
     *
     * 関連商品リスト取得（バック用）
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 関連商品エンティティリスト
     */
    @Override
    public List<GoodsRelationEntity> execute(Integer goodsGroupSeq) {

        // (1) パラメータチェック
        // 商品グループSEQが null でないことをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeq", goodsGroupSeq);

        // (2) 関連商品リスト取得
        // 関連商品Daoの関連商品マップ検索処理を実行する。
        List<GoodsRelationEntity> goodsRelationEntityList =
                        goodsRelationDao.getGoodsRelationListByGoodsGroupSeq(goodsGroupSeq);

        // (3) 戻り値
        // （戻り値用）関連商品DTOリストを返す。
        return goodsRelationEntityList;

    }

}