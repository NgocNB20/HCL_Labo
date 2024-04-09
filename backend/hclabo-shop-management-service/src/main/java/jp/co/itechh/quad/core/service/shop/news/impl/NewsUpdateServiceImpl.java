/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.news.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.core.logic.shop.news.NewsUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.news.NewsUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ニュース更新
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
@Service
public class NewsUpdateServiceImpl extends AbstractShopService implements NewsUpdateService {

    /** ニュース更新Logic */
    private final NewsUpdateLogic newsUpdateLogic;

    @Autowired
    public NewsUpdateServiceImpl(NewsUpdateLogic newsUpdateLogic) {
        this.newsUpdateLogic = newsUpdateLogic;
    }

    /**
     * ニュース更新
     *
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    @Override
    public int execute(NewsEntity newsEntity) {

        // パラメータチェック
        this.checkParam(newsEntity);

        // 更新
        int result = this.update(newsEntity);

        return result;
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

    /**
     * ニュース更新処理
     *
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    protected int update(NewsEntity newsEntity) {
        int result = newsUpdateLogic.execute(newsEntity);
        if (result == 0) {
            throwMessage(MSGCD_NEWS_UPDATE_FAIL);
        }

        return result;
    }

}