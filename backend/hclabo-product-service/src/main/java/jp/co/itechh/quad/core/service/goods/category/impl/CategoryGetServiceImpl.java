/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionDetailListGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDisplayGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsMapGetLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsSortGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.List;

/**
 * カテゴリ取得
 *
 * @author kimura
 * @version $Revision: 1.1 $
 */
@Service
public class CategoryGetServiceImpl extends AbstractShopService implements CategoryGetService {

    /**
     * カテゴリ情報取得
     */
    private final CategoryGetLogic categoryGetLogic;

    /** カテゴリ表示取得 */
    private final CategoryDisplayGetLogic categoryDisplayGetLogic;

    /** カテゴリ登録商品並び順取得 */
    private final CategoryGoodsSortGetLogic categoryGoodsSortGetLogic;

    /** カテゴリ条件取得 */
    private final CategoryConditionGetLogic categoryConditionGetLogic;

    /** カテゴリ条件詳細リスト取得 */
    private final CategoryConditionDetailListGetLogic categoryConditionDetailListGetLogic;

    /** カテゴリ登録商品マップ取得 */
    private final CategoryGoodsMapGetLogic categoryGoodsGetLogic;

    @Autowired
    public CategoryGetServiceImpl(CategoryGetLogic categoryGetLogic,
                                  CategoryDisplayGetLogic categoryDisplayGetLogic,
                                  CategoryGoodsSortGetLogic categoryGoodsSortGetLogic,
                                  CategoryConditionGetLogic categoryConditionGetLogic,
                                  CategoryConditionDetailListGetLogic categoryConditionDetailListGetLogic,
                                  CategoryGoodsMapGetLogic categoryGoodsGetLogic) {
        this.categoryGetLogic = categoryGetLogic;
        this.categoryDisplayGetLogic = categoryDisplayGetLogic;
        this.categoryGoodsSortGetLogic = categoryGoodsSortGetLogic;
        this.categoryConditionGetLogic = categoryConditionGetLogic;
        this.categoryConditionDetailListGetLogic = categoryConditionDetailListGetLogic;
        this.categoryGoodsGetLogic = categoryGoodsGetLogic;
    }

    /**
     * カテゴリの取得
     *
     * @param categoryId カテゴリId
     * @return カテゴリ情報エンティティ
     */
    @Override
    public CategoryDto execute(String categoryId) {
        return execute(categoryId, null, null);
    }

    /**
     * カテゴリの取得
     *
     * @param categoryId カテゴリId
     * @param openStatus 公開状態
     * @return カテゴリ情報エンティティ
     */
    @Override
    public CategoryDto execute(HTypeOpenStatus openStatus, String categoryId, Timestamp frontDisplayReferenceDate) {
        return execute(categoryId, openStatus, frontDisplayReferenceDate);
    }

    /**
     * カテゴリの取得
     *
     * @param categoryId                カテゴリId
     * @param openStatus                公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return カテゴリ情報エンティティ
     */
    @Override
    public CategoryDto execute(String categoryId, HTypeOpenStatus openStatus, Timestamp frontDisplayReferenceDate) {

        AssertionUtil.assertNotNull("categoryId", categoryId);

        CategoryDto categoryDto = new CategoryDto();

        CategoryEntity categoryEntity =
                        categoryGetLogic.execute(1001, categoryId, openStatus, frontDisplayReferenceDate);

        // CategoryEntityが取得できなければ、このタイミングでnull返却
        // ↑現在存在しないので今回新規追加
        if (ObjectUtils.isEmpty(categoryEntity)) {
            return null;
        } else {
            categoryDto.setCategoryEntity(categoryEntity);
        }

        // カテゴリ表示取得
        CategoryDisplayEntity categoryDisplayEntity = categoryDisplayGetLogic.execute(categoryEntity.getCategorySeq());
        categoryDto.setCategoryDisplayEntity(categoryDisplayEntity);

        // カテゴリ登録商品並び順取得
        CategoryGoodsSortEntity categoryGoodsSortEntity =
                        categoryGoodsSortGetLogic.execute(categoryEntity.getCategoryId());
        categoryDto.setCategoryGoodsSortEntity(categoryGoodsSortEntity);

        // カテゴリー種別が「自動」の場合、以下を追加で検索
        if (HTypeCategoryType.AUTO.equals(categoryEntity.getCategoryType())) {
            //カテゴリ条件取得
            CategoryConditionEntity categoryConditionEntity =
                            categoryConditionGetLogic.execute(categoryEntity.getCategorySeq());
            categoryDto.setCategoryConditionEntity(categoryConditionEntity);

            if (categoryConditionEntity != null) {
                // カテゴリ情報を取得する
                List<CategoryConditionDetailEntity> categoryConditionDetailEntities =
                                categoryConditionDetailListGetLogic.execute(categoryConditionEntity.getCategorySeq());
                categoryDto.setCategoryConditionDetailEntityList(categoryConditionDetailEntities);
            }
        }

        // カテゴリ登録商品クラス
        List<CategoryGoodsEntity> categoryGoodsEntityList =
                        categoryGoodsGetLogic.getCategoryGoodsListByCategorySeq(categoryEntity.getCategorySeq(), null);
        categoryDto.setCategoryGoodsEntityList(categoryGoodsEntityList);

        // フロント表示基準日時が設定されている場合
        // フロント表示状態を判定（公開状態と公開期間から判断）
        Timestamp openStartTime = categoryEntity.getCategoryOpenStartTimePC();
        Timestamp openEndTime = categoryEntity.getCategoryOpenEndTimePC();
        // フロント表示基準日時が未設定の場合、区分値の判定処理で現在日時に変換
        categoryDto.setFrontDisplay(
                        HTypeFrontDisplayStatus.isDisplay(categoryEntity.getCategoryOpenStatusPC(), openStartTime,
                                                          openEndTime, frontDisplayReferenceDate
                                                         ) ?
                                        HTypeFrontDisplayStatus.OPEN :
                                        HTypeFrontDisplayStatus.NO_OPEN);
        // 公開以外の場合はワーニングメッセージを設定
        if (HTypeFrontDisplayStatus.NO_OPEN.equals(categoryDto.getFrontDisplay())) {
            // 公開状態であり期間外の場合
            if (HTypeOpenStatus.OPEN.equals(categoryEntity.getCategoryOpenStatusPC()) && !ObjectUtils.isEmpty(
                            openStartTime) || !ObjectUtils.isEmpty(openEndTime)) {
                categoryDto.setWarningMessage(AppLevelFacesMessageUtil.getAllMessage(MSGCD_OPENSTATUS_OUT_OF_TERM,
                                                                                     new Object[] {ObjectUtils.isEmpty(
                                                                                                     openStartTime) ?
                                                                                                     "" :
                                                                                                     openStartTime,
                                                                                                     ObjectUtils.isEmpty(
                                                                                                                     openEndTime) ?
                                                                                                                     "" :
                                                                                                                     openEndTime}
                                                                                    ).getMessage());
            } else {
                categoryDto.setWarningMessage(
                                AppLevelFacesMessageUtil.getAllMessage(MSGCD_OPENSTATUS_NO_OPEN, null).getMessage());
            }
        }

        return categoryDto;
    }

}