/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.news;

import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;

/**
 * ニュース削除処理
 *
 * @author nose
 *
 */
public interface NewsDeleteLogic {

    /**
     *
     * ニュース削除
     *
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    int execute(NewsEntity newsEntity);
}
