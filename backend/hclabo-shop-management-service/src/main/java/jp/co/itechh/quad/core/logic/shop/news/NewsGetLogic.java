/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.news;

import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;

/**
 * ニュース詳細情報取得Logic<br/>
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
public interface NewsGetLogic {

    /**
     *
     * ニュース詳細情報取得<br/>
     *
     * @param shopSeq ショップSEQ
     * @param newsSeq ニュースSEQ
     * @return ニュースエンティティ
     */
    NewsEntity execute(Integer shopSeq, Integer newsSeq);

}