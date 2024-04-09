/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDisplayDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDisplayGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品グループ表示取得
 *
 * @author ozaki
 * @version $Revision: 1.2 $
 */
@Component
public class GoodsGroupDisplayGetLogicImpl extends AbstractShopLogic implements GoodsGroupDisplayGetLogic {

    /** 商品グループ表示DAO */
    private final GoodsGroupDisplayDao goodsGroupDisplayDao;

    /**
     * 変換ユーティリティクラス
     */
    @Autowired
    public GoodsGroupDisplayGetLogicImpl(GoodsGroupDisplayDao goodsGroupDisplayDao) {
        this.goodsGroupDisplayDao = goodsGroupDisplayDao;
    }

    /**
     * 商品グループ表示取得
     * 商品グループSEQをもとに商品グループ表示エンティティを取得する。
     *
     * @param goodsGroupSeq 商品グループSEQリスト
     * @return 商品グループ表示情報
     */
    @Override
    public GoodsGroupDisplayEntity execute(Integer goodsGroupSeq) {

        // (1) パラメータチェック
        // 商品グループSEQが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeq", goodsGroupSeq);

        // (2) 商品グループ表示情報取得
        // 商品グループ表示Daoの商品グループ表示情報取得処理を実行する。
        // DAO GoodsGroupDisplayDao
        // メソッド 商品グループ表示エンティティ getEntity( （パラメータ）商品グループSEQ)
        GoodsGroupDisplayEntity goodsGroupDisplayEntity = goodsGroupDisplayDao.getEntity(goodsGroupSeq);

        // (3) 戻り値
        // 取得した商品グループ表示エンティティを返す。
        return goodsGroupDisplayEntity;
    }

    @Override
    public List<GoodsGroupDisplayEntity> execute(List<Integer> goodsGroupSeqList) {

        // (1) パラメータチェック
        // 商品グループSEQが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeqList", goodsGroupSeqList);

        // (2) 商品グループ表示情報取得
        // 商品グループ表示Daoの商品グループ表示情報取得処理を実行する。
        // DAO GoodsGroupDisplayDao
        // メソッド 商品グループ表示エンティティ getEntity( （パラメータ）商品グループSEQ)
        List<GoodsGroupDisplayEntity> goodsGroupDisplayEntityList =
                        goodsGroupDisplayDao.getGoodsGroupDisplayList(goodsGroupSeqList);

        // (3) 戻り値
        // 取得した商品グループ表示エンティティを返す。
        return goodsGroupDisplayEntityList;
    }

    /**
     * 商品検索キーワードからエンティティのリストを取得
     *
     * @param shopSeq ショップSEQ
     * @param searchKeyword 商品検索キーワード
     * @return エンティティのリスト
     */
    @Override
    public List<GoodsGroupDisplayEntity> getEntityListByKeyword(Integer shopSeq, String searchKeyword) {
        return goodsGroupDisplayDao.getEntityListByKeyword(shopSeq, searchKeyword);
    }
}