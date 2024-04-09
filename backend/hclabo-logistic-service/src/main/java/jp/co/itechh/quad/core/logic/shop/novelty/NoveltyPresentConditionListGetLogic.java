/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.novelty;

import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchResultGoodsDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;

import java.util.List;

/**
 * ノベルティプレゼント条件検索Logic
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
public interface NoveltyPresentConditionListGetLogic {

    /**
     * 検索実行
     *
     * @param conditionDto 検索条件DTO
     * @return 検索結果
     */
    List<NoveltyPresentConditionEntity> execute(NoveltyPresentSearchForDaoConditionDto conditionDto);

    /**
     * 検索実行（商品・在庫情報の取得）
     *
     * @param noveltyPresentConditionSeqList ノベルティプレゼント条件SEQのList
     * @return 検索結果
     */
    List<NoveltyPresentSearchResultGoodsDto> getGoodsList(List<Integer> noveltyPresentConditionSeqList);

    /**
     * 除外条件の取得
     *
     * @param noveltyPresentConditionSeq 除外するノベルティプレゼント条件SEQ
     * @return 検索結果
     */
    List<NoveltyPresentConditionEntity> getExclusionList(Integer noveltyPresentConditionSeq);

    /**
     * 検索条件に一致するレコードを取得する
     *
     * @param conditionDto 検索条件
     * @return 検索結果
     */
    List<NoveltyPresentConditionEntity> getJudgmentListByCondition(NoveltyPresentSearchForDaoConditionDto conditionDto);

}