/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.novelty;

import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentJudgmentDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;

import java.util.List;

/**
 *
 *  ノベルティプレゼント判定用チェックサービス<br/>
 *
 * @author aoyama
 *
 */
public interface NoveltyPresentJudgmentCheckService {

    /**
     * ノベルティプレゼント判定処理<br/>
     *
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @return ノベルティプレゼント条件
     */
    List<NoveltyPresentConditionEntity> execute(NoveltyPresentJudgmentDto judgmentDto);
}
