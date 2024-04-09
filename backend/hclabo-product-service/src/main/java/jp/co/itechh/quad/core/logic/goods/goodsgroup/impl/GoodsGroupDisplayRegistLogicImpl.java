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
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDisplayDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDisplayRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 商品グループ表示登録
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
@Component
public class GoodsGroupDisplayRegistLogicImpl extends AbstractShopLogic implements GoodsGroupDisplayRegistLogic {

    /** 商品グループ表示DAO */
    private final GoodsGroupDisplayDao goodsGroupDisplayDao;

    @Autowired
    public GoodsGroupDisplayRegistLogicImpl(GoodsGroupDisplayDao goodsGroupDisplayDao) {
        this.goodsGroupDisplayDao = goodsGroupDisplayDao;
    }

    /**
     *
     * 商品グループ表示登録
     *
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @return 登録した件数
     */
    @Override
    public int execute(GoodsGroupDisplayEntity goodsGroupDisplayEntity) {

        // (1) パラメータチェック
        // 商品グループ表示エンティティが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupDisplayEntity", goodsGroupDisplayEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // (2) 登録日・更新日をセット
        // 登録・更新日時の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        goodsGroupDisplayEntity.setRegistTime(currentTime);
        goodsGroupDisplayEntity.setUpdateTime(currentTime);

        // (3) 商品グループ表示情報登録
        int ret = goodsGroupDisplayDao.insert(goodsGroupDisplayEntity);

        // (4) 戻り値
        return ret;
    }
}