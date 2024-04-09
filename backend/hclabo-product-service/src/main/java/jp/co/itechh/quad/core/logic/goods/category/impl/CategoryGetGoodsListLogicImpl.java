/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.category.CategoryDao;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGetGoodsListLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * カテゴリに紐づく商品ｸﾞﾙｰﾌﾟﾘｽﾄを取得
 *
 * @author kimura
 * @version $Revision: 1.4 $
 *
 */
@Component
public class CategoryGetGoodsListLogicImpl extends AbstractShopLogic implements CategoryGetGoodsListLogic {

    /** カテゴリ情報DAO */
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryGetGoodsListLogicImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * カテゴリに紐づく商品ｸﾞﾙｰﾌﾟﾘｽﾄを取得
     * @param conditionDto カテゴリ商品情報Dao用検索条件Dto
     * @return カテゴリ内商品詳細DTOﾘｽﾄ
     */
    @Override
    public List<CategoryGoodsDetailsDto> execute(CategoryGoodsSearchForDaoConditionDto conditionDto) {
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);
        return categoryDao.getCategoryGoodsList(conditionDto, conditionDto.getPageInfo().getSelectOptions());
    }
}