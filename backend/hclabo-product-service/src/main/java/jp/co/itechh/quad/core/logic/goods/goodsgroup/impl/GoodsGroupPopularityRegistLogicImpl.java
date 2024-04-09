/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupPopularityDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupPopularityEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupPopularityRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 商品グループ人気登録
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class GoodsGroupPopularityRegistLogicImpl extends AbstractShopLogic implements GoodsGroupPopularityRegistLogic {

    /** 商品グループ人気DAO */
    private final GoodsGroupPopularityDao goodsGroupPopularityDao;

    @Autowired
    public GoodsGroupPopularityRegistLogicImpl(GoodsGroupPopularityDao goodsGroupPopularityDao) {
        this.goodsGroupPopularityDao = goodsGroupPopularityDao;
    }

    /**
     *
     * 商品グループ人気登録
     *
     * @param goodsGroupPopularityEntity 商品グループ人気エンティティ
     * @return 登録した件数
     */
    @Override
    public int execute(GoodsGroupPopularityEntity goodsGroupPopularityEntity) {

        // (1) パラメータチェック
        // 商品グループ人気エンティティが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupPopularityEntity", goodsGroupPopularityEntity);

        // (2) 登録日・更新日をセット

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 登録・更新日時の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        goodsGroupPopularityEntity.setRegistTime(currentTime);
        goodsGroupPopularityEntity.setUpdateTime(currentTime);

        // (3) 商品グループ人気情報登録
        int ret = goodsGroupPopularityDao.insert(goodsGroupPopularityEntity);

        // (4) 戻り値
        return ret;
    }
}