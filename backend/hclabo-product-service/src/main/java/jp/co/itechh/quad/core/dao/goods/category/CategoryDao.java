/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.category;

import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryExclusiveDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.sql.Timestamp;
import java.util.List;

/**
 * カテゴリDaoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface CategoryDao {

    /**
     * インサート
     *
     * @param categoryEntity カテゴリエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(CategoryEntity categoryEntity);

    /**
     * アップデート
     *
     * @param categoryEntity カテゴリエンティティ
     * @return 処理件数
     */
    @Update
    int update(CategoryEntity categoryEntity);

    /**
     * デリート
     *
     * @param categoryEntity カテゴリエンティティ
     * @return 処理件数
     */
    @Delete
    int delete(CategoryEntity categoryEntity);

    /**
     * カテゴリSEQ採番
     *
     * @return カテゴリSEQ
     */
    @Select
    int getCategorySeqNextVal();

    /**
     * エンティティ取得
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリエンティティ
     */
    @Select
    CategoryEntity getEntity(Integer categorySeq);

    /**
     * エンティティ取得
     *
     * @param shopSeq                   ショップSEQ
     * @param categoryId                カテゴリID
     * @param openStatus                公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return カテゴリエンティティ
     */
    @Select
    CategoryEntity getCategory(Integer shopSeq,
                               String categoryId,
                               HTypeOpenStatus openStatus,
                               Timestamp frontDisplayReferenceDate);

    /**
     * エンティティ取得
     *
     * @param shopSeq                   ショップSEQ
     * @param categoryId                カテゴリID
     * @param openStatus                公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return カテゴリエンティティ
     */
    @Select
    CategoryEntity getCategoryOpen(Integer shopSeq,
                                   String categoryId,
                                   HTypeOpenStatus openStatus,
                                   Timestamp frontDisplayReferenceDate);

    /**
     * カテゴリエンティティリスト取得
     *
     * @param conditionDto カテゴリ検索条件DTO
     * @return カテゴリエンティティリスト
     */
    @Select
    List<CategoryEntity> getCategoryList(CategorySearchForDaoConditionDto conditionDto);

    /**
     * カテゴリ排他取得（カテゴリSEQリスト）
     *
     * @param categorySeqList カテゴリSEQリスト
     * @return カテゴリエンティティリスト
     */
    @Select
    List<CategoryEntity> getCategoryBySeqForUpdate(List<Integer> categorySeqList);

    /**
     * カテゴリエンティティリスト取得（カテゴリSEQ）
     *
     * @param categorySeqList カテゴリSEQリスト
     * @return カテゴリエンティティリスト
     */
    @Select
    List<CategoryEntity> getCategoryListBySeqList(List<Integer> categorySeqList);

    /**
     * カテゴリテーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * カテゴリに紐づく商品ｸﾞﾙｰﾌﾟﾘｽﾄを取得
     *
     * @param conditionDto  カテゴリ情報Dao用検索条件Dto
     * @param selectOptions Doma検索オプション
     * @return 商品ｸﾞﾙｰﾌﾟﾘｽﾄ
     */
    @Select
    List<CategoryGoodsDetailsDto> getCategoryGoodsList(CategoryGoodsSearchForDaoConditionDto conditionDto,
                                                       SelectOptions selectOptions);

    /**
     * カテゴリ詳細DTOリスト取得
     *
     * @param conditionDto カテゴリ検索条件DTO
     * @param selectOptions 検索オプション
     * @return カテゴリ詳細DTOリスト
     */
    @Select
    List<CategoryDetailsDto> getCategoryInfoList(CategorySearchForDaoConditionDto conditionDto,
                                                 SelectOptions selectOptions);

    /**
     * カテゴリ詳細Dtoのリストを取得する
     *
     * @param categoryIdList categoryIdList
     * @param shopSeq        shopSeq
     * @param siteType       siteType
     * @param openStatus     openStatus
     * @return カテゴリ詳細Dtoのリスト
     */
    @Select
    List<CategoryDetailsDto> getCategoryDetailsDtoListByCategoryId(List<String> categoryIdList,
                                                                   int shopSeq,
                                                                   HTypeSiteType siteType,
                                                                   HTypeOpenStatus openStatus);

    /**
     * カテゴリ排他取得
     *
     * @return カテゴリ排他Dtoクラス
     */
    @Select
    CategoryExclusiveDto getExclusiveCategory();

    /**
     * カテゴリIDのリストからエンティティのリストを取得
     *
     * @param shopSeq ショップSEQ
     * @param categoryIdList カテゴリIDのリスト
     * @return エンティティのリスト
     */
    @Select
    List<CategoryEntity> getEntityListByIdList(Integer shopSeq, List<String> categoryIdList);

    /**
     * 商品コードからエンティティのリストを取得する
     *
     * @param goodsGroupCode 商品コード
     * @return エンティティのリスト
     */
    @Select
    List<CategoryEntity> getEntityListByGoodsGroupCode(String goodsGroupCode);

    /**
     * カテゴリSEQのリストを取得する
     *
     * @param categoryType カテゴリ種別
     * @return カテゴリSEQのリスト
     */
    @Select
    List<Integer> getCategorySeqListByCategoryType(HTypeCategoryType categoryType);
}