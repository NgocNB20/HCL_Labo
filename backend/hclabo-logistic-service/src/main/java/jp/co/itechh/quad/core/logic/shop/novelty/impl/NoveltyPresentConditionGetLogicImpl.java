/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.novelty.impl;

import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentConditionDao;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyPresentConditionGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * ノベルティプレゼント条件エンティティ取得Logic<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class NoveltyPresentConditionGetLogicImpl extends AbstractShopLogic implements NoveltyPresentConditionGetLogic {

    /** ノベルティプレゼント条件Dao */
    private final NoveltyPresentConditionDao noveltyPresentConditionDao;

    @Autowired
    public NoveltyPresentConditionGetLogicImpl(NoveltyPresentConditionDao noveltyPresentConditionDao) {
        this.noveltyPresentConditionDao = noveltyPresentConditionDao;
    }

    /**
     * エンティティ取得
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return エンティティ
     */
    @Override
    public NoveltyPresentConditionEntity execute(Integer noveltyPresentConditionSeq) {

        return noveltyPresentConditionDao.getEntity(noveltyPresentConditionSeq);
    }

    /**
     * エンティティ一覧取得
     *
     * @param noveltyPresentConditionSeqList ノベルティプレゼント条件SEQのリスト
     * @return エンティティのリスト
     */
    @Override
    public List<NoveltyPresentConditionEntity> execute(List<Integer> noveltyPresentConditionSeqList) {
        return noveltyPresentConditionDao.getListBySeqList(noveltyPresentConditionSeqList);
    }

}