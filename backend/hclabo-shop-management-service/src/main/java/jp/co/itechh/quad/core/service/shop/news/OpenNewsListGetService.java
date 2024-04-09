/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.news;

import jp.co.itechh.quad.core.dto.shop.NewsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;

import java.util.List;

/**
 * 公開ニュースリスト情報取得Service<br/>
 *
 * @author ozaki
 * @version $Revision: 1.3 $
 *
 */
public interface OpenNewsListGetService {

    /**
     *
     * ニュース詳細情報を取得する<br/>
     *
     * @param conditionDto ニュース情報検索条件DTO
     * @return ニュースエンティティリスト
     */
    List<NewsEntity> execute(NewsSearchForDaoConditionDto conditionDto);
}