/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGetGoodsListLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGetGoodsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * カテゴリに紐づく商品ｸﾞﾙｰﾌﾟﾘｽﾄを取得
 *
 * @author kimura
 * @version $Revision: 1.4 $
 */
@Service
public class CategoryGetGoodsListServiceImpl extends AbstractShopService implements CategoryGetGoodsListService {

    /**
     * カテゴリ情報取得
     */
    private final CategoryGetGoodsListLogic categoryGetGoodsListLogic;

    @Autowired
    public CategoryGetGoodsListServiceImpl(CategoryGetGoodsListLogic categoryGetGoodsListLogic) {
        this.categoryGetGoodsListLogic = categoryGetGoodsListLogic;
    }

    /**
     * カテゴリに紐づく商品ｸﾞﾙｰﾌﾟﾘｽﾄを取得
     *
     * @param conditionDto カテゴリ商品情報Dao用検索条件Dto
     * @return カテゴリ内商品詳細DTOﾘｽﾄ
     */
    @Override
    public List<CategoryGoodsDetailsDto> execute(CategoryGoodsSearchForDaoConditionDto conditionDto) {
        AssertionUtil.assertNotNull("conditionDto", conditionDto);
        return categoryGetGoodsListLogic.execute(conditionDto);
    }

}
