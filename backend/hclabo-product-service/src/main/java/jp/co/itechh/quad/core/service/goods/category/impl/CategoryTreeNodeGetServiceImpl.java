/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import com.google.common.collect.Maps;
import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryTreeDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;
import jp.co.itechh.quad.core.logic.goods.menu.MenuGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryTreeNodeGetService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * カテゴリ木構造取得
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Service
public class CategoryTreeNodeGetServiceImpl extends AbstractShopService implements CategoryTreeNodeGetService {

    /** メニューLogic */
    private final MenuGetLogic menuGetLogic;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /** DateUtility */
    private final DateUtility dateUtility;

    /** categoryInfoMap */
    private Map<String, CategoryEntity> categoryInfoMap = new HashMap<>();

    @Autowired
    public CategoryTreeNodeGetServiceImpl(MenuGetLogic menuGetLogic,
                                          ConversionUtility conversionUtility,
                                          DateUtility dateUtility) {
        this.menuGetLogic = menuGetLogic;
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * カテゴリー木構造Dtoクラス取得
     *
     * @param conditionDto      検索条件DTO
     * @return カテゴリDTO
     */
    @Override
    public CategoryTreeDto execute(CategorySearchForDaoConditionDto conditionDto) {

        // (1) パラメータチェック
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);

        // メニューデータを取得
        // ※メニューIDは1001固定で良い
        MenuEntity menuEntity = menuGetLogic.execute(1001);

        CategoryTreeDto categoryTreeDto = new CategoryTreeDto();

        if (ObjectUtils.isNotEmpty(menuEntity) && ObjectUtils.isNotEmpty(menuEntity.getCategoryTree())) {

            // Stringレスポンスボディからオブジェクトクラスに変換
            categoryTreeDto = conversionUtility.toObject(menuEntity.getCategoryTree(), CategoryTreeDto.class);

            // 取得対象の指定日時を設定
            Timestamp targetTime = null;
            if (ObjectUtils.isNotEmpty(conditionDto.getFrontDisplayReferenceDate())) {
                targetTime = conditionDto.getFrontDisplayReferenceDate();
            } else {
                targetTime = dateUtility.getCurrentTime();
            }

            List<CategoryEntity> categoryInfoList =
                            menuGetLogic.getCategoryInfo(1001, targetTime, conditionDto.getOpenStatus());
            categoryInfoMap = Maps.uniqueIndex(categoryInfoList, CategoryEntity::getCategoryId);

            // ・メニュー．カテゴリーツリーの要素分、再帰的に処理をする
            recursiveCategoryTree(categoryTreeDto.getCategoryTreeDtoList(), 0, "", conditionDto.getMaxHierarchical());
        }

        return categoryTreeDto;
    }

    /**
     * カテゴリーツリーの要素分、再帰的に処理をする
     *
     * @param categoryTreeDtoList カテゴリDTOリスト
     * @param level               レベル
     * @param hierarchical        表示階層数
     * @param maxHierarchical     最大表示階層数
     */
    private void recursiveCategoryTree(List<CategoryTreeDto> categoryTreeDtoList,
                                       Integer level,
                                       String hierarchical,
                                       Integer maxHierarchical) {

        if (CollectionUtils.isEmpty(categoryTreeDtoList)) {
            return;
        }

        level++;

        for (int index = 0; index < categoryTreeDtoList.size(); index++) {

            CategoryTreeDto categoryTreeDto = categoryTreeDtoList.get(index);

            CategoryEntity categoryEntity = categoryInfoMap.get(categoryTreeDto.getCategoryId());

            if (CollectionUtils.isNotEmpty(categoryTreeDto.getCategoryTreeDtoList())) {
                if (maxHierarchical != null && level > maxHierarchical) {
                    categoryTreeDtoList.remove(index);
                    --index;
                    continue;
                }
                if (ObjectUtils.isEmpty(categoryEntity)) {
                    categoryTreeDtoList.remove(index);
                    --index;
                    continue;
                }
                if (level > 1) {
                    hierarchical += "-";
                }
                hierarchical += categoryTreeDto.getSerialNumber();
                categoryTreeDto.setHierarchicalSerialNumber(hierarchical);
                recursiveCategoryTree(categoryTreeDto.getCategoryTreeDtoList(), level, hierarchical, maxHierarchical);
                if (level > 1) {
                    hierarchical = hierarchical.substring(0, hierarchical.length() - 2);
                } else {
                    hierarchical = hierarchical.substring(0, hierarchical.length() - 1);
                }
            } else if (StringUtils.isNotEmpty(categoryTreeDto.getCategoryId())) {

                if (ObjectUtils.isEmpty(categoryEntity)) {
                    categoryTreeDtoList.remove(index);
                    --index;
                    continue;
                }
                String hierarchicalNode = hierarchical;
                if (level > 1) {
                    hierarchicalNode += "-";
                }
                hierarchicalNode += categoryTreeDto.getSerialNumber();

                categoryTreeDto.setHierarchicalSerialNumber(hierarchicalNode);
                categoryTreeDto.setCategoryId(categoryEntity.getCategoryId());
                categoryTreeDto.setCategoryName(categoryEntity.getCategoryName());
                categoryTreeDto.setCategoryEntity(categoryEntity);

                if (level.equals(maxHierarchical)) {
                    categoryTreeDto.setCategoryTreeDtoList(new ArrayList<>());
                }
            }

            if (index == categoryTreeDtoList.size()) {
                level--;
            }
        }
    }

}