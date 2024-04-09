/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.novelty;

import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchResultDto;

import java.util.List;

/**
 * ノベルティプレゼント条件検索サービス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
public interface NoveltyPresentSearchService {

    /**
     * 検索実行
     *
     * @param conditionDto 検索条件DTO
     * @return 検索結果
     */
    List<NoveltyPresentSearchResultDto> execute(NoveltyPresentSearchForDaoConditionDto conditionDto);

}