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
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDisplayUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 商品グループ表示更新
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class GoodsGroupDisplayUpdateLogicImpl extends AbstractShopLogic implements GoodsGroupDisplayUpdateLogic {

    /** 商品グループ表示DAO */
    private final GoodsGroupDisplayDao goodsGroupDisplayDao;

    @Autowired
    public GoodsGroupDisplayUpdateLogicImpl(GoodsGroupDisplayDao goodsGroupDisplayDao) {
        this.goodsGroupDisplayDao = goodsGroupDisplayDao;
    }

    /**
     *
     * 商品グループ表示更新
     *
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @return 更新した件数
     */
    @Override
    public int execute(GoodsGroupDisplayEntity goodsGroupDisplayEntity) {

        // (1) パラメータチェック
        // 商品グループ表示エンティティが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupDisplayEntity", goodsGroupDisplayEntity);

        // (2) 更新日をセット

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 登録・更新日時の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        goodsGroupDisplayEntity.setUpdateTime(currentTime);

        // (3) 商品グループ表示情報更新
        int ret = goodsGroupDisplayDao.update(goodsGroupDisplayEntity);

        // (4) 戻り値
        return ret;
    }
}