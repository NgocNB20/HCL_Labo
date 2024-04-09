/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.novelty;

import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;

import java.util.List;

/**
 * ノベルティプレゼント条件エンティティ取得Logic
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
public interface NoveltyPresentConditionGetLogic {

    /**
     * エンティティ取得
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return エンティティ
     */
    NoveltyPresentConditionEntity execute(Integer noveltyPresentConditionSeq);

    /**
     * エンティティ一覧取得
     *
     * @param noveltyPresentConditionSeqList ノベルティプレゼント条件SEQのリスト
     * @return エンティティのリスト
     */
    List<NoveltyPresentConditionEntity> execute(List<Integer> noveltyPresentConditionSeqList);
}