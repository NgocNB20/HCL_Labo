/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * カテゴリ登録商品Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface CategoryGoodsDao {

    /**
     * インサート
     *
     * @param categoryGoodsEntity カテゴリ登録商品エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(CategoryGoodsEntity categoryGoodsEntity);

    /**
     * アップデート
     *
     * @param categoryGoodsEntity カテゴリ登録商品エンティティ
     * @return 処理件数
     */
    @Update
    int update(CategoryGoodsEntity categoryGoodsEntity);

    /**
     * デリート
     *
     * @param categoryGoodsEntity カテゴリ登録商品エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(CategoryGoodsEntity categoryGoodsEntity);

    /**
     * カテゴリSEQをキーに削除（カテゴリ削除時に使用）
     *
     * @param categorySeq カテゴリSEQ
     * @return 処理件数
     */
    @Delete(sqlFile = true)
    int deleteByCategorySeq(Integer categorySeq);

    /**
     * カテゴリ登録商品エンティティリスト取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return カテゴリ登録商品エンティティリスト
     */
    @Select
    List<CategoryGoodsEntity> getCategoryGoodsListByGoodsGroupSeq(Integer goodsGroupSeq);

    /**
     * カテゴリ登録商品エンティティリスト取得
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return カテゴリ登録商品エンティティリスト
     */
    @Select
    List<CategoryGoodsEntity> getCategoryGoodsList(List<Integer> goodsGroupSeqList);

    /**
     * カテゴリに紐づくカテゴリ登録商品を取得
     *
     * @param categorySeq      カテゴリSEQ
     * @param goodsGroupSeq    商品グループSEQ
     * @param fromOrderDisplay From並び順
     * @param toOrderDisplay   To並び順
     * @param orderBy          昇順、降順
     * @return list カテゴリ登録商品リスト
     */
    @Select
    List<CategoryGoodsEntity> getCategoryGoodsOrderList(Integer categorySeq,
                                                        Integer goodsGroupSeq,
                                                        Integer fromOrderDisplay,
                                                        Integer toOrderDisplay,
                                                        boolean orderBy);

    /**
     * カテゴリ登録商品表示順更新
     *
     * @param categorySeq   カテゴリSEQ
     * @param goodsGroupSeq 商品グループSEQ
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateOrderDisplay(Integer categorySeq, Integer goodsGroupSeq);

    /**
     * カテゴリ登録商品テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * カテゴリ登録商品表示順更新
     *
     * @param categorySeq   カテゴリSEQ
     * @param goodsGroupSeq 商品グループSEQ
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int upsertCategoryGoods(Integer categorySeq, Integer goodsGroupSeq);

    /**
     * カテゴリ登録商品クラスのリストを取得する
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリ登録商品クラスのリスト
     */
    @Select
    List<CategoryGoodsEntity> getCategoryGoodsListByCategorySeq(Integer categorySeq);

    /**
     * インサートもしくはアップデート
     *
     * @param categoryGoodsEntity カテゴリ登録商品エンティティ
     * @return 処理件数
     */
    @Insert(sqlFile = true)
    int insertOrUpdate(CategoryGoodsEntity categoryGoodsEntity);
}