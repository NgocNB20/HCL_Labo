/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.dao.goods.goods.GoodsStockDisplayDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsStockDisplaySyncLogic;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品在庫表示ロジック
 *
 * @author kimura
 */
@Component
public class GoodsStockDisplaySyncLogicImpl extends AbstractShopLogic implements GoodsStockDisplaySyncLogic {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GoodsStockDisplaySyncLogicImpl.class);

    /** 商品在庫表示Daoクラス */
    private final GoodsStockDisplayDao goodsStockDisplayDao;

    @Autowired
    public GoodsStockDisplaySyncLogicImpl(GoodsStockDisplayDao goodsStockDisplayDao) {
        this.goodsStockDisplayDao = goodsStockDisplayDao;
    }

    /**
     * 商品在庫表示テーブルアップサート（トランザクション指定）
     *
     * @param goodsStockDisplayEntityList 商品在庫表示リスト
     * @return 件数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncUpsertStock(List<GoodsStockDisplayEntity> goodsStockDisplayEntityList) {

        int cnt = 0;

        if (CollectionUtils.isNotEmpty(goodsStockDisplayEntityList)) {
            for (GoodsStockDisplayEntity target : goodsStockDisplayEntityList) {
                try {
                    cnt += this.goodsStockDisplayDao.upsertStock(target);
                } catch (Exception e) {
                    LOGGER.error("商品在庫表示テーブルの登録更新に失敗しました：" + target.getGoodsSeq());
                }
            }
        }

        return cnt;
    }

}
