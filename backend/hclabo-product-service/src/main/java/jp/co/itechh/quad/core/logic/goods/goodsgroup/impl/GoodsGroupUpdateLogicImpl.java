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
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 商品グループ更新
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class GoodsGroupUpdateLogicImpl extends AbstractShopLogic implements GoodsGroupUpdateLogic {

    /** 商品グループDAO */
    private final GoodsGroupDao goodsGroupDao;

    @Autowired
    public GoodsGroupUpdateLogicImpl(GoodsGroupDao goodsGroupDao) {
        this.goodsGroupDao = goodsGroupDao;
    }

    /**
     *
     * 商品グループ更新
     *
     * @param goodsGroupEntity 商品グループエンティティ
     * @return 更新した件数
     */
    @Override
    public int execute(GoodsGroupEntity goodsGroupEntity) {

        // (1) パラメータチェック
        // 商品グループエンティティが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupEntity", goodsGroupEntity);

        // (2) 更新日をセット

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 登録・更新日時の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        goodsGroupEntity.setUpdateTime(currentTime);

        // (3) 商品グループ情報更新
        int ret = goodsGroupDao.update(goodsGroupEntity);

        // (4) 戻り値
        return ret;
    }
}