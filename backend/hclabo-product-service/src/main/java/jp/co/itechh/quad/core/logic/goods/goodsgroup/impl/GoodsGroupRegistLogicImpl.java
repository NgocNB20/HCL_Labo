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
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupRegistLogic;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 商品グループ登録
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class GoodsGroupRegistLogicImpl extends AbstractShopLogic implements GoodsGroupRegistLogic {

    /** 商品グループDAO */
    private final GoodsGroupDao goodsGroupDao;

    @Autowired
    public GoodsGroupRegistLogicImpl(GoodsGroupDao goodsGroupDao) {
        this.goodsGroupDao = goodsGroupDao;
    }

    /**
     *
     * 商品グループ登録
     *
     * @param goodsGroupEntity 商品グループエンティティ
     * @return 登録した件数
     */
    @Override
    public int execute(GoodsGroupEntity goodsGroupEntity) {

        // (1) パラメータチェック
        // 商品グループエンティティが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupEntity", goodsGroupEntity);

        // (2) 登録日・更新日をセット

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 登録・更新日時の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        goodsGroupEntity.setRegistTime(currentTime);
        goodsGroupEntity.setUpdateTime(currentTime);
        if (goodsGroupEntity.getWhatsnewDate() == null) {
            // 現在日時から時分秒以下を除去したもの
            Timestamp currentDateTs = new Timestamp(DateUtils.truncate(currentTime, Calendar.DATE).getTime());
            goodsGroupEntity.setWhatsnewDate(currentDateTs);
        }

        // (3) 商品グループ情報登録
        int ret = goodsGroupDao.insert(goodsGroupEntity);

        // (4) 戻り値
        return ret;
    }
}