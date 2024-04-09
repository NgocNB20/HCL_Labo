/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.news;

import jp.co.itechh.quad.core.dto.shop.NewsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;

import java.util.List;

/**
 * ニュース管理機能用リスト取得
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface NewsForBackListGetService {

    /**
     * 指定条件を満たす情報を取得
     *
     * @param newsSearchForBackDaoConditionDto ニュースDao用検索条件Dto(管理機能用）
     * @return ニュースエンティティのリスト
     */
    List<NewsEntity> execute(NewsSearchForBackDaoConditionDto newsSearchForBackDaoConditionDto);
}