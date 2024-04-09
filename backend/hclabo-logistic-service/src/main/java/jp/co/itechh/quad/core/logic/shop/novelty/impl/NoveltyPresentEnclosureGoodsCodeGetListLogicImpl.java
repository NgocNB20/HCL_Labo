/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.novelty.impl;

import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentEnclosureGoodsDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyPresentEnclosureGoodsCodeGetListLogic;
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
public class NoveltyPresentEnclosureGoodsCodeGetListLogicImpl extends AbstractShopLogic
                implements NoveltyPresentEnclosureGoodsCodeGetListLogic {

    /** ノベルティプレゼント同梱商品Dao */
    public final NoveltyPresentEnclosureGoodsDao noveltyPresentEnclosureGoodsDao;

    @Autowired
    public NoveltyPresentEnclosureGoodsCodeGetListLogicImpl(NoveltyPresentEnclosureGoodsDao noveltyPresentEnclosureGoodsDao) {
        this.noveltyPresentEnclosureGoodsDao = noveltyPresentEnclosureGoodsDao;
    }

    /**
     * エンティティ取得
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return エンティティ
     */
    @Override
    public List<String> execute(Integer noveltyPresentConditionSeq) {
        return noveltyPresentEnclosureGoodsDao.getGoodsCodeListByConditionSeq(noveltyPresentConditionSeq);
    }

}