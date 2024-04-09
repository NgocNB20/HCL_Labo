/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.novelty.impl;

import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentConditionDao;
import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentEnclosureGoodsDao;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchResultGoodsDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyPresentConditionListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * ノベルティプレゼント条件検索Logic<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class NoveltyPresentConditionListGetLogicImpl extends AbstractShopLogic
                implements NoveltyPresentConditionListGetLogic {

    /** ノベルティプレゼント条件Dao */
    private final NoveltyPresentConditionDao noveltyPresentConditionDao;

    /** ノベルティプレゼント商品Dao */
    private final NoveltyPresentEnclosureGoodsDao noveltyPresentEnclosureGoodsDao;

    @Autowired
    public NoveltyPresentConditionListGetLogicImpl(NoveltyPresentConditionDao noveltyPresentConditionDao,
                                                   NoveltyPresentEnclosureGoodsDao noveltyPresentEnclosureGoodsDao) {
        this.noveltyPresentConditionDao = noveltyPresentConditionDao;
        this.noveltyPresentEnclosureGoodsDao = noveltyPresentEnclosureGoodsDao;
    }

    /**
     * 検索実行
     *
     * @param conditionDto 検索条件DTO
     * @return 検索結果
     */
    @Override
    public List<NoveltyPresentConditionEntity> execute(NoveltyPresentSearchForDaoConditionDto conditionDto) {

        List<NoveltyPresentConditionEntity> resultList = noveltyPresentConditionDao.getListByCondition(conditionDto,
                                                                                                       conditionDto.getPageInfo()
                                                                                                                   .getSelectOptions()
                                                                                                      );

        return resultList;
    }

    /**
     * 検索実行（商品・在庫情報の取得）
     *
     * @param noveltyPresentConditionSeqList ノベルティプレゼント条件SEQのList
     * @return 検索結果
     */
    @Override
    public List<NoveltyPresentSearchResultGoodsDto> getGoodsList(List<Integer> noveltyPresentConditionSeqList) {

        List<NoveltyPresentSearchResultGoodsDto> resultList =
                        noveltyPresentEnclosureGoodsDao.getGoodsListBySeqList(noveltyPresentConditionSeqList);

        return resultList;
    }

    /**
     * 除外条件の取得
     *
     * @param noveltyPresentConditionSeq 除外するノベルティプレゼント条件SEQ
     * @return 検索結果
     */
    @Override
    public List<NoveltyPresentConditionEntity> getExclusionList(Integer noveltyPresentConditionSeq) {
        List<NoveltyPresentConditionEntity> resultList =
                        noveltyPresentConditionDao.getExclusionListBySeq(noveltyPresentConditionSeq);
        return resultList;
    }

    /**
     * 検索条件に一致するレコードを取得する
     *
     * @param conditionDto 検索条件
     * @return 検索結果
     */
    @Override
    public List<NoveltyPresentConditionEntity> getJudgmentListByCondition(NoveltyPresentSearchForDaoConditionDto conditionDto) {
        return noveltyPresentConditionDao.getJudgmentListByCondition(conditionDto);
    }

}