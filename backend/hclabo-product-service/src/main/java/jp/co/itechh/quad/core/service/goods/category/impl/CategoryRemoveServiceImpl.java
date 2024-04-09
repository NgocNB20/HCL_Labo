/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.FileOperationUtility;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryTreeJsonDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDisplayRemoveLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsRemoveLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryModifyLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryRemoveLogic;
import jp.co.itechh.quad.core.logic.goods.menu.MenuGetLogic;
import jp.co.itechh.quad.core.logic.goods.menu.MenuUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryConditionRemoveService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGetService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGoodsSortRemoveService;
import jp.co.itechh.quad.core.service.goods.category.CategoryRemoveService;
import jp.co.itechh.quad.core.utility.CategoryUtility;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * カテゴリ削除
 *
 * @author kimura
 * @author Kaneko (itec) 2012/08/16 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
@Service
public class CategoryRemoveServiceImpl extends AbstractShopService implements CategoryRemoveService {

    /**
     * カテゴリ削除
     */
    private final CategoryRemoveLogic categoryRemoveLogic;

    /**
     * カテゴリ表示削除
     */
    private final CategoryDisplayRemoveLogic categoryDisplayRemoveLogic;

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRemoveServiceImpl.class);

    /** カテゴリ取得 */
    private final CategoryGetService categoryGetService;

    /** カテゴリ登録商品並び順削除 */
    private final CategoryGoodsSortRemoveService categoryGoodsSortRemoveService;

    /** categoryConditionRemoveService */
    private final CategoryConditionRemoveService categoryConditionRemoveService;

    /** カテゴリ登録商品情報削除 */
    private final CategoryGoodsRemoveLogic categoryGoodsRemoveLogic;

    /** メニューLogic */
    private final MenuGetLogic menuGetLogic;

    /** メニュー更新 */
    private final MenuUpdateLogic menuUpdateLogic;

    /** 変換Helper取得 */
    private final ConversionUtility conversionUtility;

    @Autowired
    public CategoryRemoveServiceImpl(CategoryRemoveLogic categoryRemoveLogic,
                                     CategoryDisplayRemoveLogic categoryDisplayRemoveLogic,
                                     CategoryGoodsRemoveLogic categoryGoodsRemoveLogic,
                                     CategoryModifyLogic categoryModifyLogic,
                                     CategoryGetService categoryGetService,
                                     CategoryGoodsSortRemoveService categoryGoodsSortRemoveService,
                                     CategoryConditionRemoveService categoryConditionRemoveService,
                                     MenuGetLogic menuGetLogic,
                                     MenuUpdateLogic menuUpdateLogic,
                                     ConversionUtility conversionUtility) {

        this.categoryGetService = categoryGetService;
        this.categoryRemoveLogic = categoryRemoveLogic;
        this.categoryDisplayRemoveLogic = categoryDisplayRemoveLogic;
        this.categoryGoodsRemoveLogic = categoryGoodsRemoveLogic;
        this.categoryGoodsSortRemoveService = categoryGoodsSortRemoveService;
        this.categoryConditionRemoveService = categoryConditionRemoveService;
        this.menuGetLogic = menuGetLogic;
        this.menuUpdateLogic = menuUpdateLogic;
        this.conversionUtility = conversionUtility;
    }

    /**
     * カテゴリの削除
     *
     * @param categoryId カテゴリID
     * @return 件数
     */
    @Override
    public int execute(String categoryId) {

        // 削除済かチェック
        CategoryDto categoryDto = categoryGetService.execute(categoryId);

        if (categoryDto == null) {
            return 0;
        }

        // 2.メニューのカテゴリーツリーをお掃除
        // 2.1.メニューデータを取得する
        int menuId = 1001;
        MenuEntity menuEntity = menuGetLogic.execute(menuId);
        /*  2.2.↑で取得したカテゴリーツリーの中を再帰的に見ていき、
            今回削除対象のcategoryIdと一致する要素があれば削除する
            ※子要素含めて削除する
        */

        // 削除カテゴリー（再帰）
        Map<String, CategoryDto> removeCategoryDtoMap = new HashMap<>();

        removeCategoryDtoMap.put(categoryId, categoryDto);

        if (menuEntity.getCategoryTree() != null) {
            CategoryTreeJsonDto categoryTreeDto =
                            conversionUtility.toObject(menuEntity.getCategoryTree(), CategoryTreeJsonDto.class);

            if (categoryTreeDto != null) {
                recursiveCategoryTree(categoryTreeDto.getCategoryTreeDtoList(), categoryId);
            }

            menuEntity.setCategoryTree(conversionUtility.toJson(categoryTreeDto));
            menuUpdateLogic.execute(menuEntity);
        }

        List<CategoryDisplayEntity> removeCategoryDisplayList = new ArrayList<>();
        for (String key : removeCategoryDtoMap.keySet()) {
            CategoryDto categoryDtoRemove = removeCategoryDtoMap.get(key);
            removeCategory(categoryDtoRemove);
            removeCategoryDisplayList.add(categoryDtoRemove.getCategoryDisplayEntity());
        }

        // 物理ファイルの削除
        removeFile(removeCategoryDisplayList);

        return removeCategoryDtoMap.size();
    }

    /**
     * カテゴリを削除する
     */
    private void recursiveCategoryTree(List<CategoryTreeJsonDto> categoryTreeDtoList, String categoryId) {

        for (int i = 0; i < categoryTreeDtoList.size(); i++) {
            CategoryTreeJsonDto categoryTreeDto = categoryTreeDtoList.get(i);
            if (categoryId.equals(categoryTreeDto.getCategoryId())) {
                categoryTreeDtoList.remove(categoryTreeDto);
                i--;
            } else if (CollectionUtils.isNotEmpty(categoryTreeDto.getCategoryTreeDtoList())) {
                recursiveCategoryTree(categoryTreeDto.getCategoryTreeDtoList(), categoryId);
            }
        }
    }

    /**
     * カテゴリーテーブル群　削除実行
     */
    protected void removeCategory(CategoryDto categoryDto) {
        if (categoryRemoveLogic.execute(categoryDto.getCategoryEntity()) != 1) {
            throwMessage("SGC000101");
        }
        if (categoryDisplayRemoveLogic.execute(categoryDto.getCategoryDisplayEntity()) != 1) {
            throwMessage("SGC000101");
        }
        if (categoryGoodsSortRemoveService.execute(categoryDto.getCategoryGoodsSortEntity()) != 1) {
            throwMessage("SGC000101");
        }
        if (categoryDto.getCategoryConditionEntity() != null
            && categoryConditionRemoveService.execute(categoryDto.getCategoryConditionEntity()) < 1) {
            throwMessage("SGC000101");
        }

        if (CollectionUtils.isNotEmpty(categoryDto.getCategoryGoodsEntityList())) {
            int categoryGoodsCount = 0;
            for (CategoryGoodsEntity categoryGoodsEntity : categoryDto.getCategoryGoodsEntityList()) {
                categoryGoodsCount += categoryGoodsRemoveLogic.execute(categoryGoodsEntity);
            }
            if (categoryGoodsCount < categoryDto.getCategoryGoodsEntityList().size()) {
                throwMessage("SGC000101");
            }
        }
    }

    /**
     * カテゴリの画像を物理削除
     */
    protected void removeFile(List<CategoryDisplayEntity> removeCategoryDisplayList) {
        // カテゴリUtility取得
        CategoryUtility categoryUtility = ApplicationContextUtility.getBean(CategoryUtility.class);

        String realPath = categoryUtility.getCategoryImageRealPath();
        Iterator<CategoryDisplayEntity> removeCategoryDisplayIterator = removeCategoryDisplayList.iterator();

        // ファイル操作Helper取得
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

        String targetPath = "";
        try {
            while (removeCategoryDisplayIterator.hasNext()) {
                CategoryDisplayEntity categoryDisplayEntity = removeCategoryDisplayIterator.next();
                // PCの画像ファイルがある場合は削除
                if (categoryDisplayEntity.getCategoryImagePC() != null) {
                    targetPath = realPath + "/" + categoryDisplayEntity.getCategoryImagePC();
                    fileOperationUtility.remove(targetPath);
                }
            }
        } catch (IOException e) {
            LOGGER.warn("商品規格画像の削除に失敗しました。(" + targetPath + ")", e);
        }
    }

}