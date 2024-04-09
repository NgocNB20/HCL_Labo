/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupLockLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品グループレコードロック
 *
 * @author hirata
 * @version $Revision: 1.3 $
 */
@Component
public class GoodsGroupLockLogicImpl extends AbstractShopLogic implements GoodsGroupLockLogic {

    /** 商品グループDAO */
    private final GoodsGroupDao goodsGroupDao;

    @Autowired
    public GoodsGroupLockLogicImpl(GoodsGroupDao goodsGroupDao) {
        this.goodsGroupDao = goodsGroupDao;
    }

    /**
     * 商品グループレコードロック
     * 商品グループテーブルのレコードを排他取得する。
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @param versionNo 更新カウンタ
     */
    @Override
    public void execute(List<Integer> goodsGroupSeqList, Integer versionNo) {

        // (1) パラメータチェック
        // 商品グループSEQリストが null または 0件 でないかをチェック
        ArgumentCheckUtil.assertNotEmpty("goodsGroupSeqList", goodsGroupSeqList);

        // (2) 商品グループのレコード排他取得
        List<GoodsGroupEntity> goodsGroupEntityList =
                        goodsGroupDao.getGoodsGroupBySeqForUpdate(goodsGroupSeqList, versionNo);
        // 商品グループSEQリストと取得件数が異なる場合
        if (goodsGroupSeqList.size() != goodsGroupEntityList.size()) {
            // 商品グループ排他取得エラーを投げる。
            throwMessage(MSGCD_GOODSGROUP_SELECT_FOR_UPDATE_FAIL);
        }
    }
}