/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.news.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.core.logic.shop.news.NewsGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.news.NewsDetailsGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ニュース詳細情報取得Service<br/>
 *
 * @author ozaki
 * @author Nishigaki Mio (Itec) 2011/10/28 チケット #2638 対応
 *
 */
@Service
public class NewsDetailsGetServiceImpl extends AbstractShopService implements NewsDetailsGetService {

    /** ニュース詳細情報取得Logic */
    private final NewsGetLogic newsGetLogic;

    @Autowired
    public NewsDetailsGetServiceImpl(NewsGetLogic newsGetLogic) {
        this.newsGetLogic = newsGetLogic;
    }

    /**
     *
     * ニュース詳細情報を取得する<br/>
     *
     * @param newsSeq ニュースSEQ
     * @return ニュースエンティティ
     */
    @Override
    public NewsEntity execute(Integer newsSeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("newsSeq", newsSeq);

        // 共通情報の取得
        Integer shopSeq = 1001;

        NewsEntity newsEntity = null;

        // ニュース詳細情報取得処理を実行
        newsEntity = getNews(newsSeq, shopSeq);

        if (newsEntity == null) {
            throwMessage(MSGCD_NEWS_GET_FAIL);
        }

        return newsEntity;
    }

    /**
     * ニュース取得
     *
     * @param newsSeq ニュースSEQ
     * @param shopSeq ショップSEQ
     * @return ニュースエンティティ
     */
    protected NewsEntity getNews(Integer newsSeq, Integer shopSeq) {
        NewsEntity newsEntity = newsGetLogic.execute(shopSeq, newsSeq);
        return newsEntity;
    }

}