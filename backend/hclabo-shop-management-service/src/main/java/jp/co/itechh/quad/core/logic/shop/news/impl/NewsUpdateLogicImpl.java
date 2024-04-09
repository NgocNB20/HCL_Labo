/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.news.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.shop.news.NewsDao;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.news.NewsUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ニュース更新
 *
 * @author shibuya
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class NewsUpdateLogicImpl extends AbstractShopLogic implements NewsUpdateLogic {

    /** ニュースDao */
    private final NewsDao newsDao;

    @Autowired
    public NewsUpdateLogicImpl(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    /**
     * ニュース更新
     *
     * @param newsEntity ニュースエンティティ
     * @return 更新件数
     */
    @Override
    public int execute(NewsEntity newsEntity) {

        // パラメータチェック
        this.checkParam(newsEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 更新日時に現在日付をセット
        newsEntity.setUpdateTime(dateUtility.getCurrentTime());

        return newsDao.update(newsEntity);
    }

    /**
     * パラメータチェック
     * (更新時の必須項目がnullでないかチェック)
     *
     * @param newsEntity ニュースエンティティ
     */
    protected void checkParam(NewsEntity newsEntity) {

        ArgumentCheckUtil.assertNotNull("newsEntity", newsEntity);
        ArgumentCheckUtil.assertNotNull("newsTime", newsEntity.getNewsTime());
        ArgumentCheckUtil.assertNotNull("newsOpenStatusPC", newsEntity.getNewsOpenStatusPC());
        ArgumentCheckUtil.assertGreaterThanZero("newsSeq", newsEntity.getNewsSeq());
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", newsEntity.getShopSeq());

    }

}