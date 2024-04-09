/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.news.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.news.NewsDao;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.news.NewsGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ニュース詳細情報取得Logic<br/>
 *
 * @author ozaki
 * @version $Revision: 1.2 $
 *
 */
@Component
public class NewsGetLogicImpl extends AbstractShopLogic implements NewsGetLogic {

    /** ニュースDAO */
    private final NewsDao newsDao;

    @Autowired
    public NewsGetLogicImpl(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    /**
     *
     * ニュース詳細情報取得<br/>
     *
     * @param shopSeq ショップSEQ
     * @param newsSeq ニュースSEQ
     * @return ニュースエンティティ
     */
    @Override
    public NewsEntity execute(Integer shopSeq, Integer newsSeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("newsSeq", newsSeq);

        // 取得
        return newsDao.getEntityByShopSeq(shopSeq, newsSeq);

    }

}