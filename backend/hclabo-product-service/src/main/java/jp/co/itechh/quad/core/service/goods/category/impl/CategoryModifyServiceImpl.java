/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.dao.goods.category.CategoryConditionDetailDao;
import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsDao;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionDetailModifyLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionModifyLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDisplayModifyLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsSortModifyLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryModifyLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryModifyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * カテゴリ修正
 *
 * @author kimura
 * @version $Revision: 1.1 $
 */
@Service
public class CategoryModifyServiceImpl extends AbstractShopService implements CategoryModifyService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryModifyServiceImpl.class);

    /**
     * カテゴリ修正
     */
    private final CategoryModifyLogic categoryModifyLogic;

    /**
     * カテゴリ表示修正
     */
    private final CategoryDisplayModifyLogic categoryDisplayModigyLogic;

    /**
     * カテゴリ登録商品並び順修正
     */
    private final CategoryGoodsSortModifyLogic categoryGoodsSortModifyLogic;

    /**
     * カカテゴリ条件修正
     */
    private final CategoryConditionModifyLogic categoryConditionModifyLogic;

    /**
     * カテゴリ条件詳細修正
     */
    private final CategoryConditionDetailModifyLogic categoryConditionDetailModifyLogic;

    /** カテゴリ登録商品DAO */
    private final CategoryGoodsDao categoryGoodsDao;

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** カテゴリ条件詳細Daoクラス */
    CategoryConditionDetailDao categoryConditionDetailDao;

    /**
     * カテゴリApi
     */
    private final CategoryApi categoryApi;

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    @Autowired
    public CategoryModifyServiceImpl(CategoryModifyLogic categoryModifyLogic,
                                     CategoryDisplayModifyLogic categoryDisplayModigyLogic,
                                     CategoryGoodsSortModifyLogic categoryGoodsSortModifyLogic,
                                     CategoryConditionModifyLogic categoryConditionModifyLogic,
                                     CategoryConditionDetailModifyLogic categoryConditionDetailModifyLogic,
                                     CategoryGoodsDao categoryGoodsDao,
                                     CategoryConditionDetailDao categoryConditionDetailDao,
                                     MessagePublisherService messagePublisherService,
                                     CategoryApi categoryApi) {
        this.categoryModifyLogic = categoryModifyLogic;
        this.categoryDisplayModigyLogic = categoryDisplayModigyLogic;
        this.categoryGoodsSortModifyLogic = categoryGoodsSortModifyLogic;
        this.categoryConditionModifyLogic = categoryConditionModifyLogic;
        this.categoryConditionDetailModifyLogic = categoryConditionDetailModifyLogic;
        this.categoryGoodsDao = categoryGoodsDao;
        this.categoryConditionDetailDao = categoryConditionDetailDao;
        this.messagePublisherService = messagePublisherService;
        this.categoryApi = categoryApi;
    }

    /**
     * カテゴリの修正
     *
     * @param originalCategoryDto カテゴリ情報DTO
     * @param modifyCategoryDto   カテゴリ情報DTO
     * @return 件数
     */
    @Override
    public int execute(CategoryDto originalCategoryDto, CategoryDto modifyCategoryDto) {

        int size;
        AssertionUtil.assertNotNull("originalCategoryDto", originalCategoryDto);
        AssertionUtil.assertNotNull("modifyCategoryDto", modifyCategoryDto);

        size = categoryModifyLogic.execute(modifyCategoryDto.getCategoryEntity());

        if (size != 1) {
            return size;
        }

        size = categoryDisplayModigyLogic.execute(modifyCategoryDto.getCategoryDisplayEntity());

        if (size != 1) {
            return size;
        }

        size = categoryGoodsSortModifyLogic.execute(modifyCategoryDto.getCategoryGoodsSortEntity());

        if (size != 1) {
            return size;
        }

        // categoryDto.getCategoryConditionEntity()があり、且つ修正差分があれば
        if (originalCategoryDto.getCategoryConditionEntity() != null
            && !originalCategoryDto.getCategoryConditionEntity()
                                   .equals(modifyCategoryDto.getCategoryConditionEntity())) {
            size = categoryConditionModifyLogic.execute(modifyCategoryDto.getCategoryConditionEntity());

            if (size != 1) {
                return size;
            }
        }

        // categoryDto.getCategoryConditionDetailEntityList()があり、且つ修正差分があれば
        if (originalCategoryDto.getCategoryConditionDetailEntityList() != null) {

            boolean checkChangeCategoryConditionDetailList = false;

            checkChangeCategoryConditionDetailList = checkChangeCategoryConditionDetailEntityList(
                            originalCategoryDto.getCategoryConditionDetailEntityList(),
                            modifyCategoryDto.getCategoryConditionDetailEntityList()
                                                                                                 );

            if (checkChangeCategoryConditionDetailList) {
                categoryConditionDetailDao.deleteByCategorySeq(modifyCategoryDto.getCategoryEntity().getCategorySeq());
                for (CategoryConditionDetailEntity categoryConditionDetailEntity : modifyCategoryDto.getCategoryConditionDetailEntityList()) {
                    size = categoryConditionDetailModifyLogic.execute(categoryConditionDetailEntity);

                    if (size != 1) {
                        return size;
                    }
                }
            }
        }

        // ソート順が手動並び替えの場合のみ、
        if (modifyCategoryDto.getCategoryGoodsSortEntity() != null
            && modifyCategoryDto.getCategoryGoodsEntityList() != null) {
            // リクエストで送られた商品管理の情報をCategoryGoodsに反映
            // カテゴリ種別は自動の場合は、更新のみ行う
            if (HTypeCategoryType.AUTO.equals(modifyCategoryDto.getCategoryEntity().getCategoryType())) {
                for (CategoryGoodsEntity categoryGoodsEntity : modifyCategoryDto.getCategoryGoodsEntityList()) {
                    categoryGoodsDao.update(categoryGoodsEntity);
                }
            }
            // カテゴリ種別は手動の場合は、登録、更新と削除を行う
            else {
                List<Integer> goodsGroupSeqList = new ArrayList<>();
                for (CategoryGoodsEntity categoryGoodsEntity : modifyCategoryDto.getCategoryGoodsEntityList()) {
                    goodsGroupSeqList.add(categoryGoodsEntity.getGoodsGroupSeq());
                    categoryGoodsDao.insertOrUpdate(categoryGoodsEntity);
                }
                List<CategoryGoodsEntity> dbCategoryGoodsEntityList =
                                categoryGoodsDao.getCategoryGoodsListByCategorySeq(
                                                modifyCategoryDto.getCategoryEntity().getCategorySeq());
                // 対象の商品グループSEQ が、カテゴリ登録商品テーブルに存在しない場合、該当レコードを削除
                if (!CollectionUtils.isEmpty(dbCategoryGoodsEntityList)) {
                    dbCategoryGoodsEntityList.forEach(item -> {
                        if (!goodsGroupSeqList.contains(item.getGoodsGroupSeq())) {
                            categoryGoodsDao.delete(item);
                        }
                    });
                }
            }
        }

        return size;
    }

    /**
     * 項目変更チェック
     *
     * @param originalCategoryConditionDetailEntityList          カテゴリ条件詳細エンティティリスト
     * @param modifyCategoryConditionDetailEntityList          カテゴリ条件詳細エンティティリスト
     * @return 変更チェック
     */
    @Override
    public boolean checkChangeCategoryConditionDetailEntityList(List<CategoryConditionDetailEntity> originalCategoryConditionDetailEntityList,
                                                                List<CategoryConditionDetailEntity> modifyCategoryConditionDetailEntityList) {

        // Case CategoryConditionDetailEntityList update
        if (originalCategoryConditionDetailEntityList.size() != modifyCategoryConditionDetailEntityList.size()) {

            return true;
        } else {
            for (int i = 0; i < originalCategoryConditionDetailEntityList.size(); i++) {

                CategoryConditionDetailEntity originalCategoryConditionDetailEntity =
                                originalCategoryConditionDetailEntityList.get(i);
                CategoryConditionDetailEntity modifyCategoryConditionDetailEntity =
                                modifyCategoryConditionDetailEntityList.get(i);

                // Case CategoryConditionDetailEntity not change condition
                if (StringUtils.isNotEmpty(originalCategoryConditionDetailEntity.getConditionColumn())
                    && originalCategoryConditionDetailEntity.getConditionColumn()
                                                            .equals(modifyCategoryConditionDetailEntity.getConditionColumn())
                    && StringUtils.isNotEmpty(originalCategoryConditionDetailEntity.getConditionOperator())
                    && originalCategoryConditionDetailEntity.getConditionOperator()
                                                            .equals(modifyCategoryConditionDetailEntity.getConditionOperator())
                    && StringUtils.isNotEmpty(originalCategoryConditionDetailEntity.getConditionValue())
                    && originalCategoryConditionDetailEntity.getConditionValue()
                                                            .equals(modifyCategoryConditionDetailEntity.getConditionValue())) {

                    continue;
                } else {

                    // Case CategoryConditionDetailEntity change condition
                    return true;
                }
            }
        }
        return false;
    }

}