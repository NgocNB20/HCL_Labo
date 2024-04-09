/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.news.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.core.logic.shop.news.NewsRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.news.NewsRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ニュース登録
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Service
public class NewsRegistServiceImpl extends AbstractShopService implements NewsRegistService {

    /** ニュース登録Logic */
    private final NewsRegistLogic newsRegistLogic;

    @Autowired
    public NewsRegistServiceImpl(NewsRegistLogic newsRegistLogic) {
        this.newsRegistLogic = newsRegistLogic;
    }

    /**
     * ニュース登録
     *
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    @Override
    public int execute(NewsEntity newsEntity) {

        // パラメータチェック
        this.checkParam(newsEntity);
        // 共通情報チェック
        Integer shopSeq = 1001;
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

        // 共通情報セット
        newsEntity.setShopSeq(shopSeq);

        // 登録
        int result = this.insert(newsEntity);

        return result;
    }

    /**
     * パラメータチェック
     * (登録時の必須項目がnullでないかチェック)
     *
     * @param newsEntity ニュースエンティティ
     */
    protected void checkParam(NewsEntity newsEntity) {

        ArgumentCheckUtil.assertNotNull("newsEntity", newsEntity);
        ArgumentCheckUtil.assertNotNull("newsTime", newsEntity.getNewsTime());
        ArgumentCheckUtil.assertNotNull("newsOpenStatusPC", newsEntity.getNewsOpenStatusPC());

    }

    /**
     * ニュース登録処理
     *
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    protected int insert(NewsEntity newsEntity) {
        int result = newsRegistLogic.execute(newsEntity);
        if (result == 0) {
            throwMessage(MSGCD_NEWS_REGIST_FAIL);
        }

        return result;
    }
}