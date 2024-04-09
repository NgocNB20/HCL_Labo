/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * カテゴリリスト取得
 *
 * @author MN7017
 * @version $Revision: 1.2 $
 */
@Service
public class CategoryListGetServiceImpl extends AbstractShopService implements CategoryListGetService {

    /**
     * カテゴリ情報リスト取得ロジッククラス
     */
    private final CategoryListGetLogic categoryListGetLogic;

    @Autowired
    public CategoryListGetServiceImpl(CategoryListGetLogic categoryListGetLogic) {
        this.categoryListGetLogic = categoryListGetLogic;
    }

    /**
     * カテゴリDTOのリストを取得する
     *
     * @param categorySearchForDaoConditionDto 検索条件DTO
     * @return カテゴリ情報エンティティリスト
     */
    @Override
    public List<CategoryEntity> execute(CategorySearchForDaoConditionDto categorySearchForDaoConditionDto,
                                        Integer shopSeq,
                                        HTypeSiteType siteType) {

        // <処理内容>
        // (1) パラメータチェック
        // ・カテゴリDao用検索条件DTO ： null の場合、 エラーとして処理を終了する
        ArgumentCheckUtil.assertNotNull("categorySearchForDaoConditionDto", categorySearchForDaoConditionDto);

        // (2) 共通情報設定
        // ‥ショップSEQ = 共通情報.ショップSEQ
        // ‥サイト区分 = 共通情報.サイト区分
        categorySearchForDaoConditionDto.setShopSeq(shopSeq);
        categorySearchForDaoConditionDto.setSiteType(siteType);

        // ・カテゴリ情報リスト取得処理を実行する
        // ※『logic基本設計書（カテゴリ情報リスト取得）.xls』参照
        // Logic CategoryListGetLogic
        // パラメータ ・カテゴリ情報Dao用検索条件Dto
        // (公開状態=null)
        // 戻り値 カテゴリエンティティリスト
        return categoryListGetLogic.execute(categorySearchForDaoConditionDto);
    }

    /**
     * 商品コードの一覧からエンティティを取得
     *
     * @param categoryIdList カテゴリIDのリスト
     * @return エンティティのリスト
     */
    @Override
    public List<CategoryEntity> getEntityListByIdList(List<String> categoryIdList) {
        AssertionUtil.assertNotNull("categoryIdList", categoryIdList);

        return categoryListGetLogic.getEntityListByIdList(1001, categoryIdList);
    }

    /**
     * 商品コードからエンティティのリストを取得する
     *
     * @param goodsGroupCode 商品コード
     * @return エンティティのリスト
     */
    @Override
    public List<CategoryEntity> getEntityListByGoodsGroupCode(String goodsGroupCode) {

        return categoryListGetLogic.getEntityListByGoodsGroupCode(goodsGroupCode);
    }

}