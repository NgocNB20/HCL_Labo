/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupImageDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品グループ画像取得
 *
 * @author ozaki
 * @version $Revision: 1.2 $
 */
@Component
public class GoodsGroupImageGetLogicImpl extends AbstractShopLogic implements GoodsGroupImageGetLogic {

    /** 商品グループ画像DAO */
    private final GoodsGroupImageDao goodsGroupImageDao;

    @Autowired
    public GoodsGroupImageGetLogicImpl(GoodsGroupImageDao goodsGroupImageDao) {
        this.goodsGroupImageDao = goodsGroupImageDao;
    }

    /**
     *
     * 商品グループ画像取得
     * 商品グループSEQリストを元に商品グループ画像マップを取得する。
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return 商品グループ画像マップ
     *
     */
    @Override
    public Map<Integer, List<GoodsGroupImageEntity>> execute(List<Integer> goodsGroupSeqList) {

        // (1) パラメータチェック
        // 商品グループSEQリストが null または 空 でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeqList", goodsGroupSeqList);

        // (2) 商品グループ画像情報取得
        // 商品グループ画像Daoの商品グループ画像情報取得処理を実行する。
        // DAO GoodsGroupImageDao
        // メソッド List<商品グループ画像エンティティ> getGoodsGroupImageList(
        // （パラメータ）商品グループSEQリスト)
        List<GoodsGroupImageEntity> goodsGroupImageEntityList =
                        goodsGroupImageDao.getGoodsGroupImageList(goodsGroupSeqList);

        // (3) マップオブジェクトの生成
        // 空のマップオブジェクトを作成する。
        // 空の tmp商品グループ画像エンティティリスト を生成する。
        // tmp商品グループSEQ = null;

        // ・(2)で取得した商品グループ画像エンティティリストの件数分、以下の処理を行う
        // ・1件目の場合
        // tmp商品グループ画像エンティティリストに商品グループ画像エンティティを追加
        // tmp商品グループSEQ = 商品グループ画像エンティティ．商品グループSEQ
        // ・2件目以降の場合
        // ・tmp商品グループSEQ と 商品グループ画像エンティティ．商品グループSEQ が一致しない場合
        // tmp商品グループ画像エンティティリストをマップオブジェクトに追加。（キー項目は
        // tmp商品グループ画像エンティティリスト[0]．商品グループSEQ とする）
        // tmp商品グループ画像エンティティリストを初期化
        // tmp商品グループSEQ = 商品グループ画像エンティティ．商品グループSEQ
        // ・2件目以降の共通処理
        // tmp商品グループ画像エンティティリストに商品グループ画像エンティティを追加
        // ・全件終了後、 tmp商品グループ画像エンティティリスト が空でない場合
        // tmp商品グループ画像エンティティリストをマップオブジェクトに追加。（キー項目は
        // tmp商品グループ画像エンティティリスト[0]．商品グループSEQ とする）
        Map<Integer, List<GoodsGroupImageEntity>> goodsGroupImageEntityListMap = new HashMap<>();
        Integer tmpGoodsGroupSeq = null;
        List<GoodsGroupImageEntity> tmpGoodsGroupImageEntityList = new ArrayList<>();
        for (int i = 0; i < goodsGroupImageEntityList.size(); i++) {
            GoodsGroupImageEntity goodsGroupImageEntity = goodsGroupImageEntityList.get(i);
            if (tmpGoodsGroupSeq == null) {
                // 初回
                tmpGoodsGroupSeq = goodsGroupImageEntity.getGoodsGroupSeq();
                tmpGoodsGroupImageEntityList.add(goodsGroupImageEntity);
            } else {
                if (tmpGoodsGroupSeq.intValue() != goodsGroupImageEntity.getGoodsGroupSeq().intValue()) {
                    goodsGroupImageEntityListMap.put(tmpGoodsGroupSeq, tmpGoodsGroupImageEntityList);
                    tmpGoodsGroupSeq = goodsGroupImageEntity.getGoodsGroupSeq();
                    tmpGoodsGroupImageEntityList = new ArrayList<>();
                }
                tmpGoodsGroupImageEntityList.add(goodsGroupImageEntity);
            }
        }

        if (tmpGoodsGroupImageEntityList.size() > 0) {
            goodsGroupImageEntityListMap.put(tmpGoodsGroupSeq, tmpGoodsGroupImageEntityList);
        }

        // (4) 戻り値
        // 生成したマップオブジェクトを返す。
        return goodsGroupImageEntityListMap;
    }
}