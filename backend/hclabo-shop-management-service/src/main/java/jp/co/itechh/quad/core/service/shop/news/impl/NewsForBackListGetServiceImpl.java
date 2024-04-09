/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.news.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dto.shop.NewsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.core.logic.shop.news.NewsListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.news.NewsForBackListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ニュース管理機能用リスト取得
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Service
public class NewsForBackListGetServiceImpl extends AbstractShopService implements NewsForBackListGetService {

    /** ニュース取得ロジック */
    private final NewsListGetLogic newsListGetLogic;

    @Autowired
    public NewsForBackListGetServiceImpl(NewsListGetLogic newsListGetLogic) {
        this.newsListGetLogic = newsListGetLogic;
    }

    /**
     * 指定条件を満たす情報を取得
     *
     * @param newsSearchForBackDaoConditionDto ニュースDao用検索条件Dto(管理機能用）
     * @return ニュースエンティティのリスト
     */
    @Override
    public List<NewsEntity> execute(NewsSearchForBackDaoConditionDto newsSearchForBackDaoConditionDto) {
        // パラメータチェック
        this.checkParam(newsSearchForBackDaoConditionDto);

        // 共通情報の取得
        Integer shopSeq = 1001;
        // 共通情報チェック
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);
        // パラメータにセット
        newsSearchForBackDaoConditionDto.setShopSeq(shopSeq);

        // 取得処理
        List<NewsEntity> newsList = newsListGetLogic.execute(newsSearchForBackDaoConditionDto);

        return newsList;
    }

    /**
     * パラメータが正常であるかチェック
     *
     * @param newsSearchForBackDaoConditionDto ニュースDao用検索条件Dto(管理機能用）
     */
    protected void checkParam(NewsSearchForBackDaoConditionDto newsSearchForBackDaoConditionDto) {
        ArgumentCheckUtil.assertNotNull("newsSearchForBackDaoConditionDto", newsSearchForBackDaoConditionDto);

    }
}