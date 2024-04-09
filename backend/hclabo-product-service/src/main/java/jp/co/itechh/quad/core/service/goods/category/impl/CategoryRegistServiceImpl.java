/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 20022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionDetailRegistLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionRegistLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDisplayRegistLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsSortRegistLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryRegistService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * カテゴリ登録
 *
 * @author kimura
 * @version $Revision: 1.10 $
 */
@Service
public class CategoryRegistServiceImpl extends AbstractShopService implements CategoryRegistService {

    /** カテゴリ登録 */
    private final CategoryRegistLogic categoryRegistLogic;

    /** カテゴリ表示登録 */
    private final CategoryDisplayRegistLogic categoryDisplayRegistLogic;

    /** カテゴリ登録商品並び順登録 */
    private final CategoryGoodsSortRegistLogic categoryGoodsSortRegistLogic;

    /** カテゴリ条件登録 */
    private final CategoryConditionRegistLogic categoryConditionRegistLogic;

    /** カテゴリ条件詳細登録 */
    private final CategoryConditionDetailRegistLogic categoryConditionDetailRegistLogic;

    @Autowired
    public CategoryRegistServiceImpl(CategoryRegistLogic categoryRegistLogic,
                                     CategoryDisplayRegistLogic categoryDisplayRegistLogic,
                                     CategoryGoodsSortRegistLogic categoryGoodsSortRegistLogic,
                                     CategoryConditionRegistLogic categoryConditionRegistLogic,
                                     CategoryConditionDetailRegistLogic categoryConditionDetailRegistLogic) {
        this.categoryRegistLogic = categoryRegistLogic;
        this.categoryDisplayRegistLogic = categoryDisplayRegistLogic;
        this.categoryGoodsSortRegistLogic = categoryGoodsSortRegistLogic;
        this.categoryConditionRegistLogic = categoryConditionRegistLogic;
        this.categoryConditionDetailRegistLogic = categoryConditionDetailRegistLogic;
    }

    /**
     * カテゴリの登録
     *
     * @param categoryDto      カテゴリ情報DTO
     * @return 件数
     */
    @Override
    public int execute(CategoryDto categoryDto) {

        int size = 0;
        Integer shopSeq = 1001;
        AssertionUtil.assertNotNull("categoryDto", categoryDto);

        CategoryEntity categoryEntity = categoryDto.getCategoryEntity();

        if (categoryEntity == null) {
            throwMessage("SGC001105");
        }

        // カテゴリ情報設定
        categoryDto.getCategoryEntity().setShopSeq(shopSeq);

        size = categoryRegistLogic.execute(categoryDto.getCategoryEntity());

        if (size != 1) {
            return size;
        }

        size = categoryDisplayRegistLogic.execute(categoryDto.getCategoryDisplayEntity());

        if (size != 1) {
            return size;
        }

        size = categoryGoodsSortRegistLogic.execute(categoryDto.getCategoryGoodsSortEntity());

        if (size != 1) {
            return size;
        }

        if (ObjectUtils.isNotEmpty(categoryDto.getCategoryConditionEntity())) {
            size = categoryConditionRegistLogic.execute(categoryDto.getCategoryConditionEntity());

            if (size != 1) {
                return size;
            }
        }

        if (CollectionUtils.isNotEmpty(categoryDto.getCategoryConditionDetailEntityList())) {
            categoryConditionDetailRegistLogic.execute(categoryDto.getCategoryConditionDetailEntityList());
        }

        return size;
    }

}
